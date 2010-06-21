package org.epzilla.dispatcher;

import net.epzilla.stratification.restruct.RestructuringDaemon;
import org.epzilla.dispatcher.controlers.DispatcherUIController;
import org.epzilla.dispatcher.controlers.MainDispatcherController;
import org.epzilla.dispatcher.dataManager.NodeVariables;
import org.epzilla.dispatcher.loadAnalyzer.CpuMemAnalyzer;
import org.epzilla.dispatcher.rmi.DispImpl;
import org.epzilla.dispatcher.rmi.DispInterface;
import org.epzilla.dispatcher.rmi.OpenSecurityManager;
import org.epzilla.leader.LeaderElectionInitiator;
import org.epzilla.util.Logger;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RemoteException;

/**
 * Created by IntelliJ IDEA.
 * User: chathura
 * Date: Jun 16, 2010
 * Time: 5:36:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class DispatcherStartup {
    private static String serviceName = "DISPATCHER_SERVICE";
    private static String STMserverIP = "127.0.0.1";

    private void bindDispatcher(String serviceName) throws RemoteException, UnknownHostException, MalformedURLException {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new OpenSecurityManager());
        }
        DispInterface dispInt = new DispImpl();
        InetAddress inetAddress;
        inetAddress = InetAddress.getLocalHost();

        String ipAddress = inetAddress.getHostAddress();
        NodeVariables.setNodeIP(ipAddress);
        String id = dispIdGen(ipAddress);
        String disServiceName = serviceName + id;
        String name = "rmi://" + ipAddress + "/" + disServiceName;
        Naming.rebind(name, dispInt);
    }

    /*
      * generate dispatcher id
      */

    private String dispIdGen(String addr) {
        String[] addrArray = addr.split("\\.");
        String temp = "";
        String value = "";
        for (int i = 0; i < addrArray.length; i++) {
            temp = addrArray[i];
            while (temp.length() != 3) {
                temp = '0' + temp;
            }
            value += temp;
        }
        return value;
    }

    public static void main(String[] args) {
        try {
            DispatcherStartup main = new DispatcherStartup();
            main.bindDispatcher(serviceName);

            //added leader election init
            LeaderElectionInitiator.mainMethod();


//        LeaderElectionInitiator.mainMethod();
            DispatcherUIController.InitializeUI();
            InitSTM();


            CpuMemAnalyzer.Initialize();
            //To run dispatcher as STM client
            //MainDispatcherController.runAsClient();
            //Logger.log("running as client...");


        } catch (Exception e) {
            Logger.error("", e);
        }
    }

    public static void InitSTM() {
        String leader = "";
        while (leader.equalsIgnoreCase("")) {
            leader = LeaderElectionInitiator.getLeader();

        }
        if (leader.equalsIgnoreCase(NodeVariables.getNodeIP())) {

            //To run as Dispatcher as STM server
            DispatcherUIController.setUIVisibility(true);
            MainDispatcherController.runAsServer();
            Logger.log("running as server...");
            try {
                if (RestructuringDaemon.isRestructuring()) {
                    RestructuringDaemon.forceRestructuring();
                }
            } catch (Exception e) {
                Logger.error("", e);
            }

            RestructuringDaemon.start();

        } else {

            DispatcherUIController.setUIVisibility(true);
            NodeVariables.setCurrentServerIP(leader);
            MainDispatcherController.runAsClient();
        }
    }

    public static boolean triggerLEFromRemote() {
        return LeaderElectionInitiator.initiateLeaderElection();
    }
}
