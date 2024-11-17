package com.example.finalproyect.Json;

import com.example.finalproyect.Elements.Activity;
import com.example.finalproyect.Elements.ProcessUQ;
import com.example.finalproyect.Elements.Task;
import com.example.finalproyect.QueueTask.MyQueue;
import com.example.finalproyect.UserTree.User;
import com.example.finalproyect.UserTree.UserTree;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class UserTreeImporter {

    public static UserTree importFromJson(String filePath) throws IOException {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("La ruta del archivo no puede ser null o vacía");
        }

        File jsonFile = new File(filePath);
        if (!jsonFile.exists()) {
            throw new IOException("El archivo no existe: " + filePath);
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(jsonFile);

        if (rootNode == null) {
            throw new IOException("No se pudo leer el JSON del archivo");
        }

        // Crear el usuario root con validación
        User rootUser = createUserFromNode(rootNode);
        if (rootUser == null) {
            throw new IOException("No se pudo crear el usuario root");
        }

        // Procesar los children (procesos)
        if (rootNode.has("children") && !rootNode.get("children").isNull()) {
            JsonNode childrenNode = rootNode.get("children");
            for (JsonNode processNode : childrenNode) {
                ProcessUQ process = createProcessFromNode(processNode);
                if (process != null) {
                    rootUser.getChild().add(process);

                    // Procesar las actividades del proceso
                    if (processNode.has("children") && !processNode.get("children").isNull()) {
                        for (JsonNode activityNode : processNode.get("children")) {
                            Activity activity = createActivityFromNode(activityNode);
                            if (activity != null) {
                                process.getChild().add(activity);

                                // Procesar las tareas de la actividad
                                if (activityNode.has("myTask") && !activityNode.get("myTask").isNull()) {
                                    JsonNode myTaskNode = activityNode.get("myTask");
                                    if (myTaskNode.has("queue") && !myTaskNode.get("queue").isNull()) {
                                        MyQueue<Task> taskQueue = createTaskQueueFromNode(myTaskNode.get("queue"));
                                        if (taskQueue != null) {
                                            activity.setMyTask(taskQueue);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return new UserTree(rootUser);
    }

    private static User createUserFromNode(JsonNode node) {
        if (node == null || !node.has("value") || !node.has("description")) {
            return null;
        }

        String value = node.get("value").asText();
        String description = node.get("description").asText();

        if (value == null || description == null) {
            return null;
        }

        return new User(value, description, new ArrayList<>(), null);
    }

    private static ProcessUQ createProcessFromNode(JsonNode node) {
        if (node == null || !node.has("value") || !node.has("description") || !node.has("idProcess")) {
            return null;
        }

        String value = node.get("value").asText();
        String description = node.get("description").asText();
        String idProcess = node.get("idProcess").asText();

        if (value == null || description == null || idProcess == null) {
            return null;
        }

        return new ProcessUQ(value, description, new ArrayList<>(), idProcess);
    }

    private static Activity createActivityFromNode(JsonNode node) {
        if (node == null || !node.has("value") || !node.has("description") || !node.has("mandatory")) {
            return null;
        }

        String value = node.get("value").asText();
        String description = node.get("description").asText();
        boolean mandatory = node.get("mandatory").asBoolean();

        if (value == null || description == null) {
            return null;
        }

        return new Activity(value, description, new ArrayList<>(), mandatory, new MyQueue<>(new LinkedList<>()));
    }

    private static MyQueue<Task> createTaskQueueFromNode(JsonNode queueNode) {
        if (queueNode == null || !queueNode.isArray()) {
            return new MyQueue<>(new LinkedList<>());
        }

        MyQueue<Task> taskQueue = new MyQueue<>(new LinkedList<>());

        for (JsonNode taskNode : queueNode) {
            Task task = createTaskFromNode(taskNode);
            if (task != null) {
                taskQueue.enqueue(task);
            }
        }

        return taskQueue;
    }

    private static Task createTaskFromNode(JsonNode node) {
        if (node == null || !node.has("value") || !node.has("description") ||
                !node.has("mandatory") || !node.has("time")) {
            return null;
        }

        String value = node.get("value").asText();
        String description = node.get("description").asText();
        boolean mandatory = node.get("mandatory").asBoolean();
        int time = node.get("time").asInt();

        if (value == null || description == null) {
            return null;
        }

        return new Task(value, description, new ArrayList<>(), mandatory, time);
    }
}
