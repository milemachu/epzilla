/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.core;

import java.util.ArrayList;
import java.util.Map;

import jstm.core.TransactedObject.ReferencesWalker;
import jstm.misc.Debug;

/**
 * Watches a set of classes and their references so a graph can be maintained.
 * In core package to access protected stuff, in particular for NSTM as graphs
 * are in separate assemblies.
 */
public final class GraphWatcher {

    public static class Event {

        public static final class Object extends Event {

            public TransactedObject Object;

            public boolean Removal;
        }

        public static final class Reference extends Event {

            public TransactedObject A, B;

            public TransactedObject.Reference Reference;

            public boolean Removal;
        }
    }

    public interface Listener {

        void onChanged();
    }

    private final TransactedSet<TransactedObject> _objects = new TransactedSet<TransactedObject>();

    private final ArrayList<Event> _events = new ArrayList<Event>();

    private final TransactionListener _transactionListener = new TransactionListener();

    private final Listener _listener;

    private Transaction _activeTransaction;

    public GraphWatcher(Site local, Listener listener) {
        _listener = listener;
        local.addSiteListener(new SiteListener());
    }

    public void add(TransactedObject o) {
        _objects.add(o);
    }

    public void remove(TransactedObject o) {
        _objects.remove(o);
    }

    public Transaction getActiveTransaction() {
        return _activeTransaction;
    }

    public void setActiveTransaction(Transaction value) {
        if (_activeTransaction != null) {
            _activeTransaction.removeListener(_transactionListener);
            walkVersions(_activeTransaction, true);
        }

        _activeTransaction = value;

        if (_activeTransaction != null) {
            _activeTransaction.addListener(_transactionListener);
            walkVersions(_activeTransaction, false);
        }
    }

    public Event[] takeEvents() {
        Event[] events = null;

        synchronized (_events) {
            if (_events.size() > 0) {
                events = new Event[_events.size()];
                _events.toArray(events);
                _events.clear();
            }
        }

        return events;
    }

    //

    private final class SiteListener implements Site.Listener {

        public void onCommitted(Transaction transaction) {
            walkVersions(transaction, false);
        }

        public void onAborted(Transaction transaction) {
        }
    }

    private final class TransactionListener implements Transaction.Listener {

        public void onNewChange(Change change) {
            walkChange(change);
        }

        public void onStatusChanged(Transaction transaction) {
        }
    }

    //

//    private void walkSnapshot(Transaction transaction, TransactedObject o, boolean reverse) {
//        Walker walker = new Walker(o, transaction, reverse);
//        o.getSnapshot(transaction).walkReferences(walker, false, true);
//    }

    private void walkChange(Change change) {
        Walker walker = new Walker(change.getObject(), change.getTransaction(), false);
        change.getDelta().walkReferences(walker, true, true);
    }

    private void walkVersions(Transaction transaction, boolean reverse) {
        if (transaction.getPrivateObjects() != null) {
            for (Map.Entry<TransactedObject, TransactedObject.Version> entry : transaction.getPrivateObjects().entrySet()) {
                Walker walker = new Walker(entry.getKey(), transaction, reverse);
                entry.getValue().walkReferences(walker, true, true);
            }
        }
    }

    private void addEvent(Event event) {
        synchronized (_events) {
            _events.add(event);
        }

        _listener.onChanged();
    }

    //

    private final class Walker implements ReferencesWalker {

        private final TransactedObject _walked;

        private final Transaction _transaction;

        private final boolean _reverse;

        public Walker(TransactedObject walked, Transaction transaction, boolean reverse) {
            _walked = walked;
            _transaction = transaction;
            _reverse = reverse;
        }

        public Transaction getTransaction() {
            return _transaction;
        }

        @SuppressWarnings("unchecked")
        public void onReference(TransactedObject o, boolean removal, TransactedObject.Reference reference) {
            if (_reverse)
                removal = !removal;

            Debug.assertion(o != _objects);

            if (_walked == _objects) {
                Event.Object e = new Event.Object();
                e.Object = o;
                e.Removal = removal;
                addEvent(e);

                // walkSnapshot(getTransaction(), o, _reverse);
            } else {
                if (!_objects.contains(o) && !removal)
                    _objects.addFast(o);

                Event.Reference e = new Event.Reference();
                e.A = _walked;
                e.B = o;
                e.Reference = reference;
                e.Removal = removal;
                addEvent(e);
            }
        }
    }
}
