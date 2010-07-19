package org.epzilla.dispatcher.ui;

import org.epzilla.dispatcher.DispatcherRegister;
import org.epzilla.dispatcher.controlers.DispatcherUIController;
import org.epzilla.dispatcher.dataManager.RecoveredTriggers;
import org.epzilla.dispatcher.logs.ReadLog;
import org.epzilla.dispatcher.xml.ConfigurationManager;
import org.epzilla.dispatcher.xml.ServerSettingsReader;
import org.epzilla.util.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * This is the UI class of the Dispatcher
 * Author: Chathura
 * To change this template use File | Settings | File Templates.
 */
public class DispatcherUI extends JFrame implements ActionListener {
    private JTabbedPane tabbedPane = null;
    private JTextField txtIP = null;
    private JTextField txtPort = null;
    private JTextField txtNameServer = null;
    private JLabel labelIP = null;
    private JLabel labelPort = null;
    private JLabel labelName = null;
    private JButton btnRegister = null;
    private JButton btnLoadSettings = null;
    private JLabel lblDetails = null;
    private JMenuBar menuBar = null;
    private JMenuItem about = null;
    private JMenuItem help = null;
    private JMenuItem adminSettings = null;
    private JMenuItem closetabs = null;
    private JMenuItem exit = null;
    private JPanel mainSettings = null;
    private JPanel summary = null;
    private JPanel dispStatus = null;
    private JPanel configPanel = null;
    private JTextArea txtTriggers = null;
    private JTextField txtDispSerName = null;
    private JTextArea txtStatus = null;
    private JTextArea txtIPs = null;
    private JScrollPane statusScrollPane = null;
    private JScrollPane triggerListScrollPane = null;
    private JScrollPane ipScrollPane = null;
    private JScrollPane resultScrollPane = null;
    private JScrollPane discoveryStatusPane = null;
    private JScrollPane recTriggerList = null;
    private JScrollPane dispIps = null;
    private JTextField txtInEventCount = null;
    private JTextArea txtResult = null;
    private boolean isRegister = false;
    private JTextField txtOutEventCount = null;
    private JTextField txtEDRate = null;
    private JTextArea txtDiscoveryStatus = null;
    private JCheckBox chkLogs = null;
    private JButton btnReplayLogs = null;
    private JTextArea txtClusterPerformance = null;
    private JTextArea txtRecoveredList = null;
    private JTextArea txtDispIps = null;
    private JPanel mainPanel = null;
    private JButton btnSaveConfig;
    private JButton btnClearConfig;
    private JTextArea txtIp = new JTextArea();
    private JTextArea txtAcc = new JTextArea();
    private JTextField txtClusterID = new JTextField();

    public DispatcherUI() {
        initialize();
    }

    private void initialize() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (UnsupportedLookAndFeelException e) {
            Logger.error("UI eror:", e);
        }
        catch (ClassNotFoundException e) {
            Logger.error("UI eror:", e);
        }
        catch (InstantiationException e) {
            Logger.error("UI eror:", e);
        }
        catch (IllegalAccessException e) {
            Logger.error("UI eror:", e);
        }
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = screen.width;
        int y = screen.height;
        this.setTitle("Dispatcher");
        this.setBackground(SystemColor.control);
        Image img = Toolkit.getDefaultToolkit().getImage("images//logo.jpg");
        this.setIconImage(img);
        this.setSize(x, y);
        this.setPreferredSize(new Dimension(1024, 768));
        this.setContentPane(getMyTabbedPane());
        this.setJMenuBar(getmyMenuBar());
        loadSettings();
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "Confirm Exit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.YES_OPTION) {
                    dispose();
                    System.exit(0);
                }
            }
        });
    }

    private JTabbedPane getMyTabbedPane() {
        if (tabbedPane == null) {
            ImageIcon settingsIcon = new ImageIcon("images//settings.jpg");
            ImageIcon summaryIcon = new ImageIcon("images//summary.jpg");
            ImageIcon dispStatusIcon = new ImageIcon("images//clusterDe.jpg");

            lblDetails = new JLabel();
            lblDetails.setText("NameServer Details ");
            lblDetails.setLocation(new Point(15, 8));
            lblDetails.setFont(new Font("Dialog", Font.BOLD, 12));
            lblDetails.setSize(new Dimension(151, 25));
            labelName = new JLabel();
            labelName.setText("Service Name :");
            labelName.setSize(new Dimension(93, 25));
            labelName.setLocation(new Point(30, 92));
            labelPort = new JLabel();
            labelPort.setText("Port :");
            labelPort.setSize(new Dimension(41, 22));
            labelPort.setLocation(new Point(30, 132));
            labelIP = new JLabel();
            labelIP.setText("Server IP Address :");
            labelIP.setSize(new Dimension(121, 25));
            labelIP.setLocation(new Point(30, 49));

            tabbedPane = new JTabbedPane();
            tabbedPane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
            tabbedPane.setBackground(SystemColor.control);
            tabbedPane.addTab("Summary", summaryIcon, getSummeryTab());
            tabbedPane.addTab("Dispatcher Status", dispStatusIcon, getDispStatusTab());
            tabbedPane.addTab("Settings", settingsIcon, getMainSettings());
//            tabbedPane.addTab("Configurations", getConfigurations());
            tabbedPane.setVisible(true);
        }
        return tabbedPane;
    }

    private JPanel getMainSettings() {
        if (mainSettings == null) {

            JLabel lblStatus = new JLabel("Status :");
            lblStatus.setBounds(new Rectangle(400, 10, 71, 25));
            lblStatus.setFont(new Font("Dialog", Font.BOLD, 12));
            JLabel lblName = new JLabel("Service Name :");
            lblName.setLocation(new Point(30, 203));
            lblName.setSize(new Dimension(94, 25));
            JLabel lblDisp = new JLabel("Dispatcher Details ");
            lblDisp.setLocation(new Point(15, 171));
            lblDisp.setFont(new Font("Dialog", Font.BOLD, 12));
            lblDisp.setSize(new Dimension(175, 25));
            mainSettings = new JPanel() {
                public void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g;
                    int w = getWidth();
                    int h = getHeight();
                    GradientPaint gp = new GradientPaint(0, 100, Color.white, 0, h, Color.gray);
                    g2d.setPaint(gp);
                    g2d.fillRect(0, 0, w, h);
                }
            };
            mainSettings.setLayout(null);
            mainSettings.add(getTxtNSIp(), null);
            mainSettings.add(getTxtPort(), null);
            mainSettings.add(getTxtNameServer(), null);
            mainSettings.add(labelIP, null);
            mainSettings.add(labelPort, null);
            mainSettings.add(labelName, null);
            mainSettings.add(getBtnRegister(), null);
            mainSettings.add(getBtnLoadSetings(), null);
            mainSettings.add(lblDetails, null);
            mainSettings.add(lblDisp, null);
            mainSettings.add(lblName, null);
            mainSettings.add(getTbDispSerName(), null);
            mainSettings.add(lblStatus, null);
            mainSettings.add(getResultPane(), null);
            mainSettings.add(getChkLogs(), null);
            mainSettings.add(getBtnReplayLogs(), null);
            mainSettings.add(getConfigurations(), null);
        }
        return mainSettings;
    }

    private JPanel getSummeryTab() {
        if (summary == null) {
            JLabel lblClusterPer = new JLabel();
            lblClusterPer.setBounds(new Rectangle(713, 313, 151, 25));
            lblClusterPer.setText("Cluster Performance:");
            JLabel lblOutEC = new JLabel();
            lblOutEC.setBounds(new Rectangle(220, 553, 138, 25));
            lblOutEC.setText("Outgoing Event Count :");
            JLabel lblInEC = new JLabel();
            lblInEC.setBounds(new Rectangle(15, 553, 140, 25));
            lblInEC.setText("Incoming Event Count :");
            JLabel lblIPs = new JLabel();
            lblIPs.setBounds(new Rectangle(713, 17, 150, 25));
            lblIPs.setText("Cluster Leader IP List :");
            JLabel lblEvents = new JLabel();
            lblEvents.setBounds(new Rectangle(15, 21, 117, 25));
            lblEvents.setText("Status :");
            JLabel lblTriggers = new JLabel();
            lblTriggers.setBounds(new Rectangle(15, 313, 116, 25));
            lblTriggers.setText("Trigger List :");
            summary = new JPanel() {
                public void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g;
                    int w = getWidth();
                    int h = getHeight();
                    GradientPaint gp = new GradientPaint(0, 100, Color.white, 0, h, Color.gray);
                    g2d.setPaint(gp);
                    g2d.fillRect(0, 0, w, h);
                }
            };
            summary.setLayout(null);
            summary.add(getTriggerScrollPane(), null);
            summary.add(getStatusScrollPane(), null);
            summary.add(getIpScrollPane(), null);
            summary.add(lblTriggers, null);
            summary.add(lblEvents, null);
            summary.add(lblIPs, null);
            summary.add(lblInEC, null);
            summary.add(getTxtInEventCount(), null);
            summary.add(lblOutEC, null);
            summary.add(getTxtOutEventCount(), null);
            summary.add(lblClusterPer, null);

            EpzillaTable et = new EpzillaTable();
            et.setBounds(new Rectangle(713, 340, 300, 170));
            summary.add(et);

        }
        return summary;
    }

    private JPanel getDispStatusTab() {
        if (dispStatus == null) {
            JLabel lblRecTriggers = new JLabel();
            lblRecTriggers.setBounds(new Rectangle(10, 350, 124, 25));
            lblRecTriggers.setText("Recovered Triggers:");
            JLabel lblDiscoveryStatus = new JLabel();
            lblDiscoveryStatus.setBounds(new Rectangle(10, 10, 200, 25));
            lblDiscoveryStatus.setText("Dispatcher Discovery Status :");
            JLabel lblDispIps = new JLabel();
            lblDispIps.setBounds(new Rectangle(320, 350, 124, 25));
            lblDispIps.setText("Dispatcher IP set:");
            JLabel lblMemory = new JLabel();
            lblMemory.setBounds(700, 355, 160, 25);
            lblMemory.setText("Event Dispatch Rate (Evt/sec):");
            JLabel lblCPU = new JLabel();
            lblCPU.setBounds(new Rectangle(700, 10, 125, 25));
            lblCPU.setText("CPU Usage:");
            dispStatus = new JPanel() {
                public void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g;
                    int w = getWidth();
                    int h = getHeight();
                    GradientPaint gp = new GradientPaint(0, 100, Color.white, 0, h, Color.gray);
                    g2d.setPaint(gp);
                    g2d.fillRect(0, 0, w, h);
                }
            };
            dispStatus.setLayout(null);
            dispStatus.setOpaque(false);
            dispStatus.add(lblDiscoveryStatus, null);
            dispStatus.add(getDiscoveryStaPane(), null);
            dispStatus.add(getRecTriggersPane(), null);
            dispStatus.add(lblRecTriggers, null);
            dispStatus.add(lblDispIps, null);
            dispStatus.add(getDispIpScrollPane(), null);
            dispStatus.add(lblMemory, null);
            dispStatus.add(lblCPU, null);

            MemoryTable mt = new MemoryTable();
            mt.setBounds(new Rectangle(700, 381, 300, 250));
            dispStatus.add(mt);

            CpuAnalyzer ca = new CpuAnalyzer();
            ca.setBounds(new Rectangle(700, 35, 300, 250));
            dispStatus.add(ca);
        }
        return dispStatus;
    }

    /**
     * This method initializes the configuration panel
     *
     * @return javax.swing.JPanel
     */
    private JPanel getConfigurations() {
        if (configPanel == null) {
            //EpZIlla IP range
            JLabel lblCl = new JLabel("Cluster Configurations:");
            lblCl.setBounds(new Rectangle(10, 8, 150, 20));
            lblCl.setFont(new Font("Dialog", Font.BOLD, 12));

            JLabel lbl1 = new JLabel("Dispatcher IP's :");
            lbl1.setBounds(new Rectangle(15, 65, 100, 20));

            JLabel lbl3 = new JLabel("Cluster ID:");
            lbl3.setBounds(new Rectangle(15, 35, 100, 20));

            //text fields
            txtIp.setBounds(new Rectangle(150, 65, 200, 80));
            txtIp.setBackground(Color.BLACK);
            txtIp.setForeground(Color.GREEN);
            txtIp.setOpaque(true);
            JScrollPane jspIP = new JScrollPane(txtIp);
            jspIP.setBounds(new Rectangle(150, 65, 200, 80));
            jspIP.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            jspIP.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            jspIP.setOpaque(false);

            txtClusterID.setBounds(new Rectangle(150, 35, 200, 20));
            txtClusterID.setBackground(Color.BLACK);
            txtClusterID.setForeground(Color.GREEN);

            //button configurations
            btnSaveConfig = new JButton("Save");
            btnSaveConfig.setBounds(new Rectangle(160, 150, 80, 25));
            btnSaveConfig.addActionListener(this);
            btnClearConfig = new JButton("Clear");
            btnClearConfig.setBounds(new Rectangle(260, 150, 80, 25));
            btnClearConfig.addActionListener(this);


            configPanel = new JPanel();
            configPanel.setLayout(null);
            configPanel.add(lblCl, null);
            configPanel.add(lbl1, null);
            configPanel.add(lbl3, null);
            configPanel.add(txtClusterID);
            configPanel.add(jspIP, null);
            configPanel.add(btnSaveConfig, null);
            configPanel.add(btnClearConfig, null);
            configPanel.setBounds(new Rectangle(0, 380, 400, 300));
            configPanel.setOpaque(false);
        }
        return configPanel;
    }

    private JMenuBar getmyMenuBar() {
        if (menuBar == null) {
            menuBar = new JMenuBar();
            menuBar.setBackground(SystemColor.control);
            JMenu file = new JMenu("File");
            JMenu helpmenu = new JMenu("Help");
            file.add(getAdminSettingMI());
            file.add(getCloseMI());
            file.add(getExitMI());
            helpmenu.add(getAboutMI());
            menuBar.add(file);
            menuBar.add(helpmenu);
        }
        return menuBar;
    }

    private JMenuItem getAboutMI() {
        if (about == null) {
            about = new JMenuItem();
            about.setText("About");
            about.addActionListener(this);
        }
        return about;
    }

    private JMenuItem getAdminSettingMI() {
        if (adminSettings == null) {
            ImageIcon settingsIcon = new ImageIcon("images//settings.jpg");
            adminSettings = new JMenuItem(settingsIcon);
            adminSettings.setText("Administrator Settings");
            adminSettings.addActionListener(this);
        }
        return adminSettings;
    }

    private JMenuItem getExitMI() {
        if (exit == null) {
            exit = new JMenuItem();
            exit.setText("Exit");
            exit.addActionListener(this);
        }
        return exit;
    }

    private JMenuItem getCloseMI() {
        if (closetabs == null) {
            ImageIcon closeIcon = new ImageIcon("images//close.jpg");
            closetabs = new JMenuItem(closeIcon);
            closetabs.setText("Close Tabs");
            closetabs.addActionListener(this);
        }
        return closetabs;
    }

    private JTextField getTxtNSIp() {
        if (txtIP == null) {
            txtIP = new JTextField();
            txtIP.setLocation(new Point(150, 50));
            txtIP.setSize(new Dimension(200, 20));
            txtIP.setBackground(Color.black);
            txtIP.setForeground(Color.green);
            txtIP.setEditable(false);
        }
        return txtIP;
    }

    private JTextField getTxtPort() {
        if (txtPort == null) {
            txtPort = new JTextField();
            txtPort.setLocation(new Point(150, 135));
            txtPort.setSize(new Dimension(200, 20));
            txtPort.setBackground(Color.black);
            txtPort.setForeground(Color.green);
            txtPort.setEditable(false);
        }
        return txtPort;
    }

    private JTextField getTxtNameServer() {
        if (txtNameServer == null) {
            txtNameServer = new JTextField();
            txtNameServer.setLocation(new Point(150, 95));
            txtNameServer.setSize(new Dimension(200, 20));
            txtNameServer.setBackground(Color.black);
            txtNameServer.setForeground(Color.green);
            txtNameServer.setEditable(false);
        }
        return txtNameServer;
    }

    private JButton getBtnRegister() {
        if (btnRegister == null) {
            ImageIcon registerIcon = new ImageIcon("images//register.jpg");
            btnRegister = new JButton(registerIcon);
            btnRegister.setLocation(new Point(150, 250));
            btnRegister.setText("Register");
            btnRegister.setSize(new Dimension(97, 20));
            btnRegister.addActionListener(this);
        }
        return btnRegister;
    }

    private JButton getBtnLoadSetings() {
        if (btnLoadSettings == null) {
            ImageIcon reloadIcon = new ImageIcon("images//reload.jpg");
            btnLoadSettings = new JButton(reloadIcon);
            btnLoadSettings.setLocation(255, 250);
            btnLoadSettings.setText("Reload");
            btnLoadSettings.setSize(new Dimension(97, 20));
            btnLoadSettings.addActionListener(this);
        }
        return btnLoadSettings;
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
            txtTriggers.setBounds(new Rectangle(15, 337, 588, 173));
            txtTriggers.setForeground(Color.green);
            txtTriggers.setEditable(false);
            txtTriggers.setBackground(Color.black);
        }
        return txtTriggers;
    }

    private JTextField getTbDispSerName() {
        if (txtDispSerName == null) {
            txtDispSerName = new JTextField();
            txtDispSerName.setSize(new Dimension(200, 20));
            txtDispSerName.setText("DISPATCHER_SERVICE");
            txtDispSerName.setLocation(new Point(150, 202));
            txtDispSerName.setBackground(Color.black);
            txtDispSerName.setForeground(Color.green);
            txtDispSerName.setEditable(false);
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
            txtStatus.setForeground(Color.green);
            txtStatus.setEditable(false);
            txtStatus.setBackground(Color.black);
        }
        return txtStatus;
    }

    private JScrollPane getIpScrollPane() {
        if (ipScrollPane == null) {
            ipScrollPane = new JScrollPane();
            ipScrollPane.setBounds(new Rectangle(713, 44, 300, 175));
            ipScrollPane.setViewportView(getTxtIPSet());
        }
        return ipScrollPane;
    }

    public JTextArea getTxtIPSet() {
        if (txtIPs == null) {
            txtIPs = new JTextArea();
            txtIPs.setBounds(new Rectangle(300, 44, 280, 175));
            txtIPs.setForeground(Color.green);
            txtIPs.setEditable(false);
            txtIPs.setBackground(Color.black);
        }
        return txtIPs;
    }

    public JTextField getTxtInEventCount() {
        if (txtInEventCount == null) {
            txtInEventCount = new JTextField();
            txtInEventCount.setBounds(new Rectangle(15, 581, 154, 30));
            txtInEventCount.setBackground(Color.black);
            txtInEventCount.setEditable(false);
            txtInEventCount.setForeground(Color.green);
        }
        return txtInEventCount;
    }

    public JTextField getTxtOutEventCount() {
        if (txtOutEventCount == null) {
            txtOutEventCount = new JTextField();
            txtOutEventCount.setBounds(new Rectangle(220, 581, 154, 30));
            txtOutEventCount.setForeground(Color.green);
            txtOutEventCount.setEditable(false);
            txtOutEventCount.setBackground(Color.black);
        }
        return txtOutEventCount;
    }

    public JTextField getEventDispatchRate() {
        if (txtEDRate == null) {
            txtEDRate = new JTextField();
            txtEDRate.setBounds(new Rectangle(420, 581, 154, 30));
            txtEDRate.setForeground(Color.GREEN);
            txtEDRate.setBackground(Color.BLACK);
            txtEDRate.setEditable(false);
        }
        return txtEDRate;
    }

    private JScrollPane getResultPane() {
        if (resultScrollPane == null) {
            resultScrollPane = new JScrollPane();
            resultScrollPane.setBounds(new Rectangle(400, 45, 600, 250));
            resultScrollPane.setViewportView(getTxtResult());
        }
        return resultScrollPane;
    }

    public JTextArea getTxtResult() {
        if (txtResult == null) {
            txtResult = new JTextArea();
            txtResult.setBounds(new Rectangle(400, 10, 600, 250));
            txtResult.setBackground(Color.black);
            txtResult.setForeground(Color.green);
            txtResult.setEditable(false);
        }
        return txtResult;
    }

    public JTextArea getTxtClusterPerformance() {
        if (txtClusterPerformance == null) {
            txtClusterPerformance = new JTextArea();
            txtClusterPerformance.setBounds(new Rectangle(713, 340, 281, 170));
            txtClusterPerformance.setForeground(Color.GREEN);
            txtClusterPerformance.setBackground(Color.BLACK);
            txtClusterPerformance.setEditable(false);

        }
        return txtClusterPerformance;
    }

    public JTextArea getTxtRecoveredList() {
        if (txtRecoveredList == null) {
            txtRecoveredList = new JTextArea();
            txtRecoveredList.setBounds(new Rectangle(10, 335, 280, 250));
            txtRecoveredList.setForeground(Color.green);
            txtRecoveredList.setBackground(Color.black);
            txtRecoveredList.setEditable(false);
        }
        return txtRecoveredList;
    }

    private JScrollPane getRecTriggersPane() {
        if (recTriggerList == null) {
            recTriggerList = new JScrollPane();
            recTriggerList.setBounds(new Rectangle(10, 375, 300, 250));
            recTriggerList.setViewportView(getTxtRecoveredList());

        }
        return recTriggerList;
    }

    public JTextArea getTxtDiscoveryStatus() {
        if (txtDiscoveryStatus == null) {
            txtDiscoveryStatus = new JTextArea();
            txtDiscoveryStatus.setBounds(new Rectangle(10, 35, 610, 250));
            txtDiscoveryStatus.setForeground(Color.green);
            txtDiscoveryStatus.setBackground(Color.black);
            txtDiscoveryStatus.setEditable(false);
            txtDiscoveryStatus.setWrapStyleWord(true);
            txtDiscoveryStatus.setLineWrap(true);
        }
        return txtDiscoveryStatus;
    }

    private JScrollPane getDiscoveryStaPane() {
        if (discoveryStatusPane == null) {
            discoveryStatusPane = new JScrollPane();
            discoveryStatusPane.setBounds(new Rectangle(10, 35, 610, 250));
            discoveryStatusPane.setViewportView(getTxtDiscoveryStatus());
        }
        return discoveryStatusPane;
    }

    private JScrollPane getDispIpScrollPane() {
        if (dispIps == null) {
            dispIps = new JScrollPane();
            dispIps.setBounds(new Rectangle(320, 375, 300, 250));
            dispIps.setViewportView(getDispIPSet());
        }
        return dispIps;
    }

    public JTextArea getDispIPSet() {
        if (txtDispIps == null) {
            txtDispIps = new JTextArea();
            txtDispIps.setBounds(new Rectangle(320, 375, 300, 250));
            txtDispIps.setForeground(Color.green);
            txtDispIps.setEditable(false);
            txtDispIps.setBackground(Color.black);
        }
        return txtDispIps;
    }

    //     txtRecoveredList.setBounds(new Rectangle(703, 35, 295, 500));

    private JCheckBox getChkLogs() {
        if (chkLogs == null) {
            chkLogs = new JCheckBox();
            chkLogs.setText("Replay Logs");
            chkLogs.setLocation(new Point(150, 290));
            chkLogs.setSize(new Dimension(102, 21));
            chkLogs.addActionListener(this);
            chkLogs.setEnabled(true);
            chkLogs.setBackground(new Color(230, 230, 225));
            chkLogs.setBorderPainted(false);
        }
        return chkLogs;
    }

    private JButton getBtnReplayLogs() {
        if (btnReplayLogs == null) {
            ImageIcon logsIcon = new ImageIcon("images//logs.jpg");
            btnReplayLogs = new JButton(logsIcon);
            btnReplayLogs.setBounds(new Rectangle(255, 290, 97, 20));
            btnReplayLogs.setText("Replay");
            btnReplayLogs.setEnabled(false);
            btnReplayLogs.addActionListener(this);
        }
        return btnReplayLogs;
    }

    public void register() throws MalformedURLException, RemoteException, UnknownHostException, NotBoundException {
        String ip = txtIP.getText();
        String nameService = txtNameServer.getText();
        String dispatcherName = txtDispSerName.getText();
        String port = txtPort.getText();
        if (!isValidIp(ip)) {
            JOptionPane.showMessageDialog(null, "Enter valid IP Address of NameServer.", "Epzilla", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!isValidPort(port)) {
            JOptionPane.showMessageDialog(null, "Enter valid Port number", "Message", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (nameService.length() != 0 && dispatcherName.length() != 0) {
            DispatcherRegister.register(ip, nameService, port, dispatcherName);
            isRegister = true;
//            btnRegister.setEnabled(false);
            btnLoadSettings.setEnabled(false);

        } else {
            JOptionPane.showMessageDialog(null, "Dispatcher registration fails. Enter setting details correctly.", "Epzilla", JOptionPane.ERROR_MESSAGE);
        }
    }

    /*
    This method check the port is in a valid range
    */

    private boolean isValidPort(String port) {
        boolean returnValue = true;
        if (port.length() != 0) {
            try {
                int num = Integer.parseInt(port);
                if (num < 1) {
                    returnValue = false;
                } else if (num > 65000) {
                    returnValue = false;
                }
            } catch (NumberFormatException e) {
                returnValue = false;
            }
        } else
            returnValue = false;

        return returnValue;

    }

    /**
     * This method check the IP is valid, use regular expression to validate
     *
     * @param ip
     * @return
     */
    private static boolean isValidIp(final String ip) {
        boolean format = ip.matches("^[\\d]{1,3}\\.[\\d]{1,3}\\.[\\d]{1,3}\\.[\\d]{1,3}$");
        if (format) {
            boolean validIp = true;
            String[] values = ip.split("\\.");
            for (String value : values) {
                short v = Short.valueOf(value);
                if ((v < 0) || (v > 255)) {
                    validIp = false;
                    break;
                }
            }
            return validIp;
        }
        return false;
    }

    public void showAbout() {
        About abut = new About();
        abut.setVisible(true);
    }

    /*
   load XML file to read the name server details
    */

    private void loadSettings() {
        try {
            ArrayList<String[]> data = ServerSettingsReader.getServerIPSettings("server_settings.xml");
            String[] ar = data.get(0);
            txtIP.setText(ar[0]);
            txtPort.setText(ar[1]);
            txtNameServer.setText(ar[2]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method call system exit
     */
    private void systemExit() {
        int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "Confirm Exit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (response == 0)
            System.exit(0);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        ArrayList<String> recArray;
        Object source = event.getSource();
        if (source == adminSettings) {
            tabbedPane.setVisible(true);
            tabbedPane.setFocusable(false);
        } else if (source == help) {

        } else if (source == exit) {
            systemExit();
        } else if (source == about) {
            showAbout();
        } else if (source == closetabs) {
            tabbedPane.setVisible(false);
        } else if (source == btnLoadSettings) {
            loadSettings();
        } else if (source == chkLogs) {
            if (chkLogs.isSelected()) {
                btnReplayLogs.setEnabled(true);
            } else if (!chkLogs.isSelected()) {
                btnReplayLogs.setEnabled(false);
            }
        } else if (source == btnReplayLogs) {
            try {
                recArray = ReadLog.readLog();
                RecoveredTriggers.getRecTriggerList(recArray);
                JOptionPane.showMessageDialog(null, "Trigger List successfully recovered", "Epzilla", JOptionPane.INFORMATION_MESSAGE);
                btnReplayLogs.setEnabled(false);
                chkLogs.setSelected(false);
            }
            catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Recovery failed. Make sure settings details are correct", "Epzilla", JOptionPane.ERROR_MESSAGE);
                btnReplayLogs.setEnabled(false);
                chkLogs.setSelected(false);
            }
        } else if (source == btnRegister) {
            try {
                if (!isRegister) {
                    register();
                } else
                    JOptionPane.showMessageDialog(null, "Dispatcher already registered", "Epzilla", JOptionPane.INFORMATION_MESSAGE);
            } catch (MalformedURLException e) {
                Logger.error("Dispatcher registration error:", e);
            } catch (RemoteException e) {
                DispatcherUIController.appendResults("Name Service not working...");
            } catch (UnknownHostException e) {
                Logger.error("Dispatcher registration error:", e);
            } catch (NotBoundException e) {
                Logger.error("Dispatcher registration error:", e);
            }

        } else if (source == btnSaveConfig) {
            String[] nodes = txtIp.getText().split("\\n");
            ConfigurationManager cf = new ConfigurationManager(nodes, txtClusterID.getText());
            //logic write config data
            if (cf.writeInfo())
                JOptionPane.showMessageDialog(null, "Configurations details successfully saved", "Epzilla", JOptionPane.INFORMATION_MESSAGE);
            else
                JOptionPane.showMessageDialog(null, "Error in Configurations. Make sure the IP values are in a valid range", "Epzilla", JOptionPane.ERROR_MESSAGE);

        } else if (source == btnClearConfig) {
            txtClusterID.setText("");
            txtIp.setText("");
        }

    }

}