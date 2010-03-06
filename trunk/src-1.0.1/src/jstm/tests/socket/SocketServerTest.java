/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.tests.socket;

import jstm.misc.Console;
import jstm.tests.ServerTest;
import jstm.transports.clientserver.socket.SocketServer;

public class SocketServerTest {

    public static void main(String[] args) throws Exception {
        SocketServer server = new SocketServer(4444);
        server.start();

        ServerTest.Test(server);

        System.out.println("Press enter to continue");
        Console.readLine();

        server.stop();
    }
}
