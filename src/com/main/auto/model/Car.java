package com.main.auto.model;

import java.math.BigDecimal;

/**
 * Entity class for cars
 */

public class Car {

    //    Field for car's identifier in data base
    private int id;
    //    Field for Vehicle identification number (VIN)
    private String number;
    //    Field for car's categories such as: Microcar, getCityDAO car, Compact car, Supercar, etc.
    private CarCategory category;
    //    Field for car's brands
    private CarBrand brand;
    //    Field for car's model
    private String model;
    //    Field for car's year of manufacture
    private int productionYear;
    //    Field for car's color
    private CarColor color;
    //    Field for car's price per day BigDecimal
    private BigDecimal rentalPrice;
    //    Field for car's price BigDecimal
    private BigDecimal totalPrice;
    // 	  Field for total rental for the car
    private BigDecimal totalRental;
    
    
    //    Field for car's rate number 
    private int rateNumber;
    //    Field for car's type of transmission
    private CarTransmission transmission;
    //    Field for car's number of seats (passengers)
    private int numberOfSeats;
    //    VField for car's luggage capacity
    private int numberOfSuitcases;
    //    Field for presence of air conditioning in a car
    private boolean airConditioning;
    //    Field for current car's location
    private Office currentOffice;
    //    Field for default car's location
    private Office defaultOffice;

    public Car() {
    }

/*    public enum CarTransmission {
        MANUAL, AUTOMAT;
    }*/

    //  Equals compares cars only by VIN (Vehicle identification number)
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        Car thatCar = (Car) obj;
        return this.number.equals(thatCar.getNumber());
    }

    //    HashCode depends only on VIN (Vehicle identification number)
    @Override
    public int hashCode() {
        return number != null ? number.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", Category =" + category.getTypeName() +
                ", Brand =" + brand.getBrandName() +
                ", Production Year: " + productionYear +
                ", Color = " + color.getColorName() +
                ", Cost Price = " + rentalPrice +
                ", Default Location = " + defaultOffice.getName() +
                ", Current Location = " + currentOffice.getName() +
                ", Model: '" + model + '\'' +
                ", Transmission: '" + transmission.getName() + '\'' +
                ", Number Of Seats: " + numberOfSeats +
                ", Number Of Suitcases: " + numberOfSuitcases +
                ", Air Conditioning: " + airConditioning +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CarCategory getCategory() {
        return category;
    }

    public void setCategory(CarCategory category) {
        this.category = category;
    }

    public CarBrand getBrand() {
        return brand;
    }

    public void setBrand(CarBrand brand) {
        this.brand = brand;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getProductionYear() {
        return productionYear;
    }

    public void setProductionYear(int productionYear) {
        this.productionYear = productionYear;
    }

    public CarColor getColor() {
        return color;
    }

    public void setColor(CarColor color) {
        this.color = color;
    }

    public BigDecimal getRentalPrice() {
        return rentalPrice;
    }

    public void setRentalPrice(BigDecimal rentalPrice) {
        this.rentalPrice = rentalPrice;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public CarTransmission getTransmission() {
        return transmission;
    }

    public void setTransmission(CarTransmission transmission) {
        this.transmission = transmission;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public int getNumberOfSuitcases() {
        return numberOfSuitcases;
    }

    public void setNumberOfSuitcases(int numberOfSuitcases) {
        this.numberOfSuitcases = numberOfSuitcases;
    }

    public boolean getAirConditioning() {
        return airConditioning;
    }

    public void setAirConditioning(boolean airConditioning) {
        this.airConditioning = airConditioning;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getRateNumber() {
        return rateNumber;
    }

    public void setRateNumber(int rateNumber) {
        this.rateNumber = rateNumber;
    }

    public boolean isAirConditioning() {
        return airConditioning;
    }

    public Office getCurrentOffice() {
        return currentOffice;
    }

    public void setCurrentOffice(Office currentOffice) {
        this.currentOffice = currentOffice;
    }

    public Office getDefaultOffice() {
        return defaultOffice;
    }

    public void setDefaultOffice(Office defaultOffice) {
        this.defaultOffice = defaultOffice;
    }

	public BigDecimal getTotalRental() {
		return totalRental;
	}

	public void setTotalRental(BigDecimal totalRental) {
		this.totalRental = totalRental;
	}

}
