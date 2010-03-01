package net.epzilla.accumulator.service;

import net.epzilla.accumulator.service.AccumulatorService;
import net.epzilla.accumulator.global.DerivedEvent;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;

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

    public void receiveDerivedEvent(DerivedEvent event) throws RemoteException {
        System.out.println("accumulator service was called...!");

        // todo -
        // add to stm
        // notify dispatcher
        // notify client.
    }
}
