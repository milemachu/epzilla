package org.epzilla.clusterNode.sharedMemory;

import jstm.core.*;
import jstm.transports.clientserver.socket.SocketClient;
import jstm.transports.clientserver.ConnectionInfo;


import java.util.Set;
import java.util.TimerTask;

import org.epzilla.clusterNode.clusterInfoObjectModel.ClusterObjectModel;
import org.epzilla.clusterNode.clusterInfoObjectModel.TriggerObject;
import org.epzilla.clusterNode.clusterInfoObjectModel.NodeIPObject;
import org.epzilla.clusterNode.NodeController;
import org.epzilla.clusterNode.TriggerManager;
import org.epzilla.clusterNode.ClusterIPManager;

/**
 * Created by IntelliJ IDEA.
 * User: Dishan
 * Date: Mar 7, 2010
 * Time: 7:55:08 AM
 * To change this template use File | Settings | File Templates.
 */
public class NodeAsNonLeader {
    private static Share share;
       private static SocketClient client;

       public static boolean startClient() {
           boolean success = false;
//           DispatcherUIController.appendTextToStatus("Starting STM Client on: " + NodeVariables.getNodeIP());
           Site.getLocal().registerObjectModel(new ClusterObjectModel());
           try {
               client = new SocketClient(NodeController.getLeaderIP(), NodeController.getPort());
               ConnectionInfo connection = client.connect();
//               DispatcherUIController.appendTextToStatus("Client Started Successfully.... ");
//               DispatcherUIController.appendTextToStatus("Connected to server: " + connection.getServer().getObjectModelUID().toString());
               share = new Share();

//               DispatcherUIController.appendTextToStatus("Number of Open Shares: "
//                       + String.valueOf(connection.getServerAndClients()
//                       .getOpenShares().size()));
               // Once connected, retrieve the Group that represents the
               // server and its
               // clients

               Set<Share> shares = connection.getServerAndClients()
                       .getOpenShares();

               // Open a share in this group is there is none yet

               share = (Share) shares.toArray()[0];

               share
                       .addListener(new TransactedSet.Listener<TransactedObject>() {

                           public void onAdded(Transaction transaction,
                                               TransactedObject object) {

                               if (object instanceof TransactedList<?>)
                                   addList((TransactedList<?>) object);

                           }

                           public void onRemoved(Transaction transaction,
                                                 TransactedObject object) {

                           }
                       });

               // The share might already contain Lists add them to local memory

               for (TransactedObject o : share) {

                   if (o instanceof TransactedList<?>) {
                       addList((TransactedList<?>) o);
                   }

               }
               success = true;
           } catch (Exception e2) {
//               DispatcherUIController.appendTextToStatus("Attempt to Start Client Failed... ");
//               DispatcherUIController.appendTextToStatus(e2.getMessage());
           }


           return success;
       }

       private static void addList(final TransactedList<?> info) {

           if (info.get(0) instanceof TriggerObject) {
               addTriggerList((TransactedList<TriggerObject>) info);
           }
           if (info.get(0) instanceof NodeIPObject) {
              addIpList((TransactedList<NodeIPObject>) info);
           }
       }

       private static void addTriggerList(final TransactedList<TriggerObject> info) {
//           DispatcherUIController.appendTextToStatus("TriggerList added.");
           TriggerManager.setTriggers(info);
           info.addListener(new FieldListener() {
               public void onChange(Transaction transaction, int i) {
//                   DispatcherUIController.appendTrigger(String.valueOf(TriggerManager.getTriggers().get(TriggerManager.getTriggers().size() - 1).gettrigger()));
               }
           });
       }



       private static void addIpList(final TransactedList<NodeIPObject> info) {

//           DispatcherUIController.appendTextToStatus("IPList added.");
           ClusterIPManager.setIpList(info);
           info.addListener(new FieldListener() {
               public void onChange(Transaction transaction, int i) {
//                   DispatcherUIController.appendIP("IP added to List: " + ClusterLeaderIpListManager.getIpList().get(i));
               }
           });

       }


       public static void checkServerStatus() {
           final java.util.Timer timer1 = new java.util.Timer();
           timer1.schedule(new TimerTask() {
               @Override
               public void run() {
                   if (client.getStatus() == SocketClient.Status.DISCONNECTED) {
//                     DispatcherUIController.appendTextToStatus("Server Status..." + client.getStatus().toString());
                     this.cancel();
                   }
               }
           }, 10, 1000);
       }


}
