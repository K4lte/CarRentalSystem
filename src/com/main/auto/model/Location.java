package com.main.auto.model;

public class Location {
    private int id;
    private String address;
    private City city;
    private String zip;

    public Location() {

    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Location thatLocation = (Location) obj;
        return this.city.equals(thatLocation.getCity()) &&
                this.address.equals(thatLocation.getAddress()) &&
                this.zip.equals(thatLocation.getZip());
    }

    @Override
    public int hashCode() {
        int result = (city != null ? city.hashCode() : 0) * (zip != null ? zip.hashCode() : 0);
        result = result + 37 * (address != null ? address.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Location{" +
                "id=" + id +
                ", address='" + address + '\'' +
                ", city=" + city.getCityName() +
                ", zip='" + zip + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
}
