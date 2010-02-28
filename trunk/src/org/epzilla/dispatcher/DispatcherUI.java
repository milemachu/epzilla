package org.epzilla.dispatcher;

import javax.management.ServiceNotFoundException;
import javax.swing.SwingUtilities;
import java.awt.*;
import javax.swing.*;
import java.awt.Dimension;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.ComponentOrientation;
import javax.swing.JLabel;
import java.awt.Point;
import javax.swing.JTextArea;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.Rectangle;
import javax.swing.JCheckBox;
import java.awt.Font;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.swing.plaf.metal.*;
import javax.swing.JTextPane;



public class DispatcherUI extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;
	private JTabbedPane tabbedPane = null;
	private JTextField txtIP = null;
	private JTextField tbPort = null;
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
	EventListener listener = new EventListener();  //  @jve:decl-index=0:
	
	
	private JTabbedPane getMyTabbedPane() {
		if (tabbedPane == null) {
			lblDetails = new JLabel();
			lblDetails.setBounds(new Rectangle(12, 8, 151, 25));
			lblDetails.setText("NameServer Details");
			labelName = new JLabel();
			labelName.setText("Service Name :");
			labelName.setSize(new Dimension(121, 25));
			labelName.setLocation(new Point(15, 91));
			labelPort = new JLabel();
			labelPort.setText("Port :");
			labelPort.setSize(new Dimension(119, 25));
			labelPort.setLocation(new Point(17, 135));
			labelIP = new JLabel();
			labelIP.setText("Server IP Address :");
			labelIP.setSize(new Dimension(121, 25));
			labelIP.setLocation(new Point(15, 49));
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
			lblName.setBounds(new Rectangle(15, 211, 113, 16));
			lblName.setText("Service Name :");
			lblDisp = new JLabel();
			lblDisp.setBounds(new Rectangle(17, 180, 175, 16));
			lblDisp.setText("Dispatcher Service Details :");
			mainSettings=new JPanel();
			mainSettings.setLayout(null);
			mainSettings.add(getIpTextField(), null);
			mainSettings.add(getTbPort(), null);
			mainSettings.add(getTbName(), null);
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
			summary.add(getTxtTriggers(), null);
			summary.add(getTxtStatus(), null);
			summary.add(lblTriggers, null);
			summary.add(lblEvents, null);
			summary.add(lblIPs, null);
			summary.add(getTxtIPs(), null);
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
			closetabs.setText("Close All");
			closetabs.addActionListener(this);
		}
		return closetabs;
	}
	private JTextField getIpTextField() {
		if (txtIP == null) {
			txtIP = new JTextField();
			txtIP.setLocation(new Point(158, 51));
			txtIP.setSize(new Dimension(200, 20));
		}
		return txtIP;
	}
	private JTextField getTbPort() {
		if (tbPort == null) {
			tbPort = new JTextField();
			tbPort.setLocation(new Point(161, 135));
			tbPort.setSize(new Dimension(200, 20));
		}
		return tbPort;
	}
	private JTextField getTbName() {
		if (txtNameServer == null) {
			txtNameServer = new JTextField();
			txtNameServer.setLocation(new Point(160, 95));
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
			btnCancel.setMnemonic(KeyEvent.VK_UNDEFINED);
			btnCancel.setSize(new Dimension(85, 20));
			btnCancel.addActionListener(this);
		}
		return btnCancel;
	}
	private JTextArea getTxtResult() {
		if (txtResult == null) {
			txtResult = new JTextArea();
			txtResult.setLocation(new Point(19, 340));
			txtResult.setBackground(Color.white);
			txtResult.setSize(new Dimension(447, 132));
		}
		return txtResult;
	}
	public JTextArea getTxtTriggers() {
		if (txtTriggers == null) {
			txtTriggers = new JTextArea();
			txtTriggers.setBounds(new Rectangle(13, 337, 588, 173));
			txtTriggers.setRows(10000);
		}
		return txtTriggers;
	}
	private JTextField getTbDispSerName() {
		if (txtDispSerName == null) {
			txtDispSerName = new JTextField();
			txtDispSerName.setBounds(new Rectangle(164, 211, 195, 20));
		}
		return txtDispSerName;
	}
	public JTextArea getTxtStatus() {
		if (txtStatus == null) {
			txtStatus = new JTextArea();
			txtStatus.setBounds(new Rectangle(15, 47, 587, 177));
		}
		return txtStatus;
	}
	public JTextArea getTxtIPs() {
		if (txtIPs == null) {
			txtIPs = new JTextArea();
			txtIPs.setBounds(new Rectangle(723, 44, 270, 293));
		}
		return txtIPs;
	}
	private void register() {
		String ip = txtIP.getText().toString();
		String nameService = txtNameServer.getText().toString();
		String dispatcherName = txtDispSerName.getText().toString();
		if((isValidIp(ip)==true)&& nameService.length()!=0 && dispatcherName.length()!=0){
			try {
				listener.register(ip, nameService,dispatcherName);
			} catch (MalformedURLException e) {
				JOptionPane.showMessageDialog(null,e,"epZilla",JOptionPane.ERROR_MESSAGE);			
				} catch (RemoteException e) {
					JOptionPane.showMessageDialog(null,e,"epZilla",JOptionPane.ERROR_MESSAGE);
			} catch (UnknownHostException e) {
				JOptionPane.showMessageDialog(null,e,"epZilla",JOptionPane.ERROR_MESSAGE);
			} catch (NotBoundException e) {
				JOptionPane.showMessageDialog(null,e,"epZilla",JOptionPane.ERROR_MESSAGE);	
			}
		}else{
			JOptionPane.showMessageDialog(null,"Enter setting details correctly.","epZilla",JOptionPane.ERROR_MESSAGE);
		}
		}
	private void bind(){
		String dispatcherName = txtDispSerName.getText().toString();
		if(dispatcherName.length()!=0){
		try {
			listener.bindDispatcher(txtDispSerName.getText().toString());
		} catch (RemoteException e) {
			JOptionPane.showMessageDialog(null,e,"epZilla",JOptionPane.ERROR_MESSAGE);
		} catch (UnknownHostException e) {
			JOptionPane.showMessageDialog(null,e,"epZilla",JOptionPane.ERROR_MESSAGE);	
		} catch (MalformedURLException e) {
			JOptionPane.showMessageDialog(null,e,"epZilla",JOptionPane.ERROR_MESSAGE);	
		}
		}
		else
			JOptionPane.showMessageDialog(null,"Enter Dispatcher Service name","epZilla",JOptionPane.ERROR_MESSAGE);
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
	public DispatcherUI() {
		super();
		initialize();
	}
	
	private void initialize() {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = screen.width;
        int y = screen.height;
       	this.setTitle("Dispatcher");
       	this.setSize(new Dimension(628, 439));
       	this.setResizable(false);
       	this.setSize(x,y);
        this.setContentPane(getMyTabbedPane());
        this.setJMenuBar(getmyMenuBar());
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
			
		}else if(source==closetabs){
			tabbedPane.setVisible(false);
		}else if(source==btnCancel){
			
		}else if(source==btnRegister){
			register();
			bind();
		}
	}
	 public static boolean isValidIp(final String ip)
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
}  //  @jve:decl-index=0:visual-constraint="14,9"
