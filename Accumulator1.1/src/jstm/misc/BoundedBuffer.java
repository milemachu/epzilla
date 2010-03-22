/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.misc;

import java.io.IOException;

public final class BoundedBuffer {

    private final byte[] _buffer = new byte[4096];

    private int _length;

    public BoundedBuffer() {
    }

    public int length() {
        return _length;
    }

    public void clear() {
        synchronized (_buffer) {
            _length = 0;
            _buffer.notify();
        }
    }

    public void write(byte[] data, int length) {
        synchronized (_buffer) {
            while (_length + length > _buffer.length) {
                try {
                    _buffer.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            System.arraycopy(data, 0, _buffer, _length, length);
            _length += length;
            _buffer.notify();
        }
    }

    public int read(byte[] target) throws IOException {
        int length;

        synchronized (_buffer) {
            while (_length == 0) {
                try {
                    _buffer.wait();
                } catch (InterruptedException e) {
                    throw new IOException("Interrupted");
                }
            }

            System.arraycopy(_buffer, 0, target, 0, _length);
            length = _length;
            _length = 0;
            _buffer.notify();
        }

        return length;
    }

    public static final class Filler extends BinaryReader.Filler {

        private final BoundedBuffer _buffer;

        public Filler(BoundedBuffer buffer) {
            _buffer = buffer;
        }

        @Override
        public int fill(byte[] buffer) throws IOException {
            return _buffer.read(buffer);
        }
    }

    public static final class Flusher extends BinaryWriter.Flusher {

        public final BoundedBuffer Buffer;

        public Flusher() {
            Buffer = new BoundedBuffer();
        }

        @Override
        public void flush(byte[] buffer, int length, boolean forced) {
            Buffer.write(buffer, length);
        }
    }
}
