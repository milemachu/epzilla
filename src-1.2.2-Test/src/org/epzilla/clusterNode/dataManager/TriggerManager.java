package org.epzilla.clusterNode.dataManager;

import jstm.core.Site;
import jstm.core.TransactedList;
import jstm.core.Transaction;
import org.epzilla.clusterNode.clusterInfoObjectModel.TriggerObject;
import org.epzilla.dispatcher.RandomStringGenerator;
import org.epzilla.dispatcher.rmi.TriggerRepresentation;

import java.util.StringTokenizer;
import java.util.TimerTask;

/**
 * Created by IntelliJ IDEA.
 * User: Dishan
 * Date: Mar 4, 2010
 * Time: 10:23:48 AM
 * To change this template use File | Settings | File Templates.
 */
public class TriggerManager {
    private static TransactedList<TriggerObject> triggers = new TransactedList<TriggerObject>();
    static int count = 0;


    // Code For Testing Only -Dishan

    public static void initTestTriggerStream() {
        final java.util.Timer timer1 = new java.util.Timer();
        timer1.schedule(new TimerTask() {
            @Override
            public void run() {
                TriggerRepresentation obj = new TriggerRepresentation();
                obj.setTriggerId(String.valueOf(count));
                obj.setTrigger(RandomStringGenerator.nextString());
                addTriggerToList(obj);
                count++;
            }


        }, 0, 500);

        final java.util.Timer timer2 = new java.util.Timer();
        timer2.schedule(new TimerTask() {
            @Override
            public void run() {
                removeTriggerFromList(triggers.get(0).gettriggerID());
            }
        }, 2000, 1500);

    }

    //AddTriggers through RMI to the shared memory

    public static boolean addTriggerToList(TriggerRepresentation tr) {
        boolean success = false;
        if (getTriggers() != null) {
            try {
                synchronized (triggers) {
                    if (Site.getLocal().getPendingCommitCount() < Site.MAX_PENDING_COMMIT_COUNT) {
                        Site.getLocal().allowThread();
                        Transaction transaction = Site.getLocal().startTransaction();
                        TriggerObject obj = new TriggerObject();
                        obj.settriggerID(tr.getTriggerId());
                        obj.settrigger(tr.getTrigger());
                        obj.setclientID(tr.getClientId());
                        obj.settriggerID(tr.getTriggerId());
                        triggers.add(obj);
                        transaction.commit();
                        success = true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return success;
    }

    public static boolean removeTriggerFromList(String triggerID) {
        boolean success = false;
        if (getTriggers() != null) {
            try {
                synchronized (triggers) {
                    if (Site.getLocal().getPendingCommitCount() < Site.MAX_PENDING_COMMIT_COUNT) {
                        Site.getLocal().allowThread();
                        for (int i = 0; i < triggers.size(); i++) {
                            if (triggers.get(i).gettriggerID().equalsIgnoreCase(triggerID)) {
                                Transaction transaction = Site.getLocal().startTransaction();
                                triggers.remove(i);
                                transaction.commit();
                                success = true;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return success;
    }


    public static TransactedList<TriggerObject> getTriggers() {
        return triggers;
    }

    public static void setTriggers(TransactedList<TriggerObject> triggers) {
        TriggerManager.triggers = triggers;
    }
}
