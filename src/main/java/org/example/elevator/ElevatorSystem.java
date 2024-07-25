package org.example.elevator;

import org.example.exceptions.OverloadedElevatorException;
import org.example.passenger.Passenger;
import java.util.ArrayList;
import java.util.List;

public class ElevatorSystem {
    private List<Elevator> elevators;
    private List<Passenger> passengers;

    public ElevatorSystem(List<Elevator> elevators, List<Passenger> passengers) {
        this.elevators = elevators;
        this.passengers = passengers;
    }

    // Getters and Setters
    public List<Elevator> getElevators() {
        return elevators;
    }

    public void setElevators(List<Elevator> elevators) {
        this.elevators = elevators;
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<Passenger> passengers) {
        this.passengers = passengers;
    }

    public void assignPassengersToElevators() throws OverloadedElevatorException {
        for (Passenger passenger : new ArrayList<>(passengers)) {
            Elevator optimalElevator = findMostOptimalElevator(passenger);
            if (optimalElevator != null && optimalElevator.canAccommodateWeight(passenger)) {
                optimalElevator.loadPassenger(passenger);
                System.out.println("Passenger " + passenger.getName() + " assigned to elevator " + optimalElevator.getId());
            } else {
                System.out.println("Passenger " + passenger.getName() + " could not be assigned to any elevator.");
            }
        }
    }

    public void simulateElevatorMovements() {
        for (Elevator elevator : elevators) {
            while (!elevator.getPassengers().isEmpty()) {
                Integer nextFloor = elevator.getNextFloor();
                if (nextFloor != null) {
                    elevator.setCurrentFloor(nextFloor);
                    List<Passenger> unloadedPassengers = new ArrayList<>();
                    for (Passenger passenger : new ArrayList<>(elevator.getPassengers())) {
                        if (passenger.getTargetFloor() == elevator.getCurrentFloor()) {
                            unloadedPassengers.add(passenger);
                        }
                    }
                    elevator.getPassengers().removeAll(unloadedPassengers);
                }
                System.out.println("Elevator ID: " + elevator.getId() + ", Current Floor: " + elevator.getCurrentFloor() + ", Passengers: " + elevator.getPassengers().size());
            }
        }
    }

   
    public void printFinalState() {
        System.out.println("Final State of Elevator System:");
        for (Elevator elevator : elevators) {
            System.out.println("Elevator ID: " + elevator.getId() + ", Current Floor: " + elevator.getCurrentFloor());
            System.out.println("Passengers in Elevator: " + elevator.getPassengers().size());
        }
        System.out.println("Passengers: " + passengers.size());
    }

    public Elevator findMostOptimalElevator(Passenger passenger) {
        Elevator optimalElevator = null;
        int shortestDistance = Integer.MAX_VALUE;

        for (Elevator elevator : elevators) {
            if (!elevator.canAccommodateWeight(passenger)) continue;

            int distance = Math.abs(elevator.getCurrentFloor() - passenger.getCurrentFloor());
            // Adjusting distance based on direction
            boolean isElevatorApproaching = (elevator.getDirection() == Direction.UP && passenger.getCurrentFloor() > elevator.getCurrentFloor()) ||
                    (elevator.getDirection() == Direction.DOWN && passenger.getCurrentFloor() < elevator.getCurrentFloor());
            if (!isElevatorApproaching) {
                distance += 100; // Arbitrary large number to deprioritize elevators moving away
            }

            if (distance < shortestDistance) {
                optimalElevator = elevator;
                shortestDistance = distance;
            }
        }

        return optimalElevator;
    }
}
