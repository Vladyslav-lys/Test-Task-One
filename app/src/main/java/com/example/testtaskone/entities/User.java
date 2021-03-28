package com.example.testtaskone.entities;

import android.graphics.Bitmap;

public class User {
    private long id;
    private Bitmap picture;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    public User(long id, Bitmap picture, String firstName, String lastName, String email, String phone) {
        this.id = id;
        this.picture = picture;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Bitmap getPhoto() {
        return picture;
    }

    public void setPhoto(Bitmap picture) {
        this.picture = picture;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
