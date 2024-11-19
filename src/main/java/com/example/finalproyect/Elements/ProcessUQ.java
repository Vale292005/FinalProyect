package com.example.finalproyect.Elements;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.example.finalproyect.QueueTask.MyQueue;
import com.example.finalproyect.UserTree.Node;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

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
        public static void serializeProcess(ProcessUQ process, String filePath) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
                // Write process details
                writer.write("Proceso:\n");
                writer.write("Valor: " + process.getValue() + "\n");
                writer.write("Descripción: " + process.getDescription() + "\n");
                writer.write("ID: " + process.getIdProcess() + "\n");
                writer.write("Obligatorio: false\n");

                // Write activities
                writer.write("Actividades:\n");
                List<Node> activities = process.getChild();
                if (activities != null) {
                    for (Node activityNode : activities) {
                        ProcessUQ activity = (ProcessUQ) activityNode;
                        writer.write("\tActividad:\n");
                        writer.write("\tValor: " + activity.getValue() + "\n");
                        writer.write("\tDescripción: " + activity.getDescription() + "\n");
                        writer.write("\tObligatoria: false\n");

                        // Write tasks
                        writer.write("\tTareas:\n");
                        List<Node> tasks = activity.getChild();
                        if (tasks != null) {
                            for (Node taskNode : tasks) {
                                ProcessUQ task = (ProcessUQ) taskNode;
                                writer.write("\t\tTarea:\n");
                                writer.write("\t\tValor: " + task.getValue() + "\n");
                                writer.write("\t\tDescripción: " + task.getDescription() + "\n");
                                writer.write("\t\tObligatoria: false\n");
                                writer.write("\t\tTiempo: 9\n"); // Default time or from task
                            }
                        }
                    }
                }
                writer.write("\n"); // Separator between entries
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    public static ProcessUQ loadProcess(String filePath) {
        ProcessUQ mainProcess = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            ProcessUQ currentProcess = null;
            ProcessUQ currentActivity = null;
            ProcessUQ currentTask = null;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("Proceso:")) {
                    mainProcess = new ProcessUQ("", "", null, "");
                    currentProcess = mainProcess;
                    currentActivity = null;
                    currentTask = null;
                } else if (line.startsWith("Valor:")) {
                    String value = line.substring(6).trim();
                    if (currentTask != null) {
                        currentTask.setValue(value);
                    } else if (currentActivity != null) {
                        currentActivity.setValue(value);
                    } else if (currentProcess != null) {
                        currentProcess.setValue(value);
                    }
                } else if (line.startsWith("Descripción:")) {
                    String description = line.substring(12).trim();
                    if (currentTask != null) {
                        currentTask.setDescription(description);
                    } else if (currentActivity != null) {
                        currentActivity.setDescription(description);
                    } else if (currentProcess != null) {
                        currentProcess.setDescription(description);
                    }
                } else if (line.startsWith("ID:")) {
                    String id = line.substring(3).trim();
                    if (currentProcess != null) {
                        currentProcess.setIdProcess(id);
                    }
                } else if (line.startsWith("Actividad:")) {
                    currentActivity = new ProcessUQ("", "", null, "");
                    if (currentProcess != null) {
                        if (currentProcess.getChild() == null) {
                            currentProcess.setChild(new ArrayList<>());
                        }
                        currentProcess.getChild().add(currentActivity);
                    }
                    currentTask = null;
                } else if (line.startsWith("Tarea:")) {
                    if (currentActivity != null) {
                        currentTask = new ProcessUQ("", "", null, "");
                        if (currentActivity.getChild() == null) {
                            currentActivity.setChild(new ArrayList<>());
                        }
                        currentActivity.getChild().add(currentTask);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mainProcess;
    }
    @Override
    public void serialize(BufferedWriter writer) throws IOException {
        writer.write("Proceso:\n");
        writer.write("Valor: " + value + "\n");
        writer.write("Descripción: " + description + "\n");
        writer.write("ID: " + idProcess + "\n");
        writer.write("Obligatorio: false\n");

        if (getChild() != null) {
            writer.write("Actividades:\n");
            for (Node child : getChild()) {
                child.serialize(writer);
            }
        }
    }


}





