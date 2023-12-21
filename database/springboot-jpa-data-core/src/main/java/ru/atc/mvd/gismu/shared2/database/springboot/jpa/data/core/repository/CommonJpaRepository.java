package ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Общий интерфейс репозитория.
 *
 * @param <T> тип сущности
 * @param <ID> тип идентификатора сущности
 */
@NoRepositoryBean
public interface CommonJpaRepository<T, ID> extends JpaRepository<T, ID> {
}
