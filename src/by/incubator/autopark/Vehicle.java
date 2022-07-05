package by.incubator.autopark;

import java.util.Objects;

public class Vehicle implements Comparable<Vehicle> {
    private VehicleType type;
    private CarColor color;
    private String model;
    private String registrationNumber;
    private double mass;
    private int mileage;
    private int manufactureYear;

    public Vehicle() {
        type = new VehicleType("DEFAULT", 0);
        model = "DEFAULT";
        registrationNumber = "DEFAULT";
        manufactureYear = 0;
        color = CarColor.WHITE;
        registrationNumber = "DEFAULT";
        mass = 0.0;
    }

    public Vehicle(VehicleType type, CarColor color, String model, String registrationNumber, double mass,
                   int mileage, int manufactureYear) {
        setType(type);
        setColor(color);
        setModel(model);
        setRegistrationNumber(registrationNumber);
        setMass(mass);
        setMileage(mileage);
        setManufactureYear(manufactureYear);
    }

    public double getCalcTaxPerMonth() {
        return (this.mass * 0.0013) + (type.getTaxCoefficient() * 30) + 5;
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
                + getCalcTaxPerMonth();
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

    public VehicleType getType() {
        return type;
    }

    public void setType(VehicleType type) {
        this.type = type;
    }

    public CarColor getColor() {
        return color;
    }

    public void setColor(CarColor color) {
        this.color = color;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public int getManufactureYear() {
        return manufactureYear;
    }

    public void setManufactureYear(int manufactureYear) {
        this.manufactureYear = manufactureYear;
    }
}
