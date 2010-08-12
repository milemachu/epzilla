/***************************************************************************************************
Copyright 2009 - 2010 Harshana Eranga Martin, Dishan Metihakwala, Rajeev Sampath, Chathura Randika

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
****************************************************************************************************/
package org.epzilla.clusterNode.rmi;

import org.epzilla.dispatcher.rmi.TriggerRepresentation;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by IntelliJ IDEA.
 * Interface class of the Cluster Implementation
 * Author: Chathura
 * Date: Mar 11, 2010
 * Modified: May 11, 2010
 * Time: 10:44:48 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ClusterInterface extends Remote {
//    public String acceptTiggerStream(ArrayList<String> tList, String clusterID, String clientID) throws RemoteException;

    public String acceptTiggerStream(List<TriggerRepresentation> tList) throws RemoteException;

    public String acceptEventStream(byte[] event, String clusterID) throws RemoteException;

    public void addEventStream(String event) throws RemoteException;

    public boolean deleteTriggers(ArrayList<TriggerRepresentation> list, String clusterID, String clientID) throws RemoteException;

    public void initNodeProcess() throws RemoteException;

    public void sleepNodeProcess() throws RemoteException;
//    public boolean deleteTriggers(HashMap<String, ArrayList<String>> rep) throws RemoteException ;
}
