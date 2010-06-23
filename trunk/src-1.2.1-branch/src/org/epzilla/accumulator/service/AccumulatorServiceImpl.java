package org.epzilla.accumulator.service;


import jstm.core.TransactedList;
import org.epzilla.accumulator.dataManager.EventManager;
import org.epzilla.accumulator.generated.SharedDerivedEvent;
import org.epzilla.accumulator.global.DerivedEvent;
import org.epzilla.accumulator.notificationSys.ClientNotifier;
import org.epzilla.accumulator.notificationSys.NotificationManager;
import org.epzilla.accumulator.stm.STMAccess;
import org.epzilla.accumulator.userinterface.AccumulatorUIControler;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.StringTokenizer;

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
//        TransactedList<SharedDerivedEvent> list = STMAccess.clientMap.get(event.getClientId());
//        if (list == null) {
//            list = new TransactedList<SharedDerivedEvent>();
//            STMAccess.clientMap.put(event.getClientId(), list);
//        }
//        list.add(EventConverter.toSharedDerivedEvent(event));


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

    int count = 0;

    public void receiveDeriveEvent(byte[] deriveEvent) throws RemoteException {
        String eventS = new String(deriveEvent);
                                    // todo uncomment.
//        EventManager.setEventSegement(eventS);
//        count++;
        AccumulatorUIControler.appendEventResults(eventS);
//        AccumulatorUIControler.appendDeriveEventCount(count + "");
//        AccumulatorUIControler.appendEventprocessed(count + "");
        NotificationManager.setAlertCount();  // set count of the processed events

        //add logic to send the result to the client
//        StringTokenizer st = new StringTokenizer(eventS, ":");
//        String result = st.nextToken();  //trigger
//        String clientID = st.nextToken(); //client ID

        ClientNotifier.addAlertMessage(eventS);
    }
}
