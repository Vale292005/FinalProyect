package com.example.finalproyect.Features;

import java.util.concurrent.ConcurrentLinkedQueue;
import com.example.finalproyect.Elements.Activity;
import com.example.finalproyect.Elements.ProcessUQ;
import com.example.finalproyect.Elements.Task;
import com.example.finalproyect.UserTree.Node;

public class TaskSearchTask implements Runnable {
    private Node node;
    private String nodeName;
    private ConcurrentLinkedQueue<Node> foundNodes;

    public TaskSearchTask(Node node, String nodeName, ConcurrentLinkedQueue<Node> foundNodes) {
        this.node = node;
        this.nodeName = nodeName;
        this.foundNodes = foundNodes;
    }

    @Override
    public void run() {
        searchNodes(node);
    }

    private void searchNodes(Node node) {
        // Verifica si el nodo actual es de tipo ProcessUQ, Activity o Task y coincide con el nombre
        if ((node instanceof ProcessUQ || node instanceof Activity || node instanceof Task)
                && node.getValue().equals(nodeName)) {
            foundNodes.add(node); // Añadir el nodo al resultado si coincide
        }

        // Si el nodo es de tipo Activity, también revisamos su cola de tareas
        if (node instanceof Activity) {
            Activity activityNode = (Activity) node;
            if (activityNode.getMyTask() != null) {
                for (Task task : activityNode.getMyTask()) {
                    if (task.getValue().equals(nodeName)) {
                        foundNodes.add(task); // Añadir la tarea coincidente
                    }
                }
            }
        }

        // Búsqueda recursiva en los hijos
        for (Node child : node.getChild()) {
            searchNodes(child);
        }
    }
}

