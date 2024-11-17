package com.example.finalproyect.UserTree;
import com.example.finalproyect.Elements.Activity;
import com.example.finalproyect.Elements.ProcessUQ;
import com.example.finalproyect.Elements.Task;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

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
    protected List<Node> children;

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
}



