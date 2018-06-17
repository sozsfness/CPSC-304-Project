package com.cpsc304.UI;

import com.cpsc304.JDBC.CustomerDBC;
import com.cpsc304.JDBC.RestaurantManagerDBC;
import com.cpsc304.JDBC.UserDBC;
import com.cpsc304.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Date;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.cpsc304.UI.MainUI.currentUser;

public class ManagerW extends JFrame {
    private Login l;
    private JPanel current;
    private JTextField na;
    private JTextField phone;
    private JTextField password;
    private JTextField from;
    private JTextField to;

    private Map<Integer,Restaurant> integerRestaurantMap;
    private Set<Restaurant> restaurants;
    Date fromDate;
    Date toDate;

    public ManagerW(Login l){
        this.l = l;

        restaurants = ((RestaurantManager)currentUser).getRestaurants();
        if (restaurants!=null) {
            for (Restaurant next : restaurants) {

                integerRestaurantMap.put(next.getId(), next);
            }
        }
        //initializing basic view
        setLayout(new FlowLayout());
        current = new JPanel(new FlowLayout());
        Container c = getContentPane();
        c.setPreferredSize(new Dimension(800,800));
        pack();
        setVisible(true);
        setTitle("Manager");
        //user info

        JPanel userINFO = new JPanel(new FlowLayout());
        add(userINFO);
        add(current);
        ActionListener a1 = new btnListner();
        Button b1 = new Button("My INFO");
        userINFO.add(b1);
        b1.addActionListener(a1);
        Button b2 = new Button("My restaurants");
        userINFO.add(b2);
        b2.addActionListener(a1);
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
            MainUI.managerLogOut();
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
                case "My restaurants":
                    removeComponents(current);
                    current.invalidate();
                    current.revalidate();
                    current.add(new Label("\n\n\n"));
                    buildRestaurants(current);
                    break;
                case "Orders":
                    buildOrder(current);
                    break;
                case "My INFO":
                    buildINFO(current);
                    break;
                case "Reports":
                    beforeBuildingReport(current);
                    break;
                case "delete":

                    try {
                        RestaurantManagerDBC.deleteRestaurant(Integer.parseInt(e.getActionCommand()));
                    } catch (SQLException e1) {
                        new ErrorMsg(e1.getMessage());
                        break;
                    }

                    removeComponents(current);
                    current.invalidate();
                    current.revalidate();
                    current.add(new Label("\n\n\n"));
                    buildRestaurants(current);
                    break;
//                case "Add new restaurant":
//                    new addNewRestaurant();
//                    break;
            }
        }
    }
    private void removeComponents(JPanel current){
        Component[] c = current.getComponents();
        for (Component next:c){
            current.remove(next);
        }
    }
//    private class addNewRestaurant extends Frame{
//        public addNewRestaurant(){
//            setLayout(new FlowLayout());
//            Container c = getContentPane();
//            c.setPreferredSize(new Dimension(500,500));
//            setVisible(true);
//            setSize(500,500);
//            add(new Label("Please provide the information of the restaurant you want to add"));
//            //Jpanel for info
//            JPanel info = new JPanel(new FlowLayout());
//            info.invalidate();
//            info.revalidate();
//            info.setLayout(null);
//            info.setLayout(new BoxLayout(info,BoxLayout.PAGE_AXIS));
//
//        }
//    }


    private void buildRestaurants(JPanel current){
        removeComponents(current);
        current.invalidate();
        current.revalidate();
        current.setLayout(null);
        current.setLayout(new BoxLayout(current,BoxLayout.PAGE_AXIS));
        integerRestaurantMap = new HashMap<>();
        String tmp = new String(new char[80]);
        current.add(new Label(tmp.replace('\0','*')));
        current.add(new Label("Restaurants"));
        current.add(new Label("Click on restaurant id to view/manipulate its menu"));
        btnListner b = new btnListner();
        menuListener m = new menuListener();

        if (restaurants!=null) {
            for (Restaurant next : restaurants) {
                JPanel p = new JPanel(new FlowLayout());
                current.add(p);
                p.add(new Label("Name: " + next.getName()+"ID: "));
                Button id = new Button(((Integer)next.getId()).toString());
                p.add(id);
                id.addActionListener(m);
                Button temp = new Button("delete");
//                temp.setName(((Integer) next.getId()).toString());
                temp.setActionCommand(((Integer) next.getId()).toString());
                p.add(temp);
                temp.addActionListener(b);
            }
        }else{
            JPanel p = new JPanel(new FlowLayout());
            current.add(p);
            p.add(new Label("Name: test " +"ID: "));
            Button id = new Button("0");
            p.add(id);
            id.addActionListener(m);
            Button temp = new Button("delete");
            temp.setActionCommand("0");
            p.add(temp);
            temp.addActionListener(b);
            integerRestaurantMap.put(0,new Restaurant((RestaurantManager) currentUser,0,null,null,null,0,false,null,null,null));
        }
//        Button addNew = new Button("Add new restaurant");
//        addNew.addActionListener(new btnListner());
//        current.add(addNew);
    }
    private class menuListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            Integer id = Integer.parseInt(e.getActionCommand());
            Restaurant toShow = integerRestaurantMap.get(id);
            showMenuForRes(toShow);
        }
    }

    private void showMenuForRes(Restaurant r){
        new Menu(r);
    }

    private class Menu extends Frame{
        Map<String,Food> stringFoodHashMap = new HashMap<>();
        Map<String,JPanel> stringJPanelMap = new HashMap<>();

        private Restaurant restaurant;
        private JPanel menu;
        private JPanel change;
        private JTextField na;
        private JTextField pr;
        private boolean isInAdding = false;
        public Menu(Restaurant r){
            restaurant = r;
            setLayout(new FlowLayout());

            setSize(500,800);
            Container c = getContentPane();
            c.setPreferredSize(new Dimension(500,800));
            setVisible(true);
            addWindowListener(new MenuWindowListener());
            menu = new JPanel(new FlowLayout());
            change = new JPanel(new FlowLayout());
            menu.invalidate();
            menu.revalidate();
            menu.setLayout(null);
            menu.setLayout(new BoxLayout(menu,BoxLayout.PAGE_AXIS));
            change.invalidate();
            change.revalidate();
            change.setLayout(null);
            change.setLayout(new BoxLayout(change,BoxLayout.PAGE_AXIS));



            String tmp = new String(new char[80]);
            menu.add(new Label(tmp.replace('\0','*')));
            menu.add(new Label("MENU"));
            add(menu);
            add(change);
            stringFoodHashMap = restaurant.getOffers()==null?stringFoodHashMap:restaurant.getOffers();
            MenuBtnListener listener = new MenuBtnListener();
            if (stringFoodHashMap!=null) {
                for (Map.Entry<String, Food> next : stringFoodHashMap.entrySet()) {
                    JPanel temp = new JPanel(new FlowLayout());
                    temp.add(new Label("Food name: "+next.getKey()+" Price: $"+next.getValue().getPrice()));
                    menu.add(temp);
                    Button del = new Button("delete food");
                    del.setActionCommand(next.getKey());
                    del.addActionListener(listener);
                    temp.add(del);
                    stringJPanelMap.put(next.getKey(),temp);
                }
            }

            Button add = new Button("add new food");
            add.addActionListener(listener);
            change.add(new Label(tmp.replace('\0','*')));
            change.add(add);

        }

        private class MenuBtnListener implements ActionListener{

            @Override
            public void actionPerformed(ActionEvent e) {
                String evt = e.getActionCommand();
                if (evt.equals("add new food")){

                    if (!isInAdding) {
                        isInAdding = true;

                        change.invalidate();
                        change.revalidate();
                        change.add(new Label("please provide the name and price for the food you want to add"));
                        JPanel name = new JPanel(new FlowLayout());
                        name.add(new Label("Name: "));
                        change.add(name);
                        na = new JTextField(10);
                        name.add(na);
                        JPanel price = new JPanel(new FlowLayout());
                        price.add(new Label("Price: "));
                        change.add(price);
                        pr = new JTextField(5);
                        price.add(pr);
                        change.add(new Label("click submit to confirm"));
                        Button sbmt = new Button("submit");
                        sbmt.setActionCommand("confirm");
                        sbmt.addActionListener(new MenuBtnListener());
                        change.add(sbmt);
                    }


                }else{
                    if (evt.equals("confirm")){
                        menu.invalidate();
                        menu.revalidate();
                        Food toA = new Food(na.getText(),restaurant,Double.parseDouble(pr.getText()));
                        try {
                            RestaurantManagerDBC.addToMenu(toA);
                        } catch (SQLException e1) {
                            new ErrorMsg(e1.getMessage());
                        }
                        JPanel temp = new JPanel(new FlowLayout());
                        try {
                            temp.add(new Label("Food name: " + na.getText() + " Price: $" + Double.parseDouble(pr.getText())));
                        }catch (Exception ex){
                            new ErrorMsg(ex.getMessage());
                        }
                        menu.add(temp);
                        Button del = new Button("delete food");
                        del.setActionCommand(na.getText());
                        stringFoodHashMap.put(na.getText(),toA);
                        del.addActionListener(new MenuBtnListener());
                        temp.add(del);
                        stringJPanelMap.put(na.getText(),temp);
                        isInAdding=false;
                        change.invalidate();
                        change.revalidate();
                        removeComponents(change);
                        Button add = new Button("add new food");
                        add.addActionListener(new MenuBtnListener());
                        String tmp = new String(new char[80]);
                        change.add(new Label(tmp.replace('\0','*')));
                        change.add(add);

                    }else {
                        Food todel = stringFoodHashMap.get(evt);
                        try {
                            RestaurantManagerDBC.deleteInMenu(restaurant, todel);
                        } catch (SQLException e1) {
                            new ErrorMsg(e1.getMessage());
                        }
                        menu.invalidate();
                        menu.revalidate();
                        menu.remove(stringJPanelMap.get(evt));
                        stringJPanelMap.remove(evt);
                    }
                }
            }
        }

        private class MenuWindowListener implements WindowListener{

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

    private void buildOrder(JPanel current){
        removeComponents(current);
        current.invalidate();
        current.revalidate();
        current.setLayout(null);
        current.setLayout(new BoxLayout(current,BoxLayout.PAGE_AXIS));
        String tmp = new String(new char[80]);
        current.add(new Label(tmp.replace('\0','*')));
        current.add(new Label("Orders"));
        JPanel search = new JPanel(new FlowLayout());
        search.setLayout(null);
        search.setLayout(new BoxLayout(search,BoxLayout.PAGE_AXIS));
        current.add(search);
        search.add(new Label("please provide the time period"));
        JPanel p1 = new JPanel(new FlowLayout());
        search.add(p1);
        p1.add(new Label("From: "));
        from = new JTextField("all",8);
        p1.add(from);
        to = new JTextField("all",8);
        p1.add(to);
        Button sbmt = new Button("Get orders");
        sbmt.setActionCommand("searchOrder");
        JPanel p2 = new JPanel(new FlowLayout());

        p1.add(sbmt);
        sbmt.addActionListener(new orderBtnListener());

    }
    private class orderBtnListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {

            orderBtnListener o = new orderBtnListener();
            String evt = e.getActionCommand();
            if (evt.equals("searchOrder")){
                try{
                    Date.valueOf(from.getText());
                    Date.valueOf(to.getText());
                }catch (Exception ev){
                    new ErrorMsg("Date format wrong! please put in the form of YYYYMMDD");
                    return;
                }
                current.invalidate();
                current.revalidate();
                removeComponents(current);
                String tmp = new String(new char[80]);
                current.add(new Label(tmp.replace('\0','*')));
                current.add(new Label("click on restaurant id to see related orders"));
                if (!from.getText().equals("all")&&!to.getText().equals("all")) {

                        fromDate = Date.valueOf(from.getText());
                        toDate = Date.valueOf(to.getText());

                    for (Restaurant next : restaurants) {
                        Button temp = new Button(((Integer) next.getId()).toString());
                        temp.addActionListener(o);
                        current.add(temp);
                    }
                }else{
                    Button temp = new Button("0");
                    temp.addActionListener(o);
                    current.add(temp);
                }
            }else{
                if (!evt.equals("0")) {
                    new resOrders(integerRestaurantMap.get(Integer.parseInt(evt)),fromDate,toDate);
                }else{
                    new resOrders(new Restaurant((RestaurantManager) currentUser,0,null,null,null,0,false,null,null,null),null,null);
                }
            }
        }
    }

    private class resOrders extends Frame{
        Restaurant restaurant;
        JPanel current;
        List<Order> orders;
        Map<Integer,Order> integerOrderMap = new HashMap<>();
        Map<Integer,JPanel> integerJPanelMap = new HashMap<>();
        resOrders(Restaurant r,Date from, Date to){
            restaurant = r;
            setLayout(new FlowLayout());
            current = new JPanel(new FlowLayout());
            Container c = getContentPane();
            c.setPreferredSize(new Dimension(800,800));
            setSize(800,800);
            setVisible(true);
            setTitle("Orders in restaurant "+restaurant.getId());
            current.setLayout(null);
            current.setLayout(new BoxLayout(current,BoxLayout.PAGE_AXIS));
            try {
                orders = RestaurantManagerDBC.getOrders(restaurant,from,to);
            } catch (SQLException e) {
                new ErrorMsg(e.getMessage());
            }
            resOrderBtnListener listener = new resOrderBtnListener();
            if (orders!=null){
                for (Order next: orders){
                    integerOrderMap.put(next.getOrderID(),next);
                    JPanel temp = new JPanel(new FlowLayout());
                    integerJPanelMap.put(next.getOrderID(),temp);
                    temp.add(new Label("Order #"+ next.getOrderID()+" Status: "+next.getStatus()));
                    if (next.getStatus().equals(OrderStatus.SUBMITTED)) {
                        Button changeS = new Button("change status");
                        changeS.setActionCommand(((Integer) next.getOrderID()).toString());
                        temp.add(changeS);
                        changeS.addActionListener(listener);
                    }
                    current.add(temp);
                }
            }else{
                integerOrderMap.put(0,new Order(null,0,restaurant,null));
                JPanel temp = new JPanel(new FlowLayout());
                integerJPanelMap.put(0,temp);
                temp.add(new Label("Order #"+ 0+" Status: "+ OrderStatus.SUBMITTED));
                Button changeS = new Button("change status");
                changeS.setActionCommand("0");
                temp.add(changeS);
                changeS.addActionListener(listener);
                current.add(temp);
            }
            add(current);
            addWindowListener(new windowListener());

        }
        public class windowListener implements WindowListener{

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
        private class resOrderBtnListener implements ActionListener{

            @Override
            public void actionPerformed(ActionEvent e) {
                current.invalidate();
                current.revalidate();

                String evt = e.getActionCommand();
                Integer orderID = Integer.parseInt(evt);

                JPanel panelToChange = integerJPanelMap.get(orderID);
                panelToChange.invalidate();
                panelToChange.revalidate();
                removeComponents(panelToChange);
                panelToChange.add(new Label("Order #"+ orderID+" Status: "+OrderStatus.READY));
                try {
                    CustomerDBC.updateOrderStatus(orderID,OrderStatus.READY);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
    private void beforeBuildingReport(JPanel current){
        removeComponents(current);
        current.invalidate();
        current.revalidate();
        current.setLayout(null);
        current.setLayout(new BoxLayout(current,BoxLayout.PAGE_AXIS));
        String tmp = new String(new char[80]);
        current.add(new Label(tmp.replace('\0','*')));
        current.add(new Label("Report"));
        current.add(new Label(tmp.replace('\0','*')));
        JPanel p1 = new JPanel(new FlowLayout());
        p1.setLayout(null);
        p1.setLayout(new BoxLayout(p1,BoxLayout.PAGE_AXIS));
        p1.add(new Label("View most popular food in specified time period"));
        p1.add(new Label("Or view the revenue of specified time period"));
        JPanel inside = new JPanel(new FlowLayout());
        inside.add(new Label("From: "));
        from = new JTextField("all",8);
        inside.add(from);
        inside.add(new Label(" To: "));
        to = new JTextField("all",8);
        inside.add(to);
        p1.add(inside);
        current.add(p1);
        p1.add(new Label(tmp.replace('\0','*')));
        Button submit = new Button("View Popular Food");
        submit.setActionCommand("popFood");
        p1.add(submit);
        submit.addActionListener(new reportListener());
        Button submit2 = new Button("View Revenue");
        submit2.setActionCommand("rev");
        p1.add(submit2);
        submit2.addActionListener(new reportListener());
    }
    private class reportListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {

            try {
                Date.valueOf(from.getText());
                Date.valueOf(to.getText());
            }catch (Exception ex){
                new ErrorMsg("Date format wrong! YYYYMMDD form required");
                return;
            }
            reportListener l = new reportListener();
            removeComponents(current);
            current.invalidate();
            current.revalidate();
            String tmp = new String(new char[80]);
            current.add(new Label(tmp.replace('\0','*')));
            String evt = e.getActionCommand();
            if (evt.equals("popFood")){
                current.add(new Label("click on restaurant ID to see popular dishes"));
                if (restaurants!=null) {
                    for (Restaurant next : restaurants) {
                        Button temp = new Button(((Integer) next.getId()).toString());
                        temp.addActionListener(l);
                        current.add(temp);
                    }
                }else{

                }
            }else if (evt.equals("rev")){
                current.add(new Label("Revenue grouped by restaurant"));
                if (restaurants!=null) {
                    for (Restaurant next : restaurants) {

                        current.add(new Label(tmp.replace('\0', '*')));
                        current.add(new Label("click on restaurant ID to see revenue for each restaurant"));
                        Button temp = new Button(((Integer) next.getId()).toString());
                        current.add(temp);
                        temp.addActionListener(new revListener());
                    }
                }else{

                }

            }else{
                Integer id = Integer.parseInt(evt);
                Restaurant restaurant = integerRestaurantMap.get(id);
                new PopularFood(restaurant);

            }
        }
        private class revListener implements ActionListener{

            @Override
            public void actionPerformed(ActionEvent e) {
                String evt = e.getActionCommand();
                Integer id = Integer.parseInt(evt);
                Restaurant restaurant = integerRestaurantMap.get(id);
                new Revenue(restaurant);
            }
        }
        private class PopularFood extends Frame{
            private  JPanel current;
            PopularFood(Restaurant r){
                setLayout(new FlowLayout());
                Container c = getContentPane();
                c.setPreferredSize(new Dimension(500,500));
                setSize(500,500);
                current = new JPanel(new FlowLayout());
                current.setLayout(null);
                current.setLayout(new BoxLayout(current,BoxLayout.PAGE_AXIS));
                String tmp = new String(new char[80]);
                current.add(new Label(tmp.replace('\0','*')));
                current.add(new Label("Most popular dish for restaurant "+r.getName()));
                current.add(new Label(tmp.replace('\0','*')));
                String food = "";

                try {
                    food = RestaurantManagerDBC.getPopularDish(r.getId(),Date.valueOf(from.getText()),Date.valueOf(to.getText()));
                } catch (SQLException e) {
                    new ErrorMsg(e.getMessage());
                }
                current.add(new Label("The most popular dish: "+food));
                add(current);
                setVisible(true);
                addWindowListener(new windowListener());
            }
            public class windowListener implements WindowListener{
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
        private class Revenue extends Frame{
            JPanel current;
            Revenue(Restaurant r){

                setLayout(new FlowLayout());
                Container c = getContentPane();
                c.setPreferredSize(new Dimension(500,500));
                setSize(500,500);
                current = new JPanel(new FlowLayout());
                current.setLayout(null);
                current.setLayout(new BoxLayout(current,BoxLayout.PAGE_AXIS));
                current.add(new Label("Revenue for restaurant "+r.getName()));

                    fromDate = Date.valueOf(from.getText());
                    toDate = Date.valueOf(to.getText());

                List<Order> orders = null;
                try {
                    orders = RestaurantManagerDBC.getOrders(r,fromDate,toDate);
                } catch (SQLException e) {
                    new ErrorMsg(e.getMessage());
                }
                for (Order next: orders){
                    current.add(new Label("Order id: "+next.getOrderID()+ " Amount: "+next.getAmount()+ " Ordered At: "+next.getDate()));
                }
                add(current);
                try {
                    current.add(new Label("Total earning: "+ RestaurantManagerDBC.getRevenue(r,fromDate,toDate)));
                } catch (SQLException e) {
                    new ErrorMsg(e.getMessage());
                }
                setVisible(true);

                addWindowListener(new windowListener());
        }
            public class windowListener implements WindowListener{
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
        RestaurantManager c = (RestaurantManager) currentUser;
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
                            }else {
                                try{
                                    Integer.parseInt(newNum);

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
