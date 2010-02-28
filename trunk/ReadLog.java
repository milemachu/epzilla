package org.epzilla.Logs;

import java.io.*;
/*
 * FileScanner class takes the trigger set ID and recover them
 * ID is in the form:- T0,T1,........T125.....
 * RecoveryList class takes log file and create undo and redo lists
 */
public class ReadLog {
	 static File file = new File("c:\\checkpoint.txt");
	 static String reqstr = "T2";
	
    public static void main(String[] args) {
//    	FileScannerIndi fs = new FileScannerIndi(file,reqstr);
//    	Thread scanner = new Thread(fs);
//    	scanner.start();
    	RecoveryList ldl = new RecoveryList(file);
    	ldl.readFile(file);
    }


}

