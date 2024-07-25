package org.example.passenger;

import org.example.exceptions.InvalidFloorException;
import org.example.exceptions.InvalidWeightException;

public class ValidationUtil {

    public static void validateWeight(int weight) throws InvalidWeightException {
        if (weight <= 0 || weight > 600) { // Assuming 600 is the max weight limit for any elevator
            throw new InvalidWeightException("Invalid weight: " + weight);
        }
    }

    public static void validateFloorNumber(int floorNumber, int minFloor, int maxFloor) throws InvalidFloorException {
        if (floorNumber < minFloor || floorNumber > maxFloor) {
            throw new InvalidFloorException("Invalid floor number: " + floorNumber);
        }
    }
}