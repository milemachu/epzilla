package org.epzilla.Logs;

import java.io.IOException;
import java.util.*;
public class RandomStringGennerator {
	static String token = "";
	static List<String> myArr = new ArrayList<String>();
    static int START = 1;
    static int END = 10;
	
	public static void main(String[] args) throws IOException {
		WriteLog lf = new WriteLog();
		System.out.println("Generating random strings");
	    Random random = new Random();
	    
	    for(int j = 0; j<50 ;j++){
	      generateRandomStrings(START, END, random);
	  lf.writeInit(myArr,j,1);
	    }
	    
	    System.out.println("Done.");  
			}
	 private static void generateRandomStrings(int aStart, int aEnd, Random aRandom){		    
		    token = Long.toString(Math.abs(aRandom.nextLong()), 36);
		    myArr.add(token);
		    }
	 
    
}
