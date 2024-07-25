package org.example;

import org.example.exceptions.JsonParseException;
import org.example.JsonUtil;
import org.example.elevator.ElevatorSystemData;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class JsonUtilTest {

    @Test
    void testReadElevatorSystemData() throws JsonParseException {
        String filePath = "src/test/resources/testScenario.json"; // Ensure this test file exists
        ElevatorSystemData data = JsonUtil.readElevatorSystemData(filePath);
        assertNotNull(data);
        assertFalse(data.getElevators().isEmpty()); // Assuming the test file has at least one elevator
        assertFalse(data.getPassengers().isEmpty()); // Assuming the test file has at least one passenger
    }
}