package ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.core.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 * Базовые общие системные атрибуты.
 */
@MappedSuperclass
@Getter
@Setter
@SuppressWarnings("unused")
public abstract class SimpleEntityAttributes implements CommonEntity {

    /**
     * Дата создания.
     */
    @Column(name = "create_dttm", nullable = false, updatable = false)
    private LocalDateTime createdDttm;

    /**
     * Дата изменения.
     */
    @Column(name = "modify_dttm", nullable = false)
    private LocalDateTime modifyDttm;
}
