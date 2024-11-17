package com.example.finalproyect.Elements;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.example.finalproyect.QueueTask.MyQueue;
import com.example.finalproyect.UserTree.Node;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("ProcessUQ")
public class ProcessUQ extends Node {
    String idProcess;

    public ProcessUQ() {
        // Constructor vacío para Jackson
    }

    public ProcessUQ(String value, String description, List<Node> children, String id) {
        super(value, description, children);
        this.idProcess = id;
    }

    @JsonProperty
    public String getId() {
        return idProcess;
    }

    @JsonProperty
    public void setId(String id) {
        this.idProcess = id;
    }

    @Override
    public void add(Node child) {
        // Agregar hijo
    }

    @Override
    public void printNodeInfo() {
        System.out.println("Valor: " + value + ", Descripción: " + description + ", ID: " + idProcess);
    }

    public List<Node> getChild() {
        return children;
    }

    @JsonProperty
    public String getIdProcess() {
        return idProcess;
    }

    @JsonProperty
    public void setIdProcess(String idProcess) {
        this.idProcess = idProcess;
    }

    public String toString() {
        return "Process [value=" + value + ", description=" + description + ", idProcess=" + idProcess + "]";
    }

    public static void exportToTxt(ProcessUQ process, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath,true))) {
            // Verificar si el proceso es null
            if (process == null) {
                writer.write("Error: El proceso es null.\n");
                return;
            }

            // Escribir los datos del proceso en formato texto
            writer.write("Actividad:\n");
            writer.write("Valor: " + process.getValue() + "\n");
            writer.write("Descripción: " + process.getDescription() + "\n");
            writer.write("ID del proceso: " + process.idProcess + "\n");

            // Verificar si la lista de hijos del proceso es null o vacía
            if (process.getChild() != null && !process.getChild().isEmpty()) {
                boolean hasActivities = false; // Flag para saber si hay actividades
                // Escribir las actividades y sus tareas
                for (Node child : process.getChild()) {
                    if (child instanceof Activity) {
                        hasActivities = true;  // Se encontró una actividad
                        Activity activity = (Activity) child;
                        writer.write("Actividad:\n");
                        writer.write("Valor: " + activity.getValue() + "\n");
                        writer.write("Descripción: " + activity.getDescription() + "\n");
                        writer.write("Obligatoria: " + activity.isMandatory() + "\n");

                        // Verificar si la lista de tareas es null o vacía
                        writer.write("Tareas:\n");
                        if (activity.getMyTask() != null && !activity.getMyTask().isEmpty()) {
                            for (Task task : activity.getMyTask()) {
                                writer.write("\tTarea:\n");
                                writer.write("\t\tValor: " + task.getValue() + "\n");
                                writer.write("\t\tDescripción: " + task.getDescription() + "\n");
                                writer.write("\t\tObligatoria: " + task.isMandatory() + "\n");
                                writer.write("\t\tTiempo: " + task.getTime() + "\n");
                            }
                        } else {
                            writer.write("\tNo hay tareas asignadas.\n");
                        }
                    }
                }
                if (!hasActivities) {
                    writer.write("El proceso no tiene actividades.\n");
                }
            } else {
                writer.write("El proceso no tiene actividades.\n");
            }
        }
    }




    public static ProcessUQ loadFromTxt(String filePath) throws IOException {
        // Verificar si el archivo es null o si la ruta está vacía
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("La ruta del archivo es nula o vacía.");
        }

        // Verificar si el archivo existe antes de intentar abrirlo
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IllegalArgumentException("El archivo no existe en la ruta especificada.");
        }

        // Si el archivo existe, proceder con la lectura
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String value = null;
            String description = null;
            String idProcess = null;
            List<Node> children = new ArrayList<>();

            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue; // Ignorar líneas vacías

                String[] parts = line.split(":", 2);
                if (parts.length < 2) continue;

                String key = parts[0].trim();
                String valuePart = parts[1].trim();

                switch (key) {
                    case "Valor":
                        value = valuePart;
                        break;
                    case "Descripción":
                        description = valuePart;
                        break;
                    case "ID":
                        idProcess = valuePart;
                        break;
                    case "Actividades":
                        if (!valuePart.isEmpty()) {
                            String[] activities = valuePart.split(",");
                            for (String activityName : activities) {
                                children.add(new Activity(activityName.trim(), null, new ArrayList<>(), false, new MyQueue<>()));
                            }
                        }
                        break;
                    default:
                        break;
                }
            }

            // Proporcionar valores predeterminados si los campos no se encuentran
            if (value == null) {
                value = "Valor por defecto"; // Proporciona un valor predeterminado
            }
            if (description == null) {
                description = "Descripción por defecto"; // Proporciona una descripción predeterminada
            }
            if (idProcess == null) {
                idProcess = "ID por defecto"; // Proporciona un ID predeterminado
            }

            // Crear y devolver el objeto ProcessUQ
            return new ProcessUQ(value, description, children, idProcess);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("El archivo no se encontró en la ruta especificada: " + filePath, e);
        }
    }



}

