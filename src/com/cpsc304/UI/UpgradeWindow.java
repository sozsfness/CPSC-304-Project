package com.cpsc304.UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class UpgradeWindow extends Frame{
    public UpgradeWindow(int fromLevel, int toLevel){
        setLayout(new FlowLayout());
        setVisible(true);
        setSize(500,300);

        String tmp = new String(new char[80]);

        setTitle("Upgrade Notice");
        JPanel up = new JPanel(new FlowLayout());
        add(up);
        up.add(new Label(tmp.replace('\0','*')));
        up.add(new Label("Congratulations! You've reached vip level "+toLevel+" from level "+fromLevel+" !"));
        up.add(new Label(tmp.replace('\0','*')));
        addWindowListener(new WIN());

    }
    private class WIN implements WindowListener{

        @Override
        public void windowOpened(WindowEvent e) {

        }

        @Override
        public void windowClosing(WindowEvent e) {
            dispose();
        }

        @Override
        public void windowClosed(WindowEvent e) {

        }

        @Override
        public void windowIconified(WindowEvent e) {

        }

        @Override
        public void windowDeiconified(WindowEvent e) {

        }

        @Override
        public void windowActivated(WindowEvent e) {

        }

        @Override
        public void windowDeactivated(WindowEvent e) {

        }
    }
}
