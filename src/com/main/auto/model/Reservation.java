package com.main.auto.model;

import java.sql.Date;

public class Reservation {
    private int id;
    private String uniqueNumber;
    private Car car;
    private Client client;
    private Date pickUpDate;
    private Date returnDate;
    private Office pickUpLocation;
    private Office returnLocation;
    private ReservationStatus status;
    private String note;

    public Reservation() {

    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Reservation that = (Reservation) obj;
        return id == that.id &&
                this.car.equals(that.getCar()) &&
                this.client.equals(that.getClient()) &&
                this.pickUpDate.equals(that.getPickUpDate()) &&
                this.returnDate.equals(that.getReturnDate()) &&
                this.pickUpLocation.equals(that.getPickUpLocation()) &&
                this.returnLocation.equals(that.getReturnLocation()) &&
                this.status.equals(that.getStatus()) &&
                this.uniqueNumber.equals(that.getUniqueNumber()) &&
                this.note.equals(that.getNote());
    }

    @Override
    public int hashCode() {
        int result = (car != null ? car.hashCode() : 0) * 15;
        result = result + (client != null ? client.hashCode() : 0) * 15;
        result = result + (pickUpDate != null ? pickUpDate.hashCode() : 0) - 99;
        result = result + (returnDate != null ? returnDate.hashCode() : 0) * 31;
        result = result + (pickUpLocation != null ? pickUpLocation.hashCode() : 0) * 17;
        result = result + (returnLocation != null ? returnLocation.hashCode() : 0);
        result = result + (status != null ? status.hashCode() : 0) * 77;
        result = result + (note != null ? note.hashCode() : 0) * 11 - 95;
        result = result + (uniqueNumber != null ? uniqueNumber.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", uniqueNumber='" + uniqueNumber + '\'' +
                ", car=" + car.getNumber() +
                ", client=" + client.getLastName() + " " + client.getFirstName() +
                ", pickUpDate=" + pickUpDate +
                ", returnDate=" + returnDate +
                ", pickUpLocation=" + pickUpLocation.getName() +
                ", returnLocation=" + returnLocation.getName() +
                ", status=" + status +
                ", note='" + note + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUniqueNumber() {
        return uniqueNumber;
    }

    public void setUniqueNumber(String uniqueNumber) {
        this.uniqueNumber = uniqueNumber;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Date getPickUpDate() {
        return pickUpDate;
    }

    public void setPickUpDate(Date pickUpDate) {
        this.pickUpDate = pickUpDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public Office getPickUpLocation() {
        return pickUpLocation;
    }

    public void setPickUpLocation(Office pickUpLocation) {
        this.pickUpLocation = pickUpLocation;
    }

    public Office getReturnLocation() {
        return returnLocation;
    }

    public void setReturnLocation(Office returnLocation) {
        this.returnLocation = returnLocation;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
