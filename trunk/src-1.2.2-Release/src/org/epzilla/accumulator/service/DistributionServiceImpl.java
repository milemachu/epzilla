package org.epzilla.accumulator.service;


import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.ArrayList;

import org.epzilla.accumulator.generated.SharedDerivedEvent;
import org.epzilla.accumulator.global.DerivedEvent;
import org.epzilla.accumulator.global.Event;
import org.epzilla.accumulator.stm.EventConverter;
import org.epzilla.accumulator.stm.STMAccess;

import jstm.core.TransactedList;

/**
 * Created by IntelliJ IDEA.
 * User: Rajeev
 * Date: Feb 28, 2010
 * Time: 10:59:13 AM
 * To change this template use File | Settings | File Templates.
 */
public class DistributionServiceImpl extends UnicastRemoteObject implements DistributionService {

    public DistributionServiceImpl() throws RemoteException {

    }


    public Event getEvent(long eventId, long clientId, String accessKey) throws RemoteException {


        // todo - validate client.

        TransactedList<SharedDerivedEvent> list = STMAccess.clientMap.get(clientId);
         if (list == null) {
             return null;
         }

        for (SharedDerivedEvent sde: list) {
            if (sde.getid() == eventId) {
                return EventConverter.toDerivedEvent(sde);
            }
        }

        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public ArrayList<Event> getAvailableEvents(long clientId, String accessKey) throws RemoteException {
        // todo logic
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public ArrayList<Event> getEvents(long[] eventIds, long clientId, String accessKey) throws RemoteException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
