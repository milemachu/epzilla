package org.epzilla.accumulator.service;


import java.rmi.Remote;
import java.rmi.RemoteException;

import org.epzilla.accumulator.global.DerivedEvent;

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
