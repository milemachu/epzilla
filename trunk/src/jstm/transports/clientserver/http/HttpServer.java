/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.transports.clientserver.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import jstm.core.Site;
import jstm.transports.clientserver.polling.PollingServer;

/**
 * A Jetty based server implementation is available at
 * https://jstm.svn.sourceforge.net/svnroot/jstm/trunk/jetty
 */
public class HttpServer extends PollingServer {

    public HttpServer() {
        allowThread(Site.getLocal());
    }

    public final void call(String id, InputStream input, int inputLength, OutputStream output) throws IOException {
        allowThread(Site.getLocal());

        HttpSession session;

        if (id == null) {
            session = new HttpSession(this);
            addSession(session, true);
        } else
            session = (HttpSession) getSession(id);

        if (session != null) {
            session.notifyOfCall();
            session.read(input, inputLength);
            session.write(output);
        }
    }
}
