package com.main.auto.model;

public class ReservationStatus {
	private int id;
	private String statusName;
	
	public ReservationStatus() {
		
	}
	
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ReservationStatus that = (ReservationStatus) obj;
        return this.statusName.equals(that.getStatusName());
    }

    @Override
    public int hashCode() {
    	int result = 13 * statusName.hashCode();
        result = result + 11 * id;
        return result;
    }

    @Override
    public String toString() {
        return "Confirmation{" +
                "id=" + id +
                ", status='" + statusName + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

}
