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
package org.epzilla.daemon.services;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Hashtable;

/**
 * This is the implementation of daemon service for cluster node load balancing.
 * This class is used to wakeup the sleeping nodes and sleep the running unnecessary nodes.
 * @author Harshana Eranga Martin
 *
 */
public class DaemonImpl extends UnicastRemoteObject implements DaemonInterface {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8435420804355272692L;
	private String javaHome;

	protected DaemonImpl() throws RemoteException {
		super();
		
		try{
			Process p = Runtime.getRuntime().exec("cmd.exe /c echo %JAVA_HOME%");
			BufferedReader br = new BufferedReader( new InputStreamReader( p.getInputStream() ) );
			String myvar = br.readLine();
			System.out.println("Java Home Path ="+myvar);

			char[] ss=myvar.toCharArray();
			int index=0;
			for (char s : ss) {
				if(s=='\\'){
					s='/';
					ss[index]=s;
			}
			index++;
		}		
			javaHome=new String(ss);
		}catch(Exception ex){
			System.out.println("Java Home Not found.");
		}
	}

	@Override
	public boolean sleepEpzilla() throws RemoteException {
		Hashtable<Integer, String> processMap=new Hashtable<Integer, String>();
		try {
			//Process epzillaProcess=Runtime.getRuntime().exec("C:/Program Files/Java/jdk1.6.0/bin/jps.exe");
			Process epzillaProcess=Runtime.getRuntime().exec(javaHome+"/bin/jps.exe");
			BufferedReader reader=new BufferedReader(new InputStreamReader(epzillaProcess.getInputStream()));
			String line=reader.readLine();
//			System.out.println(line);
			while (line !=null) {
				String[] proc=line.split(" ");
				if(proc.length==2){
					processMap.put(Integer.parseInt(proc[0]), proc[1]);
					System.out.println(line);
					if(proc[1].equalsIgnoreCase("ClusterStartup")){
						writeToBatFile(Integer.parseInt(proc[0]));
						String[] commands = {"cmd", "/c", "start", "Taskkill.bat","Sleep.bat"};
//						Runtime.getRuntime().exec("taskkill /f /PID "+proc[0]);
						Runtime.getRuntime().exec(commands);
						break;
					}
				}
				line=reader.readLine();
			}
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean wakeEpZilla() throws RemoteException {
		String[] commands = {"cmd", "/c", "start", "Cluster.bat","Cluster.bat"};
		try {
			Runtime.getRuntime().exec(commands);
			System.out.println("Cluster node is waking up.");
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		

	}
	
	public static void main(String[] args) throws IOException {
		Hashtable<Integer, String> processMap=new Hashtable<Integer, String>();
		Process epzillaProcess=Runtime.getRuntime().exec("Jps.exe");
		BufferedReader reader=new BufferedReader(new InputStreamReader(epzillaProcess.getInputStream()));
		String line=reader.readLine();
		while (line !=null) {
			String[] proc=line.split(" ");
			processMap.put(Integer.parseInt(proc[0]), proc[1]);
			System.out.println(line);
		}
		
	}
	
	private void writeToBatFile(int procId){
		try {
			FileWriter writer=new FileWriter("Sleep.bat",false);
			writer.write("Taskkill /F /PID "+procId);
			writer.flush();
			writer.close();
			writer=null;
			System.gc();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
