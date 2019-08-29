package com.main.auto.model;

public class CarBrand {
    private int id;
    private String brandName;

    public CarBrand() {
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CarBrand thatBrand = (CarBrand) obj;
        return this.brandName.equals(thatBrand.getBrandName());
    }

    @Override
    public int hashCode() {
        int result = brandName != null ? brandName.hashCode() : 0;
        return result;
    }

    @Override
    public String toString() {
        return "CarBrand{" +
                "id=" + id +
                ", brandName='" + brandName + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
}
