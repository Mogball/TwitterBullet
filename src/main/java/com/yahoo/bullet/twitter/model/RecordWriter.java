package com.yahoo.bullet.twitter.model;

import com.yahoo.bullet.record.BulletRecord;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * This class handles writing objects to a {@code BulletRecord}.
 */
@Slf4j
public class RecordWriter {

    /**
     * Record to which to write.
     */
    private final BulletRecord record;

    /**
     * Create a record writer.
     *
     * @param record record to which to write
     */
    public RecordWriter(@NonNull BulletRecord record) {
        this.record = record;
    }

    /**
     * Find fields marked with {@code RecordEntry},
     * to be written to the record.
     *
     * @param cls class from which to find fields
     * @return a list of fields
     */
    protected static List<Field> getEntryFields(Class<?> cls) {
        Field[] declaredFields = cls.getDeclaredFields();
        List<Field> entryFields = new ArrayList<>(declaredFields.length);
        for (Field declaredField : declaredFields) {
            if (declaredField.isAnnotationPresent(RecordEntry.class)) {
                entryFields.add(declaredField);
            }
        }
        return entryFields;
    }

    /**
     * Shorthand to get the entry name from a field.
     *
     * @param field the field whose record name to get
     * @return name to use in the bullet record
     */
    protected static String getNameOf(Field field) {
        return field.getAnnotation(RecordEntry.class).value();
    }

    /**
     * Write a value of a field from an object to
     * a bullet record based on the field type.
     *
     * @param record the record to which to write
     * @param field  the field to write
     * @param obj    the object whose field to write
     * @param prefix a prefix attached to the field names
     */
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

    /**
     * @param field the field to check
     * @return true if the field type is writable as a long
     */
    protected static boolean isLong(Field field) {
        Class<?> fieldType = field.getType();
        return Long.class.equals(fieldType)
                || long.class.equals(fieldType)
                || Integer.class.equals(fieldType)
                || int.class.equals(fieldType);
    }

    /**
     * @param field the field to check
     * @return true if the field type is writable as a double
     */
    protected static boolean isDouble(Field field) {
        Class<?> fieldType = field.getType();
        return Double.class.equals(fieldType)
                || double.class.equals(fieldType)
                || Float.class.equals(fieldType)
                || float.class.equals(fieldType);
    }

    /**
     * @param field the field to check
     * @return true if the field type is boolean
     */
    protected static boolean isBool(Field field) {
        Class<?> fieldType = field.getType();
        return Boolean.class.equals(fieldType) || boolean.class.equals(fieldType);
    }

    /**
     * @param field the field to check
     * @return true if the field type is String
     */
    protected static boolean isString(Field field) {
        return String.class.equals(field.getType());
    }

    /**
     * Write all the {@code RecordEntry} fields of a provided
     * object to the managed bullet record.
     *
     * @param obj    object whose fields to write
     * @param prefix name prefix on fields
     */
    public void writeFieldsOf(@NonNull Object obj, String prefix) {
        List<Field> fields = getEntryFields(obj.getClass());
        for (Field field : fields) {
            writeValue(record, field, obj, prefix);
        }
    }

}
