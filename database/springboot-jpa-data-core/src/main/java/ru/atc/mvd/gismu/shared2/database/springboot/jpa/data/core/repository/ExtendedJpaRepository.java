package ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.core.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.core.model.ExtendedEntityAttributes;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.core.model.enums.ActionIndex;

import java.util.List;
import java.util.Set;

/**
 * Расширенный интерфейс репозитория.
 *
 * @param <T> тип сущности
 * @param <ID> тип идентификатора сущности
 */
@SuppressWarnings("unused")
@NoRepositoryBean
public interface ExtendedJpaRepository<T extends ExtendedEntityAttributes, ID>
        extends SimpleJpaRepository<T, ID> {

    /**
     * Получить все записи по определенным состоянием.
     *
     * @param actionInds список состояний записи {@link Set}<{@link ActionIndex}>
     * @return список сущностей {@link List}<{@link T}>
     */
    List<T> findAllByActionIndIn(Set<ActionIndex> actionInds);

    /**
     * Получить все записи по определенным состоянием постранично.
     *
     * @param actionInds список состояний записи {@link Set}<{@link ActionIndex}>
     * @param pageable параметры пагинации {@link Pageable}
     * @return постраничный список сущностей {@link Page}<{@link T}>
     */
    Page<T> findAllByActionIndIn(Set<ActionIndex> actionInds, Pageable pageable);

    /**
     * Получить записи по списку ид по определенным состоянием.
     *
     * @param ids идентификаторы {@link Set}<{@link ID}>
     * @param actionInds список состояний записи {@link Set}<{@link ActionIndex}>
     * @return список сущностей {@link List}<{@link T}>
     */
    List<T> findAllByIdInAndActionIndIn(Set<ID> ids, Set<ActionIndex> actionInds);
}
