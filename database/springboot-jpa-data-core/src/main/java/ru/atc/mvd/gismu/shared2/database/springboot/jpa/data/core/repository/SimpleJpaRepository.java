package ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.core.repository;

import org.springframework.data.repository.NoRepositoryBean;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.core.model.SimpleEntityAttributes;

/**
 * Басовый интерфейс репозитория.
 *
 * @param <T> тип сущности
 * @param <ID> тип идентификатора сущности
 */
@NoRepositoryBean
public interface SimpleJpaRepository<T extends SimpleEntityAttributes, ID>
        extends CommonJpaRepository<T, ID> {
}
