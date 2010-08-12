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

import java.rmi.Naming;
import java.rmi.RMISecurityManager;

import org.epzilla.accumulator.global.DerivedEvent;
import org.epzilla.accumulator.service.AccumulatorService;

/**
 * Created by IntelliJ IDEA.
 * User: Rajeev
 * Date: Feb 28, 2010
 * Time: 10:35:36 AM
 * To change this template use File | Settings | File Templates.
 */
public class DummyClient {

    public static void main(String[] argv) {
        try {
            if (System.getSecurityManager() == null) {
                System.setSecurityManager(new RMISecurityManager() {
                    public void checkConnect(String host, int port) {
                    }

                    public void checkConnect(String host, int port, Object context) {
                    }
                });
            }


            AccumulatorService serv = (AccumulatorService) Naming.lookup("rmi://127.0.0.1:1099/AccumulatorService");
            serv.receiveDerivedEvent(new DerivedEvent());
        } catch (Exception e) {
            System.out.println("rmi client exception: " + e);
        }
    }

}
