package org.epzilla.Logs;

import java.util.*;
import java.io.*;
/*
 * initializing method which will accept List<String> and trigger ID, trigger owner
 * default log file location is taken from the CurrentValues class
 * size of the log file is defined, here it is 1MB, if greater than 1MB it will destroy existing an create new one
 * owerwriteLog and write methods are there to perform that task
 */
public class WriteLog{
	 public WriteLog (){
	 }
	
    public void writeInit(List<String> myArr,int id,int ownerId) throws IOException{
    	long size = getFileSize(CurrentValues.defaultLogFile);
    		
    	if(size>1024*1024) 
    		overwriteLog(CurrentValues.defaultLogFile,myArr,id,ownerId);
    	else
    		writeLog(CurrentValues.defaultLogFile,myArr,id,ownerId);
        }
    private void overwriteLog(String filename,List<String> myArr, int id, int ownerId) throws IOException{
    	 BufferedWriter writer = new BufferedWriter(new FileWriter(filename,false));
         String tag = CurrentValues.fileTag+id;      
         writer.write(tag+" "+"Checkpoint");
         writer.newLine();
     	for(int i=0; i<myArr.size();i++){
     		writer.write(myArr.get(i));
     		writer.newLine();
     }    	
         writer.write("</commit>");
         writer.newLine();
     	writer.flush();
     	writer.close();
    }
    private void writeLog(String filename, List<String> myArr,int id, int ownerId)throws IOException{

    	BufferedWriter writer = new BufferedWriter(new FileWriter(filename,true));
    	String tag = CurrentValues.fileTag+id;     
        writer.write(tag+" "+"Checkpoint");
        writer.newLine();
    	for(int i=0; i<myArr.size();i++){
    		writer.write(myArr.get(i));
    		writer.newLine();
    }    	
        writer.write("</commit>");
        writer.newLine();
    	writer.flush();
    	writer.close();
    }
    public long getFileSize(String filename) {
        File file = new File(filename);
        if (!file.exists() || !file.isFile()) {
          System.out.println("File doesn't exist");
          return -1;
        }
        return file.length();
      }
 }
