package com.example.finalproyect.Json;

import com.example.finalproyect.UserTree.UserTree;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class JsonUtils {
    public static void serializeUserTree(UserTree userTree, String filePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File(filePath), userTree);
    }


    public static UserTree deserializeUserTree(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonString, UserTree.class);
    }
}


