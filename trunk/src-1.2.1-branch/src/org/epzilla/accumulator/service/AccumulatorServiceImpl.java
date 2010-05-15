package org.epzilla.accumulator.service;


import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;

import org.epzilla.accumulator.generated.SharedDerivedEvent;
import org.epzilla.accumulator.global.DerivedEvent;
import org.epzilla.accumulator.service.AccumulatorService;
import org.epzilla.accumulator.stm.EventConverter;
import org.epzilla.accumulator.stm.STMAccess;

import jstm.core.TransactedList;

/**
 * Created by IntelliJ IDEA.
 * User: Rajeev
 * Date: Feb 28, 2010
 * Time: 7:59:00 AM
 * To change this template use File | Settings | File Templates.
 */
public class AccumulatorServiceImpl extends UnicastRemoteObject implements AccumulatorService {

    public AccumulatorServiceImpl() throws RemoteException {
//        super(0);

    }

    public boolean receiveDerivedEvent(DerivedEvent event) throws RemoteException {
//        System.out.println("accumulator service was called...!");

        // todo -
        // add to stm
        TransactedList<SharedDerivedEvent> list = STMAccess.clientMap.get(event.getClientId());
        if (list == null) {
            list = new TransactedList<SharedDerivedEvent>();
            STMAccess.clientMap.put(event.getClientId(), list);
        }
        list.add(EventConverter.toSharedDerivedEvent(event));
        return true;
        // notify dispatcher
        // notify client. - done by dispatcher.

//        return false;
    }

    public boolean isEventAvailable(long srcId, int clientId) throws RemoteException {
        TransactedList<SharedDerivedEvent> list = STMAccess.clientMap.get(clientId);

        if (list != null) {
            DerivedEvent de = new DerivedEvent();
            de.setSrcId(srcId);
            de.setClientId(clientId);
            return list.contains(de);
        }

        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
