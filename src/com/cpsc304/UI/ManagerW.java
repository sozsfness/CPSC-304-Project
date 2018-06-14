package com.cpsc304.UI;

import com.cpsc304.JDBC.RestaurantManagerDBC;
import com.cpsc304.JDBC.UserDBC;
import com.cpsc304.model.Customer;
import com.cpsc304.model.Restaurant;
import com.cpsc304.model.RestaurantManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.cpsc304.UI.MainUI.currentUser;

public class ManagerW extends JFrame {
    private Login l;
    private JPanel current;
    private JTextField na;
    private JTextField phone;
    private JTextField password;
    private Map<Integer,Restaurant> integerRestaurantMap = new HashMap<>();

    public ManagerW(Login l){
        this.l = l;
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
                    //TODO: BUILD ORDER
                    break;
                case "My INFO":
                    buildINFO(current);
                    break;
                case "Reports":
                    beforeBuildingReport(current);
                    //TODO:build report
                    break;
                case "delete":
                    Restaurant toDelete = integerRestaurantMap.get(Integer.parseInt(((Button)e.getSource()).getName()));
                    RestaurantManagerDBC.deleteRestaurant(toDelete);
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
        String tmp = new String(new char[80]);
        current.add(new Label(tmp.replace('\0','*')));
        current.add(new Label("Restaurants"));
        current.add(new Label("Click on restaurant id to view/manipulate its menu"));
        btnListner b = new btnListner();
        menuListener m = new menuListener();
        Set<Restaurant> restaurants = ((RestaurantManager)currentUser).getRestaurants();
        if (restaurants!=null) {
            for (Restaurant next : restaurants) {
                JPanel p = new JPanel(new FlowLayout());
                current.add(p);
                p.add(new Label("Name: " + next.getName()+"ID: "));
                Button id = new Button(((Integer)next.getId()).toString());
                p.add(id);
                id.addActionListener(m);
                Button temp = new Button("delete");
                temp.setName(((Integer) next.getId()).toString());
                p.add(temp);
                temp.addActionListener(b);
                integerRestaurantMap.put(next.getId(),next);
            }
        }else{
            JPanel p = new JPanel(new FlowLayout());
            current.add(p);
            p.add(new Label("Name: test" +"ID: "));
            Button id = new Button("0");
            p.add(id);
            id.addActionListener(m);
            Button temp = new Button("delete");
            temp.setName("0");
            p.add(temp);
            temp.addActionListener(b);
            integerRestaurantMap.put(0,new Restaurant((RestaurantManager) currentUser,0,null,null,null,0,false,null,null,null,null));
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
        private Restaurant restaurant;
        public Menu(Restaurant r){
            restaurant = r;
            setLayout(new FlowLayout());
            setSize(800,800);
            Container c = getContentPane();
            c.setPreferredSize(new Dimension(800,800));
            //TODO:CONSTRUCT A LIST OF FOOD WITH PRICE, ALLOWING DELETION AND ADDITION
        }
    }

    private void buildOrder(JPanel current){

    }
    private void beforeBuildingReport(JPanel current){

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
}
