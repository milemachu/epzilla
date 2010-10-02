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
package org.epzilla.clusterNode.userInterface;

import org.epzilla.clusterNode.nodeControler.SleepNode;
import org.epzilla.clusterNode.nodeControler.WakeNode;
import org.epzilla.clusterNode.xml.ConfigurationManager;
import org.epzilla.util.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by IntelliJ IDEA.
 * This class initializes the Node User Interface
 * Date:Feb 1, 2010
 * Time: 10:20:41 PM
 */
public class NodeUI extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    private JTabbedPane tabbedPane = null;
    private JPanel jContentPane = null;
    private JPanel configPanel = null;
    private JScrollPane jScrollPane = null;
    private JScrollPane jScrollPane1 = null;
    private JScrollPane jScrollPane2 = null;
    private JScrollPane jScrollPane3 = null;
    private JScrollPane jScrollPane4 = null;
    private JScrollPane clusterNodesPane = null;
    private JTextArea jTextAreaStatus = null;
    private JTextArea jTextAreaTriggers = null;
    private JTextArea jTextAreaIPList = null;
    private JTextArea jTextAreaPerformance = null;
    private JTextArea jTextAreaMachineInfo = null;
    private JTextArea jTextAreaLeader = null;
    private JTextArea jTextAreaEventCount = null;
    private JTextArea nodesList = null;
    public JButton btnAddNode = null;
    public JButton btnRemoveNode = null;
    private JButton btnSaveConfig;
    private JButton btnClearConfig;
    private JTextArea txtIp = new JTextArea();
    private JTextArea txtAcc = new JTextArea();
    private JTextField txtClusterID = new JTextField();

    /**
     * This is the default constructor
     */
    public NodeUI() {
        super();
        initialize();
    }

    private void initialize() {
        try {
            // Set cross-platform Java L&F (also called "Metal")
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
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
        Toolkit tk = Toolkit.getDefaultToolkit();
        int xSize = ((int) tk.getScreenSize().getWidth());
        int ySize = ((int) tk.getScreenSize().getHeight());
        this.setSize(xSize, ySize);
        this.setPreferredSize(new Dimension(1024, 768));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation(new Point(0, 0));
        this.setContentPane(getMyTabbedPane());
        this.setTitle("epZilla Cluster Node ");
        Image img = Toolkit.getDefaultToolkit().getImage("images//logo.jpg");
        this.setIconImage(img);
    }

    /**
     * This method initializes jtabbedPane
     *
     * @return javax.swing.JTabbedPane
     */
    private JTabbedPane getMyTabbedPane() {
        if (tabbedPane == null) {
            ImageIcon settingsIcon = new ImageIcon("images//settings.jpg");
            ImageIcon summaryIcon = new ImageIcon("images//summary.jpg");

            tabbedPane = new JTabbedPane();
            tabbedPane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
            tabbedPane.setBackground(SystemColor.control);
            tabbedPane.addTab("Summary", summaryIcon, getJContentPane());
            tabbedPane.addTab("Settings", settingsIcon, getConfigurations());
        }
        return tabbedPane;
    }

    /**
     * This method initializes jContentPane
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            JLabel lblCNL = new JLabel();
            lblCNL.setBounds(new Rectangle(736, 10, 98, 16));
            lblCNL.setText("Node List:");
            JLabel lblCTL = new JLabel();
            lblCTL.setBounds(new Rectangle(46, 344, 120, 16));
            lblCTL.setText("Cluster Trigger List:");
            JLabel lblEC = new JLabel();
            lblEC.setBounds(new Rectangle(343, 10, 85, 16));
            lblEC.setText("Event Count:");
            JLabel lblNodeStatus = new JLabel();
            lblNodeStatus.setBounds(new Rectangle(46, 10, 88, 16));
            lblNodeStatus.setText("Node Status:");
            jContentPane = new JPanel() {
                public void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g;
                    int w = getWidth();
                    int h = getHeight();
                    GradientPaint gp = new GradientPaint(0, 100, Color.white, 0, h, Color.gray);
                    g2d.setPaint(gp);
                    g2d.fillRect(0, 0, w, h);
                }
            };
            jContentPane.setLayout(null);
            jContentPane.add(getJScrollPane(), null);
            jContentPane.add(getJScrollPane1(), null);
            jContentPane.add(getJTextAreaLeader(), null);
            jContentPane.add(getJTextAreaEventCount(), null);
            jContentPane.add(getNodesPane(), null);
            jContentPane.add(lblNodeStatus, null);
            jContentPane.add(lblEC, null);
            jContentPane.add(lblCTL, null);
            jContentPane.add(getAddNodeButton(), null);
            jContentPane.add(getRemoveNodeButton(), null);
            jContentPane.add(lblCNL, null);

            CpuAnalyzer ca = new CpuAnalyzer();
            ca.setBounds(new Rectangle(736, 510, 244, 140));
            jContentPane.add(ca);

            MemoryTable mt = new MemoryTable();
            mt.setBounds(new Rectangle(736, 366, 244, 140));
            jContentPane.add(mt);

        }
        return jContentPane;
    }

    /**
     * This method initializes the configuration panel
     *
     * @return javax.swing.JPanel
     */
    private JPanel getConfigurations() {
        if (configPanel == null) {
            //EpZIlla IP range
            JLabel lbl1 = new JLabel("EpZilla Node IP's :");
            lbl1.setBounds(new Rectangle(20, 35, 100, 20));

            //Accumulator range
            JLabel lbl2 = new JLabel("Accumulator Ip's:");
            lbl2.setBounds(new Rectangle(20, 150, 100, 20));

            JLabel lbl3 = new JLabel("Cluster ID:");
            lbl3.setBounds(new Rectangle(20, 10, 100, 20));

            //text fields
            txtIp.setBounds(new Rectangle(120, 35, 200, 80));
            txtIp.setBackground(Color.BLACK);
            txtIp.setForeground(Color.GREEN);
            txtIp.setOpaque(true);
            JScrollPane jspIP = new JScrollPane(txtIp);
            jspIP.setBounds(new Rectangle(120, 35, 200, 80));
            jspIP.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            jspIP.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            jspIP.setOpaque(false);

            txtAcc.setBounds(new Rectangle(120, 150, 200, 80));
            txtAcc.setBackground(Color.BLACK);
            txtAcc.setForeground(Color.GREEN);
            JScrollPane jspAcc = new JScrollPane(txtAcc);
            jspAcc.setBounds(new Rectangle(120, 150, 200, 80));
            jspAcc.setHorizontalScrollBarPolicy((JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
            jspAcc.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            jspAcc.setOpaque(false);

            txtClusterID.setBounds(new Rectangle(120, 10, 200, 20));
            txtClusterID.setForeground(Color.green);
            txtClusterID.setBackground(Color.BLACK);

            //save configurations
            btnSaveConfig = new JButton("Save");
            btnSaveConfig.setBounds(new Rectangle(240, 250, 80, 25));
            btnSaveConfig.addActionListener(this);
            btnClearConfig = new JButton("Clear");
            btnClearConfig.setBounds(new Rectangle(140, 250, 80, 25));
            btnClearConfig.addActionListener(this);

            configPanel = new JPanel() {
                public void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g;
                    int w = getWidth();
                    int h = getHeight();
                    GradientPaint gp = new GradientPaint(0, 100, Color.white, 0, h, Color.gray);
                    g2d.setPaint(gp);
                    g2d.fillRect(0, 0, w, h);
                }
            };
            configPanel.setLayout(null);
            configPanel.add(lbl1, null);
            configPanel.add(lbl2, null);
            configPanel.add(lbl3, null);
            configPanel.add(txtClusterID);
            configPanel.add(jspIP, null);
            configPanel.add(jspAcc, null);
            configPanel.add(btnSaveConfig, null);
            configPanel.add(btnClearConfig, null);
        }
        return configPanel;
    }

    /**
     * This method initializes jScrollPane
     *
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getJScrollPane() {
        if (jScrollPane == null) {
            jScrollPane = new JScrollPane();
            jScrollPane.setBounds(new Rectangle(46, 45, 642, 280));
            jScrollPane.setViewportView(getJTextAreaStatus());
        }
        return jScrollPane;
    }

    /**
     * This method initializes jScrollPane1
     *
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getJScrollPane1() {
        if (jScrollPane1 == null) {
            jScrollPane1 = new JScrollPane();
            jScrollPane1.setBounds(new Rectangle(46, 366, 643, 285));
            jScrollPane1.setViewportView(getJTextAreaTriggers());
        }
        return jScrollPane1;
    }

    /**
     * This method initializes jScrollPane2
     *
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getJScrollPane2() {
        if (jScrollPane2 == null) {
            jScrollPane2 = new JScrollPane();
            jScrollPane2.setBounds(new Rectangle(736, 45, 244, 120));
            jScrollPane2.setViewportView(getJTextAreaIPList());
        }
        return jScrollPane2;
    }

    private JScrollPane getNodesPane() {
        if (clusterNodesPane == null) {
            clusterNodesPane = new JScrollPane();
            clusterNodesPane.setBounds(new Rectangle(736, 45, 244, 280));
            clusterNodesPane.setViewportView(getJTextAreaNodeList());
        }
        return clusterNodesPane;
    }

    /**
     * This method initializes jScrollPane3
     *
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getJScrollPane3() {
        if (jScrollPane3 == null) {
            jScrollPane3 = new JScrollPane();
            jScrollPane3.setBounds(new Rectangle(736, 510, 244, 150));
            jScrollPane3.setViewportView(getjTextAreaPerformance());
        }
        return jScrollPane3;
    }

    /**
     * This method initializes jScrollPane4
     *
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getJScrollPane4() {
        if (jScrollPane4 == null) {
            jScrollPane4 = new JScrollPane();
            jScrollPane4.setBounds(new Rectangle(736, 366, 244, 145));
            jScrollPane4.setViewportView(getjTextAreaMachineInfo());
        }
        return jScrollPane4;
    }


    /**
     * This method initializes jTextAreaStatus
     *
     * @return javax.swing.JTextArea
     */
    public JTextArea getJTextAreaStatus() {
        if (jTextAreaStatus == null) {
            jTextAreaStatus = new JTextArea();
            jTextAreaStatus.setBackground(Color.black);
            jTextAreaStatus.setForeground(Color.green);
        }
        return jTextAreaStatus;
    }

    /**
     * This method initializes jTextAreaTriggers
     *
     * @return javax.swing.JTextArea
     */
    public JTextArea getJTextAreaTriggers() {
        if (jTextAreaTriggers == null) {
            jTextAreaTriggers = new JTextArea();
            jTextAreaTriggers.setBackground(Color.black);
            jTextAreaTriggers.setForeground(Color.green);
        }
        return jTextAreaTriggers;
    }

    /**
     * This method initializes jTextAreaIPList
     *
     * @return javax.swing.JTextArea
     */
    public JTextArea getJTextAreaIPList() {
        if (jTextAreaIPList == null) {
            jTextAreaIPList = new JTextArea();
            jTextAreaIPList.setBackground(Color.black);
            jTextAreaIPList.setForeground(Color.green);
        }
        return jTextAreaIPList;
    }

    public JTextArea getJTextAreaNodeList() {
        if (nodesList == null) {
            nodesList = new JTextArea();
            nodesList.setBackground(Color.black);
            nodesList.setForeground(Color.green);
        }
        return nodesList;
    }

    public JTextArea getjTextAreaPerformance() {
        if (jTextAreaPerformance == null) {
            jTextAreaPerformance = new JTextArea();
            jTextAreaPerformance.setBackground(Color.black);
            jTextAreaPerformance.setForeground(Color.green);
        }
        return jTextAreaPerformance;
    }

    public JTextArea getjTextAreaMachineInfo() {
        if (jTextAreaMachineInfo == null) {
            jTextAreaMachineInfo = new JTextArea();
            jTextAreaMachineInfo.setBackground(Color.black);
            jTextAreaMachineInfo.setForeground(Color.green);
        }
        return jTextAreaMachineInfo;
    }


    /**
     * This method initializes jTextAreaLeader
     *
     * @return javax.swing.JTextArea
     */
    public JTextArea getJTextAreaLeader() {
        if (jTextAreaLeader == null) {
            jTextAreaLeader = new JTextArea();
            jTextAreaLeader.setForeground(Color.green);
            jTextAreaLeader.setBounds(new Rectangle(120, 10, 150, 20));
            jTextAreaLeader.setBackground(Color.black);
        }
        return jTextAreaLeader;
    }

    /**
     * This method initializes jTextAreaEventCount
     *
     * @return javax.swing.JTextArea
     */
    public JTextArea getJTextAreaEventCount() {
        if (jTextAreaEventCount == null) {
            jTextAreaEventCount = new JTextArea();
            jTextAreaEventCount.setForeground(Color.green);
            jTextAreaEventCount.setBounds(new Rectangle(420, 10, 150, 20));
            jTextAreaEventCount.setBackground(Color.black);
        }
        return jTextAreaEventCount;
    }

    public JButton getAddNodeButton() {
        if (btnAddNode == null) {
            ImageIcon logsIcon = new ImageIcon("images//register.jpg");
            btnAddNode = new JButton(logsIcon);
            btnAddNode.setBounds(new Rectangle(46, 660, 120, 20));
            btnAddNode.setText("Add Node");
            btnAddNode.addActionListener(this);
        }
        return btnAddNode;
    }

    public JButton getRemoveNodeButton() {
        if (btnRemoveNode == null) {
            ImageIcon logsIcon = new ImageIcon("images//close.jpg");
            btnRemoveNode = new JButton(logsIcon);
            btnRemoveNode.setBounds(new Rectangle(200, 660, 120, 20));
            btnRemoveNode.setText("Remove Node");
            btnRemoveNode.addActionListener(this);
        }
        return btnRemoveNode;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == btnAddNode) {
            WakeNode.wake();
        }
        if (source == btnRemoveNode) {
            SleepNode.sleep();
        }
        if (source == btnSaveConfig) {
            String[] accumulators = txtAcc.getText().split("\\n");
            String[] nodes = txtIp.getText().split("\\n");
            ConfigurationManager cf = new ConfigurationManager(nodes, accumulators, txtClusterID.getText());
            //logic write config data
            if (cf.writeInfo())
                JOptionPane.showMessageDialog(null, "Configurations details successfully saved", "Epzilla", JOptionPane.INFORMATION_MESSAGE);
            else
                JOptionPane.showMessageDialog(null, "Error in Configurations. Make sure the IP values are in a valid range", "Epzilla", JOptionPane.ERROR_MESSAGE);

        }
        if (source == btnClearConfig) {
            txtAcc.setText("");
            txtIp.setText("");
        }

    }
}
