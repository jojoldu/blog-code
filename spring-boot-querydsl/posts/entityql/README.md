# Spring Boot & Gradle에 EntityQL 적용하기


## 설정

```groovy
plugins {
    id 'pl.exsio.querydsl.entityql' version "0.0.12"
}

dependencies {

    ...
    implementation("joda-time:joda-time:2.9.4") // querydsl-sql
    implementation("org.reflections:reflections:0.9.11") // entityql
    api("com.querydsl:querydsl-sql-spring:4.3.1") // querydsl-sql
    api("com.github.eXsio:querydsl-entityql:3.1.0") // entityql

}

def generated='src/main/generated_sql'

def defaultPackage = '패키지명.'
entityql {
    generators = [
            generator = {
                type = 'JPA'
                sourceClasses = [
                        defaultPackage+'billproof.eai.EaiBillProof'
                ]
                destinationPackage = defaultPackage+'sql'
                destinationPath = file(generated).absolutePath
                filenamePattern = 'E%s.java'
            }
    ]
    sourceSets {
        main.java.srcDirs += [ generated ]
    }
    idea.module.generatedSourceDirs += file(generated)

    clean.doLast {
        file(generated).deleteDir()
    }
}

generateModels {
    dependsOn('compileJava')
}
```

```java
@Slf4j
@RequiredArgsConstructor
@Configuration
public class EntityQlConfiguration {

    @Bean
    @Profile("local")
    public SQLTemplates h2SqlTemplates() {
        return new H2Templates();
    }

    @Bean
    @Profile("!local")
    public SQLTemplates mySqlTemplates() {
        return new MySQLTemplates();
    }

    @Bean
    public SQLQueryFactory sqlQueryFactory(DataSource dataSource, SQLTemplates sqlTemplates) {
        com.querydsl.sql.Configuration configuration = new com.querydsl.sql.Configuration(sqlTemplates);
        configuration.setExceptionTranslator(new SpringExceptionTranslator());
        configuration.register(new DateTimeType());
        configuration.register(new LocalDateType());

        return new EntityQlQueryFactory(configuration, dataSource)
                .registerEnumsByName("com.woowabros.settler.domain.billproof.eai")
                .registerEnumsByName("com.woowabros.settler.domain.txitem.type")
                .registerEnumsByName("com.woowabros.settler.domain.salesscale.type")
                .registerEnumsByName("com.woowabros.settler.domain.amount")
                .registerEnumsByName("com.woowabros.settler.constraint");
    }
}
```


```java
public class EntityMapper implements Mapper<Object> {
    public static final EntityMapper DEFAULT = new EntityMapper(false);

    public static final EntityMapper WITH_NULL_BINDINGS = new EntityMapper(true);

    private final boolean withNullBindings;

    public EntityMapper(boolean withNullBindings) {
        this.withNullBindings = withNullBindings;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public Map<Path<?>, Object> createMap(RelationalPath<?> path, Object object) {
        try {
            Map<String, Path<?>> columnToPath = new HashMap<>();
            for (Path<?> column : path.getColumns()) {
                columnToPath.put(ColumnMetadata.getName(column), column);
            }
            Map<Path<?>, Object> values = new HashMap<>();
            for (Field field : ReflectionUtils.getFields(object.getClass())) {
                putByEmbedded(object, columnToPath, values, field);
                putByColumn(object, columnToPath, values, field);
                putByJoinColumn(object, columnToPath, values, field);
            }
            return values;
        } catch (IllegalAccessException e) {
            throw new QueryException(e);
        }
    }

    void putByEmbedded(Object object, Map<String, Path<?>> columnToPath, Map<Path<?>, Object> values, Field field) throws IllegalAccessException {
        Embedded ann = field.getAnnotation(Embedded.class);
        if (ann != null) {
            field.setAccessible(true);
            Object embeddedObject = field.get(object);
            if (embeddedObject != null) {
                for (Field embeddedField : ReflectionUtils.getFields(embeddedObject.getClass())) {
                    putByColumn(embeddedObject, columnToPath, values, embeddedField);
                }
            }
        }
    }

    void putByColumn(Object object, Map<String, Path<?>> columnToPath, Map<Path<?>, Object> values, Field field) throws IllegalAccessException {
        Column ann = field.getAnnotation(Column.class);
        if (ann != null) {
            field.setAccessible(true);
            Object propertyValue = field.get(object);
            String columnName = ann.name();
            if (propertyValue != null) {
                if (columnToPath.containsKey(columnName)) {
                    values.put(columnToPath.get(columnName), propertyValue);
                }
            } else if (withNullBindings) {
                values.put(columnToPath.get(columnName), Null.DEFAULT);
            }
        }
    }

    void putByJoinColumn(Object object, Map<String, Path<?>> columnToPath, Map<Path<?>, Object> values, Field field) throws IllegalAccessException {
        JoinColumn ann = field.getAnnotation(JoinColumn.class);
        if (ann != null) {
            field.setAccessible(true);
            Object joinObject = field.get(object);
            String columnName = ann.name();
            String joinColumnName = ann.referencedColumnName();
            if (joinObject != null) {
                if (columnToPath.containsKey(columnName)) {
                    Object joinColumnValue = getJoinColumnValue(joinObject, joinColumnName);
                    if(joinColumnValue != null) {
                        values.put(columnToPath.get(columnName), joinColumnValue);
                    }
                }
            } else if (withNullBindings) {
                values.put(columnToPath.get(columnName), Null.DEFAULT);
            }
        }
    }

    private Object getJoinColumnValue(Object joinObject, String joinColumnName) throws IllegalAccessException {
        for (Field field : ReflectionUtils.getFields(joinObject.getClass())){
            Column ann = field.getAnnotation(Column.class);
            if(ann != null && ann.name().equals(joinColumnName)) {
                field.setAccessible(true);
                return field.get(joinObject);
            }
        }
        return null;
    }
}
```