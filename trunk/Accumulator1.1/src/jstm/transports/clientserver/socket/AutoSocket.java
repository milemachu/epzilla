/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.transports.clientserver.socket;

import java.io.IOException;

import jstm.core.Group;
import jstm.misc.Log;
import jstm.transports.clientserver.ConnectionInfo;

/**
 * Tries to start a socket server. If it fails because one already exists on
 * localhost, it tries to connect to it instead using a socket client. Handy for
 * demos.
 */
public class AutoSocket {

    private static final String SERVER_ADDRESS = "localhost";

    private final int _port;

    private Group _serverAndClients;

    public AutoSocket(int port) {
        _port = port;
    }

    public Group getServerAndClients() {
        if (_serverAndClients == null) {
            _serverAndClients = tryToStartServer();

            if (_serverAndClients == null) {
                _serverAndClients = tryToStartClient();
            }
        }

        return _serverAndClients;
    }

    private Group tryToStartServer() {
        SocketServer server = new SocketServer(_port);

        try {
            server.start();

            Log.write("Started a socket server on port " + server.getPort());
        } catch (IOException e) {
            Log.write("Could not start a socket server on port " + server.getPort() + " (" + e.toString() + ")");
            return null;
        }

        return server.getServerAndClients();
    }

    private Group tryToStartClient() {
        Log.write("Starting a socket client instead");

        SocketClient client = new SocketClient(SERVER_ADDRESS, _port);
        ConnectionInfo connection;

        try {
            connection = client.connect();
            Log.write("Connected to socket server " + SERVER_ADDRESS + " on port " + _port);
        } catch (IOException e) {
            Log.write("Could not connect to socket server " + SERVER_ADDRESS + " on port " + _port + " (" + e + ")");
            throw new RuntimeException(e);
        }

        return connection.getServerAndClients();
    }
}
