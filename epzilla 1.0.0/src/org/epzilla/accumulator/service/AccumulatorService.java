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
package org.epzilla.accumulator.service;


import org.epzilla.accumulator.global.DerivedEvent;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by IntelliJ IDEA.
 * User: Rajeev
 * Date: Feb 28, 2010
 * Time: 7:52:24 AM
 * To change this template use File | Settings | File Templates.
 */
public interface AccumulatorService extends Remote {

    public boolean receiveDerivedEvent(DerivedEvent event) throws RemoteException;

    public boolean isEventAvailable(long srcId, int clientId) throws RemoteException;

    public void receiveDeriveEvent(byte[] deriveEvent) throws RemoteException;

}
