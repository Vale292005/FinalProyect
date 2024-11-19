package com.example.finalproyect.UserTree;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class NodeSerializer {
    public static void serializeNode(Node node, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath,true))) {
            node.serialize(writer);
            writer.write("\n"); // Separador entre entradas
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
