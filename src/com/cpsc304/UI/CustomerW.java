package com.cpsc304.UI;

import com.cpsc304.model.Customer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class CustomerW extends JFrame{
    private String id;
    private static CustomerW instance;
    private  Login l;
    private JPanel current;
    private Customer currentUser;
    CustomerW(String id,Login l){

        instance = this;
        this.id = id;
        this.l = l;
        //initializing basic view...
        setLayout(new FlowLayout());
        current = new JPanel(new FlowLayout());
        current.add(new Label("\n\n\n"));
        current.add(new Label("exploring the system......"));
        Container c = getContentPane();
        c.setPreferredSize(new Dimension(800,800));
        setResizable(false);
        pack();
        setVisible(true);
        //user info

        JPanel userINFO = new JPanel(new FlowLayout());
        btnListner b = new btnListner();
        Button l1 = new Button("History Order");
        l1.addActionListener(b);
        Button l2 = new Button("Order in Progress");
        l2.addActionListener(b);
        Button l3 = new Button("New Order");
        l3.addActionListener(b);
        Button l4 = new Button("My INFO");
        l4.addActionListener(b);
        Button l5 = new Button("Reports");
        l5.addActionListener(b);
        userINFO.add(l1);
        userINFO.add(l2);
        userINFO.add(l3);
        userINFO.add(l4);
        userINFO.add(l5);
        add(userINFO);
        add(current);
        addWindowListener(new windowListener());


    }
    class windowListener implements WindowListener {

        @Override
        public void windowOpened(WindowEvent e) {

        }

        @Override
        public void windowClosing(WindowEvent e) {
            l.setVisible(true);
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
        class btnListner implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                String st = e.getActionCommand();
                switch (st){
                    case "History Order":
                        removeComponents(current);
                        buildHistoryOrder(current);
                        break;
                    case "Order in Progress":
                        removeComponents(current);
                        buildCurrOrder(current);
                        break;
                    case "New Order":
                        removeComponents(current);
                        buildNewOrder(current);
                        break;
                    case "My INFO":
                        removeComponents(current);
                        buildINFO(current);
                        break;
                    case "Reports":
                        removeComponents(current);
                        buildReport(current);
                        break;
                }
            }
        }
        private void removeComponents(JPanel current){
            Component[] c = current.getComponents();
            for (Component next:c){
                current.remove(next);
            }
        }
        private void buildHistoryOrder(JPanel current){

        }
        private void buildCurrOrder(JPanel current){

        }
        private void buildNewOrder(JPanel current){

        }
        private void buildINFO(JPanel current){

        }
        private void buildReport(JPanel current){

        }
}
