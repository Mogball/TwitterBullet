package com.yahoo.bullet.twitter.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import static com.yahoo.bullet.twitter.model.ColumnWriter.COL_BOOLEAN_TYPE;
import static com.yahoo.bullet.twitter.model.ColumnWriter.COL_DESC;
import static com.yahoo.bullet.twitter.model.ColumnWriter.COL_DOUBLE_TYPE;
import static com.yahoo.bullet.twitter.model.ColumnWriter.COL_LONG_TYPE;
import static com.yahoo.bullet.twitter.model.ColumnWriter.COL_NAME;
import static com.yahoo.bullet.twitter.model.ColumnWriter.COL_STRING_TYPE;
import static com.yahoo.bullet.twitter.model.ColumnWriter.COL_TYPE;
import static com.yahoo.bullet.twitter.model.ColumnWriter.getTypeName;
import static com.yahoo.bullet.twitter.model.ColumnWriter.writeColumn;
import static org.junit.Assert.assertTrue;
import static org.testng.Assert.assertEquals;

public class ColumnWriterTest {

    public String stringField;
    public Long longField;
    public Double doubleField;
    public Boolean boolField;

    @Test
    public void testGetTypeName() throws NoSuchFieldException {
        Class<?> cls = getClass();
        assertEquals(getTypeName(cls.getField("stringField")), COL_STRING_TYPE);
        assertEquals(getTypeName(cls.getField("longField")), COL_LONG_TYPE);
        assertEquals(getTypeName(cls.getField("doubleField")), COL_DOUBLE_TYPE);
        assertEquals(getTypeName(cls.getField("boolField")), COL_BOOLEAN_TYPE);
    }

    private static class Column {
        @RecordEntry(value = "field", desc = "description")
        public final String field;

        private Column(String field) {
            this.field = field;
        }
    }

    @Test
    public void testWriteColumn() throws NoSuchFieldException {
        JsonArray array = new JsonArray();
        Field field = Column.class.getField("field");
        String prefix = "col";
        writeColumn(array, field, prefix);
        assertEquals(array.size(), 1);
        JsonElement element = array.get(0);
        assertTrue(element.isJsonObject());
        JsonObject object = element.getAsJsonObject();
        assertTrue(object.has(COL_NAME));
        assertTrue(object.has(COL_TYPE));
        assertTrue(object.has(COL_DESC));
        assertEquals(object.get(COL_NAME).getAsString(), "col_field");
        assertEquals(object.get(COL_TYPE).getAsString(), "STRING");
        assertEquals(object.get(COL_DESC).getAsString(), "description");
    }

    private static class MultiColumn {
        @RecordEntry(value = "field1", desc = "desc1")
        public final String field1 = null;
        @RecordEntry(value = "field2", desc = "desc2")
        public final Long field2 = null;
        @RecordEntry(value = "field3", desc = "desc3")
        public final Double field3 = null;
        @RecordEntry(value = "field4", desc = "desc4")
        public final Boolean field4 = null;
    }

    @Test
    public void testWriteColumnsFor() {
        ColumnWriter writer = new ColumnWriter();
        writer.writeColumnsFor(MultiColumn.class, null);
        verifyMultiColumnArray(writer.columns);
    }

    @Test
    public void testWriteColumnsToStream() throws IOException {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        ColumnWriter writer = new ColumnWriter();
        writer.writeColumnsFor(MultiColumn.class, null);
        writer.writeColumnsTo(buf, true);
        String json = new String(buf.toByteArray());
        JsonElement element = new JsonParser().parse(json);
        verifyMultiColumnArray(element.getAsJsonArray());
    }

    private static void verifyMultiColumnArray(JsonArray cols) {
        assertEquals(cols.size(), 4);
        Map<String, String> nameToType = new TreeMap<String, String>() {
            {
                put("field1", "STRING");
                put("field2", "LONG");
                put("field3", "DOUBLE");
                put("field4", "BOOLEAN");
            }
        };
        Set<String> nameSet = new TreeSet<>();
        for (int i = 1; i <= 4; i++) {
            nameSet.add("field" + i);
        }
        for (JsonElement element : cols) {
            JsonObject col = element.getAsJsonObject();
            assertTrue(col.has(COL_NAME));
            String name = col.get(COL_NAME).getAsString();
            assertEquals(col.get(COL_TYPE).getAsString(), nameToType.get(name));
            assertEquals(col.get(COL_DESC).getAsString(), "desc" + name.charAt(5));
            assertTrue(nameSet.remove(name));
        }
    }

}
