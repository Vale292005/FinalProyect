module com.example.finalproyect {
    // Exporting your package so JavaFX can access it
    exports com.example.finalproyect.application;
    exports com.example.finalproyect.controllers;
    exports com.example.finalproyect.QueueTask; // Export any other package needed for access

    // Requires JavaFX modules
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;

    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.annotation;

    exports com.example.finalproyect.UserTree;
    exports com.example.finalproyect.Elements;

    // Requires Apache Commons Collections
    requires org.apache.commons.collections4;

    // Requires Jakarta Mail
    requires jakarta.mail;

    // Requires POI
    requires org.apache.poi.ooxml;


    // Requires JFoenix
    requires com.jfoenix;
    requires com.google.gson;
    requires org.json;
    opens com.example.finalproyect.UserTree to com.google.gson, com.fasterxml.jackson.databind;

    opens com.example.finalproyect.controllers;
}
