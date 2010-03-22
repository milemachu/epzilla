///**
// * JSTM (http://jstm.sourceforge.net)
// * Distributed under the Apache License Version 2.0
// * Copyright © 2006-2007 Cyprien Noel
// */
//
//package jstm.distributed.cluster.vm;
//
//import java.io.*;
//import java.util.Timer;
//import java.util.TimerTask;
//import java.util.concurrent.CopyOnWriteArrayList;
//
//import jstm.distributed.cluster.ClusterNode;
//import jstm.distributed.cluster.Transport;
//import java.io.Serializable;
//import jstm.misc.Debug;
//
//public class VM extends ClusterNode {
//
//    private final VM _server;
//
//    private final CopyOnWriteArrayList<VM> _clients;
//
//    private final Timer _timer;
//
//    private VM(VM server, CopyOnWriteArrayList<VM> clients) {
//        super(
//        _server = server;
//        _clients = clients;
//        _timer = new Timer();
//    }
//
//    public static VM createServer() {
//        return new VM(null, new CopyOnWriteArrayList<VM>());
//    }
//
//    public VM createClient() {
//        VM vm = new VM(this, null);
//        _clients.add(vm);
//        return vm;
//    }
//
//    public boolean isCoordinator() {
//        return _server == null;
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
//    public void sendToAll(Serializable message) {
//        Debug.assertion(isCoordinator());
//
//        final byte[] array = serialize(message);
//
//        TimerTask task = new TimerTask() {
//
//            @Override
//            public void run() {
//                try {
//                    for (VM client : _clients) {
//                        ByteArrayInputStream buffer = new ByteArrayInputStream(array);
//                        ObjectInputStream serializer = new ObjectInputStream(buffer);
//                        Serializable temp = (Serializable) serializer.readObject();
//
//                        for (Listener listener : client._listeners)
//                            listener.receive(temp);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//
//        _timer.schedule(task, 0);
//    }
//
//    public void sendToCoordinator(Serializable message) {
//        Debug.assertion(!isCoordinator());
//
//        final byte[] array = serialize(message);
//
//        TimerTask task = new TimerTask() {
//
//            @Override
//            public void run() {
//                try {
//                    ByteArrayInputStream buffer = new ByteArrayInputStream(array);
//                    ObjectInputStream serializer = new ObjectInputStream(buffer);
//                    Serializable temp = (Serializable) serializer.readObject();
//
//                    for (Listener listener : _server._listeners)
//                        listener.receive(temp);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//
//        _timer.schedule(task, 0);
//    }
//
//    private static byte[] serialize(Serializable serializable) {
//        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
//        ObjectOutputStream serializer;
//        try {
//            serializer = new ObjectOutputStream(buffer);
//            serializer.writeObject(serializable);
//        } catch (IOException ex) {
//            throw new RuntimeException(ex);
//        }
//        return buffer.toByteArray();
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
//}
