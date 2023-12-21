package ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.impl.search.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.core.AbstractSearchQuery;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.core.enums.CompareOperations;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.impl.search.enums.TestType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Test.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Test2SearchQueryDto extends AbstractSearchQuery {

    private Long id;
    private String name;
    private Date date;
    private LocalDate localDate;
    private LocalDateTime localDateTime;
    private OffsetDateTime offsetDateTime;
    private TestType type;
    private List<String> listOfString;
    private List<Long> listOfLong;

    @Override
    public Optional<Map<String, String>> validate() {
        return super.validate();
    }

    @Override
    public Optional<Map<String, CompareOperations>> operationMap() {
        return super.operationMap();
    }
}
