package org.example.elevator;

import org.example.exceptions.InvalidFloorException;
import org.example.exceptions.InvalidWeightException;
import org.example.exceptions.OverloadedElevatorException;
import org.example.passenger.Passenger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ElevatorSystemTest {

    private List<Elevator> elevators;
    private List<Passenger> passengers;
    private ElevatorSystem elevatorSystem;

    @BeforeEach
    void setUp() {
        elevators = new ArrayList<>();
        passengers = new ArrayList<>();
        // Adjust the elevator's capacity and max weight to ensure it can accommodate the test passengers
        elevatorSystem = new ElevatorSystem(elevators, passengers);
    }


    @Test
    void testElevatorIdleState() throws InvalidWeightException, InvalidFloorException, OverloadedElevatorException {
        // Setup
        elevators.add(new Elevator(1, 1, 1000,10)); // No passengers added to trigger IDLE state

        elevatorSystem = new ElevatorSystem(elevators, passengers);

        // Action & Assertions
        for (Elevator elevator : elevators) {
            assertEquals(Direction.IDLE, elevator.getDirection(), "Elevator should be in IDLE direction when there are no passengers.");
        }
    }

    @Test
    void testSimulateElevatorMovement() throws OverloadedElevatorException {
        // Setup
        Elevator elevator = new Elevator(1, 1, 1000, 10); // Elevator ID 1, starting floor 1, max weight 1000
        elevators.add(elevator);
        Passenger passenger1 = new Passenger("Alice", 1, 5, 80); // Passenger Alice, current floor 1, target floor 5, weight 80
        Passenger passenger2 = new Passenger("Bob", 1, 3, 70); // Passenger Bob, current floor 1, target floor 3, weight 70
        passengers.add(passenger1);
        passengers.add(passenger2);

        // Action
        elevatorSystem.assignPassengersToElevators();
        elevatorSystem.simulateElevatorMovements();

        // Assertions
        assertEquals(5, elevator.getCurrentFloor(), "Elevator should be on the top floor (5) after simulation.");
        assertTrue(elevator.getPassengers().isEmpty(), "Elevator should have no passengers after simulation.");
    }

}
