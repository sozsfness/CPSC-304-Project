package com.cpsc304.UI;

import com.cpsc304.model.Customer;
import com.cpsc304.model.Order;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;

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
                        System.out.println("hisorderpressed");
                        removeComponents(current);
                        current.invalidate();
                        current.revalidate();
                        current.add(new Label("\n\n\n"));
                        beforeBuidlingHistoryOrder(current);

                        break;
                    case "Order in Progress":
                        current = buildCurrOrder();
                        break;
                    case "New Order":
                        current = buildNewOrder();
                        break;
                    case "My INFO":
                        current = buildINFO();
                        break;
                    case "Reports":
                        current = buildReport();
                        break;
                }
            }
        }


    class historyOrderListner implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            buildHistoryOrder(current);
        }
    }


        private void removeComponents(JPanel current){
            Component[] c = current.getComponents();
            for (Component next:c){
                current.remove(next);
            }
        }
        private void beforeBuidlingHistoryOrder(JPanel current){
            current.setLayout(null);
            current.setLayout(new BorderLayout());
            current.add(new Label("            What past orders do you want to look at? Please provide the date or name of the restaurant...\n"),BorderLayout.PAGE_START);

            JTextField dates = new JTextField("all",8);
            JTextField res = new JTextField("all",15);
            JPanel p1 = new JPanel(new FlowLayout());
            p1.add(new Label("date:"));
            p1.add(dates);
            current.add(p1,BorderLayout.LINE_START);
            JPanel p2 = new JPanel(new FlowLayout());
            p2.add(new Label("restaurant:"));
            p2.add(res);
            current.add(p2,BorderLayout.CENTER);
            Button b = new Button("submit");
            b.addActionListener(new historyOrderListner());
            current.add(b,BorderLayout.LINE_END);


        }

        //build the panel for viewing history order
        private void buildHistoryOrder(JPanel current){
            removeComponents(current);
            current.invalidate();
            current.revalidate();
            current.setLayout(null);
            current.setLayout(new BorderLayout());
            Label title = new Label("\t\t\t\t\t\t\tHistory Orders\t\t\t\t\t\t\t");
            current.add(title,BorderLayout.PAGE_START);
            JPanel orderNumbers = new JPanel(new FlowLayout());
            current.add(orderNumbers,BorderLayout.LINE_START);
            orderNumbers.add(new Label("order#"));
            JPanel dates = new JPanel(new FlowLayout());
            dates.add(new Label("date"));
            current.add(dates,BorderLayout.CENTER);
            JPanel details = new JPanel(new FlowLayout());
            details.add(new Label("details of order"));
            current.add(details,BorderLayout.LINE_END);


        }
        //build the panel for viewing current order
        private JPanel buildCurrOrder(){
            JPanel toret = new JPanel(new FlowLayout());
            return toret;
        }
        //build the panel for adding new order(search,recommend,etc.)
        private JPanel buildNewOrder(){
            JPanel toret = new JPanel(new FlowLayout());
            return toret;
        }
        //build the panel for viewing user info
        private JPanel buildINFO(){
            JPanel toret = new JPanel(new FlowLayout());
            return toret;
        }
        //build the panel for viewing report
        private JPanel buildReport(){
            JPanel toret = new JPanel(new FlowLayout());
            return toret;
        }
}
