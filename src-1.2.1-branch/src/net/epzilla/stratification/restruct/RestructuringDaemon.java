package net.epzilla.stratification.restruct;

import org.epzilla.dispatcher.rmi.DispInterface;
import org.epzilla.leader.LeaderElectionInitiator;
import org.epzilla.util.Logger;

import java.rmi.Naming;
import java.util.HashSet;


public class RestructuringDaemon {
    private static boolean alive = false;
    private static boolean restructuring = false;
    public static int RESTRUCTURING_WAITING_TIME = 50000;


    public static boolean isRestructuring() {
        return restructuring;
    }


    public static void setRestructuring(boolean restructuring) {
        RestructuringDaemon.restructuring = restructuring;
    }

    public static void start() {
        alive = true;
        Thread t = new Thread() {
            public void run() {
                try {
                    Thread.sleep(RestructuringDaemon.RESTRUCTURING_WAITING_TIME);
                } catch (InterruptedException e) {
                    Logger.error("error sleeping", e);
                }

                while (alive) {
                    System.out.println("SystemRestructure....");
                    long st = System.currentTimeMillis();
                    try {

                        HashSet<String> disp = LeaderElectionInitiator.getDispatchers();

                        for (String ip : disp) {
                            String id = dispIdGen(ip);
                            String serviceName = "DISPATCHER_SERVICE" + id;
                            String url = "rmi://" + ip + "/" + serviceName;
                            DispInterface obj = (DispInterface) Naming.lookup(url);
                        }

                        SystemRestructure.getInstance().restructureSystem();
                        SystemRestructure.getInstance().sendRestructureCommands();
                    } catch (Exception e) {
                        Logger.error("Error: invalid syntax? ", e);

                    }
                    System.out.println("SystemRestructure ended....." + (System.currentTimeMillis() - st) + " ms");


                    try {
                        Thread.sleep(RestructuringDaemon.RESTRUCTURING_WAITING_TIME);
                    } catch (InterruptedException e) {
                        Logger.error("error sleeping", e);

                    }
                }
            }
        };
        t.start();
    }

    /*
    * generate dispatcher id
    */
    private static String dispIdGen(String addr) {
        String[] addrArray = addr.split("\\.");
        String temp = "";
        String value = "";
        for (int i = 0; i < addrArray.length; i++) {
            temp = addrArray[i].toString();
            while (temp.length() != 3) {
                temp = '0' + temp;
            }
            value += temp;
        }
        return value;
    }

    public static void stop() {
        alive = false;
    }


}
