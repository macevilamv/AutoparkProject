package by.incubator.autopark;

public class Main {
    public static void main(String[] args) {
        Vehicle [] vehicles = initVehicleArray();

        vehicleUtil.print(vehicles);
        vehicleUtil.sort(vehicles);
        vehicleUtil.print(vehicles);
        vehicleUtil.findMileageExtrema(vehicles);
    }

   private static class vehicleUtil {
        private static void print(Vehicle[] vehicles) {
            for (Vehicle vehicle : vehicles) {
                System.out.println(vehicle);
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
        return new VehicleType[] {
                new VehicleType("Bus", 1.2d),
                new VehicleType("Car", 1d),
                new VehicleType("Rink", 1.5d),
                new VehicleType("Tractor", 1.2d)
        };
    }

    private static Vehicle[] initVehicleArray() {
        VehicleType [] vehicleTypes = initVehicleTypeArray();

        return new Vehicle[] {
                new Vehicle(vehicleTypes[0], CarColor.BLUE, "Volkswagen Crafter",
                        "5427 AX-7", 2022, 376000, 2015),
                new Vehicle(vehicleTypes[0], CarColor.WHITE, "Volkswagen Crafter",
                        "6427 AX-7", 2500, 227010, 2014),
                new Vehicle(vehicleTypes[0], CarColor.GREEN, "Electric Bus E321",
                        "6785 BA-7", 12080, 20451, 2019),
                new Vehicle(vehicleTypes[1], CarColor.GRAY, "Golf 5",
                        "8682 AX-7", 1200, 230451, 2006),
                new Vehicle(vehicleTypes[1], CarColor.WHITE, "Tesla Model S 70D",
                        "0001 AA-7", 2200, 10454, 2019),
                new Vehicle(vehicleTypes[2], CarColor.YELLOW, "Hamm HD 12 VV",
                        null, 3000, 122, 2016),
                new Vehicle(vehicleTypes[3], CarColor.RED, "МТЗ Беларус-1025.4",
                        "1145 AB-7", 1200, 109, 2020),
        };
    }
}
