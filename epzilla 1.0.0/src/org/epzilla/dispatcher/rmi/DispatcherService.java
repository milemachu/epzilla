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
package org.epzilla.dispatcher.rmi;

import net.epzilla.stratification.restruct.RestructuringDaemon;
import org.epzilla.dispatcher.controlers.DispatcherUIController;
import org.epzilla.dispatcher.controlers.MainDispatcherController;
import org.epzilla.dispatcher.dataManager.NodeVariables;
import org.epzilla.dispatcher.loadAnalyzer.CpuMemAnalyzer;
import org.epzilla.leader.LeaderElectionInitiator;
import org.epzilla.util.Logger;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RemoteException;

/**
 * Created by IntelliJ IDEA.
 * This class is to bind the Dispatcher to its RMI registry
 * Initialize the Leader Election process
 * Author: Chathura
 * Date: Jan 31, 2010
 * Time: 11:34:09 AM
 * To change this template use File | Settings | File Templates.
 */
public class DispatcherService {

    private static String serviceName = "DISPATCHER_SERVICE";
    private static String STMserverIP = "127.0.0.1";

    public DispatcherService() {

    }
    /*
    * bind the dispatcher to its own RMI registry
    */

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

    public static boolean triggerLEFromRemote() {
        return LeaderElectionInitiator.initiateLeaderElection();
    }

    public static void main(String[] args) {

        try {
            DispatcherService service = new DispatcherService();
            service.bindDispatcher(serviceName);

            //added leader election init
            LeaderElectionInitiator.mainMethod();


//        LeaderElectionInitiator.mainMethod();
            String leader = "";
            while (leader.equalsIgnoreCase("")) {
                leader = LeaderElectionInitiator.getLeader();

            }
            if (leader.equalsIgnoreCase(NodeVariables.getNodeIP())) {

                //To run as Dispatcher as STM server
                DispatcherUIController.InitializeUI();
                MainDispatcherController.runAsServer();
                Logger.log("running as server...");
                RestructuringDaemon.start();

            } else {
                DispatcherUIController.InitializeUI();
                NodeVariables.setCurrentServerIP(leader);
                MainDispatcherController.runAsClient();
            }


            CpuMemAnalyzer.Initialize();
            //To run dispatcher as STM client
            //MainDispatcherController.runAsClient();
            //Logger.log("running as client...");

//            ApproximateDispatcher ad = new ApproximateDispatcher();
//            ArrayList<TriggerInfoObject> tlist = new ArrayList();
//
//            TriggerInfoObject tio = new TriggerInfoObject();
//            tio.settrigger("SELECT avg(StockTrades.price), min(StockPrices.price) RETAIN 10 EVENTS OUTPUT StkTrades.avgprice, StkTrades.minprice;");
//            tlist.add(tio);
//            tio = new TriggerInfoObject();
//            tio.settrigger("SELECT sum(TDWLTrades.trades) RETAIN 10 MINUTES noSliding OUTPUT TDTrades.sumtrades;");
//            tlist.add(tio);
//
//            tio.settrigger("SELECT avg(MobitelStocks.stockPrice) RETAIN 20 EVENTS OUTPUT MobiStoks.avgprice;");
//            tlist.add(tio);
//
//            ad.assignClusters(tlist, 8);
//            for (TriggerInfoObject x: tlist) {
//                Logger.log("str:cls:");
//                Logger.log(x.getstratumId());
//                Logger.log(x.getclusterID());
//
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
