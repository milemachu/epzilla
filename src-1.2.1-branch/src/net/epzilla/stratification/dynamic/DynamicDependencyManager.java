package net.epzilla.stratification.dynamic;

import jstm.core.*;
import org.epzilla.dispatcher.dispatcherObjectModel.TriggerDependencyStructure;
import org.epzilla.util.Logger;

import java.util.Hashtable;
import java.util.Iterator;



public class DynamicDependencyManager {
    private static DynamicDependencyManager instance = new DynamicDependencyManager();
    private Hashtable<Long, TriggerDependencyStructure> table = new Hashtable();
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

    public void addTriggerDependencyStructure(long clientId, TriggerDependencyStructure tds) {
        this.table.put(clientId, tds);
    }

    public TriggerDependencyStructure getDependencyStructure(long clientId)  {
        TriggerDependencyStructure tds = this.table.get(clientId);
        if (tds != null) {
            return tds;
        } else {

            // client added by another peer. to be discovered here.
            // rare case. no concerns about performance here.
            try {
                Iterator<TransactedObject> it = DynamicDependencyManager.dependencyShare.iterator();
                while (it.hasNext()) {

                    TransactedObject to = it.next();
                    if (to instanceof TriggerDependencyStructure) {
                        tds = (TriggerDependencyStructure) to;
                        if (Long.toString(clientId).equals(tds.getclientId())) {
                            this.table.put(clientId, tds);
                            return tds;
                        }
                    }
                }
            } catch (Exception e) {
                Logger.error("error while iterating dependency share.", e);
            }

            // new client.
            tds = new TriggerDependencyStructure();
            TransactedList<TransactedList<TransactedSet<String>>> struc = new TransactedList();
            tds.setInputStructure(struc);
            tds.setOutputStructure(new TransactedList());

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

            this.table.put(clientId, tds);
            int attempts = 0;

//            loop:
//            do {
                try {
                    if (Site.getLocal().getPendingCommitCount() < Site.MAX_PENDING_COMMIT_COUNT) {
                        Site.getLocal().allowThread();
                        Transaction transaction = Site.getLocal().startTransaction();
                        DynamicDependencyManager.dependencyShare.add(tds);
                        transaction.commit();
//                        break loop;
                    }
//                    Thread.sleep(500);
                } catch (Exception e) {
                    Logger.error("can't commit dependency object.", e);
                }
//            } while (attempts < 5);
            return tds;
        }
    }
}
