//==============================================================================
//                                                                              
//  THIS FILE HAS BEEN GENERATED BY JSTM                                        
//                                                                              
//==============================================================================

package org.epzilla.dispatcher.dispatcherObjectModel;

import jstm.core.*;

public final class DispatcherObjectModel extends jstm.core.ObjectModel {

    public static final String UID = "o+dIL1jrhRpIWivA8+mFIw";

    public static final String XML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><ObjectModelDefinition xsi:noNamespaceSchemaLocation=\"http://www.xstm.net/schemas/xstm-0.3.xsd\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><Name>DispatcherObjectModel</Name><RootPackage name=\"org.epzilla.dispatcher.dispatcherObjectModel\"><Packages/><Structures><Structure name=\"TriggerInfoObject\"><Fields><Field transient=\"false\" name=\"triggerID\"><Type name=\"java.lang.String\"/></Field><Field transient=\"false\" name=\"clientID\"><Type name=\"java.lang.String\"/></Field><Field transient=\"false\" name=\"clusterID\"><Type name=\"java.lang.String\"/></Field><Field transient=\"false\" name=\"trigger\"><Type name=\"java.lang.String\"/></Field><Field transient=\"false\" name=\"stratumId\"><Type name=\"java.lang.String\"/></Field><Field transient=\"false\" name=\"oldStratumId\"><Type name=\"java.lang.String\"/></Field><Field transient=\"false\" name=\"oldClusterId\"><Type name=\"java.lang.String\"/></Field></Fields><Methods/></Structure><Structure name=\"ClientInfoObject\"><Fields><Field transient=\"false\" name=\"clientID\"><Type name=\"java.lang.String\"/></Field><Field transient=\"false\" name=\"clientIP\"><Type name=\"java.lang.String\"/></Field></Fields><Methods/></Structure><Structure name=\"LeaderInfoObject\"><Fields><Field transient=\"false\" name=\"clusterID\"><Type name=\"java.lang.String\"/></Field><Field transient=\"false\" name=\"leaderIP\"><Type name=\"java.lang.String\"/></Field></Fields><Methods/></Structure></Structures></RootPackage></ObjectModelDefinition>";

    public DispatcherObjectModel() {
    }

    @Override
    public String getUID() {
        return UID;
    }

    @Override
    public String getXML() {
        return XML;
    }

    @Override
    public int getClassCount() {
        return 3;
    }

    @Override
    public TransactedObject createInstance(int classId, Connection route) {
        switch (classId) {
            case 0:
                return new org.epzilla.dispatcher.dispatcherObjectModel.TriggerInfoObject();
            case 1:
                return new org.epzilla.dispatcher.dispatcherObjectModel.ClientInfoObject();
            case 2:
                return new org.epzilla.dispatcher.dispatcherObjectModel.LeaderInfoObject();
        }

        throw new IllegalArgumentException("Unknown class id: " + classId);
    }
}
