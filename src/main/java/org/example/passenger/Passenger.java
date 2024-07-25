package org.example.passenger;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Passenger {
    private String name;
    private int currentFloor;
    private int targetFloor;
    private int weight;

    @JsonCreator
    public Passenger(@JsonProperty("name") String name,
                     @JsonProperty("currentFloor") int currentFloor,
                     @JsonProperty("targetFloor") int targetFloor,
                     @JsonProperty("weight") int weight) {
        this.name = name;
        this.currentFloor = currentFloor;
        this.targetFloor = targetFloor;
        this.weight = weight;
    }

    // Getters and setters

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("currentFloor")
    public int getCurrentFloor() {
        return currentFloor;
    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    @JsonProperty("targetFloor")
    public int getTargetFloor() {
        return targetFloor;
    }

    public void setTargetFloor(int targetFloor) {
        this.targetFloor = targetFloor;
    }

    @JsonProperty("weight")
    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}