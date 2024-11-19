package com.example.finalproyect.UserTree;
import com.example.finalproyect.Elements.Activity;
import com.example.finalproyect.Elements.ProcessUQ;
import com.example.finalproyect.Elements.Task;
import com.example.finalproyect.QueueTask.MyQueue;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = User.class, name = "user"),
    @JsonSubTypes.Type(value = ProcessUQ.class, name = "processUQ"),
    @JsonSubTypes.Type(value = Activity.class, name = "activity"),
    @JsonSubTypes.Type(value = Task.class, name = "task")
})
public abstract class Node {

    @JsonProperty
    public String value;

    @JsonProperty
    protected String description;

    @JsonProperty
    protected static List<Node> children;

    public Node() {
        // Constructor vacío
    }

    public Node(String value, String description, List<Node> children) {
        this.value = value;
        this.description = description;
        this.children = children;
    }

    @JsonProperty
    public String getDescription() {
        return description;
    }

    @JsonProperty
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty
    public String getValue() {
        return value;
    }

    @JsonProperty
    public void setValue(String value) {
        this.value = value;
    }

    @JsonProperty
    public List<Node> getChild() {
        return children;
    }

    @JsonProperty
    public void setChild(List<Node> children) {
        this.children = children;
    }

    public abstract void add(Node child);
    public abstract void printNodeInfo();
    public abstract void serialize(BufferedWriter writer) throws IOException;

    public void deserialize(BufferedReader reader) throws IOException {
        String line;
        List<Node> children = new ArrayList<>();
        ProcessUQ currentProcess = null;
        Activity currentActivity = null;
        User user=null;

        while ((line = reader.readLine()) != null) {
            line = line.trim(); // Limpiar espacios innecesarios

            if (line.startsWith("User:")) {
                // Leer la sección del User
                String userValue = reader.readLine().trim().substring(7); // Omitir "Valor: "
                String userDescription = reader.readLine().trim().substring(12); // Omitir "Descripción: "
                String userName = reader.readLine().trim().substring(6); // Omitir "User: "
                user = new User(userValue, userDescription, children, userName);
            }
            else if (line.startsWith("Proceso:")) {
                // Leer la sección del Proceso
                String processValue = reader.readLine().trim().substring(8); // Omitir "Valor: "
                String processDescription = reader.readLine().trim().substring(12); // Omitir "Descripción: "
                String processId = reader.readLine().trim().substring(3); // Omitir "ID: "
                currentProcess = new ProcessUQ(processValue, processDescription, children, processId);
                if (user != null) {
                    children.add(currentProcess); // Añadir el proceso al nodo hijo si user no es null
                }
            }
            else if (line.startsWith("Actividad:")) {
                // Leer la sección de Actividad
                String activityValue = reader.readLine().trim().substring(10); // Omitir "Valor: "
                String activityDescription = reader.readLine().trim().substring(12); // Omitir "Descripción: "
                boolean mandatory = Boolean.parseBoolean(reader.readLine().trim().substring(11)); // Omitir "Obligatoria: "
                currentActivity = new Activity(activityValue, activityDescription, children, mandatory, new MyQueue<>());
                if (currentProcess != null) {
                    currentProcess.getChild().add(currentActivity); // Añadir actividad al proceso si no es null
                }
            }
            else if (line.startsWith("Tarea:")) {
                // Leer la sección de Tarea
                String taskValue = reader.readLine().trim().substring(7); // Omitir "Valor: "
                String taskDescription = reader.readLine().trim().substring(12); // Omitir "Descripción: "
                boolean taskMandatory = Boolean.parseBoolean(reader.readLine().trim().substring(11)); // Omitir "Obligatoria: "
                int taskTime = Integer.parseInt(reader.readLine().trim().substring(7)); // Omitir "Tiempo: "
                Task task = new Task(taskValue, taskDescription, new ArrayList<>(), taskMandatory, taskTime);
                if (currentActivity != null) {
                    currentActivity.getMyTask().enqueue(task); // Añadir la tarea a la cola de tareas si la actividad no es null
                }
            }
        }
    }

    public Node getChildren(int i) {
        return children.get(i);
    }
}



