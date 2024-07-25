package org.example.elevator;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.passenger.Passenger;

import java.util.List;

public class ElevatorSystemData {
    private List<Elevator> elevators;
    private List<Passenger> passengers;

    @JsonProperty("elevators")
    public List<Elevator> getElevators() {
        return elevators;
    }

    public void setElevators(List<Elevator> elevators) {
        this.elevators = elevators;
    }

    @JsonProperty("passengers")
    public List<Passenger> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<Passenger> passengers) {
        this.passengers = passengers;
    }
}