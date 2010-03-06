/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.visualization;

import java.util.HashSet;

import jstm.core.TransactedObject;
import jstm.misc.Debug;

public final class Edge {

    private final Graph _graph;

    public final Vertex A, B;

    private final HashSet<TransactedObject.Reference> _references = new HashSet<TransactedObject.Reference>();

    public Object Tag;

    public Edge(Graph graph, Vertex a, Vertex b) {
        _graph = graph;
        A = a;
        B = b;
    }

    public Graph getGraph() {
        return _graph;
    }

    protected boolean addReference(TransactedObject.Reference reference) {
        synchronized (_references) {
            return _references.add(reference);
        }
    }

    protected boolean removeReference(TransactedObject.Reference reference) {
        synchronized (_references) {
            return _references.remove(reference);
        }
    }

    public TransactedObject.Reference[] getReferences() {
        synchronized (_references) {
            return _references.toArray(new TransactedObject.Reference[_references.size()]);
        }
    }

    @Override
    public int hashCode() {
        return A.hashCode() ^ B.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Edge) {
            Edge other = (Edge) obj;
            Debug.assertion(other._graph == _graph);
            return other.A == A && other.B == B;
        }

        return false;
    }
}