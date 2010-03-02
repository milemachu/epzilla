package org.epzilla.dispatcher;

import generatedObjectModels.triggerInfoObject;
import jstm.core.TransactedList;
import jstm.core.Site;
import jstm.core.Transaction;
import org.epzilla.dispatcher.RandomStringGenerator;

import java.util.Random;
import java.util.TimerTask;

/**
 * Created by IntelliJ IDEA.
 * User: Dishan
 * Date: Feb 20, 2010
 * Time: 8:57:24 AM
 * To change this template use File | Settings | File Templates.
 */
public class TriggerManager {

    public static TransactedList<triggerInfoObject> triggers = new TransactedList<triggerInfoObject>();
    static int  count = 0;

    // Code For Testing Only -Dishan
    public static void acceptTriggerStream() {
        final java.util.Timer timer1 = new java.util.Timer();
        timer1.schedule(new TimerTask() {
            @Override
            public void run() {
                if (triggers != null) {

                    if (Site.getLocal().getPendingCommitCount() < Site.MAX_PENDING_COMMIT_COUNT) {
                        Site.getLocal().allowThread();
                        Transaction transaction = Site.getLocal().startTransaction();
                        triggerInfoObject obj = new triggerInfoObject();
                        obj.settriggerID(String.valueOf(count));
                        obj.settrigger(RandomStringGenerator.nextString());
                        triggers.add(obj);
                        transaction.commit();
                    }
                    count++;

                    if (count == 200)
                        timer1.cancel();
                }
            }
        }, 0, 50);
    }

    //AddTriggers through RMI to the shared memory
    public static boolean addTriggerToList(byte[] trigger) {
        boolean success = false;
        if (triggers != null) {
            if (Site.getLocal().getPendingCommitCount() < Site.MAX_PENDING_COMMIT_COUNT) {
                Site.getLocal().allowThread();
                Transaction transaction = Site.getLocal().startTransaction();
                triggerInfoObject obj = new triggerInfoObject();
                // ID is the sequential number of the trigger
                obj.settriggerID("TID:" + String.valueOf(count));
                obj.settrigger(new String(trigger));
                triggers.add(obj);
                transaction.commit();
                success = true;
            }
            count++;
        }
        return success;
    }

}
