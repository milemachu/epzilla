/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.core;

import java.io.IOException;
import java.util.Map;

import jstm.core.TransactedObject.Removal;
import jstm.misc.Debug;

final class Serializer {

    public static final byte TRANSACTED_OBJECT = 1;

    public static final byte IMMUTABLE_OBJECT = 2;

    public static final byte REMOVE_KEY = 3;

    public static final byte SWITCH_TO_WRITES = 4;

    public static final byte ENTRIES_VALUE = 5;

    public static final byte NULL = 6;

    public static final byte END = 7;

    private Serializer() {
    }

    public static void writeObject(Writer writer, Object o) throws IOException {
        if (o instanceof TransactedObject) {
            writer.writeByte(TRANSACTED_OBJECT);
            writer.writeTransactedObject((TransactedObject) o);
        } else {
            writer.writeByte(IMMUTABLE_OBJECT);
            writer.writeImmutable(o);
        }
    }

    public static Object readObject(byte code, Reader reader) throws IOException {
        switch (code) {
            case TRANSACTED_OBJECT:
                return reader.readTransactedObject();
            case REMOVE_KEY:
                return Removal.Instance;
            case ENTRIES_VALUE:
                return TransactedSetVersion.ENTRIES_VALUE;
            case NULL:
                return null;
            default:
                Debug.assertion(code == IMMUTABLE_OBJECT);
                return reader.readImmutable();
        }
    }

    public static void writeEntry(Map.Entry entry, Writer writer) throws IOException {
        if (entry.getKey() instanceof TransactedObject) {
            writer.writeByte(TRANSACTED_OBJECT);
            writer.writeTransactedObject((TransactedObject) entry.getKey());
        } else {
            writer.writeByte(IMMUTABLE_OBJECT);
            writer.writeImmutable(entry.getKey());
        }

        if (entry.getValue() instanceof TransactedObject) {
            writer.writeByte(TRANSACTED_OBJECT);
            writer.writeTransactedObject((TransactedObject) entry.getValue());
        } else {
            if (entry.getValue() == TransactedSetVersion.ENTRIES_VALUE)
                writer.writeByte(ENTRIES_VALUE);
            else if (entry.getValue() == Removal.Instance)
                writer.writeByte(REMOVE_KEY);
            else {
                writer.writeByte(IMMUTABLE_OBJECT);
                writer.writeImmutable(entry.getValue());
            }
        }
    }

    private static final byte TAG_STRING = 0;

    private static final byte TAG_TRANSACTED_OBJECT = 1;

    public static final byte TAG_END = 2;

    public static boolean serializeTag(Map.Entry<Object, Object> entry, boolean written, Writer writer) throws IOException {
        String keyAsString = null, valueAsString = null;
        TransactedObject keyAsTO = null, valueAsTO = null;
        boolean res = written;

        // This is to ignore other types of keys and values that
        // could be in the transaction tags

        if (entry.getKey() instanceof String)
            keyAsString = (String) entry.getKey();

        if (entry.getKey() instanceof TransactedObject)
            keyAsTO = (TransactedObject) entry.getKey();

        if (entry.getValue() instanceof String)
            valueAsString = (String) entry.getValue();

        if (entry.getValue() instanceof TransactedObject)
            valueAsTO = (TransactedObject) entry.getValue();

        if (keyAsString != null || keyAsTO != null) {
            if (valueAsString != null || valueAsTO != null) {
                if (!res) {
                    writer.writeInteger(TransactedObject.Id.TAGS.Value);
                    res = true;
                }

                if (keyAsString != null) {
                    writer.writeByte(TAG_STRING);
                    writer.writeString(keyAsString);
                } else {
                    writer.writeByte(TAG_TRANSACTED_OBJECT);
                    writer.writeTransactedObject(keyAsTO);
                }

                if (valueAsString != null) {
                    writer.writeByte(TAG_STRING);
                    writer.writeString(valueAsString);
                } else {
                    writer.writeByte(TAG_TRANSACTED_OBJECT);
                    writer.writeTransactedObject(valueAsTO);
                }
            }
        }

        return res;
    }

    public static void deserializeTag(Map<Object, Object> tags, byte code, Reader reader) throws IOException {
        Object key, value;

        switch (code) {
            case TAG_STRING:
                key = reader.readString();
                break;
            case TAG_TRANSACTED_OBJECT:
                key = reader.readTransactedObject();
                break;
            default:
                throw new IllegalStateException();
        }

        switch (reader.readByte()) {
            case TAG_STRING:
                value = reader.readString();
                break;
            case TAG_TRANSACTED_OBJECT:
                value = reader.readTransactedObject();
                break;
            default:
                throw new IllegalStateException();
        }

        tags.put(key, value);
    }
}
