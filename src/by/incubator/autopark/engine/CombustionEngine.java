package by.incubator.autopark.engine;

import by.incubator.autopark.vehicle.TechnicalSpecialist;

public class CombustionEngine extends AbstractEngine {
    private double engineVolume;
    private double fuelTankCapacity;
    private double fuelConsumptionPer100;

    public CombustionEngine(String engineName, double engineTypeTaxCoefficient, double engineVolume,
                            double fuelConsumptionPer100, double fuelTankCapacity) {
        super(engineName, engineTypeTaxCoefficient);
        setEngineVolume(engineVolume);
        setFuelTankCapacity(fuelTankCapacity);
        setFuelConsumptionPer100(fuelConsumptionPer100);
    }

    @Override
    public double getTaxPerMonth() {
        return this.engineTypeTaxCoefficient;
    }

    @Override
    public double getMaxKilometers() {
        return ((fuelTankCapacity * 100) / fuelConsumptionPer100);
    }

    public double getEngineVolume() {
        return engineVolume;
    }

    public void setEngineVolume(double engineVolume) {
        if (TechnicalSpecialist.validateEngineVolume(engineVolume)) {
            this.engineVolume = engineVolume;
        } else {
            this.engineVolume = 0.0d;
        }
    }

    public double getFuelTankCapacity() {
        return fuelTankCapacity;
    }

    public void setFuelTankCapacity(double fuelTankCapacity) {
        if (TechnicalSpecialist.validateFuelTankCapacity(fuelTankCapacity)) {
            this.fuelTankCapacity = fuelTankCapacity;
        } else {
            this.fuelTankCapacity = 0.0d;
        }
    }

    public double getFuelConsumptionPer100() {
        return fuelConsumptionPer100;
    }

    public void setFuelConsumptionPer100(double fuelConsumptionPer100) {
        if (TechnicalSpecialist.validateFuelConsumption(fuelConsumptionPer100)) {
            this.fuelConsumptionPer100 = fuelConsumptionPer100;
        } else {
            this.fuelConsumptionPer100 = 0.0d;
        }
    }

    @Override
    public String toString() {
        return super.toString() + ", "
                + getEngineVolume() + ", "
                + getFuelTankCapacity() + ", "
                + getFuelConsumptionPer100();
    }
}
