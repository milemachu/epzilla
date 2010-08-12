/***************************************************************************************************
Copyright 2009 - 2010 Harshana Eranga Martin, Dishan Metihakwala, Rajeev Sampath, Chathura Randika

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
****************************************************************************************************/
package org.epzilla.accumulator.stm;

import jstm.core.*;
import jstm.transports.clientserver.socket.SocketClient;
import jstm.transports.clientserver.ConnectionInfo;


import java.util.Set;

import org.epzilla.accumulator.generated.AccumulatorObjectModel;
import org.epzilla.accumulator.generated.LeaderInfoObject;
import org.epzilla.accumulator.userinterface.AccumulatorUIControler;
import org.epzilla.accumulator.dataManager.ClusterLeaderManager;

/**
 * Created by IntelliJ IDEA.
 * User: Dishan
 * Date: May 18, 2010
 * Time: 1:33:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class AccumulatorAsClient {
    private static Share share;
    private static SocketClient client;

    public static boolean startClient() {
        boolean success = false;
        AccumulatorUIControler.appendEventResults("Starting STM Client....");
        Site.getLocal().registerObjectModel(new AccumulatorObjectModel());
        try {
            client = new SocketClient("127.0.0.1", 4444);
            ConnectionInfo connection = client.connect();
            AccumulatorUIControler.appendEventResults("Client Started Successfully.... ");
            AccumulatorUIControler.appendEventResults("Connected to server: ");
            share = new Share();

            AccumulatorUIControler.appendEventResults("Number of Open Shares: "
                    + String.valueOf(connection.getServerAndClients()
                    .getOpenShares().size()));
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
            AccumulatorUIControler.appendEventResults("Attempt to Start Client Failed... ");
            AccumulatorUIControler.appendEventResults(e2.getMessage());
        }


        return success;
    }

    private static void addList(final TransactedList<?> info) {

        if (info.get(0) instanceof LeaderInfoObject) {
            addIpList((TransactedList<LeaderInfoObject>) info);
        }

    }

    private static void addIpList(final TransactedList<LeaderInfoObject> info) {

        AccumulatorUIControler.appendAccumulatorStatus("IPList added.");
        ClusterLeaderManager.setDetailsList(info);
        info.addListener(new FieldListener() {
            public void onChange(Transaction transaction, int i) {
//                DispatcherUIController.appendIP("IP added to List: " + ClusterLeaderIpListManager.getIpList().get(i));
//                ClusterLeaderIpListManager.printIPList();
            }
        });

    }


}
