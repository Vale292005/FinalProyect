package com.example.finalproyect.controllers;

import com.example.finalproyect.Elements.Activity;
import com.example.finalproyect.Elements.ProcessUQ;
import com.example.finalproyect.Elements.Task;
import com.example.finalproyect.MyMap.MyTreeMap;
import com.example.finalproyect.QueueTask.MyQueue;
import com.example.finalproyect.UserTree.Node;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.finalproyect.Elements.Activity.exportToTxt;
import static com.example.finalproyect.MyMap.MyTreeMap.loadFromFile;

public class CrearTareaController {

    @FXML
    private Button Anhadir_Tarea;

    @FXML
    private TextField Descripcion;

    @FXML
    private Label Label;

    @FXML
    private TextField Nombre;

    @FXML
    private TextField Obligatoria;

    @FXML
    private Button Regresar;

    @FXML
    private TextArea Tiempo;
    private Activity activity;
    private ProcessUQ processUQ;
    private MyQueue<Task> queue;


    public void initialize() throws IOException {
       activity=activity.loadFromTxt(SharedFileName.fileName);
       processUQ=processUQ.loadFromTxt("C:\\Users\\Valeria\\Desktop\\process.txt");
    }

    @FXML
    void Anhadir_Tarea(ActionEvent event) {
        // Verificar si la actividad está inicializada
        System.out.println("Estado de activity: " + activity);
        if (activity == null) {
            showErrorAlert("La actividad no está inicializada.");
            return;
        }

        // Obtener y limpiar los datos de los campos
        String name = Nombre.getText().trim();
        String description = Descripcion.getText().trim();
        String mandatoryText = Obligatoria.getText().trim();
        String timeText = Tiempo.getText().trim();

        // Validar campos vacíos
        if (name.isEmpty() || description.isEmpty() || timeText.isEmpty() || mandatoryText.isEmpty()) {
            showErrorAlert("Todos los campos son obligatorios.");
            return;
        }

        try {
            // Parsear valores de la tarea
            boolean mandatory = Boolean.parseBoolean(mandatoryText);  // Verifica si el texto es "true" o "false"
            int time = Integer.parseInt(timeText);  // Convierte el tiempo en un número entero

            // Crear la tarea
            Task task = new Task(name, description, null, mandatory, time);

            // Inicializar la cola si no está creada
            if (queue == null) {
                queue = new MyQueue<>();
            }

            // Agregar la tarea a la cola
            queue.enqueue(task);

            // Asignar la cola a la actividad
            activity.setMyTask(queue);
            List<Node> s=new ArrayList<>();
            s.add(activity);
            processUQ.setChild(s);

            // Exportar los datos a un archivo de texto
            exportToTxt(activity);

            // Mostrar éxito
            showSuccessAlert("Tarea añadida exitosamente.");
        } catch (NumberFormatException e) {
            // Mostrar mensaje de error en caso de problemas con los valores numéricos
            showErrorAlert("Error al ingresar los datos. Verifique el formato de tiempo.");
        } catch (Exception e) {
            // Capturar cualquier otro error no anticipado
            showErrorAlert("Ha ocurrido un error inesperado.");
            e.printStackTrace();  // Para depurar el error
        }
    }


    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Éxito");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorAlert(String m) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("Error");
        errorAlert.setHeaderText(null);
        errorAlert.setContentText(m);
        errorAlert.showAndWait();
    }

    @FXML
    void Regresar(ActionEvent event) {
        try {
            Stage currentStage = (Stage) Regresar.getScene().getWindow();

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

}
