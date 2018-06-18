package com.cpsc304.UI;

import com.cpsc304.JDBC.CustomerDBC;
import com.cpsc304.JDBC.RestaurantDBC;
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
import java.util.*;
import java.util.List;

import static com.cpsc304.UI.MainUI.currentUser;

public class CustomerW extends JFrame{


    private Login l;
    private JPanel current;
    private JTextField na;
    private JTextField phone;
    private JTextField password;
    private JTextField datef;
    private JTextField datet;
    private JTextField res;
    private JTextField food;
    private Choice type;
    private JTextField rating;
    private JTextField status;
    private Order order;
    private Order currentOrder;
    private List<Restaurant> restaurants=null;
    private JTextField reportFrom;
    private JTextField reportTo;
    private Checkbox crating;
    private Checkbox hours;
    private Checkbox deliveryOption;
    private Checkbox address;
    private Checkbox ctype;
    private Map<Long,Order> integerOrderMap = new HashMap<>();
    private Checkbox cdel;
    private Checkbox cpic;



    CustomerW(Login l){

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
        Button l5 = new Button("Report");
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

            MainUI.customerLogOut();
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
                    case "Report":
                        beforeBuildingReport(current);
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
            Integer resID = 0;
            boolean can = true;
            if (!cdel.getState()&&!cpic.getState()){
                new ErrorMsg("You have to choose at lease one type of orders!");
                return;
            }
            if (!res.getText().equals("all")) {

                try {
                    Integer.parseInt(res.getText());
                } catch (Exception ev) {
                    new ErrorMsg("Please type in integer only for the restaurant id");
                    can = false;
                }
            }else{
                resID = 0;
            }
            try{
                Date.valueOf(dateFrom);
                Date.valueOf(dateTo);
            }catch (Exception ev){
                new ErrorMsg("Please provide dates of correct format(YYYY-MM-DD)");
                can = false;
            }
            if (can) {
                buildHistoryOrder(current, dateFrom, dateTo, resID);
            }


        }
    }



        private void beforeBuidlingHistoryOrder(JPanel current){
            current.setLayout(null);
            current.setLayout(new BoxLayout(current,BoxLayout.PAGE_AXIS));

            String tmp = new String(new char[80]);
            current.add(new Label(tmp.replace('\0','*')));
            current.add(new Label("What past orders(pickups,deliveries) do you want to look at?\n"));
            current.add(new Label(" Please provide the dates or ID of the restaurant, and select types"));
            datef = new JTextField("",8);
            res = new JTextField("all",15);
            JPanel p1 = new JPanel(new FlowLayout());
            p1.add(new Label("date:"));
            p1.add(datef);
            datet = new JTextField("",8);
            p1.add(new Label("to"));
            p1.add(datet);

            current.add(p1);
            JPanel p2 = new JPanel(new FlowLayout());
            p2.add(new Label("restaurant:"));
            p2.add(res);
            current.add(p2);
            JPanel p3 = new JPanel(new FlowLayout());
            current.add(p3);
            cdel = new Checkbox("Deliveries",false);
            p3.add(cdel);
            cpic = new Checkbox("Pick-ups",false);
            p3.add(cpic);
            Button b = new Button("submit");
            b.addActionListener(new historySubmitListener());
            current.add(b);


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

                order = integerOrderMap.get(Long.parseLong(orderID));
                new OrderDetails(order);

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

                if (order.getStatus().equals(OrderStatus.DELIVERED)||order.getStatus().equals(OrderStatus.READY)){
                    canChange = true;
                }
                if (order  instanceof Delivery){
                    isDelivery = true;
                }
                if (order instanceof  Pickup){

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
                toAdd.add(new Label("Date and Time : "+order.getDate()+" "+order.getTime()));
                toAdd.add(new Label("Ordered at: " + order.getRestOrderedAt().getName()));
                toAdd.add(new Label("Amount spent: "+order.getAmount()));
                //panel for food and quantities
                JPanel foodq = new JPanel(new FlowLayout());
                foodq.invalidate();
                foodq.revalidate();
                foodq.setLayout(null);
                foodq.setLayout(new BoxLayout(foodq,BoxLayout.PAGE_AXIS));
                Map<Food,Integer> foodmap = order.getQuantity();
                toAdd.add(foodq);
                if (foodmap!=null) {
                    for (Map.Entry<Food, Integer> next : foodmap.entrySet()) {
                        foodq.add(new Label(next.getKey().getName() + " quantity: " + next.getValue()));
                    }
                }
                if (isDelivery){
                    toAdd.add(new Label("Delivery fee: "+((Delivery)order).getDeliveryFee()));
                    toAdd.add(new Label("DELIVERED by: "+((Delivery)order).getCourier().getName()));
                    if (!canChange){
                        toAdd.add(new Label("Arrival Time: "+((Delivery)order).getArrivalTime()));
                    }
                }else{
                    toAdd.add(new Label("READY time: "+((Pickup)order).getREADYTime()));
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
                    try {
                        CustomerDBC.updateOrderStatus(order.getOrderID(),OrderStatus.CANCELLED);
                    } catch (SQLException e1) {
                        new ErrorMsg(e1.getMessage());
                    }
                    order.setStatus(OrderStatus.CANCELLED);
                }
            }

            private class changeListener implements ActionListener{

                @Override
                public void actionPerformed(ActionEvent e) {
                    switch (order.getStatus()){
                        case DELIVERED:
                            try {
                                CustomerDBC.updateOrderStatus(order.getOrderID(),OrderStatus.COMPLETE);
                            } catch (SQLException e1) {
                                new ErrorMsg(e1.getMessage());
                            }
                            Container p = status.getParent();
                            p.invalidate();
                            p.revalidate();
                            status.setText(OrderStatus.COMPLETE.toString());
                            order.setStatus(OrderStatus.COMPLETE);
                        case READY:
                            try {
                                CustomerDBC.updateOrderStatus(order.getOrderID(),OrderStatus.COMPLETE);
                            } catch (SQLException e1) {
                                new ErrorMsg(e1.getMessage());
                            }
                            Container c = status.getParent();
                            c.invalidate();
                            c.revalidate();
                            status.setText(OrderStatus.COMPLETE.toString());
                            order.setStatus(OrderStatus.COMPLETE);
                            break;
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
        private void buildHistoryOrder(JPanel current,String dateF,String dateT, Integer resID){
            boolean pic = cpic.getState();
            boolean del = cdel.getState();

            String types = "Type(s): ";
            if (pic&&del){
                types += "pick-ups and deliveries";
            }else {
                if (pic) {
                    types += "pick-ups ";
                }else{
                    types+="deliveries";
                }
            }

            removeComponents(current);
            current.invalidate();
            current.revalidate();
            current.setLayout(null);
            current.setLayout(new BoxLayout(current,BoxLayout.PAGE_AXIS));
            String tmp = new String(new char[80]);
            current.add(new Label(tmp.replace('\0','*')));

            Label title = new Label("History Orders "+types);
            current.add(title);
            current.add(new Label("Please click on order id to see details\t\t\t\t\t\t\t"));
            //panel for dynamic list of orders
            JPanel orderNumbers = new JPanel(new FlowLayout(FlowLayout.CENTER));
            current.add(orderNumbers);
            orderNumbers.add(new Label("order#"));

            Date from = Date.valueOf(dateF);
            Date to = Date.valueOf(dateT);


            historyOrderListener l = new historyOrderListener();

                List<Order> orders = null;
                try {

                    if (pic&&del) {
                        orders = CustomerDBC.getOrders(resID, from,to);
                    }else{
                        if (pic){
                            orders = CustomerDBC.getPickups(resID,from,to);
                        }else{
                            orders = CustomerDBC.getDeliveries(resID,from,to);
                        }

                    }
                } catch (SQLException e1) {
                    new ErrorMsg(e1.getMessage());
                }

                if (orders==null) {
                    current.add(new Label("No such order is found. Try again with different time period or type"));
                    return;
                }

                for (Order next : orders) {
                    Button o = new Button(Long.toString(next.getOrderID()) + "( on" + next.getDate() + ")");
                    o.setActionCommand(Long.toString(next.getOrderID()));
                    current.add(o);
                    o.addActionListener(l);
                    integerOrderMap.put(next.getOrderID(),next);
                }


            pack();

        }


        private class orderListener implements ActionListener{

            @Override
            public void actionPerformed(ActionEvent e) {
                String evt = e.getActionCommand();
                boolean isRateSelected = crating.getState();
                boolean isAddSelected = address.getState();
                boolean isHoursSelected = hours.getState();
                boolean isTypeSelected = ctype.getState();
                boolean isD = deliveryOption.getState();
                switch (evt){
                    case "Search":
                        try {
                            buildNewOrder(food.getText(),type.getSelectedItem(),rating.getText(),isRateSelected,isHoursSelected,isD,isTypeSelected,isAddSelected);
                        } catch (SQLException e1) {
                            new ErrorMsg(e1.getMessage());
                        }
                        break;
                    case "re":
                        buildNewOrder(isRateSelected,isHoursSelected,isD,isTypeSelected,isAddSelected);
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
            type = new Choice();
            type.add("Fast Food");
            type.add("Asian Food");
            type.add("European Food");
            type.add("Coffee Shop");
            t.add(type);
            //panel for rating
            JPanel r = new JPanel(new FlowLayout(FlowLayout.LEFT));
            current.add(r);
            r.add(new Label("rating(1 to 5): "));
            rating = new JTextField("all");
            r.add(rating);
            //checkbox
            current.add(new Label("Please select extra info of restaurant you'd like to see in the restaurant list in search result"));
            JPanel c = new JPanel(new GridLayout(1,5));
            current.add(c);
            crating = new Checkbox("rating",false);
            c.add(crating);
            hours = new Checkbox("hours",false);
            c.add(hours);
            deliveryOption = new Checkbox("deliveryOption",false);
            c.add(deliveryOption);
            ctype = new Checkbox("type",false);
            c.add(ctype);
            address = new Checkbox("address",false);
            c.add(address);
            //button for submitting order search
            Button submitSearch = new Button("Search");
            submitSearch.addActionListener(new orderListener());
            current.add(submitSearch);
            //button for recommendation
            Button recommend = new Button("Recommendation based on previous Order");
            recommend.addActionListener(new orderListener());
            recommend.setActionCommand("re");
            current.add(recommend);


        }

        private void buildNewOrder(String food, String resType, String rating, boolean r, boolean h, boolean d, boolean t, boolean a) throws SQLException {

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

            current.add(new Label("Click on restaurant id to open restaurant menu"));
            current.add(new Label(tmp.replace('\0','*')));
            if (!foodList.get(0).equals("all")) {
                if (rate.equals("all")){
                    restaurants = CustomerDBC.getRankedRestaurants(foodList,r,h,d,t,a);
                }else {
                    restaurants = CustomerDBC.getRankedRestaurants(foodList,Integer.parseInt(rating),r,h,d,t,a);
                }
            }else{
                if (!type.equals("all")) {
                    if (rate.equals("all")) {
                        restaurants = CustomerDBC.getBestRestaurants(type, r, h, d, t, a);
                    }else{
                        restaurants = CustomerDBC.getBestRestaurants(type,Integer.parseInt(rate),  r, h, d, t, a);
                    }
                }else{
                    if (!rate.equals("all")) {
                        restaurants = CustomerDBC.getRestaurantsOfRating(Integer.parseInt(rate),r,h,d,t,a);
                    }
                }
            }
            if (restaurants!=null){
                for (Restaurant next: restaurants){
                    String info = "(";
                    if (r){
                        info+="Rating: ";
                        info+=next.getRating();
                        info+=" ";
                    }
                    if (h){
                        info+="Hours: ";
                        info+=next.getOpenTime();
                        info+=" to ";
                        info+=next.getCloseTime();
                        info+=" ";
                    }
                    if (d){
                        info+="Delivery Option: ";
                        info+=next.isDeliveryOption();
                        info+=" ";
                    }
                    if (t){
                        info+="Type: ";
                        info+=next.getType();
                        info+=" ";
                    }
                    if (a){
                        info+="Address: ";
                        String temp = "";

                        temp = next.getAddress().getHouseNum() + " " + next.getAddress().getStreet() + ", " + next.getAddress().getCity() + " " + next.getAddress().getProvince() + ", " + next.getAddress().getPostalCode();
                        info+=temp;
                        info+=" ";
                    }
                    info+=")";
                    JPanel m = new JPanel(new FlowLayout());
                    m.add(new Label("Restaurant name: "+next.getName()+" ID: "));
                    Button resB = new Button(((Integer)next.getId()).toString());
                    resB.addActionListener(new resSelectListener());
                    m.add(resB);
                    m.add(new Label(info));
                    current.add(m);
                }
            }else{


            }
        }
    private void buildNewOrder(boolean r, boolean h, boolean d, boolean t, boolean a){

        try {
            restaurants=CustomerDBC.getRecommendedRestaurants();
        } catch (SQLException e) {
            new ErrorMsg(e.getMessage());
        }
        removeComponents(current);
        current.invalidate();
        current.revalidate();
        current.setLayout(null);
        current.setLayout(new BoxLayout(current,BoxLayout.PAGE_AXIS));
        String tmp = new String(new char[80]);
        current.add(new Label(tmp.replace('\0','*')));


        current.add(new Label("Recommendation of restaurants"));
        current.add(new Label("Click on restaurant id to open restaurant menu"));
        current.add(new Label(tmp.replace('\0','*')));

        if (restaurants!=null){
            if (restaurants.size()==0){
                current.add(new Label("restaurants not found."));
            }else {
                for (Restaurant next : restaurants) {
                    String info = "(";
                    if (r){
                        info+="Rating: ";
                        info+=next.getRating();
                        info+=" ";
                    }
                    if (h){
                        info+="Hours: ";
                        info+=next.getOpenTime();
                        info+=" to ";
                        info+=next.getCloseTime();
                        info+=" ";
                    }
                    if (d){
                        info+="Delivery Option: ";
                        info+=next.isDeliveryOption();
                        info+=" ";
                    }
                    if (t){
                        info+="Type: ";
                        info+=next.getType();
                        info+=" ";
                    }
                    if (a){
                        info+="Address: ";
                        String temp = "";

                        temp = next.getAddress().getHouseNum() + " " + next.getAddress().getStreet() + ", " + next.getAddress().getCity() + " " + next.getAddress().getProvince() + ", " + next.getAddress().getPostalCode();
                        info+=temp;
                        info+=" ";
                    }
                    info+=")";
                    JPanel m = new JPanel(new FlowLayout());
                    m.add(new Label("Restaurant name: "+next.getName()+" ID: "));
                    Button resB = new Button(((Integer)next.getId()).toString());
                    resB.addActionListener(new resSelectListener());
                    m.add(resB);
                    m.add(new Label(info));
                    current.add(m);
                }
            }

        }else{
            current.add(new Label("No recommendations. Sorry."));
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
            private boolean isSubmitShown = false;
            private JTextField subtotal;
            private Map<Food,JTextField> fields;
            private Map<String,Food> offers = new HashMap<>();
            private List<Food> menu;
            JPanel j;
            private Restaurant restaurant;
            public showRestaurant (Restaurant r){
                restaurant = r;
                setLayout(new FlowLayout());
                setSize(1000,800);
                setVisible(true);
                fields = new HashMap<>();

                try {
                    menu = RestaurantDBC.getMenu(r.getId());
                } catch (SQLException e) {
                    new ErrorMsg(e.getMessage());
                }

                j = new JPanel(new FlowLayout());
                j.invalidate();
                j.revalidate();
                j.setLayout(null);
                j.setLayout(new BoxLayout(j,BoxLayout.PAGE_AXIS));
                String tmp = new String(new char[100]);
                j.add(new Label(tmp.replace('\0','*')));
                j.add(new Label("Restaurant: " +r.getName() ));
                j.add(new Label(" Hours: "+r.getOpenTime()+ " to "+r.getCloseTime()));
                j.add(new Label(" Rating: "+r.getRating()));
                j.add(new Label(tmp.replace('\0','*')));
                if (menu!=null) {
                    for (Food next : menu) {
                        JPanel tmpFood = new JPanel(new FlowLayout());
                        tmpFood.add(new Label(next.getName() + " "));
                        tmpFood.add(new Label("Price: " + next.getPrice()));
                        tmpFood.add(new Label("Quantity: "));
                        JTextField quantity = new JTextField("0");
                        quantity.setName(next.getName());
                        tmpFood.add(quantity);
                        j.add(tmpFood);
                        fields.put(next, quantity);
                        offers.put(next.getName(),next);
                    }
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
                Map<Food,Integer> quantity = new HashMap<>();
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
                        if (!isSubmitShown) {
                            isSubmitShown = true;
                            Button submit = new Button("Submit");
                            submit.addActionListener(new submitListener());
                            add(submit);
                        }
                    }else{
                        currentOrder = new Order((Customer) currentUser,total,restaurant,quantity);
                        try {
                            CustomerDBC.commitOrder(currentOrder);
                        } catch (SQLException e1) {
                            new ErrorMsg(e1.getMessage());
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
                            if (newPw.length()<6){
                                new ErrorMsg("Password must be longer than 6 characters!");
                            }else {
                                if (newNum.length()!=10){
                                    new ErrorMsg("Phone number must be in Canadian format!");
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
        //before building the panel for viewing report
        private void beforeBuildingReport(JPanel current){
            removeComponents(current);
            current.invalidate();
            current.revalidate();
            current.setLayout(null);
            current.setLayout(new BoxLayout(current,BoxLayout.PAGE_AXIS));
            String tmp = new String(new char[80]);
            current.add(new Label(tmp.replace('\0','*')));
            current.add(new Label("Report generator"));
            current.add(new Label("Please provide the time period for your report"));
            reportFrom = new JTextField("",8);
            reportTo = new JTextField("",8);
            JPanel fromto = new JPanel(new FlowLayout());
            fromto.add(new Label("From "));
            fromto.add(reportFrom);
            fromto.add(new Label(" To "));
            fromto.add(reportTo);
            current.add(fromto);
            Button submit = new Button("View Report");
            submit.addActionListener(new reportListener());
            current.add(submit);

        }

        private class reportListener implements ActionListener{

            @Override
            public void actionPerformed(ActionEvent e) {
                String fromDate = reportFrom.getText();
                String toDate = reportTo.getText();
                if (fromDate.equals("all")&&toDate.equals("all")){
                    buildReport(null,null);
                }else {
                    try {
                        Date from = Date.valueOf(fromDate);
                        Date to = Date.valueOf(toDate);
                        buildReport(from,to);
                    }catch (Exception ev){
                        new ErrorMsg("Wrong date format. Only supports form of YYYY-MM-DD");
                    }

                }
            }
        }

        private void buildReport(Date from, Date to){
            List<Order> orders = null;
            try {
                orders = CustomerDBC.getOrders(from,to);
            } catch (SQLException e) {
                new ErrorMsg(e.getMessage());
            }
            removeComponents(current);
            current.invalidate();
            current.revalidate();
            current.setLayout(null);
            current.setLayout(new BoxLayout(current,BoxLayout.PAGE_AXIS));
            String tmp = new String(new char[80]);
            current.add(new Label(tmp.replace('\0','*')));
            current.add(new Label("REPORT FOR CUSTOMER "+ currentUser.getUserID()+ " FROM DATE "+from+" TO "+to));
            if (orders!=null) {
                for (Order next : orders) {
                    current.add(new Label("OrderID: " + next.getOrderID() + " ordered at restaurant " + next.getRestOrderedAt().getName() + " total amount: " + next.getAmount()+ " Order Status: "+next.getStatus()));
                }
            }else{
                current.add(new Label("No orders found. Try again?"));
                return;
            }
            try {
                current.add(new Label("Total spending in selected time period: "+CustomerDBC.getSpending(from,to)));
            } catch (SQLException e) {
                new ErrorMsg(e.getMessage());
            }

        }
}
