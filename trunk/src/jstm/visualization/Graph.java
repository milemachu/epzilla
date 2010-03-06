/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.visualization;

import java.util.HashMap;
import java.util.Iterator;

import jstm.core.GraphWatcher;
import jstm.core.TransactedObject;
import jstm.core.TransactedObject.Reference;
import jstm.misc.Debug;

public final class Graph {

    private final HashMap<TransactedObject, Vertex> _map = new HashMap<TransactedObject, Vertex>();

    private final GraphWatcher _watcher;

    public Graph(GraphWatcher watcher) {
        _watcher = watcher;
    }

    public void add(TransactedObject o) {
        _watcher.add(o);
    }

    public void remove(TransactedObject o) {
        _watcher.remove(o);
    }

    public Vertex getVertex(TransactedObject o) {
        return _map.get(o);
    }

    public HashMap<TransactedObject, Vertex> getMap() {
        return _map;
    }

    public void readEvents() {
        GraphWatcher.Event[] events = _watcher.takeEvents();

        if (events != null) {
            for (GraphWatcher.Event event : events) {
                if (event instanceof GraphWatcher.Event.Object) {
                    GraphWatcher.Event.Object o = (GraphWatcher.Event.Object) event;
                    onObject(o.Object, o.Removal);
                }

                if (event instanceof GraphWatcher.Event.Reference) {
                    GraphWatcher.Event.Reference r = (GraphWatcher.Event.Reference) event;

                    if (r.Removal)
                        onReferenceRemoved(r.A, r.B, r.Reference);
                    else
                        onReference(r.A, r.B, r.Reference);
                }
            }
        }
    }

    public void updateFolds() {
        for (Vertex vertex : _map.values()) {
            vertex._folder = null;
            vertex._propagatedFold = 0;
        }

        for (Vertex vertex : _map.values())
            if (vertex.getFoldLevel() > 0)
                for (Edge edge : vertex.getOutEdges())
                    propagateFold(edge.B, vertex, vertex.getFoldLevel());

        for (Vertex vertex : _map.values()) {
            if (vertex._folder != null)
                vertex._folder._foldedCount++;
        }
    }

    private void propagateFold(Vertex vertex, Vertex folder, int level) {
        if (vertex._propagatedFold < level) {
            vertex._propagatedFold = level;
            vertex._folder = folder;
        }

        if (level > 1)
            for (Edge edge : vertex.getOutEdges())
                propagateFold(edge.B, vertex, level - 1);
    }

    private void onObject(TransactedObject o, boolean removal) {
        if (removal) {
            Vertex vertex = _map.remove(o);
            Debug.assertion(vertex.getTransactedObject() == o);
        } else {
            Vertex previous = _map.put(o, new Vertex(this, o));
            Debug.assertion(previous == null);
        }
    }

    private void onReference(TransactedObject ta, TransactedObject tb, Reference reference) {
        Vertex a = _map.get(ta);
        Vertex b = _map.get(tb);

        Edge edge = null;

        for (Iterator<Edge> it = a.getOutEdges().iterator(); it.hasNext();) {
            Edge current = it.next();

            if (current.B == b)
                edge = current;
        }

        if (edge == null) {
            edge = new Edge(this, a, b);
            a.getOutEdges().add(edge);
            b.getInEdges().add(edge);
        }

        boolean t = edge.addReference(reference);
        Debug.assertion(t);
    }

    private void onReferenceRemoved(TransactedObject ta, TransactedObject tb, Reference reference) {
        Vertex a = _map.get(ta);
        Vertex b = _map.get(tb);

        Edge edge = null;

        for (Iterator<Edge> it = a.getOutEdges().iterator(); it.hasNext();) {
            Edge current = it.next();

            if (current.B == b)
                edge = current;
        }

        if (edge != null) {
            boolean t = edge.removeReference(reference);
            Debug.assertion(t);

            if (edge.getReferences().length == 0) {
                a.getOutEdges().remove(edge);
                b.getInEdges().remove(edge);
            }
        }
    }
}
