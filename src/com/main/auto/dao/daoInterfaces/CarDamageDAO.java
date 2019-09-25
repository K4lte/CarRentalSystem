package com.main.auto.dao.daoInterfaces;

import com.main.auto.model.CarDamage;

public interface CarDamageDAO extends DAO<CarDamage> {
	
	CarDamage getDamagedWithoutPayment(int reservationId);
}
