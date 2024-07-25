package org.example.passenger;

import org.example.exceptions.InvalidFloorException;
import org.example.exceptions.InvalidWeightException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidationUtilTest {

    @Test
    void validateWeight_ValidWeight_NoException() {
        assertDoesNotThrow(() -> ValidationUtil.validateWeight(300));
    }

    @Test
    void validateWeight_ZeroWeight_ThrowsException() {
        assertThrows(InvalidWeightException.class, () -> ValidationUtil.validateWeight(0));
    }

    @Test
    void validateWeight_NegativeWeight_ThrowsException() {
        assertThrows(InvalidWeightException.class, () -> ValidationUtil.validateWeight(-1));
    }

    @Test
    void validateWeight_WeightAboveLimit_ThrowsException() {
        assertThrows(InvalidWeightException.class, () -> ValidationUtil.validateWeight(601));
    }

    @Test
    void validateFloorNumber_ValidFloor_NoException() {
        assertDoesNotThrow(() -> ValidationUtil.validateFloorNumber(5, 1, 10));
    }

    @Test
    void validateFloorNumber_BelowMinimumFloor_ThrowsException() {
        assertThrows(InvalidFloorException.class, () -> ValidationUtil.validateFloorNumber(0, 1, 10));
    }

    @Test
    void validateFloorNumber_AboveMaximumFloor_ThrowsException() {
        assertThrows(InvalidFloorException.class, () -> ValidationUtil.validateFloorNumber(11, 1, 10));
    }

    // Optional: Test boundary conditions for floor numbers
    @Test
    void validateFloorNumber_MinimumFloor_NoException() {
        assertDoesNotThrow(() -> ValidationUtil.validateFloorNumber(1, 1, 10));
    }

    @Test
    void validateFloorNumber_MaximumFloor_NoException() {
        assertDoesNotThrow(() -> ValidationUtil.validateFloorNumber(10, 1, 10));
    }
}