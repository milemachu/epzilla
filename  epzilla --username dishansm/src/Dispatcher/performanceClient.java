package Dispatcher;

import generated.PerformanceTesterObjectModel;
import generated.TestObject;
import jstm.core.*;
import jstm.core.Transaction.AbortedException;
import jstm.transports.clientserver.ConnectionInfo;
import jstm.transports.clientserver.socket.SocketClient;
import jstm.misc.Console;

import java.io.IOException;
import java.util.Random;
import java.util.Set;

public class performanceClient {
    private Share share;
    private TestObject object;
    private int changeCount;
    long start;
    long stop;

    public static void main(String[] args) {
        performanceClient main = new performanceClient();
        main.run();
    }

    private void run() {
        Site.getLocal().registerObjectModel(new PerformanceTesterObjectModel());
        try {
            SocketClient client = new SocketClient("localhost", 4444);
            ConnectionInfo connection = client.connect();

            System.out.println("Connected to server: "
                    + connection.getServer().toString());

            share = new Share();

            System.out.println("Number of Open Shares: "
                    + String.valueOf(connection.getServerAndClients()
                    .getOpenShares().size()));
            // Once connected, retrieve the Group that represents the
            // server and its
            // clients

            Set<Share> shares = connection.getServerAndClients()
                    .getOpenShares();

            // Open a share in this group is there is none yet

            share = (Share) shares.toArray()[0];


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

            // The share might already contain images, show them

            for (TransactedObject o : share) {
                if (o instanceof TestObject)
                    addInfo((TestObject) o);

            }

        } catch (AbortedException e2) {
            e2.printStackTrace();
        } catch (IOException e3) {

            e3.printStackTrace();
        }
        Console.readLine();
    }

    private void addInfo(final TestObject info) {
        System.out.println("Object Added To Share... ");

        object = info;
        info.addListener(new FieldListener() {

            public void onChange(Transaction transaction, int i) {
                if (changeCount == 0)
                    start = System.currentTimeMillis();

                changeCount++;
            }
        });

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
        System.exit(0);
    }

}
