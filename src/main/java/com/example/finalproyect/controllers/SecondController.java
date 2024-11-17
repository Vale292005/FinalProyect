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

import java.io.IOException;

import static com.example.finalproyect.MyMap.MyTreeMap.loadFromFile;


public class SecondController {
    private MyTreeMap<User,String>MyMap;

    @FXML
    private Button Aceptar;

    @FXML
    private TextField Contrasenha;

    @FXML
    private Button Regresar;

    @FXML
    private TextField Usuario;



    public void initialize() {

        try {
            // Crear el mapa
            MyMap = new MyTreeMap<>();

            // Cargar el archivo
            System.out.println("Cargando el archivo...");
            MyMap=loadFromFile("C:\\Users\\Valeria\\Desktop\\user.txt");

            // Verificar si el mapa tiene entradas
            System.out.println("Imprimiendo las entradas cargadas...");
            for (MyTreeMap.Entry<User, String> entry : MyMap.entrySet()) {
                System.out.println("Usuario: " + entry.getKey() + " | Valor: " + entry.getValue());
            }

            // Verificar si el mapa está vacío después de cargarlo
            if (MyMap.size() == 0) {
                System.out.println("El mapa está vacío.");
            } else {
                System.out.println("El mapa contiene " + MyMap.size() + " entradas.");
            }
        } catch (Exception e) {
            System.out.println();
            System.out.println(e.getMessage());
            System.out.println();
            e.printStackTrace();
        }
    }


    @FXML
    void OpenAdmin1(ActionEvent event) throws IOException {

        try {
            String cent=Usuario.getText();
            User user=new User(null,null,null,cent);
            String contrasenhaText=Contrasenha.getText();

            if(MyMap!=null) {
                for (MyTreeMap.Entry<User, String> entry : MyMap.entrySet()) {
                    System.out.println("Checking equality:");
                    System.out.println("Key: " + entry.getKey() + " | Input User: " + user);
                    System.out.println("Key.equals(user): " + entry.getKey().equals(user));
                    if (user.equals(entry.getKey())) {
                        Stage currentStage = (Stage) Aceptar.getScene().getWindow();
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/TerceraWindow.fxml"));
                        Scene secondScene = new Scene(loader.load());
                        Stage secondStage = new Stage();
                        secondStage.setTitle("Tercera Ventana");
                        secondStage.setScene(secondScene);
                        secondStage.show();
                        currentStage.close();  // Close the main window
                    }
                }
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText(null);
                alert.setContentText("Ingreso invalido");
                alert.showAndWait();
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
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
            currentStage.close();  // Cerrar la ventana principal

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
}
