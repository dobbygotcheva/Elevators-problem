package org.example;

import org.example.elevator.ElevatorSystem;
import org.example.elevator.ElevatorSystemData;
import org.example.exceptions.InvalidFloorException;
import org.example.exceptions.InvalidWeightException;
import org.example.exceptions.JsonParseException;
import org.example.exceptions.OverloadedElevatorException;

public class Main {
    public static void main(String[] args) throws InvalidWeightException, InvalidFloorException, JsonParseException, OverloadedElevatorException {
        String[] scenarioFiles = {
                "src/main/resources/initialState.json",
                "src/main/resources/highTrafficScenario.json",
                "src/main/resources/maintenanceMode.json",
                "src/main/resources/peakHoursLoad.json"
        };

        for (String filePath : scenarioFiles) {
            System.out.println("Processing scenario: " + filePath);

            // Read the elevator system data from the JSON file
            ElevatorSystemData data = JsonUtil.readElevatorSystemData(filePath);

            // Create an instance of the ElevatorSystem class
            ElevatorSystem elevatorSystem = new ElevatorSystem(data.getElevators(), data.getPassengers());

            // Assign passengers to elevators
            elevatorSystem.assignPassengersToElevators();

            // Simulate elevator movements
            elevatorSystem.simulateElevatorMovements();

            // Print the final state of the elevators and passengers
            elevatorSystem.printFinalState();

            System.out.println("Finished processing scenario: " + filePath + "\n");
        }
    }
}