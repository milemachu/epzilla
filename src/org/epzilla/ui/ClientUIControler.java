package org.epzilla.ui;

import javax.swing.JFrame;

import org.epzilla.ui.*;

public class ClientUIControler {
	private static ClientUI clientInstance;
	
	  public static void InitializeUI() {
	        clientInstance = new ClientUI();
	        clientInstance.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        clientInstance.setVisible(true);
	    }
	  public static void appendTextToStatus(String text) {
	        clientInstance.getTxtResults().append(text + "\n");
	    }

}
