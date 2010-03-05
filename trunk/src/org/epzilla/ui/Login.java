package org.epzilla.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import java.awt.Rectangle;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.Font;

public class Login extends JFrame implements ActionListener{

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

	public Login() {
		super();
		initialize();
	}
	private JTextField getTbUName() {
		if (tbUName == null) {
			tbUName = new JTextField(25);
			tbUName.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			tbUName.setSize(new Dimension(175, 20));
			tbUName.setLocation(new Point(120, 55));
		}
		return tbUName;
	}
	private JPasswordField getTbPassword() {
		if (tbPassword == null) {
			tbPassword = new JPasswordField(25);
			tbPassword.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			tbPassword.setSize(new Dimension(175, 20));
			tbPassword.setLocation(new Point(122, 98));
		}
		return tbPassword;
	}
	private JButton getBtnOK() {
		if (btnOK == null) {
			btnOK = new JButton();
			btnOK.setLocation(new Point(129, 131));
			btnOK.setText("Ok");
			btnOK.setSize(new Dimension(75, 20));
			btnOK.addActionListener(this);
		}
		return btnOK;
	}
	private void showMainUI(){
		new ClientUIControler();
		ClientUIControler.initializeClientUI();
	}
	private JButton getBtnCancel() {
		if (btnCancel == null) {
			btnCancel = new JButton();
			btnCancel.setLocation(new Point(216, 132));
			btnCancel.setText("Cancel");
			btnCancel.setPreferredSize(new Dimension(75, 26));
			btnCancel.setSize(new Dimension(75, 20));
			btnCancel.addActionListener(this);
			}
		return btnCancel;
	}
	private JButton getBtnHelp() {
		if (btnHelp == null) {
			btnHelp = new JButton();
			btnHelp.setBounds(new Rectangle(3, 145, 31, 31));
			btnHelp.setBackground(new Color(238, 238, 238));
			btnHelp.setIcon(new ImageIcon("images//iconHelp.JPG"));
			btnHelp.setBorderPainted(false);
			btnHelp.addActionListener(this);
			}
		return btnHelp;
	}
	private JButton getBtnClose() {
		if (btnClose == null) {
			btnClose = new JButton();
			btnClose.setBounds(new Rectangle(322, 3, 25, 22));
			btnClose.setIcon(new ImageIcon("images//iconClose.JPG"));
			btnClose.addActionListener(this);
		}
		return btnClose;
	}
	private void initialize() {
		lblDetails = new JLabel();
		lblDetails.setBounds(new Rectangle(142, 5, 55, 23));
		lblDetails.setFont(new Font("Dialog", Font.BOLD, 18));
		lblDetails.setText("Login");
		lblPassword = new JLabel();
		lblPassword.setText("Password :");
		lblPassword.setLocation(new Point(27, 98));
		lblPassword.setSize(new Dimension(80, 20));
		lblUName = new JLabel();
		lblUName.setText("User Name :");
		lblUName.setSize(new Dimension(80, 20));
		lblUName.setLocation(new Point(27, 57));
		
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setSize(new Dimension(350, 175));
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
        int width = 350;
        int height =175;
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width-width)/2;
        int y = (screen.height-height)/2;
        setBounds(x,y,width,height);
        this.setUndecorated(true);
        this.getContentPane().add(panel);  
        setVisible(true); 
        this.setTitle("login");
	}
	@Override
	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		if(source==btnHelp){
			
		}else if(source==btnCancel){
			tbUName.setText("");
			tbPassword.setText("");
		}else if(source == btnClose){
			System.exit(0);
		}else if(source ==btnOK){
			showMainUI();
			this.hide();
		}
	}
}
