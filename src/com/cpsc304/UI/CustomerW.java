package com.cpsc304.UI;

import com.cpsc304.JDBC.UserDBC;
import com.cpsc304.model.Customer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;


public class CustomerW extends JFrame{

    private static CustomerW instance;
    private  Login l;
    private JPanel current;
    private JTextField na;
    private JTextField phone;
    private JTextField password;
    private JTextField datef;
    private JTextField datet;
    private JTextField res;
    private JTextField food;
    private JTextField type;
    private JTextField rating;

    CustomerW(Login l){

        instance = this;

        this.l = l;
        //initializing basic view...
        setLayout(new FlowLayout());
        current = new JPanel(new FlowLayout());
        String tmp = new String(new char[80]);
        current.add(new Label(tmp.replace('\0','*')));

        Container c = getContentPane();
        c.setPreferredSize(new Dimension(800,800));
        setResizable(false);
        pack();
        setVisible(true);
        //user info

        JPanel userINFO = new JPanel(new FlowLayout());
        btnListner b = new btnListner();
        Button l1 = new Button("Order History");
        l1.addActionListener(b);
        Button l3 = new Button("New Order");
        l3.addActionListener(b);
        Button l4 = new Button("My INFO");
        l4.addActionListener(b);
        Button l5 = new Button("Reports");
        l5.addActionListener(b);
        userINFO.add(l1);
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
                    case "Order History":
                        removeComponents(current);
                        current.invalidate();
                        current.revalidate();
                        current.add(new Label("\n\n\n"));
                        beforeBuidlingHistoryOrder(current);
                        break;
                    case "New Order":

                        beforeBuildingNewOrder(current);
                        break;
                    case "My INFO":
                        buildINFO(current);
                        break;
                    case "Reports":
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


    class historyOrderListner implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String dateFrom = datef.getText();
            String dateTo = datet.getText();
            String resName = res.getText();
            buildHistoryOrder(current,dateFrom,dateTo,resName);
        }
    }



        private void beforeBuidlingHistoryOrder(JPanel current){
            current.setLayout(null);
            current.setLayout(new BorderLayout());
            current.add(new Label("            What past orders do you want to look at? Please provide the dates or name of the restaurant...\n"),BorderLayout.PAGE_START);

            datef = new JTextField("all",8);
            res = new JTextField("all",15);
            JPanel p1 = new JPanel(new FlowLayout());
            p1.add(new Label("date:"));
            p1.add(datef);
            datet = new JTextField("all",8);
            p1.add(new Label("to"));
            p1.add(datet);

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
        private void buildHistoryOrder(JPanel current,String dateF,String dateT, String resN){
            removeComponents(current);
            current.invalidate();
            current.revalidate();
            current.setLayout(null);
            current.setLayout(new BorderLayout());
            Label title = new Label("\t\t\t\t\t\t\tHistory Orders\t\t\t\t\t\t\t");
            current.add(title,BorderLayout.PAGE_START);
            //panel for dynamic list of orders
            JPanel orderNumbers = new JPanel(new FlowLayout());
            current.add(orderNumbers,BorderLayout.LINE_START);
            orderNumbers.add(new Label("order#"));
            //TODO:construct a jpanel containing dynamic # of orderids, which allows selection

            //panel for details of selected order
            JPanel details = new JPanel(new FlowLayout());
            details.add(new Label("details of order"));
            current.add(details,BorderLayout.LINE_END);
            //TODO:show details of order selected


        }


        private class orderListener implements ActionListener{

            @Override
            public void actionPerformed(ActionEvent e) {
                String evt = e.getActionCommand();
                switch (evt){
                    case "search":
                        //TODO:rebuild current
                        break;
                    case "submit":
                        //TODO:submit order
                        break;
                }
            }
        }


        //build the panel for adding new order(search,recommend,etc.)
        private void beforeBuildingNewOrder(JPanel current){
            removeComponents(current);
            current.invalidate();
            current.revalidate();
            current.setLayout(null);
            current.setLayout(new BoxLayout(current,BoxLayout.PAGE_AXIS));
            String tmp = new String(new char[80]);
            current.add(new Label(tmp.replace('\0','*')));

            current.add(new Label("New orders? Searching for restaurants..."));
            //panel for food list
            JPanel f = new JPanel(new FlowLayout(FlowLayout.LEFT));

            current.add(f);
            f.add(new Label("Food: "));
            food = new JTextField("all",15);
            f.add(food);
            f.add(new Label("(separate multiple food names with commas)"));
            //panel for type
            JPanel t = new JPanel(new FlowLayout(FlowLayout.LEFT));
            current.add(t);
            t.add(new Label("Type: "));
            type = new JTextField("all",8);
            t.add(type);
            //panel for rating
            JPanel r = new JPanel(new FlowLayout(FlowLayout.LEFT));
            current.add(r);
            r.add(new Label("rating(1 to 5): "));
            rating = new JTextField("all");
            r.add(rating);
            //button for submitting order search
            Button submitSearch = new Button("search");
            submitSearch.addActionListener(new orderListener());
            current.add(submitSearch);

        }
        //build the panel for viewing user info

        private class infoListener implements ActionListener{

            @Override
            public void actionPerformed(ActionEvent e) {
                String evt = e.getActionCommand();
                switch (evt){
                    case "click to view/hide":
                        current.invalidate();
                        current.revalidate();
                        if (password.isVisible()){
                            password.setVisible(false);
                        }else {
                            password.setVisible(true);
                        }
                        break;
                    case "edit my info/submit":
                        current.invalidate();
                        current.revalidate();
                        if (na.isEditable()){
                            String newName = na.getText();
                            String newNum = phone.getText();
                            String newPw = password.getText();
                            MainUI.currentUser.setName(newName);
                            MainUI.currentUser.setPassword(newPw);
                            MainUI.currentUser.setPhoneNum(newNum);
                            UserDBC.updateUserInfo(MainUI.currentUser);
                            password.setEditable(false);
                            na.setEditable(false);
                            phone.setEditable(false);
                        }else {
                            password.setEditable(true);
                            na.setEditable(true);
                            phone.setEditable(true);
                        }
                        break;
                }
            }
        }

        private void buildINFO(JPanel current){
            removeComponents(current);
            current.invalidate();
            current.revalidate();
            current.setLayout(null);
            current.setLayout(new BoxLayout(current,BoxLayout.PAGE_AXIS));
            String tmp = new String(new char[80]);
            current.add(new Label(tmp.replace('\0','*')));
            current.add(new Label("\t\t\t\t\t\t\tmy INFO\t\t\t\t\t\t\t"));
            Customer c = (Customer) MainUI.currentUser;
            //panel for id
            JPanel id = new JPanel(new FlowLayout());
            current.add(id);
            id.add(new Label("user id:"));
            JTextField uid = new JTextField(((Integer)c.getUserID()).toString(),15);
            uid.setEditable(false);
            id.add(uid);
            //panel for name
            JPanel n  = new JPanel(new FlowLayout());
            n.add(new Label("name:"));
            current.add(n);
            na = new JTextField(c.getName(),15);
            na.setEditable(false);
            n.add(na);
            //panel for phone number
            JPanel p  = new JPanel(new FlowLayout());
            p.add(new Label("phone number:"));
            current.add(p);
            phone = new JTextField(c.getPhoneNum(),11);
            phone.setEditable(false);
            p.add(phone);
            //panel for password
            JPanel pw = new JPanel(new FlowLayout());
            pw.add(new Label("password:"));
            current.add(pw);
            password = new JTextField(c.getPassword(),15);
            password.setEditable(false);
            password.setVisible(false);
            pw.add(password);
            Button pwBtn = new Button("click to view/hide");
            pw.add(pwBtn);
            pwBtn.addActionListener(new infoListener());

            //panel for vip level and points
            JPanel vp = new JPanel(new FlowLayout());
            vp.add(new Label("current points: "+c.getPoints()));
            vp.add(new Label("  current VIP level: "+c.getVipLevel()));
            current.add(vp);
            //button for editing info
            Button edit = new Button("edit my info/submit");
            current.add(edit);
            edit.addActionListener(new infoListener());

        }
        //build the panel for viewing report
        private void buildReport(JPanel current){
            //TODO:buildreport
            removeComponents(current);
            current.invalidate();
            current.revalidate();
        }
}
