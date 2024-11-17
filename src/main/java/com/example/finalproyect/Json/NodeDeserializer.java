package com.example.finalproyect.Json;

import java.io.IOException;

import com.example.finalproyect.Elements.Activity;
import com.example.finalproyect.Elements.ProcessUQ;
import com.example.finalproyect.Elements.Task;
import com.example.finalproyect.UserTree.Node;
import com.example.finalproyect.UserTree.User;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

//Nuevo deserializador personalizado para Node
class NodeDeserializer extends JsonDeserializer<Node> {
 @Override
 public Node deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
     ObjectMapper mapper = (ObjectMapper) jp.getCodec();
     JsonNode node = jp.readValueAsTree();
     
     // Determinar el tipo basado en @class o type
     String type = null;
     if (node.has("@class")) {
         type = node.get("@class").asText();
     } else if (node.has("type")) {
         type = node.get("type").asText();
     }
     
     // Mapear el tipo al objeto correcto
     Class<?> targetClass;
     switch (type) {
         case "UserTree.User":
             targetClass = User.class;
             break;
         case "Process":
             targetClass = ProcessUQ.class;
             break;
         case "Activity":
             targetClass = Activity.class;
             break;
         case "Task":
             targetClass = Task.class;
             break;
         default:
             throw new IOException("Unknown type: " + type);
     }
     
     return (Node) mapper.treeToValue(node, targetClass);
 }}