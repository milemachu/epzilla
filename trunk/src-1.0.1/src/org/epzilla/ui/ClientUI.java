package org.epzilla.ui;


import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.rmi.*;
import java.util.*;
import javax.swing.*;
import java.awt.Rectangle;
import javax.swing.event.*;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.Point;

public class ClientUI extends JFrame implements ActionListener,ListSelectionListener,WindowListener{

	private JTabbedPane tabbedPane = null;
	private JTextField txtIP = null;
	private JTextField txtPort = null;
	private JTextField txtName = null;
	private JLabel labelIP = null;
	private JLabel labelPort = null;
	private JLabel labelName = null;
	private JButton btnSave = null;
	private JButton btnCancel = null;
	Vector<String> ips = new Vector<String>();  
	private javax.swing.JFileChooser jFileChooser = null;
	private JMenuBar menuBar =null;
	private JMenuItem about = null;
	private JMenuItem help = null;
	private JMenuItem adminSettings = null;
	private JMenuItem closetabs = null;
	private JMenuItem exit = null;
	private JMenu file = new JMenu("File");
	private JMenu helpmenu = new JMenu("Help");
	private JButton btnBrowse = null;
	private JTextField txtFile = null;
	private JButton btnSend = null;
	private JButton btnCancelSend = null;
	private JLabel lblDetails = null;
	private JList listLookup = null;
	private JButton btnClear = null;
	private JButton btnLookup = null;
	private JLabel lblDispIP1 = null;
	private JTextField txtDispIP = null;
	private JLabel lblSettings = null;
	private JButton btnOK = null;
	private JScrollPane resultsScrollPane = null;
	public JTextArea txtResults = null;
	private JLabel lblDispatcherServiceName = null;
	private JTextField txtDispName = null;
	private JCheckBox chkEvents = null;
	private JCheckBox chkTriggers = null;
	ClientHandler client;
	boolean isRegister = false;
	int eventSeqID;
	int triggerSeqID;
	public ClientUI() {
		super();
		client = new ClientHandler();
		initialize();
	}
	private void initialize() {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = screen.width;
        int y = screen.height;
       	this.setSize(new Dimension(685, 697));
       	this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/logo.JPG")));
       	this.setResizable(false);
       	this.setSize(x,y);
        this.setContentPane(getMyTabbedPane());
        this.setJMenuBar(getmyMenuBar());
       	this.setTitle("Epzilla DS");	
       	this.addWindowListener(this);
	}
	private JTabbedPane getMyTabbedPane() {
		if (tabbedPane == null) {
			lblDispatcherServiceName = new JLabel();
			lblDispatcherServiceName.setText("Service Name :");
			lblDispatcherServiceName.setLocation(new Point(15, 178));
			lblDispatcherServiceName.setSize(new Dimension(123, 16));
			lblSettings = new JLabel();
			lblSettings.setText("Server Settings");
			lblSettings.setLocation(new Point(15, 15));
			lblSettings.setSize(new Dimension(196, 25));
			lblDispIP1 = new JLabel();
			lblDispIP1.setBounds(new Rectangle(15, 148, 141, 16));
			lblDispIP1.setText("Dispatcher IP Selected :");
			lblDetails = new JLabel();
			lblDetails.setBounds(new Rectangle(15, 16, 259, 16));
			lblDetails.setText("Lookup available services in the System");
			labelName = new JLabel();
			labelName.setText("Name :");
			labelName.setSize(new Dimension(47, 25));
			labelName.setLocation(new Point(55, 92));
			labelPort = new JLabel();
			labelPort.setText("Port :");
			labelPort.setBounds(new Rectangle(64, 135, 41, 25));
			labelIP = new JLabel();
			labelIP.setText("IP Address :");
			labelIP.setSize(new Dimension(72, 25));
			labelIP.setLocation(new Point(25, 49));
			tabbedPane = new JTabbedPane();
		JPanel mainSettings = new JPanel();
		mainSettings.setLayout(null);
		mainSettings.add(getIpTextField(), null);
		mainSettings.add(getTbPort(), null);
		mainSettings.add(getTbName(), null);
		mainSettings.add(labelIP, null);
		mainSettings.add(labelPort, null);
		mainSettings.add(labelName, null);
		mainSettings.add(getBtnSave(), null);
		mainSettings.add(getBtnCancel(), null);
		mainSettings.add(lblSettings, null);
		JPanel upload = new JPanel();
		upload.setLayout(null);
		upload.add(getTxtFile(), null);
		upload.add(getBtnBrowse(), null);
		upload.add(getBtnSend(), null);
		upload.add(getBtnCancelSend(), null);
		upload.add(lblDetails, null);
		upload.add(getListLookup(), null);
		upload.add(getBtnClear(), null);
		upload.add(getBtnLookup(), null);
		upload.add(lblDispIP1, null);
		upload.add(getTxtDispIP1(), null);
		upload.add(lblDispatcherServiceName, null);
		upload.add(getTxtDispName(), null);
		upload.add(getChkEvents(), null);
		upload.add(getChkTriggers(), null);
		JPanel results = new JPanel();
		results.setLayout(null);
		results.add(getBtnOK(), null);
		results.add(getResultsScrollPane(), null);
		tabbedPane.addTab("Settings",mainSettings);
		tabbedPane.addTab("Service",upload);
		tabbedPane.addTab("Results",results);

			tabbedPane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			tabbedPane.setVisible(true);
		}
		return tabbedPane;
	}
	private JMenuBar getmyMenuBar(){
		if(menuBar==null){
			menuBar = new JMenuBar();
			file.add(getAdminSettingMI());
			file.add(getCloseMI());
			file.add(getExitMI());
			helpmenu.add(getAboutMI());
			helpmenu.add(getHelpMI());
			menuBar.add(file);
			menuBar.add(helpmenu);
			}
		return menuBar;
	}
	private JMenuItem getAboutMI(){
		if(about==null){
			about = new JMenuItem();
			about.setText("About");
			about.addActionListener(this);
		}
		return about;
	}
	private JMenuItem getHelpMI(){
		if(help==null){
			help = new JMenuItem();
			help.setText("Help");
			help.addActionListener(this);
		}
		return help;
	}
	private JMenuItem getAdminSettingMI(){
		if(adminSettings==null){
			adminSettings = new JMenuItem();
			adminSettings.setText("Administrator Settings");
			adminSettings.addActionListener(this);
			}
		return adminSettings;
	}
	private JMenuItem getExitMI(){
		if(exit==null){
			exit = new JMenuItem();
			exit.setText("Exit");
			exit.addActionListener(this);
		}
		return exit;
	}
	private JMenuItem getCloseMI(){
		if(closetabs==null){
			closetabs = new JMenuItem();
			closetabs.setText("Close Tabs");
			closetabs.addActionListener(this);
			}
		return closetabs;
	}
	private JTextField getIpTextField() {
		if (txtIP == null) {
			txtIP = new JTextField();
			txtIP.setLocation(new Point(110, 52));
			txtIP.setSize(new Dimension(200, 20));
		}
		return txtIP;
	}
	private JTextField getTbPort() {
		if (txtPort == null) {
			txtPort = new JTextField();
			txtPort.setLocation(new Point(110, 140));
			txtPort.setSize(new Dimension(204, 20));
		}
		return txtPort;
	}
	private JTextField getTbName() {
		if (txtName == null) {
			txtName = new JTextField();
			txtName.setLocation(new Point(110, 96));
			txtName.setSize(new Dimension(200, 20));
		}
		return txtName;
	}
	private JButton getBtnSave() {
		if (btnSave == null) {
			btnSave = new JButton();
			btnSave.setLocation(new Point(121, 183));
			btnSave.setText("Save");
			btnSave.setSize(new Dimension(85, 20));
			btnSave.addActionListener(this);
			}
		return btnSave;
	}
	private JButton getBtnCancel() {
		if (btnCancel == null) {
			btnCancel = new JButton();
			btnCancel.setLocation(new Point(218, 182));
			btnCancel.setText("Cancel");
			btnCancel.setSize(new Dimension(85, 20));
			btnCancel.addActionListener(this);
			}
		return btnCancel;
	}
	private javax.swing.JFileChooser getJFileChooser() {
		if (jFileChooser == null) {
			jFileChooser = new javax.swing.JFileChooser();
			jFileChooser.setMultiSelectionEnabled(false);
			jFileChooser.setSize(new Dimension(433, 299));
		}
		return jFileChooser;
	}
	private JButton getBtnBrowse() {
		if (btnBrowse == null) {
			btnBrowse = new JButton();
			btnBrowse.setText("Browse");
			btnBrowse.setLocation(new Point(15, 218));
			btnBrowse.setSize(new Dimension(85, 20));
			btnBrowse.addActionListener(this);
		}
		return btnBrowse;
	}
	private JTextField getTxtFile() {
		if (txtFile == null) {
			txtFile = new JTextField();
			txtFile.setPreferredSize(new Dimension(4, 20));
			txtFile.setSize(new Dimension(325, 20));
			txtFile.setEditable(false);
			txtFile.setLocation(new Point(165, 219));
		}
		return txtFile;
	}
	private JButton getBtnSend() {
		if (btnSend == null) {
			btnSend = new JButton();
			btnSend.setText("Send");
			btnSend.setBounds(new Rectangle(218, 250, 85, 20));
			btnSend.addActionListener(this);
			}
		return btnSend;
	}
	private JButton getBtnCancelSend() {
		if (btnCancelSend == null) {
			btnCancelSend = new JButton();
			btnCancelSend.setText("Cancel");
			btnCancelSend.setBounds(new Rectangle(331, 250, 85, 20));
			btnCancelSend.addActionListener(this);
			}
		return btnCancelSend;
	}
	private JList getListLookup() {
		if (listLookup == null) {
			listLookup = new JList(ips);
			listLookup.setSize(new Dimension(325, 72));
			listLookup.setLocation(new Point(165, 46));
			listLookup.addListSelectionListener(this);		}
		return listLookup;
	}
	private JButton getBtnClear() {
		if (btnClear == null) {
			btnClear = new JButton();
			btnClear.setBounds(new Rectangle(15, 92, 85, 20));
			btnClear.setText("Clear");
			btnClear.addActionListener(this);
			}
		return btnClear;
	}
	private JButton getBtnLookup() {
		if (btnLookup == null) {
			btnLookup = new JButton();
			btnLookup.setText("Lookup");
			btnLookup.setSize(new Dimension(85, 20));
			btnLookup.setLocation(new Point(15, 45));
			btnLookup.setName("");
			btnLookup.addActionListener(this);
			}
		return btnLookup;
	}
	private JTextField getTxtDispIP1() {
		if (txtDispIP == null) {
			txtDispIP = new JTextField();
			txtDispIP.setPreferredSize(new Dimension(4, 20));
			txtDispIP.setLocation(new Point(165, 147));
			txtDispIP.setEditable(false);
			txtDispIP.setSize(new Dimension(325, 20));
		}
		return txtDispIP;
	}
	private JButton getBtnOK() {
		if (btnOK == null) {
			btnOK = new JButton();
			btnOK.setText("OK");
			btnOK.setSize(new Dimension(85, 20));
			btnOK.setLocation(new Point(25, 620));
		}
		return btnOK;
	}
	private JScrollPane getResultsScrollPane() {
		if (resultsScrollPane == null) {
			resultsScrollPane = new JScrollPane();
			resultsScrollPane.setBounds(new Rectangle(25, 15, 800,600));
			resultsScrollPane.setViewportView(getTxtResults());
		}
		return resultsScrollPane;
	}
	public JTextArea getTxtResults() {
		if (txtResults == null) {
			txtResults = new JTextArea();
			txtResults.setBounds(new Rectangle(0, 0, 800, 600));
			txtResults.setEditable(false);
		}
		return txtResults;
	}
	private JTextField getTxtDispName() {
		if (txtDispName == null) {
			txtDispName = new JTextField();
			txtDispName.setLocation(new Point(165, 181));
			txtDispName.setEditable(false);
			txtDispName.setSize(new Dimension(325, 20));
		}
		return txtDispName;
	}
	private JCheckBox getChkEvents() {
		if (chkEvents == null) {
			chkEvents = new JCheckBox();
			chkEvents.setText("Events");
			chkEvents.setLocation(new Point(520, 220));
			chkEvents.setSize(new Dimension(80, 21));
		}
		return chkEvents;
	}
	private JCheckBox getChkTriggers() {
		if (chkTriggers == null) {
			chkTriggers = new JCheckBox();
			chkTriggers.setText("Triggers");
			chkTriggers.setLocation(new Point(520, 250));
			chkTriggers.setSize(new Dimension(93, 21));
		}
		return chkTriggers;
	}
	private void loadFile(){
		int state = getJFileChooser().showOpenDialog(this);
		if (state == JFileChooser.APPROVE_OPTION) {
			File f = getJFileChooser().getSelectedFile();
			txtFile.setText(f.getAbsolutePath());
			}
	}
	private void getDispatchers(){
		String ip = txtIP.getText().toString();
		String serverName = txtName.getText().toString();
		String port = txtPort.getText().toString();
		if((isValidIp(ip)==true)&& (serverName.length()!=0)){
		try {
			ips=client.getServiceIp(ip,serverName);
		} catch (MalformedURLException e) {
			JOptionPane.showMessageDialog(null,e,"epZilla",JOptionPane.ERROR_MESSAGE);
		} catch (RemoteException e) {
			JOptionPane.showMessageDialog(null,e,"epZilla",JOptionPane.ERROR_MESSAGE);
		} catch (NotBoundException e) {
			JOptionPane.showMessageDialog(null,e,"epZilla",JOptionPane.ERROR_MESSAGE);
		}
		listLookup.setListData(ips);
		}else
			JOptionPane.showMessageDialog(null,"Make sure setting details correct.","epZilla",JOptionPane.ERROR_MESSAGE);
	}
	private void sendFiles(){
		String dispIP = txtDispIP.getText().toString();
		String dispName = txtDispName.getText().toString();
		String fileLocation = txtFile.getText().toString();
		String clientIp = getIpAddress();
		if((dispIP.length()==0) && (dispName.length()==0)){
			JOptionPane.showMessageDialog(null,"Perform Lookup operation and select service you want.","epZilla",JOptionPane.ERROR_MESSAGE);
			return;
		}else if(fileLocation.length()==0){
			JOptionPane.showMessageDialog(null,"Browse file to send.","epZilla",JOptionPane.ERROR_MESSAGE);
			return;
		}
		else if((chkEvents.isSelected()==false)&&(chkTriggers.isSelected()==false)){
			JOptionPane.showMessageDialog(null,"Select either send as Event or send as Triggers.","epZilla",JOptionPane.ERROR_MESSAGE);
			return;
		}else if((chkEvents.isSelected()==true)&&(chkTriggers.isSelected()==true)){
			JOptionPane.showMessageDialog(null,"Select either send as Event or send as Triggers.","epZilla",JOptionPane.ERROR_MESSAGE);
			chkEvents.setSelected(false);
			chkTriggers.setSelected(false);
			return;	
		}
		if((dispIP.length()!=0) && (dispName.length()!=0) && (fileLocation.length()!=0) && (chkEvents.isSelected()==true)){
			try {
				client.uploadEventsFile(dispIP, dispName, fileLocation,clientIp,eventSeqID);
				eventSeqID++;
				JOptionPane.showMessageDialog(null,"Successfully sent the Event file.","epZilla",JOptionPane.INFORMATION_MESSAGE);
			} catch (NotBoundException e) {
				JOptionPane.showMessageDialog(null,e,"epZilla",JOptionPane.ERROR_MESSAGE);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null,e,"epZilla",JOptionPane.ERROR_MESSAGE);
			}
		}else if((dispIP.length()!=0) && (dispName.length()!=0) && (fileLocation.length()!=0) && (chkTriggers.isSelected()==true)){
			try {
				client.uploadTriggersFile(dispIP, dispName, fileLocation,clientIp,triggerSeqID);
				triggerSeqID++;
				JOptionPane.showMessageDialog(null,"Successfully sent the Triggers file.","epZilla",JOptionPane.INFORMATION_MESSAGE);
			} catch (NotBoundException e) {
				JOptionPane.showMessageDialog(null,e,"epZilla",JOptionPane.ERROR_MESSAGE);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null,e,"epZilla",JOptionPane.ERROR_MESSAGE);
			}

		}
		else
			JOptionPane.showMessageDialog(null,"Error in file send process.","epZilla",JOptionPane.ERROR_MESSAGE);
		}
	private String getIpAddress(){
		InetAddress inetAddress = null;
		try {
			inetAddress = InetAddress.getLocalHost();
		} catch (java.net.UnknownHostException e) {
			e.printStackTrace();
		}
    	String ipAddress = inetAddress.getHostAddress();
		return ipAddress;
	}
	private void saveSettings(){
		String ip = txtIP.getText().toString();
		String serverName = txtName.getText().toString();
		String port = txtPort.getText().toString();
		if((isValidIp(ip)==true)&& (serverName.length()!=0) && (isValidPort(port)==true)){
		txtIP.setEditable(false);
		txtName.setEditable(false);
		txtPort.setEditable(false);
		}else
			JOptionPane.showMessageDialog(null,"Enter setting details correctly.","epZilla",JOptionPane.ERROR_MESSAGE);	
	}
	private void cancelSettings(){
		txtIP.setEditable(true);
		txtName.setEditable(true);
		txtPort.setEditable(true);
	}
	public void setResults(String str){
		txtResults.setText(str);
	}
	public void cancelSend(){
		JOptionPane.showMessageDialog(null,"Are you sure cancel the operation.","epZilla",JOptionPane.WARNING_MESSAGE);
		return;
	}
	private void setDispValues(String str){
		StringTokenizer st = new StringTokenizer(str);
		String ip = st.nextToken();
		String servicename = st.nextToken();
		txtDispIP.setText(ip);
		txtDispName.setText(servicename);
		if(isRegister==false){
		try {
			client.regForCallback(ip, servicename);
			isRegister=true;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}
	private void unregisterCallbackLocal(){
		String ip = txtDispIP.getText().toString();
		String servicename = txtDispName.getText().toString();
		try {
			client.unregisterCallback(ip, servicename);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		txtDispIP.setText("");
		txtDispName.setText("");
		ips.removeAllElements();
		listLookup.setListData(ips);
		
	}
	private void showAbout(){
		About abut = new About();
//		abut.show();
        abut.setVisible(true);
	}
	public static boolean isValidIp(String ip)
    {
        boolean format = ip.matches("^[\\d]{1,3}\\.[\\d]{1,3}\\.[\\d]{1,3}\\.[\\d]{1,3}$"); 
        if (format)
        {
            boolean validIp = true;
            String [] values = ip.split("\\.");
            for (int k = 0; k < values.length; ++k)
            {
                short v = Short.valueOf(values[k]).shortValue();
                if ((v < 0) || (v > 255))
                {
                    validIp = false;
                    break;
                }
            }
            return validIp;
        }
        return false;
    }
	private boolean isValidPort(String port){
		 boolean returnValue = true;
	      if (port.length() != 0) {
	        try {
	          int num = Integer.parseInt(port);
	          if(num<1){
	        	  returnValue = false;  
	          }else if(num>65000){
	        	  returnValue = false;
	          }
	        } catch (NumberFormatException e) {
	          returnValue = false;
	        }
	      }else
	    	  returnValue = false;
	      
	      return returnValue;

	} 
//	public static void main(String[] args) {
//		SwingUtilities.invokeLater(new Runnable() {
//			public void run() {
//				ClientUI thisClass = new ClientUI();
//				thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//				thisClass.setVisible(true);
//			}
//		});
//
//	}
	
	public void valueChanged(ListSelectionEvent event) {
		Object source = event.getSource();
		if(source==listLookup){
			int i = listLookup.getSelectedIndex();
			String s = i>=0?ips.get(i):"";
			setDispValues(s);
		}
	}	

	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		if(source==btnBrowse){
			loadFile();
		}else if(source==btnSend){
			sendFiles();
		}else if(source==btnCancel){
			cancelSettings();
		}else if(source==btnCancelSend){
			cancelSend();
		}else if(source==btnClear){
			unregisterCallbackLocal();
		}else if(source==btnSave){
			saveSettings();
		}else if(source==btnLookup){
			getDispatchers();
		}else if(source==btnOK){
			
		}else if(source == adminSettings){
			tabbedPane.setVisible(true);
		}else if(source== closetabs){
			tabbedPane.setVisible(false);
		}else if(source == exit){
			System.exit(0);
		}else if(source==help){
			
		}else if(source==about){
			showAbout();
		}
	}
	public void windowActivated(WindowEvent e) {}

	public void windowClosed(WindowEvent e) {}

	public void windowClosing(WindowEvent e) { 
		ActionListener task = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
        	unregisterCallbackLocal();
            System.exit(0);
        }
        };
}

	public void windowDeactivated(WindowEvent e) {}

	public void windowDeiconified(WindowEvent e) {}

	public void windowIconified(WindowEvent e) {}

	public void windowOpened(WindowEvent e) {}
}
