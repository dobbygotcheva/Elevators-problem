package org.example.elevator;

import org.example.exceptions.InvalidFloorException;
import org.example.exceptions.InvalidWeightException;
import org.example.exceptions.OverloadedElevatorException;
import org.example.elevator.Elevator;
import org.example.passenger.Passenger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ElevatorTest {

    private Elevator elevator;
    private ElevatorSystem elevatorSystem;
    private List<Elevator> elevators;
    private List<Passenger> passengers;

    @BeforeEach
    void setUp() {
        elevator = new Elevator(1, 5, 500, 10); // ID, currentFloor, maxWeight
        elevator.setCapacity(5); // Set a capacity for the elevator
        elevators = new ArrayList<>();
        passengers = new ArrayList<>();
        elevatorSystem = new ElevatorSystem(elevators, passengers);


    }


    @Test
    void testUnloadSpecificPassenger() throws OverloadedElevatorException {
        Passenger passenger1 = new Passenger("John Doe", 1, 2, 70);
        Passenger passenger2 = new Passenger("Jane Doe", 1, 3, 70);
        elevator.loadPassenger(passenger1);
        elevator.loadPassenger(passenger2);
        elevator.unloadPassenger(passenger1);// Move to floor 2, should unload passenger1
        assertFalse(elevator.getPassengers().contains(passenger1));
        assertTrue(elevator.getPassengers().contains(passenger2));
    }

    @Test
    void testIsOverloaded() {
        Passenger passenger = new Passenger("John Doe", 0, 2, 600); // Weight exceeds maxWeight
        assertFalse(elevator.canLoadPassenger(passenger)); // Elevator should not be able to load passenger
    }

    @Test
    void testCanLoadPassenger() {
        Passenger passenger = new Passenger("John Doe", 0, 2, 300);
        assertTrue(elevator.canLoadPassenger(passenger)); // Elevator can load passenger without exceeding maxWeight
    }

    @Test
    void testMoveWithMultiplePassengers() throws OverloadedElevatorException {
        Passenger passenger1 = new Passenger("John Doe", 1, 10, 70);
        Passenger passenger2 = new Passenger("Jane Doe", 1, 20, 70);
        elevator.loadPassenger(passenger1);
        elevator.loadPassenger(passenger2);
        elevator.move();
        elevator.unloadPassenger(passenger1);// Move to floor 1, should unload passenger1
        assertTrue(elevator.getPassengers().contains(passenger2) && !elevator.getPassengers().contains(passenger1));
        elevator.move(); // Move to floor 20, should unload passenger2
        elevator.unloadPassenger(passenger2);
        assertTrue(elevator.getPassengers().isEmpty());
    }

    @Test
    void testAdjustDirectionBasedOnRemainingPassengers() throws OverloadedElevatorException {
        Passenger passenger1 = new Passenger("John Doe", 15, 100, 70);
        Passenger passenger2 = new Passenger("Jane Doe", 30, 40, 70);
        elevator.loadPassenger(passenger1);
        elevator.loadPassenger(passenger2);
        elevator.move(); // Move to floor 1, should unload passenger1 and set direction to UP for passenger2
        assertEquals(Direction.UP, elevator.getDirection());
    }

    @Test
    void testMoveUp() throws OverloadedElevatorException {
        elevator.setDirection(Direction.UP);
        elevator.move();
        assertEquals(6, elevator.getCurrentFloor());
    }

    @Test
    void testMoveDown() throws OverloadedElevatorException {
        elevator.setCurrentFloor(2);
        elevator.setDirection(Direction.DOWN);
        elevator.move();
        assertEquals(1, elevator.getCurrentFloor());
    }

    @Test
    void testLoadPassenger() throws InvalidWeightException, InvalidFloorException, OverloadedElevatorException {
        // Assuming the fourth parameter is of type int and represents some additional data required for the Passenger
        int additionalParameter = 0; // Example value, adjust according to what the parameter actually represents
        Passenger passenger = new Passenger("John Doe", 2, 70, 69); // Adjusted to include the fourth parameter
        assertTrue(elevator.loadPassenger(passenger));
        assertFalse(elevator.getPassengers().isEmpty());
    }

    @Test
    void testOverloadedElevator() throws OverloadedElevatorException {
        elevator.setMaxWeight(100); // Adjust max weight to ensure overload with two passengers
        Passenger passenger1 = new Passenger("John Doe", 1, 2, 50);
        Passenger passenger2 = new Passenger("Jane Doe", 1, 2, 60);
        passengers.add(passenger1);
        passengers.add(passenger2);// This should cause an overload
        ElevatorSystem elevatorSystem = new ElevatorSystem(elevators, passengers);
        assertTrue(elevator.loadPassenger(passenger1)); // Should succeed
        assertThrows(OverloadedElevatorException.class, () -> elevator.loadPassenger(passenger2)); // Should throw exception
    }

    @Test
    void testUnloadPassenger() throws InvalidWeightException, OverloadedElevatorException {
        // Load a passenger whose target floor is 1
        Passenger passenger = new Passenger("John Doe", 1, 15, 70);
        assertTrue(elevator.loadPassenger(passenger));

        // Explicitly set the elevator's direction to UP to simulate moving towards the target floor
        elevator.setDirection(Direction.UP);

        // Move the elevator to simulate reaching the target floor
        elevator.move();
        elevator.unloadPassenger(passenger);// This should also trigger unloading the passenger

        // Assert that the elevator has no passengers, indicating the passenger was unloaded
        assertTrue(elevator.getPassengers().isEmpty(), "Passenger was not unloaded at the target floor.");
    }

    @Test
    void testSetAndGetDirection() {
        elevator.setDirection(Direction.UP);
        assertEquals(Direction.UP, elevator.getDirection(), "Elevator direction should be UP.");

        elevator.setDirection(Direction.DOWN);
        assertEquals(Direction.DOWN, elevator.getDirection(), "Elevator direction should be DOWN.");
    }

    @Test
    void testOverloadingWithMultiplePassengers() throws OverloadedElevatorException {
        elevator.setMaxWeight(200); // Set max weight to 200 for this test
        elevator.loadPassenger(new Passenger("John Doe", 1, 2, 40));
        elevator.loadPassenger(new Passenger("Jane Doe", 1, 2, 50)); // This should not throw an exception yet
        OverloadedElevatorException thrown = assertThrows(OverloadedElevatorException.class, () -> elevator.loadPassenger(new Passenger("Jim Beam", 1, 2, 130)), "Elevator should be overloaded.");
        assertTrue(thrown.getMessage().contains("Elevator is overloaded."), "Elevator is overloaded.");
    }

    @Test
    void testUnloadSpecificPassengerByName() throws OverloadedElevatorException {
        Passenger passenger1 = new Passenger("Unique Name", 1, 2, 70);
        elevator.loadPassenger(passenger1);
        elevator.unloadPassenger(passenger1); // Assuming an implementation exists to unload by passenger object
        assertFalse(elevator.getPassengers().contains(passenger1), "Passenger should be unloaded.");
    }

    @Test
    void testLoadMultiplePassengersWithinLimits() throws OverloadedElevatorException {
        Passenger passenger1 = new Passenger("John", 1, 2, 100);
        Passenger passenger2 = new Passenger("Jane", 1, 3, 150);
        assertTrue(elevator.loadPassenger(passenger1));
        assertTrue(elevator.loadPassenger(passenger2));
        assertEquals(2, elevator.getPassengers().size());
    }


    @Test
    void testElevatorMovementWithPassengers() throws OverloadedElevatorException {
        Elevator elevator= new Elevator(1, 1, 500, 10);
        elevator.loadPassenger(new Passenger("John", 1, 3, 100));
        elevator.loadPassenger(new Passenger("Jane", 1, 2, 100));
        elevator.setDirection(Direction.UP);
        elevator.move(); // Move to floor 2
        assertEquals(2, elevator.getCurrentFloor());
        elevator.move(); // Move to floor 3
        assertEquals(3, elevator.getCurrentFloor());
        assertTrue(elevator.getPassengers().isEmpty());
    }

    @Test
    void testElevatorIdleWithNoPassengers() throws OverloadedElevatorException {
        elevator.move();
        assertEquals(Direction.IDLE, elevator.getDirection());
    }

    @Test
    void testDirectionAdjustmentWithPassengers() throws OverloadedElevatorException {
        // Setup
        Elevator elevator = new Elevator(1, 4, 500, 10); // Initial floor 4
        elevator.loadPassenger(new Passenger("John", 6, 8, 100)); // Destination floor 8

        // Initial state check
        assertEquals(Direction.IDLE, elevator.getDirection(), "Elevator should initially be IDLE.");

        // Move step-by-step towards floor 8 and check direction
        for (int i = 5; i <= 8; i++) {
            elevator.moveTowards(i);
            if (i < 8) {
                assertEquals(Direction.UP, elevator.getDirection(), "Elevator should move UP towards floor " + i);
            }
        }

        // Check the final floor
        assertEquals(8, elevator.getCurrentFloor(), "Elevator should be at floor 8.");

        // Check the final direction
        assertEquals(Direction.IDLE, elevator.getDirection(), "Elevator should be IDLE after reaching floor 8 and unloading passengers.");
    }

    @Test
    void testFindMostOptimalElevator() {
        elevators.add(new Elevator(1, 1, 1000, 10));
        elevators.add(new Elevator(2, 10, 1000, 10));
        Passenger passenger = new Passenger("Test", 3, 6, 80);
        passengers.add(passenger);

        Elevator optimalElevator = elevatorSystem.findMostOptimalElevator(passenger);
        assertEquals(1, optimalElevator.getId(), "Elevator 1 is closer to the passenger and should be selected.");
    }

    @Test
    void testAssignPassengersToElevators() throws OverloadedElevatorException {
        Elevator elevator1 = new Elevator(1, 1, 1000, 10);
        Elevator elevator2 = new Elevator(2, 5, 1000, 10);
        elevators.add(elevator1);
        elevators.add(elevator2);
        Passenger passenger1 = new Passenger("Alice", 1, 5, 80);
        Passenger passenger2 = new Passenger("Bob", 4, 6, 70);
        passengers.add(passenger1);
        passengers.add(passenger2);

        elevatorSystem.assignPassengersToElevators();

        assertTrue(elevator1.getPassengers().contains(passenger1), "Passenger Alice should be assigned to Elevator 1.");
        assertTrue(elevator2.getPassengers().contains(passenger2), "Passenger Bob should be assigned to Elevator 2.");
    }







    @Test
    void testLoadPassengerAtWeightLimit() throws OverloadedElevatorException {
        Passenger passenger = new Passenger("Max Weight", 1, 2, elevator.getMaxWeight());
        assertTrue(elevator.loadPassenger(passenger), "Elevator should load passenger at max weight limit.");
    }


    @Test
    void testElevatorMovementToSpecificFloorWithoutPassengers() {
        Elevator elevator = new Elevator(1, 4, 500, 10);
        elevator.moveTowards(5); // Move towards floor 5
        assertEquals(5, elevator.getCurrentFloor(), "Elevator should move to floor 5.");
        assertEquals(Direction.IDLE, elevator.getDirection(), "Elevator should be idle after reaching target floor.");
    }

    @Test
    void testCanAccommodateWeight() {
        Passenger passenger = new Passenger("Lightweight", 1, 2, 100);
        assertTrue(elevator.canAccommodateWeight(passenger), "Elevator should accommodate passenger without exceeding weight limit.");
    }

    @Test
    void testGetNextFloor() throws OverloadedElevatorException {
        elevator.loadPassenger(new Passenger("To Floor 3", 1, 30, 70));
        elevator.loadPassenger(new Passenger("To Floor 2", 1, 20, 60));
        elevator.setDirection(Direction.UP);
        Integer nextFloor = elevator.getNextFloor();
        assertNotNull(nextFloor, "Next floor should not be null.");
        assertEquals(Integer.valueOf(20), nextFloor, "Next floor should be 2 as it's the closest target floor in the current direction.");
    }


    @Test
    void testDetermineDirectionWhenIdleAndPassengersAbove() throws OverloadedElevatorException {
        Elevator elevator = new Elevator(1, 0, 500, 10);
        elevator.loadPassenger(new Passenger("John", 1, 3, 100)); // Passenger wants to go up
        assertEquals(Direction.UP.ordinal(), elevator.determineDirection(), "Elevator should determine direction as UP.");
    }

    @Test
    void testDetermineDirectionWhenIdleAndPassengersBelow() throws OverloadedElevatorException {
        Elevator elevator = new Elevator(1, 5, 500, 10);
        elevator.loadPassenger(new Passenger("Jane", 1, 2, 100)); // Passenger wants to go down
        assertEquals(Direction.DOWN.ordinal(), elevator.determineDirection(), "Elevator should determine direction as DOWN.");
    }

    @Test
    void testDetermineDirectionWhenMovingUp() throws OverloadedElevatorException {
        Elevator elevator = new Elevator(1, 2, 500, 10);
        elevator.setDirection(Direction.UP);
        elevator.loadPassenger(new Passenger("John", 1, 4, 100)); // Passenger wants to go further up
        elevator.loadPassenger(new Passenger("Jane", 1, 1, 100)); // Passenger wants to go down
        assertEquals(Direction.UP.ordinal(), elevator.determineDirection(), "Elevator should continue moving UP.");
    }

    @Test
    void testDetermineDirectionWhenMovingDown() throws OverloadedElevatorException {
        Elevator elevator = new Elevator(1, 4, 500, 10);
        elevator.setDirection(Direction.DOWN);
        elevator.loadPassenger(new Passenger("John", 1, 2, 100)); // Passenger wants to go further down
        elevator.loadPassenger(new Passenger("Jane", 1, 5, 100)); // Passenger wants to go up
        assertEquals(Direction.DOWN.ordinal(), elevator.determineDirection(), "Elevator should continue moving DOWN.");
    }

    @Test
    void testDetermineDirectionWithNoPassengers() {
        Elevator elevator = new Elevator(1, 2, 500, 10);
        assertEquals(Direction.IDLE.ordinal(), elevator.determineDirection(), "Elevator should determine direction as IDLE with no passengers.");
    }
}

