package org.epzilla.accumulator.stm;

import jstm.core.*;
import jstm.transports.clientserver.Server;
import jstm.transports.clientserver.socket.SocketServer;
import static org.epzilla.accumulator.stm.STMAccess.*;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Set;

import org.epzilla.accumulator.Variables;
import org.epzilla.accumulator.generated.AccumulatorObjectModel;
import org.epzilla.accumulator.generated.EventMapMarker;
import org.epzilla.accumulator.generated.SharedDerivedEvent;
import org.epzilla.accumulator.generated.StructureMarker;
import org.epzilla.accumulator.global.DerivedEvent;

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

        System.out.println("server started.");
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
                        System.out.println("New event added.");


                    }

                    public void onRemoved(Transaction transaction,
                                          TransactedObject object) {
                        System.out.println("event removed.");
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
