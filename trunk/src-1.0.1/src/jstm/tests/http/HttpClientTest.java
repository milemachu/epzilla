/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.tests.http;

import java.net.URL;

import jstm.core.Site;
import jstm.tests.ClientTest;
import jstm.tests.generated.SimpleObjectModel;
import jstm.transports.clientserver.http.HttpClient;

/**
 * Tests the http connection. You can use the Jetty implementation as server.
 * Check out the jstm.transports.clientserver.jetty project from the SVN
 * repository at https://jstm.svn.sourceforge.net/svnroot/jstm/trunk.
 */
public class HttpClientTest extends ClientTest {

    public HttpClientTest() {
        _site = Site.getLocal();
        _number = 0;

        try {
            _client = new HttpClient(new URL("http://localhost:80/jstm"));
        } catch (Exception e) {
            throw new RuntimeException();
        }

        _site.registerObjectModel(new SimpleObjectModel());
    }

    public static void main(String[] args) {
        HttpClientTest test = new HttpClientTest();
        test.run();
    }
}
