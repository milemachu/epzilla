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
import jstm.transports.clientserver.Server;
import jstm.transports.clientserver.socket.SocketServer;
import org.epzilla.accumulator.Variables;
import org.epzilla.accumulator.generated.AccumulatorObjectModel;
import org.epzilla.accumulator.generated.SharedDerivedEvent;
import org.epzilla.accumulator.global.DerivedEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;
import org.epzilla.util.Logger;
//import com.sun.corba.se.spi.activation.Server;

/**
 * Created by IntelliJ IDEA.
 * User: Rajeev
 * Date: Mar 4, 2010
 * Time: 6:59:46 AM
 * To change this template use File | Settings | File Templates.
 */
public class StmServer {
    static Share share = null;

    public static void main(String[] args) throws IOException {
        StmServer ss = new StmServer();
        ss.start();

        DerivedEvent e = new DerivedEvent();
        e.setContent("first event");
        STMAccess.getInstance().addDerivedEvent(e);

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        br.readLine();
    }


    public static void start() throws IOException {
        Site.getLocal().registerObjectModel(new AccumulatorObjectModel());


        Server server = new SocketServer(Variables.port);
        server.start();

        Logger.log("server started.");
        share = new Share();
        TransactedMap<Integer, TransactedList<SharedDerivedEvent>> map = null;
        if (server.getServerAndClients().getOpenShares().size() == 0) {

            map = new TransactedMap<Integer, TransactedList<SharedDerivedEvent>>();

            Transaction transaction = Site.getLocal().startTransaction();

            server.getServerAndClients().getOpenShares().add(share);
            share.add(map);
            transaction.commit();
        }

        Set<Share> shares = server.getServerAndClients().getOpenShares();

        Object[] arr = ((Share) shares.toArray()[0]).toArray();
        for (Object o : arr) {
            if (o instanceof TransactedMap) {
                map = (TransactedMap<Integer, TransactedList<SharedDerivedEvent>>) o;
                break;
            }
        }

        STMAccess.clientMap = map;

        share
                .addListener(new TransactedSet.Listener<TransactedObject>() {

                    public void onAdded(Transaction transaction,
                                        TransactedObject object) {
                        Logger.log("New event added.");


                    }

                    public void onRemoved(Transaction transaction,
                                          TransactedObject object) {
                        Logger.log("event removed.");
                    }
                });
    }

    public void loadObjects() {
        if (Site.getLocal().getPendingCommitCount() < Site.MAX_PENDING_COMMIT_COUNT) {
            Site.getLocal().allowThread();
            Transaction transaction = Site.getLocal().startTransaction();
            transaction.commit();
            // todo
        }
    }

}
