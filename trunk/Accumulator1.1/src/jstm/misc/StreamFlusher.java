/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.misc;

import java.io.IOException;
import java.io.OutputStream;

import jstm.misc.BinaryWriter.Flusher;

public final class StreamFlusher extends Flusher {

    private final OutputStream _stream;

    public StreamFlusher(OutputStream stream) {
        _stream = stream;
    }

    @Override
    public void flush(byte[] buffer, int length, boolean forced) {
        try {
            _stream.write(buffer, 0, length);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
