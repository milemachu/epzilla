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
package org.epzilla.client.userInterface;


import org.epzilla.client.controlers.ClientHandler;
import org.epzilla.client.controlers.ClientInit;
import org.epzilla.client.xml.ServerSettingsReader;
import org.epzilla.dispatcher.rmi.TriggerRepresentation;
import org.epzilla.dispatcher.ui.CustomGridLayout;
import org.epzilla.util.Logger;

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
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * This class initializes the CLient UI
 * Author: Chathura
 * Date:Feb 1, 2010
 * Time: 10:20:41 PM
 */

public class ClientUI extends JFrame implements ActionListener, ListSelectionListener {

    private JTabbedPane tabbedPane = null;
    private JTextField txtIP = null;
    private JTextField txtPort = null;
    private JTextField txtName = null;
    public Vector<String> ips = new Vector<String>();
    private JMenuBar menuBar = null;
    private JMenuItem about = null;
    private JMenuItem adminSettings = null;
    private JMenuItem closetabs = null;
    private JMenuItem exit = null;
    private JMenu file = new JMenu("File");
    private JMenu helpmenu = new JMenu("Help");
    private JButton btnSend = null;
    private JButton btnCancelSend = null;
    private JList listLookup = null;
    private JButton btnClear = null;
    private JButton btnLookup = null;
    public JTextField txtDispIP = null;
    private JScrollPane resultsScrollPane = null;
    private JScrollPane notificationSP = null;
    public JTextArea txtResults = null;
    public JTextField txtDispName = null;
    private static ClientHandler client;
    public boolean isRegister = false;
    private boolean isLookup = false;
    private static String clientID = "";
    private static String clientIP = "";
    private static ServerSettingsReader reader;
    private JTextField txtNotiCount = null;
    private JTextArea txtNotifications = null;
    JPanel simulatorPanel = null;
    private JPanel workBenchUpperPanel = null;
    JButton sendQueryBtn = new JButton();
    private JPanel getAllTriggers = null;
    private JTextArea txtAllTriggers = null;
    private JButton deleteBtn = null;
    private String dispIP = null;
    private String dispName = null;

    public ClientUI() {
        super();
        client = new ClientHandler();
        ClientInit clientTest = new ClientInit();
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
        this.setTitle("Epzilla Client");
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
        btnClear.setVisible(false);
        btnCancelSend.setVisible(false);

    }

    private JTabbedPane getMyTabbedPane() {
        if (tabbedPane == null) {
            ImageIcon settingsIcon = new ImageIcon("images//settings.jpg");
            ImageIcon summaryIcon = new ImageIcon("images//summary.jpg");
            ImageIcon serviceIcon = new ImageIcon("images//service.jpg");
            ImageIcon detailsIcon = new ImageIcon("images//clusterDe.jpg");

            tabbedPane = new JTabbedPane();
            tabbedPane.addTab("Service", serviceIcon, getUploadTab());
            tabbedPane.addTab("Summary", summaryIcon, getResultsTab());
            tabbedPane.addTab("Trigger Manager", detailsIcon, getTriggerInfoTab());
            tabbedPane.addTab("Settings", settingsIcon, getMainSettingsTab());

            tabbedPane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
            tabbedPane.setVisible(true);
        }
        return tabbedPane;
    }

    private JPanel getUploadTab() {
        JLabel lblDetails = new JLabel();
        lblDetails.setBounds(new Rectangle(15, 16, 259, 16));
        lblDetails.setText("Lookup available services in the System");
        JLabel lblDispIP1 = new JLabel();
        lblDispIP1.setBounds(new Rectangle(15, 148, 141, 16));
        lblDispIP1.setText("Dispatcher IP Selected :");
        JLabel lblDispatcherServiceName = new JLabel();
        lblDispatcherServiceName.setText("Service Name :");
        lblDispatcherServiceName.setLocation(new Point(15, 178));
        lblDispatcherServiceName.setSize(new Dimension(123, 16));

        JPanel upload = new JPanel() {
            public void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                int w = getWidth();
                int h = getHeight();
                GradientPaint gp = new GradientPaint(0, 100, Color.white, 0, h, Color.gray);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
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
        upload.add(getSimulatorPanel());
        upload.add(getWorkBenchPanel());
        return upload;
    }

    private JPanel getResultsTab() {
        JLabel lblSummary = new JLabel();
        lblSummary.setBounds(new Rectangle(26, 5, 69, 24));
        lblSummary.setText("Summary:");
        JLabel lblCount = new JLabel();
        lblCount.setBounds(new Rectangle(600, 18, 120, 25));
        lblCount.setText("Notifications count:");
        JLabel jLabel = new JLabel();
        jLabel.setBounds(new Rectangle(600, 63, 154, 22));
        jLabel.setText("Notification messages:");

        JPanel results = new JPanel() {
            public void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                int w = getWidth();
                int h = getHeight();
                GradientPaint gp = new GradientPaint(0, 100, Color.white, 0, h, Color.gray);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        results.setLayout(null);
        results.add(getResultsScrollPane(), null);
        results.add(lblSummary, null);
        results.add(lblCount, null);
        results.add(getTxtNotiCount(), null);
        results.add(getNotificationScrollpane(), null);
        results.add(jLabel, null);
        return results;
    }

    private JPanel getTriggerInfoTab() {
        JLabel lblTriggers = new JLabel();
        lblTriggers.setText("Triggers :");
        lblTriggers.setBounds(new Rectangle(26, 5, 69, 24));

        JPanel deleteTriggers = new JPanel() {
            public void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                int w = getWidth();
                int h = getHeight();
                GradientPaint gp = new GradientPaint(0, 100, Color.white, 0, h, Color.gray);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        deleteTriggers.setLayout(null);
        deleteTriggers.add(getAllTrigersScPane());
        deleteTriggers.add(lblTriggers);

        return deleteTriggers;
    }

    private JPanel getMainSettingsTab() {
        JLabel lblSettings = new JLabel();
        lblSettings.setText("Server Settings:");
        lblSettings.setLocation(new Point(15, 15));
        lblSettings.setFont(new Font("Dialog", Font.BOLD, 12));
        lblSettings.setSize(new Dimension(196, 25));
        JLabel labelName = new JLabel();
        labelName.setText("Name :");
        labelName.setSize(new Dimension(47, 25));
        labelName.setLocation(new Point(30, 92));
        JLabel labelPort = new JLabel();
        labelPort.setText("Port :");
        labelPort.setBounds(new Rectangle(30, 135, 41, 25));
        JLabel labelIP = new JLabel();
        labelIP.setText("IP Address :");
        labelIP.setSize(new Dimension(72, 25));
        labelIP.setLocation(new Point(30, 49));

        JPanel mainSettings = new JPanel() {
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
        mainSettings.add(getIpTextField(), null);
        mainSettings.add(getTbPort(), null);
        mainSettings.add(getTbName(), null);
        mainSettings.add(labelIP, null);
        mainSettings.add(labelPort, null);
        mainSettings.add(labelName, null);
        mainSettings.add(lblSettings, null);

        return mainSettings;
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
            txtNotiCount.setBounds(new Rectangle(707, 19, 122, 20));
            txtNotiCount.setForeground(Color.green);
            txtNotiCount.setEditable(false);
            txtNotiCount.setBackground(Color.black);
        }
        return txtNotiCount;
    }

    private JButton getBtnSend() {
        if (btnSend == null) {
            ImageIcon startIcon = new ImageIcon("images//reload.jpg");
            btnSend = new JButton(startIcon);
            btnSend.setText("Connect");
            btnSend.setBounds(new Rectangle(165, 220, 120, 20));
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

    public JList getListLookup() {
        if (listLookup == null) {
            listLookup = new JList(ips);
            listLookup.setSize(new Dimension(325, 72));
            listLookup.setLocation(new Point(165, 46));
            listLookup.addListSelectionListener(this);
            listLookup.setBackground(Color.BLACK);
            listLookup.setForeground(Color.GREEN);
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

    public JTextField getTxtDispIP1() {
        if (txtDispIP == null) {
            txtDispIP = new JTextField();
            txtDispIP.setPreferredSize(new Dimension(4, 20));
            txtDispIP.setLocation(new Point(165, 147));
            txtDispIP.setEditable(false);
            txtDispIP.setForeground(Color.GREEN);
            txtDispIP.setBackground(Color.BLACK);
            txtDispIP.setSize(new Dimension(325, 20));
        }
        return txtDispIP;
    }

    private JScrollPane getResultsScrollPane() {
        if (resultsScrollPane == null) {
            resultsScrollPane = new JScrollPane();
            resultsScrollPane.setBounds(new Rectangle(25, 30, 500, 600));
            resultsScrollPane.setViewportView(getTxtResults());
            resultsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        }
        return resultsScrollPane;
    }

    public JTextArea getTxtResults() {
        if (txtResults == null) {
            txtResults = new JTextArea();
            txtResults.setBounds(new Rectangle(0, 0, 500, 600));
            txtResults.setEditable(false);
            txtResults.setLineWrap(true);
            txtResults.setWrapStyleWord(true);
            txtResults.setForeground(Color.green);
            txtResults.setBackground(Color.black);
        }
        return txtResults;
    }

    public JTextArea getNotifications() {
        if (txtNotifications == null) {
            txtNotifications = new JTextArea();
            txtNotifications.setBounds(new Rectangle(600, 90, 400, 535));
            txtNotifications.setEditable(false);
            txtNotifications.setLineWrap(true);
            txtNotifications.setWrapStyleWord(true);
            txtNotifications.setForeground(Color.GREEN);
            txtNotifications.setBackground(Color.BLACK);
        }
        return txtNotifications;
    }

    private JScrollPane getNotificationScrollpane() {
        if (notificationSP == null) {
            notificationSP = new JScrollPane();
            notificationSP.setBounds(new Rectangle(600, 90, 400, 535));
            notificationSP.setViewportView(getNotifications());
            notificationSP.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        }
        return notificationSP;
    }

    /*
   Scroll pane dispaly data in Trigger Manager Tab
    */

    private JPanel getAllTrigersScPane() {
        if (getAllTriggers == null) {

            getAllTriggers = new JPanel(new CustomGridLayout(new String[]{"100%"}, new String[]{"100%", "10", "35", "10"}));
            getAllTriggers.setBounds(new Rectangle(25, 30, 600, 500));

            JScrollPane jsp = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            jsp.setViewportView(txtGetAllTriggers());
            getAllTriggers.add(jsp);
            JLabel jl = new JLabel();
            jl.setOpaque(false);
            getAllTriggers.add(jl);

            JPanel oppanel = new JPanel(new CustomGridLayout(new String[]{"10", "80", "100%", "100", "15", "80", "10"}, new String[]{"100%"}));

            jl = new JLabel();
            jl.setOpaque(false);
            oppanel.add(jl);

            btnRefreshTriggers = new JButton("Refresh");
            //logic to get triggers
            btnRefreshTriggers.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    String dispIP = txtDispIP.getText();
                    String dispName = txtDispName.getText();

                    if ((dispIP.length() == 0) && (dispName.length() == 0)) {
                        JOptionPane.showMessageDialog(null, "Perform Lookup operation and select service you want.", "Epzilla", JOptionPane.ERROR_MESSAGE);
                    }
                    if ((dispIP.length() != 0) && (dispName.length() != 0)) {
                        try {
                            triggerList = ClientInit.getAllTriggersFromDispatcher(clientID);
                            Collections.sort(triggerList, new Comparator<TriggerRepresentation>() {

                                @Override
                                public int compare(TriggerRepresentation o1, TriggerRepresentation o2) {

                                    try {
                                        long x = Long.parseLong(o1.getTriggerId());
                                        long y = Long.parseLong(o2.getTriggerId());
                                        if (x < y) {
                                            return -1;
                                        } else if (x == y) {
                                            return 0;
                                        } else {
                                            return 1;
                                        }
                                    } catch (NumberFormatException e1) {
                                        Logger.error("Trigger sorting error:", e1);

                                    }
                                    return 0;
                                }
                            });
                            txtAllTriggers.setText("");
                            if (triggerList != null) {
                                for (TriggerRepresentation tr : triggerList) {
                                    txtAllTriggers.append(tr.getTriggerId());
                                    txtAllTriggers.append(":\n");
                                    txtAllTriggers.append(tr.getTrigger());
                                    txtAllTriggers.append("\n");

                                }
                            }

                        } catch (Exception ex) {
                            Logger.error("Trigger receive error:", ex);
                        }
                    }
                }
            });
            oppanel.add(btnRefreshTriggers);

            jl = new JLabel();
            jl.setOpaque(false);
            oppanel.add(jl);

            deleteField = new JTextField();
            oppanel.add(deleteField);
            oppanel.setOpaque(false);
            jl = new JLabel();
            jl.setOpaque(false);
            oppanel.add(jl);

            btnDeleteTrigger = new JButton("Delete");
            //logic to delete triggers
            btnDeleteTrigger.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String dispIP = txtDispIP.getText();
                    String dispName = txtDispName.getText();
                    if ((dispIP.length() == 0) && (dispName.length() == 0)) {
                        JOptionPane.showMessageDialog(null, "Perform Lookup operation and select service you want.", "Epzilla", JOptionPane.ERROR_MESSAGE);
                    }
                    try {
                        String in = ClientUI.this.deleteField.getText();
                        String[] ids = in.trim().split(",");
                        ArrayList<TriggerRepresentation> toDelete = new ArrayList();
                        for (String id : ids) {
                            if ((id = id.trim()).length() > 0) {
                                TriggerRepresentation tr = new TriggerRepresentation();
                                tr.setTriggerId(id);
                                tr.setClientId(clientID);
                                toDelete.add(tr);
                                if (triggerList != null) {
                                    try {
                                        for (TriggerRepresentation t : triggerList) {
                                            if (t.getTriggerId().equals(id)) {
                                                tr.setTrigger(t.getTrigger());
                                                break;
                                            }
                                        }
                                    } catch (Exception e1) {
                                        Logger.error("Trigger accepting error :", e1);
                                    }
                                }
                            }
                        }
                        if (toDelete.size() > 0) {
                            ClientInit.deleteTriggers(clientID, toDelete);
                        }
                    } catch (Exception ex) {
                        Logger.error("Trigger deletion error:", ex);
                    }
                }
            });
            oppanel.add(btnDeleteTrigger);


            jl = new JLabel();
            jl.setOpaque(false);
            oppanel.add(jl);

            getAllTriggers.add(oppanel);

            jl = new JLabel();
            jl.setOpaque(false);
            getAllTriggers.add(jl);
            getAllTriggers.setOpaque(false);
        }
        return getAllTriggers;
    }

    ArrayList<TriggerRepresentation> triggerList = null;

    JButton btnRefreshTriggers;
    JButton btnDeleteTrigger;
    JTextField deleteField;

    public JTextArea txtGetAllTriggers() {
        if (txtAllTriggers == null) {
            txtAllTriggers = new JTextArea();
            txtAllTriggers.setBounds(new Rectangle(25, 30, 400, 500));
            txtAllTriggers.setEditable(false);
            txtAllTriggers.setLineWrap(true);
            txtAllTriggers.setWrapStyleWord(true);
            txtAllTriggers.setForeground(Color.GREEN);
            txtAllTriggers.setBackground(Color.BLACK);
            txtAllTriggers.setOpaque(true);
        }
        return txtAllTriggers;
    }

    private JTextField getTxtDispName() {
        if (txtDispName == null) {
            txtDispName = new JTextField();
            txtDispName.setLocation(new Point(165, 181));
            txtDispName.setEditable(false);
            txtDispName.setForeground(Color.GREEN);
            txtDispName.setBackground(Color.BLACK);
            txtDispName.setSize(new Dimension(325, 20));
        }
        return txtDispName;
    }

    /*
   Panel shoow the simulation controlers of the user interface
    */

    private JPanel getSimulatorPanel() {
        if (simulatorPanel == null) {
            simulatorPanel = new JPanel(new CustomGridLayout(new String[]{"40%", "10", "30%", "20", "30%"}, new String[]{"25", "50%", "10", "50%"}));
        }
        dispIP = txtDispIP.getText();
        dispName = txtDispName.getText();

        ImageIcon cancelIcon = new ImageIcon("images//cancel.jpg");
        ImageIcon startIcon = new ImageIcon("images//start.jpg");

        simulatorPanel.add(new JLabel("Simulations:"));
        simulatorPanel.add(new JLabel());
        simulatorPanel.add(new JLabel());
        simulatorPanel.add(new JLabel());
        simulatorPanel.add(new JLabel());


        simulatorPanel.add(new JLabel("Triggers:"));
        simulatorPanel.add(new JLabel());

        JButton jb1 = new JButton(startIcon);
        jb1.setText("Start");
        jb1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // code to start trigger generation
                if ((dispIP.length() == 0) && (dispName.length() == 0)) {
                    JOptionPane.showMessageDialog(null, "Perform Lookup operation and select service you want.", "Epzilla", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                ClientInit.initTrigers();
            }
        });
        simulatorPanel.add(jb1);
        simulatorPanel.add(new JLabel());

        JButton jb2 = new JButton(cancelIcon);
        jb2.setText("Stop");
        jb2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // code to stop trigger generation
                ClientInit.stopTriggerStream();
            }
        });
        simulatorPanel.add(jb2);

        simulatorPanel.add(new JLabel());
        simulatorPanel.add(new JLabel());
        simulatorPanel.add(new JLabel());
        simulatorPanel.add(new JLabel());
        simulatorPanel.add(new JLabel());


        simulatorPanel.add(new JLabel("Events:"));
        simulatorPanel.add(new JLabel());


        JButton jb3 = new JButton(startIcon);
        jb3.setText("Start");
        jb3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //event generation
                if ((dispIP.length() == 0) && (dispName.length() == 0)) {
                    JOptionPane.showMessageDialog(null, "Perform Lookup operation and select service you want.", "Epzilla", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                ClientInit.initEvents();
            }
        });

        simulatorPanel.add(jb3);
        simulatorPanel.add(new JLabel());


        JButton jb4 = new JButton(cancelIcon);
        jb4.setText("Stop");
        jb4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // stop event generation
                ClientInit.stopEventStream();
            }
        });
        simulatorPanel.add(jb4);

        simulatorPanel.setLocation(new Point(15, 271));
        simulatorPanel.setSize(new Dimension(400, 80));
        simulatorPanel.setOpaque(false);

        return simulatorPanel;
    }

    private JTextArea txtQuery;
    private JPanel workBenchPanel;
    private JButton clearQueryBtn = null;

    /*
    work bench panel
     */

    private JPanel getWorkBenchPanel() {
        if (workBenchPanel == null) {
            workBenchPanel = new JPanel();
        }
        workBenchUpperPanel = new JPanel();
        workBenchPanel.setLayout(new CustomGridLayout(new String[]{"100%"}, new String[]{"100%", "10", "22"}));
        workBenchPanel.setOpaque(false);
        workBenchUpperPanel.setLayout(new CustomGridLayout(new String[]{"10", "150", "100%"}, new String[]{"100%"}));
        JPanel lowerPanel = new JPanel(new CustomGridLayout(new String[]{"50%", "25%", "10", "25%"}, new String[]{"100%"}));
        lowerPanel.setOpaque(false);

        JPanel labelPanel = new JPanel(new CustomGridLayout(new String[]{"100%"}, new String[]{"30", "100%"}));
        labelPanel.setOpaque(false);

        JLabel lblQuery = new JLabel("Enter query:");


        txtQuery = new JTextArea();
        txtQuery.setBackground(Color.BLACK);
        txtQuery.setForeground(Color.GREEN);
        txtQuery.setWrapStyleWord(true);
        txtQuery.setLineWrap(true);
        txtQuery.setEditable(true);
        txtQuery.setOpaque(true);

        JScrollPane jsp = new JScrollPane(txtQuery);
        jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        jsp.setOpaque(false);


        sendQueryBtn.setText("Send Query");
        sendQueryBtn.addActionListener(this);

        clearQueryBtn = new JButton();
        clearQueryBtn.setText("Clear");
        clearQueryBtn.addActionListener(this);

        JLabel jlx = new JLabel();
        jlx.setOpaque(false);
        lowerPanel.add(jlx);
        lowerPanel.add(sendQueryBtn);
        jlx = new JLabel();
        jlx.setOpaque(false);
        lowerPanel.add(jlx);
        lowerPanel.add(clearQueryBtn);


        labelPanel.add(lblQuery);

        jlx = new JLabel();
        jlx.setOpaque(false);
        workBenchUpperPanel.add(jlx);
        workBenchUpperPanel.add(labelPanel);

        workBenchUpperPanel.add(jsp);
        jlx = new JLabel();
        jlx.setOpaque(false);
        workBenchUpperPanel.add(jlx);
        workBenchUpperPanel.setOpaque(false);
        workBenchPanel.add(workBenchUpperPanel);

        jlx = new JLabel();
        jlx.setOpaque(false);
        workBenchPanel.add(jlx);
        workBenchPanel.add(lowerPanel);

        workBenchPanel.setBounds(new Rectangle(5, 400, 490, 150));

        return workBenchPanel;
    }

    private void getDispatchers() throws MalformedURLException, RemoteException, NotBoundException {
        String ip = txtIP.getText();
        String serverName = txtName.getText();
        String port = txtPort.getText();
        String clientIp = getIpAddress();
        if ((isValidIp(ip)) && (serverName.length() != 0)) {
            ips = ClientHandler.getServiceIp(ip, serverName, clientIp);
            if (!ips.isEmpty()) {
                listLookup.setListData(ips);
            }
        }
    }

    public void cancelSend() {
        int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to cancel the Process", "Epzilla", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (response == JOptionPane.YES_OPTION) {
            btnCancelSend.setEnabled(false);
        }

    }

    /*
   set the Dispatcher Service details in the client user interface
    */

    public void setDispValues(String str) {
        StringTokenizer st = new StringTokenizer(str);
        String ip = st.nextToken();
        String servicename = st.nextToken();
        txtDispIP.setText(ip);
        txtDispName.setText(servicename);
        if (!isRegister) {
            try {
                client.regForCallback(ip, servicename);
                ClientHandler.registerClient(clientIP, clientID);
                isRegister = true;
            } catch (RemoteException e) {
                Logger.error("Remote: ", e);
            } catch (MalformedURLException e) {
                Logger.error("Malformed: ", e);
            } catch (NotBoundException e) {
                Logger.error("Not bound: ", e);
            } catch (UnknownHostException e) {
                Logger.error("Host: ", e);
            }
        }
    }

    private void unregisterCallbackLocal() {
        String ip = txtDispIP.getText();
        String servicename = txtDispName.getText();
        try {
            client.unregisterCallback(ip, servicename);
            ClientHandler.unRegisterClient(clientIP, clientID);
        } catch (MalformedURLException e) {
            Logger.error("URL error", e);
        } catch (RemoteException e) {
            Logger.error("Remote error", e);
        } catch (NotBoundException e) {
            Logger.error("Registry bind error", e);
        } catch (UnknownHostException e) {
            Logger.error("Host error", e);
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

    /*
   check the IP address is valid
   use regular expression to validate the IP address
    */

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

    /*
   check the port is in a valid range
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

    /*
   Connecting to the Dispatcher through client initialize class
    */

    public void initProcess() {
        dispIP = txtDispIP.getText();
        dispName = txtDispName.getText();

        if ((dispIP.length() == 0) && (dispName.length() == 0)) {
            JOptionPane.showMessageDialog(null, "Perform Lookup operation and select service you want.", "Epzilla", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if ((dispIP.length() != 0) && (dispName.length() != 0)) {
            try {
                ClientInit.initSend(dispIP, dispName, clientID);
                JOptionPane.showMessageDialog(null, "Successfully connected to the Dispatcher", "Epzilla", JOptionPane.INFORMATION_MESSAGE);
            } catch (MalformedURLException e) {
                JOptionPane.showMessageDialog(null, "Error in file send process.", "Epzilla", JOptionPane.ERROR_MESSAGE);
            } catch (NotBoundException e) {
                JOptionPane.showMessageDialog(null, "Dispatcher failure.", "Epzilla", JOptionPane.ERROR_MESSAGE);
            } catch (RemoteException ignored) {
            }
        } else
            JOptionPane.showMessageDialog(null, "Error in file send process.", "Epzilla", JOptionPane.ERROR_MESSAGE);

    }

    /*
   load file settings
    */

    private void loadSettings() {
        try {
            ArrayList<String[]> data = reader.getServerIPSettings("server_settings.xml");
            String[] ar = data.get(0);
            txtIP.setText(ar[0]);
            txtPort.setText(ar[1]);
            txtName.setText(ar[2]);
        } catch (IOException e) {
            Logger.error("Error in load setings: ", e);
        }
    }

    /**
     * This method system call system exit
     */
    private void systemExit() {
        int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "Confirm Exit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (response == 0) {
            System.exit(0);
            unregisterCallbackLocal();
        }
    }

    /*
   get the IP address of the own machine
    */

    private String getIpAddress() {
        InetAddress inetAddress = null;
        try {
            inetAddress = InetAddress.getLocalHost();
        } catch (java.net.UnknownHostException e) {
            Logger.error("Error in getting IP address: ", e);
        }
        assert inetAddress != null;
        clientIP = inetAddress.getHostAddress();
        return clientIP;
    }

    /*
    get generated client ID via ClientInit class
     */

    private void getClientID() throws RemoteException {
        String clientIP = getIpAddress();
        clientID = ClientHandler.getClientsID(clientIP);
    }

    @Override
    public void valueChanged(ListSelectionEvent event) {
        Object source = event.getSource();
        if (source == listLookup) {
            int i = listLookup.getSelectedIndex();
            if (i < 0) {
                i = 0;
            }
            ListModel model = listLookup.getModel();

            String s = (String) model.getElementAt(0);
            if (s != null) {
                setDispValues(s);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        try {
            Object source = event.getSource();
            if (source == btnSend) {
                initProcess();
            } else if (source == btnCancelSend) {
                cancelSend();
            } else if (source == btnClear) {
                unregisterCallbackLocal();
            } else if (source == btnLookup) {
                try {
                    getDispatchers();
                    getClientID();
                    if (ips.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "No active Dispatchers available, make sure settings details are correct", "Epzilla", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (MalformedURLException e) {
                    JOptionPane.showMessageDialog(null, "NameService IP Address incorrect", "Epzilla", JOptionPane.ERROR_MESSAGE);
                } catch (RemoteException e) {
                    JOptionPane.showMessageDialog(null, "Invalid NameService name, make sure settings details are correct", "Epzilla", JOptionPane.ERROR_MESSAGE);
                    txtDispName.setText("");
                    txtDispIP.setText("");
                    listLookup.removeAll();
                    listLookup.repaint();
                } catch (NotBoundException e) {
                    JOptionPane.showMessageDialog(null, "Invalid NameService name, make sure settings details are correct", "Epzilla", JOptionPane.ERROR_MESSAGE);
                }
            } else if (source == adminSettings) {
                tabbedPane.setVisible(true);
            } else if (source == closetabs) {
                tabbedPane.setVisible(false);
            } else if (source == exit) {
                systemExit();
            } else if (source == about) {
                showAbout();
            } else if (source == sendQueryBtn) {
                dispIP = txtDispIP.getText();
                dispName = txtDispName.getText();
                if ((dispIP.length() != 0) && (dispName.length() != 0)) {
                    if (!txtQuery.getText().equalsIgnoreCase("")) {
                        ClientInit.sendCustomTriggers(this.txtQuery.getText());
                    } else if (txtQuery.getText().equalsIgnoreCase("")) {
                        JOptionPane.showMessageDialog(null, "Enter a query to proceed..", "Epzilla", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else if (source == this.clearQueryBtn) {
                this.txtQuery.setText("");
            }
        } catch (HeadlessException e) {
            Logger.error("error in action performed", e);
        }
    }


}
