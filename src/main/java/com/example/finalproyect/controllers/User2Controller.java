package com.example.finalproyect.controllers;

import com.example.finalproyect.Elements.Activity;
import com.example.finalproyect.Elements.ProcessUQ;
import com.example.finalproyect.Elements.Task;
import com.example.finalproyect.Json.prueba;
import com.example.finalproyect.QueueTask.MyQueue;
import com.example.finalproyect.UserTree.User;
import com.example.finalproyect.UserTree.UserTree;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class User2Controller {

    @FXML
    private Button Crear;

    @FXML
    private Button Editar;

    @FXML
    private Button Eliminar;

    @FXML
    private ListView<UserTree> Listaprocesos;

    @FXML
    private Button Regresar;

    @FXML
    private Button Ver;

    public void initialize() throws IOException {
        prueba serializer = new prueba();
        ArrayList<UserTree> trees=new ArrayList<>();
        File jsonFile = new File("arbol.json");
        UserTree deserializedTree = serializer.deserialize(jsonFile);
        deserializedTree.setRoot(deserializedTree.getRoot().getChildren(0));
        Listaprocesos.getItems().add(deserializedTree);}
    @FXML
    void Crear(ActionEvent event) {
        try {
            Stage currentStage = (Stage) Crear.getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CrearWindow.fxml"));
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
    void Editar(ActionEvent event) {

    }

    @FXML
    void Eliminar(ActionEvent event) {
        UserTree selectedItem = Listaprocesos.getSelectionModel().getSelectedItem();

        // Comprobar si hay un elemento seleccionado
        if (selectedItem != null) {
            // Mostrar una alerta de confirmación
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmación");
            alert.setHeaderText("Eliminar elemento");
            alert.setContentText("¿Estás seguro de que quieres eliminar este elemento?");

            // Esperar la respuesta del usuario
            alert.showAndWait().ifPresent(response -> {
                // Si el usuario selecciona "OK", eliminar el elemento
                if (response == ButtonType.OK) {
                    Listaprocesos.getItems().remove(selectedItem);
                    // Mostrar una alerta de éxito
                    showSuccessAlert();
                } else {
                    // Si el usuario cancela, mostrar una alerta de cancelación
                    showCancelAlert();
                }
            });
        } else {
            // Si no hay ningún elemento seleccionado, mostrar una alerta de error
            showErrorAlert();

        }}
    private void showSuccessAlert() {
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("Éxito");
        successAlert.setHeaderText("Elemento eliminado");
        successAlert.setContentText("El elemento ha sido eliminado correctamente.");
        successAlert.showAndWait();
    }

    // Alerta de cancelación
    private void showCancelAlert() {
        Alert cancelAlert = new Alert(Alert.AlertType.INFORMATION);
        cancelAlert.setTitle("Cancelado");
        cancelAlert.setHeaderText("Operación cancelada");
        cancelAlert.setContentText("La eliminación del elemento ha sido cancelada.");
        cancelAlert.showAndWait();
    }

    // Alerta de error (cuando no se selecciona un elemento)
    private void showErrorAlert() {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("Error");
        errorAlert.setHeaderText("No se seleccionó ningún elemento");
        errorAlert.setContentText("Por favor, selecciona un elemento para eliminar.");
        errorAlert.showAndWait();
    }

    @FXML
    void Regresar(ActionEvent event) {
        try {
            Stage currentStage = (Stage) Regresar.getScene().getWindow();

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
    void Ver(ActionEvent event) {
        UserTree userTree =Listaprocesos.getSelectionModel().getSelectedItem();
        if(userTree!=null){        Pane treePane = new Pane();

            // Crear la escena para mostrar el árbol
            Scene scene = new Scene(treePane, 1000, 800);
            Stage stage = new Stage();
            stage.setTitle("Jerarquía de Usuario - Procesos - Actividades - Tareas");

            // Obtener el root user del árbol y comenzar a dibujar
            drawUserTree(userTree.getRoot(), 500, 50, treePane);

            stage.setScene(scene);
            stage.show();}
        else{showErrorAlert();}

    }

    private void drawUserTree(User node, double x, double y, Pane pane) {
        // Dibujar nodo de usuario
        drawNode(x, y, node.getValue(), "Usuario", "#4CAF50", pane);

        // Calcular posiciones para los procesos
        int numProcesses = node.getChild().size();
        double startX = x - (numProcesses - 1) * 200.0;
        double processY = y + 120;

        // Dibujar procesos
        for (int i = 0; i < numProcesses; i++) {
            ProcessUQ process = (ProcessUQ) node.getChild().get(i);
            double processX = startX + i * 200;

            // Línea conectora
            drawConnector(x, y, processX, processY, pane);

            // Dibujar proceso
            drawNode(processX, processY, process.getValue(), "Proceso", "#2196F3", pane);

            // Dibujar actividades del proceso
            drawActivities(process, processX, processY, pane);
        }
    }

    private void drawActivities(ProcessUQ process, double processX, double processY, Pane pane) {
        int numActivities = process.getChild().size();
        double startX = processX - (numActivities - 1) * 150.0;
        double activityY = processY + 120;

        for (int i = 0; i < numActivities; i++) {
            Activity activity = (Activity) process.getChild().get(i);
            double activityX = startX + i * 150;

            // Línea conectora
            drawConnector(processX, processY, activityX, activityY, pane);

            // Dibujar actividad
            drawNode(activityX, activityY, activity.getValue(), "Actividad", "#FFC107", pane);

            // Dibujar tareas de la actividad
            drawTasks(activity, activityX, activityY, pane);
        }
    }

    private void drawTasks(Activity activity, double activityX, double activityY, Pane pane) {
        MyQueue<Task> tasks = activity.getMyTask();
        List<Task> taskList = new ArrayList<>();
        for (Task task : tasks) {
            taskList.add(task);
        }
        int numTasks = taskList.size();
        double startX = activityX - (numTasks - 1) * 100.0;
        double taskY = activityY + 120;

        for (int i = 0; i < numTasks; i++) {
            Task task = taskList.get(i);
            double taskX = startX + i * 100;

            // Línea conectora
            drawConnector(activityX, activityY, taskX, taskY, pane);

            // Dibujar tarea
            drawNode(taskX, taskY, task.getValue(), "Tarea", "#FF5722", pane);
        }
    }

    private void drawNode(double x, double y, String name, String type, String color, Pane pane) {
        // Crear grupo para el nodo
        Group nodeGroup = new Group();

        // Crear círculo para el nodo
        Circle circle = new Circle(x, y, 30);
        circle.setFill(Color.web(color));
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(2);

        // Crear texto para el nombre
        Text nameText = new Text(name);
        nameText.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        nameText.setFill(Color.WHITE);
        double textX = x - nameText.getBoundsInLocal().getWidth() / 2;
        double textY = y;
        nameText.setX(textX);
        nameText.setY(textY);

        // Crear texto para el tipo
        Text typeText = new Text(type);
        typeText.setFont(Font.font("Arial", 10));
        typeText.setFill(Color.WHITE);
        double typeX = x - typeText.getBoundsInLocal().getWidth() / 2;
        double typeY = y + 15;
        typeText.setX(typeX);
        typeText.setY(typeY);

        // Añadir efectos
        circle.setEffect(new DropShadow(10, Color.GRAY));

        // Añadir tooltip
        Tooltip tooltip = new Tooltip(name + "\nTipo: " + type);
        Tooltip.install(circle, tooltip);

        // Añadir interactividad
        circle.setOnMouseEntered(e -> circle.setFill(Color.web(color).brighter()));
        circle.setOnMouseExited(e -> circle.setFill(Color.web(color)));

        pane.getChildren().addAll(circle, nameText, typeText);
    }

    private void drawConnector(double startX, double startY, double endX, double endY, Pane pane) {
        Line line = new Line(startX, startY + 30, endX, endY - 30);
        line.setStroke(Color.GRAY);
        line.setStrokeWidth(2);
        line.getStrokeDashArray().addAll(5d);
        pane.getChildren().add(line);
    }

    // Método recursivo para crear el árbol a partir del JSONObject
    private TreeItem<String> createTree(JSONObject jsonObject) {
        String value = jsonObject.optString("value", "No Value");
        String description = jsonObject.optString("description", "No Description");

        // Crear un nodo para el valor del nodo actual
        TreeItem<String> treeItem = new TreeItem<>(value + ": " + description);
        treeItem.setExpanded(true);

        // Si el nodo tiene hijos, procesarlos recursivamente
        if (jsonObject.has("children")) {
            JSONArray children = jsonObject.getJSONArray("children");
            for (int i = 0; i < children.length(); i++) {
                JSONObject child = children.getJSONObject(i);
                TreeItem<String> childItem = createTree(child);
                treeItem.getChildren().add(childItem);
            }
        }

        return treeItem;
    }

}


