package com.example.finalproyect.Json;

import com.example.finalproyect.QueueTask.MyQueue;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
public class MyQueueSerializer extends StdSerializer<MyQueue<?>> {

    // Constructor que pasa el tipo que este serializador maneja
    public MyQueueSerializer() {
        super(MyQueue.class, false);
    }

    @Override
    public void serialize(MyQueue<?> queue, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        // Obtenemos el ObjectMapper desde el generador JSON
        ObjectMapper mapper = (ObjectMapper) gen.getCodec();

        // Creamos un ArrayNode que contendrá los elementos de la cola
        ArrayNode arrayNode = mapper.createArrayNode();

        // Iteramos sobre los elementos de la cola y los agregamos al ArrayNode
        for (Object element : queue) {
            // Usamos valueToTree para convertir el objeto en un JsonNode
            arrayNode.add(mapper.valueToTree(element));  // Agregamos el JsonNode al ArrayNode
        }

        // Escribimos el ArrayNode al generador JSON
        gen.writeObject(arrayNode);
    }
}








