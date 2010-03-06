/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.misc;

import java.io.IOException;
import java.util.Date;

public class BinaryReader {

    public static abstract class Filler {

        public abstract int fill(byte[] buffer) throws IOException;
    }

    private final byte[] _buffer = new byte[4096];

    private final Filler _filler;

    private int _offset;

    private int _length;

    private int _counter = Integer.MIN_VALUE;

    public BinaryReader(Filler filler) {
        _filler = filler;
    }

    public final int getRemaining() {
        return _length - _offset;
    }

    public final void assertCounter() throws IOException {
        if (Debug.ASSERT_COMMUNICATIONS) {
            int a = readInteger();
            int b = _counter++;
            Debug.assertion(a == b);
        }
    }

    public final byte readByte() throws IOException {
        if (_offset >= _length) {
            _offset = 0;
            _length = _filler.fill(_buffer);
        }

        return _buffer[_offset++];
    }

    public final boolean readBoolean() throws IOException {
        return readByte() == 1;
    }

    public final short readShort() throws IOException {
        int b0 = readByte() & 0x000000ff;
        int b1 = (readByte() << 8) & 0x0000ff00;
        return (short) (b1 | b0);
    }

    public final char readCharacter() throws IOException {
        return (char) readShort();
    }

    public final int readInteger() throws IOException {
        int b0 = readByte() & 0x000000ff;
        int b1 = (readByte() << 8) & 0x0000ff00;
        int b2 = (readByte() << 16) & 0x00ff0000;
        int b3 = (readByte() << 24) & 0xff000000;
        return b3 | b2 | b1 | b0;
    }

    public final long readLong() throws IOException {
        long i0 = readInteger() & 0xffffffffL;
        long i1 = readInteger() & 0xffffffffL;
        return i0 | (i1 << 32);
    }

    public final float readFloat() throws IOException {
        // Not supported by gwt
        // return Float.intBitsToFloat(readInteger());
        return Float.parseFloat(readString());
    }

    public final double readDouble() throws IOException {
        // Not supported by gwt
        // return Double.longBitsToDouble(readLong());
        return Double.parseDouble(readString());
    }

    public final String readString() throws IOException {
        int length = readInteger();

        if (length != -1) {
            char[] array = new char[length];

            for (int i = 0; i < array.length; i++)
                array[i] = (char) readShort();

            return new String(array);
        }

        return null;
    }

    public final Date readDate() throws IOException {
        long value = readLong();

        if (value != -1)
            return new Date(value);

        return null;
    }

    public final Object readImmutable() throws IOException {
        switch (readByte()) {
            case -1:
                return null;
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
            case ImmutableClasses.DateIndex:
                return readDate();
        }

        throw new java.lang.IllegalArgumentException();
    }
}
