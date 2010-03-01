package org.epzilla.ui;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.*;
import javax.swing.*;
import java.awt.Rectangle;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.Point;
import java.awt.Dimension;

public class ClientUI extends JFrame implements ActionListener,ListSelectionListener{

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
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
	private JButton btnView = null;
	private JScrollPane resultsScrollPane = null;
	public JTextArea txtResults = null;
	EventListener listener = new EventListener();  //  @jve:decl-index=0:

	private JLabel lblDispatcherServiceName = null;
	private JTextField txtDispName = null;
	
	public ClientUI() {
		super();
		initialize();
	}
	private void initialize() {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = screen.width;
        int y = screen.height;
       	this.setSize(new Dimension(628, 535));
       	this.setResizable(false);
       	this.setSize(x,y);
        this.setContentPane(getMyTabbedPane());
        this.setJMenuBar(getmyMenuBar());
       	this.setTitle("Epzilla DS");		
	}
	private JTabbedPane getMyTabbedPane() {
		if (tabbedPane == null) {
			lblDispatcherServiceName = new JLabel();
			lblDispatcherServiceName.setText("Service Name :");
			lblDispatcherServiceName.setLocation(new Point(15, 178));
			lblDispatcherServiceName.setSize(new Dimension(123, 16));
			lblSettings = new JLabel();
			lblSettings.setBounds(new Rectangle(17, 15, 196, 16));
			lblSettings.setText("Server Settings");
			lblDispIP1 = new JLabel();
			lblDispIP1.setBounds(new Rectangle(15, 148, 141, 16));
			lblDispIP1.setText("Dispatcher IP Selected :");
			lblDetails = new JLabel();
			lblDetails.setBounds(new Rectangle(15, 16, 259, 16));
			lblDetails.setText("Lookup available services in the System");
			labelName = new JLabel();
			labelName.setText("Name :");
			labelName.setSize(new Dimension(80, 25));
			labelName.setLocation(new Point(14, 91));
			labelPort = new JLabel();
			labelPort.setText("Port :");
			labelPort.setSize(new Dimension(80, 25));
			labelPort.setLocation(new Point(15, 136));
			labelIP = new JLabel();
			labelIP.setText("IP Address :");
			labelIP.setSize(new Dimension(80, 25));
			labelIP.setLocation(new Point(15, 49));
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
		JPanel results = new JPanel();
		results.setLayout(null);
		results.add(getBtnOK(), null);
		results.add(getBtnView(), null);
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
			btnBrowse.setText("Browse");			btnBrowse.setLocation(new Point(15, 218));
			btnBrowse.setSize(new Dimension(85, 20));
			btnBrowse.addActionListener(this);
		}
		return btnBrowse;
	}
	private JTextField getTxtFile() {
		if (txtFile == null) {
			txtFile = new JTextField();
			txtFile.setPreferredSize(new Dimension(4, 20));			txtFile.setSize(new Dimension(325, 20));
			txtFile.setEditable(false);
			txtFile.setLocation(new Point(165, 219));
		}
		return txtFile;
	}
	private JButton getBtnSend() {
		if (btnSend == null) {
			btnSend = new JButton();
			btnSend.setText("Send");			btnSend.setLocation(new Point(231, 255));
			btnSend.setSize(new Dimension(85, 20));
			btnSend.addActionListener(this);
			}
		return btnSend;
	}
	private JButton getBtnCancelSend() {
		if (btnCancelSend == null) {
			btnCancelSend = new JButton();
			btnCancelSend.setText("Cancel");			btnCancelSend.setBounds(new Rectangle(338, 255, 85, 20));
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
			btnOK.setLocation(new Point(172, 419));
		}
		return btnOK;
	}
	private JButton getBtnView() {
		if (btnView == null) {
			btnView = new JButton();
			btnView.setLocation(new Point(272, 417));
			btnView.setText("View");
			btnView.setSize(new Dimension(85, 20));
			btnView.addActionListener(this);
		}
		return btnView;
	}
	private JScrollPane getResultsScrollPane() {
		if (resultsScrollPane == null) {
			resultsScrollPane = new JScrollPane();
			resultsScrollPane.setBounds(new Rectangle(24, 15, 734, 390));
			resultsScrollPane.setViewportView(getTxtResults());
		}
		return resultsScrollPane;
	}
	private JTextArea getTxtResults() {
		if (txtResults == null) {
			txtResults = new JTextArea();
			txtResults.setSize(new Dimension(700, 214));
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
		if((isValidateIp(ip)==true) && (serverName.length()!=0)){
		try {
			ips=listener.lookUP(ip,serverName);
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
	private void clearList(){
		ips.removeAllElements();
		listLookup.setListData(ips);
	}
	private void sendFiles(){
		String dispIP = txtDispIP.getText().toString();
		String dispName = txtDispName.getText().toString();
		String fileLocation = txtFile.getText().toString();
		if((dispIP.length()==0) && (dispName.length()==0)){
			JOptionPane.showMessageDialog(null,"Perform Lookup operation and select service you want.","epZilla",JOptionPane.ERROR_MESSAGE);
			return;
		}
		if((dispIP.length()!=0) && (dispName.length()!=0) && (fileLocation.length()!=0) ){
			try {
				listener.uploadFiles(dispIP, dispName, fileLocation);
			} catch (NotBoundException e) {
				JOptionPane.showMessageDialog(null,e,"epZilla",JOptionPane.ERROR_MESSAGE);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null,e,"epZilla",JOptionPane.ERROR_MESSAGE);
			}
		}else
			JOptionPane.showMessageDialog(null,"Browse file to send.","epZilla",JOptionPane.ERROR_MESSAGE);
		}
	private void saveSettings(){
		String ip = txtIP.getText().toString();
		String serverName = txtName.getText().toString();
		if((isValidateIp(ip)==true)&& serverName.length()!=0){
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
		JOptionPane.showMessageDialog(null,"Are you sure cancel the operation.","epZilla",JOptionPane.INFORMATION_MESSAGE);
//		txtDispIP.setText("");
//		txtDispName.setText("");
	}
	private void setDispValues(String str){
		StringTokenizer st = new StringTokenizer(str);
		String ip = st.nextToken();
		String servicename = st.nextToken();
		txtDispIP.setText(ip);
		txtDispName.setText(servicename);
	}
	private void showAbout(){
		About abut = new About();
		abut.show();
	}
	public static void main(String[] args) {
		ClientUI ui = new ClientUI();
		ui.start();
	}
	public void start(){
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				ClientUI thisClass = new ClientUI();
				thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				thisClass.setVisible(true);
			}
		});
	}
	@Override
	public void valueChanged(ListSelectionEvent event) {
		Object source = event.getSource();
		if(source==listLookup){
			int i = listLookup.getSelectedIndex();
			String s = i>=0?ips.get(i):"";
			setDispValues(s);
		}
	}	
	@Override
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
			clearList();
		}else if(source==btnSave){
			saveSettings();
		}else if(source==btnLookup){
			getDispatchers();
		}else if(source == btnView){
			
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
	public static boolean isValidateIp(String ip)
    {
        boolean correctFormat = ip.matches("^[\\d]{1,3}\\.[\\d]{1,3}\\.[\\d]{1,3}\\.[\\d]{1,3}$"); 
        if (correctFormat)
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
	
}  //  @jve:decl-index=0:visual-constraint="54,12" 
