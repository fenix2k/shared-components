package ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.core.service.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.core.mapper.CommonMapper;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.core.model.CommonEntity;

import java.util.Optional;

/**
 * Интерфейс базового CRUD сервиса с mapstruct для конвертации в DTO.
 *
 * @param <E> тип сущности
 * @param <D> тип dto сущности
 * @param <ID> тип идентификатора сущности
 */
@SuppressWarnings("unused")
public interface BaseJpaService<E extends CommonEntity, D, ID>
        extends CoreJpaService<E, ID> {

    /**
     * Получить маппер сущность - дто.
     *
     * @return {@link CommonMapper}<{@link E}, {@link D}>
     */
    CommonMapper<E, D> getMapper();

    /**
     * Получить список dto сущностей постранично.
     *
     * @param pageable параметры пагинации {@link Pageable}
     * @return список dto сущностей постранично {@link Page}<{@link D}>
     */
    Page<D> findAll(Pageable pageable);

    /**
     * Получить dto сущности по идентификатору.
     *
     * @param id идентификатор {@link ID}
     * @return dto сущности {@link Optional}<{@link D}>
     */
    Optional<D> findById(ID id);

    /**
     * Получить dto сущности по идентификатору.
     *
     * @param id идентификатор {@link ID}
     * @return dto сущности {@link Optional}<{@link D}>
     */
    D getById(ID id);

    /**
     * Создать новую сущность по дто (операция CREATE).
     *
     * @param dto дто сущности {@link D}
     * @return дто сохраненной сущности {@link D}
     */
    D create(D dto);

    /**
     * Обновить сущность (операция UPDATE).
     *
     * @param id идентификатор {@link ID}
     * @param dto дто сущности {@link D}
     * @return обновлённое дто сущности {@link D}
     */
    D update(ID id, D dto);
}
