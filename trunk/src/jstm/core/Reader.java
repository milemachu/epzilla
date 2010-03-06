/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.core;

import java.io.IOException;
import java.util.HashMap;

import jstm.core.TransactedObject.Version;
import jstm.misc.BinaryReader;
import jstm.misc.Debug;

/**
 * Internal class.
 */
public final class Reader extends BinaryReader {

    private final Transport _transport;

    private final HashMap<TransactedObject.Id, TransactedObject> _topology = new HashMap<TransactedObject.Id, TransactedObject>();

    private final HashMap<TransactedObject.Id, ObjectModel> _objectModels = new HashMap<TransactedObject.Id, ObjectModel>();

    private Transaction _transaction;

    protected Reader(Transport transport, Filler filler) {
        super(filler);

        _transport = transport;
    }

    public void readMessage() throws IOException {
        switch (readByte()) {
            case Transport.REQUEST_INITIAL_OBJECTS: {
                if (Debug.MESSAGING)
                    Transport.logRequestInitialObjects(_transport.getLocalSite(), true);

                _transport.sendInitialObjects();
                break;
            }
            case Transport.INITIAL_OBJECTS: {
                Transaction transaction = _transport.createFakeTransaction();
                deserialize(transaction, false);

                if (Debug.MESSAGING)
                    Transport.logInitialObjects(_transport.getLocalSite(), true, transaction);

                _transport.receivedInitialObjects(transaction);
                break;
            }
            case Transport.COMMIT: {
                Transaction transaction = _transport.getManager().startTransaction();
                deserialize(transaction, false);
                long newServerVersion = readLong();

                if (Debug.MESSAGING)
                    Transport.logCommit(_transport.getLocalSite(), true, transaction, newServerVersion);

                _transport.receivedCommit(transaction, newServerVersion);
                break;
            }
            case Transport.REQUEST_COMMIT: {
                long id = readLong();
                long replicatedServerVersionOnInterception = readLong();

                Transaction transaction = _transport.getManager().startTransaction(id, replicatedServerVersionOnInterception, true);
                deserialize(transaction, false);

                short dependencyCount = readShort();
                long[] dependencies = null;

                if (dependencyCount > 0) {
                    dependencies = new long[dependencyCount];

                    for (int i = 0; i < dependencies.length; i++)
                        dependencies[i] = readLong();
                }

                if (Debug.MESSAGING)
                    Transport.logCommitRequest(_transport.getLocalSite(), true, transaction, replicatedServerVersionOnInterception, dependencies);

                _transport.receivedCommitRequest(transaction, replicatedServerVersionOnInterception, dependencies);
                break;
            }
            case Transport.COMMIT_RESULT: {
                long id = readLong();
                boolean success = readBoolean();
                long newServerVersion = readLong();

                if (Debug.MESSAGING)
                    Transport.logCommitResult(_transport.getLocalSite(), true, id, success, newServerVersion);

                _transport.receivedCommitResult(id, success, newServerVersion);
                break;
            }
            case Transport.COMMIT_RESULT_MODIFIED: {
                long id = readLong();
                Transaction transaction = _transport.getManager().startTransaction();
                deserialize(transaction, true);
                long newServerVersion = readLong();

                if (Debug.MESSAGING)
                    Transport.logCommitResultModified(_transport.getLocalSite(), true, id, transaction, newServerVersion);

                _transport.receivedCommitResultModified(id, transaction, newServerVersion);
                break;
            }
            case Transport.REPLICATION_REPORT: {
                long replicatedServerVersion = readLong();
                long replicatedTransactionId = readLong();

                if (Debug.MESSAGING)
                    Transport.logReplicationReport(_transport.getLocalSite(), true, replicatedServerVersion, replicatedTransactionId);

                _transport.receivedReplicationReport(replicatedServerVersion, replicatedTransactionId);
                break;
            }
            case Transport.OBJECT_MODEL_IS_KNOWN: {
                String uid = readString();

                if (Debug.MESSAGING)
                    Transport.logObjectModelIsKnown(_transport.getLocalSite(), true, uid);

                _transport.receivedObjectModelIsKnown(uid);
                break;
            }
        }

        assertCounter();
    }

    //

    public TransactedObject readTransactedObject() throws IOException {
        int id = readInteger();
        return readTransactedObject(id);
    }

    private TransactedObject readTransactedObject(int id) throws IOException {
        if (id == TransactedObject.Id.NULL.Value)
            return null;

        if (id < 0)
            return readTopologyInvolved(id);

        if (id == TransactedObject.Id.DISCONNECTED.Value)
            return null;

        return readSite().getObject(id);
    }

    private Site readSite() throws IOException {
        int id = readInteger();
        return (Site) readTopologyInvolved(id);
    }

    private TransactedObject readTopologyInvolved(int id) throws IOException {
        TransactedObject.Id toid = new TransactedObject.Id(id);
        TransactedObject o = _topology.get(toid);

        if (o == null) {
            int actualId = readInteger();

            if (actualId == TransactedObject.Id.SITE.Value) {
                o = _transport.getManager().getTopology().getOrCreateSite(readString(), _transport);
                _topology.put(toid, o);
            } else {
                Site origin = readSite();
                int classId = readInteger();

                TransactedObject previous = origin.getObject(actualId);

                if (previous != null) {
                    Debug.assertion(previous.getId() == actualId);
                    Debug.assertion(previous.getClassId() == classId);
                    o = previous;
                } else {
                    ObjectModel om = _transport.getLocalSite().getDefaultObjectModel();
                    o = om.createInstance(classId, _transport);
                    origin.setObject(actualId, o);
                }

                _topology.put(toid, o);
            }

            Version version = o.createVersion(_transaction);
            o.deserialize(version, this);
            version.commit();
        }

        return o;
    }

    private ObjectModel readObjectModel(int id) throws IOException {
        TransactedObject.Id toid = new TransactedObject.Id(id);
        ObjectModel model = _objectModels.get(toid);

        if (model == null) {
            String uid = readString();
            boolean known = readBoolean();

            if (known) {
                synchronized (_transport.getManager().getLock()) {
                    model = _transport.getManager().getKnownObjectModel(uid);
                }

                Debug.assertion(model != null);
            } else {
                String xml = readString();
                model = _transport.getLocalSite().createObjectModel(uid, xml);

                synchronized (_transport.getManager().getLock()) {
                    _transport.getManager().registerObjectModel(model);
                }
            }

            _objectModels.put(toid, model);
        }

        return model;
    }

    private void deserialize(Transaction transaction, boolean results) throws IOException {
        _transaction = transaction;

        int id = readInteger();

        while (id > TransactedObject.Id.END.Value) {
            ObjectModel om = readObjectModel(id);
            Site origin = readSite();
            id = readInteger();

            TransactedObject object = origin.getObject(id);
            int classId = readInteger();

            if (classId != ObjectModel.TRANSACTED_ARRAY_CLASS_ID) {
                if (object == null) {
                    object = om.createInstance(classId, _transport);
                    origin.setObject(id, object);
                }
            } else {
                int length = readInteger();

                if (object == null) {
                    object = _transport.getLocalSite().createTransactedArray(length);
                    origin.setObject(id, object);
                }
            }

            id = readInteger();
        }

        while (id == TransactedObject.Id.SNAPSHOTS.Value || id > TransactedObject.Id.END.Value) {
            if (id > TransactedObject.Id.END.Value) {
                TransactedObject object = readTransactedObject(id);
                Version version = object.createVersion(transaction);
                object.deserialize(version, this);
                version.commit();
            }

            id = readInteger();
        }

        while (id == TransactedObject.Id.VERSIONS.Value || id > TransactedObject.Id.END.Value) {
            if (id > TransactedObject.Id.END.Value) {
                TransactedObject object = readTransactedObject(id);
                Version version = object.createVersion(transaction);
                object.deserialize(version, this);
                transaction.getOrCreatePrivateObjects().put(object, version);
            }

            id = readInteger();
        }

        while (id == TransactedObject.Id.CALLS.Value || id > TransactedObject.Id.END.Value) {
            if (id > TransactedObject.Id.END.Value) {
                TransactedStructure s = (TransactedStructure) readTransactedObject(id);
                int index = readShort();
                Object[] args = s.deserializeArguments(index, this);
                transaction.getOrCreateCalls().add(new MethodCall(s, index, args));
            }

            id = readInteger();
        }

        if (id == TransactedObject.Id.TAGS.Value) {
            Debug.assertion(transaction.getTags() == null);
            transaction.setTags(new HashMap<Object, Object>());

            byte code;

            while ((code = readByte()) != Serializer.TAG_END)
                Serializer.deserializeTag(transaction.getTags(), code, this);

            id = readInteger();
        }

        Debug.assertion(id == TransactedObject.Id.END.Value);

        transaction.setOrigin(readSite());

        if (readBoolean())
            transaction.setAbortReason(readString());

        if (results && transaction.getCalls() != null) {
            for (int i = 0; i < transaction.getCalls().size(); i++) {
                Object result;

                if (readBoolean())
                    result = new MethodCall.Error(readString());
                else
                    result = transaction.getCalls().get(i).Structure.deserializeResult(i, this);

                transaction.getCalls().get(i).Result = result;
            }
        }

        _transaction = null;
    }
}
