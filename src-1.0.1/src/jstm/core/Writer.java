/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jstm.misc.BinaryWriter;
import jstm.misc.Debug;

/**
 * Internal class.
 */
public final class Writer extends BinaryWriter {

    private final Transport _transport;

    private final HashMap<TransactedObject, TransactedObject.Id> _topology = new HashMap<TransactedObject, TransactedObject.Id>();

    private final HashMap<String, TransactedObject.Id> _objectModels = new HashMap<String, TransactedObject.Id>();

    private int _nextId = -1; // Negative for topology related

    private Transaction _transaction;

    protected Writer(Transport transport, Flusher flusher) {
        super(flusher);

        _transport = transport;
    }

    public void writeRequestInitialObjects() throws IOException {
        if (Debug.MESSAGING)
            Transport.logRequestInitialObjects(_transport.getLocalSite(), false);

        writeByte(Transport.REQUEST_INITIAL_OBJECTS);
        flush();
    }

    public void writeInitialObjects(Transport transport, Transaction transaction) throws IOException {
        if (Debug.MESSAGING)
            Transport.logInitialObjects(_transport.getLocalSite(), false, transaction);

        writeByte(Transport.INITIAL_OBJECTS);
        serialize(transport, transaction, false);
        flush();
    }

    public void writeCommit(Connection connection, Transaction transaction, long newServerVersion) throws IOException {
        if (Debug.MESSAGING)
            Transport.logCommit(_transport.getLocalSite(), false, transaction, newServerVersion);

        writeByte(Transport.COMMIT);
        serialize(connection, transaction, false);
        writeLong(newServerVersion);
        flush();
    }

    public void writeCommitRequest(Connection connection, Transaction transaction, long replicatedServerVersionOnInterception, long[] dependencies) throws IOException {
        if (Debug.MESSAGING)
            Transport.logCommitRequest(_transport.getLocalSite(), false, transaction, replicatedServerVersionOnInterception, dependencies);

        writeByte(Transport.REQUEST_COMMIT);

        writeLong(transaction.getId());
        writeLong(replicatedServerVersionOnInterception);

        serialize(connection, transaction, false);

        if (dependencies == null)
            writeShort((short) 0);
        else {
            Debug.assertion(dependencies.length > 0);
            writeShort((short) dependencies.length);

            for (long dependency : dependencies)
                writeLong(dependency);
        }

        flush();
    }

    public void writeCommitResult(long id, boolean success, long newServerVersion) throws IOException {
        if (Debug.MESSAGING)
            Transport.logCommitResult(_transport.getLocalSite(), false, id, success, newServerVersion);

        writeByte(Transport.COMMIT_RESULT);
        writeLong(id);
        writeBoolean(success);
        writeLong(newServerVersion);
        flush();
    }

    public void writeCommitResultModified(Connection connection, long id, Transaction transaction, long newServerVersion) throws IOException {
        if (Debug.MESSAGING)
            Transport.logCommitResultModified(_transport.getLocalSite(), false, id, transaction, newServerVersion);

        writeByte(Transport.COMMIT_RESULT_MODIFIED);
        writeLong(id);
        serialize(connection, transaction, true);
        writeLong(newServerVersion);
        flush();
    }

    public void writeReplicationReport(long replicatedServerVersion, long replicatedTransactionId) throws IOException {
        if (Debug.MESSAGING)
            Transport.logReplicationReport(_transport.getLocalSite(), false, replicatedServerVersion, replicatedTransactionId);

        writeByte(Transport.REPLICATION_REPORT);
        writeLong(replicatedServerVersion);
        writeLong(replicatedTransactionId);
        flush();
    }

    public void writeObjectModelIsKnown(ObjectModel model) throws IOException {
        if (Debug.MESSAGING)
            Transport.logObjectModelIsKnown(_transport.getLocalSite(), false, model.getUID());

        writeByte(Transport.OBJECT_MODEL_IS_KNOWN);
        writeString(model.getUID());
        flush();
    }

    //

    public void writeTransactedObject(TransactedObject o) throws IOException {
        if (o.getId() == TransactedObject.Id.NULL.Value) {
            // Not registered
            Debug.assertion(o.getOrigin() == null);
            writeInteger(TransactedObject.Id.NULL.Value);
        } else if (o.getId() < 0) {
            writeTopologyInvolved(o);
        } else {
            writeInteger(o.getId());
            writeSite(o.getOrigin());
        }
    }

    private void writeSite(Site site) throws IOException {
        writeTopologyInvolved(site);
    }

    private void writeTopologyInvolved(TransactedObject o) throws IOException {
        TransactedObject.Id id = _topology.get(o);

        if (id != null)
            writeInteger(id.Value);
        else {
            _topology.put(o, id = new TransactedObject.Id(_nextId--));

            writeInteger(id.Value);
            writeInteger(o.getId());

            if (o.getId() == TransactedObject.Id.SITE.Value)
                writeString(((Site) o).getUid());
            else {
                writeSite(o.getOrigin());
                writeInteger(o.getClassId());
            }

            o.serialize(o.getSnapshot(_transaction), this);
        }
    }

    private void writeObjectModel(String uid) throws IOException {
        TransactedObject.Id id = _objectModels.get(uid);

        if (id != null)
            writeInteger(id.Value);
        else {
            boolean knownByReader = _transport.getObjectModelIsKnownByRemoteSite(uid);

            _objectModels.put(uid, id = new TransactedObject.Id(_nextId--));

            writeInteger(id.Value);
            writeString(uid);
            writeBoolean(knownByReader);

            if (!knownByReader) {
                ObjectModel model = _transport.getManager().getKnownObjectModel(uid);
                writeString(model.getXML());
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void serialize(Connection connection, Transaction transaction, boolean results) throws IOException {
        _transaction = transaction;

        // First register and send new objects

        if (connection.getNew().size() > 0) { // For SNAPSHOTS.Value
            for (Map.Entry<TransactedObject, TransactedObject.Version> entry : connection.getNew().entrySet()) {
                Debug.assertion(entry.getKey().getId() > 0);

                writeObjectModel(entry.getKey().getObjectModelUID());
                writeSite(entry.getKey().getOrigin());
                writeInteger(entry.getKey().getId());

                int classId = entry.getKey().getClassId();
                writeInteger(classId);

                if (classId == ObjectModel.TRANSACTED_ARRAY_CLASS_ID)
                    writeInteger(((TransactedArray) entry.getKey()).length());
            }

            // Then snapshot new sites and objects
            // TODO snapshot only if not null
            writeInteger(TransactedObject.Id.SNAPSHOTS.Value);

            for (Map.Entry<TransactedObject, TransactedObject.Version> entry2 : connection.getNew().entrySet()) {
                writeTransactedObject(entry2.getKey());
                entry2.getKey().serialize(entry2.getKey().getSnapshot(transaction), this);
            }
        }

        // Then object versions

        boolean firstVersion = true;

        for (Map.Entry<TransactedObject, TransactedObject.Version> entry : connection.getNew().entrySet()) {
            if (entry.getValue() != null) {
                if (firstVersion) {
                    writeInteger(TransactedObject.Id.VERSIONS.Value);
                    firstVersion = false;
                }

                writeTransactedObject(entry.getKey());
                entry.getKey().serialize(entry.getValue(), this);
            }
        }

        for (Map.Entry<TransactedObject, TransactedObject.Version> entry : connection.getShared().entrySet()) {
            if (entry.getValue() != null) {
                if (firstVersion) {
                    writeInteger(TransactedObject.Id.VERSIONS.Value);
                    firstVersion = false;
                }

                writeTransactedObject(entry.getKey());
                entry.getKey().serialize(entry.getValue(), this);
            }
        }

        //

        if (transaction.getCalls() != null) {
            writeInteger(TransactedObject.Id.CALLS.Value);

            for (MethodCall call : transaction.getCalls()) {
                writeTransactedObject(call.Structure);
                writeShort((short) call.Index);
                call.Structure.serializeArguments(call.Index, call.Arguments, this);
            }
        }

        if (transaction.getTags() != null) {
            boolean written = false;

            for (Map.Entry<Object, Object> entry : transaction.getTags().entrySet())
                written = Serializer.serializeTag(entry, written, this);

            if (written)
                writeByte(Serializer.TAG_END);
        }

        writeInteger(TransactedObject.Id.END.Value);
        writeSite(transaction.getOrigin());

        if (transaction.getAbortReason() != null && transaction.getAbortReason().length() > 0) {
            writeBoolean(true);
            writeString(transaction.getAbortReason());
        } else
            writeBoolean(false);

        if (results && transaction.getCalls() != null) {
            for (int i = 0; i < transaction.getCalls().size(); i++) {
                Object o = transaction.getCalls().get(i).Result;

                if (o instanceof MethodCall.Error) {
                    writeBoolean(true);
                    writeString(((MethodCall.Error) o).Message);
                } else {
                    writeBoolean(false);
                    transaction.getCalls().get(i).Structure.serializeResult(i, o, this);
                }
            }
        }

        _transaction = null;
    }
}
