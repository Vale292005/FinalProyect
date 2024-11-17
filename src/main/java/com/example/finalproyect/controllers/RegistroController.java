package com.example.finalproyect.controllers;

import com.example.finalproyect.MyMap.MyTreeMap;
import com.example.finalproyect.UserTree.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Iterator;

import static com.example.finalproyect.MyMap.MyTreeMap.loadFromFile;

public class RegistroController {

    @FXML
    private Button Aceptar;

    @FXML
    private TextField Contrasenha;

    @FXML
    private Button Regresar;

    @FXML
    private TextField Usuario;
    @FXML
    private TextField Correo;


    private MyTreeMap<User,String> UserMap;
    private MyTreeMap<User,String> gmailMap;

    public void initialize(){
        UserMap=new MyTreeMap<>();
        gmailMap=new MyTreeMap<>();
        UserMap=loadFromFile("C:\\Users\\Valeria\\Desktop\\user.txt");
    }

    @FXML
    void aceptar(ActionEvent event) {
        String cent = Usuario.getText();

        // Verifica si el campo está vacío o es null
        if (cent == null || cent.isEmpty()) {
            showErrorAlert();  // Alerta de campo vacío
            return;
        }

        User user = new User(null, null, null, cent);
        String contrasenhaText = Contrasenha.getText();
        String correo = Correo.getText();

        // Verifica si los campos no están vacíos
        if (!user.getUser().isEmpty() && !contrasenhaText.isEmpty() && !correo.isEmpty()) {

            // Verifica si el usuario ya existe en UserMap sin usar containsKey()
            boolean userExists = false;
            for (User existingUser : UserMap.keySet()) {
                if (existingUser.getUser() != null && existingUser.getUser().equals(user.getUser())) {
                    userExists = true;
                    break;
                }
            }
            if (!userExists) {
                UserMap.put(user, contrasenhaText);
                UserMap.saveToFile("C:\\Users\\Valeria\\Desktop\\user.txt");  // Guarda el mapa en el archivo
                gmailMap.put(user, correo);

                try {
                    // Cargar la siguiente vista si el usuario fue creado exitosamente
                    Stage currentStage = (Stage) Aceptar.getScene().getWindow();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/hello-view.fxml"));
                    Scene secondScene = new Scene(loader.load());
                    Stage secondStage = new Stage();
                    secondStage.setScene(secondScene);
                    secondStage.show();
                    currentStage.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                showSuccessAlert();  // Alerta de éxito
            } else {
                // Si el usuario ya existe, muestra una alerta
                showErrorRepeatAlert();  // Alerta de usuario repetido
            }
        } else {
            // Si falta algún campo
            showErrorAlert();  // Alerta de campos vacíos
        }
    }





    private void showSuccessAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Éxito");
        alert.setHeaderText(null);
        alert.setContentText("¡Usuario creado con éxito!");
        alert.showAndWait();
    }


    @FXML
    void Return(ActionEvent event) {
        try {
            Stage currentStage = (Stage) Aceptar.getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/hello-view.fxml"));
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
    void getContrasenha(ActionEvent event) {

    }

    @FXML
    void getUser(ActionEvent event) {

    }
    private void showErrorAlert() {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("Error");
        errorAlert.setHeaderText("No ha llenado ningun campo");
        errorAlert.setContentText("Por favor, complete los campos.");
        errorAlert.showAndWait();
    }
    private void showErrorRepeatAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error");
        alert.setHeaderText("Usuario duplicado");
        alert.setContentText("El usuario ya existe. Por favor, elige un nombre de usuario diferente.");
        alert.showAndWait();
    }


    public void getCorreo(ActionEvent event) {
    }
}
