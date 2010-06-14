package org.epzilla.dispatcher.dataManager;

import jstm.core.Site;
import jstm.core.TransactedList;
import jstm.core.Transaction;
import net.epzilla.stratification.dynamic.ApproximateDispatcher;
import net.epzilla.stratification.query.InvalidSyntaxException;
import org.epzilla.dispatcher.RandomStringGenerator;
import org.epzilla.dispatcher.clusterHandler.TriggerSender;
import org.epzilla.dispatcher.dispatcherObjectModel.TriggerInfoObject;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.TimerTask;

import org.epzilla.dispatcher.rmi.TriggerRepresentation;
import org.epzilla.util.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Dishan
 * Date: Feb 20, 2010
 * Time: 8:57:24 AM
 * To change this template use File | Settings | File Templates.
 */
public class TriggerManager {

    private static TransactedList<TriggerInfoObject> triggers = new TransactedList<TriggerInfoObject>();
    private static Object triggerIdSyncLock = new Object();

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

    public static boolean addTriggerToList(String trigger, String clientID) {
        boolean success = false;
        if (getTriggers() != null) {
            synchronized (triggerIdSyncLock) {

                if (Site.getLocal().getPendingCommitCount() < Site.MAX_PENDING_COMMIT_COUNT) {
                    Site.getLocal().allowThread();
                    Transaction transaction = Site.getLocal().startTransaction();
                    TriggerInfoObject obj = new TriggerInfoObject();
                    // ID is the sequential number of the trigger
//                    obj.settriggerID("TID:" + String.valueOf(count));
                    obj.settriggerID( String.valueOf(count));
                    obj.setclientID(clientID);
                    obj.settrigger(new String(trigger));
                    getTriggers().add(obj);

                    // TODO - modify to do structuring...
                    sendTriggersToclusters(trigger);
                    transaction.commit();
                    success = true;
                    Logger.log(trigger);
                }

                if (success) {
                    count++;
                }
            }
        }
        return success;
    }

    public static boolean addAllTriggersToList(List<String> triggerList, String clientID) throws InvalidSyntaxException {
        boolean success = false;
//        ApproximateDispatcher ad = new ApproximateDispatcher();
        if (getTriggers() != null) {
            synchronized (triggerIdSyncLock) {
                int tempCount = count;
                if (Site.getLocal().getPendingCommitCount() < Site.MAX_PENDING_COMMIT_COUNT) {
//                    Site.getLocal().allowThread();
//                    Transaction transaction = Site.getLocal().startTransaction();
                    ArrayList<TriggerInfoObject> tio = new ArrayList();
                    for (String trigger : triggerList) {
                        TriggerInfoObject obj = new TriggerInfoObject();
                        // ID is the sequential number of the trigger
                        obj.settriggerID( String.valueOf(tempCount));
//                        obj.settriggerID("TID:" + String.valueOf(tempCount));
                        obj.setclientID(clientID);
                        obj.settrigger(new String(trigger));
                        obj.setdispatcherId(NodeVariables.getDispatcherId());
                        tempCount++;
                        tio.add(obj);

                        // TODO - modify to do correct structuring...

                    }



//                    ad.assignClusters(tio, clientID);
                    long start = System.currentTimeMillis();
                    ApproximateDispatcher.getInstance().assignClusters(tio, clientID);
                    System.out.println("assigning clusters time: " + (System.currentTimeMillis() - start));
                    count = count + tio.size();
                    Site.getLocal().allowThread();
                    Transaction transaction = Site.getLocal().startTransaction();
                    getTriggers().addAll(tio);
                    transaction.commit();
                    
                    // todo send.

                    ArrayList<String> ips = ClusterLeaderIpListManager.getClusterIpList();
                    Logger.log("getting ip list size:  "+ips.size());
                    Hashtable<String, ArrayList<TriggerRepresentation>> ht = new Hashtable();

                    for (TriggerInfoObject tx : tio) {
                        String id = tx.getstratumId() + ":" + tx.getclusterID();
                        ArrayList<TriggerRepresentation> list = ht.get(id);
                        if (list == null) {
                            list = new ArrayList<TriggerRepresentation>();
                            ht.put(id, list);
                        }
                        TriggerRepresentation tr = new TriggerRepresentation();
                        tr.setTrigger(tx.gettrigger());
                        tr.setTriggerId(tx.gettriggerID());
                        tr.setClientId(tx.getclientID());
                        list.add(tr);
                    }

                    for (TriggerInfoObject tx : tio) {
//                        sendTriggersToclusters(tx.gettrigger());
                        Logger.log(tx.gettrigger());
                        Logger.log(tx.getclusterID());

                    }


                   for (String key: ht.keySet()) {
                       ArrayList<TriggerRepresentation> lis = ht.get(key);
                       if (lis.size() > 0) {
                            String cl = key.split(":")[1];
                           // todo assign clusters properly for system variables.

                           try {
                               String ip =  ips.get(Integer.parseInt(cl));
                               TriggerSender.acceptTrigger(ip, "x", lis, clientID);
                           } catch (NumberFormatException e) {
                               
                               e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                           } catch (RemoteException e) {
                               e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                           } catch (MalformedURLException e) {
                               e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                           } catch (NotBoundException e) {
                               e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                           }
                       }

                   }

                    success = true;
//                    Logger.log(new String(trigger));
                }
                if (success) {
                    count = tempCount;
                }
            }
        }
        return success;
    }


    public static TransactedList<TriggerInfoObject> getTriggers() {
        return triggers;
    }

    public static void setTriggers(TransactedList<TriggerInfoObject> triggers) {
        TriggerManager.triggers = triggers;
    }

    // get the Cluster leader IP list >> ClusterLeaderIpListManager.getIpList()
    //  and send them the triggers
    // the algo fr sending allocating triggers to clusters should be run here

    public static void sendTriggersToclusters(String trigger) {
//        if (currentIP == "localhost") {
//            current1IP = ClusterLeaderIpListManager.getIpList().get(1).getleaderIP();
//            current2IP = ClusterLeaderIpListManager.getIpList().get(2).getleaderIP();
//            currentIP = current1IP;
//        }
//
//        if (currentIP == current1IP) {
//            try {
//                TriggerLog.writeTolog(currentIP, "001", trigger);    //trigger log
//                TriggerSender.acceptTrigger(currentIP, "001", trigger);
//            } catch (MalformedURLException e) {
//                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//            } catch (NotBoundException e) {
//                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//            } catch (RemoteException e) {
//                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//            }
//            currentIP = current2IP;
//        } else if (currentIP == current2IP) {
//            try {
//                TriggerLog.writeTolog(currentIP, "002", trigger);  //trigger log
//                TriggerSender.acceptTrigger(currentIP, "002", trigger);
//            } catch (MalformedURLException e) {
//                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//            } catch (NotBoundException e) {
//                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//            } catch (RemoteException e) {
//                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//            }
//            currentIP = current1IP;
//        }


    }

}
