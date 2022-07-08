package by.incubator.autopark;

import by.incubator.autopark.engine.*;
import by.incubator.autopark.vehicle.*;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Vehicle[] vehicles = initVehicleArray();
        VehicleUtil.findEqualCars(vehicles);
        VehicleUtil.print(VehicleUtil.findMaxKilometersCar(vehicles));
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

    private static VehicleType[] initVehicleTypeArray() {
        return new VehicleType[]{
                new VehicleType("Bus", 1.2d),
                new VehicleType("Car", 1d),
                new VehicleType("Rink", 1.5d),
                new VehicleType("Tractor", 1.2d)
        };
    }

    private static Vehicle[] initVehicleArray() {
        VehicleType[] vehicleTypes = initVehicleTypeArray();

        return new Vehicle[]{
                new Vehicle(vehicleTypes[0], CarColor.BLUE, "Volkswagen Crafter",
                        "5427 AX-7", 2022, 376000, 2015, new DieselEngine(2.0d, 8.1d, 75d)),
                new Vehicle(vehicleTypes[0], CarColor.WHITE, "Volkswagen Crafter",
                        "6427 AX-7", 2500, 227010, 2014, new DieselEngine(2.18d, 8.5d, 150d)),
                new Vehicle(vehicleTypes[0], CarColor.GREEN, "Electric Bus E321",
                        "6785 BA-7", 12080, 20451, 2019, new ElectricalEngine(50d, 150d)),
                new Vehicle(vehicleTypes[1], CarColor.GRAY, "Golf 5",
                        "8682 AX-7", 1200, 230451, 2006, new DieselEngine(1.6d, 7.2d, 55d)),
                new Vehicle(vehicleTypes[1], CarColor.WHITE, "Tesla Model S 70D",
                        "0001 AA-7", 2200, 10454, 2019, new ElectricalEngine(25d, 70d)),
                new Vehicle(vehicleTypes[2], CarColor.YELLOW, "Hamm HD 12 VV",
                        null, 3000, 122, 2016, new DieselEngine(3.2d, 25d, 20d)),
                new Vehicle(vehicleTypes[3], CarColor.RED, "МТЗ Беларус-1025.4",
                        "1145 AB-7", 1200, 109, 2020, new DieselEngine(4.75d, 20.1d, 135d)),
        };
    }
}
