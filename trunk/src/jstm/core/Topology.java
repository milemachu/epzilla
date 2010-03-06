
package jstm.core;

import java.util.*;

import jstm.misc.Debug;
import jstm.misc.Utils;

final class Topology {

    private final TransactionManager _manager;

    private final HashMap<String, Site> _knownSites = new HashMap<String, Site>();

    private final ArrayList<Group> _groups = new ArrayList<Group>();

    private final ArrayList<Connection> _connections = new ArrayList<Connection>();

    private Share[] _openShares = new Share[0];

    private boolean _invalid;

    public Topology(TransactionManager manager) {
        _manager = manager;
    }

    public boolean idle() {
        if (_groups.size() > 0)
            return false;

        if (_connections.size() > 0)
            return false;

        return true;
    }

    public void addSite(Site site) {
        _knownSites.put(site.getUid(), site);
    }

    public ArrayList<Group> getGroups() {
        return _groups;
    }

    public Share[] getOpenShares() {
        return _openShares;
    }

    public ArrayList<Connection> getConnections() {
        return _connections;
    }

    public boolean isInvalid() {
        return _invalid;
    }

    public void markAsInvalid() {
        _invalid = true;
    }

    public Site getOrCreateSite(String uid, Connection connection) {
        Site site;

        synchronized (_manager.getLock()) {
            _manager.assertThreadIsAllowed();

            site = _knownSites.get(uid);

            if (site == null)
                _knownSites.put(uid, site = _manager.getLocalSite().createSite(uid, connection));
        }

        return site;
    }

    public void recompute(Transaction transaction) {
        HashMap<Share, HashSet<Location>> map = new HashMap<Share, HashSet<Location>>();

        for (Share share : _openShares)
            share.close();

        // Walk local locations to find open shares

        walkLocation(transaction, _manager.getLocalSite(), map);

        for (Group group : _groups)
            walkLocation(transaction, group, map);

        _openShares = Utils.copy(map.keySet(), new Share[map.size()]);

        for (Share share : _openShares)
            share.open();

        // Walk all other locations to find remaining shares

        for (Site site : _knownSites.values()) {
            if (site != _manager.getLocalSite()) {
                walkLocation(transaction, site, map);

                for (TransactedObject o : site.getObjectsArray()) {
                    if (o instanceof Group)
                        walkLocation(transaction, (Group) o, map);
                }
            }
        }

        // Register shares if needed and their connections

        for (Map.Entry<Share, HashSet<Location>> entry : map.entrySet()) {
            Share share = entry.getKey();

            if (share.getId() == 0)
                _manager.registerAsTopologyInvolved(share);

            ArrayList<Connection> connections = share.getConnections();

            connections.clear();

            for (Location location : entry.getValue()) {
                if (location != _manager.getLocalSite()) {
                    Debug.assertion(location.getRoute() != null);

                    if (!connections.contains(location.getRoute()))
                        connections.add(location.getRoute());
                }
            }
        }

        _invalid = false;
    }

    private void walkLocation(Transaction transaction, Location location, HashMap<Share, HashSet<Location>> map) {
        for (Share share : location.getOpenShares().readWithoutRecord(transaction)) {
            HashSet<Location> list = map.get(share);

            if (list == null)
                map.put(share, list = new HashSet<Location>());

            list.add(location);
        }
    }

    /**
     * Only for GWT
     */
    public Site updateSiteKey(String previous, String current) {
        Site site = _knownSites.remove(previous);
        _knownSites.put(current, site);
        return site;
    }

    // Connections

    @SuppressWarnings("unchecked")
    public void prepareConnections(Transaction transaction) {
        for (int i = 0; i < getConnections().size(); i++)
            getConnections().get(i).reset();

        // Send newly open shares

        if (transaction.modifiesTopology() && transaction.getPrivateObjects() != null) {
            // getPrivateObjects() can be null if topology is modified by an
            // initial object snapshot

            for (Group group : getGroups()) {
                TransactedSetVersion<Share> version = (TransactedSetVersion) transaction.getPrivateObjects().get(group);

                if (version != null && version.getWrites() != null) {
                    for (Map.Entry<Share, Object> entry : version.getWrites().entrySet()) {
                        if (entry.getValue() != TransactedObject.Removal.Instance) {
                            Share addedShare = entry.getKey();
                            TransactedObject.Version shareVersion = transaction.getPrivateObjects().get(addedShare);
                            group.getRoute().addShared(addedShare, shareVersion);
                        }
                    }
                }
            }
        }

        transaction.addCallsAsNewObjectsToConnections();

        // Update connections from shares

        for (Share share : getOpenShares())
            share.updateAndPrepareConnections(transaction);

        if (transaction.getTags() != null) {
            for (Map.Entry<Object, Object> entry : transaction.getTags().entrySet()) {
                addTagToConnections(transaction, entry.getKey());
                addTagToConnections(transaction, entry.getValue());
            }
        }
    }

    private void addTagToConnections(Transaction transaction, Object o) {
        TransactedObject to = null;
        TransactedObject.Version version = null;

        if (o instanceof TransactedObject)
            to = (TransactedObject) o;

        if (to != null) {
            for (int i = 0; i < getConnections().size(); i++) {
                Connection c = getConnections().get(i);

                if (c.involved()) {
                    if (version == null && transaction.getPrivateObjects() != null)
                        version = transaction.getPrivateObjects().get(to);

                    c.addNewWithVersion(to, version);
                }
            }

            for (Share share : getOpenShares())
                for (Connection c : share.getConnections())
                    if (c.containsNew(to))
                        if (share.contains(to))
                            c.addShared(to, version);
        }
    }
}
