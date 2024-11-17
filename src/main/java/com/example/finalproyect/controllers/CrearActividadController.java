package com.example.finalproyect.controllers;

import com.example.finalproyect.Elements.Activity;
import com.example.finalproyect.Elements.ProcessUQ;
import com.example.finalproyect.UserTree.Node;
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

import static com.example.finalproyect.Elements.Activity.exportToTxt;

public class CrearActividadController {

    @FXML
    private Button Anhadir_Actividad;

    @FXML
    private Label Descripcion;

    @FXML
    private TextField Nombre;

    @FXML
    private TextField Obligatoria;

    @FXML
    private Button Regresar;

    private Activity activity;
    private ProcessUQ processUQ;

    @FXML
    public void initialize() {
        try {
            // Asumiendo que ProcessUQ tiene un método estático loadFromTxt
            processUQ = ProcessUQ.loadFromTxt("C:\\Users\\Valeria\\Desktop\\process.txt");
        } catch (IOException e) {
            showErrorAlert("Error al cargar el proceso", "No se pudo cargar el archivo del proceso.");
            e.printStackTrace();
        }
    }

    @FXML
    void Anhadir_Actividad(ActionEvent event) {
        String name = Nombre.getText();
        String description = Descripcion.getText();

        // Validar que Obligatoria.getText() no sea null o vacío
        String obligatoriaText = Obligatoria.getText();
        if (obligatoriaText == null || obligatoriaText.trim().isEmpty()) {
            showErrorAlert("Error", "El campo 'Obligatoria' no puede estar vacío");
            return;
        }

        boolean mandatory = Boolean.parseBoolean(obligatoriaText);

        if (!(name.isEmpty() || description.isEmpty())) {
            Activity activity = new Activity(name, description, null, mandatory, null);
            exportToTxt(activity);

            if (processUQ != null) {
                ArrayList<Node> s = new ArrayList<>();
                s.add(activity);
                processUQ.setChild(s);

                try {
                    openCrearTareaWindow();
                } catch (Exception e) {
                    showErrorAlert("Error", "No se pudo abrir la ventana de crear tarea");
                    e.printStackTrace();
                }
            } else {
                showErrorAlert("Error", "El proceso no está inicializado");
            }
        } else {
            showErrorAlert("Campos incompletos", "Por favor, complete todos los campos.");
        }
    }

    private void openCrearTareaWindow() throws IOException {
        Stage currentStage = (Stage) Anhadir_Actividad.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/CrearTarea.fxml"));
        Scene secondScene = new Scene(loader.load());
        Stage secondStage = new Stage();
        secondStage.setScene(secondScene);
        secondStage.show();
        currentStage.close();
    }

    private void showErrorAlert(String header, String content) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("Error");
        errorAlert.setHeaderText(header);
        errorAlert.setContentText(content);
        errorAlert.showAndWait();
    }

    @FXML
    void Regresar(ActionEvent event) {
        try {
            Stage currentStage = (Stage) Regresar.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CrearWindow.fxml"));
            Scene secondScene = new Scene(loader.load());
            Stage secondStage = new Stage();
            secondStage.setScene(secondScene);
            secondStage.show();
            currentStage.close();
        } catch (Exception e) {
            showErrorAlert("Error", "No se pudo regresar a la ventana anterior");
            e.printStackTrace();
        }
    }
}