package com.cpsc304.UI;

import com.cpsc304.JDBC.CustomerDBC;
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
import java.text.DateFormat;
import java.util.*;
import java.util.List;

import static com.cpsc304.UI.MainUI.currentUser;

public class CustomerW extends JFrame{

    private CustomerW instance;
    private Login l;
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
    private JTextField status;
    private Order order;
    private Order currentOrder;
    private List<Restaurant> restaurants=null;


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
        setTitle("Customer");


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


    class historySubmitListener implements ActionListener {

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
            b.addActionListener(new historySubmitListener());
            current.add(b,BorderLayout.LINE_END);


        }
        //listener for buttons of order
        private class historyOrderListener implements ActionListener{

            @Override
            public void actionPerformed(ActionEvent e) {
                String evt = e.getActionCommand();
                showDetailsOfOrder(evt);
            }
        }
        //showing details of selected order
        private void showDetailsOfOrder(String orderID){
            if (orderID=="test") {
                new OrderDetails(new Delivery((Customer) currentUser,1,null,null,0,OrderStatus.SUBMITTED,null,null,0,null,null) {
                });
            }else{
                order = UserDBC.getOrder(orderID);
                new OrderDetails(order);
            }
        }

        private class OrderDetails extends Frame{
            boolean canCancel = false;
            boolean isDelivery = false;
            boolean canChange = false;

            OrderDetails(Order order){
                setLayout(new FlowLayout());
                JPanel toAdd = new JPanel(new FlowLayout());
                add(toAdd);
                if (order.getStatus().equals(OrderStatus.SUBMITTED)){
                    canCancel = true;
                }

                if (order.getStatus().equals(OrderStatus.DELIVERED)){
                    canChange = true;
                }
                if (order  instanceof Delivery){
                    isDelivery = true;
                }
                toAdd.invalidate();
                toAdd.revalidate();
                toAdd.setLayout(null);
                toAdd.setLayout(new BoxLayout(toAdd,BoxLayout.PAGE_AXIS));
                setSize(800,800);
                Container c = getContentPane();
                c.setPreferredSize(new Dimension(800,800));
                setVisible(true);
                setResizable(false);

                addWindowListener(new detailWindowListener());
                toAdd.add(new Label("Order Details"));
                toAdd.add(new Label("Order id: "+order.getOrderID()));
                toAdd.add(new Label("Date and Time submitted: "+order.getDate()+" "+order.getTime()));
                toAdd.add(new Label("Ordered at: " + order.getRestOrderedAt()));
                toAdd.add(new Label("Amount spent: "+order.getAmount()));
                //panel for food and quantities
                JPanel foodq = new JPanel(new FlowLayout());
                foodq.invalidate();
                foodq.revalidate();
                foodq.setLayout(null);
                foodq.setLayout(new BoxLayout(foodq,BoxLayout.PAGE_AXIS));
                Map<Food,Integer> foodmap = order.getQuantity();
                toAdd.add(foodq);
                for (Map.Entry<Food,Integer> next : foodmap.entrySet()){
                    foodq.add(new Label(next.getKey().getName()+ " quantity: "+next.getValue()));
                }
                if (isDelivery){
                    toAdd.add(new Label("Delivery fee: "+((Delivery)order).getDeliveryFee()));
                    toAdd.add(new Label("Delivered by: "+((Delivery)order).getCourier()));
                    if (!canChange){
                        toAdd.add(new Label("Arrival Time: "+((Delivery)order).getArrivalTime()));
                    }
                }else{
                    toAdd.add(new Label("Ready time: "+((Pickup)order).getReadyTime()));
                }
                //Jpanel for status
                JPanel s = new JPanel(new FlowLayout());
                status = new JTextField(order.getStatus().toString());
                status.setEditable(false);
                s.add(new Label("Status: "));
                s.add(status);
                toAdd.add(s);
                if (canChange){
                    Button changeStatus = new Button("Change status");
                    toAdd.add(changeStatus);
                    changeStatus.addActionListener(new changeListener());
                }
                if (canCancel){
                    Button cancel = new Button("Cancel order");
                    toAdd.add(cancel);
                    cancel.addActionListener(new cancelListener());
                }
                pack();
            }
            private class cancelListener implements ActionListener{

                @Override
                public void actionPerformed(ActionEvent e) {
                    Container p = status.getParent();
                    p.invalidate();
                    p.revalidate();
                    status.setText(OrderStatus.CANCELLED.toString());
//                   TODO:remove comment when dbc is implemented CustomerDBC.updateOrderStatus(order.getOrderID(),OrderStatus.CANCELLED);
                    order.setStatus(OrderStatus.CANCELLED);
                }
            }

            private class changeListener implements ActionListener{

                @Override
                public void actionPerformed(ActionEvent e) {
                    switch (order.getStatus()){
                        case DELIVERED:
                            //TODO:remove comment when dbc is implemented CustomerDBC.updateOrderStatus(order.getOrderID(),OrderStatus.COMPLETE);
                            Container p = status.getParent();
                            p.invalidate();
                            p.revalidate();
                            status.setText(OrderStatus.COMPLETE.toString());
                            order.setStatus(OrderStatus.COMPLETE);
                    }
                }
            }

            private class detailWindowListener implements WindowListener{

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

        //build the panel for viewing history order
        private void buildHistoryOrder(JPanel current,String dateF,String dateT, String resN){
            removeComponents(current);
            current.invalidate();
            current.revalidate();
            current.setLayout(null);
            current.setLayout(new BoxLayout(current,BoxLayout.PAGE_AXIS));
            String tmp = new String(new char[80]);
            current.add(new Label(tmp.replace('\0','*')));
            Label title = new Label("\t\t\t\t\t\t\tHistory Orders\t\t\t\t\t\t\t");
            current.add(title);
            current.add(new Label("\t\t\t\t\t\t\tPlease click on order id to see details\t\t\t\t\t\t\t"));
            //panel for dynamic list of orders
            JPanel orderNumbers = new JPanel(new FlowLayout(FlowLayout.CENTER));
            current.add(orderNumbers);
            orderNumbers.add(new Label("order#"));

            Long from = null;
            Long to = null;

//            if (dateF==null&&dateT==null){}else {
//                from = Long.parseLong(dateF);
//                to = Long.parseLong(dateT);
//            }

            historyOrderListener l = new historyOrderListener();
            if (currentUser.getUserID()==1){
                Button o = new Button("test");
                current.add(o);
                o.addActionListener(l);
            }else {
                List<Order> orders = UserDBC.getOrders(resN, new Date(from),new Date(to));

                for (Order next : orders) {
                    Button o = new Button(Integer.toString(next.getOrderID()) + "(submitted on" + next.getDate() + ")");
                    current.add(o);
                    o.addActionListener(l);
                }
                Button o = new Button("test");
                current.add(o);
                o.addActionListener(l);
            }
            pack();

        }


        private class orderListener implements ActionListener{

            @Override
            public void actionPerformed(ActionEvent e) {
                String evt = e.getActionCommand();
                switch (evt){
                    case "Search":
                        buildNewOrder(food.getText(),type.getText(),rating.getText());
                        break;
                    case "Recommendation":
                        buildNewOrder();
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
            Button submitSearch = new Button("Search");
            submitSearch.addActionListener(new orderListener());
            current.add(submitSearch);
            //button for recommendation
            Button recommend = new Button("Recommendation");
            recommend.addActionListener(new orderListener());
            current.add(recommend);
        }

        private void buildNewOrder(String food, String resType, String rating){

            List<String> foodList = Arrays.asList(food.split(","));
            String type = resType;
            String rate = rating;
            removeComponents(current);
            current.invalidate();
            current.revalidate();
            current.setLayout(null);
            current.setLayout(new BoxLayout(current,BoxLayout.PAGE_AXIS));
            String tmp = new String(new char[80]);
            current.add(new Label(tmp.replace('\0','*')));

            current.add(new Label("List of restaurants"));
            if (!foodList.get(0).equals("all")) {
                restaurants = CustomerDBC.getRestaurants(foodList);
            }else{
                if (!type.equals("all")) {
                    restaurants = CustomerDBC.getBestRestaurants(type);
                }else{
                    if (!rate.equals("all")) {
                        restaurants = CustomerDBC.getRestaurantsOfRating(Integer.parseInt(rate));
                    }
                }
            }
            if (restaurants!=null){
                for (Restaurant next: restaurants){
                    JPanel m = new JPanel(new FlowLayout());
                    m.add(new Label("Restaurant name: "+next.getName()+"ID: "));
                    Button resB = new Button(((Integer)next.getId()).toString());
                    resB.addActionListener(new resSelectListener());
                    m.add(resB);
                    current.add(m);
                }
            }else{

                    current.add(new Label("restaurants not found."));

            }
        }
    private void buildNewOrder(){

        restaurants=CustomerDBC.getRecommendedRestaurants();
        removeComponents(current);
        current.invalidate();
        current.revalidate();
        current.setLayout(null);
        current.setLayout(new BoxLayout(current,BoxLayout.PAGE_AXIS));
        String tmp = new String(new char[80]);
        current.add(new Label(tmp.replace('\0','*')));

        current.add(new Label("Recommendation of restaurants"));

        if (restaurants!=null){
            if (restaurants.size()==0){
                current.add(new Label("restaurants not found."));
            }else {
                for (Restaurant next : restaurants) {
                    JPanel m = new JPanel(new FlowLayout());
                    m.add(new Label("Restaurant name: " + next.getName() + "ID: "));
                    Button resB = new Button(((Integer) next.getId()).toString());
                    resB.addActionListener(new resSelectListener());
                    m.add(resB);
                    current.add(m);
                }
            }

        }else{
            current.add(new Label("restaurants not found."));
        }
    }

        private class resSelectListener implements ActionListener{

            @Override
            public void actionPerformed(ActionEvent e) {
                String evt = e.getActionCommand();
                for (Restaurant next: restaurants){
                    if (next.getId()==Integer.parseInt(evt)){
                        new showRestaurant(next);
                        break;
                    }
                }
            }
        }

        private class showRestaurant extends Frame{
            private Map<Food,Integer> foodQuantity;
            private JTextField subtotal;
            private Map<Food,JTextField> fields;
            private Map<Food,Double> offers;
            JPanel j;
            private Restaurant restaurant;
            public showRestaurant (Restaurant r){
                restaurant = r;
                setLayout(new FlowLayout());
                setSize(1000,800);
                setVisible(true);
                fields = new HashMap<>();

                String tmp = new String(new char[100]);
                add(new Label(tmp.replace('\0','*')));
                add(new Label("Restaurant: " +r.getName() ));
                add(new Label(" Location: "+r.getAddress()));
                add(new Label(" Hours: "+r.getOpenTime()+ " to "+r.getCloseTime()));
                add(new Label(" Rating: "+r.getRating()));
                add(new Label(tmp.replace('\0','*')));

                offers = r.getOffers();

                j = new JPanel(new FlowLayout());
                j.invalidate();
                j.revalidate();
                j.setLayout(null);
                j.setLayout(new BoxLayout(j,BoxLayout.PAGE_AXIS));
                for (Map.Entry<Food,Double> next: offers.entrySet()){
                    JPanel tmpFood= new JPanel(new FlowLayout());
                    tmpFood.add(new Label(next.getKey().getName()+" "));
                    tmpFood.add(new Label("Price: "+ next.getValue()));
                    tmpFood.add(new Label("Quantity: "));
                    JTextField quantity = new JTextField("0");
                    quantity.setName(next.getKey().getName());
                    tmpFood.add(quantity);
                    j.add(tmpFood);
                    fields.put(next.getKey(),quantity);
                }

                if (r.isDeliveryOption()){
                    JPanel de = new JPanel(new FlowLayout());
                    de.add(new Label("Delivery?"));
                    JTextField delivery = new JTextField("No");
                    delivery.setName("delivery");
                    de.add(delivery);
                    j.add(de);
                }
                add(j);
                subtotal = new JTextField("0");
                subtotal.setVisible(false);
                add(subtotal);
                Button comfirm = new Button("Calculate subtotal");
                comfirm.addActionListener(new submitListener());
                add(comfirm);
                Button submit = new Button("Submit");
                submit.addActionListener(new submitListener());
                add(submit);
                addWindowListener(new windowListener());
                pack();

            }
            private class windowListener implements WindowListener{

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
            private class submitListener implements ActionListener{
                Double total;
                Map<Food,Integer> quantity;
                @Override
                public void actionPerformed(ActionEvent e) {
                    String evt = e.getActionCommand();
                    if (evt.equals("Calculate subtotal")){
                        total = new Double(0);
                        for (Map.Entry<Food,JTextField> next: fields.entrySet()){
                            total+=next.getKey().getPrice()*Integer.parseInt(next.getValue().getText());
                            quantity.put(next.getKey(),Integer.parseInt(next.getValue().getText()));
                        }
                        j.invalidate();
                        j.revalidate();
                        subtotal.setText(total.toString());
                        subtotal.setVisible(true);
                    }else{
                        currentOrder = new Order((Customer) currentUser,total,restaurant,quantity);
                        try {
                            CustomerDBC.commitOrder(currentOrder);
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                        dispose();
                    }
                }
            }
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
                            currentUser.setName(newName);
                            currentUser.setPassword(newPw);
                            currentUser.setPhoneNum(newNum);
                            UserDBC.updateUserInfo(currentUser);
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
            Customer c = (Customer) currentUser;
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
            removeComponents(current);
            current.invalidate();
            current.revalidate();
            current.setLayout(null);
            current.setLayout(new BoxLayout(current,BoxLayout.PAGE_AXIS));


        }
}
