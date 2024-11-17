package com.example.finalproyect.controllers;

import com.example.finalproyect.MyMap.MyTreeMap;
import com.example.finalproyect.UserTree.User;
import com.example.finalproyect.UserTree.UserTree;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.TreeMap;

import static com.example.finalproyect.Json.JsonToCsvExporter.exportJsonToCsv;
import static com.example.finalproyect.Json.JsonToCsvExporter.readJsonFromFile;

public class TercerController {

    @FXML
    private Button Eliminar;

    @FXML
    private Button Exportar;

    @FXML
    private Button Importar;

    @FXML
    private ListView<MyTreeMap<User,UserTree>> ListTree;

    @FXML
    private Button Regresar;

    @FXML
    private Button Ver;

    private MyTreeMap<User, UserTree> treeMap;

    public void initialize (){
        treeMap=new MyTreeMap<>();
        treeMap.loadFromJson("C:\\Users\\Valeria\\eclipse-workspace\\ProyectoFinal\\arbol.json");

        ListTree.getItems().add(treeMap);
        ListTree.getItems().add(treeMap);

    }
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
    void Eliminar(ActionEvent event) {
        MyTreeMap selectedItem = ListTree.getSelectionModel().getSelectedItem();

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
                    ListTree.getItems().remove(selectedItem);
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

    @FXML
    void Exportar(ActionEvent event) {
        MyTreeMap selectedItem = ListTree.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {        try {
            // Ruta del archivo JSON (reemplaza con la ruta de tu archivo JSON)
            String jsonFilePath = "arbol.json"; // Cambia la ruta según sea necesario
            JSONObject jsonObject = readJsonFromFile(jsonFilePath);

            // Ruta donde guardar el archivo CSV (reemplaza con la ruta donde quieras guardarlo)
            String csvFilePath = "output.csv"; // Cambia la ruta de salida
            exportJsonToCsv(jsonObject, csvFilePath);

        } catch (IOException e) {
            e.printStackTrace();
        }}
        else{showErrorAlert();}


    }

    @FXML
    void Importar(ActionEvent event) {
        MyTreeMap selectedItem = ListTree.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            try {
            String jsonFilePath = "arbol1.json"; // Cambia la ruta según sea necesario
            JSONObject jsonObject = readJsonFromFile(jsonFilePath);

            System.out.println("Contenido del JSON: " + jsonObject.toString(2)); // Muestra el JSON formateado

        } catch (IOException e) {
            e.printStackTrace();
        }}
        else{showErrorAlert();}

    }

    @FXML
    void Regresar(ActionEvent event) {
        try {
            Stage currentStage = (Stage) Regresar.getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SecondWindow.fxml"));
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
    void Ver(ActionEvent event) {
        try {
            // Cargar el JSON desde el archivo seleccionado
            MyTreeMap selectedItem = ListTree.getSelectionModel().getSelectedItem();
            String jsonString = new String(Files.readAllBytes(Paths.get(selectedItem.toString())));
            JSONObject jsonObject = new JSONObject(jsonString);

            // Crear una nueva ventana (Stage)
            Stage treeWindow = new Stage();
            treeWindow.setTitle("Árbol JSON");

            // Crear el TreeItem raíz
            TreeItem<String> rootItem = createTree(jsonObject);

            // Crear el TreeView con el TreeItem raíz
            TreeView<String> treeView = new TreeView<>(rootItem);

            // Crear la escena para la nueva ventana
            StackPane root = new StackPane();
            root.getChildren().add(treeView);
            Scene scene = new Scene(root, 600, 400);
            treeWindow.setScene(scene);
            treeWindow.show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace(); // Maneja posibles excepciones, como la no existencia del archivo
        }
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

