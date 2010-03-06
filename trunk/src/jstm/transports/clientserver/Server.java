/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.transports.clientserver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import jstm.core.*;
import jstm.misc.Debug;

/**
 * 
 */
public abstract class Server extends Connection {

    public interface Listener {

        void onConnection(Session session);

        void onDisconnection(Session session);
    }

    protected static final String SERVER = "SERVER";

    protected static final String GROUP = "GROUP";

    protected static final String CLIENT = "CLIENT";

    private final Group _group;

    private final ConcurrentHashMap<Object, Session> _sessions = new ConcurrentHashMap<Object, Session>();

    private final CopyOnWriteArrayList<Listener> _listeners = new CopyOnWriteArrayList<Listener>();

    protected Server(Site localSite) {
        super(localSite);

        _group = createGroup();
    }

    public final void start() throws IOException {
        registerConnection(_group);
        listen();
    }

    protected abstract void listen() throws IOException;

    public final void stop() {
        dispose(null);
    }

    @Override
    protected void _dispose(IOException ex) {
        for (Object o : _sessions.keySet())
            onDisconnection(o);

        Debug.assertion(_sessions.size() == 0);

        super._dispose(ex);
    }

    /**
     * If you add a share to the list of open shares of this group, it will be
     * visible to all clients in addition to the server. Functionally, it is
     * equivalent to calling Site.getLocalSite().getOpenShares().add(share) on
     * each client and on the server but this way to do it is dynamic. New
     * clients connecting to the server are automatically part of the group.
     */
    public final Group getServerAndClients() {
        return _group;
    }

    public final void addListener(Listener listener) {
        _listeners.add(listener);
    }

    public final void removeListener(Listener listener) {
        _listeners.remove(listener);
    }

    protected final Map<Object, Session> getSessions() {
        return _sessions;
    }

    protected final Session getSession(Object o) {
        return _sessions.get(o);
    }

    protected final void onConnection(Object key, Session session) {
        Debug.assertion(!_sessions.containsValue(session));
        Object previous = _sessions.put(key, session);
        Debug.assertion(previous == null);

        try {
            session.start();
        } catch (IOException ex) {
            onDisconnection(key);
        }
    }

    protected final void onDisconnection(Object key) {
        Session session = _sessions.remove(key);
        session.stop();
    }

    protected final void raiseConnected(Session session) {
        for (Listener listener : _listeners)
            listener.onConnection(session);
    }

    protected final void raiseDisconnected(Session session) {
        for (Listener listener : _listeners)
            listener.onDisconnection(session);
    }

    protected final boolean serverInvolved() {
        return involved();
    }

    //

    public List<Session> getConnectedSessions() {
        ArrayList<Session> list = new ArrayList<Session>();

        for (Session session : getSessions().values())
            if (session.getStatus() == Transport.Status.SYNCHRONIZED)
                list.add(session);

        return list;
    }

    // Local

    @Override
    protected boolean interceptsCommits() {
        return false;
    }

    @Override
    protected boolean interceptsCommitsCanChange() {
        return false;
    }

    @Override
    protected void onInterception(CommitInterception interception) {
        Debug.fail("Not on server");
    }

    @Override
    protected void onCommit(Transaction transaction, long newVersion) {
        if (involved()) {
            if (Debug.MESSAGING)
                Debug.log("sendCommit to all sessions for " + transaction);

            for (Session current : getSessions().values())
                current.sendCommit(this, transaction, newVersion);
        }
    }

    @Override
    protected void onAbort(Transaction transaction) {
    }

    @Override
    protected Long getAcknowledgedVersion() {
        return null; // Done by sessions
    }
}
