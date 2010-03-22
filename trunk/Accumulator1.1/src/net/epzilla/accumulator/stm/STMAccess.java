package net.epzilla.accumulator.stm;

import jstm.core.TransactedList;
import jstm.core.Site;
import jstm.core.Transaction;
import jstm.core.TransactedMap;
import net.epzilla.accumulator.generated.SharedDerivedEvent;
import net.epzilla.accumulator.global.DerivedEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Rajeev
 * Date: Feb 28, 2010
 * Time: 8:21:03 AM
 * To change this template use File | Settings | File Templates.
 */
public class STMAccess {
    private static STMAccess instance = new STMAccess();
    public static STMAccess getInstance() {
        return instance;
    }
    private STMAccess() {
        
    }
//    public static TransactedList<SharedDerivedEvent> events = null;
     public static TransactedMap<Integer, TransactedList<SharedDerivedEvent>> clientMap;


    public void addTransactionListener(Site.Listener list) {
//                events.
    }

    public boolean addDerivedEvent(DerivedEvent e) {

        if (Site.getLocal().getPendingCommitCount() < Site.MAX_PENDING_COMMIT_COUNT) {
            Site.getLocal().allowThread();
            Transaction transaction = Site.getLocal().startTransaction();
//            events.add(EventConverter.toSharedDerivedEvent(e));
            TransactedList<SharedDerivedEvent> lis = clientMap.get(e.getClientId());

            if (lis == null) {
                                                  lis = new TransactedList<SharedDerivedEvent>();
                clientMap.put(e.getClientId(), lis);
            }

            lis.add(EventConverter.toSharedDerivedEvent(e));
            transaction.commit();
            return true;
        } else {
            return false;
        }


    }


}
