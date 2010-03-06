/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.transports.clientserver.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import jstm.core.Site;
import jstm.misc.Debug;
import jstm.transports.clientserver.Server;
import jstm.transports.clientserver.Session;

public final class SocketServer extends Server {

    private final int _port;

    private ServerSocket _socket;

    public SocketServer(int port) {
        this(Site.getLocal(), port);
    }

    protected SocketServer(Site local, int port) {
        super(local);

        _port = port;

        allowThread(Site.getLocal());
    }

    public int getPort() {
        return _port;
    }

    @Override
    protected void listen() throws IOException {
        _socket = new ServerSocket(_port);

        Acceptor thread = new Acceptor();
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public void _dispose(IOException ex) {
        for (Session session : getSessions().values())
            ((SocketSession) session).close();

        for (Session session : getSessions().values())
            ((SocketSession) session).join();

        Debug.assertion(getSessions().size() == 0);

        super._dispose(ex);

        try {
            _socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void onConnection(SocketSession session) {
        onConnection(session, session);
    }

    protected void onDisconnection(SocketSession session) {
        onDisconnection((Object) session);
    }

    private final class Acceptor extends Thread {

        @Override
        public void run() {
            try {
                while (true) {
                    Socket clientSocket = _socket.accept();

                    if (Debug.MESSAGING)
                        Debug.log("Connection from " + clientSocket.getRemoteSocketAddress());

                    new SocketSession(SocketServer.this, clientSocket);
                }
            } catch (IOException e) {
                System.out.println("Socket server on port " + _port + " stopped");
            }
        }
    }
}
