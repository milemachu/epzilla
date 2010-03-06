/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.core;

import java.util.HashSet;

import jstm.misc.Queue;

/**
 * Keeps track of previously committed transactions. They are kept because at
 * least one transaction was running during their commit and could have became
 * invalid due to their writes. Committed transactions can also be kept if we
 * are in a distributed environment. In this case, they are removed when
 * acknowledgements are received from other nodes.
 */
final class WriteTrail {

    private final TransactionManager _manager;

    private final Queue<Transaction> _committed = new Queue<Transaction>();

    private final HashSet<Transaction> _invalidated = new HashSet<Transaction>();

    public WriteTrail(TransactionManager manager) {
        _manager = manager;
    }

    public boolean idle() {
        return _committed.size() == 0;
    }

    /**
     * False if fields that have been read by this transaction have changed.
     */
    public boolean checkIfStillValid(Transaction transaction) {
        // TOTO : binary search for start item

        for (Transaction committed : _committed)
            if (committed.invalidates(transaction))
                return false;

        return true;
    }

    public void onCommit(Transaction value) {
        _committed.push(value);
    }

    /**
     * Remove writes sets if no transaction can be reading them and return
     * transactions that have become invalid due to new writes.
     */
    public HashSet<Transaction> pruneWritesTrail(long limitCommitCount) {
        _invalidated.clear();

        // int count = _committed.size();

        if (_committed.size() > 0 && _committed.first().getResultingVersion() <= limitCommitCount) {
            long minStartVersion = Long.MAX_VALUE;
            long minRunningVersion = Long.MAX_VALUE;

            {
                for (Transaction current : _manager.getTransactions()) {
                    if (minStartVersion > current.getVersionOnStart())
                        minStartVersion = current.getVersionOnStart();

                    // If the transaction is running, it might be doing reads so
                    // we must keep transactions committed after it's start
                    // version.

                    if (current.getStatus() == Transaction.Status.RUNNING) {
                        if (minRunningVersion > current.getVersionOnStart())
                            minRunningVersion = current.getVersionOnStart();
                    }
                }
            }

            while (_committed.size() > 0) {
                Transaction committed = _committed.first();

                if (_committed.first().getResultingVersion() > limitCommitCount)
                    break;

                if (committed.getResultingVersion() <= minStartVersion) {
                    // No other transaction has reads that old, this one can be
                    // removed
                    _committed.pop();
                } else if (committed.getResultingVersion() <= minRunningVersion) {
                    for (Transaction current : _manager.getTransactions())
                        if (committed.invalidates(current))
                            _invalidated.add(current);

                    // Checked all other transactions, this one can be removed
                    _committed.pop();
                } else {
                    // Next transactions can't be checked since other ones
                    // started before them are running and might be doing reads
                    break;
                }
            }
        }

        if (_committed.size() > TransactionManager.MAX_EXPECTED_WRITE_TRAIL_LENGTH)
            System.out.println("Warning : WriteTrail is getting big (" + _committed.size() + ")");

        // if (Debug.Level >= 2 && _committed.size() != count)
        // System.out.println("Dropped " + (count - _committed.size()) + " from
        // write trail");

        return _invalidated;
    }

    // private static int binarySearch(Comparable[] a, Comparable x) {
    // int low = 0;
    // int high = a.length - 1;
    // int mid;
    //
    // while (low <= high) {
    // mid = (low + high) / 2;
    //
    // if (a[mid].compareTo(x) < 0)
    // low = mid + 1;
    // else if (a[mid].compareTo(x) > 0)
    // high = mid - 1;
    // else
    // return mid;
    // }
    //
    // return -1; // NOT_FOUND
    // }
}
