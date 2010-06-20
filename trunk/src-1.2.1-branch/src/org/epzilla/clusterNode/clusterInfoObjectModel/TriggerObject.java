//==============================================================================
//                                                                              
//  THIS FILE HAS BEEN GENERATED BY JSTM                                        
//                                                                              
//==============================================================================

package org.epzilla.clusterNode.clusterInfoObjectModel;

import jstm.core.*;

public class TriggerObject extends jstm.core.TransactedStructure {

    public TriggerObject() {
        super(FIELD_COUNT);
    }

    public String getclientID() {
        return (String) getField(0);
    }

    public void setclientID(String value) {
        setField(0, value);
    }

    public String gettriggerID() {
        return (String) getField(1);
    }

    public void settriggerID(String value) {
        setField(1, value);
    }

    public String gettrigger() {
        return (String) getField(2);
    }

    public void settrigger(String value) {
        setField(2, value);
    }

    public static final int CLIENTID_INDEX = 0;

    public static final String CLIENTID_NAME = "clientID";

    public static final int TRIGGERID_INDEX = 1;

    public static final String TRIGGERID_NAME = "triggerID";

    public static final int TRIGGER_INDEX = 2;

    public static final String TRIGGER_NAME = "trigger";

    public static final int FIELD_COUNT = 3;

    @Override
    public String getFieldName(int index) {
        return getFieldNameStatic(index);
    }

    public static String getFieldNameStatic(int index) {
        switch (index) {
            case 0:
                return "clientID";
            case 1:
                return "triggerID";
            case 2:
                return "trigger";
            default:
                throw new java.lang.IllegalArgumentException();
        }
    }

    // Internal

    @Override
    protected int getClassId() {
        return 0;
    }

    @Override
    public String getObjectModelUID() {
        return "uQWTU8Mg7PUU1VKc0lWFgA";
    }

    private static final int[] NON_TRANSIENT_FIELDS = new int[] {  };

    @Override
    protected int[] getNonTransientFields() {
        return NON_TRANSIENT_FIELDS;
    }

    @Override
    protected void serialize(TransactedObject.Version version, Writer writer) throws java.io.IOException {
        boolean[] reads = ((TransactedStructure.Version) version).getReads();
        Object[] values = ((TransactedStructure.Version) version).getWrites();

        if (reads != null) {
            writer.writeShort(Short.MAX_VALUE);

            for (int i = 0; i < 3; i++)
                writer.writeBoolean(reads[i]);
        }

        if (values != null) {
            if (values[0] != null) {
                if (values[0] == Removal.Instance)
                    writer.writeShort((short) -1);
                else {
                    writer.writeShort((short) 1);
                    writer.writeString((String) values[0]);
                }
            }

            if (values[1] != null) {
                if (values[1] == Removal.Instance)
                    writer.writeShort((short) -2);
                else {
                    writer.writeShort((short) 2);
                    writer.writeString((String) values[1]);
                }
            }

            if (values[2] != null) {
                if (values[2] == Removal.Instance)
                    writer.writeShort((short) -3);
                else {
                    writer.writeShort((short) 3);
                    writer.writeString((String) values[2]);
                }
            }
        }

        writer.writeShort((short) 0);
    }

    @SuppressWarnings("null")
    @Override
    protected void deserialize(TransactedObject.Version version, Reader reader) throws java.io.IOException {
        boolean[] reads = null;
        Object[] values = null;

        short index = reader.readShort();

        if (index == Short.MAX_VALUE) {
            reads = new boolean[3];

            for (int i = 0; i < 3; i++)
                reads[i] = reader.readBoolean();

            index = reader.readShort();
        }

        if (index == 1) {
            if (values == null)
                values = new Object[3];

            values[0] = reader.readString();
            index = reader.readShort();
        } else if (index == -1) {
            if (values == null)
                values = new Object[3];

            values[0] = Removal.Instance;
            index = reader.readShort();
        }

        if (index == 2) {
            if (values == null)
                values = new Object[3];

            values[1] = reader.readString();
            index = reader.readShort();
        } else if (index == -2) {
            if (values == null)
                values = new Object[3];

            values[1] = Removal.Instance;
            index = reader.readShort();
        }

        if (index == 3) {
            if (values == null)
                values = new Object[3];

            values[2] = reader.readString();
            index = reader.readShort();
        } else if (index == -3) {
            if (values == null)
                values = new Object[3];

            values[2] = Removal.Instance;
            index = reader.readShort();
        }

        ((TransactedStructure.Version) version).setReads(reads);
        ((TransactedStructure.Version) version).setWrites(values);
    }
}
