package com.main.auto.model;

public class Country {
    private int id;
    private String countryCode;
    private String countryName;

    public Country() {

    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Country thatCountry = (Country) obj;
        return this.countryCode.equals(thatCountry.getCountryCode()) &&
                this.countryName.equals(thatCountry.getCountryName());
    }

    @Override
    public int hashCode() {
        int result = 37 * (countryCode != null ? countryCode.hashCode() : 0);
        result = result + 57 * (countryName != null ? countryName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Country{" +
                "id=" + id +
                ", countryCode='" + countryCode + '\'' +
                ", countryName='" + countryName + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
}
