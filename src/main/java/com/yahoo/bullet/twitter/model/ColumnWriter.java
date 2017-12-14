package com.yahoo.bullet.twitter.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.yahoo.bullet.twitter.model.RecordWriter.getEntryFields;
import static com.yahoo.bullet.twitter.model.RecordWriter.isDouble;
import static com.yahoo.bullet.twitter.model.RecordWriter.isLong;
import static com.yahoo.bullet.twitter.model.RecordWriter.isString;

/**
 * Class used to write column values of models
 * to a {@code columns.json} to represent the schema.
 */
public class ColumnWriter {

    protected static final String COL_STRING_TYPE = "STRING";
    protected static final String COL_LONG_TYPE = "LONG";
    protected static final String COL_DOUBLE_TYPE = "DOUBLE";
    protected static final String COL_BOOLEAN_TYPE = "BOOLEAN";

    protected static final String COL_NAME = "name";
    protected static final String COL_TYPE = "type";
    protected static final String COL_DESC = "description";

    /**
     * Get the name of the field type to write.
     *
     * @param field the field whose type to get
     * @return String representation of the field type
     */
    protected static String getTypeName(Field field) {
        if (isString(field)) {
            return COL_STRING_TYPE;
        } else if (isLong(field)) {
            return COL_LONG_TYPE;
        } else if (isDouble(field)) {
            return COL_DOUBLE_TYPE;
        } else {
            return COL_BOOLEAN_TYPE;
        }
    }

    /**
     * JSON array of the columns to write.
     */
    protected final JsonArray columns;

    /**
     * Create an empty column writer.
     */
    public ColumnWriter() {
        this.columns = new JsonArray();
    }

    /**
     * Write a column name, type, and description to an
     * array of columns.
     *
     * @param columns existing array of columns
     * @param field   the class field representing the column
     * @param prefix  the name prefix to user
     */
    protected static void writeColumn(JsonArray columns, Field field, String prefix) {
        JsonObject column = new JsonObject();
        RecordEntry entry = field.getAnnotation(RecordEntry.class);
        String name = entry.value();
        if (prefix != null && !prefix.isEmpty()) {
            name = prefix + "_" + name;
        }
        column.addProperty(COL_NAME, name);
        column.addProperty(COL_TYPE, getTypeName(field));
        column.addProperty(COL_DESC, entry.desc());
        columns.add(column);
    }

    /**
     * Write all the columns for the provided class.
     *
     * @param cls    class whose columns to write
     * @param prefix name prefix to use
     */
    public void writeColumnsFor(Class<?> cls, String prefix) {
        List<Field> fields = getEntryFields(cls);
        for (Field field : fields) {
            writeColumn(columns, field, prefix);
        }
    }

    /**
     * Write the JSON array columns to an output stream.
     *
     * @param os     output stream to which to write the columns
     * @param pretty whether the columns JSON should be formatted
     * @throws IOException if an error occurs during writing
     */
    public void writeColumnsTo(OutputStream os, boolean pretty) throws IOException {
        GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
        if (pretty) {
            builder.setPrettyPrinting();
        }
        Gson gson = builder.create();
        String columnsJson = gson.toJson(columns);
        os.write(columnsJson.getBytes(StandardCharsets.UTF_8));
    }

}
