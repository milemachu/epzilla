/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.transports.clientserver.vm;

import java.io.IOException;

import jstm.core.Site;
import jstm.transports.clientserver.Server;

public final class VMServer extends Server {

    public VMServer() {
        super(Site.getLocal());

        allowThread(Site.getLocal());
    }

    public VMClient createClient(Site clientSite, int number) {
        VMSession session = new VMSession(this);
        VMClient client = new VMClient(session, clientSite, number);
        session.Client = client;
        onConnection(client, session);
        return client;
    }

    public void remove(VMClient client) {
        onDisconnection(client);
    }

    protected Site getSite() {
        return getLocalSite();
    }

    @Override
    protected void listen() throws IOException {
    }
}
