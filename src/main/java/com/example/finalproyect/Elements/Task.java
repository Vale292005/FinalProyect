package com.example.finalproyect.Elements;

import com.example.finalproyect.QueueTask.MyQueue;
import com.example.finalproyect.UserTree.Node;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@JsonTypeName("Task")
public class Task extends Node {
    boolean mandatory;
    int time;

    @JsonCreator
    public Task(@JsonProperty("value") String value,
                @JsonProperty("description") String description,
                @JsonProperty("hijos") List<Node> hijos,
                @JsonProperty("mandatory") boolean mandatory,
                @JsonProperty("time") int time) {
        super(value, description, hijos);
        this.mandatory = mandatory;
        this.time = time;
    }

    // Getters y setters
    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    @Override
    public void add(Node child) {
        // Lógica para agregar hijos si es necesario
    }

    @Override
    public void printNodeInfo() {
        System.out.println("Nombre: " + value + ", Descripción: " + description + ", Obligatoria: " + mandatory + ", Tiempo: " + time);
    }

    public List<Node> getChild() {
        return children;
    }
    public String toString() {
        return "Tarea [value=" + value + ", description=" + description + ", obligatoria" +mandatory+", tiempo"+time+"]";
    }
    @Override
    public void serialize(BufferedWriter writer) throws IOException {
        writer.write("\t\tTarea:\n");
        writer.write("\t\tValor: " + value + "\n");
        writer.write("\t\tDescripción: " + description + "\n");
        writer.write("\t\tObligatoria: "+ mandatory+"\n");
        writer.write("\t\tTiempo: \n");
    }


}
