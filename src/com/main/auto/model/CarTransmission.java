package com.main.auto.model;

public class CarTransmission {
    private int id;
    private String name;

    public CarTransmission(){

    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CarTransmission that = (CarTransmission) obj;
        return this.name.equals(that.getName());
    }

    @Override
    public int hashCode() {
    	int result = 3 * name.hashCode();
        result = result + 17 * id;
        return result;
    }

    @Override
    public String toString() {
        return "CarTransmission{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
