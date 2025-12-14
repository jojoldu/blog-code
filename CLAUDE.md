# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Repository Overview

This is a collection of independent sample code projects for blog posts at http://jojoldu.tistory.com/. Each subdirectory is a standalone project with its own build configuration, typically demonstrating a specific concept or technique.

## Architecture

- **No unified build**: There is no root-level build system. Each project is self-contained.
- **Primary stack**: Java/Kotlin with Spring Boot and Gradle (various Spring Boot versions from 1.x to 2.x)
- **Test frameworks**: JUnit 4/5, Spock (Groovy), with Spring Boot Test
- **Common patterns**: JPA/Hibernate, Spring Batch, QueryDSL, REST APIs

## Working with Individual Projects

Each project directory contains its own `gradlew` wrapper. Navigate to the specific project directory first.

```bash
# Build a specific project
cd spring-boot-tips
./gradlew build

# Run tests
./gradlew test

# Run a single test class
./gradlew test --tests "com.example.SomeTest"

# Run a single test method
./gradlew test --tests "com.example.SomeTest.testMethod"
```

## Directory Structure Patterns

- `spring-*`, `springboot-*`: Spring Boot example projects
- `jpa-*`: JPA/Hibernate focused examples
- `database/`: MySQL/database optimization examples
- `aws/`: AWS integration examples
- `1_생각정리`, `2_독서`, etc.: Korean-named directories contain blog post drafts/notes (non-code)
- Each project's README.md contains the related blog post content

## Language Note

Many directory names and documentation are in Korean. Blog posts and code comments may also be in Korean.
