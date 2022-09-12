package by.incubator.autopark.collections;

import by.incubator.autopark.engine.DieselEngine;
import by.incubator.autopark.engine.ElectricalEngine;
import by.incubator.autopark.engine.GasolineEngine;
import by.incubator.autopark.engine.Startable;
import by.incubator.autopark.rent.Rent;
import by.incubator.autopark.vehicle.CarColor;
import by.incubator.autopark.vehicle.Vehicle;
import by.incubator.autopark.vehicle.VehicleType;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VehicleCollection {
    private List<VehicleType> types;
    private List<Vehicle> vehicles;
    private List<Rent> rents;
    private static String csvConfigPath = "resources/csv/csv-config.properties";
    private static final Map<String, Integer> config = new HashMap<>();

    static {
        try {
            initProperties(csvConfigPath);
        } catch (IOException e) {
            throw new RuntimeException("Error: \"csv-config.properties\" file reading has failed.", e);
        }
    }

    public VehicleCollection(String types, String vehicles, String rents) {
        try {
            this.types = loadTypes(types);
            this.rents = loadRents(rents);
            this.vehicles = loadVehicles(vehicles);
        } catch (IOException | ParseException e) {
            throw new IllegalArgumentException("Error: incorrect path to .csv file.", e);
        }
    }

    public void insert(int index, Vehicle vehicle) {
        if (index < 0 || index >= this.vehicles.size()) {
            this.vehicles.add(vehicle);
        } else {
            this.vehicles.add(index, vehicle);
        }
    }

    public int delete(int index) {
        if (index < 0 || index >= this.vehicles.size()) {
            return -1;
        } else {
            this.vehicles.remove(index);
            return index;
        }
    }

    public double sumTotalProfit() {
        double sum = 0.0d;

        for (Vehicle vehicle : vehicles) {
            sum += vehicle.getTotalProfit();
        }
        return sum;
    }

    public void display() {
        Map <String, Integer> columnsLengths = new HashMap<>();
        FieldsToRender [] fields = FieldsToRender.values();
        String pattern;

        getColumnsLengthData(columnsLengths, fields);

        pattern = "| %" + columnsLengths.get(FieldsToRender.ID.name()) + "s |"
                + " %" + columnsLengths.get(FieldsToRender.Type.name()) + "s |"
                + " %" + columnsLengths.get(FieldsToRender.Model.name()) + "s |"
                + " %" + columnsLengths.get(FieldsToRender.Number.name()) + "s |"
                + " %" + columnsLengths.get(FieldsToRender.Weight.name()) + "s |"
                + " %" + columnsLengths.get(FieldsToRender.Year.name()) + "s |"
                + " %" + columnsLengths.get(FieldsToRender.Mileage.name()) + "s |"
                + " %" + columnsLengths.get(FieldsToRender.Color.name()) + "s | "
                + " %" + columnsLengths.get(FieldsToRender.Income.name()) + "s |"
                + " %" + columnsLengths.get(FieldsToRender.Tax.name()) + "s | "
                + " %" + columnsLengths.get(FieldsToRender.Profit.name()) + "s | \n";

        System.out.printf(pattern, FieldsToRender.ID.name(), FieldsToRender.Type.name(),
                                   FieldsToRender.Model.name(), FieldsToRender.Number.name(),
                                   FieldsToRender.Weight.name(), FieldsToRender.Year.name(),
                                   FieldsToRender.Mileage.name(), FieldsToRender.Color.name(),
                                   FieldsToRender.Income.name(), FieldsToRender.Tax.name(),
                                   FieldsToRender.Profit.name());

        for (Vehicle vehicle : this.vehicles) {
            System.out.printf(pattern,
                    vehicle.getId(), vehicle.getType().getTypeName(), vehicle.getModel(),
                    vehicle.getRegistrationNumber(), vehicle.getMass(), vehicle.getManufactureYear(),
                    vehicle.getMileage(), vehicle.getColor(), vehicle.getTotalIncome(), vehicle.getCalcTaxPerMonth(),
                    vehicle.getTotalProfit()
            );
        }
    }

    public void sort() {
        Collections.sort(this.vehicles, new VehicleModelComparator());
    }

    private void getColumnsLengthData(Map<String, Integer> columnsLengths, FieldsToRender[] fields) {
        for (FieldsToRender field : fields) {
            columnsLengths.put(field.name(), field.name().length());
        }

        for (Vehicle vehicle : this.vehicles) {
            columnsLengths.computeIfPresent(FieldsToRender.ID.name(),
                    (key, oldValue) -> Integer.max(oldValue, vehicle.getId()));
            columnsLengths.computeIfPresent(FieldsToRender.Type.name(),
                    (key, oldValue) -> Integer.max(oldValue, vehicle.getType().getTypeName().length()));
            columnsLengths.computeIfPresent(FieldsToRender.Model.name(),
                    (key, oldValue) -> Integer.max(oldValue, vehicle.getModel().length()));
            columnsLengths.computeIfPresent(FieldsToRender.Color.name(),
                    (key, oldValue) -> Integer.max(oldValue, vehicle.getColor().name().length()));
            columnsLengths.computeIfPresent(FieldsToRender.Profit.name(),
                    (key, oldValue) -> Integer.max(oldValue, ((Object) vehicle.getTotalProfit()).toString().length()));
            columnsLengths.computeIfPresent(FieldsToRender.Weight.name(),
                    (key, oldValue) -> Integer.max(oldValue, ((Object) vehicle.getMass()).toString().length()));
            columnsLengths.computeIfPresent(FieldsToRender.Mileage.name(),
                    (key, oldValue) -> Integer.max(oldValue, ((Object) vehicle.getMileage()).toString().length()));
            columnsLengths.computeIfPresent(FieldsToRender.Number.name(),
                    (key, oldValue) -> Integer.max(oldValue, vehicle.getRegistrationNumber().length()));
            columnsLengths.computeIfPresent(FieldsToRender.Income.name(),
                    (key, oldValue) -> Integer.max(oldValue, ((Object) vehicle.getTotalIncome()).toString().length()));
            columnsLengths.computeIfPresent(FieldsToRender.Year.name(),
                    (key, oldValue) -> Integer.max(oldValue, ((Object) vehicle.getManufactureYear()).toString().length()));
            columnsLengths.computeIfPresent(FieldsToRender.Tax.name(),
                    (key, oldValue) -> Integer.max(oldValue, ((Object) vehicle.getCalcTaxPerMonth()).toString().length()));
        }
    }

    public static List<VehicleType> loadTypes(String inFile) throws IOException {
        List<VehicleType> typesList = new ArrayList<>();
        String path = "resources/csv/" + inFile;
        File csvFile = new File(path);
        List<String> typesBuffer = proceedStrings(readFromFile(csvFile));

        for (String csvString : typesBuffer) {
            typesList.add(createType(csvString));
        }
        return typesList;
    }

    public static List<Rent> loadRents(String inFile) throws IOException, ParseException {
        List<Rent> rentList = new ArrayList<>();
        String path = "resources/csv/" + inFile;
        File csvFile = new File(path);
        List<String> rentsBuffer = proceedStrings(readFromFile(csvFile));

        for (String csvString : rentsBuffer) {
            rentList.add(createRent(csvString));
        }
        return rentList;
    }

    public static List<Vehicle> loadVehicles(String inFile) throws IOException, ParseException {
        List<Vehicle> vehicleList = new ArrayList<>();
        String path = "resources/csv/" + inFile;
        File csvFile = new File(path);
        List<String> vehiclesBuffer = proceedStrings(readFromFile(csvFile));

        for (String csvString : vehiclesBuffer) {
            vehicleList.add(createVehicle(csvString));
        }
        return vehicleList;
    }

    private static VehicleType createType(String csvStringOfType) {
        int id = 0;
        String typeName = null;
        double tax = 0.0d;
        String[] typeParametersBuffer = csvStringOfType.split(",");

        id = Integer.parseInt(typeParametersBuffer[config.get("TYPE-CSV_ID")]);
        typeName = typeParametersBuffer[config.get("TYPE-CSV_NAME")];
        tax = Double.parseDouble(typeParametersBuffer[config.get("TYPE-CSV_TAX")]);

        return new VehicleType(id, typeName, tax);
    }

    private static Rent createRent(String csvStringOfRent) throws ParseException {
        int vehicleId = 0;
        String rentDate = null;
        double cost = 0.0d;
        DateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String[] rentParametersBuffer = csvStringOfRent.split(",");

        vehicleId = Integer.parseInt(rentParametersBuffer[config.get("RENT-CSV_CAR_ID")]);
        rentDate = simpleDateFormat.format(simpleDateFormat.
                parse(rentParametersBuffer[config.get("RENT-CSV_DATE")]));
        cost = Double.parseDouble(rentParametersBuffer[config.get("RENT-CSV_COST")]);

        return new Rent(vehicleId, rentDate, cost);
    }

    private static Vehicle createVehicle(String csvStringOfVehicle) throws IOException, ParseException {
        double mass = 0.0d;
        int mileage = 0;
        int manufactureYear = 0;
        int id = 0;
        int typeId = 0;
        VehicleType vehicleType;
        CarColor color;
        String model;
        String registrationNumber;
        String engineName;
        Startable engine;
        List<Rent> rents;
        String[] vehicleParametersBuffer = csvStringOfVehicle.split(",");

        id = Integer.parseInt(vehicleParametersBuffer[config.get("VEHICLE-CSV_ID")]);
        typeId = Integer.parseInt(vehicleParametersBuffer[config.get("VEHICLE-CSV_TYPE_ID")]);
        vehicleType = generateTypeById(typeId);
        model = vehicleParametersBuffer[config.get("VEHICLE-CSV_MODEL")];
        registrationNumber = vehicleParametersBuffer[config.get("VEHICLE-CSV_NUMBER")];
        mass = Double.parseDouble(vehicleParametersBuffer[config.get("VEHICLE-CSV_WEIGHT")]);
        manufactureYear = Integer.parseInt(vehicleParametersBuffer[config.get("VEHICLE-CSV_YEAR")]);
        mileage = Integer.parseInt(vehicleParametersBuffer[config.get("VEHICLE-CSV_MILEAGE")]);
        color = CarColor.valueOf(vehicleParametersBuffer[config.get("VEHICLE-CSV_COLOR")].toUpperCase(Locale.ROOT));
        engineName = vehicleParametersBuffer[config.get("VEHICLE-CSV_ENGINE")];
        engine = parseEngine(engineName, vehicleParametersBuffer);
        rents = generateRentListById(id);

        return new Vehicle(id, vehicleType, color, model, registrationNumber, mass,
                mileage, manufactureYear, engine, rents);
    }

    private static VehicleType generateTypeById(int typeId) throws IOException {
        for (VehicleType vehicleType : loadTypes("types.csv")) {
            if (vehicleType.getId() == typeId) {
                return vehicleType;
            }
        }
        throw new RuntimeException("Type with such ID doesn't exist");
    }

    private static List<Rent> generateRentListById(int id) throws IOException, ParseException {
        List<Rent> rents = new ArrayList<>();

        for (Rent rent : loadRents("rents.csv")) {
            if (rent.getVehicleId() == id) {
                rents.add(rent);
            }
        }
        return rents;
    }

    private static Startable parseEngine(String name, String[] vehicleParametersBuffer) {
        if (name.equals("Gasoline")) {
            return new GasolineEngine(
                    Double.parseDouble(vehicleParametersBuffer[config.get("VEHICLE-CSV_VOLUME")]),
                    Double.parseDouble(vehicleParametersBuffer[config.get("VEHICLE-CSV_FUEL_CONS")]),
                    Double.parseDouble(vehicleParametersBuffer[config.get("VEHICLE-CSV_TANK_SIZE")]));
        } else if (name.equals("Diesel")) {
            return new DieselEngine(
                    Double.parseDouble(vehicleParametersBuffer[config.get("VEHICLE-CSV_VOLUME")]),
                    Double.parseDouble(vehicleParametersBuffer[config.get("VEHICLE-CSV_FUEL_CONS")]),
                    Double.parseDouble(vehicleParametersBuffer[config.get("VEHICLE-CSV_TANK_SIZE")])
            );
        } else if (name.equals("Electrical")) {
            return new ElectricalEngine(
                    Double.parseDouble(vehicleParametersBuffer[config.get("VEHICLE-CSV_EL_CONS")]),
                    Double.parseDouble(vehicleParametersBuffer[config.get("VEHICLE-CSV_BATTERY")])
            );
        } else {
            throw new RuntimeException("Such engine doesn't exist");
        }
    }

    private static List<String> readFromFile(File file) throws IOException {
        List<String> typesBuffer = new ArrayList<>();
        String string = "";

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while ((string = reader.readLine()) != null) {
                typesBuffer.add(string);
            }
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Error: cannot read the file", e);
        }
        return typesBuffer;
    }

    private static List<String> proceedStrings(List<String> vehicleBuffer) {
        Pattern pattern = Pattern.compile("(\")(\\d+)(,)(\\d+)(\")");
        int bufferSize = vehicleBuffer.size();

        for (int i = 0; i < bufferSize; i++) {
            Matcher matcher = pattern.matcher(vehicleBuffer.get(i));
            vehicleBuffer.remove(i);
            vehicleBuffer.add(i, matcher.replaceAll("$2.$4"));
        }
        return vehicleBuffer;
    }

    private static void initProperties(String configPropPath) throws IOException {
        try (FileReader fileReader =
                     new FileReader(configPropPath);
        ) {
            Properties properties = new Properties();
            properties.load(fileReader);

            for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                config.put((String) entry.getKey(), Integer.parseInt((String) entry.getValue()));
            }
        }
    }
}
