/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.misc;

import java.io.IOException;
import java.io.InputStream;

import jstm.misc.BinaryReader.Filler;

public final class StreamFiller extends Filler {

    private final InputStream _stream;

    public StreamFiller(InputStream stream) {
        _stream = stream;
    }

    @Override
    public int fill(byte[] buffer) throws IOException {
        int read = _stream.read(buffer, 0, buffer.length);

        if (read <= 0)
            throw new IOException();

        return read;
    }
}
