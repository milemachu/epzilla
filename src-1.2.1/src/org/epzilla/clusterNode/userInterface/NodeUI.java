package org.epzilla.clusterNode.userInterface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import javax.swing.*;
import java.awt.Point;
import java.awt.Color;
import java.awt.Label;

public class NodeUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JScrollPane jScrollPane = null;
	private JScrollPane jScrollPane1 = null;
	private JScrollPane jScrollPane2 = null;
	private JTextArea jTextAreaStatus = null;
	private JTextArea jTextAreaTriggers = null;
	private JTextArea jTextAreaIPList = null;
	private Label label = null;
	private Label label1 = null;
	private Label label2 = null;

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
		this.setSize(790, 501);
		this.setLocation(new Point(100, 100));
		this.setContentPane(getJContentPane());
		this.setTitle("epZilla Cluster Node ");
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			label2 = new Label();
			label2.setBounds(new Rectangle(574, 25, 85, 20));
			label2.setText("Cluster IP List:");
			label1 = new Label();
			label1.setBounds(new Rectangle(36, 204, 132, 21));
			label1.setText("Cluster Trigger List:");
			label = new Label();
			label.setBounds(new Rectangle(38, 26, 114, 17));
			label.setText("Node Status:");
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getJScrollPane(), null);
			jContentPane.add(getJScrollPane1(), null);
			jContentPane.add(getJScrollPane2(), null);
			jContentPane.add(label, null);
			jContentPane.add(label1, null);
			jContentPane.add(label2, null);
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
			jScrollPane.setBounds(new Rectangle(35, 48, 516, 153));
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
			jScrollPane1.setBounds(new Rectangle(34, 225, 518, 203));
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
			jScrollPane2.setBounds(new Rectangle(573, 49, 168, 383));
			jScrollPane2.setViewportView(getJTextAreaIPList());
		}
		return jScrollPane2;
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

}  //  @jve:decl-index=0:visual-constraint="10,10"
