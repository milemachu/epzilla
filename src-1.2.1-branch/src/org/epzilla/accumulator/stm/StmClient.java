package org.epzilla.accumulator.stm;

import jstm.core.*;
import jstm.transports.clientserver.ConnectionInfo;
import jstm.transports.clientserver.socket.SocketClient;
import org.epzilla.accumulator.Variables;
import org.epzilla.accumulator.generated.AccumulatorObjectModel;
import org.epzilla.accumulator.generated.SharedDerivedEvent;
import org.epzilla.accumulator.global.DerivedEvent;
import org.epzilla.util.Logger;
import java.io.IOException;
import java.util.Scanner;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Rajeev
 * Date: Mar 6, 2010
 * Time: 9:45:06 AM
 * To change this template use File | Settings | File Templates.
 */
public class StmClient {
    private static Share share;
    private static SocketClient client;

    public static void main(String[] args) throws IOException {
        start();
        STMAccess.getInstance().addDerivedEvent(new DerivedEvent());
        Scanner s = new Scanner(System.in);
        s.nextLine();

        STMAccess.getInstance().addDerivedEvent(new DerivedEvent());

    }

    public static void start() throws IOException {
        Site.getLocal().registerObjectModel(new AccumulatorObjectModel());
        client = new SocketClient(Variables.ip, Variables.port);
        ConnectionInfo connection = client.connect();

        Set<Share> shares = connection.getServerAndClients().getOpenShares();
        try {

            // todo improve this.
            Object[] transactedItems = ((Share) shares.toArray()[0]).toArray();
            try {
                Share s = ((Share) shares.toArray()[0]);
                s.addListener(new TransactedSet.Listener() {

                    public void onAdded(Transaction transaction, Object o) {
                        //To change body of implemented methods use File | Settings | File Templates.
                        Logger.log("client: new event added.");

                    }

                    public void onRemoved(Transaction transaction, Object o) {
                        //To change body of implemented methods use File | Settings | File Templates.
                        Logger.log("client: event removed.");
                    }
                });

            } catch (Exception e) {
                Logger.log("error adding listener.");
            }
            TransactedMap<String, TransactedObject> map = null;
            for (Object transactedItem : transactedItems) {
                if (transactedItem instanceof TransactedMap) {
                    STMAccess.clientMap = (TransactedMap<Integer, TransactedList<SharedDerivedEvent>>) transactedItem;
                    STMAccess.clientMap.addListener(new TransactedMap.Listener<Integer, TransactedList<SharedDerivedEvent>>() {


                        public void onPut(Transaction transaction, Integer integer, TransactedList<SharedDerivedEvent> sharedDerivedEvents) {
                            Logger.log("client: new event added.");
                        }

                        public void onRemoved(Transaction transaction, Integer integer) {
                            Logger.log("client: event removed.");
                        }
                    });
                    break;
                }
            }
        } catch (Exception e) {

        }
//        Logger.log("initial map size:" + STMAccess.clientMap.size());
    }
}
