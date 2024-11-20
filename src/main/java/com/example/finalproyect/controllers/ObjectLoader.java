package com.example.finalproyect.controllers;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class ObjectLoader {

    /**
     * Lee un archivo que contiene una dirección (ruta) de un archivo serializado
     * y devuelve el objeto deserializado desde esa dirección.
     *
     * @param pathToFile la ruta del archivo que contiene las direcciones
     * @return Lista de objetos deserializados
     * @throws IOException si ocurre un error al leer el archivo
     * @throws ClassNotFoundException si la clase del objeto no puede ser encontrada
     */
    public static List<Object> loadObjectsFromFile(String pathToFile) throws IOException, ClassNotFoundException {
        List<Object> objects = new ArrayList<>();

        // Leer todas las líneas del archivo que contiene las direcciones
        List<String> lines = Files.readAllLines(Paths.get(pathToFile));

        for (String filePath : lines) {
            // Verifica si la línea no está vacía
            if (!filePath.trim().isEmpty()) {
                // Deserializa el objeto desde la ruta especificada
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath.trim()))) {
                    Object obj = ois.readObject();
                    objects.add(obj); // Agregar el objeto a la lista
                } catch (IOException | ClassNotFoundException e) {
                    System.err.println("Error al cargar el objeto desde: " + filePath + ". " + e.getMessage());
                    throw e; // Relanzar la excepción para manejo externo
                }
            }
        }

        return objects;
    }
}

