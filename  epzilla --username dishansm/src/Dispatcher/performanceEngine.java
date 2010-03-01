package Dispatcher;

import generated.PerformanceTesterObjectModel;
import generated.TestObject;
import jstm.core.*;
import jstm.misc.Console;
import jstm.transports.clientserver.Server;
import jstm.transports.clientserver.socket.SocketServer;


import java.io.IOException;

public class performanceEngine {
    private TestObject object;
    Share share;
    private int changeCount = 0;
    long start = 0;
    long stop = 0;

    public static void main(String[] args) throws IOException {
        performanceEngine main = new performanceEngine();
        main.run();
    }

    private void run() throws IOException {
        Site.getLocal().registerObjectModel(new PerformanceTesterObjectModel());

        System.out.println("Starting server");

        Server server = new SocketServer(4444);
        server.start();

        System.out.println("Attaching a share to sites group: server and clients");

        share = new Share();
        server.getServerAndClients().getOpenShares().add(share);


        // Log all modifications happening to the local site
        share.addListener(new TransactedSet.Listener<TransactedObject>() {

            public void onAdded(Transaction transaction,
                                TransactedObject object) {
                if (object instanceof TestObject)
                    addInfo((TestObject) object);
            }

            public void onRemoved(Transaction transaction,
                                  TransactedObject object) {
                if (object instanceof TestObject)
                    RemoveInfo((TestObject) object);
            }
        });
//        Site.getLocal().addInternalListener(new Logger());
        if (Site.getLocal().getPendingCommitCount() < Site.MAX_PENDING_COMMIT_COUNT) {
            Site.getLocal().allowThread();
            Transaction transaction = Site.getLocal().startTransaction();
            TestObject info = new TestObject();
            info.setName("Object1");
            share.add(info);
            transaction.commit();
        }
        System.out.println("Press Enter to Start modifying shared object...");
        Console.readLine();
        modifyobject();

    }

    private void addInfo(final TestObject info) {
        System.out.println("Object Added To Share... ");
        object = info;
        info.addListener(new FieldListener() {

            public void onChange(Transaction transaction, int i) {
                changeCount++;
                if (i == TestObject.NAME_INDEX) {

                }

            }
        });

    }


    private void modifyobject() {
        objectEngine eng = new objectEngine(1000);
        start = System.currentTimeMillis();
        for (int j = 0; j < 1000; j++) {

            if (Site.getLocal().getPendingCommitCount() < Site.MAX_PENDING_COMMIT_COUNT) {
                Site.getLocal().allowThread();
                Transaction transaction = Site.getLocal().startTransaction();
                object.setName(eng.getRandomString());
                transaction.commit();
            }
        }
        share.remove(object);
    }

    private void RemoveInfo(final TestObject info) {
        System.out.println("Object Removed From Share... ");
        System.out.println("Object change count: " + String.valueOf(changeCount));
        stop = System.currentTimeMillis();
        System.out.println("Time in Milliseconds: " + (stop - start));
        System.out.println("Transction/sec: " + (float) (changeCount / (float) ((stop - start))) * 1000);
        changeCount = 0;
        start = 0;
        stop = 0;

    }

}
