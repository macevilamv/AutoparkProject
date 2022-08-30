package by.incubator.autopark.service;

import by.incubator.autopark.collections.MyQueue;
import by.incubator.autopark.vehicle.Vehicle;

public class CarWash {

    public static void launchCarWash(Vehicle [] vehicles) {
        MyQueue<Vehicle> vehicleQueue = new MyQueue<>();

        for (Vehicle vehicle : vehicles) {
            vehicleQueue.enqueue(vehicle);
        }
        for (int i = 0; i < vehicleQueue.size(); i++) {
            washCar((Vehicle) vehicleQueue.dequeue());
        }
    }

    private static void washCar(Vehicle vehicle) {
        System.out.println("Car: " + vehicle.getModel() + " |"
                + vehicle.getRegistrationNumber() +  "| is washed!");
    }
}
