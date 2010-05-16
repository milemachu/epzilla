package org.epzilla.client.userInterface;


import org.epzilla.client.controlers.ClientHandler;
import org.epzilla.client.controlers.ClientInit;
import org.epzilla.client.xml.ServerSettingsReader;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Vector;


public class ClientUI extends JFrame implements ActionListener, ListSelectionListener {

    private JTabbedPane tabbedPane = null;
    private JTextField txtIP = null;
    private JTextField txtPort = null;
    private JTextField txtName = null;
    private JLabel labelIP = null;
    private JLabel labelPort = null;
    private JLabel labelName = null;
    private Vector<String> ips = new Vector<String>();
    private JMenuBar menuBar = null;
    private JMenuItem about = null;
    private JMenuItem adminSettings = null;
    private JMenuItem closetabs = null;
    private JMenuItem exit = null;
    private JMenu file = new JMenu("File");
    private JMenu helpmenu = new JMenu("Help");
    private JButton btnSend = null;
    private JButton btnCancelSend = null;
    private JLabel lblDetails = null;
    private JList listLookup = null;
    private JButton btnClear = null;
    private JButton btnLookup = null;
    private JLabel lblDispIP1 = null;
    private JTextField txtDispIP = null;
    private JLabel lblSettings = null;
    private JScrollPane resultsScrollPane = null;
    public JTextArea txtResults = null;
    private JLabel lblDispatcherServiceName = null;
    private JTextField txtDispName = null;
    private static ClientHandler client;
    private static ClientInit clientTest;
    private boolean isRegister = false;
    private boolean isLookup = false;
    private static String clientID = "";
    private static String clientIP = "";
    private static ServerSettingsReader reader;
    private JLabel lblSummary = null;
    private JLabel lblCount = null;
    private JTextField txtNotiCount = null;

    public ClientUI() {
        super();
        client = new ClientHandler();
        clientTest = new ClientInit();
        reader = new ServerSettingsReader();
        initialize();
    }

    private void initialize() {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = screen.width;
        int y = screen.height;
        Image img = Toolkit.getDefaultToolkit().getImage("images\\logo.jpg");
        this.setIconImage(img);
        this.setSize(x, y);
        this.setPreferredSize(new Dimension(1024, 768));
        this.setContentPane(getMyTabbedPane());
        this.setJMenuBar(getmyMenuBar());
        this.setTitle("Epzilla DS");
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "Confirm Exit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.YES_OPTION) {
                    dispose();
                    System.exit(0);
                }
            }
        });

        loadSettings();
        btnClear.setEnabled(false);

    }

    private JTabbedPane getMyTabbedPane() {
        if (tabbedPane == null) {
            ImageIcon settingsIcon = new ImageIcon("images//settings.jpg");
            ImageIcon summaryIcon = new ImageIcon("images//summary.jpg");
            ImageIcon serviceIcon = new ImageIcon("images//service.jpg");

            lblCount = new JLabel();
            lblCount.setBounds(new Rectangle(705, 28, 120, 25));
            lblCount.setText("Notifications count:");
            lblSummary = new JLabel();
            lblSummary.setBounds(new Rectangle(26, 5, 69, 24));
            lblSummary.setText("Summary:");
            lblDispatcherServiceName = new JLabel();
            lblDispatcherServiceName.setText("Service Name :");
            lblDispatcherServiceName.setLocation(new Point(15, 178));
            lblDispatcherServiceName.setSize(new Dimension(123, 16));
            lblSettings = new JLabel();
            lblSettings.setText("Server Settings:");
            lblSettings.setLocation(new Point(15, 15));
            lblSettings.setFont(new Font("Dialog", Font.BOLD, 12));
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
            labelName.setLocation(new Point(30, 92));
            labelPort = new JLabel();
            labelPort.setText("Port :");
            labelPort.setBounds(new Rectangle(30, 135, 41, 25));
            labelIP = new JLabel();
            labelIP.setText("IP Address :");
            labelIP.setSize(new Dimension(72, 25));
            labelIP.setLocation(new Point(30, 49));

            tabbedPane = new JTabbedPane();

            JPanel mainSettings = new JPanel();
            mainSettings.setLayout(null);
            mainSettings.add(getIpTextField(), null);
            mainSettings.add(getTbPort(), null);
            mainSettings.add(getTbName(), null);
            mainSettings.add(labelIP, null);
            mainSettings.add(labelPort, null);
            mainSettings.add(labelName, null);
            mainSettings.add(lblSettings, null);

            JPanel upload = new JPanel();
            upload.setLayout(null);
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
            results.add(getResultsScrollPane(), null);
            results.add(lblSummary, null);
            results.add(lblCount, null);
            results.add(getTxtNotiCount(), null);

            tabbedPane.addTab("Service", serviceIcon, upload);
            tabbedPane.addTab("Summary", summaryIcon, results);
            tabbedPane.addTab("Settings", settingsIcon, mainSettings);

            tabbedPane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
            tabbedPane.setVisible(true);
        }
        return tabbedPane;
    }

    private JMenuBar getmyMenuBar() {
        if (menuBar == null) {
            menuBar = new JMenuBar();
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

    private JTextField getIpTextField() {
        if (txtIP == null) {
            txtIP = new JTextField();
            txtIP.setLocation(new Point(110, 52));
            txtIP.setSize(new Dimension(200, 20));
            txtIP.setBackground(Color.black);
            txtIP.setForeground(Color.GREEN);
            txtIP.setEditable(false);
        }
        return txtIP;
    }

    private JTextField getTbPort() {
        if (txtPort == null) {
            txtPort = new JTextField();
            txtPort.setLocation(new Point(110, 140));
            txtPort.setSize(new Dimension(204, 20));
            txtPort.setBackground(Color.BLACK);
            txtPort.setForeground(Color.GREEN);
            txtPort.setEditable(false);
        }
        return txtPort;
    }

    private JTextField getTbName() {
        if (txtName == null) {
            txtName = new JTextField();
            txtName.setLocation(new Point(110, 96));
            txtName.setSize(new Dimension(200, 20));
            txtName.setBackground(Color.BLACK);
            txtName.setForeground(Color.GREEN);
            txtName.setEditable(false);
        }
        return txtName;
    }

    public JTextField getTxtNotiCount() {
        if (txtNotiCount == null) {
            txtNotiCount = new JTextField();
            txtNotiCount.setBounds(new Rectangle(820, 30, 122, 20));
            txtNotiCount.setForeground(Color.green);
            txtNotiCount.setEditable(false);
            txtNotiCount.setBackground(Color.black);
        }
        return txtNotiCount;
    }

    private JButton getBtnSend() {
        if (btnSend == null) {
            ImageIcon startIcon = new ImageIcon("images//start.jpg");
            btnSend = new JButton(startIcon);
            btnSend.setText("Start");
            btnSend.setBounds(new Rectangle(216, 220, 90, 20));
            btnSend.addActionListener(this);
        }
        return btnSend;
    }

    private JButton getBtnCancelSend() {
        if (btnCancelSend == null) {
            ImageIcon cancelIcon = new ImageIcon("images//cancel.jpg");
            btnCancelSend = new JButton(cancelIcon);
            btnCancelSend.setText("Cancel");
            btnCancelSend.setBounds(new Rectangle(331, 220, 90, 20));
            btnCancelSend.setVisible(true);
            btnCancelSend.setEnabled(false);
            btnCancelSend.addActionListener(this);
        }
        return btnCancelSend;
    }

    private JList getListLookup() {
        if (listLookup == null) {
            listLookup = new JList(ips);
            listLookup.setSize(new Dimension(325, 72));
            listLookup.setLocation(new Point(165, 46));
            listLookup.addListSelectionListener(this);
        }
        return listLookup;
    }

    private JButton getBtnClear() {
        if (btnClear == null) {
            ImageIcon clearIcon = new ImageIcon("images//clear.jpg");
            btnClear = new JButton(clearIcon);
            btnClear.setBounds(new Rectangle(15, 92, 90, 20));
            btnClear.setText("Clear");
            btnClear.addActionListener(this);
        }
        return btnClear;
    }

    private JButton getBtnLookup() {
        if (btnLookup == null) {
            ImageIcon lookupIcon = new ImageIcon("images//lookup.jpg");
            btnLookup = new JButton(lookupIcon);
            btnLookup.setText("Lookup");
            btnLookup.setSize(new Dimension(90, 20));
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

    private JScrollPane getResultsScrollPane() {
        if (resultsScrollPane == null) {
            resultsScrollPane = new JScrollPane();
            resultsScrollPane.setBounds(new Rectangle(25, 30, 600, 600));
            resultsScrollPane.setViewportView(getTxtResults());
        }
        return resultsScrollPane;
    }

    public JTextArea getTxtResults() {
        if (txtResults == null) {
            txtResults = new JTextArea();
            txtResults.setBounds(new Rectangle(0, 0, 600, 600));
            txtResults.setEditable(false);
            txtResults.setForeground(Color.green);
            txtResults.setBackground(Color.black);
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

    private void getDispatchers() {
        String ip = txtIP.getText();
        String serverName = txtName.getText().toString();
        String port = txtPort.getText();
        String clientIp = getIpAddress();
        if ((isValidIp(ip) == true) && (serverName.length() != 0)) {
            try {
                ips = client.getServiceIp(ip, serverName, clientIp);
                listLookup.setListData(ips);
                if (!ips.isEmpty()) {
                    btnLookup.setEnabled(false);
                    btnClear.setEnabled(true);
                }
            } catch (MalformedURLException e) {
                JOptionPane.showMessageDialog(null, "NameService IP Address incorrect", "Message", JOptionPane.ERROR_MESSAGE);
            } catch (RemoteException e) {
                JOptionPane.showMessageDialog(null, "Remote Server not working", "Message", JOptionPane.ERROR_MESSAGE);
            } catch (NotBoundException e) {
                JOptionPane.showMessageDialog(null, "Invalid NameService name", "Message", JOptionPane.ERROR_MESSAGE);
            }

        } else
            JOptionPane.showMessageDialog(null, "Make sure setting details correct.", "Message", JOptionPane.ERROR_MESSAGE);
    }

    public void setResults(String str) {
        txtResults.setText(str);
    }

    public void cancelSend() {
        int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to cancel?", "Confirm Cancel", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (response == JOptionPane.YES_OPTION) {
            btnCancelSend.setEnabled(false);
            ClientInit.stopEventTriggerStream();
        }

    }

    private void setDispValues(String str) {
        StringTokenizer st = new StringTokenizer(str);
        String ip = st.nextToken();
        String servicename = st.nextToken();
        txtDispIP.setText(ip);
        txtDispName.setText(servicename);
        if (isRegister == false) {
            try {
                client.regForCallback(ip, servicename);
                ClientHandler.registerClient(clientIP, clientID);
                isRegister = true;
            } catch (RemoteException e) {
//                JOptionPane.showMessageDialog(null, e, "Message", JOptionPane.ERROR_MESSAGE);
            } catch (MalformedURLException e) {
//                JOptionPane.showMessageDialog(null, e, "Message", JOptionPane.ERROR_MESSAGE);
            } catch (NotBoundException e) {
//                JOptionPane.showMessageDialog(null, e, "Message", JOptionPane.ERROR_MESSAGE);
            } catch (UnknownHostException e) {
//                JOptionPane.showMessageDialog(null, e, "Message", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void unregisterCallbackLocal() {
        String ip = txtDispIP.getText();
        String servicename = txtDispName.getText().toString();
        try {
            client.unregisterCallback(ip, servicename);
            ClientHandler.unRegisterClient(clientIP, clientID);
        } catch (MalformedURLException e) {
//            JOptionPane.showMessageDialog(null, e, "epZilla", JOptionPane.ERROR_MESSAGE);
        } catch (RemoteException e) {
//            JOptionPane.showMessageDialog(null, e, "epZilla", JOptionPane.ERROR_MESSAGE);
        } catch (NotBoundException e) {
//            JOptionPane.showMessageDialog(null, e, "epZilla", JOptionPane.ERROR_MESSAGE);
        } catch (UnknownHostException e) {
        }
        btnLookup.setEnabled(true);
        btnClear.setEnabled(false);
        txtDispIP.setText("");
        txtDispName.setText("");
        ips.removeAllElements();
        listLookup.repaint();
        isRegister = false;

    }

    private void showAbout() {
        About abut = new About();
        abut.setVisible(true);
    }

    public static boolean isValidIp(String ip) {
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

    private void initProcess() {
        String dispIP = txtDispIP.getText();
        String dispName = txtDispName.getText().toString();
        String clientIp = getIpAddress();
        btnCancelSend.setEnabled(true);
        if ((dispIP.length() == 0) && (dispName.length() == 0)) {
            JOptionPane.showMessageDialog(null, "Perform Lookup operation and select service you want.", "Message", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if ((dispIP.length() != 0) && (dispName.length() != 0)) {
            try {
                ClientInit.initProcess(dispIP, dispName, clientID);
            } catch (MalformedURLException e) {
                JOptionPane.showMessageDialog(null, "Error in file send process.", "Message", JOptionPane.ERROR_MESSAGE);
            } catch (NotBoundException e) {
                JOptionPane.showMessageDialog(null, "Dispatcher failure.", "Message", JOptionPane.ERROR_MESSAGE);
            } catch (RemoteException e) {
            }
        } else
            JOptionPane.showMessageDialog(null, "Error in file send process.", "epZilla", JOptionPane.ERROR_MESSAGE);

    }

    private void loadSettings() {
        try {
            ArrayList<String[]> data = reader.getServerIPSettings("./src/settings/server_settings.xml");
            String[] ar = data.get(0);
            txtIP.setText(ar[0]);
            txtPort.setText(ar[1]);
            txtName.setText(ar[2]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void systemExit() {
        int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "Confirm Exit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (response == 0) {
            System.exit(0);
            unregisterCallbackLocal();
        }
    }

    private String getIpAddress() {
        InetAddress inetAddress = null;
        try {
            inetAddress = InetAddress.getLocalHost();
        } catch (java.net.UnknownHostException e) {
            e.printStackTrace();
        }
        clientIP = inetAddress.getHostAddress();
        return clientIP;
    }


    private void getClientID() {
        String clientIP = getIpAddress();
        try {
            clientID = ClientHandler.getClientsID(clientIP);
        } catch (RemoteException e) {
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent event) {
        Object source = event.getSource();
        if (source == listLookup) {
            int i = listLookup.getSelectedIndex();
            String s = i >= 0 ? ips.get(i) : "";
            if (s != null) {
                setDispValues(s);
            } else
                return;
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        if (source == btnSend) {
            initProcess();
        } else if (source == btnCancelSend) {
            cancelSend();
        } else if (source == btnClear) {
            unregisterCallbackLocal();
        } else if (source == btnLookup) {
            getDispatchers();
            getClientID();
        } else if (source == adminSettings) {
            tabbedPane.setVisible(true);
        } else if (source == closetabs) {
            tabbedPane.setVisible(false);
        } else if (source == exit) {
            systemExit();
        } else if (source == about) {
            showAbout();
        }
    }


}
