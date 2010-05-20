package org.epzilla.accumulator.userinterface;

import javax.swing.JFrame;

public class AccumulatorUIControler {
	private static AccumulatorUI instance;
	
	public static void InitializeUI() {
        instance = new AccumulatorUI();
        instance.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        instance.setVisible(true);
        appendAccumulatorStatus("ACTIVE");
	}
	public static void appendAccumulatorStatus(String text){
		instance.getAccumulatorStatus().append(text + "\n");
	}
	public static void appendDeriveEventCount(String text){
		instance.getDeriveEventCount().append(text + "\n");
	}
	public static void appendEventResults(String text){
		instance.getEventResults().append(text + "\n");
	}
	public static void appendEventprocessed(String text){
		instance.getEventProcessed().append(text+ "\n");
	}
}
