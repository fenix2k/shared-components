package ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.core;

import lombok.Getter;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.core.api.QueryParser;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.exception.ExceptionCodes;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.exception.JpaDataSearchException;

/**
 * Параметры билдера спецификаций.
 *
 * @param <E> entity class
 * @param <D> search dto class
 */
@Getter
public class SpecificationBuilderParams<E, D> {

    /** Параметры спецификации. */
    private final SpecificationParams<E> specificationParams;

    /** Парсер запроса. */
    private final QueryParser<D> queryParser;

    /**
     * Конструктор.
     *
     * @param specificationParams параметры спецификации {@link SpecificationParams}
     * @param queryParser парсер запроса {@link QueryParser}
     */
    public SpecificationBuilderParams(SpecificationParams<E> specificationParams,
                                      QueryParser<D> queryParser) {
        if (specificationParams == null) {
            throw new JpaDataSearchException(ExceptionCodes.INVALID_ARGUMENT, "Параметр [specificationParams] не может быть null");
        }
        if (queryParser == null) {
            throw new JpaDataSearchException(ExceptionCodes.INVALID_ARGUMENT, "Параметр [queryParser] не может быть null");
        }
        this.specificationParams = specificationParams;
        this.queryParser = queryParser;
    }

    public SpecificationBuilderParams(Class<E> entityClass, QueryParser<D> queryParser) {
        this(new SpecificationParams<>(entityClass), queryParser);
    }
}
