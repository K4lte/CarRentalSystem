package com.main.auto.model;

public class CarCategory {
    private int id;
    private String typeName;

    public CarCategory() {
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CarCategory thatCategory = (CarCategory) obj;
        return this.typeName.equals(thatCategory.getTypeName());
    }

    @Override
    public int hashCode() {
        int result = typeName != null ? typeName.hashCode() : 0;
        return result;
    }

    @Override
    public String toString() {
        return "CarCategory{" +
                "id=" + id +
                ", typeName='" + typeName + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
