package net.epzilla.accumulator.service;

import net.epzilla.accumulator.global.DerivedEvent;
import net.epzilla.accumulator.global.Event;
import net.epzilla.accumulator.generated.SharedDerivedEvent;
import net.epzilla.accumulator.stm.STMAccess;
import net.epzilla.accumulator.stm.EventConverter;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.ArrayList;

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
