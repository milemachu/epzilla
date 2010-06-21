package org.epzilla.daemon.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Hashtable;

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
						Runtime.getRuntime().exec("taskkill /f /PID "+proc[0]);
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

}