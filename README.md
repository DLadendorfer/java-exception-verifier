# ExceptionVerifier

A lightweight, fluent, JUnit-friendly library for asserting Java
exceptions with expressive, readable tests.

``` java
ExceptionVerifier.assertThat(() -> service.process(null))
    .throwsExactly(IllegalArgumentException.class)
    .messageContains("must not be null");
```

## ✨ Features

-   Fluent API for exception checking
-   Supports:
    -   presence checks (`throwsAny`, `doesNotThrow`, etc.)
    -   exact type and subtype matching (`throwsExactly`,`throwsSubtypeOf`, etc.)
    -   message equality, substring checks, regex checks (`messageContains`, `messageMatches`, etc.)
    -   cause type and cause-message checks
-   No further dependencies required at test runtime
-   Fully null-safe (JSpecify + `@NullMarked`)

## 📦 Installation

### Gradle (Kotlin DSL)

``` kotlin
dependencies {
    testImplementation("com.aero.testing:exception-verifier:<version>")
}
```

### Maven

``` xml
<dependency>
    <groupId>com.aero.testing</groupId>
    <artifactId>exception-verifier</artifactId>
    <version>VERSION_HERE</version>
    <scope>test</scope>
</dependency>
```

## 🚀 Quick Start

``` java
// assert that division by zero is in fact, not possible
ExceptionVerifier.assertThat(() -> calculator.divide(10, 0))
    .throwsExactly(ArithmeticException.class)
    .withMessage("/ by zero");
```

``` java
// assert that something does not throw
ExceptionVerifier.assertThat(() -> userService.save(user))
    .doesNotThrow();
```

``` java
// assert that any kind of IOexception is thrown
ExceptionVerifier.assertThat(() -> parser.parse(input))
    .throwsSubtypeOf(IOException.class);
```

``` java
// assert that the cause message of the underlying exception cause contains a specific substring
ExceptionVerifier.assertThat(() -> loader.load(path))
    .withCause(FileNotFoundException.class)
    .causeMessageContains("config.yml");
```

## 🧩 How It Works

`ExceptionVerifier.assertThat(runnable)` captures the thrown exception
(if any) and then provides an `ExceptionAssertion` object, which
delegates checks to specialized internal components such as:

-   `ExceptionPresenceDelegate`

## 🔒 Null-Safety

-   `@NullMarked` --- all types are non-null by default\
-   `@Nullable` --- explicitly marking possibly-null values

## 🛠 Requirements

-   Java 21+

## 📄 License

This project is licensed under the MIT License.
