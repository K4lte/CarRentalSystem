package com.main.auto.model;

import java.math.BigDecimal;

public class Accessory {
    private int id;
    private AccessoryType type;
    private BigDecimal cost;
    private int quantity;

    public Accessory() {

    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Accessory thatAccessory = (Accessory) obj;
        return this.type.equals(thatAccessory.getType()) &&
                this.cost.compareTo(thatAccessory.getCost()) == 0 &&
                this.quantity == thatAccessory.getQuantity();
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 37 * result + cost.intValueExact() * 3;
        result = result + quantity * 17;
        return result;
    }

    @Override
    public String toString() {
        return "Accessory{" +
                "id=" + id +
                ", type=" + type.getName() +
                ", cost=" + cost +
                ", quantity=" + quantity +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public AccessoryType getType() {
        return type;
    }

    public void setType(AccessoryType type) {
        this.type = type;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
