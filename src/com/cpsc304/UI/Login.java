package com.cpsc304.UI;
import com.cpsc304.JDBC.CourierDBC;
import com.cpsc304.JDBC.CustomerDBC;
import com.cpsc304.JDBC.LoginDBC;
import com.cpsc304.JDBC.RestaurantManagerDBC;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.SQLException;

public class Login extends JFrame  {
    private JTextField userID;
    private JPasswordField pw;
    private boolean isvalid;
    private Choice typeChooser;
    Login t;

    Login(){
        t = this;

        setLayout(new FlowLayout());
        JPanel p1 = new JPanel(new FlowLayout());
        JPanel p2 = new JPanel(new FlowLayout());
        JPanel p3 = new JPanel(new FlowLayout());
        Label id = new Label("userID:");
        p1.add(id);
        userID = new JTextField("",10);
        Label p = new Label("password:");
        pw = new JPasswordField("", 10);
        p1.add(userID);
        p2.add(p);
        p2.add(pw);
        add(p1);
        add(p2);
        Button s = new Button("submit");
        s.addActionListener(new btnListner());
        typeChooser = new Choice();
        typeChooser.add("customer");
        typeChooser.add("restaurant manager");
        typeChooser.add("courier");
        p3.add(typeChooser);
        add(p3);
        add(s);
        setTitle("Login");
        Container c = getContentPane();
        c.setPreferredSize(new Dimension(400,400));
        setResizable(false);
        pack();
        setVisible(true);
        addWindowListener(new windowListener());

    }

    class windowListener implements WindowListener{
        @Override
        public void windowOpened(WindowEvent e) {

        }

        @Override
        public void windowClosing(WindowEvent evt) {
            System.exit(0);  // terminate the program
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


     class btnListner implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent evt){
            String btnLabel = evt.getActionCommand();
            if (btnLabel.equals("submit")){
                String id =userID.getText();
                String password = pw.getText();
                String type=typeChooser.getSelectedItem();
                System.out.println("logging in...");
                if (id.equals("id")&&password.equals("password")){
//                    MainUI.currentUser = new Customer("1","test","1","123456",123,0,0);
//                    MainUI.currentUser = new Courier("1","test","1","123456",null);
//                    MainUI.currentUser = new RestaurantManager("1","test","1","123456",null);

                    isvalid = true;

                }else {
                    try {
                        isvalid = LoginDBC.verify(type, id, password);
                    } catch (SQLException e) {
                        new ErrorMsg(e.getMessage());
                    }
                }
                //submit the userid and pw to remote server
                if (isvalid){
                    switch (type){
                        case "customer":
                            try {
                                MainUI.currentUser = CustomerDBC.getCustomer(id);
                            } catch (SQLException e) {
                                new ErrorMsg(e.getMessage());
                                break;
                            }
                            System.out.println("customer.");
                            MainUI.getCustomerUI();
                            t.setVisible(false);
                            break;
                        case "restaurant manager":
                            System.out.println("manager.");
                            //                            MainUI.currentUser = new Customer();
                            try {
                                MainUI.currentUser = RestaurantManagerDBC.getManager(id);
                            } catch (SQLException e) {
                                new ErrorMsg(e.getMessage());
                                break;
                            }
                            MainUI.getManagerUI();
                            t.setVisible(false);
                            break;
                        case "courier":
                            System.out.println("courier.");
                            try {
                                MainUI.currentUser = CourierDBC.getCurier(id);
                            } catch (SQLException e) {
                                new ErrorMsg(e.getMessage());
                                break;
                            }
                            MainUI.getCourierUI();
                            t.setVisible(false);
                            break;
                    }


                }else{
                    new ErrorMsg("Combination of user id, password and type is invalid! Try again.");
                }
            }
        }

    }

}
