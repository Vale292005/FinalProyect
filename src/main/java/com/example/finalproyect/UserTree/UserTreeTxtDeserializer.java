package com.example.finalproyect.UserTree;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class UserTreeTxtDeserializer {

    public static UserTree deserializeFromTxt(String filePath) throws IOException {
        // Validar que la ruta del archivo no sea nula
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("La ruta del archivo no puede ser nula o vacía");
        }

        try {
            // Leer contenido del archivo
            String content = new String(Files.readAllBytes(Paths.get(filePath)));

            // Validar que el contenido no esté vacío
            if (content.trim().isEmpty()) {
                throw new IllegalArgumentException("El archivo está vacío");
            }

            // Configurar ObjectMapper con configuraciones tolerantes
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            // Deserializar con manejo de valores por defecto
            UserTree userTree = mapper.readValue(content, UserTree.class);

            // Validaciones adicionales post-deserialización
            validateUserTree(userTree);

            return userTree;
        } catch (IOException e) {
            System.err.println("Error deserializando UserTree: " + e.getMessage());
            throw e;
        }
    }

    private static void validateUserTree(UserTree userTree) {
        // Validar que el árbol no sea nulo
        if (userTree == null) {
            throw new IllegalStateException("UserTree no puede ser nulo");
        }

        // Validar que el nodo raíz no sea nulo
        Node root = userTree.getRoot();
        if (root == null) {
            // Crear un nodo raíz por defecto si no existe
            root = new User("root", null, null, null);
            userTree.setRoot(root);
        }

        // Inicializar lista de hijos si es nula
        if (root.getChild() == null) {
            root.setChild(new ArrayList<>());
        }
    }

    public static UserTree deserializeFromTxt(File file) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("Archivo no puede ser nulo");
        }
        return deserializeFromTxt(file.getPath());
    }
}