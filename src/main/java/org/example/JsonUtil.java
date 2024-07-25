package org.example;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.elevator.ElevatorSystemData;
import org.example.exceptions.JsonParseException;

import java.io.File;
import java.io.IOException;

public class JsonUtil {

    public static ElevatorSystemData readElevatorSystemData(String filePath) throws JsonParseException {
        ObjectMapper mapper = new ObjectMapper();
        // Configure ObjectMapper to ignore unknown properties
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            return mapper.readValue(new File(filePath), ElevatorSystemData.class);
        } catch (IOException e) {
            throw new JsonParseException("Failed to parse JSON file: " + filePath, e);
        }
    }
}