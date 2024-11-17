package com.example.finalproyect.Json;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JsonToCsvExporter {

    public static void exportJsonToCsv(JSONObject jsonObject, String outputFilePath) {
        try (FileWriter writer = new FileWriter(outputFilePath)) {
            // Escribir encabezados en el archivo CSV
            writer.append("Usuario,Proceso,Actividad,Tarea,Descripción,Tarea Obligatoria,Tiempo\n");

            // Llamar a la función recursiva para procesar el JSON
            processNode(jsonObject, writer, "", "", "", 0);

            System.out.println("Exportación completada.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processNode(JSONObject jsonObject, FileWriter writer, String usuario, String proceso, String actividad, int level) throws IOException {
        // Obtenemos el valor y descripción del nodo
        String value = jsonObject.optString("value", "");
        String description = jsonObject.optString("description", "");

        if (level == 0) {
            // Si es el nodo raíz (Usuario), agregamos el valor y la descripción
            usuario = value;
        } else if (level == 1) {
            // Si es un nodo de Proceso, agregamos el valor y la descripción
            proceso = value;
        } else if (level == 2) {
            // Si es un nodo de Actividad, agregamos el valor y la descripción
            actividad = value;
        }

        // Si es un nodo de tarea (nivel 3), exportamos la tarea
        if (jsonObject.has("myTask")) {
            JSONArray tasks = jsonObject.getJSONObject("myTask").getJSONArray("queue");
            for (int i = 0; i < tasks.length(); i++) {
                JSONObject task = tasks.getJSONObject(i);
                String taskValue = task.optString("value", "");
                String taskDescription = task.optString("description", "");
                boolean mandatory = task.optBoolean("mandatory", false);
                int time = task.optInt("time", 0);

                // Escribir la línea en el archivo CSV
                writer.append(String.join(",", usuario, proceso, actividad, taskValue, taskDescription, String.valueOf(mandatory), String.valueOf(time)) + "\n");
            }
        }

        // Procesar los hijos del nodo
        if (jsonObject.has("children")) {
            JSONArray children = jsonObject.getJSONArray("children");
            for (int i = 0; i < children.length(); i++) {
                processNode(children.getJSONObject(i), writer, usuario, proceso, actividad, level + 1);
            }
        }
    }

    // Método para leer el archivo JSON desde una ruta y convertirlo en un JSONObject
    public static JSONObject readJsonFromFile(String filePath) throws IOException {
        // Leer el contenido del archivo JSON como una cadena
        String content = new String(Files.readAllBytes(Paths.get(filePath)));
        // Convertir la cadena JSON a un JSONObject
        return new JSONObject(content);
    }

    public static void main(String[] args) {
        try {
            // Ruta del archivo JSON (reemplaza con la ruta de tu archivo JSON)
            String jsonFilePath = "arbol.json"; // Cambia la ruta según sea necesario
            JSONObject jsonObject = readJsonFromFile(jsonFilePath);

            // Ruta donde guardar el archivo CSV (reemplaza con la ruta donde quieras guardarlo)
            String csvFilePath = "output.csv"; // Cambia la ruta de salida
            exportJsonToCsv(jsonObject, csvFilePath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

