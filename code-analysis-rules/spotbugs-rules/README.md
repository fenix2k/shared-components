# Библиотека для статического анализа исходного кода (spotbugs)

## Описание
Определяет правила и исключения для стилистического анализа исходного кода (spotbugs).

## Сборка
```shell
mvn clean install
```

## Подключение к проекту
В pom.xml добавить родителя проекта:
```xml
<properties>
    <spotbugs-rules.version>0.0.1</spotbugs-rules.version>
</properties>
<build>
    <plugins>
        <plugin>
            <groupId>com.github.spotbugs</groupId>
            <artifactId>spotbugs-maven-plugin</artifactId>
            <configuration>
                <excludeFilterFile>/spotbugs/excludes.xml</excludeFilterFile>
                <omitVisitors>UnreadFields</omitVisitors>
            </configuration>
            <dependencies>
                <dependency>
                    <groupId>${shared.group-id.version}</groupId>
                    <artifactId>spotbugs-rules</artifactId>
                    <version>${spotbugs-rules.version}</version>
                </dependency>
            </dependencies>
            <executions>
                <execution>
                    <id>spotbugs-validate-sources</id>
                    <phase>test</phase>
                    <goals>
                        <goal>check</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```