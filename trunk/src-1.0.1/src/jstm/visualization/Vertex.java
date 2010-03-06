/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.visualization;

import java.util.HashSet;

import jstm.core.TransactedObject;

public final class Vertex {

    private final Graph _graph;

    private final TransactedObject _o;

    private final HashSet<Edge> _inEdges = new HashSet<Edge>();

    private final HashSet<Edge> _outEdges = new HashSet<Edge>();

    private int _foldLevel;

    protected Vertex _folder;

    protected int _propagatedFold;

    protected int _foldedCount;

    public Object Tag;

    public Vertex(Graph graph, TransactedObject root) {
        _graph = graph;
        _o = root;
    }

    public Graph getGraph() {
        return _graph;
    }

    public TransactedObject getTransactedObject() {
        return _o;
    }

    public HashSet<Edge> getInEdges() {
        return _inEdges;
    }

    public HashSet<Edge> getOutEdges() {
        return _outEdges;
    }

    public int getFoldLevel() {
        return _foldLevel;
    }

    public void fold() {
        _foldLevel++;
    }

    public void unfold() {
        _foldLevel--;
    }

    public Vertex getFolder() {
        return _folder;
    }

    public int getPropagatedFold() {
        return _propagatedFold;
    }

    public int getFoldedCount() {
        return _foldedCount;
    }
}
