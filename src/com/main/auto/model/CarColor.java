package com.main.auto.model;

public class CarColor {
    private int id;
    private String colorName;

    public CarColor() {
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CarColor thatColor = (CarColor) obj;
        return this.colorName.equals(thatColor.getColorName());
    }

    @Override
    public int hashCode() {
        int result = colorName != null ? colorName.hashCode() : 0;
        return result;
    }

    @Override
    public String toString() {
        return "CarColor{" +
                "id=" + id +
                ", colorName='" + colorName + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }
}
