package org.example.passenger;

import org.example.passenger.Passenger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PassengerTest {

    @Test
    public void testPassengerConstructorAndGetters() {
        Passenger passenger = new Passenger("John Doe", 1, 5, 150);
        Assertions.assertEquals("John Doe", passenger.getName());
        Assertions.assertEquals(1, passenger.getCurrentFloor());
        Assertions.assertEquals(5, passenger.getTargetFloor());
        Assertions.assertEquals(150, passenger.getWeight());
    }

    @Test
    public void testSetName() {
        Passenger passenger = new Passenger("John Doe", 1, 5, 150);
        passenger.setName("Jane Doe");
        Assertions.assertEquals("Jane Doe", passenger.getName());
    }

    @Test
    public void testSetCurrentFloor() {
        Passenger passenger = new Passenger("John Doe", 1, 5, 150);
        passenger.setCurrentFloor(2);
        Assertions.assertEquals(2, passenger.getCurrentFloor());
    }

    @Test
    public void testSetTargetFloor() {
        Passenger passenger = new Passenger("John Doe", 1, 5, 150);
        passenger.setTargetFloor(6);
        Assertions.assertEquals(6, passenger.getTargetFloor());
    }

    @Test
    public void testSetWeight() {
        Passenger passenger = new Passenger("John Doe", 1, 5, 150);
        passenger.setWeight(160);
        Assertions.assertEquals(160, passenger.getWeight());
    }
}