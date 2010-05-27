package org.epzilla.clusterNode.userInterface;

import javax.swing.*;
import java.awt.*;
import javax.swing.JLabel;
import java.awt.Rectangle;
import java.awt.Font;

public class NodeUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel jContentPane = null;
    private JScrollPane jScrollPane = null;
    private JScrollPane jScrollPane1 = null;
    private JScrollPane jScrollPane2 = null;
    private JScrollPane jScrollPane3 = null;
    private JScrollPane jScrollPane4 = null;
    private JTextArea jTextAreaStatus = null;
    private JTextArea jTextAreaTriggers = null;
    private JTextArea jTextAreaIPList = null;
    private JTextArea jTextAreaPerformance = null;
    private JTextArea jTextAreaMachineInfo = null;
    private JTextArea jTextAreaLeader = null;
    private JTextArea jTextAreaEventCount = null;
	private JLabel lblNodeStatus = null;
	private JLabel lblEC = null;
	private JLabel lblCTL = null;
	private JLabel lblCIL = null;
	/**
     * This is the default constructor
     */
    public NodeUI() {
        super();
        initialize();
    }

    /**
     * This method initializes this
     *
     * @return void
     */
    private void initialize() {
        try {
            // Set cross-platform Java L&F (also called "Metal")
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        }
        catch (UnsupportedLookAndFeelException e) {
            // handle exception
        }
        catch (ClassNotFoundException e) {
            // handle exception
        }
        catch (InstantiationException e) {
            // handle exception
        }
        catch (IllegalAccessException e) {
            // handle exception
        }
        Toolkit tk = Toolkit.getDefaultToolkit();
        int xSize = ((int) tk.getScreenSize().getWidth());
        int ySize = ((int) tk.getScreenSize().getHeight());
        this.setSize(xSize, ySize);
        this.setPreferredSize(new Dimension(1024, 768));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation(new Point(0, 0));
        this.setContentPane(getJContentPane());
        this.setTitle("epZilla Cluster Node ");
        Image img = Toolkit.getDefaultToolkit().getImage("images//logo.jpg");
        this.setIconImage(img);
    }

    /**
     * This method initializes jContentPane
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            lblCIL = new JLabel();
            lblCIL.setBounds(new Rectangle(736, 63, 98, 16));
            lblCIL.setText("Cluster IP List:");
            lblCTL = new JLabel();
            lblCTL.setBounds(new Rectangle(46, 364, 120, 16));
            lblCTL.setText("Cluster Trigger List:");
            lblEC = new JLabel();
            lblEC.setBounds(new Rectangle(343, 61, 85, 16));
            lblEC.setText("Event Count:");
            lblNodeStatus = new JLabel();
            lblNodeStatus.setBounds(new Rectangle(46, 60, 88, 16));
            lblNodeStatus.setText("Node Status:");
            jContentPane = new JPanel(){
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
            jContentPane.add(getJScrollPane2(), null);
            jContentPane.add(getJScrollPane3(), null);
            jContentPane.add(getJScrollPane4(), null);
            jContentPane.add(getJTextAreaLeader(), null);
            jContentPane.add(getJTextAreaEventCount(), null);
            jContentPane.add(lblNodeStatus, null);
            jContentPane.add(lblEC, null);
            jContentPane.add(lblCTL, null);
            jContentPane.add(lblCIL, null);
        }
        return jContentPane;
    }

    /**
     * This method initializes jScrollPane
     *
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getJScrollPane() {
        if (jScrollPane == null) {
            jScrollPane = new JScrollPane();
            jScrollPane.setBounds(new Rectangle(46, 93, 642, 246));
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
            jScrollPane1.setBounds(new Rectangle(46, 386, 643, 295));
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
            jScrollPane2.setBounds(new Rectangle(736, 89, 244, 250));
            jScrollPane2.setViewportView(getJTextAreaIPList());
        }
        return jScrollPane2;
    }

    /**
     * This method initializes jScrollPane3
     *
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getJScrollPane3() {
        if (jScrollPane3 == null) {
            jScrollPane3 = new JScrollPane();
            jScrollPane3.setBounds(new Rectangle(736, 531, 244, 150));
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
            jScrollPane4.setBounds(new Rectangle(736, 386, 244, 145));
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
            jTextAreaLeader.setBounds(new Rectangle(138, 60, 165, 20));
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
            jTextAreaEventCount.setBounds(new Rectangle(433, 60, 136, 21));
            jTextAreaEventCount.setBackground(Color.black);
        }
        return jTextAreaEventCount;
    }

}  //  @jve:decl-index=0:visual-constraint="10,10"
