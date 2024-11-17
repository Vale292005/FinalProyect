package com.example.finalproyect.Json;
import org.json.JSONObject;
import org.json.JSONArray;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JsonImporter {

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
            String jsonFilePath = "ruta/del/archivo.json"; // Cambia la ruta según sea necesario
            JSONObject jsonObject = readJsonFromFile(jsonFilePath);

            // Ahora puedes manipular el jsonObject como un objeto JSONObject
            System.out.println("Contenido del JSON: " + jsonObject.toString(2)); // Muestra el JSON formateado

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

