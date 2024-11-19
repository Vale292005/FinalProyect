package com.example.finalproyect.UserTree;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

import java.util.List;


@JsonTypeName("User")
public class User extends Node implements Comparable<User> {
    public String user;  // Atributo para almacenar el nombre del usuario


    // Constructor que incluye el atributo 'user'
    @JsonCreator
    public User(String value, String description, List<Node> children, String user) {
        super(value, description, children);  // Llamada al constructor de la clase base Node
        this.user = user;  // Inicializa el nombre del usuario
    }

    // Constructor por defecto
    public User() {
        super("", "", new ArrayList<>());  // Llamada al constructor de la clase base Node
    }

    public String getUser() {
        return user;  // Método para obtener el nombre del usuario
    }

    public void setUser(String user) {
        this.user = user;  // Método para establecer el nombre del usuario
    }

    @Override
    public void add(Node child) {
        if (getChild() == null) {
            setChild(new ArrayList<>());  // Asegurarse de que haya una lista de hijos
        }
        getChild().add(child);  // Añadir el hijo a la lista de hijos
    }
    public void addChild(Node parent, Node child) {
        if (parent.getChild() == null) {
            parent.setChild(new ArrayList<>());
        }
        parent.getChild().add(child);  // Añadir un hijo al nodo padre
    }

    @Override
    public void printNodeInfo() {
        System.out.println("User: " + user);  // Imprimir el nombre del usuario
    }

    @Override
    public void serialize(BufferedWriter writer) throws IOException {
        writer.write("User:\n");
        writer.write("Valor: " + user + "\n");

        if (getChild() != null) {
            writer.write("Actividades:\n");
            for (Node child : getChild()) {
                child.serialize(writer);
            }
        }

    }


    @Override
    public String toString() {
        return "User: " + user;  // Usar 'user' en lugar de 'value'
    }


    @Override
    public int compareTo(User other) {
        if (this.user == null && other.user == null) return 0;
        if (this.user == null) return -1;  // Si 'this' es null, va al final
        if (other.user == null) return 1;  // Si 'other' es null, va al final
        return this.user.compareTo(other.user);  // Comparar los nombres de usuario
    }


    @Override
    public int hashCode() {
        return user != null ? user.hashCode() : 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User other = (User) obj;
        return user != null && user.equals(other.user);
    }

}

