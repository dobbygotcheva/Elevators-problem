package org.example.elevator;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.passenger.Passenger;
import org.example.exceptions.InvalidFloorException;
import org.example.exceptions.InvalidWeightException;
import org.example.exceptions.OverloadedElevatorException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.OptionalInt;

enum Direction {
    UP, DOWN, IDLE
}

public class Elevator {
    private int id;
    private int currentFloor;
    private int maxWeight;
    private List<Passenger> passengers;
    private Direction direction = Direction.IDLE;
    private int currentWeight;
    private int capacity;

    @JsonCreator
    public Elevator(@JsonProperty("id") int id,
                    @JsonProperty("currentFloor") int currentFloor,
                    @JsonProperty("maxWeight") int maxWeight,
                    @JsonProperty("capacity") int capacity) {
        this.id = id;
        this.currentFloor = currentFloor;
        this.maxWeight = maxWeight;
        this.passengers = new ArrayList<>();
        this.capacity = capacity; // Correctly assign the capacity
    }


    public List<Passenger> getPassengers() {
        return passengers;
    }

    @JsonProperty("capacity")
    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    public int getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(int maxWeight) {
        this.maxWeight = maxWeight;
    }

    public void setPassengers(List<Passenger> passengers) {
        this.passengers = passengers;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public boolean isOverloaded() {
        int totalWeight = passengers.stream().mapToInt(Passenger::getWeight).sum();
        return totalWeight > maxWeight;
    }


    public boolean canLoadPassenger(Passenger passenger) {
        int totalWeight = passengers.stream().mapToInt(Passenger::getWeight).sum();
        return (totalWeight + passenger.getWeight()) <= maxWeight;
    }


    public void moveTowards(int targetFloor) {
        // Set the initial direction based on the target floor
        if (this.currentFloor < targetFloor) {
            this.direction = Direction.UP;
        } else if (this.currentFloor > targetFloor) {
            this.direction = Direction.DOWN;
        } else {
            this.direction = Direction.IDLE;
        }
        System.out.println("Initial direction set to: " + this.direction);

        // Logic to move towards the target floor
        while (this.currentFloor != targetFloor) {
            // Simulate moving floor by floor towards the target
            if (this.direction == Direction.UP) {
                this.currentFloor++;
            } else if (this.direction == Direction.DOWN) {
                this.currentFloor--;
            }

            System.out.println("Moving to floor: " + this.currentFloor);

            // Unload passengers at each floor if necessary
            unloadPassengers(this.currentFloor);
        }

        // Final adjustment after reaching the target floor
        adjustDirectionBasedOnRemainingPassengers();
        System.out.println("Final direction after adjustment: " + this.direction);
    }


    void adjustDirectionBasedOnRemainingPassengers() {
        if (passengers.isEmpty()) {
            direction = Direction.IDLE;
            return;
        }

        OptionalInt closestUpFloor = passengers.stream()
                .mapToInt(Passenger::getTargetFloor)
                .filter(floor -> floor > currentFloor)
                .min();

        OptionalInt closestDownFloor = passengers.stream()
                .mapToInt(Passenger::getTargetFloor)
                .filter(floor -> floor < currentFloor)
                .max();

        if (closestUpFloor.isPresent() && (!closestDownFloor.isPresent() || closestUpFloor.getAsInt() < closestDownFloor.getAsInt())) {
            direction = Direction.UP;
        } else if (closestDownFloor.isPresent()) {
            direction = Direction.DOWN;
        } else {
            direction = Direction.IDLE;
        }
    }

    public boolean canAccommodateWeight(Passenger passenger) {
        return this.currentWeight + passenger.getWeight() <= this.maxWeight;
    }


    public int determineDirection() {
        int closestUpDistance = Integer.MAX_VALUE;
        int closestDownDistance = Integer.MAX_VALUE;

        for (Passenger passenger : passengers) {
            int distance = passenger.getTargetFloor() - this.currentFloor;
            if (distance > 0) {
                closestUpDistance = Math.min(closestUpDistance, distance);
            } else if (distance < 0) {
                closestDownDistance = Math.min(closestDownDistance, -distance);
            }
        }

        if (closestUpDistance != Integer.MAX_VALUE && closestDownDistance != Integer.MAX_VALUE) {
            if (this.direction == Direction.DOWN) {
                return closestDownDistance < closestUpDistance ? Direction.DOWN.ordinal() : this.direction.ordinal();
            } else {
                return closestUpDistance <= closestDownDistance ? Direction.UP.ordinal() : this.direction.ordinal();
            }
        } else if (closestUpDistance != Integer.MAX_VALUE) {
            return Direction.UP.ordinal();
        } else if (closestDownDistance != Integer.MAX_VALUE) {
            return Direction.DOWN.ordinal();
        } else {
            return Direction.IDLE.ordinal();
        }
    }

    public void move() throws OverloadedElevatorException {
        if (isOverloaded()) {
            throw new OverloadedElevatorException("Elevator cannot exceed max weight.");
        }
        switch (direction) {
            case UP:
                currentFloor++;
                break;
            case DOWN:
                currentFloor--;
                break;
            case IDLE:
                adjustDirectionBasedOnRemainingPassengers();
                break;
        }
        unloadPassengers(currentFloor);
        if (passengers.isEmpty()) {
            direction = Direction.IDLE;
        } else {
            adjustDirectionBasedOnRemainingPassengers();
        }
    }

    public void unloadPassengers(int floor) {
        Iterator<Passenger> iterator = passengers.iterator();
        while (iterator.hasNext()) {
            Passenger passenger = iterator.next();
            if (passenger.getTargetFloor() == floor) {
                iterator.remove();
            }
        }
    }

    public boolean loadPassenger(Passenger passenger) throws OverloadedElevatorException {
        if (isOverloaded() || passengers.size() >= capacity) {
            throw new OverloadedElevatorException("Elevator is overloaded.");
        }
        else{
            passengers.add(passenger);
            currentWeight += passenger.getWeight();
            if(currentWeight>maxWeight){
                throw new OverloadedElevatorException("Elevator is overloaded.");
            }
            return true;
        }

    }

    public Integer getNextFloor() {
        Integer closestUpFloor = null;
        Integer closestDownFloor = null;

        // Iterate through all passengers to find the closest target floor in both directions
        for (Passenger passenger : passengers) {
            int targetFloor = passenger.getTargetFloor();
            // If the target floor is above the current floor
            if (targetFloor > currentFloor) {
                // Update closestUpFloor if it's null or the found target floor is closer
                if (closestUpFloor == null || targetFloor < closestUpFloor) {
                    closestUpFloor = targetFloor;
                }
            }
            // If the target floor is below the current floor
            else if (targetFloor < currentFloor) {
                // Update closestDownFloor if it's null or the found target floor is closer
                if (closestDownFloor == null || targetFloor > closestDownFloor) {
                    closestDownFloor = targetFloor;
                }
            }
        }

        // Determine the next floor based on the current direction
        if (direction == Direction.UP && closestUpFloor != null) {
            return closestUpFloor;
        } else if (direction == Direction.DOWN && closestDownFloor != null) {
            return closestDownFloor;
        } else if (closestUpFloor != null) {
            // If the elevator is idle but there's a closer floor upwards, prioritize it
            return closestUpFloor;
        } else if (closestDownFloor != null) {
            // If the elevator is idle but there's a closer floor downwards, prioritize it
            return closestDownFloor;
        }

        // Return null if there are no passengers or no reachable floors in the current direction
        return null;
    }

    public void unloadPassenger(Passenger passenger1) {
        passengers.remove(passenger1);
    }
}