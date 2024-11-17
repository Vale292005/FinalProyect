package com.example.finalproyect.controllers;


import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class HelloController {
    @FXML
    private Label welcomeText;
    @FXML
    private Button Administrador;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    public void OpenUser(ActionEvent event) {
        try {
            Stage currentStage = (Stage) Administrador.getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/User1.fxml"));
            Scene secondScene = new Scene(loader.load());
            Stage secondStage = new Stage();
            secondStage.setScene(secondScene);
            secondStage.show();
            currentStage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void OpenAdmin(ActionEvent event) {
        try {
            // Primero, obtener el Stage de la ventana actual (la principal)
            Stage currentStage = (Stage) Administrador.getScene().getWindow();

            // Cargar el archivo FXML de la segunda ventana
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SecondWindow.fxml"));
            Scene secondScene = new Scene(loader.load());  // Carga la escena de la segunda ventana

            // Crear una nueva ventana (Stage) para la segunda ventana
            Stage secondStage = new Stage();
            secondStage.setTitle("Segunda Ventana");
            secondStage.setScene(secondScene);
            secondStage.show();  // Mostrar la segunda ventana

            // Ahora cerramos la ventana actual (la principal)
            currentStage.close();  // Cerrar la ventana principal

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}