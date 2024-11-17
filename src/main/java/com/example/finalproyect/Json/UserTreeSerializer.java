package com.example.finalproyect.Json;

import java.io.File;
import java.io.IOException;
import com.example.finalproyect.Elements.Activity;
import com.example.finalproyect.Elements.ProcessUQ;
import com.example.finalproyect.Elements.Task;
import com.example.finalproyect.QueueTask.MyQueue;
import com.example.finalproyect.UserTree.Node;
import com.example.finalproyect.UserTree.User;
import com.example.finalproyect.UserTree.UserTree;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;

/*public class UserTreeSerializer {
    
    public static void serialize(Node root, String filePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();

        // Registra mapeos para la jerarquía de Node
        module.addAbstractTypeMapping(Node.class, ProcessUQ.class);
        module.addAbstractTypeMapping(Node.class, Activity.class);
        module.addAbstractTypeMapping(Node.class, Task.class);

        // Registrar serializadores para MyQueue<T>
        module.addSerializer(new MyQueueSerializer()); // Esto debería funcionar para cualquier tipo de MyQueue

        mapper.registerModule(module);
        mapper.writeValue(new java.io.File(filePath), root);
    }

    public MyQueue<Task> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        MyQueue<Task> queue = new MyQueue<>();

        // Verificar si el nodo es de tipo ArrayNode
        if (node.isArray()) {
            ArrayNode arrayNode = (ArrayNode) node;
            for (JsonNode taskNode : arrayNode) {
                Task task = jp.getCodec().treeToValue(taskNode, Task.class);
                queue.add(task);
            }
        } else if (node.isObject()) {
            // En caso de que `myTask` sea un objeto, maneja el caso como ObjectNode
            Task task = jp.getCodec().treeToValue(node, Task.class);
            queue.add(task);
        } else {
            throw new JsonMappingException(jp, "Expected ArrayNode or ObjectNode for myTask");
        }

        return queue;
    }

}*/
import java.io.IOException;

public class UserTreeSerializer {
    
    public static void serialize(Node root, String filePath) throws IOException {
        ObjectMapper mapper = configureMapper();
        mapper.writeValue(new java.io.File(filePath), root);
    }
    
    public static UserTree deserialize(File jsonFile) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        // Ignora propiedades desconocidas que puedan estar en el JSON
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // Realiza la deserialización
        return mapper.readValue(jsonFile, UserTree.class);
    }
    
    private static ObjectMapper configureMapper() {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        
        // Registrar los tipos concretos
        mapper.registerSubtypes(
            new NamedType(User.class, "UserTree.User"),
            new NamedType(ProcessUQ.class, "Process"),
            new NamedType(Activity.class, "Activity"),
            new NamedType(Task.class, "Task")
        );
        
        // Registrar serializadores/deserializadores personalizados
        module.addSerializer(new MyQueueSerializer());
        module.addDeserializer(MyQueue.class, new MyQueueDeserializer<>(Task.class));
        
        mapper.registerModule(module);
        return mapper;
    }
}








