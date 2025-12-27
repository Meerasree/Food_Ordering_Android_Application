package com.example.cravz.Domain;

import java.io.Serializable;

public class SelectedAddress implements Serializable {

    private String id;
    private String name;
    private String address;
    private String city;
    private String postal;
    private String phone;
    private boolean selected;

    public SelectedAddress() {
        // required for Firebase
    }

    public SelectedAddress(String name, String address, String city, String postal, String phone) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.postal = postal;
        this.phone = phone;
        this.selected = false;
    }

    // ---------------- ID ----------------
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // ---------------- GETTERS & SETTERS ----------------

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostal() {
        return postal;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    // Optional helper
    public String getFullAddress() {
        return name + ", " + address + ", " + city + ", " + postal + ", " + phone;
    }
}
