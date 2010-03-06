/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.core;

import java.io.IOException;

import jstm.misc.Debug;

public abstract class Location extends TransactedObject {

    private final Connection _route;

    private TransactedSet<Share> _openShares;

    protected Location(Connection route) {
        _route = route;
    }

    protected final void createOpenShares() {
        _openShares = createTransactedSet();
    }

    /**
     * Overriden by NSTM
     */
    protected TransactedSet<Share> createTransactedSet() {
        return new TransactedSet<Share>();
    }

    protected final Connection getRoute() {
        if (Debug.Level > 0 && _route == null)
            Debug.assertion(((Site) this).getManager() != null);

        return _route;
    }

    /**
     * Adding a share to this set will open the share. It the share was not
     * already open on the local Site, its content will be downloaded and it
     * will then stay synchronized with the other locations which have open it.
     */
    public final TransactedSet<Share> getOpenShares() {
        return _openShares;
    }

    @Override
    protected TransactedObject.Version createVersion(Transaction transaction) {
        return new Version(this);
    }

    @Override
    protected TransactedObject.Version getSnapshot(Transaction transaction) {
        return new Version(this);
    }

    @Override
    protected void serialize(TransactedObject.Version version, Writer writer) throws IOException {
        writer.writeTransactedObject(_openShares);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void deserialize(TransactedObject.Version version, Reader reader) throws IOException {
        TransactedSet<Share> openShares = (TransactedSet<Share>) reader.readTransactedObject();

        // Should be set only on creation or to same value on site snapshot
        Debug.assertion(_openShares == null || _openShares == openShares);
        Debug.assertion(openShares.getId() < 0);
        _openShares = openShares;
    }

    protected static class Version extends TransactedObject.Version {

        protected Version(Location location) {
            super(location);
        }

        private Location getLocation() {
            return (Location) getSharedObject();
        }

        @Override
        protected void walkReferences(ReferencesWalker walker, boolean privateOnly, boolean trackRemovals) {
            walker.onReference(getLocation().getOpenShares(), false, new OpenSharesReference());
        }

        private final class OpenSharesReference extends Reference {

            public OpenSharesReference() {
            }

            private TransactedObject getObject() {
                return Version.this.getSharedObject();
            }

            @Override
            public boolean canDelete() {
                return false;
            }

            @Override
            public void delete() {
                throw new UnsupportedOperationException();
            }

            @Override
            public int hashCode() {
                return getObject().hashCode();
            }

            @Override
            public boolean equals(Object obj) {
                if (obj instanceof OpenSharesReference) {
                    OpenSharesReference other = (OpenSharesReference) obj;

                    if (other.getObject() == getObject())
                        return true;
                }

                return false;
            }

            @Override
            public String toString() {
                return "Open Shares";
            }
        }
    }
}
