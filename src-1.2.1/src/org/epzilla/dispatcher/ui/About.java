package org.epzilla.dispatcher.ui;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class About extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    private JPanel jContentPane = null;
    private JPanel topPanel = null;
    private JPanel bottomPanel = null;
    private JLabel lblLogo = null;
    private JButton btnOK = null;
    private JTextPane jTextPane = null;

    private JPanel getTopPanel() {
        if (topPanel == null) {
            lblLogo = new JLabel();
            lblLogo.setSize(new Dimension(200, 85));
            lblLogo.setIcon(new ImageIcon("images\\logo.JPG"));
            lblLogo.setLocation(new Point(1, -1));
            topPanel = new JPanel();
            topPanel.setLayout(null);
            topPanel.setBounds(new Rectangle(-1, -1, 476, 175));
            topPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
            topPanel.add(lblLogo, null);
            topPanel.add(getJTextPane(), null);
        }
        return topPanel;
    }

    private JPanel getBottomPanel() {
        if (bottomPanel == null) {
            bottomPanel = new JPanel();
            JLabel lblcpyR = new JLabel("  Copyright © epZilla Team  2009-2010. All Rights Reserved");
            lblcpyR.setSize(400, 20);
            bottomPanel.setLayout(null);
            bottomPanel.setBounds(new Rectangle(0, 177, 469, 50));
            bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            bottomPanel.add(getBtnOK(), null);
            bottomPanel.add(lblcpyR);
        }
        return bottomPanel;
    }

    private JButton getBtnOK() {
        if (btnOK == null) {
            btnOK = new JButton();
            btnOK.setBounds(new Rectangle(390, 12, 50, 25));
            btnOK.setText("OK");
            btnOK.addActionListener(this);
        }
        return btnOK;
    }

    private JTextPane getJTextPane() {
        if (jTextPane == null) {
            jTextPane = new JTextPane();
            jTextPane.setBounds(new Rectangle(172, 5, 275, 168));
            jTextPane.setEditable(false);
            jTextPane.setText("epZilla, Scalable Fault Tolerant Architecture for Complex Event processing Systems" + "\n" +
                    "\n" + "More information visit:" + "\n" + "http://www.epzilla.net");
        }
        return jTextPane;
    }

    public About() {
        super();
        initialize();
    }

    private void initialize() {
        int width = 500;
        int height = 325;
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width - width) / 2;
        int y = (screen.height - height) / 2;
        setBounds(x, y, width, height);
        Image img = Toolkit.getDefaultToolkit().getImage("images\\logo.jpg");
        this.setIconImage(img);
        this.setSize(new Dimension(450, 258));
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

    @Override
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        if (source == btnOK) {
            this.setVisible(false);
        }

    }
}
