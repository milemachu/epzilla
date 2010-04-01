package org.epzilla.dispatcher.logs;

import java.util.*;
import java.io.*;
/*
 * initializing method which will accept List<String> and trigger ID, trigger owner
 * default log file location is taken from the CurrentValues class
 * size of the log file is defined, here it is 2MB, if greater than 1MB it will destroy existing an create new one
 * owerwriteLog and write methods are there to perform that task
 */
public class WriteLog{
	 public WriteLog (){
	 }
	
    public static void writeInit(List<String> triggerList,String userID) throws IOException{
    	long size = getFileSize(CurrentValues.defaultLogFile);

    	if(size>2*1024*1024)
    		overwriteLog(CurrentValues.defaultLogFile,triggerList,userID);
    	else
    		writeLog(CurrentValues.defaultLogFile,triggerList,userID);
        }
    private static void overwriteLog(String filename,List<String> myArr,String ownerId) throws IOException{
    	int id=0;
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename,false));
         String tag = ownerId;
         writer.write(tag+" "+"Checkpoint");
         writer.newLine();
     	for(int i=0; i<myArr.size();i++){
     		writer.write(myArr.get(i));
     		writer.newLine();
     }
         writer.write("</commit>");
        id++;
         writer.newLine();
     	writer.flush();
     	writer.close();
    }
    private static void writeLog(String filename, List<String> myArr,String ownerId)throws IOException{
        int id=0;
    	BufferedWriter writer = new BufferedWriter(new FileWriter(filename,true));
    	String tag = ownerId;
        writer.write(tag+" "+"Checkpoint");
        writer.newLine();
    	for(int i=0; i<myArr.size();i++){
    		writer.write(myArr.get(i));
    		writer.newLine();
    }    	
        writer.write("</commit>");
        id++;
        writer.newLine();
    	writer.flush();
    	writer.close();
    }
    public static long getFileSize(String filename) {
        File file = new File(filename);
        if (!file.exists() || !file.isFile()) {
         
          return -1;
        }
        return file.length();
      }
 }
