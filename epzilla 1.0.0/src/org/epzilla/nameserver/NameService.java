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
package org.epzilla.nameserver;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by IntelliJ IDEA.
 * This is the Interface of the NameServer
 * Author: Chathura
 * To change this template use File | Settings | File Templates.
 */
public interface NameService extends Remote {

    public int insertNode(String name, String ipAdrs, int portNumber) throws RemoteException;

    public int getDirectorySize() throws RemoteException;

    public String getDispatcherIP() throws RemoteException;

    public String getHostName(int i) throws RemoteException;

    public String getName(int i) throws RemoteException;

    public String getClientID(String ipAdrs) throws RemoteException;

    public void updateIncLoad(String ip) throws RemoteException;

    public void updateDecLoad(String ip) throws RemoteException;
}

