package org.epzilla.accumulator.userinterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AccumulatorUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel jContentPane = null;
    private JTextArea txtEventResults = null;
    private JLabel lblEventCount = null;
    private JLabel lblStatus = null;
    private JTextArea txtAccStatus = null;
    private JTextArea txtDeriveEvent = null;
    private JScrollPane resultScrollPane = null;
    private JLabel lblTrigger = null;
    private JTextArea txtEvPro = null;
    private JLabel lblResults = null;

    /**
     * This is the default constructor
     */
    public AccumulatorUI() {
        super();
        initialize();
    }

    private void initialize() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (UnsupportedLookAndFeelException e) {
        }
        catch (ClassNotFoundException e) {
        }
        catch (InstantiationException e) {
        }
        catch (IllegalAccessException e) {
        }
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = screen.width;
        int y = screen.height;
        this.setTitle("Accumulator");
        Image img = Toolkit.getDefaultToolkit().getImage("images//logo.jpg");
        this.setIconImage(img);
        this.setSize(x, y);
        this.setPreferredSize(new Dimension(1024, 768));
        this.setContentPane(getJContentPane());
        this.setTitle("Accumulator");
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

    public JTextArea getEventResults() {
        if (txtEventResults == null) {
            txtEventResults = new JTextArea();
            txtEventResults.setBackground(Color.black);
            txtEventResults.setForeground(Color.green);
        }
        return txtEventResults;
    }

    public JTextArea getAccumulatorStatus() {
        if (txtAccStatus == null) {
            txtAccStatus = new JTextArea();
            txtAccStatus.setEditable(false);
            txtAccStatus.setForeground(Color.green);
            txtAccStatus.setSize(new Dimension(220, 25));
            txtAccStatus.setLocation(new Point(790, 90));
            txtAccStatus.setBackground(Color.black);
        }
        return txtAccStatus;
    }

    public JTextArea getDeriveEventCount() {
        if (txtDeriveEvent == null) {
            txtDeriveEvent = new JTextArea();
            txtDeriveEvent.setEditable(false);
            txtDeriveEvent.setForeground(Color.green);
            txtDeriveEvent.setSize(new Dimension(220, 25));
            txtDeriveEvent.setLocation(new Point(790, 130));
            txtDeriveEvent.setBackground(Color.black);
        }
        return txtDeriveEvent;
    }

    private JScrollPane getResultScrollPane() {
        if (resultScrollPane == null) {
            resultScrollPane = new JScrollPane();
            resultScrollPane.setBounds(new Rectangle(30, 90, 572, 500));
            resultScrollPane.setViewportView(getEventResults());
        }
        return resultScrollPane;
    }

    private JPanel getJContentPane() {
        if (jContentPane == null) {
            lblResults = new JLabel();
            lblResults.setBounds(new Rectangle(29, 17, 77, 27));
            lblResults.setBounds(new Rectangle(29, 48, 77, 27));
            lblResults.setText("Results:");
            lblTrigger = new JLabel();
            lblTrigger.setText("Events processed: ");
            lblTrigger.setLocation(new Point(690, 170));
            lblTrigger.setSize(new Dimension(129, 20));
            lblStatus = new JLabel();
            lblStatus.setText("Accumulator Status:");
            lblStatus.setSize(new Dimension(128, 25));
            lblStatus.setLocation(new Point(690, 90));
            lblEventCount = new JLabel();
            lblEventCount.setText("Derive Event Count:");
            lblEventCount.setLocation(new Point(690, 130));
            lblEventCount.setSize(new Dimension(117, 25));
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
            jContentPane.add(lblEventCount, null);
            jContentPane.add(lblStatus, null);
            jContentPane.add(getAccumulatorStatus(), null);
            jContentPane.add(getDeriveEventCount(), null);
            jContentPane.add(getResultScrollPane(), null);
            jContentPane.add(lblTrigger, null);
            jContentPane.add(getEventProcessed(), null);
            jContentPane.add(lblResults, null);
        }
        return jContentPane;
    }

    public JTextArea getEventProcessed() {
        if (txtEvPro == null) {
            txtEvPro = new JTextArea();
            txtEvPro.setBackground(Color.black);
            txtEvPro.setForeground(Color.green);
            txtEvPro.setLocation(new Point(790, 170));
            txtEvPro.setSize(new Dimension(220, 25));
        }
        return txtEvPro;
    }


}
