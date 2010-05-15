package net.epzilla.stratification.immediate;

import jstm.core.Share;
import jstm.core.TransactedList;
import jstm.core.TransactedSet;
import org.epzilla.dispatcher.dispatcherObjectModel.TriggerDependencyStructure;

import java.util.Hashtable;

/**
 * Created by IntelliJ IDEA.
 * User: Rajeev
 * Date: May 12, 2010
 * Time: 10:21:41 AM
 * To change this template use File | Settings | File Templates.
 */
public class DynamicDependencyManager {
    private static DynamicDependencyManager instance = new DynamicDependencyManager();
    private Hashtable<Integer, TriggerDependencyStructure> table = new Hashtable();
    private static Share dependencyShare = null;

    private DynamicDependencyManager() {

    }

    public static Share getDependencyShare() {
        return dependencyShare;
    }

    public static void setDependencyShare(Share dependencyShare) {
        DynamicDependencyManager.dependencyShare = dependencyShare;
    }

    public static DynamicDependencyManager getInstance() {
        return instance;
    }

    public void addTriggerDependencyStructure(int clientId, TriggerDependencyStructure tds) {
        this.table.put(clientId, tds);
    }

    public TriggerDependencyStructure getDependencyStructure(int clientId) {
        TriggerDependencyStructure tds = this.table.get(clientId);
        if (tds != null) {
            return tds;
        } else {
            tds = new TriggerDependencyStructure();
            TransactedList<TransactedList<TransactedSet<String>>> struc = new TransactedList();
            tds.setstructure(struc);

            int strata = SystemVariables.getNumStrata();
            for (int i = 0; i < strata; i++) {
                TransactedList<TransactedSet<String>> stratum = new TransactedList();
                int clusters = SystemVariables.getClusters(i);
                for (int j = 0; j < clusters; j++) {
                    TransactedSet<String> ts = new TransactedSet();
                    stratum.add(ts);
                }
                struc.add(stratum);
            }
            // todo add to share.
//            DynamicDependencyManager.getDependencyShare().add(tds);
            this.table.put(clientId, tds);
            return tds;
        }
    }
}
