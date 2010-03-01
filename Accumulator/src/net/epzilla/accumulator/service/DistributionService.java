package net.epzilla.accumulator.service;

import net.epzilla.accumulator.global.DerivedEvent;
import net.epzilla.accumulator.global.Event;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface DistributionService extends Remote {

    public Event getEvent(long eventId, long clientId, String accessKey) throws RemoteException;

    public ArrayList<Event> getAvailableEvents(long clientId, String accessKey) throws RemoteException;

    public ArrayList<Event> getEvents(long[] eventIds, long clientId, String accessKey) throws RemoteException;


}
