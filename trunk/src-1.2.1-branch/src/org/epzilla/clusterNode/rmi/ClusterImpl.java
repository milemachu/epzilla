package org.epzilla.clusterNode.rmi;

import jstm.core.Site;
import jstm.core.TransactedList;
import jstm.core.Transaction;
import org.epzilla.clusterNode.accConnector.DeriveEventSender;
import org.epzilla.clusterNode.clusterInfoObjectModel.TriggerObject;
import org.epzilla.clusterNode.dataManager.EventsManager;
import org.epzilla.clusterNode.dataManager.TriggerManager;
import org.epzilla.clusterNode.dataManager.EventsCounter;
import org.epzilla.clusterNode.processor.EventProcessor;
import org.epzilla.dispatcher.dispatcherObjectModel.TriggerInfoObject;
import org.epzilla.dispatcher.rmi.TriggerRepresentation;
import org.epzilla.util.Logger;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Team epZilla
 * Date: Mar 11, 2010
 * Modified: May 11, 2010
 * Time: 10:49:26 AM
 * To change this template use File | Settings | File Templates.
 */
public class ClusterImpl extends UnicastRemoteObject implements ClusterInterface {

    public ClusterImpl() throws RemoteException {
    }

    /*
   Accept trigger stream by Leader node
    */

    public String acceptTiggerStream(ArrayList<String> tList, String clusterID, String clientID) throws RemoteException {
        try {
            for (int i = 0; i < tList.size(); i++) {
                TriggerManager.addTriggerToList(tList.get(i), clientID);
            }
            return "OK";
        } catch (Exception e) {
            System.err.println("Trigger adding failure");
        }
        return null;
    }


    public String acceptEventStream(byte[] event, String clusterID, String clientID) throws RemoteException {
        try {
            EventsManager em = new EventsManager(clientID);
            String eventS = new String(event);
            EventsManager.addEvents(eventS);
            EventsCounter.setInEventCount();
            Logger.log(eventS);
            return "OK";
        } catch (Exception e) {
            Logger.log("Events adding failure");
        }
        return null;
    }


    // todo - add acc. ip

    public void addEventStream(String event, String clientID) throws RemoteException {
        String derivedEvent = EventProcessor.getInstance().processEvent(event);
        System.out.println("addeventstream called.");
        try {
            DeriveEventSender.sendDeriveEvent("192.168.1.2", derivedEvent.getBytes());
            System.out.println("sending derived event...");
        } catch (Exception e) {
            Logger.error("error adding event in cluster node", e);
        }

    }

    /*
   Accept trigger stream by Processing Node
    */

    public String addTriggerStream(ArrayList<String> tlist, String clientID) throws RemoteException {
        return null;
    }

    public String deleteTriggers(ArrayList<String> list, String clusterID, String clientID) throws RemoteException {
        // trigger deleting logic here
        return null;
    }

    @Override
    public boolean deleteTriggers(HashMap<String, ArrayList<String>> rep) throws RemoteException {
        try {
//        TriggerManager.getTriggers().get(1)
            List<TriggerObject> toRemoveList = new LinkedList();
            TransactedList<TriggerObject> tlist = TriggerManager.getTriggers();

            for (TriggerObject to : tlist) {
                ArrayList<String> tr = rep.get(to.getclientID());
                if (tr != null) {
                    if (tr.contains(to.gettrigger())) {
                        toRemoveList.add(to);
                    }
                }
            }

            if (Site.getLocal().getPendingCommitCount() < Site.MAX_PENDING_COMMIT_COUNT) {
                Site.getLocal().allowThread();
                Transaction transaction = Site.getLocal().startTransaction();
                TriggerManager.getTriggers().removeAll(toRemoveList);
                transaction.commit();
            }  else {
                return false;
            }


        } catch (Exception e) {
            return false;
        }
        return true;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
