package com.example.finalproyect.application;


import com.example.finalproyect.Elements.Activity;
import com.example.finalproyect.Elements.ProcessUQ;
import com.example.finalproyect.Elements.Task;
import com.example.finalproyect.Json.prueba;
import com.example.finalproyect.QueueTask.MyQueue;
import com.example.finalproyect.UserTree.User;
import com.example.finalproyect.UserTree.UserTree;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

/*public class Main {
    public static void main(String[] args) throws Exception {
        // Crear las tareas, actividades y procesos
        Task task1 = new Task("Tarea 1", "Descripción de la tarea 1", new ArrayList<>(), true, 30);
        Task task2 = new Task("Tarea 2", "Descripción de la tarea 2", new ArrayList<>(), false, 45);
        Task task3 = new Task("Tarea 3", "Descripción de la tarea 3", new ArrayList<>(), false, 60);

        Activity activity1 = new Activity("Actividad 1", "Descripción de la actividad 1", new ArrayList<>(), 1.0, new MyQueue<>(new LinkedList<>()));
        activity1.getMyTask().enqueue(task1);
        activity1.getMyTask().enqueue(task2);
        activity1.getMyTask().enqueue(task3);

        ProcessUQ process1 = new ProcessUQ("Proceso 1", "Descripción del proceso 1", new ArrayList<>(), "ID1");
        process1.getChild().add(activity1);

        // Crear usuario y árbol
        UserTree userTree = new UserTree(new User("root", null, null, null));
        userTree.addChild(process1);

        // Serializar el árbol a JSON
        String filePath = "arbol.json";
        UserTreeSerializer.serialize(userTree.getRoot(), filePath);
        System.out.println("Árbol serializado en: " + filePath);

        try {
            UserTreeSerializer serializer = new UserTreeSerializer();
            File jsonFile = new File("C:\\Users\\Valeria\\eclipse-workspace\\ProyectoFinal\\arbol.json");
            UserTree userTree2 = serializer.deserialize(jsonFile);
            System.out.println(userTree2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(userTree);
            System.out.println(jsonString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


        // Imprimir el árbol deserializado
        /*System.out.println("Árbol deserializado:");
        //deserializedTree.printNodeInfo(); // o el método adecuado para imprimir el árbol

        // Buscar nodos de tipo `ProcessUQ`, `Activity` o `Task`
        String searchName = "Tarea 2";
        ConcurrentLinkedQueue<Node> foundNodes = new ConcurrentLinkedQueue<>();
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        TaskSearchTask searchTask = new TaskSearchTask(deserializedTree, searchName, foundNodes);
        executorService.submit(searchTask);

        executorService.shutdown();
        while (!executorService.isTerminated()) {}

        System.out.println("\nResultados de búsqueda:");
        if (foundNodes.isEmpty()) {
            System.out.println("No se encontraron resultados para: " + searchName);
        } else {
            foundNodes.forEach(node -> System.out.println("Encontrado: " + node.getValue()));
        }

        // Serializar y deserializar la cola MyQueue
        /*String queueFilePath = "queue.json";
        objectMapper.writeValue(new java.io.File(queueFilePath), activity1.getMyTask()); // serializa la MyQueue de las tareas
        System.out.println("Cola de tareas serializada en: " + queueFilePath);*/
import java.util.*;

public class Main {
    public static void main(String[] args) {
        try {
            // Crear las tareas, actividades y procesos
            Task task1 = new Task("Tarea 1", "Descripción de la tarea 1", new ArrayList<>(), true, 30);
            Task task2 = new Task("Tarea 2", "Descripción de la tarea 2", new ArrayList<>(), false, 45);
            Task task3 = new Task("Tarea 3", "Descripción de la tarea 3", new ArrayList<>(), false, 60);

            MyQueue<Task> taskQueue = new MyQueue<>(new LinkedList<>());
            taskQueue.enqueue(task1);
            taskQueue.enqueue(task2);
            taskQueue.enqueue(task3);

            Activity activity1 = new Activity("Actividad 1", "Descripción de la actividad 1", new ArrayList<>(), false, taskQueue);

            ProcessUQ process1 = new ProcessUQ("Proceso 1", "Descripción del proceso 1", new ArrayList<>(), "ID1");
            process1.getChild().add(activity1);

            // Crear usuario y árbol
            User rootUser = new User("Usuario Root", "Usuario principal", new ArrayList<>(), null);
            rootUser.getChild().add(process1);
            UserTree userTree = new UserTree(rootUser);

            // Crear instancia de la clase prueba (serializador)
            prueba serializer = new prueba();

            // Serializar el árbol
            String filePath = "arbol.json";
            prueba.serialize(userTree.getRoot(), filePath);
            System.out.println("Árbol serializado en: " + filePath);

            // Deserializar el árbol
            File jsonFile = new File(filePath);
            UserTree deserializedTree = serializer.deserialize(jsonFile);

            // Imprimir el árbol serializado en formato JSON
            ObjectMapper mapper = new ObjectMapper();
            try {
                String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(userTree.getRoot());
                System.out.println("\nÁrbol en formato JSON:");
                System.out.println(jsonString);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            // Imprimir información del árbol deserializado
            System.out.println("\nInformación del árbol deserializado:");
            printTreeInfo(deserializedTree.getRoot(), "");

        } catch (IOException e) {
            System.err.println("Error durante la serialización/deserialización: " + e.getMessage());
            e.printStackTrace();
        }

    }

    // Método auxiliar para imprimir la información del árbol
    private static void printTreeInfo(User node, String indent) {
        System.out.println(indent + "Usuario: " + node.getValue());
        if (node.getDescription() != null) {
            System.out.println(indent + "Descripción: " + node.getDescription());
        }

        for (Object child : node.getChild()) {
            if (child instanceof ProcessUQ) {
                ProcessUQ process = (ProcessUQ) child;
                System.out.println(indent + "  Proceso: " + process.getValue());
                System.out.println(indent + "  ID Proceso: " + process.getIdProcess());
                System.out.println(indent + "  Descripción: " + process.getDescription());

                for (Object processChild : process.getChild()) {
                    if (processChild instanceof Activity) {
                        Activity activity = (Activity) processChild;
                        System.out.println(indent + "    Actividad: " + activity.getValue());
                        System.out.println(indent + "    Descripción: " + activity.getDescription());
                        System.out.println(indent + "    Mandatory: " + activity.isMandatory());

                        System.out.println(indent + "      Tareas:");
                        MyQueue<Task> taskQueue = activity.getMyTask();
                        if (taskQueue != null && !taskQueue.isEmpty()) {
                            Iterator<Task> iterator = taskQueue.iterator();
                            while (iterator.hasNext()) {
                                Task task = iterator.next();
                                System.out.println(indent + "        - " + task.getValue());
                                System.out.println(indent + "          Descripción: " + task.getDescription());
                                System.out.println(indent + "          Mandatory: " + task.isMandatory());
                                System.out.println(indent + "          Tiempo: " + task.getTime());
                            }
                        }
                    }
                }
            }
        }
    }
}












