package com.cpsc304.UI;

import com.cpsc304.JDBC.CourierDBC;
import com.cpsc304.JDBC.CustomerDBC;
import com.cpsc304.JDBC.RestaurantManagerDBC;
import com.cpsc304.JDBC.UserDBC;
import com.cpsc304.model.*;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CourierW extends JFrame{
    private Login l;
    private JPanel current;
    private JTextField na;
    private JTextField phone;
    private JTextField password;
    private JTextField from;
    private JTextField to;
    private JTextField id;
    private Map<Integer,Delivery> integerDeliveryMap = new HashMap<>();

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
        Set<Delivery> deliveries = ((Courier)currentUser).getDeliveries();
        OrderBtnListener l = new OrderBtnListener();
        if (deliveries!=null) {
            for (Delivery next : deliveries) {
                integerDeliveryMap.put(next.getOrderID(), next);
//                Button de = new Button(((Integer) next.getOrderID()).toString());
//                current.add(de);
//                de.addActionListener(l);
            }
        }else{
            integerDeliveryMap.put(0, new Delivery(null,0,null,null,0,OrderStatus.READY,null,null,0,null,(Courier) currentUser,null));
        }
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
                Integer.parseInt(evt);
                Integer id = Integer.parseInt(evt);
                Delivery delivery = integerDeliveryMap.get(id);
                new OrderDetail(delivery);
            }catch (Exception ev){
                new ErrorMsg("Please type in one integer only");
            }

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
            current.add(new Label("Date and Time submitted: "+delivery.getDate()+" "+delivery.getTime()));
            current.add(new Label("Ordered at: " + delivery.getRestOrderedAt()));
            Address des = delivery.getDest();
            if (des!=null) {
                current.add(new Label("Delivered to: " + des.getHouseNum() + " " + des.getStreet() + ", " + des.getCity() + " " + des.getProvince() + ", " + des.getPostalCode()));
            }
            current.add(new Label("Estimated arrival time: "+delivery.getArrivalTime()));

            status = new Label("Status: "+ delivery.getStatus().toString());
            current.add(status);
            if (canChange){
                change = new Button("Change status");
                current.add(change);
                change.addActionListener(new DetailBtnListener());
            }
            add(current);
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
        current.add(new Label("Or view the monthly average income report with Max/Min average specified"));
        Button max = new Button("View Max");
        Button min = new Button("View Min");
        max.addActionListener(new reportListener());
        min.addActionListener(new reportListener());
        current.add(max);
        current.add(min);
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
                Long.parseLong(fromDate);
                Long.parseLong(toDate);
            }catch (Exception ev){
                new ErrorMsg("Date format is incorrect. Please provide dates of format YYYYMMDD");
                return;
            }

            Long from = Long.parseLong(fromDate);
            Long to = Long.parseLong(toDate);
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
                            buildReport(new Date(from), new Date(to));
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                    }
                    break;
                case "View Max":
                    buildReport(new Date(from),new Date(to),1);
                    break;
                case "View Min":
                    buildReport(new Date(from),new Date(to),0);
                    break;
                case "View stats":
                    buildStats(new Date(from),new Date(to));
                    break;

            }
        }
    }
    private void buildStats(Date from, Date to){
        removeComponents(current);
        current.invalidate();
        current.revalidate();
        current.setLayout(null);
            current.setLayout(new FlowLayout());
        String tmp = new String(new char[100]);
        current.add(new Label(tmp.replace('\0','*')));
        current.add(new Label("Statistics for delivery fees in the specified time period"));
        current.add(new Label(tmp.replace('\0','*')));


    }
    private void buildReport(Date from, Date to, int max){
        Map<Integer,Double> income = CourierDBC.getMonthlyIncomes(from,to);
        int specify = 0;
        String m;
        if (max==1){
            m = "MAX";
            specify = CourierDBC.getMaxMonth(from,to);
        }else{
            m = "MIN";
            specify = CourierDBC.getMinMonth(from,to);
        }

        removeComponents(current);
        current.invalidate();
        current.revalidate();
        current.setLayout(null);
        current.setLayout(new BoxLayout(current,BoxLayout.PAGE_AXIS));

        String tmp = new String(new char[100]);
        current.add(new Label(tmp.replace('\0','*')));
        current.add(new Label("Monthly Income Report" + "("+m+")"));
        current.add(new Label(tmp.replace('\0','*')));
        DateFormatSymbols dfs = new DateFormatSymbols();
        String [] months = dfs.getMonths();
        if (income!=null) {
            for (Map.Entry<Integer, Double> next : income.entrySet()) {
                if (next.getKey() == specify) {
                    if (max == 1) {
                        current.add(new Label("Month: " + months[next.getKey() - 1] + " Income: " + next.getValue() + " (Max)"));
                    } else {
                        current.add(new Label("Month: " + months[next.getKey() - 1] + " Income: " + next.getValue() + " (Min)"));
                    }
                } else {
                    current.add(new Label("Month: " + months[next.getKey() - 1] + " Income: " + next.getValue()));
                }
            }
        }else{

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
                current.add(new Label("OrderID: " + next.getOrderID() + " delivered for restaurant " + next.getRestOrderedAt().getName() + " earning: " +((Delivery)next).getDeliveryFee()));
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
                            if (newNum.length()!=10){
                                new ErrorMsg("Phone number must be in Canadian format!");
                            }try{
                                Integer.parseInt(newNum);
                                currentUser.setName(newName);
                                currentUser.setPassword(newPw);
                                currentUser.setPhoneNum(newNum);
                                UserDBC.updateUserInfo(currentUser);
                                password.setEditable(false);
                                na.setEditable(false);
                                phone.setEditable(false);
                            }catch (Exception ev){
                                new ErrorMsg("Phone number contains letters? Incorrect");
                            }
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
