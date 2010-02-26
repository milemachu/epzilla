package org.epzilla.dispatcher;

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
import java.awt.event.KeyEvent;
import java.awt.Rectangle;
import javax.swing.JCheckBox;
import java.awt.Font;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.RemoteException;

import javax.swing.plaf.metal.*;
import javax.swing.JTextPane;


public class DispatcherUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTabbedPane tabbedPane = null;
    private JTextField tbIP = null;
    private JTextField tbPort = null;
    private JTextField tbName = null;
    private JLabel labelIP = null;
    private JLabel labelPort = null;
    private JLabel labelName = null;
    private JButton btnRegister = null;
    private JButton btnCancel = null;
    private JTextArea txtResult = null;
    private JLabel lblDetails = null;
    private JMenuBar menuBar = null;
    private JMenuItem about = null;
    private JMenuItem help = null;
    private JMenuItem adminSettings = null;
    private JMenuItem closetabs = null;
    private JMenuItem exit = null;
    private JMenu file = new JMenu("File");
    private JMenu helpmenu = new JMenu("Help");
    JPanel mainSettings = null;
    JPanel helptab = null;
    JPanel other = null;
    private JTextArea txtTriggers = null;
    private JLabel lblDisp = null;
    private JLabel lblName = null;
    private JTextField tbDispSerName = null;
    private JButton btnBind = null;
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
            lblDetails.setText("  NameServer Details");
            labelName = new JLabel();
            labelName.setText("  Service Name :");
            labelName.setSize(new Dimension(121, 25));
            labelName.setLocation(new Point(15, 91));
            labelPort = new JLabel();
            labelPort.setText("  Port :");
            labelPort.setSize(new Dimension(119, 25));
            labelPort.setLocation(new Point(17, 135));
            labelIP = new JLabel();
            labelIP.setText("  Server IP Address :");
            labelIP.setSize(new Dimension(121, 25));
            labelIP.setLocation(new Point(15, 49));
            tabbedPane = new JTabbedPane();
            tabbedPane.addTab("Settings", getMainSettings());
            tabbedPane.addTab("Help", getHelpTab());
            tabbedPane.addTab("Other", getOthertab());
        }
        return tabbedPane;
    }

    private JPanel getMainSettings() {
        if (mainSettings == null) {
            lblName = new JLabel();
            lblName.setBounds(new Rectangle(25, 266, 113, 16));
            lblName.setText("  Service Name :");
            lblDisp = new JLabel();
            lblDisp.setBounds(new Rectangle(22, 235, 175, 16));
            lblDisp.setText(" Dispatcher Service Details");
            mainSettings = new JPanel();
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
            mainSettings.add(getBtnBind(), null);
        }
        return mainSettings;
    }

    private JPanel getHelpTab() {
        if (helptab == null) {
            helptab = new JPanel();
            helptab.setLayout(null);
        }
        return helptab;
    }

    private JPanel getOthertab() {
        if (other == null) {
            lblIPs = new JLabel();
            lblIPs.setBounds(new Rectangle(571, 17, 38, 16));
            lblIPs.setText("  IP :");
            lblEvents = new JLabel();
            lblEvents.setBounds(new Rectangle(16, 16, 117, 16));
            lblEvents.setText(" Status :");
            lblTriggers = new JLabel();
            lblTriggers.setBounds(new Rectangle(15, 256, 116, 16));
            lblTriggers.setText("  Trigger List :");
            other = new JPanel();
            other.setLayout(null);
            other.add(getTxtTriggers(), null);
            other.add(getTxtStatus(), null);
            other.add(lblTriggers, null);
            other.add(lblEvents, null);
            other.add(lblIPs, null);
            other.add(getTxtIPs(), null);
        }
        return other;
    }

    private JMenuBar getmyMenuBar() {
        if (menuBar == null) {
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

    private JMenuItem getAboutMI() {
        if (about == null) {
            about = new JMenuItem();
            about.setText("About");
            about.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                }
            });
        }
        return about;
    }

    private JMenuItem getHelpMI() {
        if (help == null) {
            help = new JMenuItem();
            help.setText("Help");
            help.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
                }
            });
        }
        return help;
    }

    private JMenuItem getAdminSettingMI() {
        if (adminSettings == null) {
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

    private JMenuItem getExitMI() {
        if (exit == null) {
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

    private JMenuItem getCloseMI() {
        if (closetabs == null) {
            closetabs = new JMenuItem();
            closetabs.setText("Close All");
            closetabs.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    tabbedPane.setVisible(false);
                }
            });
        }
        return closetabs;
    }

    private JTextField getIpTextField() {
        if (tbIP == null) {
            tbIP = new JTextField();
            tbIP.setLocation(new Point(158, 51));
            tbIP.setSize(new Dimension(200, 20));
        }
        return tbIP;
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
        if (tbName == null) {
            tbName = new JTextField();
            tbName.setLocation(new Point(160, 95));
            tbName.setSize(new Dimension(200, 20));
        }
        return tbName;
    }

    private JButton getBtnRegister() {
        if (btnRegister == null) {
            btnRegister = new JButton();
            btnRegister.setLocation(new Point(174, 181));
            btnRegister.setText("Register");
            btnRegister.setSize(new Dimension(85, 20));
            btnRegister.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    register();
                }
            });
        }
        return btnRegister;
    }

    private void register() {
        listener.register(tbIP.getText().toString(), tbName.getText().toString());
    }

    private JButton getBtnCancel() {
        if (btnCancel == null) {
            btnCancel = new JButton();
            btnCancel.setLocation(new Point(269, 181));
            btnCancel.setText("Cancel");
            btnCancel.setMnemonic(KeyEvent.VK_UNDEFINED);
            btnCancel.setSize(new Dimension(85, 20));
            btnCancel.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    System.out.println("mouseClicked()"); // TODO Auto-generated Event stub mouseClicked()
                }
            });
        }
        return btnCancel;
    }

    private JTextArea getTxtResult() {
        if (txtResult == null) {
            txtResult = new JTextArea();
            txtResult.setLocation(new Point(19, 340));
            txtResult.setBackground(new Color(238, 238, 238));
            txtResult.setSize(new Dimension(385, 132));
        }
        return txtResult;
    }

    private JTextArea getTxtTriggers() {
        if (txtTriggers == null) {
            txtTriggers = new JTextArea();
            txtTriggers.setBounds(new Rectangle(13, 284, 484, 173));
            txtTriggers.setRows(10000);
        }
        return txtTriggers;
    }

    private JTextField getTbDispSerName() {
        if (tbDispSerName == null) {
            tbDispSerName = new JTextField();
            tbDispSerName.setBounds(new Rectangle(169, 266, 195, 20));
        }
        return tbDispSerName;
    }

    private JButton getBtnBind() {
        if (btnBind == null) {
            btnBind = new JButton();
            btnBind.setLocation(new Point(376, 265));
            btnBind.setText("Bind");
            btnBind.setSize(new Dimension(85, 20));
            btnBind.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    bind();
                }
            });
        }
        return btnBind;
    }

    private void bind() {
        try {
            listener.bindDispatcher(tbDispSerName.getText().toString());
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private JTextArea getTxtStatus() {
        if (txtStatus == null) {
            txtStatus = new JTextArea();
            txtStatus.setBounds(new Rectangle(15, 47, 481, 177));
        }
        return txtStatus;
    }

    private JTextArea getTxtIPs() {
        if (txtIPs == null) {
            txtIPs = new JTextArea();
            txtIPs.setBounds(new Rectangle(570, 45, 226, 300));
        }
        return txtIPs;
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
        int width = 850;
        int height = 600;
        this.setContentPane(getMyTabbedPane());
        this.setJMenuBar(getmyMenuBar());
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width - width) / 2;
        int y = (screen.height - height) / 2;
        setBounds(x, y, width, height);
        this.setTitle("Dispatcher");
        this.setSize(new Dimension(753, 410));

    }

    public void setStatus(String text) {
        txtStatus.append(text);
    }
}  //  @jve:decl-index=0:visual-constraint="10,10"  
