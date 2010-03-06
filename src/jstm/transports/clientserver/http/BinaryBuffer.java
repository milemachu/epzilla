/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.transports.clientserver.http;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import jstm.misc.BinaryWriter;

final class BinaryBuffer {

    private final ArrayList<byte[]> _buffers = new ArrayList<byte[]>();

    private final Flusher _flusher;

    public BinaryBuffer() {
        _flusher = new Flusher();
    }

    public BinaryWriter.Flusher getFlusher() {
        return _flusher;
    }

    public void writeTo(OutputStream stream) throws IOException {
        byte[][] salve;

        synchronized (_buffers) {
            salve = _buffers.toArray(new byte[_buffers.size()][]);
            _buffers.clear();
        }

        for (int i = 0; i < salve.length; i++) {
            // TODO write counter & length to handle retries
            stream.write(salve[i]);
        }
    }

    private class Flusher extends BinaryWriter.Flusher {

        @Override
        public void flush(byte[] buffer, int length, boolean forced) {
            byte[] copy = new byte[length];

            System.arraycopy(buffer, 0, copy, 0, length);

            synchronized (_buffers) {
                _buffers.add(copy);
            }
        }
    }
}
