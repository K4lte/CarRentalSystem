package com.main.auto.model;

public class Permission {
	private int id;
 //   private String permission;
    private String role;

    public Permission(){

    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Permission that = (Permission) obj;
        return this.role.equals(that.getRole());
    }

    @Override
    public int hashCode() {
        return role.hashCode();
    }

    @Override
    public String toString() {
        return "Permission{" +
                "id=" + id +
                ", role='" + role + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

}
