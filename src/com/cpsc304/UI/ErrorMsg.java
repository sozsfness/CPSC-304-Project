package com.cpsc304.UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class ErrorMsg extends Frame{
    public ErrorMsg(String msg){
        setLayout(new FlowLayout());
        setVisible(true);
        setSize(600,300);
        JPanel tmp = new JPanel(new FlowLayout());
        tmp.add(new Label(msg));
        add(tmp);
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
