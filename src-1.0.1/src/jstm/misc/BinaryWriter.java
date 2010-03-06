/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.misc;

import java.io.IOException;
import java.util.Date;

public class BinaryWriter {

    public static abstract class Flusher {

        public abstract void flush(byte[] buffer, int length, boolean forced) throws IOException;
    }

    private final byte[] _buffer = new byte[4096];

    private final Flusher _flusher;

    private int _length;

    private int _counter = Integer.MIN_VALUE;

    public BinaryWriter(Flusher flusher) {
        _flusher = flusher;
    }

    public final Flusher getFlusher() {
        return _flusher;
    }

    public final void flush() throws IOException {
        flush(true);
    }

    private final void flush(boolean forced) throws IOException {
        if (_length > 0) {
            if (Debug.ASSERT_COMMUNICATIONS && forced) {
                int value = _counter++;
                _buffer[_length++] = (byte) (value & 0xff);
                _buffer[_length++] = (byte) ((value >>> 8) & 0xff);
                _buffer[_length++] = (byte) ((value >>> 16) & 0xff);
                _buffer[_length++] = (byte) ((value >>> 24) & 0xff);
            }

            _flusher.flush(_buffer, _length, forced);
            _length = 0;
        }
    }

    public final void writeByte(byte value) throws IOException {
        int leave = 0;

        if (Debug.ASSERT_COMMUNICATIONS)
            leave = Integer.SIZE;

        if (_length >= _buffer.length - leave)
            flush(false);

        _buffer[_length++] = value;
    }

    public final void writeBoolean(boolean value) throws IOException {
        writeByte(value ? (byte) 1 : (byte) 0);
    }

    public final void writeShort(short value) throws IOException {
        writeByte((byte) (value & 0xff));
        writeByte((byte) ((value >>> 8) & 0xff));
    }

    public final void writeCharacter(char value) throws IOException {
        writeShort((short) value);
    }

    public final void writeInteger(int value) throws IOException {
        writeByte((byte) (value & 0xff));
        writeByte((byte) ((value >>> 8) & 0xff));
        writeByte((byte) ((value >>> 16) & 0xff));
        writeByte((byte) ((value >>> 24) & 0xff));
    }

    public final void writeLong(long value) throws IOException {
        writeInteger((int) value);
        writeInteger((int) (value >>> 32));
    }

    public final void writeFloat(float value) throws IOException {
        // writeInteger(Float.floatToRawIntBits(value)); // GWT
        writeString(Float.toString(value));
    }

    public final void writeDouble(double value) throws IOException {
        // writeLong(Double.doubleToRawLongBits(value)); // GWT
        writeString(Double.toString(value));
    }

    public final void writeString(String value) throws IOException {
        if (value == null)
            writeInteger(-1);
        else {
            writeInteger(value.length());

            for (int i = 0; i < value.length(); i++)
                writeShort((short) value.charAt(i));
        }
    }

    public final void writeDate(Date value) throws IOException {
        if (value == null)
            writeLong(-1);
        else
            writeLong(value.getTime());
    }

    // TODO : cache to avoid search of type each time

    public final void writeImmutable(Object o) throws IOException {
        if (o == null) {
            writeByte((byte) -1);
            return;
        }

        if (o instanceof String) {
            writeByte((byte) ImmutableClasses.StringIndex);
            writeString((String) o);
            return;
        }

        if (o instanceof Integer) {
            writeByte((byte) ImmutableClasses.IntIndex);
            writeInteger(((Integer) o).intValue());
            return;
        }

        if (o instanceof Long) {
            writeByte((byte) ImmutableClasses.LongIndex);
            writeLong(((Long) o).longValue());
            return;
        }

        if (o instanceof Boolean) {
            writeByte((byte) ImmutableClasses.BoolIndex);
            writeBoolean(((Boolean) o).booleanValue());
            return;
        }

        if (o instanceof Byte) {
            writeByte((byte) ImmutableClasses.ByteIndex);
            writeByte(((Byte) o).byteValue());
            return;
        }

        if (o instanceof Short) {
            writeByte((byte) ImmutableClasses.ShortIndex);
            writeShort(((Short) o).shortValue());
            return;
        }

        if (o instanceof Character) {
            writeByte((byte) ImmutableClasses.CharIndex);
            writeCharacter(((Character) o).charValue());
            return;
        }

        if (o instanceof Float) {
            writeByte((byte) ImmutableClasses.FloatIndex);
            writeFloat(((Float) o).floatValue());
            return;
        }

        if (o instanceof Double) {
            writeByte((byte) ImmutableClasses.DoubleIndex);
            writeDouble(((Double) o).doubleValue());
            return;
        }

        if (o instanceof Date) {
            writeByte((byte) ImmutableClasses.DateIndex);
            writeDate((Date) o);
        }

        throw new java.lang.IllegalArgumentException();
    }
}
