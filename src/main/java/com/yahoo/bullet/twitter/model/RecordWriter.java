package com.yahoo.bullet.twitter.model;

import com.yahoo.bullet.record.BulletRecord;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class RecordWriter {

    private final BulletRecord record;

    public RecordWriter(@NonNull BulletRecord record) {
        this.record = record;
    }

    protected static List<Field> getEntryFields(Object obj) {
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        List<Field> entryFields = new ArrayList<>(declaredFields.length);
        for (Field declaredField : declaredFields) {
            if (declaredField.isAnnotationPresent(RecordEntry.class)) {
                entryFields.add(declaredField);
            }
        }
        return entryFields;
    }

    protected static String getNameOf(Field field) {
        return field.getAnnotation(RecordEntry.class).value();
    }

    protected static void writeValue(BulletRecord record, Field field, @NonNull Object obj, String prefix) {
        String name = getNameOf(field);
        if (prefix != null && !prefix.isEmpty()) {
            name = prefix + "_" + name;
        }
        if (record.hasField(name)) {
            log.error("Field {} already exists", name);
            return;
        }
        Object value;
        try {
            field.setAccessible(true);
            value = field.get(obj);
        } catch (IllegalAccessException e) {
            log.error("Failed to access field {}", field.getName(), e);
            return;
        }
        if (isLong(field)) {
            record.setLong(name, value != null ? ((Number) value).longValue() : 0);
        } else if (isString(field)) {
            record.setString(name, value != null ? value.toString() : "");
        } else if (isBool(field)) {
            record.setBoolean(name, value != null && (boolean) value);
        } else if (isDouble(field)) {
            record.setDouble(name, value != null ? ((Number) value).doubleValue() : 0.0);
        } else {
            record.setString(name, value.toString());
        }
    }

    protected static boolean isLong(Field field) {
        Class<?> fieldType = field.getType();
        return Long.class.equals(fieldType)
                || long.class.equals(fieldType)
                || Integer.class.equals(fieldType)
                || int.class.equals(fieldType);
    }

    protected static boolean isDouble(Field field) {
        Class<?> fieldType = field.getType();
        return Double.class.equals(fieldType)
                || double.class.equals(fieldType)
                || Float.class.equals(fieldType)
                || float.class.equals(fieldType);
    }

    protected static boolean isBool(Field field) {
        Class<?> fieldType = field.getType();
        return Boolean.class.equals(fieldType) || boolean.class.equals(fieldType);
    }

    protected static boolean isString(Field field) {
        return String.class.equals(field.getType());
    }

    public void writeFieldsOf(@NonNull Object obj, String prefix) {
        List<Field> fields = getEntryFields(obj);
        for (Field field : fields) {
            writeValue(record, field, obj, prefix);
        }
    }

}
