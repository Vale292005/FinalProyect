package com.example.finalproyect.Elements;
import java.util.List;
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

}

