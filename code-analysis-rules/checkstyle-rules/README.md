# Библиотека для статического анализа исходного кода (checkstyle)

## Описание
Определяет правила и исключения для стилистического анализа исходного кода (checkstyle).

## Сборка
```shell
mvn clean install
```

## Подключение к проекту
В pom.xml добавить родителя проекта:
```xml
<properties>
    <checkstyle-rules.version>0.0.1</checkstyle-rules.version>
</properties>
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-checkstyle-plugin</artifactId>
            <configuration>
                <configLocation>/checkstyle/checkstyle.xml</configLocation>
                <propertyExpansion>
                    suppressionFile=/checkstyle/suppressions.xml
                    suppressionXpathFile=/checkstyle/suppressions-xpath.xml
                </propertyExpansion>
                <consoleOutput>true</consoleOutput>
                <failsOnError>true</failsOnError>
                <excludes>**/generated/**/*</excludes>
                <excludes>**/generated-sources/**/*</excludes>
                <includeTestSourceDirectory>true</includeTestSourceDirectory>
            </configuration>
            <dependencies>
                <dependency>
                    <groupId>${shared.group-id.version}</groupId>
                    <artifactId>checkstyle-rules</artifactId>
                    <version>${checkstyle-rules.version}</version>
                </dependency>
                <dependency>
                    <groupId>com.puppycrawl.tools</groupId>
                    <artifactId>checkstyle</artifactId>
                    <version>${checkstyle.version}</version>
                </dependency>
            </dependencies>
            <executions>
                <execution>
                    <id>checkstyle-validate-sources</id>
                    <phase>validate</phase>
                    <goals>
                        <goal>check</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```