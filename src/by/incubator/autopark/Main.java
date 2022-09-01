package by.incubator.autopark;

import by.incubator.autopark.collections.MyQueue;
import by.incubator.autopark.collections.VehicleCollection;
import by.incubator.autopark.service.CarWash;
import by.incubator.autopark.service.Garage;
import by.incubator.autopark.vehicle.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        int vehicleNum = 15;
        Vehicle [] vehicles = initVehicleArray(vehicleNum);
        Garage garage = new Garage(vehicleNum);

        for (Vehicle vehicle : vehicles) {
            garage.pullInto(vehicle);
        }
        for (int i = 0; i < vehicleNum + 1; i++) {
            garage.pullOut();
        }
    }

    private static Vehicle[] initVehicleArray(int size) {
        Vehicle [] vehicles = new Vehicle[size];

        for (int i = 0; i < size; i++) {
            vehicles[i] = new Vehicle();
        }

        return vehicles;
    }

    private static class VehicleUtil {
        private static Vehicle findMaxKilometersCar(Vehicle[] vehicles) {
            Arrays.sort(vehicles, new Comparator<Vehicle>() {
                @Override
                public int compare(Vehicle o1, Vehicle o2) {
                    return Double.compare(o1.getEngine().getMaxKilometers(), o2.getEngine().getMaxKilometers());
                }
            });
            return vehicles[vehicles.length - 1];
        }

        private static void print(Vehicle... vehicles) {
            for (Vehicle vehicle : vehicles) {
                System.out.println(vehicle);
            }
        }

        private static void findEqualCars(Vehicle[] vehicles) {
            for (int i = 0; i < vehicles.length; i++) {
                for (int j = i + 1; j < vehicles.length; j++) {
                    if (vehicles[i].equals(vehicles[j])) {
                        print(vehicles[i], vehicles[j]);
                    }
                }
            }
        }

        private static void sort(Vehicle[] vehicles) {
            boolean flag = true;
            Vehicle holder;

            while (flag) {
                flag = false;

                for (int i = 0; i < vehicles.length - 1; i++) {
                    if (vehicles[i].compareTo(vehicles[i + 1]) > 0) {
                        holder = vehicles[i + 1];
                        vehicles[i + 1] = vehicles[i];
                        vehicles[i] = holder;
                        flag = true;
                    }
                }
            }
        }

        private static void findMileageExtrema(Vehicle[] vehicles) {
            Vehicle max = vehicles[0];
            Vehicle min = vehicles[0];

            for (int i = 0; i < vehicles.length; i++) {
                if (max.getMileage() < vehicles[i].getMileage()) {
                    max = vehicles[i];
                }
                if (min.getMileage() > vehicles[i].getMileage()) {
                    min = vehicles[i];
                }
            }
            System.out.println("The car with the minimal mileage= " + min);
            System.out.println("The car with the maximal mileage= " + max);
        }
    }
}
