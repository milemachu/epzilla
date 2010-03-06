/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.transports.cluster;

import jstm.core.Group;

public final class ConnectionInfo {

    private final Group _cluster;

    protected ConnectionInfo(Group cluster) {
        _cluster = cluster;
    }

    public Group getCluster() {
        return _cluster;
    }
}
