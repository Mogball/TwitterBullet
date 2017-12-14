package com.yahoo.bullet.twitter.model;

import com.yahoo.bullet.record.BulletRecord;
import org.testng.annotations.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

public class RecordWriterTest {

    private static class PseudoString {
        private final String string;

        private PseudoString(String string) {
            this.string = string;
        }

        @Override
        public String toString() {
            return string;
        }
    }

    public Long longBoxField;
    public long longField;
    public Integer intBoxField;
    public int intField;

    public Double doubleBoxField;
    public double doubleField;
    public Float floatBoxField;
    public float floatField;

    public Boolean boolBoxField;
    public boolean boolField;

    private static class PseudoStringBox {
        @RecordEntry("string")
        public final PseudoString string;

        private PseudoStringBox(String str) {
            this.string = new PseudoString(str);
        }
    }

    @Test
    public void testIsLong() throws NoSuchFieldException {
        Class<?> cls = getClass();
        assertTrue(RecordWriter.isLong(cls.getField("longBoxField")));
        assertTrue(RecordWriter.isLong(cls.getField("longField")));
        assertTrue(RecordWriter.isLong(cls.getField("intBoxField")));
        assertTrue(RecordWriter.isLong(cls.getField("intField")));
        assertFalse(RecordWriter.isLong(cls.getField("boolBoxField")));
        assertFalse(RecordWriter.isLong(cls.getField("boolField")));
    }

    @Test
    public void testIsDouble() throws NoSuchFieldException {
        Class<?> cls = getClass();
        assertTrue(RecordWriter.isDouble(cls.getField("doubleBoxField")));
        assertTrue(RecordWriter.isDouble(cls.getField("doubleField")));
        assertTrue(RecordWriter.isDouble(cls.getField("floatBoxField")));
        assertTrue(RecordWriter.isDouble(cls.getField("floatField")));
        assertFalse(RecordWriter.isDouble(cls.getField("longBoxField")));
        assertFalse(RecordWriter.isDouble(cls.getField("longField")));
    }

    @Test
    public void testIsBool() throws NoSuchFieldException {
        Class<?> cls = getClass();
        assertTrue(RecordWriter.isBool(cls.getField("boolBoxField")));
        assertTrue(RecordWriter.isBool(cls.getField("boolField")));
        assertFalse(RecordWriter.isBool(cls.getField("longField")));
        assertFalse(RecordWriter.isBool(cls.getField("longBoxField")));
    }

    @Test
    public void testWriteUnknownType() {
        BulletRecord record = new BulletRecord();
        RecordWriter writer = new RecordWriter(record);
        PseudoStringBox box = new PseudoStringBox("hello");
        writer.writeFieldsOf(box, "pseudo");
        assertTrue(record.hasField("pseudo_string"));
        assertEquals(record.get("pseudo_string"), "hello");
    }

    private static class Duplicator {
        @RecordEntry("duplicate")
        public final int field1;
        @RecordEntry("duplicate")
        public final int field2;

        private Duplicator(int a, int b) {
            this.field1 = a;
            this.field2 = b;
        }
    }

    @Test
    public void testDuplicateFieldNameDoesNotOverwrite() {
        BulletRecord mockRecord = mock(BulletRecord.class);
        when(mockRecord.setLong(anyString(), anyLong())).then(iom -> {
            when(mockRecord.hasField(anyString())).thenReturn(true);
            return null;
        });
        RecordWriter writer = new RecordWriter(mockRecord);
        Duplicator dup = new Duplicator(50, 100);
        writer.writeFieldsOf(dup, null);
        verify(mockRecord).setLong(anyString(), anyLong());
    }

    private static class Inaccessible {
        @RecordEntry("inaccessible")
        private final int number;

        private Inaccessible(int number) {
            this.number = number;
        }
    }

    @Test
    public void testDoesNotWriteInaccessibleField() {
        BulletRecord mockRecord = mock(BulletRecord.class);
        RecordWriter writer = new RecordWriter(mockRecord);
        writer.writeFieldsOf(new Inaccessible(10), null);
        verify(mockRecord, times(0)).setLong(anyString(), anyLong());
    }

}
