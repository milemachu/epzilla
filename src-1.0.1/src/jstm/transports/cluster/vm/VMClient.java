///**
// * JSTM (http://jstm.sourceforge.net)
// * Distributed under the Apache License Version 2.0
// * Copyright © 2006-2007 Cyprien Noel
// */
//
//package jstm.distributed.cluster.vm;
//
//import java.io.ByteArrayInputStream;
//import java.io.ObjectInputStream;
//import java.io.Serializable;
//import java.util.concurrent.ArrayBlockingQueue;
//import java.util.concurrent.CopyOnWriteArrayList;
//
//import jstm.distributed.cluster.Transport;
//
//public final class VMClient implements Transport {
//
//    private final VMServer _server;
//
//    private final ArrayBlockingQueue<byte[]> _serverToClient = new ArrayBlockingQueue<byte[]>(10);
//
//    private final ArrayBlockingQueue<byte[]> _clientToServer = new ArrayBlockingQueue<byte[]>(10);
//
//    private final CopyOnWriteArrayList<Listener> _listeners = new CopyOnWriteArrayList<Listener>();
//
//    public VMClient(VMServer server) {
//        _server = server;
//
//        launchThreads();
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
//    public boolean canSendToServer() {
//        return _clientToServer.size() <= 2;
//    }
//
//    public void sendToServer(Serializable message) {
//        final byte[] array = null;//Serializer.serialize(message);
//
//        try {
//            _clientToServer.put(array);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    protected void receiveFromServer(Serializable message) {
//        final byte[] array = null;//Serializer.serialize(message);
//
//        try {
//            _serverToClient.put(array);
//        } catch (Exception ex) {
//            throw new RuntimeException(ex);
//        }
//    }
//
//    private void launchThreads() {
//        {
//            Thread thread = new Thread("VM Client to Server") {
//
//                @Override
//                public void run() {
//                    readClientToServer();
//                }
//            };
//
//            thread.setDaemon(true);
//            thread.start();
//        }
//
//        {
//            Thread thread = new Thread("VM Server to Client") {
//
//                @Override
//                public void run() {
//                    readServerToClient();
//                }
//            };
//
//            thread.setDaemon(true);
//            thread.start();
//        }
//    }
//
//    private void readClientToServer() {
//        while (true) {
//            try {
//                ByteArrayInputStream buffer = new ByteArrayInputStream(_clientToServer.take());
//                ObjectInputStream serializer = new ObjectInputStream(buffer);
//                Serializable serializable = (Serializable) serializer.readObject();
//
//                _server.raiseReceived(serializable, VMClient.this);
//            } catch (Exception ex) {
//                throw new RuntimeException(ex);
//            }
//        }
//    }
//
//    private void readServerToClient() {
//        while (true) {
//            try {
//                ByteArrayInputStream buffer = new ByteArrayInputStream(_serverToClient.take());
//                ObjectInputStream serializer = new ObjectInputStream(buffer);
//                Serializable serializable = (Serializable) serializer.readObject();
//
//                for (Listener listener : _listeners)
//                    listener.receive(serializable);
//            } catch (Exception ex) {
//                throw new RuntimeException(ex);
//            }
//        }
//    }
//
//    public void broadcast(Serializable message) {
//        // TODO Auto-generated method stub
//
//    }
//
//    public Iterable<?> getClients() {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    public boolean isCoordinator() {
//        // TODO Auto-generated method stub
//        return false;
//    }
//}
