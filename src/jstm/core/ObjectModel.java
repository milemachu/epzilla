/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.core;

public abstract class ObjectModel {

    public static final int TRANSACTED_ARRAY_CLASS_ID = Integer.MAX_VALUE;

    public abstract String getUID();

    public abstract String getXML();

    public abstract TransactedObject createInstance(int classId, Connection route);

    public abstract int getClassCount();

    public static final class Default extends ObjectModel {

        public static final int TRANSACTED_OBJECT_CLASS_ID = 0;

        public static final int TRANSACTED_LIST_CLASS_ID = 1;

        public static final int TRANSACTED_SET_CLASS_ID = 2;

        public static final int TRANSACTED_MAP_CLASS_ID = 3;

        public static final int SHARE_CLASS_ID = 4;

        public static final int GROUP_CLASS_ID = 5;

        public static final int COUNT = 6;

        //

        public static final String UID = "default";

        @Override
        public String getUID() {
            return UID;
        }

        @Override
        public String getXML() {
            throw new IllegalStateException("Default is present on all sites");
        }

        @Override
        @SuppressWarnings("unchecked")
        public TransactedObject createInstance(int classId, Connection route) {
            switch (classId) {
                case TRANSACTED_OBJECT_CLASS_ID:
                    return new TransactedObject();
                case TRANSACTED_LIST_CLASS_ID:
                    return new TransactedList();
                case TRANSACTED_SET_CLASS_ID:
                    return new TransactedSet();
                case TRANSACTED_MAP_CLASS_ID:
                    return new TransactedMap();
                case SHARE_CLASS_ID:
                    return new Share();
                case GROUP_CLASS_ID:
                    return new Group(route);
            }

            throw new IllegalArgumentException("Unknown class id: " + classId);
        }

        @Override
        public int getClassCount() {
            return COUNT;
        }
    }
}
