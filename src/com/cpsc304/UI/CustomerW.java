package com.cpsc304.UI;

import com.cpsc304.model.Customer;

import javax.swing.*;
import java.awt.*;

public class CustomerW extends JFrame{
    private String id;
    CustomerW(String id){
        this.id = id;
        //initializing basic view...
        setLayout(new FlowLayout());
        Container c = getContentPane();
        c.setPreferredSize(new Dimension(800,800));
        setResizable(false);
        pack();
        setVisible(true);
        //user info initializing
        JPanel userINFO = new JPanel(new FlowLayout());
        userINFO.setLocation(600,600);


    }
}
