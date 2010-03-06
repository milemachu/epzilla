/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.transports.clientserver.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import jstm.core.Reader;
import jstm.core.Site;
import jstm.misc.BinaryReader;
import jstm.misc.Debug;
import jstm.misc.BinaryWriter.Flusher;
import jstm.transports.clientserver.Client;

public final class HttpClient extends Client {

    private static final int POLLING_LATENCY = 50;

    private final BinaryBuffer _buffer = new BinaryBuffer();

    private final Filler _filler = new Filler();

    private URL _url;

    private Poller _poller;

    private volatile boolean _stop;

    public HttpClient(URL url) {
        super(Site.getLocal());

        _url = url;
    }

    public void disconnect() {
        dispose(null);
    }

    @Override
    protected void _dispose(IOException ex) {
        super._dispose(ex);

        _stop = true;
    }

    @Override
    protected Filler createFiller() throws IOException {
        return _filler;
    }

    @Override
    protected Flusher createFlusher() throws IOException {
        return _buffer.getFlusher();
    }

    @Override
    protected void onStatusChanged(Status value) {
        super.onStatusChanged(value);

        if (value == Status.SYNCHRONIZING) {
            _poller = new Poller();
            _poller.start();
        }
    }

    private final class Poller extends Thread {

        public Poller() {
            setDaemon(true);
        }

        @Override
        public void run() {
            allowThread(Site.getLocal());

            IOException ex;

            try {
                Reader reader = getReader();

                _url = new URL(_url.toString() + "?Id=" + reader.readString());
                reader.assertCounter();

                while (true) {
                    getReader().readMessage();
                }
            } catch (IOException e) {
                ex = e;
            }

            dispose(ex);

            Debug.log(0, "Disconnected (" + ex + ")");
        }
    }

    private final class Filler extends BinaryReader.Filler {

        private InputStream _input;

        private int _available;

        private int _read;

        @Override
        public int fill(byte[] buffer) {
            try {
                int read = 0;

                while (read == 0) {
                    if (_input == null) {
                        if (_stop)
                            throw new IOException();

                        open();
                    }

                    if (_read < _available) {
                        read += _input.read(buffer, 0, Math.min(_available - _read, buffer.length));
                        _read += read;
                    } else {
                        close();
                    }
                }

                return read;
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }

        private void open() throws IOException {
            URLConnection request = _url.openConnection();

            request.setDoInput(true);
            request.setDoOutput(true);
            request.setDefaultUseCaches(false);
            request.setUseCaches(false);
            request.setIfModifiedSince(0);
            request.setRequestProperty("Content-Type", "application/octet-stream");
            // request.setConnectTimeout(PollingSession.SESSION_TIMEOUT_AS_SECONDS);
            // request.setReadTimeout(PollingSession.SESSION_TIMEOUT_AS_SECONDS);

            OutputStream output = request.getOutputStream();
            _buffer.writeTo(output);
            output.close();

            _input = request.getInputStream();
            _available = request.getContentLength();
            _read = 0;
        }

        private void close() throws Exception {
            if (_input != null) {
                _input.close();
                _input = null;
            }

            if (_available == 0)
                Thread.sleep(POLLING_LATENCY);
        }
    }
}
