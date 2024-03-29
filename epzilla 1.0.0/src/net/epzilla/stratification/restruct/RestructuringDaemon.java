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


/**
 * runs in leader dispatcher only.
 * responsible for periodically redistributing the query base in order to properly organize the system.
 */
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


    /**
     * indicates whether the daemon is currently doing restructuring.
     * @return
     */
    public static boolean isRestructuring() {
        return restructuring;
    }


    /**
     * sets the indicator that daemon is currently restructuring.
     * @param restructuring
     */
    public static void setRestructuring(boolean restructuring) {
        RestructuringDaemon.restructuring = restructuring;
    }

    public static void start() {
        stop();  // make sure no two threads running
        alive = true;
        daemonThread = new Thread() {
            public void run() {
                try {
                    Thread.sleep(RestructuringDaemon.INITIAL_RESTRUCTURING_WAITING_TIME);
                } catch (InterruptedException e) {
                    Logger.error("error sleeping", e);
                }

                while (alive) {
                    restructuring = true;
                    forceRestructuring();
                    restructuring = false;
                    try {
                        Thread.sleep(RestructuringDaemon.RESTRUCTURING_WAITING_TIME);
                    } catch (InterruptedException e) {
                        Logger.error("error sleeping", e);

                    }
                }
            }
        };
        daemonThread.start();
    }

    /**
     * force query redistribution. used when leader goes down while restructuring happens.
     */
    public static void forceRestructuring() {
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
                 restructuring = true;
            try {
                SystemRestructure.getInstance().restructureSystem();
                SystemRestructure.getInstance().sendRestructureCommands();
            } catch (Exception re) {
                Logger.error("", re);
            }

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

            restructuring = false;
        } catch (Exception e) {
            Logger.error("Error: invalid syntax? ", e);

        }
        System.out.println("SystemRestructure ended....." + (System.currentTimeMillis() - st) + " ms");

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

    /**
     * stops the daemon.
     */
    public static void stop() {
        try {
            alive = false;
            if (daemonThread != null) {
                daemonThread.interrupt();
            }

        } catch (Exception e) {
            Logger.error("error trying to stop restructuring thread:", e);

        } finally {
            restructuring = false;
        }
    }


}
