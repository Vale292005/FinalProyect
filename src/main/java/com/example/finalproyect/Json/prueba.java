package com.example.finalproyect.Json;
import com.example.finalproyect.Elements.*;
import com.example.finalproyect.UserTree.User;
import com.example.finalproyect.UserTree.UserTree;
import com.example.finalproyect.QueueTask.MyQueue;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class prueba {
    private final ObjectMapper mapper;

    public prueba() {
        mapper = new ObjectMapper();
    }

    public UserTree deserialize(File file) throws IOException {
        JsonNode rootNode = mapper.readTree(file);
        User root = deserializeUser(rootNode);
        return new UserTree(root);
    }

    private User deserializeUser(JsonNode node) {
        String value = node.get("value").asText();
        String description = node.has("description") ?
                (node.get("description").isNull() ? null : node.get("description").asText()) :
                null;

        User user = new User(value, description, new ArrayList<>(), null);

        if (node.has("children") || node.has("child")) {
            JsonNode childrenNode = node.has("children") ? node.get("children") : node.get("child");
            if (childrenNode.isArray()) {
                for (JsonNode childNode : childrenNode) {
                    ProcessUQ process = deserializeProcess(childNode);
                    user.getChild().add(process);
                }
            }
        }

        return user;
    }

    private ProcessUQ deserializeProcess(JsonNode node) {
        String value = node.get("value").asText();
        String description = node.get("description").asText();
        String idProcess = node.get("idProcess").asText();

        ProcessUQ process = new ProcessUQ(value, description, new ArrayList<>(), idProcess);

        if (node.has("children") || node.has("child")) {
            JsonNode childrenNode = node.has("children") ? node.get("children") : node.get("child");
            if (childrenNode.isArray()) {
                for (JsonNode childNode : childrenNode) {
                    Activity activity = deserializeActivity(childNode);
                    process.getChild().add(activity);
                }
            }
        }

        return process;
    }

    private Activity deserializeActivity(JsonNode node) {
        String value = node.get("value").asText();
        String description = node.get("description").asText();
        boolean mandatory = node.get("mandatory").asBoolean(); // Cambiado a boolean

        MyQueue<Task> taskQueue = new MyQueue<>(new LinkedList<>());
        Activity activity = new Activity(value, description, new ArrayList<>(), mandatory, taskQueue);

        if (node.has("myTask") && node.get("myTask").has("queue")) {
            JsonNode tasksNode = node.get("myTask").get("queue");
            if (tasksNode.isArray()) {
                for (JsonNode taskNode : tasksNode) {
                    Task task = deserializeTask(taskNode);
                    activity.getMyTask().enqueue(task);
                }
            }
        }

        return activity;
    }

    private Task deserializeTask(JsonNode node) {
        String value = node.get("value").asText();
        String description = node.get("description").asText();
        boolean mandatory = node.get("mandatory").asBoolean();
        int time = node.get("time").asInt();

        return new Task(value, description, new ArrayList<>(), mandatory, time);
    }

    public static void serialize(User root, String filePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
        serializeUser(root, rootNode, mapper);
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), rootNode);
    }

    private static void serializeUser(User user, ObjectNode node, ObjectMapper mapper) {
        node.put("value", user.getValue());
        if (user.getDescription() != null) {
            node.put("description", user.getDescription());
        }

        if (!user.getChild().isEmpty()) {
            ArrayNode childrenArray = mapper.createArrayNode();
            for (Object child : user.getChild()) {
                if (child instanceof ProcessUQ) {
                    ObjectNode processNode = mapper.createObjectNode();
                    serializeProcess((ProcessUQ) child, processNode, mapper);
                    childrenArray.add(processNode);
                }
            }
            node.set("children", childrenArray);
        }
    }

    private static void serializeProcess(ProcessUQ process, ObjectNode node, ObjectMapper mapper) {
        node.put("value", process.getValue());
        node.put("description", process.getDescription());
        node.put("idProcess", process.getIdProcess());

        if (!process.getChild().isEmpty()) {
            ArrayNode childrenArray = mapper.createArrayNode();
            for (Object child : process.getChild()) {
                if (child instanceof Activity) {
                    ObjectNode activityNode = mapper.createObjectNode();
                    serializeActivity((Activity) child, activityNode, mapper);
                    childrenArray.add(activityNode);
                }
            }
            node.set("children", childrenArray);
        }
    }

    private static void serializeActivity(Activity activity, ObjectNode node, ObjectMapper mapper) {
        node.put("value", activity.getValue());
        node.put("description", activity.getDescription());
        node.put("mandatory", activity.isMandatory()); // Cambiado a boolean

        ObjectNode myTaskNode = mapper.createObjectNode();
        ArrayNode tasksArray = mapper.createArrayNode();

        MyQueue<Task> taskQueue = activity.getMyTask();
        if (taskQueue != null) {
            Iterator<Task> iterator = taskQueue.iterator();
            while (iterator.hasNext()) {
                Task task = iterator.next();
                ObjectNode taskNode = mapper.createObjectNode();
                serializeTask(task, taskNode);
                tasksArray.add(taskNode);
            }
        }

        myTaskNode.set("queue", tasksArray);
        node.set("myTask", myTaskNode);
    }

    private static void serializeTask(Task task, ObjectNode node) {
        node.put("value", task.getValue());
        node.put("description", task.getDescription());
        node.put("mandatory", task.isMandatory());
        node.put("time", task.getTime());
    }
}
