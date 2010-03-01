package net.epzilla.accumulator.service;

import net.epzilla.accumulator.global.DerivedEvent;
import net.epzilla.accumulator.global.Event;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.ArrayList;

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
        return null;  //To change body of implemented methods use File | Settings | File Templates.
        // todo - validate client.
    }

    public ArrayList<Event> getAvailableEvents(long clientId, String accessKey) throws RemoteException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public ArrayList<Event> getEvents(long[] eventIds, long clientId, String accessKey) throws RemoteException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
