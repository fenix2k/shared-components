package ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.impl.search.entity;

import lombok.Data;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.impl.search.enums.TestType;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Date;

/**
 * Test.
 */
@Entity
@Data
public class TestEntity {

    @Id
    private Long id;
    private String name;
    private Date date;
    private LocalDate localDate;
    private LocalDateTime localDateTime;
    private OffsetDateTime offsetDateTime;
    private TestType type;
}
