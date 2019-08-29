package com.main.auto.model;

public class Office {
    private int id;
    private String name;
    private Location location;

    public Office() {

    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Office thatOffice = (Office) obj;
        return this.location.equals(thatOffice.getLocation()) &&
                this.name.equals(thatOffice.getName());
    }

    @Override
    public int hashCode() {
        int result = 37 * (location != null ? location.hashCode() : 0);
        result = result + 19 * (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Office{" +
                "id=" + id +
                ", location=" + location.getAddress() +
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
