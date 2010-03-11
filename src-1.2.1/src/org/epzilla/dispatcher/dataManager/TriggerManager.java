package org.epzilla.dispatcher.dataManager;

import jstm.core.TransactedList;
import jstm.core.Site;
import jstm.core.Transaction;


import java.util.TimerTask;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import org.epzilla.dispatcher.dispatcherObjectModel.TriggerInfoObject;
import org.epzilla.dispatcher.RandomStringGenerator;
import org.epzilla.dispatcher.clusterHandler.TriggerSender;

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
    static String currentIP = "localhost";
    static String current1IP = " ";
    static String current2IP = " ";

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


    public static void sendTriggersToclusters(byte[] trigger) {
        if (currentIP == "localhost") {
            currentIP = current1IP;
        }

        if (currentIP == current1IP) {
            try {
                TriggerSender.acceptTrigger(currentIP,"001",trigger);
            } catch (MalformedURLException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (NotBoundException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (RemoteException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            currentIP = current2IP;
        } else if (currentIP == current2IP) {
                        try {
                TriggerSender.acceptTrigger(currentIP,"002",trigger);
            } catch (MalformedURLException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (NotBoundException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (RemoteException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            currentIP = current1IP;
        }


    }

}
