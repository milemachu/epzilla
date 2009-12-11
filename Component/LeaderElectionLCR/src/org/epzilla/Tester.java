package org.epzilla;


public class Tester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MessageListner ml=new MessageListner();
		Thread t1=new Thread(ml);
		
	
	t1.start();
	
	MessageSender ms=new MessageSender("127.0.0.1");


	Thread t2=new Thread(ms);
	t2.start();
	
	
//	MessageSender ms1=new MessageSender("10.8.102.34");
//
//
//	Thread t3=new Thread(ms1);
//	t3.start();
	}

}
