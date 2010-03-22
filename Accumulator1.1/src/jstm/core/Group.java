/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.core;

/**
 * Represents a group of sites. E.g. a server and its clients, or the nodes of a
 * cluster.
 */
public final class Group extends Location {

    protected Group(Connection route) {
        super(route);
    }

    @Override
    protected int getClassId() {
        return ObjectModel.Default.GROUP_CLASS_ID;
    }
}