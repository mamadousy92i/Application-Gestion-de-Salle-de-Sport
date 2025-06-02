package com.example.salledesport.model;

import java.util.List;

public class Utilisateur {
    private int id;
    private String email;
    private String username;
    private String first_name;
    private String last_name;
    private String phone_number;
    private String address;
    private String date_of_birth;
    private String profile_picture;
    private String password;
    private String role;
    private String gender;
    private int age;
    private double poids;
    private double taille;
    private List<Goal> goals;

    public List<Goal> getGoals() {
        return goals;
    }
    // Constructeur
    public Utilisateur( String email, String username, String first_name, String last_name,
                        String phone_number, String address, String date_of_birth,String password,
                       String profile_picture,String gender) {
        this.email = email;
        this.username = username;
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone_number = phone_number;
        this.address = address;
        this.date_of_birth = date_of_birth;
        this.password=password;
        this.profile_picture = profile_picture;
        this.gender=gender;
    }

    public Utilisateur(String email, String username, String first_name, String last_name,
                        String phone_number, String address, String date_of_birth,
                       String password, String profile_picture, int age, double poids, double taille) {
        this.email = email;
        this.username = username;
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone_number = phone_number;
        this.address = address;
        this.date_of_birth = date_of_birth;
        this.password = password;
        this.profile_picture = profile_picture;
        this.age = age;
        this.poids = poids;
        this.taille = taille;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getPoids() {
        return poids;
    }

    public void setPoids(double poids) {
        this.poids = poids;
    }

    public double getTaille() {
        return taille;
    }

    public void setTaille(double taille) {
        this.taille = taille;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
}


