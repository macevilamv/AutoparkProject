package by.incubator.autopark.vehicle;

import java.util.Objects;
import by.incubator.autopark.engine.*;

public class Vehicle implements Comparable<Vehicle> {
    private VehicleType type;
    private CarColor color;
    private String model;
    private String registrationNumber;
    private double mass;
    private int mileage;
    private int manufactureYear;
    private Startable engine;

    public Vehicle() {
        type = new VehicleType("DEFAULT", 0);
        model = "DEFAULT";
        registrationNumber = "DEFAULT";
        manufactureYear = 0;
        color = CarColor.WHITE;
        registrationNumber = "DEFAULT";
        mass = 0.0;
        engine = null;
    }

    public Vehicle(VehicleType type, CarColor color, String model, String registrationNumber, double mass,
                   int mileage, int manufactureYear, Startable engine) {
        setType(type);
        setColor(color);
        setModel(model);
        setRegistrationNumber(registrationNumber);
        setMass(mass);
        setMileage(mileage);
        setManufactureYear(manufactureYear);
        setEngine(engine);
    }

    public String getCalcTaxPerMonth() {
        return String.format("%.2f",
                (this.mass * 0.0013) + (type.getTaxCoefficient() * engine.getTaxPerMonth() * 30) + 5);
    }

    @Override
    public String toString() {
        return type.getTypeName() + ", "
                + model + ", "
                + color + ", "
                + registrationNumber + ", "
                + mass + ", "
                + mileage + ", "
                + manufactureYear + ", "
                + getCalcTaxPerMonth() + ", "
                + engine.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Vehicle that = (Vehicle) o;

        return this.type.equals(that.type) && this.model.equals(that.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, model);
    }

    @Override
    public int compareTo(Vehicle secondVehicle) {
        if (this.manufactureYear == secondVehicle.manufactureYear) {
            return Integer.compare(this.mileage, secondVehicle.mileage);
        } else if (this.manufactureYear > secondVehicle.manufactureYear) {
            return 1;
        } else {
            return -1;
        }
    }

    public Startable getEngine() {
        return engine;
    }

    public void setEngine(Startable engine) {
        this.engine = engine;
    }

    public VehicleType getType() {
        return type;
    }

    public void setType(VehicleType type) {
        if (TechnicalSpecialist.validateVehicleType(type)) {
            this.type = type;
        } else {
            this.type = new VehicleType("default", 0);
        }
    }

    public CarColor getColor() {
        return color;
    }

    public void setColor(CarColor color) {
        if (TechnicalSpecialist.validateColor(color)) {
            this.color = color;
        } else {
            this.color = CarColor.WHITE;
        }
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        if (TechnicalSpecialist.validateModelName(model)) {
            this.model = model;
        } else {
            this.model = "DEFAULT";
        }
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        if (TechnicalSpecialist.validateRegistrationNumber(registrationNumber)) {
            this.registrationNumber = registrationNumber;
        } else {
            this.registrationNumber = "DEFAULT";
        }
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        if (TechnicalSpecialist.validateWeight(mass)) {
            this.mass = mass;
        } else {
            this.mass = 0.0;
        }
    }

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        if (TechnicalSpecialist.validateMileage(mileage)) {
            this.mileage = mileage;
        } else {
            this.mileage = 0;
        }
    }

    public int getManufactureYear() {
        return manufactureYear;
    }

    public void setManufactureYear(int manufactureYear) {
        if (TechnicalSpecialist.validateManufactureYear(manufactureYear)) {
            this.manufactureYear = manufactureYear;
        } else {
            this.manufactureYear = 0;
        }
    }
}
