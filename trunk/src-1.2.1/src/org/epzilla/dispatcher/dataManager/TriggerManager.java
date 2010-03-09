package org.epzilla.dispatcher.dataManager;

import jstm.core.TransactedList;
import jstm.core.Site;
import jstm.core.Transaction;


import java.util.TimerTask;

import org.epzilla.dispatcher.dispatcherObjectModel.TriggerInfoObject;
import org.epzilla.dispatcher.RandomStringGenerator;
import org.epzilla.dispatcher.controlers.*;

/**
 * Created by IntelliJ IDEA.
 * User: Dishan
 * Date: Feb 20, 2010
 * Time: 8:57:24 AM
 * To change this template use File | Settings | File Templates.
 */
public class TriggerManager {

    private static TransactedList<TriggerInfoObject> triggers = new TransactedList<TriggerInfoObject>();
    static int count = 0;

    // Code For Testing Only -Dishan
    public static void initTestTriggerStream() {
        final java.util.Timer timer1 = new java.util.Timer();
        timer1.schedule(new TimerTask() {
            @Override
            public void run() {
                if (getTriggers() != null) {

                    if (Site.getLocal().getPendingCommitCount() < Site.MAX_PENDING_COMMIT_COUNT) {
                        Site.getLocal().allowThread();
                        Transaction transaction = Site.getLocal().startTransaction();
                        TriggerInfoObject obj = new TriggerInfoObject();
                        obj.settriggerID(String.valueOf(count));
                        obj.settrigger(RandomStringGenerator.nextString());
                        getTriggers().add(obj);
                        transaction.commit();
                    }
                    count++;

                    if (count == 20)
                        timer1.cancel();
                }
            }
        }, 0, 50);
    }

    //AddTriggers through RMI to the shared memory
    public static boolean addTriggerToList(byte[] trigger) {
        boolean success = false;
        if (getTriggers() != null) {
            if (Site.getLocal().getPendingCommitCount() < Site.MAX_PENDING_COMMIT_COUNT) {
                Site.getLocal().allowThread();
                Transaction transaction = Site.getLocal().startTransaction();
                TriggerInfoObject obj = new TriggerInfoObject();
                // ID is the sequential number of the trigger
                obj.settriggerID("TID:" + String.valueOf(count));
                obj.settrigger(new String(trigger));
                getTriggers().add(obj);
                transaction.commit();
                success = true;
                System.out.println(new String(trigger));
            }
            count++;
        }
        return success;
    }

    public static TransactedList<TriggerInfoObject> getTriggers() {
        return triggers;
    }

    public static void setTriggers(TransactedList<TriggerInfoObject> triggers) {
        TriggerManager.triggers = triggers;
    }
}
