package org.epzilla.client.userInterface;

import org.epzilla.client.controlers.ClientUIControler;

import javax.swing.*;

import java.awt.*;
import java.awt.Color;
import java.awt.SystemColor;


public class SplashScreen extends JWindow {

    private static final long serialVersionUID = 1L;
    private int duration;
    private JProgressBar progressBar;
    private static int num;

    public SplashScreen(int d) {
        duration = d;
    }

    public void showSplash() { 
    	
        JPanel panel = new JPanel();
        int width = 425;
        int height = 240;
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width - width) / 2;
        int y = (screen.height - height) / 2;
        setBounds(x, y, width, height);

        ImageIcon img = new ImageIcon("images\\header.jpg");
        JLabel label = new JLabel();
        label.setBounds(new Rectangle(5, 5, 414, 197));
        label.setIcon(img);
        JLabel copyrt = new JLabel("Copyright@epZilla Team  2009-2010, All Rights Reserved", JLabel.CENTER);
        copyrt.setFont(new Font("Cambria", Font.BOLD, 12));
        copyrt.setForeground(Color.white);
        copyrt.setBounds(new Rectangle(16, 207, 398, 19));
        copyrt.setVisible(true);
        panel.setLayout(null);
        panel.setBackground(Color.gray);
        panel.setSize(new Dimension(419, 250));
        panel.add(label, null);
        panel.add(copyrt, null);
        panel.add(getProgressBar(), null);
        this.getContentPane().add(panel);
        setVisible(true);
        iterate();
        setVisible(false);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
       }
       catch (UnsupportedLookAndFeelException e) {}
       catch (ClassNotFoundException e) {}
       catch (InstantiationException e) {}
       catch (IllegalAccessException e) {}
       
    }

    public void iterate() {
        while (num < duration) {
            progressBar.setValue(num);
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
            }
            num += 100;
        }
    }

    private void showSplashAndExit() {
        showSplash();
        showMainUI();
    }

    private void showMainUI() {
        new ClientUIControler();
        ClientUIControler.initializeClientUI();
    }

    private JProgressBar getProgressBar() {
        if (progressBar == null) {
            progressBar = new JProgressBar(0, duration);
            progressBar.setBounds(new Rectangle(0, 228, 425, 10));
            progressBar.setValue(0);
            progressBar.setBackground(SystemColor.control);
            progressBar.setForeground(new Color(88, 224, 52));
            progressBar.setVisible(true);
        }
        return progressBar;
    }

    public static void main(String[] args) {
        SplashScreen splash = new SplashScreen(3000);
        splash.showSplashAndExit();
    }
}


