package org.epzilla.accumulator.userinterface;

import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Dimension;
import javax.swing.JTextArea;
import java.awt.Rectangle;
import javax.swing.JLabel;
import java.awt.Point;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JSplitPane;
import javax.swing.JScrollPane;

public class AccumulatorUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JTextArea txtEventResults = null;
	private JLabel lblEventCount = null;
	private JLabel lblStatus = null;
	private JTextArea txtAccStatus = null;
	private JTextArea txtEventCount = null;
	private JScrollPane resultScrollPane = null;
	private JLabel lblTrigger = null;
	private JTextArea txtTriggersPro = null;
	/**
	 * This is the default constructor
	 */
	public AccumulatorUI() {
		super();
		initialize();
	}
	private void initialize() {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = screen.width;
        int y = screen.height;
       	this.setTitle("Accumulator");
       	Image img = Toolkit.getDefaultToolkit().getImage("images//logo.jpg");
       	this.setIconImage(img);
       	this.setSize(x,y);
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

	public JTextArea getTxtEventResults() {
		if (txtEventResults == null) {
			txtEventResults = new JTextArea();
			txtEventResults.setBackground(Color.black);
			txtEventResults.setForeground(Color.green);
		}
		return txtEventResults;
	}
	public JTextArea getTxtAccStatus() {
		if (txtAccStatus == null) {
			txtAccStatus = new JTextArea();
			txtAccStatus.setEditable(false);
			txtAccStatus.setForeground(Color.green);
			txtAccStatus.setSize(new Dimension(140, 20));
			txtAccStatus.setLocation(new Point(165, 42));
			txtAccStatus.setBackground(Color.black);
		}
		return txtAccStatus;
	}

	public JTextArea getTxtEventCount() {
		if (txtEventCount == null) {
			txtEventCount = new JTextArea();
			txtEventCount.setEditable(false);
			txtEventCount.setForeground(Color.green);
			txtEventCount.setBounds(new Rectangle(836, 92, 140, 20));
			txtEventCount.setSize(new Dimension(140, 20));
			txtEventCount.setLocation(new Point(840, 92));
			txtEventCount.setBackground(Color.black);
		}
		return txtEventCount;
	}
	private JScrollPane getResultScrollPane() {
		if (resultScrollPane == null) {
			resultScrollPane = new JScrollPane();
			resultScrollPane.setBounds(new Rectangle(30, 90, 572, 500));
			resultScrollPane.setViewportView(getTxtEventResults());
		}
		return resultScrollPane;
	}
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			lblTrigger = new JLabel();
			lblTrigger.setBounds(new Rectangle(700, 128, 129, 20));
			lblTrigger.setText("Triggers processed: ");
			lblStatus = new JLabel();
			lblStatus.setText("Accumulator Status:");
			lblStatus.setSize(new Dimension(128, 20));
			lblStatus.setLocation(new Point(30, 44));
			lblEventCount = new JLabel();
			lblEventCount.setText("Event Count:");
			lblEventCount.setBounds(new Rectangle(700, 91, 80, 20));
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(lblEventCount, null);
			jContentPane.add(lblStatus, null);
			jContentPane.add(getTxtAccStatus(), null);
			jContentPane.add(getTxtEventCount(), null);
			jContentPane.add(getResultScrollPane(), null);
			jContentPane.add(lblTrigger, null);
			jContentPane.add(getTxtTriggersPro(), null);
		}
		return jContentPane;
	}
	/**
	 * This method initializes txtTriggersPro	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	public JTextArea getTxtTriggersPro() {
		if (txtTriggersPro == null) {
			txtTriggersPro = new JTextArea();
			txtTriggersPro.setLocation(new Point(838, 129));
			txtTriggersPro.setBackground(Color.black);
			txtTriggersPro.setForeground(Color.green);
			txtTriggersPro.setLocation(new Point(840, 129));
			txtTriggersPro.setSize(new Dimension(140, 20));
		}
		return txtTriggersPro;
	}
	

}
