package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class User extends Main{
    private int id;
    private String name;
    private String surname;
    private boolean status;
    private String email;
    private String password;
    private ArrayList<User> users = new ArrayList<>();

    public User(int id, String name, String surname, boolean status, String email, String password) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.status = status;
        this.email = email;
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public boolean isStatus() {
        return status;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
    {

    }
    @Override
    public void deleteElement(int id) throws SQLException {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/library","postgres","keklolloh123");
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM public.\"user\" WHERE ID = ?");
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }catch (Exception e){
            System.out.println("User with such id does not exist: " + e.getMessage());
        }
    }
    @Override
    public void addElement() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter surname: ");
        String surname = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter status (true for reader, false for author): ");
        boolean status = scanner.nextBoolean();
        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/library", "postgres", "keklolloh123")) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO public.\"user\" (name, surname, status, email, password) VALUES (?, ?, ?, ?, ?)");
            stmt.setString(1, name);
            stmt.setString(2, surname);
            stmt.setString(5,password);
            stmt.setString(4, email);
            stmt.setBoolean(3, status);
            stmt.executeUpdate();
            System.out.println("User added successfully!");
        } catch (SQLException e) {
            System.err.println("Error adding user: " + e.getMessage());
        }
    }
    public boolean isValid(String email,String password,ArrayList<User> users){
        for(int i = 0;i<users.size();i++){
            if(email.equals(users.get(i).getEmail()) && password.equals(users.get(i).getPassword())){
                System.out.println("Welcome back!");
                return true;
            }
        }
        System.out.println("Incorrect data");
        return false;
    }



    @Override
    public String toString() {
        return "User" +
                " id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", status=" + (status ? "'reader'" : "'author'") +
                ", email='" + email + '\'' +
                ", password='" + password + "'";
    }
}


