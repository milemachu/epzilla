/***************************************************************************************************
Copyright 2009 - 2010 Harshana Eranga Martin, Dishan Metihakwala, Rajeev Sampath, Chathura Randika

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
****************************************************************************************************/
package org.epzilla.dispatcher.logs;

import org.epzilla.util.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RecoveryList implements FileScannerInterface{
	private List<String> undoList = new ArrayList<String>();
	private List<String> redoList = new ArrayList<String>();
	private Pattern p1 = Pattern.compile("^[CID0-9]+ (.{10})$");
	private Pattern p2 = Pattern.compile("</commit>");
	private Matcher m1 =null;
	private Matcher m2 = null;
	private File file;
	private Scanner scanner = null;
	private String st1 = "";
	private String st2 = "";
	private String strmatch = "";
	private int i=0;
	
	public RecoveryList(File f) {
		this.file = f;
	}
	@Override
	public void readFile(File file, String strReq) {
	}
	@Override
	public void readFile(File file) {
		try {
			scanner = new Scanner(file);
			while(scanner.hasNextLine()){
				st1 = scanner.nextLine();
				m1 = p1.matcher(st1);
				m2 = p2.matcher(st1);
				if(m1.find()){
					StringTokenizer st = new StringTokenizer(st1);
					strmatch = st.nextToken();
					undoList.add(strmatch);	
					while(scanner.hasNextLine()){
						
						st2 = scanner.nextLine();
						m2 = p2.matcher(st2);
						m1 = p1.matcher(st2);
						if(m2.find()){
							undoList.remove(i);
							redoList.add(strmatch);
							break;
						}
						else if(m1.find()){
							StringTokenizer st3 = new StringTokenizer(st2);
							strmatch = st3.nextToken();
							undoList.add(strmatch);	
							i++;
						}
					}
				}
			}
			scanner.close();
			} catch (FileNotFoundException e) {
				Logger.log("File not found");
			}
			Logger.log("Redo List:");
			printArray(redoList);
			Logger.log("Undo List:");
			printArray(undoList);
			recovery(undoList); 
	}
	private void recovery(List<String> recArray){
		FileScanner scanner = new FileScanner(file, recArray); 
		Thread t = new Thread(scanner);
		t.start();
	}
	public void printArray(List<String> array) {
		for(int i=0; i<array.size();i++){
			Logger.log(array.get(i));
		}
	}
	

}
