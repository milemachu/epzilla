package org.epzilla.dispatcher.logs;

import java.io.*;
/*
 * FileScanner class takes the Cluster ID and recover them
 * ID is in the form:- CID0, CID1.....
 * RecoveryList class takes log file and create undo and redo lists
 */
public class ReadLog {
	 static File file = new File("c:\\checkpoint.txt");
	 static String reqstr = "CID2";
	
    public static void main(String[] args) {
    	FileScanner fs = new FileScanner();
    	fs.readFile(file, reqstr);
//    	Thread scanner = new Thread(fs);
//    	scanner.start();
//    	RecoveryList ldl = new RecoveryList(file);
//    	ldl.readFile(file);
    }


}

