package org.example;

import lombok.AllArgsConstructor;

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
    private float balance;
    private ArrayList<User> users = new ArrayList<>();

    public User(int id, String name, String surname, boolean status, String email, String password,float balance) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.status = status;
        this.email = email;
        this.password = password;
        this.balance = balance;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
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

        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        while(!name.matches("[a-zA-Z]+\\.?")){
            System.out.println("It should only contain letters,enter your name again: ");
            name = scanner.nextLine();
        }

        System.out.print("Enter your surname: ");
        String surname = scanner.nextLine();
        while(!surname.matches("[a-zA-Z]+\\.?")){
            System.out.println("It should only contain letters,enter your surname again: ");
            surname = scanner.nextLine();
        }

        System.out.print("Enter your email: ");
        String email = scanner.nextLine();
        while(!email.matches("\\b[\\w.%-]+@[-.\\w]+\\.[A-Za-z]{2,4}\\b")){
            System.out.println("Wrong email,enter your email again: ");
            email = scanner.nextLine();
        }

        System.out.print("Enter your password: ");
        String password = scanner.nextLine();
        while(!password.matches("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}")){
            System.out.println("Wrong password,password should consist of:\n digit,lower and upper case and at least 8 symbols");
            System.out.println("Try again:");
            password = scanner.nextLine();
        }

        System.out.print("Enter status reader or author: ");
        String statuss = scanner.nextLine();
        boolean status;

        while (true) {
            if (statuss.equalsIgnoreCase("reader") || statuss.equalsIgnoreCase("author")) {
                status = Boolean.parseBoolean(statuss);
                break;
            } else {
                System.out.println("Wrong status, try again:");
                statuss = scanner.nextLine();
            }
        }

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
