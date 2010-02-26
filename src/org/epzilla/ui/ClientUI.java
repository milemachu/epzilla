package org.epzilla.ui;


import javax.swing.SwingUtilities;
import java.awt.*;
import javax.swing.*;
import java.awt.BorderLayout;
import javax.swing.JButton;
import java.awt.Rectangle;
import javax.swing.JTextField;
import java.awt.ComponentOrientation;
import javax.swing.JLabel;
import java.awt.Point;

import javax.swing.JFileChooser;
import javax.swing.JTextArea;
import javax.swing.JList;
import javax.swing.JPanel;

import java.awt.Dimension;
import java.io.File;
import java.util.Vector;
import javax.swing.JScrollPane;


public class ClientUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JTabbedPane tabbedPane = null;
	private JTextField tbIP = null;
	private JTextField tbPort = null;
	private JTextField tbName = null;
	private JLabel labelIP = null;
	private JLabel labelPort = null;
	private JLabel labelName = null;
	private JButton btnSave = null;
	private JButton btnCancel = null;
	Vector<String> ips = new Vector<String>();  //  @jve:decl-index=0:
	
	private javax.swing.JFileChooser jFileChooser = null;
	private JMenuBar menuBar =null;
	private JMenuItem about = null;
	private JMenuItem help = null;
	private JMenuItem adminSettings = null;
	private JMenuItem closetabs = null;
	private JMenuItem exit = null;
	private JMenuItem m1 = null;
	private JMenuItem m2 = null;
	private JMenu file = new JMenu("File");
	private JMenu helpmenu = new JMenu("Help");
	private JMenu searchmenu = new JMenu("Search");
	private JButton btnBrowse = null;
	private JTextField txtFile = null;
	private JButton btnSend = null;
	private JButton btnCancelsend = null;
	private JLabel lblDetails = null;
	private JList listLookup = null;
	private JButton btnClear = null;
	private JButton btnLookup = null;
	private JLabel lblDispIP1 = null;
	private JTextField txtDispIP = null;
	private JLabel lblSettings = null;
	private JLabel lblResultsl = null;
	private JButton btnOK = null;
	private JButton btnView = null;
	private JScrollPane resultsScrollPane = null;
	private JTextArea resultsTextArea = null;
	EventListener listener = new EventListener();
	
	public ClientUI() {
		super();
		initialize();
	}
	private void initialize() {
		int width = 750;
        int height =600;
        this.setSize(new Dimension(763, 581));
        this.setMaximumSize(new Dimension(650, 450));
        this.setContentPane(getMyTabbedPane());
        this.setJMenuBar(getmyMenuBar());
        
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width-width)/2;
        int y = (screen.height-height)/2;
        setBounds(x,y,width,height);
       	this.setTitle("Epzilla DS");		
	}
	private JTabbedPane getMyTabbedPane() {
		if (tabbedPane == null) {
			lblResultsl = new JLabel();
			lblResultsl.setBounds(new Rectangle(28, 10, 88, 16));
			lblResultsl.setText("Results");
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
		mainSettings.add(getIpTextField2(), null);
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
		upload.add(getBtnCancelsend(), null);
		upload.add(lblDetails, null);
		upload.add(getListLookup(), null);
		upload.add(getBtnClear(), null);
		upload.add(getBtnLookup(), null);
		upload.add(lblDispIP1, null);
		upload.add(getTxtDispIP1(), null);
		JPanel results = new JPanel();
		results.setLayout(null);
		results.add(lblResultsl, null);
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
			searchmenu.add(getMI1());
			searchmenu.add(getMI2());
			menuBar.add(file);
			menuBar.add(helpmenu);
			menuBar.add(searchmenu);
			}
		return menuBar;
	}
	private JMenuItem getAboutMI(){
		if(about==null){
			about = new JMenuItem();
			about.setText("About");
			about.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
				}
			});
		}
		return about;
	}
	private JMenuItem getHelpMI(){
		if(help==null){
			help = new JMenuItem();
			help.setText("Help");
			help.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
				}
			});
		}
		return help;
	}
	private JMenuItem getAdminSettingMI(){
		if(adminSettings==null){
			adminSettings = new JMenuItem();
			adminSettings.setText("Admin");
			adminSettings.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
				tabbedPane.setVisible(true);
				}
			});
		}
		return adminSettings;
	}
	private JMenuItem getExitMI(){
		if(exit==null){
			exit = new JMenuItem();
			exit.setText("Exit");
			exit.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.exit(0);
				}
			});
		}
		return exit;
	}
	private JMenuItem getCloseMI(){
		if(closetabs==null){
			closetabs = new JMenuItem();
			closetabs.setText("Close Tabs");
			closetabs.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					tabbedPane.setVisible(false);
				}
			});
		}
		return closetabs;
	}
	private JMenuItem getMI1(){
		if(m1==null){
			m1 = new JMenuItem();
			m1.setText("Menu Item1");
			m1.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					//to be done
				}
			});
		}
		return m1;
	}
	private JMenuItem getMI2(){
		if(m2==null){
			m2 = new JMenuItem();
			m2.setText("Menu Item2");
			m2.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					//to be done
				}
			});
		}
		return m2;
	}
	private JTextField getIpTextField2() {
		if (tbIP == null) {
			tbIP = new JTextField();
			tbIP.setLocation(new Point(110, 52));
			tbIP.setSize(new Dimension(200, 20));
		}
		return tbIP;
	}
	private JTextField getTbPort() {
		if (tbPort == null) {
			tbPort = new JTextField();
			tbPort.setLocation(new Point(112, 140));
			tbPort.setSize(new Dimension(204, 20));
		}
		return tbPort;
	}
	private JTextField getTbName() {
		if (tbName == null) {
			tbName = new JTextField();
			tbName.setLocation(new Point(111, 96));
			tbName.setSize(new Dimension(200, 20));
		}
		return tbName;
	}
	private JButton getBtnSave() {
		if (btnSave == null) {
			btnSave = new JButton();
			btnSave.setLocation(new Point(121, 183));
			btnSave.setText("Save");
			btnSave.setSize(new Dimension(85, 25));
			btnSave.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					saveSettings();
				}
			});
		}
		return btnSave;
	}
	private void saveSettings(){
		tbIP.setEditable(false);
		tbName.setEditable(false);
		tbPort.setEditable(false);
	}
	private JButton getBtnCancel() {
		if (btnCancel == null) {
			btnCancel = new JButton();
			btnCancel.setLocation(new Point(218, 182));
			btnCancel.setText("Cancel");
			btnCancel.setSize(new Dimension(85, 25));
			btnCancel.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
						cancelSettings();
				}
			});
		}
		return btnCancel;
	}
	private void cancelSettings(){
		tbIP.setEditable(true);
		tbName.setEditable(true);
		tbPort.setEditable(true);
	}
	private javax.swing.JFileChooser getJFileChooser() {
		if (jFileChooser == null) {
			jFileChooser = new javax.swing.JFileChooser();
			jFileChooser.setMultiSelectionEnabled(false);
			jFileChooser.setSize(new Dimension(433, 299));
		}
		return jFileChooser;
	}

	private void loadFile(){
		int state = getJFileChooser().showOpenDialog(this);
		if (state == JFileChooser.APPROVE_OPTION) {
			File f = getJFileChooser().getSelectedFile();
			txtFile.setText(f.getAbsolutePath());
			}
	}
	private JButton getBtnBrowse() {
		if (btnBrowse == null) {
			btnBrowse = new JButton();
			btnBrowse.setText("Browse");			btnBrowse.setBounds(new Rectangle(13, 212, 85, 25));
			btnBrowse.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					loadFile();
				}
			});

		}
		return btnBrowse;
	}
	private JTextField getTxtFile() {
		if (txtFile == null) {
			txtFile = new JTextField();
			txtFile.setPreferredSize(new Dimension(4, 20));			txtFile.setBounds(new Rectangle(169, 211, 320, 25));

		}
		return txtFile;
	}
	private JButton getBtnSend() {
		if (btnSend == null) {
			btnSend = new JButton();
			btnSend.setText("Send");			btnSend.setBounds(new Rectangle(231, 256, 85, 25));
			btnSend.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					sendFiles();				}
			});
		}
		return btnSend;
	}
	private void sendFiles(){
		listener.uploadFiles(txtDispIP.getText().toString(),"Dispatcher", txtFile.getText().toString());
	}
	private JButton getBtnCancelsend() {
		if (btnCancelsend == null) {
			btnCancelsend = new JButton();
			btnCancelsend.setText("Cancel");			btnCancelsend.setBounds(new Rectangle(338, 255, 85, 25));
			btnCancelsend.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
				}
			});
		}
		return btnCancelsend;
	}
	private JList getListLookup() {
		if (listLookup == null) {
			listLookup = new JList();
			listLookup.setBounds(new Rectangle(163, 46, 194, 72));
		}
		return listLookup;
	}
	private JButton getBtnClear() {
		if (btnClear == null) {
			btnClear = new JButton();
			btnClear.setBounds(new Rectangle(15, 92, 85, 25));
			btnClear.setText("Clear");
			btnClear.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					clearList();
				}
			});
		}
		return btnClear;
	}
	private void clearList(){
		listLookup.clearSelection();
	}
	private JButton getBtnLookup() {
		if (btnLookup == null) {
			btnLookup = new JButton();
			btnLookup.setBounds(new Rectangle(13, 45, 85, 25));
			btnLookup.setText("Lookup");
			btnLookup.setName("");
			btnLookup.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					getDispatchers();
				}
			});
		}
		return btnLookup;
	}
	private JTextField getTxtDispIP1() {
		if (txtDispIP == null) {
			txtDispIP = new JTextField();
			txtDispIP.setBounds(new Rectangle(164, 147, 192, 20));
			txtDispIP.setPreferredSize(new Dimension(4, 20));
		}
		return txtDispIP;
	}
	private JButton getBtnOK() {
		if (btnOK == null) {
			btnOK = new JButton();
			btnOK.setText("OK");
			btnOK.setSize(new Dimension(85, 25));
			btnOK.setLocation(new Point(108, 369));
		}
		return btnOK;
	}
	private JButton getBtnView() {
		if (btnView == null) {
			btnView = new JButton();
			btnView.setLocation(new Point(233, 371));
			btnView.setText("View");
			btnView.setSize(new Dimension(85, 25));
		}
		return btnView;
	}
	private JScrollPane getResultsScrollPane() {
		if (resultsScrollPane == null) {
			resultsScrollPane = new JScrollPane();
			resultsScrollPane.setBounds(new Rectangle(37, 59, 680, 217));
			resultsScrollPane.setViewportView(getResultsTextArea());
		}
		return resultsScrollPane;
	}
	private JTextArea getResultsTextArea() {
		if (resultsTextArea == null) {
			resultsTextArea = new JTextArea();
		}
		return resultsTextArea;
	}
	private void getDispatchers(){
		ips=listener.lookUP(tbIP.getText().toString(), tbName.getText().toString());
		listLookup.setListData(ips);
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
	
} 
