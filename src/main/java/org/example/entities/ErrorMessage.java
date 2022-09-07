package org.example.entities;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class ErrorMessage {
    public ErrorMessage(String title, String message, String fileName) throws IOException {
        Map<String, String> error = new LinkedHashMap<>();
        error.put("type", title);
        error.put("message", message);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(new FileWriter((fileName != null) ? fileName : "output.json"), error);
    }
}