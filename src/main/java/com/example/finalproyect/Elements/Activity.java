package com.example.finalproyect.Elements;

import com.example.finalproyect.QueueTask.MyQueue;
import com.example.finalproyect.UserTree.Node;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

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

}
