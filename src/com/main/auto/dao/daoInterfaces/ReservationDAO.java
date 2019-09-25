package com.main.auto.dao.daoInterfaces;

import java.util.List;

import com.main.auto.model.Reservation;

public interface ReservationDAO extends DAO<Reservation> {
	
	List<Reservation> getByStatus(String status);
	
	List<Reservation> getByStatusId(int statusId);
	
	List<Reservation> getByClientId(int clientId);
	
	List<Reservation> getByClientIdStatus(int clientId, String status);
	
	boolean changeStatus(int statusId, int reservationId);
}
