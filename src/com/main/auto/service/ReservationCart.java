package com.main.auto.service;

import java.math.BigDecimal;
import java.sql.Date;

import com.main.auto.model.Car;
import com.main.auto.model.CarDamage;
import com.main.auto.model.City;
import com.main.auto.model.Client;
import com.main.auto.model.Office;
import com.main.auto.model.Reservation;

public class ReservationCart {
	private Client client;
	private Car car;
	private City cityPickUp;
	private City cityDropOff;

	private Office locationPickUp;
	private Office locationDropOff;
	private Date datePickUp;
	private Date dateDropOff;
	private int period;
	private BigDecimal totalRental;
	private Reservation reservation;
	private CarDamage damage;
	
	public ReservationCart() {		
	}
	
	@Override
	public String toString() {
	//	{"id":1,"cityName":"Zhangjiafang","country":{"id":2,"countryCode":"CN","countryName":"China"}}
		return "ReservationCart [client=" + client + ", car=" + car + ", locationPickUp=" + locationPickUp
				+ ", locationDropOff=" + locationDropOff + ", datePickUp=" + datePickUp + ", dateDropOff=" + dateDropOff
				+ "]";
	}
	
	public BigDecimal getPrice() {
		BigDecimal price = new BigDecimal(0);

		if (damage!=null && reservation!=null) {    	
			price = damage.getAmount();
		    } else {
		    	if (car!= null) {    	
		    		price = car.getTotalRental();
		    	}
		    }		   
		return price;
	}
	
	// getters & setters
	public Client getClient() {
		return client;
	}
	public void setClient(Client client) {
		this.client = client;
	}
	public Car getCar() {
		return car;
	}
	public void setCar(Car car) {
		this.car = car;
	}
	public Office getLocationPickUp() {
		return locationPickUp;
	}
	public void setLocationPickUp(Office locationPickUp) {
		this.locationPickUp = locationPickUp;
	}
	public Office getLocationDropOff() {
		return locationDropOff;
	}
	public void setLocationDropOff(Office locationDropOff) {
		this.locationDropOff = locationDropOff;
	}
	public Date getDatePickUp() {
		return datePickUp;
	}
	public void setDatePickUp(Date datePickUp) {
		this.datePickUp = datePickUp;
	}
	public Date getDateDropOff() {
		return dateDropOff;
	}
	public void setDateDropOff(Date dateDropOff) {
		this.dateDropOff = dateDropOff;
	}

	public BigDecimal getTotalRental() {
		return totalRental;
	}

	public void setTotalRental(BigDecimal totalRental) {
		this.totalRental = totalRental;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public City getCityDropOff() {
		return cityDropOff;
	}

	public void setCityDropOff(City city) {
		this.cityDropOff = city;
	}

	public City getCityPickUp() {
		return cityPickUp;
	}

	public void setCityPickUp(City cityPickUp) {
		this.cityPickUp = cityPickUp;
	}

	public Reservation getReservation() {
		return reservation;
	}

	public void setReservation(Reservation reservation) {
		this.reservation = reservation;
	}

	public CarDamage getDamage() {
		return damage;
	}

	public void setDamage(CarDamage damage) {
		this.damage = damage;
	}
}
