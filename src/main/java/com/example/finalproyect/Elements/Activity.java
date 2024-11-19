package com.example.finalproyect.Elements;

import com.example.finalproyect.QueueTask.MyQueue;
import com.example.finalproyect.UserTree.Node;
import com.example.finalproyect.controllers.SharedFileName;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@JsonTypeName("Activity")
public class Activity extends Node {
    private boolean mandatory;
    private MyQueue<Task> myTask;

    @JsonCreator
    public Activity(
        @JsonProperty("value") String value,
        @JsonProperty("description") String description,
        @JsonProperty("children") List<Node> children,
        @JsonProperty("mandatory") boolean mandatory,
        @JsonProperty("myTask") MyQueue<Task> myTask
    ) {
        super(value, description, children);
        this.mandatory = mandatory;
        this.myTask = myTask;
    }

	public boolean isMandatory() {
		return mandatory;
	}

	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}

	public MyQueue<Task> getMyTask() {
		return myTask;
	}

	public void setMyTask(MyQueue<Task> myTask) {
		this.myTask = myTask;
	}
	@Override
	public void add(Node child) {
		
	}

	@Override
	public void printNodeInfo() {
		System.out.println("Nombre: " + value + ", Descripción: " + description + ", Obligatoria: " + mandatory);
		
	}

    public static String TimeActivity(MyQueue<Task> tasks){
    	int totalTime=0;
    	for(Task task:tasks) {
    		if(task.isMandatory()) {
    			totalTime+=task.getTime();
    		}
    	}
    	return TimeConverter(totalTime);
    }
	public static String TimeConverter(int segundosTotales) {
		int minutos = segundosTotales / 60;
        int segundos = segundosTotales % 60;
        return minutos + ":" + (segundos < 10 ? "0" + segundos : segundos);
    }
	public List<Node> getChild() {
		return children;
	}

	public String toString() {
		return "Actividad [value=" + value + ", description=" + description + ", obligatoria" +mandatory+"]";
	}

	public static Activity loadFromTxt(String filePath) throws IOException {
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String value = null;
			String description = null;
			boolean mandatory = false;
			MyQueue<Task> myTask = new MyQueue<>();
			List<Node> children = new ArrayList<>();

			String line;
			while ((line = reader.readLine()) != null) {
				line = line.trim(); // Eliminar espacios en blanco al inicio y final
				if (line.isEmpty()) continue; // Ignorar líneas vacías

				System.out.println("Leyendo línea: " + line);  // Depuración

				// Extraer clave y valor
				String[] parts = line.split(":", 2);
				if (parts.length < 2) continue; // Si no tiene formato clave:valor, omitir

				String key = parts[0].trim();
				String valuePart = parts[1].trim();

				switch (key) {
					case "Valor":
						value = valuePart;
						break;
					case "Descripción":
						description = valuePart;
						break;
					case "Obligatoria":
						mandatory = Boolean.parseBoolean(valuePart);
						break;
					case "Tareas":
						// Asegúrate de que si Tareas está vacío, se maneje correctamente
						if (!valuePart.isEmpty()) {
							String[] tasks = valuePart.split(","); // Separar tareas por comas
							for (String taskName : tasks) {
								myTask.enqueue(new Task(taskName.trim(), null, null, false, 0)); // Crear tareas vacías con solo nombre
							}
						} else {
							System.out.println("No se han especificado tareas."); // Depuración
						}
						break;
					default:
						// Si aparece una clave no reconocida, ignorar
						break;
				}
			}

			// Validar que se hayan cargado los campos obligatorios
			if (value == null || description == null) {
				System.out.println("Error: Falta valor o descripción");  // Depuración
				throw new IllegalArgumentException("El archivo no contiene todos los campos obligatorios para la actividad.");
			}

			// Imprimir los valores leídos para depurar
			System.out.println("Creando actividad con valor: " + value + ", descripción: " + description);
			System.out.println("Tareas cargadas: " + myTask.size());  // Depuración

			return new Activity(value, description, children, mandatory, myTask);
		}
	}




	public static void exportToTxt(Activity activity) {
		String fileName = SharedFileName.fileName;  // Obtiene el nombre del archivo compartido

		// Crear contenido a partir de los atributos de la actividad
		String content = "Actividad: \n";
		content += "Valor: " + activity.getValue() + "\n";
		content += "Descripción: " + activity.getDescription() + "\n";
		content += "Obligatoria: " + activity.isMandatory() + "\n";
		content += "Tareas: \n";




		// Escribir en el archivo
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName,true))) {
			writer.write(content);
			System.out.println("Actividad exportada a " + fileName);
		} catch (IOException e) {
			System.err.println("Error al escribir en el archivo: " + e.getMessage());
		}
	}
	@Override
	public void serialize(BufferedWriter writer) throws IOException {
		writer.write("\tActividad:\n");
		writer.write("\tNombre: " + value + "\n");
		writer.write("\tDescripcion: " + description + "\n");
		writer.write("\tObligatoria: " + mandatory + "\n");

		if (getChild() != null) {
			writer.write("\tTareas:\n");
			for (Node child : getChild()) {
				child.serialize(writer);
			}
		}
		if (!(getMyTask() == null || !getMyTask().iterator().hasNext())) {
			for (Task task : getMyTask()) {
				writer.write("\tTarea: \n");
				writer.write("\t\tValor: " + task.getValue() + "\n");
				writer.write("\t\tDescripción: " + task.getDescription() + "\n");
				writer.write("\t\tObligatoria: " + task.isMandatory() + "\n");
				writer.write("\t\tTiempo: " + task.getTime() + "\n");
			}
		}
	}




}
