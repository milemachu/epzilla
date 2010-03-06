/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.transports.clientserver.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import jstm.core.Site;
import jstm.misc.Debug;
import jstm.misc.StreamFiller;
import jstm.misc.StreamFlusher;
import jstm.misc.BinaryReader.Filler;
import jstm.misc.BinaryWriter.Flusher;
import jstm.transports.clientserver.Session;

final class SocketSession extends Session {

    private final Socket _socket;

    private final ReaderThread _thread;

    public SocketSession(SocketServer server, Socket socket) {
        super(server);

        _socket = socket;

        _thread = new ReaderThread();
        _thread.setDaemon(true);
        _thread.start();
    }

    @Override
    protected Filler createFiller() throws IOException {
        InputStream stream = _socket.getInputStream();
        return new StreamFiller(stream);
    }

    @Override
    protected Flusher createFlusher() throws IOException {
        OutputStream stream = _socket.getOutputStream();
        return new StreamFlusher(stream);
    }

    protected void close() {
        try {
            _socket.close();
        } catch (IOException e) {
        }
    }

    protected void join() {
        try {
            _thread.join();
        } catch (InterruptedException e) {
        }
    }

    private final class ReaderThread extends Thread {

        @Override
        public void run() {
            allowThread(Site.getLocal());

            try {
                ((SocketServer) getServer()).onConnection(SocketSession.this);

                while (true) {
                    getReader().readMessage();
                }
            } catch (IOException ex) {
                Debug.log("Disconnected");
            }

            ((SocketServer) getServer()).onDisconnection(SocketSession.this);
        }
    }
}
