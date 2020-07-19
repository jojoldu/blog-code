package com.jojoldu.blogcode.querydsl.config;

import com.querydsl.core.QueryException;
import com.querydsl.core.types.Path;
import com.querydsl.core.util.ReflectionUtils;
import javax.persistence.Column;
import com.querydsl.sql.ColumnMetadata;
import com.querydsl.sql.RelationalPath;
import com.querydsl.sql.dml.BeanMapper;
import com.querydsl.sql.types.Null;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jojoldu@gmail.com on 19/07/2020
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
public class EntityMapper extends BeanMapper {
    public static final EntityMapper DEFAULT = new EntityMapper(false);

    public static final EntityMapper WITH_NULL_BINDINGS = new EntityMapper(true);

    private final boolean withNullBindings;

    public EntityMapper() {
        this(false);
    }

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
                Column ann = field.getAnnotation(Column.class);
                if (ann != null) {
                    field.setAccessible(true);
                    Object propertyValue = field.get(object);
                    if (propertyValue != null) {
                        if (columnToPath.containsKey(ann.name())) {
                            values.put(columnToPath.get(ann.name()), propertyValue);
                        }
                    } else if (withNullBindings) {
                        values.put(columnToPath.get(ann.name()), Null.DEFAULT);
                    }
                }
            }
            return values;
        } catch (IllegalAccessException e) {
            throw new QueryException(e);
        }

    }
}
