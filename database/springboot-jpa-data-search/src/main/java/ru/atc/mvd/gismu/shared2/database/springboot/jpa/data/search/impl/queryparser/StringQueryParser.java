package ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.impl.queryparser;

import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.core.SearchCriteria;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.core.api.QueryParamFilter;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.core.api.QueryParser;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.core.enums.CompareOperations;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.core.enums.GroupOperations;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.core.enums.LogicalOperations;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.exception.ExceptionCodes;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.exception.JpaDataSearchException;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.utils.QuotesReplaceUtil;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TODO необходима доработка. Не использовать.
 *
 * Парсит поисковый запрос и возвращает в виде очереди.
 * Вид запроса: [имя_атрибута][операция сравнения]["][значение]["] (можно соединять при помощи AND, OR).
 * Операции смотреть в SearchOperation.
 * Возможен поиск по частичному совпадению значения.
 *
 * @param <D> текстовая строка запроса
 */
public class StringQueryParser<D> implements QueryParser<D> {

    /** Логические операторы. */
    private static final Map<String, LogicalOperations> LOGICAL_OPERATORS = LogicalOperations.getOperations();
    /** Групповые операторы. */
    private static final Map<String, GroupOperations> GROUP_OPERATORS = GroupOperations.getOperations();

    /** Шаблон для проверки и парсинга строки запроса. */
    private final Pattern specCriteriaRegex;

    /** Фильтр поиска. */
    private final QueryParamFilter paramFilter;

    public StringQueryParser() {
        this(null);
    }

    public StringQueryParser(QueryParamFilter paramFilter) {
        this.paramFilter = paramFilter;

        String operations = String.join("|", CompareOperations.getOperations())
                .replaceAll("\\*", "\\\\*");
        String regex = String.format("^(?<key>[\\w\\.]+?)" +
                        "(?<op>%s)" +
                        "((?<value1>[\\w-]+?)" +
                        "|(\\\"(?<value2>.+?)\\\"))$",
                operations);
        specCriteriaRegex = Pattern.compile(regex);
    }

    /**
     * Парсит поисковый запрос и возвращает в виде очереди.
     *
     * @param searchParam поисковый запрос
     * @return очередь
     */
    @Override
    public Deque<?> parse(D searchParam) {

        if (searchParam == null) {
            return new LinkedList<>();
        }

        Deque<Object> output = new LinkedList<>();
        Deque<String> stack = new LinkedList<>();

        splitQueryString(searchParam.toString()).forEach(token -> {
            if (LOGICAL_OPERATORS.containsKey(token)) {
                while (!stack.isEmpty() && LogicalOperations
                        .isHigherPrecedence(LOGICAL_OPERATORS.get(token), LOGICAL_OPERATORS.get(stack.peek()))) {
                    output.push(stack.pop());
                }
                stack.push(token);
            } else if (GROUP_OPERATORS.containsKey(token)) {
                if (GROUP_OPERATORS.get(token) == GroupOperations.LEFT_BRACKET) {
                    stack.push(GroupOperations.LEFT_BRACKET.getToken());
                } else if (GROUP_OPERATORS.get(token) == GroupOperations.RIGHT_BRACKET) {
                    while (!stack.isEmpty()) {
                        String operator = stack.pop();
                        if (!operator.equals(GroupOperations.LEFT_BRACKET.getToken())) {
                            output.push(operator);
                        }
                    }
                }
            } else {
                Matcher matcher = specCriteriaRegex.matcher(token);
                int outputSize = output.size();

                while (matcher.find()) {
                    String key = matcher.group("key");

                    // фильтр по атрибуту поиска
                    if (this.paramFilter != null && !this.paramFilter.doFilter(key)) {
                        throw new JpaDataSearchException(ExceptionCodes.INVALID_SEARCH_QUERY,
                                String.format("поиск по атрибуту [%s] не поддерживается", key));
                    }

                    String operation = matcher.group("op");
                    String value = matcher.group("value1");

                    if (value == null) {
                        value = matcher.group("value2");
                    }

                    CompareOperations compareOperation = CompareOperations.getOperation(operation)
                            .orElseThrow(() -> new JpaDataSearchException(ExceptionCodes.INVALID_ARGUMENT,
                                    String.format("неверный токен операции в строке поиска: [%s]", operation)));

                    output.push(new SearchCriteria(key, compareOperation, QuotesReplaceUtil.replaceSpecialCharacter(value)));
                }

                if (output.size() == outputSize) {
                    throw new JpaDataSearchException(ExceptionCodes.INVALID_SEARCH_QUERY, "неверный синтаксис");
                }
            }
        });

        while (!stack.isEmpty()) {
            output.push(stack.pop());
        }

        if (output.isEmpty()) {
            throw new JpaDataSearchException(ExceptionCodes.INVALID_SEARCH_QUERY, "передан пустой фильтр");
        }

        return output;
    }

    /**
     * Разбивает строку запроса на части.
     *
     * @param queryString {@link String}
     * @return {@link List}<{@link String}>
     */
    private List<String> splitQueryString(String queryString) {
        List<String> tokens = new ArrayList<>();

        boolean isInsideBrackets = false;

        String normalizedQueryString = queryString.replaceAll("[\\[\\]]", "").trim();
        for (int i = 0, k = 0; i < normalizedQueryString.length(); i++) {
            char ch = normalizedQueryString.charAt(i);
            if (ch == ' ' && !isInsideBrackets || i == normalizedQueryString.length() - 1) {
                String token = normalizedQueryString.substring(k, i + 1).trim();
                if (!token.isEmpty()) {
                    tokens.add(token);
                }
                k = i;
            } else if (ch == '"' && i != 0 && normalizedQueryString.charAt(i - 1) != '\\') {
                isInsideBrackets = !isInsideBrackets;
            }
        }

        return tokens;
    }
}
