/**
 * The main module
 */
module comp3111.examsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires com.google.gson;
    requires jdk.compiler;
    requires org.apache.commons.io;


    opens comp3111.examsystem to javafx.fxml, com.google.gson;
    exports comp3111.examsystem;
    opens comp3111.examsystem.controller to javafx.fxml;
    exports comp3111.examsystem.controller;
}