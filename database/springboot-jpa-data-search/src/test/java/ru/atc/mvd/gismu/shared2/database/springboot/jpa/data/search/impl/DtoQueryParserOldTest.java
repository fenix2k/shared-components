package ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.impl;

import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.domain.Specification;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.core.JpaSpecificationsBuilder;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.core.SpecificationBuilderParams;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.core.api.QueryParser;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.impl.queryparser.DtoQueryParser;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.impl.search.dto.Test2SearchQueryDto;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.impl.search.dto.TestSearchQueryDto;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.impl.search.entity.TestEntity;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.impl.search.enums.TestType;

import java.util.ArrayList;

class DtoQueryParserOldTest {

    @Test
    void test() {
        Test2SearchQueryDto test2SearchQueryDto = new Test2SearchQueryDto();
        test2SearchQueryDto.setId(3231L);
        test2SearchQueryDto.setName("name1111");
        test2SearchQueryDto.setType(TestType.TYEP3);

        Test2SearchQueryDto test2SearchQueryDto1 = new Test2SearchQueryDto();
        test2SearchQueryDto.setId(32331L);
        test2SearchQueryDto.setName("name11112");
        test2SearchQueryDto.setType(TestType.TYEP3);

        ArrayList<Long> listOfLong = new ArrayList<>();
        listOfLong.add(212L);
        listOfLong.add(554L);
        listOfLong.add(454L);

        ArrayList<String> listOfString = new ArrayList<>();
        listOfString.add("eggrfdsf f2");
        listOfString.add("fdvadggd 321 fdsf");
        listOfString.add("eeFSF324t42 jkgk yhf");

        TestSearchQueryDto searchQuery = new TestSearchQueryDto();
//        searchQuery.setId(123454321L);
        searchQuery.setName("name1213 \\cddsa '' dsa321 dv!c dsad ");
//        searchQuery.setType(TestType.TYPE1);
//        searchQuery.setDate(new Date());
//        searchQuery.setLocalDate(LocalDate.now());
//        searchQuery.setLocalDateTime(LocalDateTime.now());
//        searchQuery.setOffsetDateTime(OffsetDateTime.now());
        searchQuery.setListOfLong(listOfLong);
//        searchQuery.setListOfString(listOfString);
        //searchQuery.setTest2SearchQueryDto(test2SearchQueryDto);
        //searchQuery.setTest2SearchQueryDtos(Arrays.asList(test2SearchQueryDto, test2SearchQueryDto1));

        if (searchQuery.validate().isPresent()) {
            throw new RuntimeException("Ошибка валидации атрибутов поиска: " + searchQuery.validate().get());
        }

        QueryParser<TestSearchQueryDto> parser2 = new DtoQueryParser<>(searchQuery.operationMap().orElse(null));

        SpecificationBuilderParams<TestEntity, TestSearchQueryDto> specBuilderParams =
                new SpecificationBuilderParams<>(TestEntity.class, parser2);
        JpaSpecificationsBuilder<TestEntity, TestSearchQueryDto> specBuilder2 = new JpaSpecificationsBuilder<>(specBuilderParams);
        Specification<TestEntity> spec2 = specBuilder2.build(searchQuery);

        System.out.println("11");
        //Assertions.assertEquals(spec, spec2);
    }
}