package net.epzilla.accumulator.service;

import net.epzilla.accumulator.service.AccumulatorService;
import net.epzilla.accumulator.global.DerivedEvent;
import net.epzilla.accumulator.stm.STMAccess;
import net.epzilla.accumulator.stm.EventConverter;
import net.epzilla.accumulator.generated.SharedDerivedEvent;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;

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
