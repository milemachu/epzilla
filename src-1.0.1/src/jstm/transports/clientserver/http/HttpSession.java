/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.transports.clientserver.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import jstm.misc.BinaryReader;
import jstm.misc.BinaryWriter.Flusher;
import jstm.transports.clientserver.polling.PollingSession;

final class HttpSession extends PollingSession {

    private final BinaryBuffer _buffer = new BinaryBuffer();

    private final Filler _filler = new Filler();

    protected HttpSession(HttpServer server) {
        super(server);
    }

    @Override
    protected Filler createFiller() throws IOException {
        return _filler;
    }

    @Override
    protected Flusher createFlusher() throws IOException {
        return _buffer.getFlusher();
    }

    protected void read(InputStream stream, int length) throws IOException {
        synchronized (_filler) {
            _filler._stream = stream;
            _filler._left = length;

            while (getReader().getRemaining() > 0 || !_filler.isDone())
                getReader().readMessage();
        }
    }

    protected void write(OutputStream stream) throws IOException {
        _buffer.writeTo(stream);
    }

    private static final class Filler extends BinaryReader.Filler {

        protected InputStream _stream;

        protected int _left;

        public boolean isDone() {
            return _left <= 0;
        }

        @Override
        public int fill(byte[] buffer) {
            try {
                int read = _stream.read(buffer, 0, buffer.length);
                _left -= read;
                return read;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
