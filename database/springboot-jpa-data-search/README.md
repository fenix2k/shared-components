## Компонент для организации гибкого поиска по атрибутам сущности при помощи Jpa Specification (Hibernate ORM)

### Зависимости

1. org.springframework.data.spring-data-jpa
2. org.hibernate.hibernate-core

### Описание

Позволяет динамически строить поисковый запрос по атрибутам сущности (Entity) путем декларативного описания поискового запроса.

На текущий момент есть следующие реализации декларирования:
1. Строковый (не использовать пока!!)
2. Дто

### Возможности

Доступные операторы сравнения:

| Оператор сравнения     | Тип данных |                            Описание |
|------------------------|------------|------------------------------------:|
| EQUALS                 | Все        |                               равно |
| EQUALS_IGNORE_CASE     | String     |            равно без учёта регистра |
| NOT_EQUALS             | все        |                            не равно |
| NOT_EQUALS_IGNORE_CASE | String     |         не равно без учёта регистра |
| GREATER                | Comparable |                              больше |
| GREATER_OR_EQUALS      | Comparable |                    больше или равно |
| LESS                   | Comparable |                              меньше |
| LESS_OR_EQUALS         | Comparable |                    меньше или равно |
| CONTAINS               | String     |                            содержит |
| CONTAINS_IGNORE_CASE   | String     |         содержит без учёта регистра |
| STARTS                 | String     |                        начинается с |
| STARTS_IGNORE_CASE     | String     |     начинается с без учёта регистра |
| ENDS                   | String     |                    заканчивается на |
| ENDS_IGNORE_CASE       | String     | заканчивается на без учёта регистра |
| IN                     | Все        |                           вхождение |
| BETWEEN                | Comparable |                               между |

Доступные логические операторы:

| Оператор сравнения      |    Описание |
|-------------------------|------------:|
| AND                     |           и |
| OR                      |         или |

### Компоненты

| Компонент                                                                                                                                          |                                                                      Описание |
|----------------------------------------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------:|
| [JpaSpecification.java](src/main/java/ru/atc/mvd/gismu/shared2/database/springboot/jpa/data/search/core/JpaSpecification.java)                     |                                  Функциональная обертка над Jpa Specification |
| [JpaSpecificationsBuilder.java](src/main/java/ru/atc/mvd/gismu/shared2/database/springboot/jpa/data/search/core/JpaSpecificationsBuilder.java)     |                                                       Билдер JpaSpecification |
| [QueryParser.java](src/main/java/ru/atc/mvd/gismu/shared2/database/springboot/jpa/data/search/core/api/QueryParser.java)                           |                                          Интерфейс парсера входных параметров |
| [TypeCaster.java](src/main/java/ru/atc/mvd/gismu/shared2/database/springboot/jpa/data/search/core/api/TypeCaster.java)                             |     Интерфейс преобразователя типа входного параметра к типу параметра Entity |
| [TypeCasterProvider.java](src/main/java/ru/atc/mvd/gismu/shared2/database/springboot/jpa/data/search/core/api/TypeCasterProvider.java)             |                                    Провайдер для набора преобразователя типов |
| [PredicateBuilder.java](src/main/java/ru/atc/mvd/gismu/shared2/database/springboot/jpa/data/search/core/api/PredicateBuilder.java)                 |                                                   Интерфейс билдера предиката |
| [SearchQuery.java](src/main/java/ru/atc/mvd/gismu/shared2/database/springboot/jpa/data/search/core/api/SearchQuery.java)                           |                                                  Интерфейс поискового запроса |
| [AbstractSearchQuery.java](src/main/java/ru/atc/mvd/gismu/shared2/database/springboot/jpa/data/search/core/AbstractSearchQuery.java)               |                                 Абстрактная реализация дто поискового запроса |
| [SearchCriteria.java](src/main/java/ru/atc/mvd/gismu/shared2/database/springboot/jpa/data/search/core/SearchCriteria.java)                         |                                                     Условие (критерий) поиска |
| [SpecificationBuilderParams.java](src/main/java/ru/atc/mvd/gismu/shared2/database/springboot/jpa/data/search/core/SpecificationBuilderParams.java) |                                                Параметры билдера спецификации |
| [SpecificationParams.java](src/main/java/ru/atc/mvd/gismu/shared2/database/springboot/jpa/data/search/core/SpecificationParams.java)               |                                                        Параметры спецификации |

### Общие настройки

Создать интерфейс репозитория и унаследоваться от JpaSpecificationExecutor (org.hibernate.hibernate-core.JpaSpecificationExecutor)
```java
public interface PersonRepository extends JpaRepository<PersonEntity, Long>, 
        JpaSpecificationExecutor<PersonEntity> {
}
```

### Настройка поискового запроса через описание ДТО

Принимает поисковый запрос в виде Dto. Поисковые атрибуты соединяются логическим оператором AND.

Пример сущности (Entity) по которой будем выполнять поиск:
```java
@Entity
@Table(name = "person")
@Data
public class PersonEntity {

    @Id
    @Column(name = "person_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "person_seq_gen")
    @SequenceGenerator(name = "person_seq_gen", sequenceName = "person_seq", allocationSize = 1)
    private Long id;

    /** Имя. */
    @Column(name = "firstname")
    private String firstname;

    /** Фамилия. */
    @Column(name = "lastname")
    private String lastname;

    /** Пол. */
    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Genders gender;

    /** Дата рождения. */
    @Column(name = "birthdate_dttm")
    private LocalDateTime birthdate;

    /** Доп. информация. */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_info_id", referencedColumnName = "person_info_id")
    @ToString.Exclude
    private PersonInfoSearchQueryDto personInfo;

    /** Индикатор состояния записи. */
    @Column(name = "action_ind")
    private ActionInd actionInd;
}

@Entity
@Table(name = "person_info")
@Data
public class PersonInfoEntity {

    @Id
    @Column(name = "address_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "address_seq_gen")
    @SequenceGenerator(name = "address_seq_gen", sequenceName = "address_seq", allocationSize = 1)
    private Long id;

    /** Адрес. */
    @Column(name = "address")
    private String address;
}
```

#### Конфигурирования

1. Создать DTO поискового запроса

    ```java
    @Data
    public class PersonSearchQueryDto extends AbstractSearchQuery {
    
        private Long id;
    
        // указываем что поля birthdateBetween нет в сущности и указываем имя реального поля
        // для операции IN в списке должно быть не менее 1 аргумента
        @VirtualField(fieldName = "id")
        private List<Long> idIn;
    
        private String firstname;
    
        private String lastname;
    
        // Enum
        private Genders gender;
    
        // игнорируем поле
        @IgnoreField
        private LocalDateTime birthdate;
    
        // указываем что поля birthdateBetween нет в сущности и указываем имя реального поля
        // для операции BETWEEN в списке должно быть 2 аргумента
        @VirtualField(fieldName = "birthdate")  
        private List<LocalDateTime> birthdateBetween;
        
        @SearchFieldType(type = SearchFieldTypes.ENTITY_OBJECT)
        private PersonInfoSearchQueryDto personInfo;
    
        // получаем только не удалённые записи
        private ActionInd actionInd = ActionInd.D;
    
        // алгоритм валидации поисковых полей
        @Override
        public Optional<Map<String, String>> validate() {
            Map<String, String> errors = new HashMap<>();
            
            if (firstname != null && firstname.length() < 3) {
                errors.put("firstname", "поле должно содержать не менее 3 символов");
            }
            if (lastname != null && lastname.length() < 3) {
                errors.put("lastname", "поле должно содержать не менее 3 символов");
            }
    
            return new ValidationResult(errors);
        }
    
        // если в дто не указано, то применяются ниже указанные операции к полям
        @Override
        public Optional<Map<String, CompareOperations>> operationMap() {
            Map<String, CompareOperations> operationMap = new HashMap<>();
    
            operationMap.put("id", CompareOperations.EQUALS); // необязательно (по-умолчанию оператор EQUALS)
            operationMap.put("idIn", CompareOperations.IN); // применяем операцию IN
            operationMap.put("firstname", CompareOperations.EQUALS_IGNORE_CASE); // применяем операцию EQUALS_IGNORE_CASE
            operationMap.put("lastname", CompareOperations.CONTAINS_IGNORE_CASE); // применяем операцию CONTAINS_IGNORE_CASE
            operationMap.put("gender", CompareOperations.EQUALS); // необязательно (по-умолчанию оператор EQUALS)
            operationMap.put("birthdate", CompareOperations.GREATER_OR_EQUALS); // применяем операцию GREATER_OR_EQUALS
            operationMap.put("birthdateBetween", CompareOperations.BETWEEN); // применяем операцию BETWEEN
            operationMap.put("actionInd", CompareOperations.NOT_EQUALS); // применяем операцию NOT_EQUALS
    
            return Optional.of(operationMap);
        }
    }
    
    @Data
    public class PersonInfoSearchQueryDto extends AbstractSearchQuery {
    
        private String address;
    
        // если в дто не указано, то применяются ниже указанные операции к полям
        @Override
        public Optional<Map<String, CompareOperations>> operationMap() {
            Map<String, CompareOperations> operationMap = new HashMap<>();
    
            operationMap.put("address", CompareOperations.STARTS); // применяем операцию STARTS
            
            return Optional.of(operationMap);
        }
    }
    ```

2. Добавить сервисный метод поиска

    ```java
    @Service
    @Slf4j
    public class PersonServiceImpl implements PersonService {
    
        @Override
        public Page<PersonDto> searchByDto(PersonSearchQuery searchQuery, Pageable pageable) {
            // Валидация входящих параметров
            Optional<Map<String, String>> validate = searchQuery.validate();
            if (validate.isPresent()) {
                throw new JpaDataSearchException(ExceptionCodes.INVALID_SEARCH_QUERY,
                        "Ошибка валидации атрибутов поиска: " + validate.get());
            }
    
            // Создаём экземпляр реализации парсера
            // Опционально можно передать operationMap для получения связанных сущностей одним запросом
            QueryParser<PersonSearchQuery> parser = new DtoQueryParser<>(searchQuery.operationMap().orElse(null));
    
            // Конфигурирование билдера запросов
            SpecificationBuilderParams<PersonEntity, PersonSearchQuery> specBuilderParams = new SpecificationBuilderParams<>(PersonEntity.class, parser);
            // Создание билдера запросов
            JpaSpecificationsBuilder<PersonEntity, PersonSearchQuery> specBuilder = new JpaSpecificationsBuilder<>(specBuilderParams);
            // Передаем searchQuery билдеру для построения спецификации
            Specification<PersonEntity> specification = specBuilder.build(searchQuery);
    
            // Делаем запрос в БД при помощи спецификации и результат мапим в Dto
            return repository.findAll(specification, pageable).stream()
                    .map(this::toDto)
                    .collect(Collectors.toList());
        }
    
    }
    ```

3. Добавить в контроллер метод для поиска

    ```java
    @RestController
    @RequestMapping("person")
    @RequiredArgsConstructor
    public class TestTableController {
    
        private final PersonService personService;
    
        @PostMapping("search")
        public Page<PersonDto> searchByDto(@RequestBody PersonSearchQuery searchQuery,
                                           Pageable pageable) {
            return personService.searchByDto(searchQuery, pageable);
        }
    }
    ```

4. Выполнить HTTP запрос

    В данном примере запрос передаётся методом POST в виде JSON строки.
    
    Все условия будут объеденины оператором AND.
    
    Пример POST запроса:
    ```json
    {
      "searchOperationMap": { 
        "firstname": "NOT_EQUALS",
        "childObject.id": "CONTAINS"
      },
      "id": 123,
      "idIn": [
        12, 32, 123
      ],
      "firstname": "firstname",
      "lastname": "lastname",
      "gender": "MALE",
      "birthdateBetween": [
        "2023-10-16T13:00:00", "2023-10-16T16:59:59"
      ],
      "personInfo": {
        "address": "address"
      }
    }
    ```
    В параметре searchOperationMap указывается операция, которая будет применена в соответствующему атрибуту.

    Параметр searchOperationMap не обязательный.

    Если в searchOperationMap наименование атрибута отсутствует, то будут действовать настройки указанные в методе operationMap() DTO
    
    Если и в DTO не указаны настройки, то будет применена операция EQUALS.


### Аннотации
@SearchFieldType(type = SearchFieldTypes.COMPOSITE_OR_OBJECT) позволяет объединить поля объекта в условие выборки по OR
```java
@Data
public class PersonSearchQuery extends AbstractSearchParam {

    @SearchFieldType(type = SearchFieldTypes.COMPOSITE_OR_OBJECT)
    private ProfileContractorUuids contractorUuids;
}

public class ProfileContractorUuids {

    @VirtualField(field = "profile.profileInfo.physicalPersonUuid")
    private Set<UUID> personUuids;

    @VirtualField(field = "profileInfo.organizationUuid")
    private Set<UUID> organizationUuids;
}
```