package com.example.finalproyect.UserTree;
import com.example.finalproyect.Elements.Activity;
import com.example.finalproyect.Elements.ProcessUQ;
import com.example.finalproyect.Elements.Task;
import com.example.finalproyect.Features.TaskSearchTask;
import com.example.finalproyect.QueueTask.MyQueue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class UserTree {
    private Node root;
    @JsonCreator
	public UserTree(@JsonProperty("root") Node root) {
		super();
		this.root = root;
	}
    public User getRoot() {
		return (User) root;
	}

	public void setRoot(Node root) {
		this.root = root;
	}
    public Node buscar(Node node, String value) {
        if (node == null) {
            return null;
        }
        if (node.getValue().equals(value)) {
            return node;
        }
        for (Node hijo : node.getChild()) {
            Node resultado = buscar(hijo, value);
            if (resultado != null) {
                return resultado;
            }
        }
        return null;
    }
    public void addEnPosicion(Node parent, Node child, int posicion) {
        List<Node> hijos = parent.getChild();
        if (posicion >= 0 && posicion <= hijos.size()) {
            hijos.add(posicion, child);
        } else {
            System.out.println("Posición inválida, el nodo no se ha añadido.");
        }
    }

    public void printTree() {
    	printTree(root, 0);
    }

    public static void printTree(Node node, int level) {
        // Imprimir la información del nodo actual
        System.out.println("  ".repeat(level) + node.toString());

        // Si el nodo tiene hijos, recorrerlos e imprimirlos
        if (node instanceof User) {
            User user = (User) node;
            if (user.getChild() != null) {
                for (Node child : user.getChild()) {
                    printTree(child, level + 1); // Llamada recursiva para imprimir los hijos
                }
            }
        } else if (node instanceof ProcessUQ) {
            ProcessUQ process = (ProcessUQ) node;
            if (process.getChild() != null) {
                for (Node child : process.getChild()) {
                    printTree(child, level + 1); // Llamada recursiva para imprimir los hijos
                }
            }
        } else if (node instanceof Activity) {
            Activity activity = (Activity) node;
            if (activity.getChild() != null) {
                for (Node child : activity.getChild()) {
                    printTree(child, level + 1); // Llamada recursiva para imprimir los hijos
                }
            }
        }
    }
    public void MoveChild(Node parent, List<Node> newOrder) {
        List<Node> hijos = parent.getChild();
        if (newOrder.size() != hijos.size()) {
            System.out.println("El tamaño del nuevo orden no coincide con la cantidad de hijos.");
            return;
        }

        for (int i = 0; i < newOrder.size(); i++) {
            Node hijo = newOrder.get(i);
            if (!hijos.contains(hijo)) {
                System.out.println("Uno o más nodos no están presentes en los hijos.");
                return;
            }
        }

        hijos.clear();
        hijos.addAll(newOrder);
        System.out.println("Hijos reordenados exitosamente.");
    }
	public void addChild(ProcessUQ process1) {
		root.add(process1);
	}
	public void addChild(User user) {
		root.add(user);
	}

	public List<Node> searchNodesByName(String nodeName) {
	    ConcurrentLinkedQueue<Node> foundNodes = new ConcurrentLinkedQueue<>();
	    ExecutorService executor = Executors.newFixedThreadPool(4); // Crear un pool de hilos

	    // Crear y enviar tareas de búsqueda para cada hijo de la raíz
	    for (Node child : root.getChild()) {
	        executor.submit(new TaskSearchTask(child, nodeName, foundNodes));
	    }

	    executor.shutdown();
	    try {
	        executor.awaitTermination(1, TimeUnit.MINUTES); // Espera que los hilos terminen
	    } catch (InterruptedException e) {
	        e.printStackTrace();
	    }

	    return List.copyOf(foundNodes); // Retorna los nodos encontrados
	}
	public static UserTree importFromCsv(String filePath) throws IOException {
        UserTree tree = new UserTree(new User("root",null,null,null));
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine(); // omitir encabezado
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String processName = values[0];
                String activityName = values[1];
                String taskName = values[2];

                // Crear proceso, actividad y tarea y añadirlos al árbol
                ProcessUQ process = new ProcessUQ(processName, "", new ArrayList<>(), "ID");
                Activity activity = new Activity(activityName, "", new ArrayList<>(), true, new MyQueue<>(new LinkedList<>()));
                Task task = new Task(taskName, "", new ArrayList<>(), true, 30);

                activity.getMyTask().enqueue(task);
                process.getChild().add(activity);
                tree.addChild(process);
            }
        }
        return tree;
    }

    public void exportToJson(String filePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File(filePath), this);
    }



}

