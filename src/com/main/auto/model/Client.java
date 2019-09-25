package com.main.auto.model;

import java.sql.Date;

/**
 * Entity class for clients
 */

public class Client {
    //    Field for client's identifier in data base
    private int id;
    //    Field for client's First Name
    private String firstName;
    //    Field for client's Last Name
    private String lastName;
    //    Fields for client's passport number
    private String passportNumber;
    //    and driver's license number
    private String driverLicenseNumber;
    //    Field for client's birth date
    private Date birthDate;
    //    Field for client's phone number
    private String phoneNumber;
    //    Field for client's email
    private String email;
 //   private String login;
    private String password;
    private Permission permission;

    public Client() {

    }

    //    Equals compares clients by their passport number, First name and Last Name
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        Client thatClient = (Client) obj;
        return this.passportNumber.equals(thatClient.getPassportNumber()) &&
                this.firstName.equals(thatClient.getFirstName()) &&
                this.lastName.equals(thatClient.getPassportNumber());
    }

    //    HashCode depends on client's passport number, First Name and Last Name
    @Override
    public int hashCode() {
        int result = (passportNumber != null ? passportNumber.hashCode() : 0);
        result = 37 * result + (firstName != null ? firstName.hashCode() : 0);
        result = result + (lastName != null ? lastName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id = " + id +
                ", First Name: '" + firstName + '\'' +
                ", Last Name: '" + lastName + '\'' +
                ", Passport Number: '" + passportNumber + '\'' +
                ", Driver License Number: '" + driverLicenseNumber + '\'' +
                ", Birth Date: " + birthDate +
                ", Phone Number: '" + phoneNumber + '\'' +
                ", email Address: '" + email + '\'' +
  //              ", login: '" + login + '\'' +
                ", password: '" + password + '\'' +
                ", permission: '" + permission + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getDriverLicenseNumber() {
        return driverLicenseNumber;
    }

    public void setDriverLicenseNumber(String driverLicenseNumber) {
        this.driverLicenseNumber = driverLicenseNumber;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

 /*   public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }*/

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

	public Permission getPermission() {
		return permission;
	}

	public void setPermission(Permission permission) {
		this.permission = permission;
	}
}
