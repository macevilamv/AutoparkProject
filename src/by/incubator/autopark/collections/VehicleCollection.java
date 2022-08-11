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
    List<VehicleType> types;
    List<Vehicle> vehicles;
    List<Rent> rents;
    private static final Map<String, Integer> config = new HashMap<>();

    static {
        try {
            initProperties("src/by/incubator/autopark/collections/csvParsingProperties/csv-config.properties");
        } catch (IOException e) {
            throw new RuntimeException("Error: \"csv-config.properties\" file reading has failed.");
        }
    }

    public VehicleCollection(String types, String vehicles, String rents) {
        try {
            this.types = loadTypes(types);
            this.rents = loadRents(rents);
            this.vehicles = loadVehicles(vehicles);
        } catch (IOException | ParseException e) {
            throw new IllegalArgumentException("Error: incorrect path to .csv file.");
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
        int maxIdLength = 0;
        int maxTypeLength = 0;
        int maxModelLength = 0;
        int maxColorLength = 0;
        int maxWeightLength = 0;
        int maxMileageLength = 0;
        int maxNumberLength = 0;
        int maxTaxLength = 0;
        int maxYearLength = 0;
        int maxIncomeLength = 0;
        int maxProfitLength = 0;
        String pattern;

        for (Vehicle vehicle : this.vehicles) {
            if (maxIdLength < ((Object) vehicle.getId()).toString().length()) {
                maxIdLength = ((Object) vehicle.getId()).toString().length();

                if (maxIdLength < FieldsToRender.ID.name().length())
                    maxIdLength = FieldsToRender.ID.name().length();
            }

            if (maxTypeLength < vehicle.getType().getTypeName().length()) {
                maxTypeLength = vehicle.getType().getTypeName().length();

                if (maxTypeLength < FieldsToRender.Type.name().length())
                    maxTypeLength = FieldsToRender.Type.name().length();
            }

            if (maxModelLength < vehicle.getModel().length()) {
                maxModelLength = vehicle.getModel().length();

                if (maxModelLength < FieldsToRender.Model.name().length())
                    maxModelLength = FieldsToRender.Model.name().length();
            }


            if (maxColorLength < vehicle.getColor().name().length()) {
                maxColorLength = vehicle.getColor().name().length();

                if (maxColorLength < FieldsToRender.Color.name().length())
                    maxColorLength = FieldsToRender.Color.name().length();
            }

            if (maxWeightLength < ((Object) vehicle.getMass()).toString().length()) {
                maxWeightLength = ((Object) vehicle.getMass()).toString().length();

                if (maxWeightLength < FieldsToRender.Weight.name().length())
                    maxWeightLength = FieldsToRender.Weight.name().length();
            }
            if (maxMileageLength < ((Object) vehicle.getMileage()).toString().length()) {
                maxMileageLength = ((Object) vehicle.getMileage()).toString().length();

                if (maxMileageLength < FieldsToRender.Mileage.name().length())
                    maxMileageLength = FieldsToRender.Mileage.name().length();
            }
            if (maxNumberLength < vehicle.getRegistrationNumber().length()) {
                maxNumberLength = vehicle.getRegistrationNumber().length();
            }
            if (maxTaxLength < ((Object) vehicle.getCalcTaxPerMonth()).toString().length()) {
                maxTaxLength = ((Object) vehicle.getCalcTaxPerMonth()).toString().length();

                if (maxTaxLength < FieldsToRender.Tax.name().length())
                    maxTaxLength = FieldsToRender.Tax.name().length();
            }
            if (maxYearLength < ((Object) vehicle.getManufactureYear()).toString().length()) {
                maxYearLength = ((Object) vehicle.getManufactureYear()).toString().length();
            }

            if (maxIncomeLength < ((Object) vehicle.getTotalIncome()).toString().length()) {
                maxIncomeLength = ((Object) vehicle.getTotalIncome()).toString().length();

                if (maxIncomeLength < FieldsToRender.Income.name().length())
                    maxIncomeLength = FieldsToRender.Income.name().length();
            }

            if (maxProfitLength < ((Object) vehicle.getTotalProfit()).toString().length()) {
                maxProfitLength = ((Object) vehicle.getTotalProfit()).toString().length();

                if (maxIncomeLength < FieldsToRender.Profit.name().length())
                    maxIncomeLength = FieldsToRender.Profit.name().length();
            }
        }

        pattern = "| %" + maxIdLength + "s | %" + maxTypeLength + "s | %" + maxModelLength + "s" +
                " | %" + maxNumberLength + "s | %" + maxWeightLength + "s | %" + maxYearLength + "s | %"
                + maxMileageLength + "s | %" + maxColorLength + "s | %" + maxIncomeLength + "s | %"
                + maxTaxLength + "s | %" + maxProfitLength + "s | \n";

        System.out.printf(pattern,
                FieldsToRender.ID, FieldsToRender.Type, FieldsToRender.Model, FieldsToRender.Number,
                FieldsToRender.Weight, FieldsToRender.Year, FieldsToRender.Mileage,
                FieldsToRender.Color, FieldsToRender.Income, FieldsToRender.Tax, FieldsToRender.Profit
        );

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

    private static List<VehicleType> loadTypes(String inFile) throws IOException {
        List<VehicleType> typesList = new ArrayList<>();
        String path = "resources/csv/" + inFile;
        File csvFile = new File(path);
        List<String> typesBuffer = proceedStrings(readFromFile(csvFile));

        for (String csvString : typesBuffer) {
            typesList.add(createType(csvString));
        }
        return typesList;
    }

    private static List<Rent> loadRents(String inFile) throws IOException, ParseException {
        List<Rent> rentList = new ArrayList<>();
        String path = "resources/csv/" + inFile;
        File csvFile = new File(path);
        List<String> rentsBuffer = proceedStrings(readFromFile(csvFile));

        for (String csvString : rentsBuffer) {
            rentList.add(createRent(csvString));
        }
        return rentList;
    }

    private static List<Vehicle> loadVehicles(String inFile) throws IOException, ParseException {
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
