/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.core;

import jstm.misc.Debug;
import jstm.misc.Utils;

/**
 * A site is where an instance of a replicated object stands. In most cases, it
 * identifies a JVM.
 */
public final class Site extends Location {

    /**
     * Notifies you of all commits and aborts on this site. Can be added only to
     * the local site.
     */
    public static interface Listener {

        void onCommitted(Transaction transaction);

        void onAborted(Transaction transaction);
    }

    /**
     * This listener should be used for logging or debugging purposes only. It
     * should not be needed for normal usage of JSTM. Those methods are called
     * during key operations on transactions. The transaction manager lock is
     * held while calling these methods so calls must return quickly and should
     * not raise exceptions.
     */
    public interface InternalListener {

        void onCommitting(Transaction transaction);

        void onCommitted(Transaction transaction, long newVersion);

        void onAborted(Transaction transaction);
    }

    public static final int MAX_PENDING_COMMIT_COUNT = 10;

    private static final Site _local = new Site();

    private final TransactionManager _manager;

    private final ObjectModel _defaultObjectModel;

    private String _uid;

    private TransactedObject[] _objects = new TransactedObject[100];

    /**
     * Local site.
     */
    protected Site() {
        this(Utils.createUID(), null, true);
    }

    /**
     * Remote site, deserialized.
     */
    protected Site(String uid, Connection route) {
        this(uid, route, false);
    }

    private Site(String uid, Connection route, boolean manager) {
        super(route);

        _uid = uid;
        _defaultObjectModel = createDefaultObjectModel();

        if (manager) {
            createOpenShares();
            _manager = new TransactionManager(this);
        } else
            _manager = null;

        setId(TransactedObject.Id.SITE.Value);
    }

    /**
     * The local site usually represents the current JVM.
     */
    public static Site getLocal() {
        return _local;
    }

    public static Site createTestSite() {
        return new Site();
    }

    protected TransactionManager getManager() {
        if (_manager == null)
            throw new UnsupportedOperationException("This method can be called only on the local site");

        return _manager;
    }

    protected String getUid() {
        return _uid;
    }

    /**
     * Only for GWT
     */
    protected void setUid(String uid) {
        Site previous = getManager().getTopology().updateSiteKey(_uid, uid);
        Debug.assertion(previous == this);
        _uid = uid;
    }

    protected ObjectModel getDefaultObjectModel() {
        return _defaultObjectModel;
    }

    protected TransactedObject getObject(int id) {
        if (id < 0)
            id = -id;

        if (id < _objects.length)
            return _objects[id];

        return null;
    }

    protected void setObject(int id, TransactedObject o) {
        Debug.assertion(o.getId() == 0);
        o.setId(id);

        Debug.assertion(o.getOrigin() == null);
        o.setOrigin(this);

        if (id < 0)
            id = -id;

        if (id >= _objects.length) {
            int newLength = _objects.length * 3 / 2 + 1;

            if (newLength < id)
                newLength = id;

            TransactedObject[] newArray = new TransactedObject[newLength];
            Utils.copy(_objects, 0, newArray, 0, _objects.length);
            _objects = newArray;
        }

        Debug.assertion(_objects[id] == null);
        _objects[id] = o;
    }

    protected void setNull(int id) {
        if (id < 0)
            id = -id;

        Debug.assertion(_objects[id] != null);
        _objects[id] = null;
    }

    protected TransactedObject[] getObjectsArray() {
        return _objects;
    }

    //

    public long getVersionNumber() {
        return getManager().getVersionNumber();
    }

    public boolean idle() {
        return getManager().idle();
    }

    public int getPendingCommitCount() {
        return getManager().getPendingCommitCount();
    }

    /**
     * Non transacted method (No need to have a current transaction)
     */
    public Transaction startTransaction() {
        return getManager().startTransaction();
    }

    public void runTransacted(Runnable runnable) {
        getManager().runTransacted(runnable);
    }

    //

    // Overriden by NSTM
    protected Transaction createTransaction(long id, long versionOnStart) {
        return new Transaction(this, id, versionOnStart);
    }

    // Overriden by NSTM
    protected Site createSite(String uid, Connection route) {
        return new Site(uid, route);
    }

    // Overriden by NSTM
    protected ObjectModel createDefaultObjectModel() {
        return new ObjectModel.Default();
    }

    // Overriden by NSTM
    protected TransactedArray createTransactedArray(int length) {
        return new TransactedArray(length);
    }

    // Can be overriden by NSTM
    protected ObjectModel createObjectModel(String uid, String xml) {
        throw new IllegalStateException("You must register the same object model on all sites.");
    }

    public void registerObjectModel(ObjectModel model) {
        synchronized (getManager().getLock()) {
            getManager().registerObjectModel(model);
        }
    }

    //

    /**
     * Non transacted method (No need to have a current transaction)
     */
    public void addInternalListener(InternalListener listener) {
        getManager().addInternalListener(listener);
    }

    /**
     * Non transacted method (No need to have a current transaction)
     */
    public void removeInternalListener(InternalListener listener) {
        getManager().removeInternalListener(listener);
    }

    /**
     * Non transacted method (No need to have a current transaction)
     */
    protected void addSiteListener(Object key, Site.Listener listener) {
        getManager().addSiteListener(key, listener);
    }

    /**
     * Non transacted method (No need to have a current transaction)
     */
    public void addSiteListener(Site.Listener listener) {
        getManager().addSiteListener(listener);
    }

    /**
     * Non transacted method (No need to have a current transaction)
     */
    protected void removeTransactionListener(Object key) {
        getManager().removeTransactionListener(key);
    }

    /**
     * Non transacted method (No need to have a current transaction)
     */
    public void removeTransactionListener(Site.Listener listener) {
        getManager().removeTransactionListener(listener);
    }

    //

    public void allowThread() {
        getManager().allowThread();
    }

    @Override
    protected final int getClassId() {
        throw new UnsupportedOperationException();
    }
}
