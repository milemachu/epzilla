///**
// * JSTM (http://jstm.sourceforge.net)
// * Distributed under the Apache License Version 2.0
// * Copyright © 2006-2007 Cyprien Noel
// */
//
//package jstm.distributed.cluster.vm;
//
//import java.util.concurrent.CopyOnWriteArrayList;
//
//import jstm.distributed.cluster.Transport;
//import java.io.Serializable;
//
//public class VMServer implements Transport {
//
//    private final CopyOnWriteArrayList<VMClient> _clients = new CopyOnWriteArrayList<VMClient>();
//
//    private final CopyOnWriteArrayList<Transport.Listener> _listeners = new CopyOnWriteArrayList<Transport.Listener>();
//
//    public VMServer() {
//    }
//
//    public void add(VMClient client) {
//        _clients.add(client);
//    }
//
//    public Iterable<?> getClients() {
//        return _clients;
//    }
//
//    public void addListener(Listener listener) {
//        _listeners.add(listener);
//    }
//
//    public void removeListener(Listener listener) {
//        _listeners.remove(listener);
//    }
//
//    public void broadcast(Serializable message) {
//        for (VMClient client : _clients)
//            client.receiveFromServer(message);
//    }
//
//    public boolean isCoordinator() {
//        return true;
//    }
//
//    protected void raiseReceived(Serializable message, VMClient sender) {
//        for (Transport.Listener listener : _listeners)
//            listener.receive(message);
//    }
//}
