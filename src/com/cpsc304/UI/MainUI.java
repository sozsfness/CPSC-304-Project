package com.cpsc304.UI;
import com.cpsc304.JDBC.DBConnection;
import com.cpsc304.model.OrderStatus;
import com.cpsc304.model.User;

public class MainUI {
    public static User currentUser;
    private static Login loginUI;
    private static ManagerW managerUI;
    private static CustomerW customerUI;
    private static CourierW courierUI;

    public static CourierW getCourierUI() {
        if (courierUI == null)
            courierUI = new CourierW(loginUI);
        return courierUI;
    }

    public static ManagerW getManagerUI() {
        if (managerUI == null)
            managerUI = new ManagerW(loginUI);
        return managerUI;
    }

    public static CustomerW getCustomerUI () {
        if (customerUI == null)
            customerUI = new CustomerW(loginUI);
        return customerUI;
    }

    public static Login getLoginUI() {
        if (loginUI == null)
            loginUI = new Login();
        return loginUI;
    }

    public static void main (String[] args){
        System.out.println(OrderStatus.valueOf("READY"));
        if (DBConnection.connect()) {
            getLoginUI();
        }
        else
            System.out.println("Connection failed");

    }
    public static void customerLogOut(){
        customerUI=null;
    }
    public static void managerLogOut(){
        managerUI=null;
    }
    public static void courierLogOut(){
        courierUI=null;
    }
}
