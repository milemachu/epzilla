package org.epzilla.ui;

import java.awt.*;
import javax.swing.*;

public class Login extends JFrame {

	private static final long serialVersionUID = 1L;
	private JLabel lblUName = null;
	private JLabel lblPassword = null;
	private JTextField tbUName = null;
	private JPasswordField tbPassword = null;
	private JLabel lblDetails = null;
	private JButton btnOK = null;
	private JButton btnCancel = null;
	private JButton btnHelp = null;
	private JButton btnClose = null;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Login thisClass = new Login();
				thisClass.setVisible(true);
			}
		});
	}
	public Login() {
		super();
		initialize();
	}
	private JTextField getTbUName() {
		if (tbUName == null) {
			tbUName = new JTextField(25);
			tbUName.setBounds(new Rectangle(120, 55, 226, 20));
			tbUName.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		}
		return tbUName;
	}
	private JPasswordField getTbPassword() {
		if (tbPassword == null) {
			tbPassword = new JPasswordField(25);
			tbPassword.setBounds(new Rectangle(122, 98, 224, 20));
			tbPassword.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		}
		return tbPassword;
	}
	private JButton getBtnOK() {
		if (btnOK == null) {
			btnOK = new JButton();
			btnOK.setLocation(new Point(151, 132));
			btnOK.setText("Ok");
			btnOK.setSize(new Dimension(80, 20));
			btnOK.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					showMainUI();
					hide();
				}
			});
		}
		return btnOK;
	}
	private void showMainUI(){
		ClientUI client = new ClientUI();
		client.start();
	}
	private JButton getBtnCancel() {
		if (btnCancel == null) {
			btnCancel = new JButton();
			btnCancel.setLocation(new Point(241, 132));
			btnCancel.setText("Cancel");
			btnCancel.setSize(new Dimension(80, 20));
			btnCancel.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
				}
			});
		}
		return btnCancel;
	}
	private JButton getBtnHelp() {
		if (btnHelp == null) {
			btnHelp = new JButton();
			btnHelp.setBounds(new Rectangle(2, 162, 31, 31));
			btnHelp.setBackground(new Color(238, 238, 238));
			btnHelp.setIcon(new ImageIcon("images//iconHelp.JPG"));
			btnHelp.setBorderPainted(false);
			btnHelp.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
				}
			});
		}
		return btnHelp;
	}
	private JButton getBtnClose() {
		if (btnClose == null) {
			btnClose = new JButton();
			btnClose.setBounds(new Rectangle(374, 3, 22, 18));
			btnClose.setIcon(new ImageIcon("images//iconClose.JPG"));
			btnClose.setBorderPainted(false);
			btnClose.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.exit(0);
				}
			});
		}
		return btnClose;
	}
	private void initialize() {
		lblDetails = new JLabel();
		lblDetails.setBounds(new Rectangle(171, 14, 55, 23));
		lblDetails.setFont(new Font("Dialog", Font.BOLD, 14));
		lblDetails.setText("  Login");
		lblPassword = new JLabel();
		lblPassword.setBounds(new Rectangle(20, 98, 83, 16));
		lblPassword.setText("  Password :");
		lblUName = new JLabel();
		lblUName.setText("  User Name :");
		lblUName.setBounds(new Rectangle(18, 57, 84, 19));
		
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setSize(new Dimension(399, 193));
		panel.setEnabled(true);
		panel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		panel.add(lblUName, null);
		panel.add(lblPassword, null);
		panel.add(getTbUName(), null);
		panel.add(getTbPassword(), null);
		panel.add(lblDetails, null);
		panel.add(getBtnOK(), null);
		panel.add(getBtnCancel(), null);
		panel.add(getBtnHelp(), null);
		panel.add(getBtnClose(), null);
        int width = 400;
        int height =200;
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width-width)/2;
        int y = (screen.height-height)/2;
        setBounds(x,y,width,height);
        this.setUndecorated(true);
        this.getContentPane().add(panel);  
        setVisible(true); 
        this.setTitle("login");
	}
}
