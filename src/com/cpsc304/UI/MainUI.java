package com.cpsc304.UI;
import com.cpsc304.JDBC.DBConnection;
import com.cpsc304.model.User;

public class MainUI {
    public User currentUser;
    private static Login loginUI;
    private static ManagerW managerUI;
    private static CustomerW customerUI;
    private static CourierW courierUI;

    public static CourierW getCourierUI() {
        if (courierUI == null)
            courierUI = new CourierW();
        return courierUI;
    }

    public static ManagerW getManagerUI() {
        if (managerUI == null)
            managerUI = new ManagerW();
        return managerUI;
    }

    public static CustomerW getCustomerUI () {
        //if (customerUI == null)
        //    customerUI = new CustomerW();
        return customerUI;
    }

    public static Login getLoginUI() {
        if (loginUI == null)
            loginUI = new Login();
        return loginUI;
    }

    public static void main (String[] args){
        if (!DBConnection.connect())
            System.out.println("Connection failed");
        getLoginUI();
    }
}
