/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.tests.socket;

import jstm.core.Site;
import jstm.tests.ClientTest;
import jstm.tests.generated.SimpleObjectModel;
import jstm.transports.clientserver.socket.SocketClient;

public class SocketClientTest extends ClientTest {

    private long _start;

    public SocketClientTest() {
        _site = Site.getLocal();
        _number = 0;

        _client = new SocketClient("localhost", 4444);
        
        _site.registerObjectModel(new SimpleObjectModel());
    }

    @Override
    protected void onStart() {
        _start = System.nanoTime();
    }

    @Override
    protected void onFinished() {
        System.out.println("Time : " + (System.nanoTime() - _start) / 1e6 + " ms");
        double writePerSec = WRITE_COUNT * 1e9 / (System.nanoTime() - _start);
        System.out.println((int) writePerSec + " writes/s");
        System.out.flush();
    }

    public static void main(String[] args) {
        SocketClientTest test = new SocketClientTest();
        test.run();
    }
}
