/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.tests;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import jstm.core.Site;
import jstm.core.TransactedList;
import jstm.core.Transaction;

@SuppressWarnings("unchecked")
public abstract class TransactedListTestBase extends TransactionsTest {

    protected static final int READ_IN_TRANSACTION = 1;

    protected static final int REUSE_LIST = 2;

    protected static final int RANDOM_STOPS = 4;

    protected static final int ONE_TRANSACTION = 8;

    protected static final int ALL = READ_IN_TRANSACTION | REUSE_LIST | RANDOM_STOPS | ONE_TRANSACTION;

    private static final boolean TEST_EXCEPTIONS = false;

    private final ThreadLocal<Integer> _run = new ThreadLocal<Integer>();

    private final ThreadLocal<TransactedList> _list = new ThreadLocal<TransactedList>();

    protected final AtomicInteger _commitCount = new AtomicInteger();

    public TransactedListTestBase() {
    }

    //

    protected Site getSite() {
        return Site.getLocal();
    }

    protected void onListCreated(TransactedList list) {
    }

    protected void onListDisposed(TransactedList list) {
    }

    //

    protected final int getRun() {
        return _run.get();
    }

    protected final void run() {
        Site.InternalListener listener = new Site.InternalListener() {

            public void onCommitted(Transaction transaction, long newVersion) {
                _commitCount.incrementAndGet();
            }

            public void onCommitting(Transaction transaction) {
            }

            public void onAborted(Transaction transaction) {
                // Debug.fail("Should not abort");
            }
        };

        getSite().addInternalListener(listener);

        for (int run = 0; run <= ALL; run++) {
            _run.set(run);
            // _run = 8;
            // {
            onBeforeRun();

            for (int i = 0; i < 14; i++) {
                // int i = 5;
                // {
                switch (i) {
                    case 0:
                        test_TransactedList();
                        break;
                    case 1:
                        test_get();
                        break;
                    case 2:
                        test_add();
                        break;
                    case 3:
                        test_addAll();
                        break;
                    case 4:
                        test_clear();
                        break;
                    case 5:
                        test_remove();
                        break;
                    case 6:
                        test_set();
                        break;
                    case 7:
                        test_contains();
                        break;
                    case 8:
                        test_isEmpty();
                        break;
                    case 9:
                        test_indexOf();
                        break;
                    case 10:
                        test_size();
                        break;
                    case 11:
                        test_lastIndexOf();
                        break;
                    case 12:
                        test_toArray();
                        break;
                    case 13:
                        test_MC_iterator();
                        break;
                }
            }

            onAfterRun();
        }

        getSite().removeInternalListener(listener);
    }

    private void log(String text) {
        // System.out.println(text);
    }

    private void assertion(boolean value, String message) {
        if (!value)
            throw new RuntimeException("Assertion failed " + message);
    }

    private void fail(String message) {
        assertion(false, message);
    }

    //

    private void onBeforeRun() {
        if ((getRun() & ONE_TRANSACTION) != 0)
            getSite().startTransaction();
    }

    private void onAfterRun() {
        if ((getRun() & ONE_TRANSACTION) != 0)
            Transaction.getCurrent().commit();
    }

    private void beforeRead() {
        if ((getRun() & ONE_TRANSACTION) == 0)
            if ((getRun() & READ_IN_TRANSACTION) != 0)
                getSite().startTransaction();
    }

    private void afterRead() {
        if ((getRun() & ONE_TRANSACTION) == 0)
            if ((getRun() & READ_IN_TRANSACTION) != 0)
                Transaction.getCurrent().commit();
    }

    private void beforeWrite() {
        if ((getRun() & ONE_TRANSACTION) == 0)
            getSite().startTransaction();
    }

    private void afterWrite() {
        if ((getRun() & ONE_TRANSACTION) == 0)
            Transaction.getCurrent().commit();
    }

    private void randomStop() {
        if ((getRun() & ONE_TRANSACTION) == 0) {
            if ((getRun() & RANDOM_STOPS) != 0) {
                if (Transaction.getCurrent() != null) {
                    Transaction.getCurrent().commit();
                    getSite().startTransaction();
                }
            }
        }
    }

    //

    private TransactedList createList() {
        if (_list.get() != null) {
            if ((getRun() & REUSE_LIST) != 0) {
                Transaction transaction = null;

                if ((getRun() & ONE_TRANSACTION) == 0)
                    transaction = getSite().startTransaction();

                _list.get().clear();

                if (transaction != null)
                    transaction.commit();
            } else {
                onListDisposed(_list.get());
                _list.set(null);
            }
        }

        if (_list.get() == null) {
            _list.set(new TransactedList());
            onListCreated(_list.get());
        }

        return _list.get();
    }

    private TransactedList build(boolean createNew) {
        TransactedList list;

        if (createNew)
            list = new TransactedList();
        else
            list = createList();

        beforeWrite();
        list.add("a");
        list.add("c");
        randomStop();
        list.add("u");
        list.add("n");
        list.add("i");
        list.add("a");
        list.add(null);
        list.add("a");
        list.add("c");
        list.add("u");
        randomStop();
        list.add("n");
        list.add("i");
        list.add("a");
        list.add(null);
        afterWrite();

        return list;
    }

    private void test_TransactedList() {
        log("TransactedList(java.util.Collection)");
        Vector v = new Vector();

        beforeWrite();
        TransactedList al = new TransactedList(v);
        afterWrite();

        beforeRead();
        assertion(al.isEmpty(), "no elements added");
        afterRead();

        v.add("a");
        v.add("c");
        v.add("u");
        v.add("n");
        v.add("i");
        v.add("a");
        v.add(null);

        beforeWrite();
        al = new TransactedList(v);
        assertion(v.equals(al), "check if everything is OK");
        afterWrite();

        if (TEST_EXCEPTIONS) {
            try {
                new TransactedList(null);
                fail("should throw a NullPointerException");
            } catch (NullPointerException npe) {
            }
        }
    }

    private void test_get() {
        log("get(int)java.lang.Object");
        TransactedList al = createList();
        beforeRead();

        if (TEST_EXCEPTIONS) {
            try {
                al.get(0);
                fail("should throw an IndexOutOfBoundsException -- 1");
            } catch (IndexOutOfBoundsException ioobe) {
            }
            try {
                al.get(-1);
                fail("should throw an IndexOutOfBoundsException -- 2");
            } catch (IndexOutOfBoundsException ioobe) {
            }
        }

        afterRead();
        al = build(false);
        beforeRead();

        if (TEST_EXCEPTIONS) {
            try {
                al.get(14);
                fail("should throw an IndexOutOfBoundsException -- 3");
            } catch (IndexOutOfBoundsException ioobe) {
            }
            try {
                al.get(-1);
                fail("should throw an IndexOutOfBoundsException -- 4");
            } catch (IndexOutOfBoundsException ioobe) {
            }
        }

        assertion("a".equals(al.get(0)), "checking returnvalue -- 1");
        assertion("c".equals(al.get(1)), "checking returnvalue -- 2");
        assertion("u".equals(al.get(2)), "checking returnvalue -- 3");
        randomStop();
        assertion("a".equals(al.get(5)), "checking returnvalue -- 4");
        assertion("a".equals(al.get(7)), "checking returnvalue -- 5");
        assertion("c".equals(al.get(8)), "checking returnvalue -- 6");
        assertion("u".equals(al.get(9)), "checking returnvalue -- 7");
        assertion("a".equals(al.get(12)), "checking returnvalue -- 8");
        assertion(null == al.get(6), "checking returnvalue -- 9");
        assertion(null == al.get(13), "checking returnvalue -- 10");
        afterRead();
    }

    private void test_add() {
        log("add(int,java.lang.Object)void");
        TransactedList al = createList();
        beforeWrite();

        if (TEST_EXCEPTIONS) {
            try {
                al.add(-1, "a");
                fail("should throw an IndexOutOfBoundsException -- 1");
            } catch (IndexOutOfBoundsException ioobe) {
            }
            try {
                al.add(1, "a");
                fail("should throw an IndexOutOfBoundsException -- 2");
            } catch (IndexOutOfBoundsException ioobe) {
            }
        }

        al.add(0, "a");
        al.add(1, "c");
        al.add(2, "u");
        al.add(1, null);
        afterWrite();
        beforeRead();
        assertion("a".equals(al.get(0)) && null == al.get(1) && "c".equals(al.get(2)) && "u".equals(al.get(3)), "checking add ...");
        afterRead();

        log("add(java.lang.Object)boolean");
        al = createList();
        beforeWrite();
        assertion(al.add("a"), "checking return value -- 1");
        assertion(al.add("c"), "checking return value -- 2");
        randomStop();
        assertion(al.add("u"), "checking return value -- 3");
        assertion(al.add("n"), "checking return value -- 4");
        assertion(al.add("i"), "checking return value -- 5");
        randomStop();
        assertion(al.add("a"), "checking return value -- 6");
        assertion(al.add(null), "checking return value -- 7");
        assertion(al.add("end"), "checking return value -- 8");
        assertion("a".equals(al.get(0)) && null == al.get(6) && "c".equals(al.get(1)) && "u".equals(al.get(2)), "checking add ... -- 1");
        afterWrite();
        beforeRead();
        assertion("a".equals(al.get(5)) && "end".equals(al.get(7)) && "n".equals(al.get(3)) && "i".equals(al.get(4)), "checking add ... -- 2");
        afterRead();
    }

    private void test_addAll() {
        log("addAll(java.util.Collection)boolean");
        TransactedList al = createList();
        beforeWrite();

        if (TEST_EXCEPTIONS) {
            try {
                al.addAll(null);
                fail("should throw NullPointerException");
            } catch (NullPointerException ne) {
            }
        }

        Collection c = Arrays.asList(al.toArray());
        assertion(!al.addAll(c), "checking returnvalue -- 1");
        al.add("a");
        al.add("b");
        randomStop();
        al.add("c");
        afterWrite();
        c = Arrays.asList(al.toArray());
        al = build(true);

        beforeWrite();
        assertion(al.addAll(c), "checking returnvalue -- 2");
        afterWrite();
        beforeRead();
        assertion(al.containsAll(c), "extra on containsAll -- 1");
        assertion(al.get(14) == "a" && al.get(15) == "b" && al.get(16) == "c", "checking added on right positions");
        afterRead();

        log("addAll(int,java.util.Collection)boolean");
        al = createList();
        c = Arrays.asList(al.toArray());
        beforeWrite();
        assertion(!al.addAll(0, c), "checking returnvalue -- 1");
        al.add("a");
        randomStop();
        al.add("b");
        al.add("c");
        afterWrite();
        c = Arrays.asList(al.toArray());
        al = build(true);
        beforeWrite();

        if (TEST_EXCEPTIONS) {
            try {
                al.addAll(-1, c);
                fail("should throw exception -- 1");
            } catch (IndexOutOfBoundsException ae) {
            }
            try {
                al.addAll(15, c);
                fail("should throw exception -- 2");
            } catch (IndexOutOfBoundsException ae) {
            }
        }

        try {
            assertion(al.addAll(11, c), "checking returnvalue -- 2");
        } catch (ArrayIndexOutOfBoundsException ae) {
            fail("shouldn't throw exception -- 1");
        }

        afterWrite();
        randomStop();
        beforeRead();
        assertion(al.containsAll(c), "extra on containsAll -- 1");
        assertion(al.get(11) == "a" && al.get(12) == "b" && al.get(13) == "c", "checking added on right positions -- 1");
        afterRead();
        beforeWrite();
        assertion(al.addAll(1, c), "checking returnvalue -- 3");
        afterWrite();
        beforeRead();
        assertion(al.get(1) == "a" && al.get(2) == "b" && al.get(3) == "c", "checking added on right positions -- 2");
        afterRead();
    }

    private void test_clear() {
        log("clear()void");
        TransactedList al = createList();
        beforeWrite();
        al.clear();
        afterWrite();
        al = build(false);
        beforeWrite();
        al.clear();
        afterWrite();
        beforeRead();
        assertion(al.size() == 0 && al.isEmpty(), "list is empty ...");
        afterRead();
    }

    private void test_remove() {
        log("remove(int)java.lang.Object");
        TransactedList al = build(false);
        beforeWrite();

        if (TEST_EXCEPTIONS) {
            try {
                al.remove(-1);
                fail("should throw an IndexOutOfBoundsException -- 1");
            } catch (IndexOutOfBoundsException e) {
            }
            try {
                al.remove(14);
                fail("should throw an IndexOutOfBoundsException -- 2");
            } catch (IndexOutOfBoundsException e) {
            }
        }

        assertion("a".equals(al.remove(5)), "checking returnvalue remove -- 1");
        assertion("a".equals(al.get(0)) && null == al.get(5) && "c".equals(al.get(1)) && "u".equals(al.get(2)), "checking remove ... -- 1");
        assertion("a".equals(al.get(6)) && "c".equals(al.get(7)) && "n".equals(al.get(3)) && "i".equals(al.get(4)), "checking remove ... -- 2");
        assertion(al.size() == 13, "checking new size -- 1");
        randomStop();
        assertion(al.remove(5) == null, "checking returnvalue remove -- 2");
        assertion(al.size() == 12, "checking new size -- 2");
        assertion(al.remove(11) == null, "checking returnvalue remove -- 3");
        assertion("a".equals(al.remove(0)), "checking returnvalue remove -- 4");
        assertion("u".equals(al.remove(1)), "checking returnvalue remove -- 5");
        randomStop();
        assertion("i".equals(al.remove(2)), "checking returnvalue remove -- 6");
        assertion("a".equals(al.remove(2)), "checking returnvalue remove -- 7");
        assertion("u".equals(al.remove(3)), "checking returnvalue remove -- 8");
        assertion("a".equals(al.remove(5)), "checking returnvalue remove -- 9");
        assertion("i".equals(al.remove(4)), "checking returnvalue remove -- 10");
        afterWrite();
        beforeRead();
        assertion("c".equals(al.get(0)) && "c".equals(al.get(2)) && "n".equals(al.get(3)) && "n".equals(al.get(1)), "checking remove ... -- 3");
        assertion(al.size() == 4, "checking new size -- 3");
        afterRead();
        beforeWrite();
        al.remove(0);
        al.remove(0);
        randomStop();
        al.remove(0);
        al.remove(0);
        assertion(al.size() == 0, "checking new size -- 4");
        afterWrite();
        al = createList();
        beforeWrite();

        if (TEST_EXCEPTIONS) {
            try {
                al.remove(0);
                fail("should throw an IndexOutOfBoundsException -- 3");
            } catch (IndexOutOfBoundsException e) {
            }
        }

        afterWrite();
    }

    private void test_set() {
        log("set(int,java.lang.Object)java.lang.Object");
        TransactedList al = createList();
        beforeWrite();

        if (TEST_EXCEPTIONS) {
            try {
                al.set(-1, "a");
                fail("should throw an IndexOutOfBoundsException -- 1");
            } catch (IndexOutOfBoundsException e) {
            }
            randomStop();
            try {
                al.set(0, "a");
                fail("should throw an IndexOutOfBoundsException -- 2");
            } catch (IndexOutOfBoundsException e) {
            }
        }

        afterWrite();
        al = build(false);
        beforeWrite();

        if (TEST_EXCEPTIONS) {
            try {
                al.set(-1, "a");
                fail("should throw an IndexOutOfBoundsException -- 3");
            } catch (IndexOutOfBoundsException e) {
            }
            try {
                al.set(14, "a");
                fail("should throw an IndexOutOfBoundsException -- 4");
            } catch (IndexOutOfBoundsException e) {
            }
        }

        randomStop();
        assertion("a".equals(al.set(5, "b")), "checking returnvalue of set -- 1");
        assertion("a".equals(al.set(0, null)), "checking returnvalue of set -- 2");
        afterWrite();
        beforeRead();
        assertion("b".equals(al.get(5)), "checking effect of set -- 1");
        assertion(al.get(0) == null, "checking effect of set -- 2");
        afterRead();
        beforeWrite();
        assertion("b".equals(al.set(5, "a")), "checking returnvalue of set -- 3");
        assertion(al.set(0, null) == null, "checking returnvalue of set -- 4");
        randomStop();
        assertion("a".equals(al.get(5)), "checking effect of set -- 3");
        assertion(al.get(0) == null, "checking effect of set -- 4");
        afterWrite();
    }

    private void test_contains() {
        log("contains(java.lang.Object)boolean");
        TransactedList al = createList();
        beforeRead();
        assertion(!al.contains(null), "checking empty List -- 1");
        assertion(!al.contains(al), "checking empty List -- 2");
        afterRead();
        al = build(false);
        beforeRead();
        assertion(al.contains(null), "check contains ... -- 1");
        randomStop();
        assertion(al.contains("a"), "check contains ... -- 2");
        assertion(al.contains("c"), "check contains ... -- 3");
        assertion(!al.contains(this), "check contains ... -- 4");
        afterRead();
        randomStop();
        beforeWrite();
        al.remove(6);
        assertion(al.contains(null), "check contains ... -- 5");
        al.remove(12);
        assertion(!al.contains(null), "check contains ... -- 6");
        assertion(!al.contains("b"), "check contains ... -- 7");
        randomStop();
        assertion(!al.contains(al), "check contains ... -- 8");
        afterWrite();
    }

    private void test_isEmpty() {
        log("isEmpty()boolean");
        TransactedList al = createList();
        beforeWrite();
        assertion(al.isEmpty(), "checking returnvalue -- 1");
        al.add("A");
        randomStop();
        assertion(!al.isEmpty(), "checking returnvalue -- 2");
        al.remove(0);
        assertion(al.isEmpty(), "checking returnvalue -- 3");
        afterWrite();
    }

    @SuppressWarnings("unchecked")
    private void test_indexOf() {
        log("indexOf(java.lang.Object)int");
        TransactedList al = createList();
        beforeRead();
        assertion(al.indexOf(null) == -1, "checks on empty list -- 1");
        assertion(al.indexOf(al) == -1, "checks on empty list -- 2");
        String o = new String();
        afterRead();
        al = build(false);
        beforeRead();
        assertion(al.indexOf(o) == -1, " doesn't contain -- 1");
        assertion(al.indexOf("a") == 0, "contains -- 2");
        afterRead();
        beforeWrite();
        assertion(al.indexOf(o) == -1, "contains -- 3");
        randomStop();
        al.add(9, o);
        assertion(al.indexOf(o) == 9, "contains -- 4");
        assertion(al.indexOf(new Object()) == -1, "doesn't contain -- 5");
        assertion(al.indexOf(null) == 6, "null was added to the Vector");
        al.remove(6);
        assertion(al.indexOf(null) == 13, "null was added twice to the Vector");
        al.remove(13);
        assertion(al.indexOf(null) == -1, "null was removed to the Vector");
        assertion(al.indexOf("c") == 1, "contains -- 6");
        randomStop();
        assertion(al.indexOf("u") == 2, "contains -- 7");
        assertion(al.indexOf("n") == 3, "contains -- 8");
        afterWrite();
    }

    @SuppressWarnings("unchecked")
    private void test_size() {
        log("size()int");
        TransactedList al = createList();
        beforeRead();
        assertion(al.size() == 0, "check on size -- 1");
        afterRead();
        Collection c = Arrays.asList(build(true).toArray());
        beforeWrite();
        al.addAll(c);
        assertion(al.size() == 14, "check on size -- 1");
        al.remove(5);
        randomStop();
        assertion(al.size() == 13, "check on size -- 1");
        al.add(4, "G");
        assertion(al.size() == 14, "check on size -- 1");
        afterWrite();
    }

    @SuppressWarnings("unchecked")
    private void test_lastIndexOf() {
        log("lastIndexOf(java.lang.Object)int");
        TransactedList al = createList();
        beforeRead();
        assertion(al.lastIndexOf(null) == -1, "checks on empty list -- 1");
        assertion(al.lastIndexOf(al) == -1, "checks on empty list -- 2");
        afterRead();
        String o = new String();
        al = build(false);
        beforeRead();
        assertion(al.lastIndexOf(o) == -1, " doesn't contain -- 1");
        assertion(al.lastIndexOf("a") == 12, "contains -- 2");
        afterRead();
        beforeWrite();
        assertion(al.lastIndexOf(o) == -1, "contains -- 3");
        al.add(9, o);
        assertion(al.lastIndexOf(o) == 9, "contains -- 4");
        assertion(al.lastIndexOf(new Object()) == -1, "doesn't contain -- 5");
        randomStop();
        assertion(al.lastIndexOf(null) == 14, "null was added to the Vector");
        al.remove(14);
        assertion(al.lastIndexOf(null) == 6, "null was added twice to the Vector");
        al.remove(6);
        assertion(al.lastIndexOf(null) == -1, "null was removed to the Vector");
        afterWrite();
        beforeRead();
        assertion(al.lastIndexOf("c") == 7, "contains -- 6, got " + al.lastIndexOf("c"));
        assertion(al.lastIndexOf("u") == 9, "contains -- 7, got " + al.lastIndexOf("u"));
        assertion(al.lastIndexOf("n") == 10, "contains -- 8, got " + al.lastIndexOf("n"));
        afterRead();
    }

    @SuppressWarnings("unchecked")
    private void test_toArray() {
        log("toArray()[java.lang.Object");
        TransactedList v = createList();
        beforeRead();
        Object o[] = v.toArray();
        assertion(o.length == 0, "checking size Object array");
        afterRead();
        beforeWrite();
        v.add("a");
        v.add(null);
        v.add("b");
        o = v.toArray();
        assertion(o[0] == "a" && o[1] == null && o[2] == "b", "checking elements -- 1");
        assertion(o.length == 3, "checking size Object array");
        afterWrite();

        log("toArray([java.lang.Object)[java.lang.Object");
        v = createList();
        beforeWrite();

        if (TEST_EXCEPTIONS) {
            try {
                v.toArray(null);
                fail("should throw NullPointerException -- 1");
            } catch (NullPointerException ne) {
            }
        }

        v.add("a");
        v.add(null);
        v.add("b");
        String sa[] = new String[5];
        sa[3] = "deleteme";
        sa[4] = "leavemealone";
        afterWrite();
        beforeRead();
        assertion(v.toArray(sa) == sa, "sa is large enough, no new array created");
        assertion(sa[0] == "a" && sa[1] == null && sa[2] == "b", "checking elements -- 1" + sa[0] + ", " + sa[1] + ", " + sa[2]);
        assertion(sa.length == 5, "checking size Object array");
        assertion(sa[3] == null && sa[4] == "leavemealone", "check other elements -- 1" + sa[3] + ", " + sa[4]);
        afterRead();
        v = build(false);
        beforeRead();

        if (TEST_EXCEPTIONS) {
            try {
                v.toArray(null);
                fail("should throw NullPointerException -- 2");
            } catch (NullPointerException ne) {
            }
            try {
                v.toArray(new Class[5]);
                fail("should throw an ArrayStoreException");
            } catch (ArrayStoreException ae) {
            }
        }

        afterRead();
        beforeWrite();
        v.add(null);
        String sar[];
        sa = new String[15];
        sar = (String[]) v.toArray(sa);
        assertion(sar == sa, "returned array is the same");
        afterWrite();
    }

    @SuppressWarnings("unchecked")
    private void test_MC_iterator() {
        log("ModCount(in)iterator");
        TransactedList xal = build(true);
        Collection c = Arrays.asList(xal.toArray());
        beforeWrite();
        Iterator it = xal.iterator();
        xal.remove(1);
        try {
            it.next();
            fail("should throw a ConcurrentModificationException -- 1");
        } catch (ConcurrentModificationException ioobe) {
        }
        afterWrite();
        beforeWrite();
        Iterator it2 = xal.iterator();
        afterWrite();

        if (TEST_EXCEPTIONS) {
            try {
                it2.next();

                // No pblm if tx not committed (Cf TransactedListTest3)
                if (Transaction.getCurrent() == null)
                    fail("should throw a ConcurrentModificationException -- 2");
            } catch (ConcurrentModificationException ioobe) {
            }
        }

        TransactedList al = build(false);
        beforeRead();
        it = al.iterator();
        al.get(0);
        al.contains(null);
        al.isEmpty();
        al.indexOf(null);
        al.lastIndexOf(null);
        al.size();
        al.toArray();
        al.toArray(new String[10]);

        try {
            it.next();
        } catch (ConcurrentModificationException ioobe) {
            fail("should not throw a ConcurrentModificationException -- 3");
        }

        afterRead();
        beforeWrite();
        it = al.iterator();
        al.add("b");

        if (TEST_EXCEPTIONS) {
            try {
                it.next();
                fail("should throw a ConcurrentModificationException -- 4");
            } catch (ConcurrentModificationException ioobe) {
            }
        }

        it = al.iterator();
        al.add(3, "b");

        if (TEST_EXCEPTIONS) {
            try {
                it.next();
                fail("should throw a ConcurrentModificationException -- 5");
            } catch (ConcurrentModificationException ioobe) {
            }
        }

        it = al.iterator();
        al.addAll(c);

        if (TEST_EXCEPTIONS) {
            try {
                it.next();
                fail("should throw a ConcurrentModificationException -- 6");
            } catch (ConcurrentModificationException ioobe) {
            }
        }

        it = al.iterator();
        al.addAll(2, c);

        if (TEST_EXCEPTIONS) {
            try {
                it.next();
                fail("should throw a ConcurrentModificationException -- 7");
            } catch (ConcurrentModificationException ioobe) {
            }
        }

        afterWrite();
        beforeWrite();
        it = al.iterator();
        al.remove(2);

        if (TEST_EXCEPTIONS) {
            try {
                it.next();
                fail("should throw a ConcurrentModificationException -- 8");
            } catch (ConcurrentModificationException ioobe) {
            }
        }

        it = al.iterator();
        al.clear();

        if (TEST_EXCEPTIONS) {
            try {
                it.next();
                fail("should throw a ConcurrentModificationException -- 9");
            } catch (ConcurrentModificationException ioobe) {
            }
        }

        afterWrite();
    }
}
