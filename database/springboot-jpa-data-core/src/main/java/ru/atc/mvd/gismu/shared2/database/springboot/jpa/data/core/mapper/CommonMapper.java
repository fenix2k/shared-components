package ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.core.mapper;

import java.util.List;

/**
 * Общий интерфейс для мапперов.
 *
 * @param <E> сущность
 * @param <D> дто
 */
@SuppressWarnings("unused")
public interface CommonMapper<E, D> {

    /**
     * Маппинг дто -> сущность.
     *
     * @param dto дто {@link D}
     * @return сущность {@link E}
     */
    E toEntity(D dto);

    /**
     * Маппинг сущность -> дто.
     *
     * @param entity сущность {@link E}
     * @return дто {@link D}
     */
    D toDto(E entity);

    /**
     * Маппинг коллекции сущность -> дто.
     *
     * @param entity сущность {@link E}
     * @return дто {@link D}
     */
    List<D> toDto(List<E> entity);

    /**
     * Обновить поля сущности полями дто.
     *
     * @param dto дто {@link D}
     * @param entity сущность {@link E}
     */
    void updateEntityFromDto(D dto, E entity);
}
