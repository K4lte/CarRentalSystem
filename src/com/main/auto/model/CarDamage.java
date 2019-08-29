package com.main.auto.model;

import java.math.BigDecimal;

public class CarDamage {
    private int id;
    private Car car;
    private DamageType damageType;
    private String info;
    private BigDecimal amount;
    private Payment payment;
    
    public CarDamage() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CarDamage that = (CarDamage) o;
        return this.car.equals(that.getCar()) &&
                this.damageType.equals(that.getDamageType()) &&
                info.equals(that.getInfo());
    }

    @Override
    public int hashCode() {
        int result = car.getId() * 37 + (damageType != null ? damageType.hashCode() : 0) - 5;
        result = result + (info != null ? info.hashCode() : 0) * 43 + payment.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "CarDamage{" +
                "id=" + id +
                ", car_number=" + car.getNumber() +
                ", damageType=" + damageType.getTypeName() +
                ", info='" + info + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

	public DamageType getDamageType() {
		return damageType;
	}

	public void setDamageType(DamageType damageType) {
		this.damageType = damageType;
	}

	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}
}
