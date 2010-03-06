package org.epzilla.dispatcher.dataManager;

import jstm.core.TransactedList;
import org.epzilla.dispatcher.dispatcherObjectModel.ClientInfoObject;

/**
 * Created by IntelliJ IDEA.
 * User: Dishan
 * Date: Mar 4, 2010
 * Time: 10:33:41 AM
 * To change this template use File | Settings | File Templates.
 */
public class ClientManager {
    private static TransactedList<ClientInfoObject> clientList = new TransactedList<ClientInfoObject>();

    public static TransactedList<ClientInfoObject> getClientList() {
        return clientList;
    }

    public static void setClientList(TransactedList<ClientInfoObject> clientList) {
        ClientManager.clientList = clientList;
    }
}
