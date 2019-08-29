package com.main.auto.model;

public class DamageType {
    private int id;
    private String typeName;
    private int percentage;

    public DamageType(){

    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        DamageType thatType = (DamageType) obj;
        return percentage == thatType.getPercentage() &&
               this.typeName.equals(thatType.getTypeName());
    }

    @Override
    public int hashCode() {
        int result = 77 * percentage;
        result = result + 89 * (typeName != null ? typeName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DamageType{" +
                "id=" + id +
                ", typeName='" + typeName + '\'' +
                ", percentage=" + percentage +
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

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }
}
