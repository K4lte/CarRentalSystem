package com.main.auto.model;

public class ReservationStatusCombs {
	private int id;
	private ReservationStatus status;
	private ReservationStatus statusMatchWith;

	public ReservationStatusCombs() {
		
	}


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ReservationStatusCombs thatObject = (ReservationStatusCombs) obj;
        return this.status.equals(thatObject.getStatus()) &&
        		this.statusMatchWith.equals(thatObject.getStatusMatchWith());
    }

    @Override
    public int hashCode() {
        int result = 17 * (status != null ? status.hashCode() : 0) + (statusMatchWith != null ? statusMatchWith.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ReservationStatusCombs{" +
                "id=" + id +
                ", statusId='" + status.getId() + '\'' +
                ", statusIdMatchWith=" + statusMatchWith.getId() +
                '}';
    }
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ReservationStatus getStatus() {
		return status;
	}

	public void setStatus(ReservationStatus status) {
		this.status = status;
	}
	
	public ReservationStatus getStatusMatchWith() {
		return statusMatchWith;
	}

	public void setStatusMatchWith(ReservationStatus statusMatchWith) {
		this.statusMatchWith = statusMatchWith;
	}
	
}
