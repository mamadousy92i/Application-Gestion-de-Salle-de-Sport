package com.example.salledesport.model;

public class UserUpdateRequest {
    private String email;
    private String first_name;
    private String last_name;
    private String phone_number;
    private Double taille;
    private Double poids;
    private String gender;
    private String objectif;

    public UserUpdateRequest() {
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public void setTaille(Double taille) {
        this.taille = taille;
    }

    public void setPoids(Double poids) {
        this.poids = poids;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public void setObjectif(String objectif) {
        this.objectif = objectif;
    }

}
