package ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.core;

import org.springframework.data.jpa.domain.Specification;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.core.enums.LogicalOperations;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.exception.ExceptionCodes;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.exception.JpaDataSearchException;

import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * Specifications Builder.
 *
 * @param <E> entity class
 * @param <D> search dto class
 */
@SuppressWarnings("unused")
public class JpaSpecificationsBuilder<E, D> {

    /** Параметры билдера. */
    private final SpecificationBuilderParams<E, D> params;

    public JpaSpecificationsBuilder(SpecificationBuilderParams<E, D> params) {
        if (params == null) {
            throw new JpaDataSearchException(ExceptionCodes.INVALID_ARGUMENT, "Аргумент [params] не определен");
        }
        this.params = params;
    }

    /**
     * Build Specification.
     *
     * @param searchQuery {@link D}
     * @return {@link Specification}
     */
    public Specification<E> build(D searchQuery) {
        Deque<?> searchQueryStack = params.getQueryParser().parse(searchQuery);

        return buildSpecification(searchQueryStack);
    }

    /**
     * Build Specification.
     *
     * @param searchQueryStack {@link Deque}
     * @return {@link Specification}
     */
    private Specification<E> buildSpecification(Deque<?> searchQueryStack) {
        if (searchQueryStack == null || searchQueryStack.isEmpty()) {
            return null;
        }

        Deque<Specification<E>> specStack = new LinkedList<>();

        Collections.reverse((List<?>) searchQueryStack);

        while (!searchQueryStack.isEmpty()) {
            Object command = searchQueryStack.pop();

            if (command instanceof SearchCriteria) {
                specStack.push(new JpaSpecification<>((SearchCriteria) command, params.getSpecificationParams()));
            } else if (command instanceof Deque) {
                Specification<E> spec = buildSpecification((Deque<?>) command);
                if (spec != null) {
                    specStack.push(spec);
                }
            } else if (command instanceof LogicalOperations) {
                LogicalOperations logicalOperation = (LogicalOperations) command;

                if (specStack.size() < logicalOperation.getNumOfArgs()) {
                    throw new JpaDataSearchException(ExceptionCodes.INVALID_SEARCH_QUERY,
                            String.format("логический оператор [%s] должен иметь кол-во операндов: [%s] ",
                                    logicalOperation.name(), logicalOperation.getNumOfArgs()));
                }

                Specification<E> operand1 = specStack.pop();
                Specification<E> operand2 = specStack.pop();

                switch (logicalOperation) {
                    case AND:
                        specStack.push(Specification
                                .where(operand1)
                                .and(operand2));
                        break;
                    case OR:
                        specStack.push(Specification
                                .where(operand1)
                                .or(operand2));
                        break;
                    default:
                        throw new JpaDataSearchException(ExceptionCodes.INVALID_ARGUMENT,
                                String.format("Логическая операция [%s] не поддерживается", logicalOperation));
                }
            }
        }

        return specStack.pop();
    }
}
