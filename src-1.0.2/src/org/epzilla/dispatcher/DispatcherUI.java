package org.epzilla.dispatcher;

import javax.swing.*;
import java.awt.Dimension;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Point;
import javax.swing.JTextArea;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Font;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.JScrollPane;

import org.epzilla.ui.ServerSettingsReader;

import java.awt.Toolkit;



public class DispatcherUI extends JFrame implements ActionListener{
	private JTabbedPane tabbedPane = null;
	private JTextField txtIP = null;
	private JTextField txtPort = null;
	private JTextField txtNameServer = null;
	private JLabel labelIP = null;
	private JLabel labelPort = null;
	private JLabel labelName = null;
	private JButton btnRegister = null;
	private JButton btnCancel = null;
	private JTextArea txtResult = null;
	private JLabel lblDetails = null;
	private JMenuBar menuBar =null;
	private JMenuItem about = null;
	private JMenuItem help = null;
	private JMenuItem adminSettings = null;
	private JMenuItem closetabs = null;
	private JMenuItem exit = null;
	private JMenu file = new JMenu("File");
	private JMenu helpmenu = new JMenu("Help");
	JPanel mainSettings = null;
	JPanel helptab = null;
	JPanel summary =null;
	private JTextArea txtTriggers = null;
	private JLabel lblDisp = null;
	private JLabel lblName = null;
	private JTextField txtDispSerName = null;
	private JTextArea txtStatus = null;
	private JLabel lblTriggers = null;
	private JLabel lblEvents = null;
	private JLabel lblIPs = null;
	private JTextArea txtIPs = null;
	private JScrollPane statusScrollPane = null;
	private JScrollPane triggerListScrollPane = null;
	private JScrollPane ipScrollPane = null;
	EventListener listener = new EventListener();
	static ServerSettingsReader reader = new ServerSettingsReader();
	
	public DispatcherUI() {
		super();
		initialize();
	}	
	private void initialize() {
//		 try {
//				UIManager.setLookAndFeel("com.pagosoft.plaf.PgsLookAndFeel");
//				SwingUtilities.updateComponentTreeUI(this);
//			} catch(Exception e) {}
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = screen.width;
        int y = screen.height;
       	this.setTitle("Dispatcher");
       	Image img = Toolkit.getDefaultToolkit().getImage("images//logo.jpg");
       	this.setIconImage(img);
       	this.setResizable(false);
       	this.setSize(new Dimension(899, 632));
       	this.setSize(new Dimension(689, 439));
       	this.setResizable(false);
       	this.setSize(x,y);
        this.setContentPane(getMyTabbedPane());
        this.setJMenuBar(getmyMenuBar());
        loadSettings();
        	}
	private JTabbedPane getMyTabbedPane() {
		if (tabbedPane == null) {
			lblDetails = new JLabel();
			lblDetails.setText("NameServer Details ");
			lblDetails.setLocation(new Point(15, 8));
			lblDetails.setFont(new Font("Dialog", Font.BOLD, 12));
			lblDetails.setSize(new Dimension(151, 25));
			labelName = new JLabel();
			labelName.setText("Service Name :");
			labelName.setSize(new Dimension(93, 25));
			labelName.setLocation(new Point(49, 92));
			labelPort = new JLabel();
			labelPort.setText("Port :");
			labelPort.setSize(new Dimension(41, 22));
			labelPort.setLocation(new Point(105, 132));
			labelIP = new JLabel();
			labelIP.setText("Server IP Address :");
			labelIP.setSize(new Dimension(121, 25));
			labelIP.setLocation(new Point(25, 49));
			tabbedPane = new JTabbedPane();	
		tabbedPane.addTab("Settings",getMainSettings());
		tabbedPane.addTab("Summary",getSummeryTab());
		tabbedPane.setVisible(false);
		}
		return tabbedPane;
	}
	private JPanel getMainSettings(){
		if(mainSettings==null){
			lblName = new JLabel();
			lblName.setText("Service Name :");
			lblName.setLocation(new Point(53, 203));
			lblName.setSize(new Dimension(94, 16));
			lblDisp = new JLabel();
			lblDisp.setText("Dispatcher Details ");
			lblDisp.setLocation(new Point(15, 171));
			lblDisp.setFont(new Font("Dialog", Font.BOLD, 12));
			lblDisp.setSize(new Dimension(175, 22));
			mainSettings=new JPanel();
			mainSettings.setLayout(null);
			mainSettings.add(getTxtNSIp(), null);
			mainSettings.add(getTxtPort(), null);
			mainSettings.add(getTxtNameServer(), null);
			mainSettings.add(labelIP, null);
			mainSettings.add(labelPort, null);
			mainSettings.add(labelName, null);
			mainSettings.add(getBtnRegister(), null);
			mainSettings.add(getBtnCancel(), null);
			mainSettings.add(getTxtResult(), null);
			mainSettings.add(lblDetails, null);
			mainSettings.add(lblDisp, null);
			mainSettings.add(lblName, null);
			mainSettings.add(getTbDispSerName(), null);
		}
		return mainSettings;
	}
	private JPanel getSummeryTab(){
		if(summary==null){
			lblIPs = new JLabel();
			lblIPs.setBounds(new Rectangle(720, 17, 38, 16));
			lblIPs.setText("IP :");
			lblEvents = new JLabel();
			lblEvents.setBounds(new Rectangle(16, 21, 117, 16));
			lblEvents.setText("Status :");
			lblTriggers = new JLabel();
			lblTriggers.setBounds(new Rectangle(15, 313, 116, 16));
			lblTriggers.setText("Trigger List :");
			summary = new JPanel();
			summary.setLayout(null);
			summary.add(getTriggerScrollPane(), null);
			summary.add(getStatusScrollPane(), null);
			summary.add(getIpScrollPane(), null);
			summary.add(lblTriggers, null);
			summary.add(lblEvents, null);
			summary.add(lblIPs, null);
		}
		return summary;
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
	private JTextField getTxtNSIp() {
		if (txtIP == null) {
			txtIP = new JTextField();
			txtIP.setLocation(new Point(150, 51));
			txtIP.setSize(new Dimension(200, 20));
		}
		return txtIP;
	}
	private JTextField getTxtPort() {
		if (txtPort == null) {
			txtPort = new JTextField();
			txtPort.setLocation(new Point(150, 135));
			txtPort.setSize(new Dimension(200, 20));
		}
		return txtPort;
	}
	private JTextField getTxtNameServer() {
		if (txtNameServer == null) {
			txtNameServer = new JTextField();
			txtNameServer.setLocation(new Point(150, 95));
			txtNameServer.setSize(new Dimension(200, 20));
		}
		return txtNameServer;
	}
	private JButton getBtnRegister() {
		if (btnRegister == null) {
			btnRegister = new JButton();
			btnRegister.setLocation(new Point(164, 269));
			btnRegister.setText("Register");
			btnRegister.setSize(new Dimension(85, 20));
			btnRegister.addActionListener(this);
		}
		return btnRegister;
	}
	private JButton getBtnCancel() {
		if (btnCancel == null) {
			btnCancel = new JButton();
			btnCancel.setLocation(new Point(271, 270));
			btnCancel.setText("Cancel");
			btnCancel.setSize(new Dimension(85, 20));
			btnCancel.addActionListener(this);
		}
		return btnCancel;
	}
	public JTextArea getTxtResult() {
		if (txtResult == null) {
			txtResult = new JTextArea();
			txtResult.setLocation(new Point(19, 340));
			txtResult.setBackground(Color.white);
			txtResult.setSize(new Dimension(447, 132));
		}
		return txtResult;
	}
	private JScrollPane getTriggerScrollPane() {
		if (triggerListScrollPane == null) {
			triggerListScrollPane = new JScrollPane();
			triggerListScrollPane.setBounds(new Rectangle(13, 337, 588, 173));
			triggerListScrollPane.setViewportView(getTxtTriggers());
		}
		return triggerListScrollPane;
	}
	public JTextArea getTxtTriggers() {
		if (txtTriggers == null) {
			txtTriggers = new JTextArea();
			txtTriggers.setBounds(new Rectangle(13, 337, 588, 173));
		}
		return txtTriggers;
	}
	private JTextField getTbDispSerName() {
		if (txtDispSerName == null) {
			txtDispSerName = new JTextField();
			txtDispSerName.setSize(new Dimension(200, 20));
			txtDispSerName.setText("Dispatcher");
			txtDispSerName.setLocation(new Point(150, 202));
		}
		return txtDispSerName;
	}
	private JScrollPane getStatusScrollPane() {
		if (statusScrollPane == null) {
			statusScrollPane = new JScrollPane();
			statusScrollPane.setBounds(new Rectangle(15, 47, 587, 177));
			statusScrollPane.setViewportView(getTxtStatus());
		}
		return statusScrollPane;
	}
	public JTextArea getTxtStatus() {
		if (txtStatus == null) {
			txtStatus = new JTextArea();
			txtStatus.setBounds(new Rectangle(15, 47, 587, 177));
		}
		return txtStatus;
	}
	private JScrollPane getIpScrollPane() {
		if (ipScrollPane == null) {
			ipScrollPane = new JScrollPane();
			ipScrollPane.setBounds(new Rectangle(723, 44, 270, 400));
			ipScrollPane.setViewportView(getTxtIPSet());
		}
		return ipScrollPane;
	}
	public JTextArea getTxtIPSet(){
		if (txtIPs == null) {
			txtIPs = new JTextArea();
			txtIPs.setBounds(new Rectangle(723, 44, 270, 400));
		}
		return txtIPs;
	}
	private void register() throws MalformedURLException, RemoteException, UnknownHostException, NotBoundException {
		String ip = txtIP.getText().toString();
		String nameService = txtNameServer.getText().toString();
		String dispatcherName = txtDispSerName.getText().toString();
		String port = txtPort.getText().toString();
		if(isValidIp(ip)==false){
			JOptionPane.showMessageDialog(null,"Enter valid IP Address of NameServer.","epZilla",JOptionPane.ERROR_MESSAGE);
			return;
		}
		if(isValidPort(port)==false){
			JOptionPane.showMessageDialog(null,"Enter valid Port number","epZilla",JOptionPane.ERROR_MESSAGE);
			return;
		}
		if(nameService.length()!=0 && dispatcherName.length()!=0){
				listener.register(ip, nameService,port,dispatcherName);
				bind();
		}else{
			JOptionPane.showMessageDialog(null,"Dispatcher registration fails. Enter setting details correctly.","epZilla",JOptionPane.ERROR_MESSAGE);
		}
		}
	private void bind() throws RemoteException, UnknownHostException, MalformedURLException{
		String dispatcherName = txtDispSerName.getText().toString();
			listener.bindDispatcher(dispatcherName);
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
	 private static boolean isValidIp(final String ip)
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
	 public void showAbout(){
		 About abut = new About();
			abut.show();
	 }
	 private void loadSettings(){
			try {
				ArrayList<String[]> data = reader.getServerIPSettings("./src/server_settings.xml");
				String[] ar = data.get(0);
				txtIP.setText(ar[0]);
				txtPort.setText(ar[1]);
				txtNameServer.setText(ar[2]);
				} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				DispatcherUI thisClass = new DispatcherUI();
				thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				thisClass.setVisible(true);
			}
		});
	}
		@Override
	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		if(source==adminSettings){
			tabbedPane.setVisible(true);
		}else if(source==help){
			
		}else if(source==exit){
			System.exit(0);
		}else if(source==about){
			showAbout();
		}else if(source==closetabs){
			tabbedPane.setVisible(false);
		}else if(source==btnCancel){
			
		}else if(source==btnRegister){
			try {
				register();
			} catch (MalformedURLException e) {
				JOptionPane.showMessageDialog(null,e,"epZilla",JOptionPane.ERROR_MESSAGE);
			} catch (RemoteException e) {
				JOptionPane.showMessageDialog(null,e,"epZilla",JOptionPane.ERROR_MESSAGE);
			} catch (UnknownHostException e) {
				JOptionPane.showMessageDialog(null,e,"epZilla",JOptionPane.ERROR_MESSAGE);
			} catch (NotBoundException e) {
				JOptionPane.showMessageDialog(null,e,"epZilla",JOptionPane.ERROR_MESSAGE);
			}
			
		}
	}
	}