package net.epzilla.stratification.immediate;

import jstm.core.TransactedList;
import org.epzilla.dispatcher.controlers.MainDispatcherController;
import org.epzilla.dispatcher.dispatcherObjectModel.TriggerDependencyStructure;
import org.epzilla.dispatcher.dispatcherObjectModel.TriggerInfoObject;
import org.epzilla.dispatcher.rmi.DispatcherService;
import org.epzilla.dispatcher.sharedMemoryModule.DispatcherAsServer;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: Rajeev
 * Date: May 12, 2010
 * Time: 8:36:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImmediateUnitTester {
    public static void main(String[] args) {
//        DispatcherAsServer.startServer();
//        DispatcherService.main(null);
//        MainDispatcherController.runAsServer();
        SystemVariables.setNumStrata(3);
        SystemVariables.setClusters(0, 2);
        SystemVariables.setClusters(1, 3);
        SystemVariables.setClusters(2, 1);

        TriggerDependencyStructure t = DynamicDependencyManager.getInstance().getDependencyStructure(10);
//        System.out.println(t.getstructure().size());
//        for (Object tl : t.getstructure()) {
//            TransactedList tlist = (TransactedList) tl;
//            System.out.println("tl size:" + tlist.size());
//        }
//
//        TriggerInfoObject tt = new TriggerInfoObject();
//        tt.settrigger("");

    }

    

}
