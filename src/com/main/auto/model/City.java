package com.main.auto.model;

public class City {
    private int id;
    private String cityName;
    private Country country;

    public City() {
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        City thatCity = (City) obj;
        return this.country.equals(thatCity.getCountry()) &&
                this.cityName.equals(thatCity.getCityName());
    }

    @Override
    public int hashCode() {
        int result = (country != null ? cityName.hashCode() : 0) * 83;
        result = result + (cityName != null ? cityName.hashCode() : 0)*17;
        return result;
    }

    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", cityName='" + cityName + '\'' +
                ", country=" + country.getCountryName() +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }
}
