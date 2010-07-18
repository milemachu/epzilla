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
