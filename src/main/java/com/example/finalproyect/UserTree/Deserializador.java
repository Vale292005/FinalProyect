package com.example.finalproyect.UserTree;

import com.example.finalproyect.Elements.Activity;
import com.example.finalproyect.Elements.ProcessUQ;
import com.example.finalproyect.Elements.Task;
import com.example.finalproyect.QueueTask.MyQueue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Deserializador {

    // Deserializar las tareas desde un archivo
    public MyQueue<Task> deserializeTareas(String filePath) throws IOException {
        MyQueue<Task> tareas = new MyQueue<>();
        String line;
        Task tareaActual = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("Tarea:")) {
                    tareaActual = new Task(null, null, new ArrayList<>(), false, 0);  // Crear una nueva tarea

                    while ((line = reader.readLine()) != null && line.trim().startsWith("\t")) {
                        line = line.trim();

                        if (line.startsWith("Valor:")) {
                            tareaActual.setValue(line.split(":")[1].trim());
                        }
                        if (line.startsWith("Descripción:")) {
                            tareaActual.setDescription(line.split(":")[1].trim());
                        }
                        if (line.startsWith("Obligatoria:")) {
                            tareaActual.setMandatory(Boolean.parseBoolean(line.split(":")[1].trim()));
                        }
                        if (line.startsWith("Tiempo:")) {
                            tareaActual.setTime(Integer.parseInt(line.split(":")[1].trim()));
                        }

                        if (line.trim().equals("")) break;
                    }

                    if (tareaActual != null) {
                        tareas.add(tareaActual);
                    }
                }
            }
        }

        return tareas;
    }

    // Deserializar las actividades desde un archivo
    public ArrayList<Activity> deserializeActividades(String filePath) throws IOException {
        ArrayList<Activity> actividades = new ArrayList<>();
        String line;
        Activity actividadActual = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("Actividad:")) {
                    if (actividadActual != null) {
                        actividades.add(actividadActual);  // Agregar la actividad anterior
                    }
                    actividadActual = new Activity(null, null, new ArrayList<>(), false, new MyQueue<>());  // Crear una nueva actividad
                }

                if (line.startsWith("Nombre:") && actividadActual != null) {
                    actividadActual.setValue(line.split(":")[1].trim());
                }

                if (line.startsWith("Obligatoria:") && actividadActual != null) {
                    actividadActual.setMandatory(Boolean.parseBoolean(line.split(":")[1].trim()));
                }

                if (line.startsWith("Tareas:") && actividadActual != null) {
                    // Aquí ignoramos las tareas como se indicó en la solicitud
                    continue;
                }
            }

            if (actividadActual != null) {
                actividades.add(actividadActual);
            }
        }

        return actividades;
    }

    // Deserializar los procesos desde un archivo
    public ArrayList<ProcessUQ> deserializeProcesos(String filePath) throws IOException {
        ArrayList<ProcessUQ> procesos = new ArrayList<>();
        String line;
        ProcessUQ procesoActual = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("Proceso:")) {
                    if (procesoActual != null) {
                        procesos.add(procesoActual);  // Agregar el proceso anterior
                    }
                    procesoActual = new ProcessUQ();  // Crear un nuevo proceso
                }

                if (line.startsWith("Valor:") && procesoActual != null) {
                    procesoActual.setValue(line.split(":")[1].trim());
                }

                if (line.startsWith("Descripción:") && procesoActual != null) {
                    procesoActual.setDescription(line.split(":")[1].trim());
                    procesoActual.setChild(new ArrayList<>());
                }

                if (line.startsWith("ID:") && procesoActual != null) {
                    procesoActual.setId(line.split(":")[1].trim());
                }
            }

            if (procesoActual != null) {
                procesos.add(procesoActual);
            }
        }

        return procesos;
    }
}


