/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.misc;

public final class TextWriter {

    public static final char SEP = '\uFFFF';

    public static abstract class Flusher {

        public abstract void flush(StringBuilder buffer);
    }

    private final StringBuilder _buffer = new StringBuilder();

    private final Flusher _flusher;

    public TextWriter(Flusher flusher) {
        _flusher = flusher;
    }

    public Flusher getFlusher() {
        return _flusher;
    }

    public void flush() {
        if (_buffer.length() > 0) {
            _flusher.flush(_buffer);
            _buffer.setLength(0);
        }
    }

    public void writeString(String value) {
        _buffer.append(value);
        _buffer.append(SEP);
    }

    public void writeByte(byte value) {
        _buffer.append(value);
        _buffer.append(SEP);
    }

    public void writeBoolean(boolean value) {
        _buffer.append(value ? "1" : "0");
        _buffer.append(SEP);
    }

    public void writeShort(short value) {
        _buffer.append(value);
        _buffer.append(SEP);
    }

    public void writeCharacter(char value) {
        _buffer.append(value);
        _buffer.append(SEP);
    }

    public void writeInteger(int value) {
        _buffer.append(value);
        _buffer.append(SEP);
    }

    public void writeLong(long value) {
        _buffer.append(value);
        _buffer.append(SEP);
    }

    public void writeFloat(float value) {
        _buffer.append(value);
        _buffer.append(SEP);
    }

    public void writeDouble(double value) {
        _buffer.append(value);
        _buffer.append(SEP);
    }

    public void writeObject(Object o) {
        if (o instanceof String) {
            writeByte((byte) ImmutableClasses.StringIndex);
            writeString((String) o);
        }

        if (o instanceof Integer) {
            writeByte((byte) ImmutableClasses.IntIndex);
            writeInteger(((Integer) o).intValue());
        }

        if (o instanceof Long) {
            writeByte((byte) ImmutableClasses.LongIndex);
            writeLong(((Long) o).longValue());
        }

        if (o instanceof Boolean) {
            writeByte((byte) ImmutableClasses.BoolIndex);
            writeBoolean(((Boolean) o).booleanValue());
        }

        if (o instanceof Byte) {
            writeByte((byte) ImmutableClasses.ByteIndex);
            writeByte(((Byte) o).byteValue());
        }

        if (o instanceof Short) {
            writeByte((byte) ImmutableClasses.ShortIndex);
            writeShort(((Short) o).shortValue());
        }

        if (o instanceof Character) {
            writeByte((byte) ImmutableClasses.CharIndex);
            writeCharacter(((Character) o).charValue());
        }

        if (o instanceof Float) {
            writeByte((byte) ImmutableClasses.FloatIndex);
            writeFloat(((Float) o).floatValue());
        }

        if (o instanceof Double) {
            writeByte((byte) ImmutableClasses.DoubleIndex);
            writeDouble(((Double) o).doubleValue());
        }

        throw new java.lang.IllegalArgumentException();
    }
}
