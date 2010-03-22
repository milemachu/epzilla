/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.misc;

public final class TextReader {

    private String _string;

    private int _offset;

    public TextReader() {
    }

    public void setString(String value) {
        _string = value;
    }

    public String readString() {
        int start = _offset;
        _offset = _string.indexOf(TextWriter.SEP, _offset) + 1;
        return _string.substring(start, _offset - 1);
    }

    public byte readByte() {
        return Byte.parseByte(readString());
    }

    public boolean readBoolean() {
        return readString().equals("1");
    }

    public short readShort() {
        return Short.parseShort(readString());
    }

    public char readCharacter() {
        return readString().charAt(0);
    }

    public int readInteger() {
        return Integer.parseInt(readString());
    }

    public long readLong() {
        return Long.parseLong(readString());
    }

    public float readFloat() {
        return Float.parseFloat(readString());
    }

    public double readDouble() {
        return Double.parseDouble(readString());
    }

    public Object readObject() {
        switch (readByte()) {
            case ImmutableClasses.StringIndex:
                return readString();
            case ImmutableClasses.IntIndex:
                return new Integer(readInteger());
            case ImmutableClasses.LongIndex:
                return new Long(readLong());
            case ImmutableClasses.BoolIndex:
                return new Boolean(readBoolean());
            case ImmutableClasses.ByteIndex:
                return new Byte(readByte());
            case ImmutableClasses.ShortIndex:
                return new Short(readShort());
            case ImmutableClasses.CharIndex:
                return new Character(readCharacter());
            case ImmutableClasses.FloatIndex:
                return new Float(readFloat());
            case ImmutableClasses.DoubleIndex:
                return new Double(readDouble());
        }

        throw new java.lang.IllegalArgumentException();
    }
}
