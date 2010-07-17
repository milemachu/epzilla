package org.epzilla.clusterNode.userInterface;

import org.epzilla.clusterNode.nodeControler.SleepNode;
import org.epzilla.clusterNode.nodeControler.WakeNode;
import org.epzilla.clusterNode.xml.ConfigurationManager;
import org.epzilla.util.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    private JTextField txtIp1 = new JTextField();
    private JTextField txtIp2 = new JTextField();

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
            tabbedPane.addTab("Configurations", settingsIcon, getConfigurations());
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

            JLabel lbl1 = new JLabel();
            lbl1.setBounds(new Rectangle(20, 10, 150, 20));
            lbl1.setText("Epzilla IP Range:");
            JLabel lbl2 = new JLabel();
            lbl2.setBounds(new Rectangle(20, 35, 100, 20));
            lbl2.setText("Start IP:");
            JLabel lbl3 = new JLabel();
            lbl3.setBounds(new Rectangle(20, 60, 100, 20));
            lbl3.setText("End IP:");

            txtIp1.setBackground(Color.BLACK);
            txtIp1.setForeground(Color.GREEN);
            txtIp1.setBounds(new Rectangle(125, 35, 150, 20));
            txtIp2.setBackground(Color.BLACK);
            txtIp2.setForeground(Color.GREEN);
            txtIp2.setBounds(new Rectangle(125, 60, 150, 20));


            btnSaveConfig = new JButton();
            btnSaveConfig.setText("Save");
            btnSaveConfig.setBounds(new Rectangle(175, 90, 100, 25));
            btnSaveConfig.addActionListener(this);

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
            configPanel.add(txtIp1, null);
            configPanel.add(txtIp2, null);
            configPanel.add(btnSaveConfig, null);
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
            //logic write config data
            ConfigurationManager.writeData(txtIp1.getText(), txtIp2.getText());
        }

    }
}  //  @jve:decl-index=0:visual-constraint="10,10"
