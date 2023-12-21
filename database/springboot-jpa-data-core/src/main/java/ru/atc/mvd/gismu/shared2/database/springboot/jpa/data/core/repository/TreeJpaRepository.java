package ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.core.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.core.model.TreeEntityAttributes;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.core.model.enums.ActionIndex;

import java.util.Set;

/**
 * Интерфейс репозитория с поддержкой иерархии.
 *
 * @param <T>  тип сущности
 * @param <ID> тип идентификатора сущности
 */
@NoRepositoryBean
public interface TreeJpaRepository<T extends TreeEntityAttributes<ID>, ID> extends ExtendedJpaRepository<T, ID> {

    /**
     * Получить список по parentId.
     *
     * @param parentId parentId {@link ID}
     * @param actionInd {@link ActionIndex}
     * @param pageable {@link Pageable}
     * @return {@link Page}<{@link T}>
     */
    Page<T> findAllByParentIdAndActionIndIn(ID parentId, Set<ActionIndex> actionInd, Pageable pageable);

    /**
     * Проверить существование записей по parentId и actionInd.
     *
     * @param parentId parentId {@link ID}
     * @param actionInd {@link ActionIndex}
     * @return boolean
     */
    boolean existsByParentIdAndActionIndIn(ID parentId, Set<ActionIndex> actionInd);

    /**
     * Получить кол-во записей по parentCode и actionInd.
     *
     * @param parentId parentId {@link ID}
     * @param actionInd {@link ActionIndex}
     * @return кол-во записей
     */
    int countByParentIdAndActionIndIn(ID parentId, Set<ActionIndex> actionInd);
}
