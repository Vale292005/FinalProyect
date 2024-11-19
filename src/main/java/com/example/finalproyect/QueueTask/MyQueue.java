package com.example.finalproyect.QueueTask;
import com.example.finalproyect.Elements.Task;
import com.example.finalproyect.Json.MyQueueDeserializer;
import com.example.finalproyect.MyStack.MyStack;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
//@JsonTypeName("MyQueue<T>")
@JsonDeserialize(using = MyQueueDeserializer.class)
public class MyQueue<T> implements Iterable<T> {
    private LinkedList<T> queue;
	private boolean empty;

	//public MyQueue() {
    //}
    @JsonCreator
    public MyQueue(@JsonProperty("queue") LinkedList<T> queue) {
        this.queue = queue;
        this.empty = queue.isEmpty();
    }
	/*public MyQueue(LinkedList<T> queue) {
		super();
		this.queue = queue;
	}*/
    public MyQueue() {
        this.queue = new LinkedList<>();
    }


    public void enqueue(T element) {
        queue.addLast(element);
    }

    public T dequeue() {
        if (queue.isEmpty()) {
            throw new NoSuchElementException("La cola está vacía");
        }
        return queue.removeFirst();
    }

    public T peek() {
        if (queue.isEmpty()) {
            throw new NoSuchElementException("La cola está vacía");
        }
        return queue.getFirst();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public int size() {
        return queue.size();
    }

    @Override
    public Iterator<T> iterator() {
        return new QueueIterator();
    }

    private class QueueIterator implements Iterator<T> {
        private Iterator<T> iterator = queue.iterator();

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public T next() {
            return iterator.next();
        }
    }
    public T buscarEnQueue(T valorBuscado) {
        for (T elemento : queue) {
            if (elemento.equals(valorBuscado)) {
                return elemento;
            }
        }
        return null; 
    }
    public void insertion(T task, T after) {
        MyStack<T> aux = new MyStack<>();
        boolean inserted = false;

        while (!queue.isEmpty()) {
            T item = queue.poll(); 
            aux.push(item); 

            if (item.equals(after) && !inserted) {
                queue.add(task); 
                inserted = true; 
            }
        }
        
        while (!aux.isEmpty()) {
            queue.add(aux.pop());
        }

        if (!inserted) {
            queue.add(task);
        }
    }
    public void printQueue() {
        for (T item : queue) {
        	Task i=(Task) item;
            i.printNodeInfo();  
        }
    }
    public void add(T item) {
        queue.add(item); 
    }
    public MyQueue<Task> deserializeTareas(BufferedReader reader) throws IOException {
        MyQueue<Task> tareas = new MyQueue<>();
        String line;
        Task tareaActual = null;

        while ((line = reader.readLine()) != null) {
            line = line.trim();

            // Cuando encontramos una línea que empieza con "Tarea:"
            if (line.startsWith("Tarea:")) {
                tareaActual = new Task(null,null,new ArrayList<>(),false,0);  // Crear una nueva tarea

                // Leemos las siguientes líneas correspondientes a los atributos de la tarea
                while ((line = reader.readLine()) != null && line.trim().startsWith("\t")) {
                    line = line.trim();

                    // Leer el valor de "Valor:"
                    if (line.startsWith("Valor:")) {
                        tareaActual.setValue(line.split(":")[1].trim());
                    }

                    // Leer el valor de "Descripción:"
                    if (line.startsWith("Descripción:")) {
                        tareaActual.setDescription(line.split(":")[1].trim());
                    }

                    // Leer el valor de "Obligatoria:"
                    if (line.startsWith("Obligatoria:")) {
                        tareaActual.setMandatory(Boolean.parseBoolean(line.split(":")[1].trim()));
                    }

                    // Leer el valor de "Tiempo:"
                    if (line.startsWith("Tiempo:")) {
                        tareaActual.setTime(Integer.parseInt(line.split(":")[1].trim()));
                    }

                    if (line.trim().equals("")) break;
                }

                // Agregar la tarea a la cola
                if (tareaActual != null) {
                    tareas.add(tareaActual);
                }
            }
        }

        return tareas;
    }

}

