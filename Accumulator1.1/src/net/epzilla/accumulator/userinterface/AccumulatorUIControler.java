package net.epzilla.accumulator.userinterface;

import javax.swing.JFrame;

public class AccumulatorUIControler {
	private static AccumulatorUI instance;
	
	public static void InitializeUI() {
        instance = new AccumulatorUI();
        instance.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        instance.setVisible(true);
	}
	public static void appendTextToStatus(String text){
		instance.getTxtAccStatus().append(text + "\n");
	}
	public static void appendEventCount(String text){
		instance.getTxtEventCount().append(text + "\n");
	}
	public static void appendEventResults(String text){
		instance.getTxtEventResults().append(text + "\n");
	}
	/*
	 * main method is just for testing
	 */
	public static void main(String[] args){
		InitializeUI();
	}

}
