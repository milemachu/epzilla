package org.epzilla.accumulator.service;


import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import org.epzilla.accumulator.global.DerivedEvent;
import org.epzilla.accumulator.global.Event;

public interface DistributionService extends Remote {

    public Event getEvent(long eventId, long clientId, String accessKey) throws RemoteException;

    public ArrayList<Event> getAvailableEvents(long clientId, String accessKey) throws RemoteException;

    public ArrayList<Event> getEvents(long[] eventIds, long clientId, String accessKey) throws RemoteException;


}
