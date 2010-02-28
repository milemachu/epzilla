package org.epzilla.Logs;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RecoveryList implements FileScannerInterface{
	List<String> undoList = new ArrayList<String>();
	List<String> redoList = new ArrayList<String>();
//	Pattern p1 = Pattern.compile("^(.{2}) (.{10})$");
	Pattern p1 = Pattern.compile("^[T0-9]+ (.{10})$");
	Pattern p2 = Pattern.compile("</commit>");
	Matcher m1 =null;
	Matcher m2 = null;
	File file;
	Scanner scanner = null;
	String st1 = "";
	String st2 = "";
	String strmatch = "";
	int i=0;
	
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
				System.out.println("File not found");
			}
			System.out.println("Redo List:");
			printArray(redoList);
			System.out.println("Undo List:");
			printArray(undoList);
			recovery(undoList); 
	}
	private void recovery(List<String> recArray){
		FileScanner fsi = new FileScanner(file, recArray); 
		Thread t = new Thread(fsi);
		t.start();
	}
	@Override
	public void printArray(List<String> array) {
		for(int i=0; i<array.size();i++){
			System.out.println(array.get(i));
		}	
	}
	

}
