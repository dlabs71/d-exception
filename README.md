# D-exception

[![Maven Central][maven-central-img]][maven-central-url]
[![Javadocs][javadocs-img]][javadocs-url]

Библиотека с готовой базовой иерархией классов исключений для вашего приложения. Дополнительно для spring приложений
предусмотрен готовый Exception Resolver, который вы можете расширять.

# Установка MAVEN

Для подключения данной библиотеки к вашему проекту воспользуйтесь репозиторием maven central.

```xml

<dependency>
    <groupId>ru.dlabs71.library</groupId>
    <artifactId>d-exception</artifactId>
    <version>0.0.1</version>
</dependency>
```

# Использование

## Общая информация

В этой части документа описано базовое использование данной библиотеки. Для более детального описания смотрите раздел "
Документация" или JavaDoc.

Библиотека предоставляет базовый интерфейс `DException` и реализацию для него - `ServiceException`. Данное исключение
предназначено для использования в качестве базового непроверяемого исключения в приложении. Оно содержит ряд удобных
factory методов `build`, для создания экземпляра класса. Используйте его при создании собственных исключений вместо
RuntimeException.

Дополнительно созданы классы-наследники `ServiceException` со своим специфичным поведением и назначением. Они описаны в
списке ниже:

- `BusinessLogicServiceException` - ошибка бизнес-логики приложения. Предназначен для информирования клиента о
  невозможности обработать процесс. Обычно в таких случаях клиент отображает пользователю читаемое сообщение об ошибке.
  Для управления с бэка уровнем сообщения существует параметр конструктора `level`. А также добавлен параметр `data` для
  передачи клиенту дополнительной информации об ошибке.
- `WithoutStacktraceServiceException` - реализация исключения при которой stacktrace не будет отправлен в ответе
  клиенту. Данное поведение гарантирует `AbstractHttpExceptionResolver`.
- `SpecialHttpStatusServiceException` - реализация исключения с указанным http status ответа. Данное поведение
  гарантирует `AbstractHttpExceptionResolver`.

![img](./img/exceptions_hierarchy.png)

`AbstractHttpExceptionResolver` - абстрактный класс, предоставляющий базовые методы обработки ошибок (методы смотрите в
в разделе документация). Для его реализации класса наследующего `AbstractHttpExceptionResolver` понадобиться экземпляр
класса реализующий интерфейс `DExceptionMessageService`. Для удобства в spring приложениях добавлен
класс `SimpleHttpExceptionResolver`.

`DExceptionMessageService` - интерфейс, который необходимо реализовать, чтобы
использовать `AbstractHttpExceptionResolver`. Его задача - получить читаемое сообщение по коду. Пример реализации
показан ниже:

```java

```

# Документация

## Оглавление

* [1. Классы исключений](#section1)
    * [1.1 DException и ServiceException](#section11)
    * [1.2 BusinessLogicServiceException](#section12)
    * [1.3 WithoutStacktraceServiceException](#section13)
    * [1.4 SpecialHttpStatusServiceException](#section14)
* [2. Exception Resolver](#section2)
    * [2.1 AbstractHttpExceptionResolver](#section21)
    * [2.2 SimpleHttpExceptionResolver](#section22)
* [3. Utility классы, enum-ы, DTO](#section3)
    * [3.1 ErrorCode](#section31)
    * [3.2 ErrorLevel](#section32)
    * [3.3 ErrorResponseDto](#section33)
    * [3.4 DExceptionMessageService](#section34)
* [4. Сборка из исходников](#section4)
* [5. Checkstyle](#section5)

## <h2 id="section1">1. Классы исключений</h2>

Иерархия классов исключений:
![img](./img/exceptions_hierarchy.png)

### <h3 id="section11">1.1 DException и ServiceException</h3>

`DException` - Базовый интерфейс всех исключений. Используйте его для создания собственного исключения или используйте
готовую реализацию `ServiceException`. Данное исключение
предназначено для использования в качестве базового непроверяемого исключения в приложении. Оно содержит ряд удобных
factory методов `build`, для создания экземпляра класса. Используйте его при создании собственных исключений вместо
RuntimeException.

Исключение содержит в себе два поля, одно из которых должно быть заполнено:

- `message` - Текстовое описание ошибки. Если в начале сообщения стоит символ $, то AbstractHttpExceptionResolver будет
  пытаться получить сообщение из Resource Bundle, используя в качестве кода строку стоящую позади символа $.
- `errorCode` - Код ошибки. Должен реализовывать интерфейс `ErrorCode`. Обычно является enum (как предоставляемый
  библиотекой `CommonErrorCode`) и содержит код ошибки и код сообщения из Resource Bundle. Если message не был указан
  явно, то при создании `ErrorResponseDto` сообщение будет браться из ErrorCode. В `ErrorResponseDto` предусмотрено
  отдельное поле для кода ошибки, что предоставляет клиенту возможность его использования в условных операторах, чтобы
  более гибко обрабатывать ошибки на запросы.

### <h3 id="section12">1.2 BusinessLogicServiceException</h3>

`BusinessLogicServiceException` - ошибка бизнес-логики приложения. Предназначен для информирования клиента о
невозможности обработать процесс. Обычно в таких случаях клиент отображает пользователю читаемое сообщение об ошибке.
Для управления с бэка уровнем сообщения существует параметр конструктора `level`. А также добавлен параметр `data` для
передачи клиенту дополнительной информации об ошибке.

Содержит следующие поля:

`level` - Предназначен для управления контекстным состоянием оповещения на клиенте (ERROR, WARNING, INFO и т.д.).
`data` - Предназначен для передачи клиенту дополнительной информации об ошибке.

### <h3 id="section13">1.3 WithoutStacktraceServiceException</h3>

`WithoutStacktraceServiceException` - реализация исключения при которой stacktrace не будет отправлен в ответе
клиенту. Данное поведение гарантирует `AbstractHttpExceptionResolver`. Не используйте повсеместно, так как для
отключения отображения stacktrace в ответе существует специальный параметр в `AbstractHttpExceptionResolver` (см.
документацию). Данное исключение нужно использовать только в тех случаях когда гарантировано нужно запретить отображение
stacktrace в `ErrorResponseDto`.

Дополнительных полей класс не содержит.

### <h3 id="section13">1.3 SpecialHttpStatusServiceException</h3>

`SpecialHttpStatusServiceException` - реализация исключения с указанным http status ответа. Данное поведение
гарантирует `AbstractHttpExceptionResolver`.

Содержит следующие поля:

`httpStatus` - http статус ответа

## <h2 id="section2">2. Exception Resolver</h2>

## <h2 id="section4">4. Сборка из исходников</h2>

Для сборки из исходников понадобиться система автосборки Maven 3.9.2 или выше. Используемая версия Java 1.8.
Для сборки с выполнением тестов достаточно выполнить следующую команду из корня проекта:

```shell
mvn clean install
```

Для сборки без выполнения тестов:

```shell
mvn clean install -DskipTests
```

## <h2 id="section5">5. Checkstyle</h2>

В проекте настроен Checkstyle при сборке проекта. Используемая версия checkstyle 9.3. Файлы настроек checkstyle
находятся в директории `checkstyle` в корне проекта. Для проекта был выбран формат Google Checkstyle. Но также были
внесены следующие изменения.

1. Увеличены отступы

```xml

<module name="Indentation">
    <property name="basicOffset" value="4"/>
    <property name="braceAdjustment" value="2"/>
    <property name="caseIndent" value="4"/>
    <property name="throwsIndent" value="4"/>
    <property name="lineWrappingIndentation" value="4"/>
    <property name="arrayInitIndent" value="4"/>
</module>
```

Для детальной информации о настройке maven-checkstyle-plugin смотрите [pom.xml](pom.xml).

Также в директории `checkstyle` добавлена директория `idea`. В ней находиться xml конфигурация Code Style для Intellij
IDEA. Данная конфигурация настроена таким образом, чтобы не противоречить checkstyle.

[maven-central-img]: https://maven-badges.herokuapp.com/maven-central/ru.dlabs71.library/d-exception/badge.svg?gav=true

[maven-central-url]: https://maven-badges.herokuapp.com/maven-central/ru.dlabs71.library/d-exception/?gav=true

[javadocs-img]: https://javadoc.io/badge2/ru.dlabs71.library/d-exception/javadoc.svg

[javadocs-url]: https://javadoc.io/doc/ru.dlabs71.library/d-exception

[license-image]: https://img.shields.io/badge/license-MIT-blue.svg

[license-url]: LICENSE