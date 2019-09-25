package com.main.auto.dao.daoInterfaces;

import java.util.List;

import com.main.auto.model.ReservationStatus;

public interface ReservationStatusDAO extends DAO<ReservationStatus> {
	
	ReservationStatus getByStatus(String status);
	
	List<ReservationStatus> getStatusMatchedById(int statusId);

}
