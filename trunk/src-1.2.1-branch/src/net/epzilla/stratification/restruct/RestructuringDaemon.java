package net.epzilla.stratification.restruct;

import org.epzilla.clusterNode.xml.XMLElement;
import org.epzilla.dispatcher.rmi.DispInterface;
import org.epzilla.dispatcher.rmi.RestructuringInfo;
import org.epzilla.leader.LeaderElectionInitiator;
import org.epzilla.util.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashSet;


public class RestructuringDaemon {
    private static boolean alive = false;
    private static boolean restructuring = false;
    public static int RESTRUCTURING_WAITING_TIME = 50000;
    public static int INITIAL_RESTRUCTURING_WAITING_TIME = 50000;
    private static Thread daemonThread = null;

    static {
        try {
            File f = new File("restructuring.xml");

            if (!f.exists()) {
                f = new File("restructuring.xml");
            }

            BufferedReader br = new BufferedReader(new FileReader(f));

            org.epzilla.clusterNode.xml.XMLElement xe = new org.epzilla.clusterNode.xml.XMLElement();
            StringBuilder sb = new StringBuilder("");
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            xe.parseString(sb.toString());
            ArrayList<XMLElement> ch = xe.getChildren();
            XMLElement e = ch.get(0);
            String init = e.getAttribute("INIT_WAIT");
            String wait = e.getAttribute("WAIT");

            int iinit = Integer.parseInt(init.trim());
            int iwait = Integer.parseInt(wait.trim());

            if (iwait > 0) {
                RESTRUCTURING_WAITING_TIME = iinit;
            }

            if (iinit > 0) {
                INITIAL_RESTRUCTURING_WAITING_TIME = iwait;
            }

            br.close();

        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println(INITIAL_RESTRUCTURING_WAITING_TIME);
        System.out.println(RESTRUCTURING_WAITING_TIME);
    }

    public static boolean isRestructuring() {
        return restructuring;
    }


    public static void setRestructuring(boolean restructuring) {
        RestructuringDaemon.restructuring = restructuring;
    }

    public static void start() {
        alive = true;
        daemonThread = new Thread() {
            public void run() {
                try {
                    Thread.sleep(RestructuringDaemon.INITIAL_RESTRUCTURING_WAITING_TIME);
                } catch (InterruptedException e) {
                    Logger.error("error sleeping", e);
                }

                while (alive) {
                    System.out.println("SystemRestructuring - starting....");
                    long st = System.currentTimeMillis();
                    try {

                        HashSet<String> disp = new HashSet<String>(LeaderElectionInitiator.getDispatchers());

                        for (String ip : disp) {
                            try {
                                String id = dispIdGen(ip);
                                String serviceName = "DISPATCHER_SERVICE" + id;
                                String url = "rmi://" + ip + "/" + serviceName;
                                DispInterface obj = (DispInterface) Naming.lookup(url);
                                RestructuringInfo ri = new RestructuringInfo();
                                obj.restructuringStarted(ri);
                            } catch (Exception e) {
                                Logger.error("error sending command", e);
                            }


                        }

                        SystemRestructure.getInstance().restructureSystem();
                        SystemRestructure.getInstance().sendRestructureCommands();


                        for (String ip : disp) {
                            try {
                                String id = dispIdGen(ip);
                                String serviceName = "DISPATCHER_SERVICE" + id;
                                String url = "rmi://" + ip + "/" + serviceName;
                                DispInterface obj = (DispInterface) Naming.lookup(url);
                                RestructuringInfo ri = new RestructuringInfo();
                                obj.restructuringEnded(ri);
                            } catch (NotBoundException e) {
                                Logger.error("error sending command", e);
                            }

                        }

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
        try {
            alive = false;
            daemonThread.interrupt();
        } catch (Exception e) {
            Logger.error("error trying to stop restructuring thread:", e);
        }
    }


}
