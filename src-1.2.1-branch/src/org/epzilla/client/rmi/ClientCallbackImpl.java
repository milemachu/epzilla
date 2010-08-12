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
package org.epzilla.client.rmi;

import org.epzilla.client.controlers.ClientUIControler;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
/**
 * Created by IntelliJ IDEA.
 * This class use to send callback information to the client
 * Author: Chathura
 * Date: Mar 2, 2010
 * Time: 12:40:41 PM
 */
public class ClientCallbackImpl extends UnicastRemoteObject implements ClientCallbackInterface {

    private static ClientUIControler clientCon;

    public ClientCallbackImpl() throws RemoteException {
        super();
    }

    public void notifyClient(String message) {
        clientCon = new ClientUIControler(message);
        Thread t = new Thread(clientCon);
        t.start();
    }
}
