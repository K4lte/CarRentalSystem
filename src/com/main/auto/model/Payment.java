package com.main.auto.model;

import java.math.BigDecimal;

public class Payment {
    private int id;
    private CreditCardType creditCardType;
    private String creditCardNumber;
    private BigDecimal totalAmount;
    private String paymentStatus;
    private Reservation reservation;
    
    public Payment() {

    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Payment that = (Payment) obj;
        return this.creditCardType.equals(that.getCreditCardType()) &&
                this.creditCardNumber.equals(that.getCreditCardNumber()) &&
                this.totalAmount.compareTo(that.getTotalAmount()) == 0 &&
                this.reservation.equals(that.getReservation()) &&
                this.paymentStatus.equals(that.getPaymentStatus());
    }

    @Override
    public int hashCode() {
        int result = 9 * (creditCardType != null ? creditCardType.hashCode() : 0);
        result = 37 * result + (creditCardNumber != null ? creditCardNumber.hashCode() : 0);
        result = result + totalAmount.intValueExact();
        result = result * (reservation != null ? reservation.hashCode() : 0) + (paymentStatus != null ? paymentStatus.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", creditCardType=" + creditCardType +
                ", creditCardNumber=" + creditCardNumber +
                ", totalAmount=" + totalAmount +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", reservation=" + reservation.getId() +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CreditCardType getCreditCardType() {
        return creditCardType;
    }

    public void setCreditCardType(CreditCardType creditCardType) {
        this.creditCardType = creditCardType;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

/*	public CreditCardType getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(CreditCardType paymentType) {
		this.paymentType = paymentType;
	}   */ 
    
    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }
}
