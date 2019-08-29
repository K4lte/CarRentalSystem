package com.main.auto.model;

import java.math.BigDecimal;

public class AccessoryType {
    private int id;
    private String name;
    private BigDecimal price;

    public AccessoryType(){

    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        AccessoryType thatType = (AccessoryType) obj;
        return this.price.compareTo(thatType.getPrice()) == 0 &&
                this.name.equals(thatType.getName());
    }

    @Override
    public int hashCode() {
        int result = price.intValueExact() * 37;
        result = result + (name != null ? name.hashCode() : 0)*77;
        return result;
    }

    @Override
    public String toString() {
        return "AccessoryType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
