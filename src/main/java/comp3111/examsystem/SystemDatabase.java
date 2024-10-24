package comp3111.examsystem;

import java.util.HashMap;

public class SystemDatabase {
    /*
    Used to store all important, shared information the system requires
     */
    // keep different account types separate
    // maps Username -> Password -> Instance of Account
    HashMap<String,HashMap<String, Student>> studentLogin;
//    HashMap<String, HashMap<String, Teacher>> teacherLogin;
//    HashMap<String, HashMap<String, Manager>> managerLogin;


    // perhaps login is done through the system.
}
