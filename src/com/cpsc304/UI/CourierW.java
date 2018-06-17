package com.cpsc304.UI;

import com.cpsc304.JDBC.CourierDBC;
import com.cpsc304.JDBC.CustomerDBC;
import com.cpsc304.JDBC.UserDBC;
import com.cpsc304.model.*;
import javafx.util.Pair;

import static com.cpsc304.UI.MainUI.currentUser;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Date;
import java.sql.SQLException;
import java.text.DateFormatSymbols;
import java.util.*;
import java.util.List;

public class CourierW extends JFrame{
    private Login l;
    private JPanel current;
    private JTextField na;
    private JTextField phone;
    private JTextField password;
    private JTextField from;
    private JTextField to;
    private JTextField id;
    private Map<Long,Delivery> integerDeliveryMap = new HashMap<>();
    private Choice typeChooser;
    private Choice a1;
    private Choice a2;
    private boolean isInS;

    public CourierW(Login l){
        this.l = l;
        //initializing basic view
        setLayout(new FlowLayout());
        current = new JPanel(new FlowLayout());
        Container c = getContentPane();
        c.setPreferredSize(new Dimension(800,800));
        pack();
        setVisible(true);
        setTitle("Courier");
        //user info
        JPanel userINFO = new JPanel(new FlowLayout());
        add(userINFO);
        add(current);
        ActionListener a1 = new btnListner();
        Button b1 = new Button("My INFO");
        userINFO.add(b1);
        b1.addActionListener(a1);

        Button b3 = new Button("Orders");
        userINFO.add(b3);
        b3.addActionListener(a1);
        Button b4 = new Button("Reports");
        userINFO.add(b4);
        b4.addActionListener(a1);
        pack();
        addWindowListener(new windowListener());
    }
    class windowListener implements WindowListener {

        @Override
        public void windowOpened(WindowEvent e) {
        }
        @Override
        public void windowClosing(WindowEvent e) {
            l.setVisible(true);
            MainUI.courierLogOut();
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
    class btnListner implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String st = e.getActionCommand();
            switch (st){

                case "Orders":
                    buildOrder();
                    break;
                case "My INFO":
                    buildINFO();
                    break;
                case "Reports":
                    beforeBuildingReport();
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
    private void buildOrder(){
        removeComponents(current);
        current.invalidate();
        current.revalidate();
        current.setLayout(null);
        current.setLayout(new BoxLayout(current,BoxLayout.PAGE_AXIS));
        String tmp = new String(new char[100]);
        current.add(new Label(tmp.replace('\0','*')));
        current.add(new Label("Orders to deliver"));
        current.add(new Label(tmp.replace('\0','*')));
//        current.add(new Label("Click on order id to see details/change status"));

        OrderBtnListener l = new OrderBtnListener();

        current.add(new Label("Please type in the order id to view the order"));
        id = new JTextField("",10);
        Button submit = new Button("Submit");
        submit.addActionListener(l);
        current.add(id);
        current.add(submit);

    }
    private class OrderBtnListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            String evt = id.getText();
            try{
                Long.parseLong(evt);

            }catch (Exception ev){
                new ErrorMsg("Please type in one integer only");
            }
            List<Order> deliveries = null;
            try {
                deliveries = CourierDBC.getOrders(Date.valueOf("2001-01-01"),Date.valueOf("2020-01-01"));
            } catch (SQLException e1) {
                new ErrorMsg(e1.getMessage());
            }

            if (deliveries!=null) {
                for (Order next : deliveries) {
                    integerDeliveryMap.put(next.getOrderID(), (Delivery) next);
//                Button de = new Button(((Integer) next.getOrderID()).toString());
//                current.add(de);
//                de.addActionListener(l);
                }
            }
            Long id = Long.parseLong(evt);
            Delivery delivery = integerDeliveryMap.get(id);
            if (delivery==null){
                new ErrorMsg("No delivery found. try another id");
                return;
            }
            new OrderDetail(delivery);

        }
    }
    private class OrderDetail extends JFrame{
        private Delivery delivery;
        private JPanel current;
        private boolean canChange = false;
        private Label status;
        JPanel sta;
        Button change;
        OrderDetail(Delivery d){
            //initializing basic view
            setLayout(new FlowLayout());
            current = new JPanel(new FlowLayout());
            add(current);
            Container c = getContentPane();
            c.setPreferredSize(new Dimension(500,500));
            setSize(500,500);
            setVisible(true);
            addWindowListener(new DetailWindowListener());
            this.delivery = d;
            current.setLayout(null);
            current.setLayout(new BoxLayout(current,BoxLayout.PAGE_AXIS));
            if (delivery.getStatus().equals(OrderStatus.READY)||delivery.getStatus().equals(OrderStatus.DELIVERING)){
                canChange = true;
            }
            current.add(new Label("Order Details"));
            current.add(new Label("Order id: "+delivery.getOrderID()));
            current.add(new Label("Date and Time : "+delivery.getDate()+" "+delivery.getTime()));
            current.add(new Label("Ordered at: " + delivery.getRestOrderedAt().getName()));
            Address des = delivery.getDest();
            if (des!=null) {
                current.add(new Label("DELIVERED to: " + des.getHouseNum() + " " + des.getStreet() + ", " + des.getCity() + " " + des.getProvince() + ", " + des.getPostalCode()));
            }
            current.add(new Label("Estimated arrival time: "+delivery.getArrivalTime()));

            status = new Label("Status: "+ delivery.getStatus().toString());
            current.add(status);
            if (canChange){
                change = new Button("Change status");
                current.add(change);
                change.addActionListener(new DetailBtnListener());
            }
            pack();

        }
        private class DetailBtnListener implements ActionListener{

            @Override
            public void actionPerformed(ActionEvent e) {
                current.invalidate();
                current.revalidate();
                current.remove(change);
                current.remove(status);
                OrderStatus s = delivery.getStatus() ;
                try {
                    if (delivery.getStatus().equals(OrderStatus.READY)){
                        s = OrderStatus.DELIVERING;
                        CustomerDBC.updateOrderStatus(delivery.getOrderID(),OrderStatus.DELIVERING);
                        delivery.setStatus(s);
                    }else {
                        s = OrderStatus.DELIVERED;
                        CustomerDBC.updateOrderStatus(delivery.getOrderID(), OrderStatus.DELIVERED);
                        delivery.setStatus(s);
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                status = new Label("Status: "+s);
                current.add(status);
                if (s.equals(OrderStatus.DELIVERING)){

                    current.add(change);
                }
            }
        }
    }
    private class DetailWindowListener implements WindowListener{

        @Override
        public void windowOpened(WindowEvent e) {

        }

        @Override
        public void windowClosing(WindowEvent e) {
//            dispose();
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

    private void beforeBuildingReport(){
        removeComponents(current);
        current.invalidate();
        current.revalidate();
        current.setLayout(null);
        current.setLayout(new BoxLayout(current,BoxLayout.PAGE_AXIS));
        String tmp = new String(new char[100]);
        current.add(new Label(tmp.replace('\0','*')));
        current.add(new Label("Reports"));
        current.add(new Label(tmp.replace('\0','*')));
        current.add(new Label("Please provide the time period for your report"));
        from = new JTextField("all",8);
        to = new JTextField("all",8);
        JPanel fromto = new JPanel(new FlowLayout());
        fromto.add(new Label("From "));
        fromto.add(from);
        fromto.add(new Label(" To "));
        fromto.add(to);
        current.add(fromto);
        Button submit = new Button("View Report");
        submit.addActionListener(new reportListener());
        current.add(submit);
        current.add(new Label(tmp.replace('\0','*')));
        current.add(new Label("Or view the monthly income report with Max/Min/Sum/Count/Average shown"));
        current.add(new Label("Max-view the maximal delivery fee in each month"));
        current.add(new Label("Min-similar to Max-minimal"));
        current.add(new Label("Sum-view total earning in each month"));
        current.add(new Label("Count-view number of deliveries in each month"));
        current.add(new Label("Average-average delivery fee in each month"));
        typeChooser = new Choice();
        typeChooser.add("Max");
        typeChooser.add("Min");
        typeChooser.add("Sum");
        typeChooser.add("Avg");
        typeChooser.add("Count");
        current.add(typeChooser);
        Button sb = new Button("View monthly income report");
        sb.addActionListener(new reportListener());
        current.add(sb);
        current.add(new Label(tmp.replace('\0','*')));
        current.add(new Label("Or view delivery fees over time per order per restaurant(grouped by restaurants) with statistics options "));
        Button nested = new Button("View stats");
        nested.addActionListener(new reportListener());
        current.add(nested);
    }
    private class reportListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            String fromDate = from.getText();
            String toDate = to.getText();
            String evt = e.getActionCommand();
            try{
                Date.valueOf(fromDate);
                Date.valueOf(toDate);
            }catch (Exception ev){
                new ErrorMsg("Date format is incorrect. Please provide dates of format YYYY-MM-DD");
                return;
            }

            Date from = Date.valueOf(fromDate);
            Date to = Date.valueOf(toDate);
            switch (evt) {
                case "View Report":

                    if (fromDate.equals("all") && toDate.equals("all")) {
                        try {
                            buildReport(null, null);
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                    } else {

                        try {
                            buildReport(from, to);
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                    }
                    break;
                case "View monthly income report":
                    buildReport(from, to,typeChooser.getSelectedItem());
                    break;
                case "View stats":
                    if (!isInS) {
                        statsOptions();
                    }
                    break;
                case "stats":
                    isInS = false;
                    buildStats(from, to);

            }
        }
    }
    private void statsOptions(){
        isInS= true;
        current.invalidate();
        current.revalidate();
        current.add(new Label("Please select options of step 1 and 2 for statistics."));
        current.add(new Label("i.e. sum+sum calculates sum of all monthly incomes for each restaurant, then the sum of the calculated results"));
        a1 = new Choice();
        a2 = new Choice();
        JPanel temp = new JPanel(new FlowLayout());
        current.add(temp);
        temp.add(a1);
        temp.add(a2);
        a1.add("Max");
        a1.add("Min");
        a1.add("Sum");
        a1.add("Avg");
        a1.add("Count");
        a2.add("Max");
        a2.add("Min");
        a2.add("Sum");
        a2.add("Avg");
        a2.add("Count");
        Button s = new Button("Submit");
        s.setActionCommand("stats");
        s.addActionListener(new reportListener());
        current.add(s);

    }
    private void buildStats(Date from, Date to){

        removeComponents(current);
        current.invalidate();
        current.revalidate();
        current.setLayout(null);
            current.setLayout(new BoxLayout(current,BoxLayout.PAGE_AXIS));
        String tmp = new String(new char[100]);
        current.add(new Label(tmp.replace('\0','*')));
        current.add(new Label("Statistics for delivery fees in the specified time period"));
        current.add(new Label("From "+from+ " To "+to));
        current.add(new Label(tmp.replace('\0','*')));
        current.add(new Label("Selected options: "+a1.getSelectedItem()+" with "+a2.getSelectedItem()));
        if (!a1.getSelectedItem().equals("Count")&&!a2.getSelectedItem().equals("Count")){
            try {
                current.add(new Label("Result: "+CourierDBC.getEarning(a1.getSelectedItem(),a2.getSelectedItem(),currentUser.getUserID())));
            } catch (SQLException e) {
                new ErrorMsg(e.getMessage());
                return;
            }
        }else{
            if (a1.getSelectedItem().equals("Count")){
                try {
                    current.add(new Label("Result: "+CourierDBC.getFirstCount(a2.getSelectedItem(),currentUser.getUserID())));
                } catch (SQLException e) {
                    new ErrorMsg(e.getMessage());
                }
            }else{
                try {
                    current.add(new Label("Result: "+CourierDBC.getSecondCount(a1.getSelectedItem(),currentUser.getUserID())));
                } catch (SQLException e) {
                    new ErrorMsg(e.getMessage());
                }
            }
        }


    }
    private void buildReport(Date from, Date to, String type){
        Map<Integer,Double> income = CourierDBC.getMonthlyIncomes(from,to);
        List<Pair<Integer, Double>> specify = new ArrayList<>();
        List<Pair<Integer,Integer>> sp = null;
        switch (type){
            case "Max":
                try {
                    specify = CourierDBC.getMaxs(from,to);
                } catch (SQLException e) {
                    new ErrorMsg(e.getMessage());
                }
                break;
            case "Min":
                try {
                    specify = CourierDBC.getMins(from,to);
                } catch (SQLException e) {
                    new ErrorMsg(e.getMessage());
                    return;
                }
                break;
            case "Sum":
                try {
                    specify = CourierDBC.getSums(from,to);
                } catch (SQLException e) {
                    new ErrorMsg(e.getMessage());
                    return;
                }
                break;
            case "Avg":
                try {
                    specify = CourierDBC.getAvgs(from,to);
                } catch (SQLException e) {
                    new ErrorMsg(e.getMessage());
                    return;
                }
                break;
            case "Count":
                try {
                    sp = CourierDBC.getCounts(from,to);
                } catch (SQLException e) {
                    new ErrorMsg(e.getMessage());
                    return;
                }
                break;
        }


        removeComponents(current);
        current.invalidate();
        current.revalidate();
        current.setLayout(null);
        current.setLayout(new BoxLayout(current,BoxLayout.PAGE_AXIS));

        String tmp = new String(new char[100]);
        current.add(new Label(tmp.replace('\0','*')));
        current.add(new Label("Monthly Income Report" + "("+type+")"));
        current.add(new Label(tmp.replace('\0','*')));
        DateFormatSymbols dfs = new DateFormatSymbols();
        String [] months = dfs.getMonths();
        if (income!=null) {
            if (sp==null) {
                for (Pair<Integer, Double> next : specify) {

                    current.add(new Label("Month: " + months[next.getKey() - 1] + " Data: " + income.get(next.getKey()) + type + ": " + next.getValue()));

                }
            }else{
                for (Pair<Integer, Integer> next : sp) {

                    current.add(new Label("Month: " + months[next.getKey() - 1] + " Data: " + income.get(next.getKey()) + type + ": " + next.getValue()));

                }
            }
        }else{
                current.add(new Label("record not found. Change time period?"));
        }
    }
    private void buildReport(Date from, Date to) throws SQLException {
        List<Order> orders = CourierDBC.getOrders(from,to);
        removeComponents(current);
        current.invalidate();
        current.revalidate();
        current.setLayout(null);
        current.setLayout(new BoxLayout(current,BoxLayout.PAGE_AXIS));
        String tmp = new String(new char[100]);
        current.add(new Label(tmp.replace('\0','*')));
        current.add(new Label("REPORT FOR Courier "+ currentUser.getUserID()+ " FROM DATE "+from+" TO "+to));
        current.add(new Label(tmp.replace('\0','*')));
        if (orders!=null) {
            for (Order next : orders) {
                current.add(new Label("OrderID: " + next.getOrderID() + " DELIVERED for restaurant " + next.getRestOrderedAt().getName() +" On "+next.getDate() + " earning: " +((Delivery)next).getDeliveryFee()));
            }
        }
        current.add(new Label("Total income in selected time period: "+CourierDBC.getIncome(from,to)));

    }


    private void buildINFO(){
        removeComponents(current);
        current.invalidate();
        current.revalidate();
        current.setLayout(null);
        current.setLayout(new BoxLayout(current,BoxLayout.PAGE_AXIS));
        String tmp = new String(new char[100]);
        current.add(new Label(tmp.replace('\0','*')));
        current.add(new Label("My INFO"));
        current.add(new Label(tmp.replace('\0','*')));
        Courier c = (Courier) currentUser;
        //panel for id
        JPanel id = new JPanel(new FlowLayout());
        current.add(id);
        id.add(new Label("user id: "+c.getUserID()));

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


        //button for editing info
        Button edit = new Button("edit my info/submit");
        current.add(edit);
        edit.addActionListener(new infoListener());

    }
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
                        if (newPw.length()<6){
                            new ErrorMsg("Password must be longer than 6 characters!");
                        }else {
                            try{
                                Long.parseLong(newNum);

                            }catch (Exception ev){
                                new ErrorMsg("Phone number contains letters? Incorrect");
                                break;
                            }
                            currentUser.setName(newName);
                            currentUser.setPassword(newPw);
                            currentUser.setPhoneNum(newNum);
                            try {
                                UserDBC.updateUserInfo(currentUser);
                            } catch (SQLException e1) {
                                new ErrorMsg(e1.getMessage());
                            }
                            password.setEditable(false);
                            na.setEditable(false);
                            phone.setEditable(false);


                        }
                    }else {
                        password.setEditable(true);
                        na.setEditable(true);
                        phone.setEditable(true);
                    }
                    break;
            }
        }
    }
}
