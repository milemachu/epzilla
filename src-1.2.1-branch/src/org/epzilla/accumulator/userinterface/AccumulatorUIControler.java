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
package org.epzilla.accumulator.userinterface;

import javax.swing.*;

/**
 * This class listens for incoming requests, and updates the Accumulator
 * user interface accordingly
 *
 * @author Chathura
 */

public class AccumulatorUIControler {
    private static AccumulatorUI instance;

    public static void InitializeUI() {
        instance = new AccumulatorUI();
        instance.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        instance.setVisible(true);
        appendAccumulatorStatus("ACTIVE");
    }

    /*
   append the accumulator status to the user interface
    */

    public static void appendAccumulatorStatus(String text) {
        instance.getAccumulatorStatus().append(text + "\n");
    }

    /*
   append derive events count in the user interface
    */

    public static void appendDeriveEventCount(String text) {
        instance.getDeriveEventCount().setText(text + "\n");
    }

    /*
   append the partial results to text area in the user interface
    */

    public static void appendEventResults(String text) {
        instance.getEventResults().append(text + "\n");
    }

    /*
   append the number of events processed to the user interface
    */

    public static void appendEventprocessed(String text) {
        instance.getEventProcessed().setText(text + "\n");
    }
}
