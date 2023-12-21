# Общие интерфейсы и абстрактные классы для работы с spring-data-jpa

## Описание

Библиотека предоставляет набор интерфейсов и абстрактных сервисов для организации общего подхода для работы с БД используя JPA

### Подключение
1. Добавить зависимость в pom.xml
```xml
<dependency>
    <groupId>ru.atc.mvd.gismu.shared2</groupId>
    <artifactId>springboot-jpa-data-core</artifactId>
</dependency>
```

2. (опционально) При использовании конвертацию в DTO добавить плагин в pom.xml
```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <configuration>
                <parameters>true</parameters>
                <source>${maven.compiler.source}</source>
                <target>${maven.compiler.target}</target>
                <annotationProcessorPaths>
                    <path>
                        <groupId>org.projectlombok</groupId>
                        <artifactId>lombok</artifactId>
                        <version>${lombok.version}</version>
                    </path>
                    <path>
                        <groupId>org.mapstruct</groupId>
                        <artifactId>mapstruct-processor</artifactId>
                        <version>${mapstruct.version}</version>
                    </path>
                    <path>
                        <groupId>org.hibernate</groupId>
                        <artifactId>hibernate-jpamodelgen</artifactId>
                        <version>${hibernate.version}</version>
                    </path>
                </annotationProcessorPaths>
            </configuration>
        </plugin>
    </plugins>
</build>
```

### Использование

<в разработке....>