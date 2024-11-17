package com.example.finalproyect.Features;


import com.example.finalproyect.Elements.Task;
import com.example.finalproyect.QueueTask.MyQueue;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.util.LinkedList;
@SuppressWarnings("unchecked")
// Asegúrate de que extienda JsonDeserializer<MyQueue>
public class MyQueueDeserializer extends JsonDeserializer<MyQueue> {

    @Override
    public MyQueue deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        // Leer el JSON y crear la cola
        ObjectNode node = p.getCodec().readTree(p);
        LinkedList<Object> queue = new LinkedList<>();

        // Leer la propiedad 'queue' (o lo que sea que quieras deserializar)
        if (node.has("queue")) {
            // Aquí puedes deserializar la lista de objetos, como un Task o cualquier otro objeto que esté en la cola
             //queue = p.getCodec().treeToValue(node.get("queue"), new TypeReference<LinkedList<Task>>() {});
        	ObjectMapper mapper = (ObjectMapper) p.getCodec();
            //queue = mapper.readValue(node.get("queue").toString(), new TypeReference<LinkedList<Task>>() {});
        	queue = (LinkedList<Object>) (LinkedList<?>) mapper.readValue(node.get("queue").toString(), new TypeReference<LinkedList<Task>>() {});
        
        }

        // Crear la instancia de MyQueue con los datos deserializados
        MyQueue<Object> myQueue = new MyQueue<>(queue);

        return myQueue;
    }
}
