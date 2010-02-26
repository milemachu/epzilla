package org.epzilla.ui;

import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.GridBagLayout;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JButton;
import java.awt.Point;
import javax.swing.ImageIcon;

public class About extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JPanel topPanel = null;
	private JPanel bottomPanel = null;
	private JLabel lblLogo = null;
	private JTextPane detailsTextPane = null;
	private JButton btnOK = null;

	private JPanel getTopPanel() {
		if (topPanel == null) {
			lblLogo = new JLabel();
			lblLogo.setText("logo image");
			lblLogo.setSize(new Dimension(170, 84));
			lblLogo.setIcon(new ImageIcon("images//logo.JPG"));
			lblLogo.setLocation(new Point(1, -1));
			topPanel = new JPanel();
			topPanel.setLayout(null);
			topPanel.setBounds(new Rectangle(-3, -2, 497, 174));
			topPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
			topPanel.add(lblLogo, null);
			topPanel.add(getDetailsTextPane(), null);
		}
		return topPanel;
	}
	private JPanel getBottomPanel() {
		if (bottomPanel == null) {
			bottomPanel = new JPanel();
			bottomPanel.setLayout(null);
			bottomPanel.setBounds(new Rectangle(1, 177, 469, 50));
			bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
			bottomPanel.add(getBtnOK(), null);
		}
		return bottomPanel;
	}
	private JTextPane getDetailsTextPane() {
		if (detailsTextPane == null) {
			detailsTextPane = new JTextPane();
			detailsTextPane.setBounds(new Rectangle(172, -4, 302, 177));
			detailsTextPane.setEditable(false);
			detailsTextPane.setText("");
		}
		return detailsTextPane;
	}
	private JButton getBtnOK() {
		if (btnOK == null) {
			btnOK = new JButton();
			btnOK.setBounds(new Rectangle(400, 12, 54, 25));
			btnOK.setText("OK");
			btnOK.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					hide();
				}
			});
		}
		return btnOK;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				About thisClass = new About();
				thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				thisClass.setVisible(true);
			}
		});
	}
	public About() {
		super();
		initialize();
	}
	private void initialize() {
		int width = 500;
	    int height =325;
	    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
	    int x = (screen.width-width)/2;
	    int y = (screen.height-height)/2;
	    setBounds(x,y,width,height);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage("C:/eclipse/ProjectEpzilla/images/logo.JPG"));
		this.setSize(new Dimension(475, 258));
		this.setResizable(false);
		this.setContentPane(getJContentPane());
		this.setTitle("About epZilla");
	}
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getTopPanel(), null);
			jContentPane.add(getBottomPanel(), null);
		}
		return jContentPane;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
