package org.epzilla.ui;

import javax.swing.JFrame;

public class ClientUIControler implements Runnable{
		private static ClientUI clientInstance;
		String message = "";
		
		public ClientUIControler(){
		}
		public ClientUIControler(String msg){
			this.message = msg;
		}
		public static void initializeClientUI(){
			clientInstance = new ClientUI();
			clientInstance.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			clientInstance.setVisible(true);
		}
	  @Override
	  public void run() {
		  clientInstance.getTxtResults().append(message + "\n");
	  }

}
