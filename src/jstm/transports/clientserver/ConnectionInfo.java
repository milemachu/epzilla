/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.transports.clientserver;

import jstm.core.Group;
import jstm.core.Site;

public final class ConnectionInfo {

    private final Site _server;

    private final Group _serverAndClients;

    protected ConnectionInfo(Site server, Group serverAndClients) {
        _server = server;
        _serverAndClients = serverAndClients;
    }

    public Site getServer() {
        return _server;
    }

    /**
     * @see Server.getServerAndClients()
     */
    public Group getServerAndClients() {
        return _serverAndClients;
    }
}
