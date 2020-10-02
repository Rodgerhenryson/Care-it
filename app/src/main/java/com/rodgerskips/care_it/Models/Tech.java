package com.rodgerskips.care_it.Models;

/**
 * Created by Kiduyu klaus
 * on 30/09/2020 18:38 2020
 */
public class Tech {

    String name,email,phone,description,skills,status;

    public Tech(String name, String email, String phone, String description, String skills, String status) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.description = description;
        this.skills = skills;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
