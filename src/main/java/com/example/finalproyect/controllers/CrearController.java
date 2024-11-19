package com.example.finalproyect.controllers;

import com.example.finalproyect.Elements.ProcessUQ;
import com.example.finalproyect.UserTree.NodeSerializer;
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
import java.util.ArrayList;

import static com.example.finalproyect.UserTree.NodeSerializer.serializeNode;


public class CrearController {

    @FXML
    private Button Anhadir_actividad;

    @FXML
    private Label Descripcion;

    @FXML
    private Label Id;

    @FXML
    private TextField Nombre;

    @FXML
    private Button Regreso;
    public static ProcessUQ process;
    private User user;

    @FXML
    void Anhadir_actividad(ActionEvent event) throws IOException {

        String name= Nombre.getText();
        String description=Descripcion.getText();
        String id=Id.getText();
        if(!(name.isEmpty()||description.isEmpty()||id.isEmpty())){

            try {
                process=new ProcessUQ(name,description,new ArrayList<>(),id);
                NodeSerializer.serializeNode(process,"C:\\Users\\Valeria\\Desktop\\procesos.txt");
                Stage currentStage = (Stage) Regreso.getScene().getWindow();

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/CrearActividad.fxml"));
                Scene secondScene = new Scene(loader.load());
                Stage secondStage = new Stage();
                secondStage.setScene(secondScene);
                secondStage.show();
                currentStage.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else{showErrorAlert();}
    }
    private void showErrorAlert() {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("Error");
        errorAlert.setHeaderText("Campos imcompletos");
        errorAlert.setContentText("Por favor, completar los campos.");
        errorAlert.showAndWait();
    }
    @FXML
    void Regreso(ActionEvent event) {
        try {
            Stage currentStage = (Stage) Regreso.getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/User2Windown.fxml"));
            Scene secondScene = new Scene(loader.load());
            Stage secondStage = new Stage();
            secondStage.setScene(secondScene);
            secondStage.show();
            currentStage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}



