package org.epzilla.accumulator.userinterface;

import org.epzilla.util.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AccumulatorUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel jContentPane = null;
    private JTextArea txtEventResults = null;
    private JTextArea txtAccStatus = null;
    private JTextArea txtDeriveEvent = null;
    private JScrollPane resultScrollPane = null;
    private JTextArea txtEvPro = null;

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
            txtAccStatus.setSize(new Dimension(200, 20));
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
            txtDeriveEvent.setSize(new Dimension(200, 20));
            txtDeriveEvent.setLocation(new Point(790, 130));
            txtDeriveEvent.setBackground(Color.black);
        }
        return txtDeriveEvent;
    }

    public JTextArea getEventProcessed() {
        if (txtEvPro == null) {
            txtEvPro = new JTextArea();
            txtEvPro.setBackground(Color.black);
            txtEvPro.setForeground(Color.green);
            txtEvPro.setLocation(new Point(790, 170));
            txtEvPro.setSize(new Dimension(200, 20));
        }
        return txtEvPro;
    }

    private JScrollPane getResultScrollPane() {
        if (resultScrollPane == null) {
            resultScrollPane = new JScrollPane();
            resultScrollPane.setBounds(new Rectangle(30, 60, 572, 600));
            resultScrollPane.setViewportView(getEventResults());
        }
        return resultScrollPane;
    }

    private JPanel getJContentPane() {
        if (jContentPane == null) {
            JLabel lblResults = new JLabel();
            lblResults.setBounds(new Rectangle(30, 30, 80, 30));
            lblResults.setText("Results:");
            JLabel lblEvents = new JLabel();
            lblEvents.setText("Events processed: ");
            lblEvents.setLocation(new Point(690, 165));
            lblEvents.setSize(new Dimension(129, 30));
            JLabel lblStatus = new JLabel();
            lblStatus.setText("Accumulator Status:");
            lblStatus.setSize(new Dimension(128, 30));
            lblStatus.setLocation(new Point(690, 85));
            JLabel lblEventCount = new JLabel();
            lblEventCount.setText("Derive Event Count:");
            lblEventCount.setLocation(new Point(690, 125));
            lblEventCount.setSize(new Dimension(117, 30));
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
            jContentPane.add(lblEvents, null);
            jContentPane.add(getEventProcessed(), null);
            jContentPane.add(lblResults, null);
        }
        return jContentPane;
    }

}
