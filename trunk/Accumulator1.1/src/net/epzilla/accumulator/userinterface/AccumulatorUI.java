package net.epzilla.accumulator.userinterface;

import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Dimension;
import javax.swing.JTextArea;
import java.awt.Rectangle;
import javax.swing.JLabel;
import java.awt.Point;
import javax.swing.JTextField;
import java.awt.Color;
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
			txtEventCount.setBounds(new Rectangle(458, 42, 140, 20));
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
			lblStatus = new JLabel();
			lblStatus.setText("Accumulator Status:");
			lblStatus.setSize(new Dimension(128, 20));
			lblStatus.setLocation(new Point(30, 44));
			lblEventCount = new JLabel();
			lblEventCount.setText("Event Count:");
			lblEventCount.setSize(new Dimension(80, 20));
			lblEventCount.setLocation(new Point(358, 42));
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(lblEventCount, null);
			jContentPane.add(lblStatus, null);
			jContentPane.add(getTxtAccStatus(), null);
			jContentPane.add(getTxtEventCount(), null);
			jContentPane.add(getResultScrollPane(), null);
		}
		return jContentPane;
	}

}
