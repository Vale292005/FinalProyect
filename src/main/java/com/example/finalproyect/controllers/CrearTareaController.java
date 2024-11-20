package com.example.finalproyect.controllers;

import com.example.finalproyect.Elements.Activity;
import com.example.finalproyect.Elements.ProcessUQ;
import com.example.finalproyect.Elements.Task;
import com.example.finalproyect.Json.prueba;
import com.example.finalproyect.MyMap.MyTreeMap;
import com.example.finalproyect.QueueTask.MyQueue;
import com.example.finalproyect.UserTree.*;
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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.finalproyect.Elements.Activity.exportToTxt;
import static com.example.finalproyect.Elements.ProcessUQ.serializeProcess;
import static com.example.finalproyect.Json.UserTreeSerializer.deserialize;
import static com.example.finalproyect.MyMap.MyTreeMap.loadFromFile;
import static com.example.finalproyect.UserTree.UserTree.escribirEnArchivo;

public class CrearTareaController {
    private ArrayList<String> listTree;
    @FXML
    private Button Ver;

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
    private UserTree treeUser;


    @FXML
    void Anhadir_Tarea(ActionEvent event) {
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
            Task task = new Task(name, description, new ArrayList<>(), mandatory, time);

            if (queue == null) {
                queue = new MyQueue<>();
            }
            queue.enqueue(task);
            NodeSerializer.serializeNode(task,"C:\\Users\\Valeria\\Desktop\\tareas.txt");

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
    @FXML
    void Ver(ActionEvent event) throws IOException {
        listTree=new ArrayList<>();

        User user=new User();
        UserTree treeUser=new UserTree(null);
        String filePathTareas = "C:\\Users\\Valeria\\Desktop\\tareas.txt";
        String filePathActividades = "C:\\Users\\Valeria\\Desktop\\actividades.txt";
        String filePathProcesos = "C:\\Users\\Valeria\\Desktop\\procesos.txt";

        Deserializador deserializador = new Deserializador();

        try {
            MyQueue<Task> tareas = deserializador.deserializeTareas(filePathTareas);
            ArrayList<Activity> actividades = deserializador.deserializeActividades(filePathActividades);
            ArrayList<ProcessUQ> procesos = deserializador.deserializeProcesos(filePathProcesos);
            Activity actividad=actividades.get(0);
            actividad.setMyTask(tareas);
            ProcessUQ proceso=procesos.get(0);
            proceso.getChild().add(actividad);
            user.getChild().addAll(procesos);
            treeUser.setRoot(user);
            prueba serializer = new prueba();

            vaciarArchivo(filePathTareas);
            vaciarArchivo(filePathActividades);
            vaciarArchivo(filePathProcesos);
            listTree.add(treeUser+"");
            prueba serializa = new prueba();
            String filePath = "arbol.json";
            prueba.serialize(treeUser.getRoot(), filePath);
            escribirEnArchivo("C:\\Users\\Valeria\\Desktop\\arbol.txt",listTree);
            prueba.serialize(treeUser.getRoot(), filePath);
            mostrarTiempoTotal("C:\\Users\\Valeria\\Desktop\\tareas.txt");

            // Procesar las tareas, actividades y procesos
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (treeUser != null) {
            Pane treePane = new Pane();

            // Crear la escena para mostrar el árbol
            Scene scene = new Scene(treePane, 1000, 800);
            Stage stage = new Stage();
            stage.setTitle("Jerarquía de Usuario - Procesos - Actividades - Tareas");

            // Obtener el nodo raíz del árbol y comenzar a dibujar
            drawUserTree(treeUser.getRoot(), 500, 50, treePane);

            stage.setScene(scene);
            stage.show();
        } else {
            showErrorAlert("El árbol de usuarios no se ha inicializado.");
        }
    }
    public static void vaciarArchivo(String filePath) throws IOException {
        File file = new File(filePath);

        // Si el archivo existe, vaciar su contenido
        if (file.exists()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                // Escribir un string vacío en el archivo para borrar su contenido
                writer.write("");
            }
        }
    }

    private void drawUserTree(User node, double x, double y, Pane pane) {
        drawNode(x, y, node.getValue(), "Usuario", "#4CAF50", pane);

        // Calcular posiciones para los procesos
        int numProcesses = node.getChild().size();
        double startX = x - (numProcesses - 1) * 200.0;
        double processY = y + 120;

        for (int i = 0; i < numProcesses; i++) {
            if (node.getChild().get(i) instanceof ProcessUQ process) { // Verifica tipo
                double processX = startX + i * 200;

                drawConnector(x, y, processX, processY, pane);
                drawNode(processX, processY, process.getValue(), "Proceso", "#2196F3", pane);

                drawActivities(process, processX, processY, pane);
            }
        }
    }

    private void drawActivities(ProcessUQ process, double processX, double processY, Pane pane) {
        int numActivities = process.getChild().size();
        double startX = processX - (numActivities - 1) * 150.0;
        double activityY = processY + 120;

        for (int i = 0; i < numActivities; i++) {
            if (process.getChild().get(i) instanceof Activity activity) { // Verifica tipo
                double activityX = startX + i * 150;

                drawConnector(processX, processY, activityX, activityY, pane);
                drawNode(activityX, activityY, activity.getValue(), "Actividad", "#FFC107", pane);

                drawTasks(activity, activityX, activityY, pane);
            }
        }
    }

    private void drawTasks(Activity activity, double activityX, double activityY, Pane pane) {
        MyQueue<Task> tasks = activity.getMyTask();
        List<Task> taskList = new ArrayList<>();
        for (Task task : tasks) { // Extraer tareas de la cola
            taskList.add(task);
        }

        int numTasks = taskList.size();
        double startX = activityX - (numTasks - 1) * 100.0;
        double taskY = activityY + 120;

        for (int i = 0; i < numTasks; i++) {
            Task task = taskList.get(i);
            double taskX = startX + i * 100;

            drawConnector(activityX, activityY, taskX, taskY, pane);
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
    public void mostrarTiempoTotal(String filePathTareas) {
        Deserializador deserializador = new Deserializador();
        try {
            // Deserializar las tareas
            MyQueue<Task> tareas = deserializador.deserializeTareas(filePathTareas);

            // Calcular el tiempo total de todas las tareas
            int tiempoTotal = 0;
            for (Task tarea : tareas) {
                tiempoTotal += tarea.getTime();  // Obtiene el tiempo de cada tarea
            }

            // Crear y mostrar una ventana emergente con el tiempo total
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "El tiempo total de todas las tareas es: " + tiempoTotal + " minutos.", ButtonType.OK);
            alert.setTitle("Tiempo Total de Tareas");
            alert.setHeaderText("Cálculo Completo");
            alert.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            // Manejo de errores si la deserialización falla
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error al leer los archivos: " + e.getMessage(), ButtonType.OK);
            alert.setTitle("Error");
            alert.showAndWait();
        }}

    }


