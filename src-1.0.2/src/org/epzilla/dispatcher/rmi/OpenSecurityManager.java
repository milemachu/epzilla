package org.epzilla.dispatcher.rmi;

import java.security.Permission;
import java.rmi.RMISecurityManager;

/**
 * Created by IntelliJ IDEA.
 * User: Rajeev
 * Date: Feb 28, 2010
 * Time: 11:00:44 AM
 * To change this template use File | Settings | File Templates.
 */
public class OpenSecurityManager extends RMISecurityManager {
    /**
     * totally free security manager. no security at all.
     *
     * @param host
     * @param port
     */
    public void checkConnect(String host, int port) {
    }

    public void checkConnect(String host, int port, Object context) {
    }

    public void checkPermission(Permission perm) {
    }

    public void checkAccept(String host, int port) {
    }

}
