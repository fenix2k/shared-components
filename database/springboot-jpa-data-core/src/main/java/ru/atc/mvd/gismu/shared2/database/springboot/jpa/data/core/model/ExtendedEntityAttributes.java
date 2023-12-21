package ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.core.model;

import lombok.Getter;
import lombok.Setter;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.core.model.enums.ActionIndex;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

/**
 * Расширенные общие системные атрибуты.
 */
@MappedSuperclass
@Getter
@Setter
public abstract class ExtendedEntityAttributes extends SimpleEntityAttributes {

    /**
     * Индикатор изменения записи.
     */
    @Column(name = "action_ind", nullable = false)
    @Enumerated(EnumType.STRING)
    private ActionIndex actionInd = ActionIndex.I;
}