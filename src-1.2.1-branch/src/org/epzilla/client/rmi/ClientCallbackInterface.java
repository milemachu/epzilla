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

import java.rmi.Remote;
import java.rmi.RemoteException;
/**
 * Created by IntelliJ IDEA.
 * Interface class
 * Author: Chathura
 * Date: Mar 2, 2010
 * Time: 12:40:41 PM
 */
public interface ClientCallbackInterface extends Remote {
    public void notifyClient(String message) throws RemoteException;
}
