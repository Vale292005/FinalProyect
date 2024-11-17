package com.example.finalproyect.Json;


import com.example.finalproyect.QueueTask.MyQueue;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;

import java.io.IOException;

public class MyQueueDeserializer<T> extends StdDeserializer<MyQueue<T>> {
    private final Class<T> type;

    public MyQueueDeserializer(Class<T> type) {
        super(MyQueue.class);
        this.type = type;
    }

    @Override
    public MyQueue<T> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        MyQueue<T> myQueue = new MyQueue<>();
        JsonNode node = jp.getCodec().readTree(jp);

        // Verifica que sea un array antes de procesarlo
        if (node.isArray()) {
            ArrayNode arrayNode = (ArrayNode) node;
            for (JsonNode itemNode : arrayNode) {
                T item = jp.getCodec().treeToValue(itemNode, type);
                myQueue.enqueue(item);
            }
        } else {
            throw new JsonMappingException(jp, "Expected an array node for MyQueue");
        }
        
        return myQueue;
    }
}







