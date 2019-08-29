package com.main.auto.model;

public class EmployeeUser {
    private int id;
    private String firstName;
    private String lastName;
    private Permission permission;
    private Office office;
    private String login;
    private String password;

    public EmployeeUser() {

    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        EmployeeUser thatUser = (EmployeeUser) obj;
        return this.firstName.equals(thatUser.getFirstName()) &&
                this.lastName.equals(thatUser.getLastName()) &&
                this.login.equals(thatUser.getLogin()) &&
                this.password.equals(thatUser.getPassword());
    }

    @Override
    public int hashCode() {
        int result = 83 * (firstName != null ? firstName.hashCode() : 0) - 16;
        result = result + 83 * (lastName != null ? lastName.hashCode() : 0);
        result = result + 83 * (login != null ? login.hashCode() : 0);
        result = result * 19 + 37 * (password != null ? password.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "EmployeeUser{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", Office=" + office.getName() +
                ", permission='" + permission + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
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

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    public Office getOffice() {
        return office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
