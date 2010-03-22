package net.epzilla.accumulator.service;

import net.epzilla.accumulator.global.DerivedEvent;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by IntelliJ IDEA.
 * User: Rajeev
 * Date: Feb 28, 2010
 * Time: 7:52:24 AM
 * To change this template use File | Settings | File Templates.
 */
public interface AccumulatorService extends Remote {

        public boolean receiveDerivedEvent(DerivedEvent event) throws RemoteException;

        public boolean isEventAvailable(long srcId, int clientId) throws RemoteException;

}
