package by.incubator.autopark.engine;

import by.incubator.autopark.vehicle.TechnicalSpecialist;

public class ElectricalEngine extends AbstractEngine {
    private double batterySize;
    private double electricityConsumption;

    public ElectricalEngine(double electricityConsumption, double batterySize) {
        super("Electrical", 0.1);
        setBatterySize(batterySize);
        setElectricityConsumption(electricityConsumption);
    }

    @Override
    public double getTaxPerMonth() {
        return this.engineTypeTaxCoefficient;
    }

    @Override
    public double getMaxKilometers() {
        return batterySize / electricityConsumption;
    }

    public double getBatterySize() {
        return batterySize;
    }

    public void setBatterySize(double batterySize) {
        if (TechnicalSpecialist.validateBatterySize(batterySize)) {
            this.batterySize = batterySize;
        } else {
            this.batterySize = 0.0d;
        }
    }

    public double getElectricityConsumption() {
        return electricityConsumption;
    }

    public void setElectricityConsumption(double electricityConsumption) {
        if (TechnicalSpecialist.validateElectricityConsumption(electricityConsumption)) {
            this.electricityConsumption = electricityConsumption;
        } else {
            this.electricityConsumption = 0.0d;
        }
    }

    @Override
    public String toString() {
        return super.toString() + ", "
                + getElectricityConsumption() + ", "
                + getBatterySize();
    }
}
