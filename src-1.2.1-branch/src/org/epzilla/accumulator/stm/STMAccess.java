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
package org.epzilla.accumulator.stm;

import org.epzilla.accumulator.generated.SharedDerivedEvent;
import org.epzilla.accumulator.global.DerivedEvent;

import jstm.core.TransactedList;
import jstm.core.Site;
import jstm.core.Transaction;
import jstm.core.TransactedMap;

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
