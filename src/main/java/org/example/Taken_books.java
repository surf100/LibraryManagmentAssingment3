//package
package org.example;

import java.sql.*;
import java.util.ArrayList;

public class Taken_books {
    private int id;
    private int number;
    private String book_name;
    private String user_name;
    private int user_id;
    private ArrayList<Taken_books> takenBooks = new ArrayList<>();

    public Taken_books(int id, int number, String book_name, String user_name, int user_id){
        this.id = id;
        this.number = number;
        this.book_name = book_name;
        this.user_name = user_name;
        this.user_id = user_id;
    }

    public int getId() {
        return id;
    }

    public int getNumber() {
        return number;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getBook_name() {
        return book_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }

    public void setTakenBooks(ArrayList<Taken_books> takenBooks) {
        this.takenBooks = takenBooks;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }


    public static void ShowTaken_books() throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/library", "postgres", "keklolloh123");
        String sql = "SELECT * FROM public.taken_books " + "ORDER BY id DESC ";
        Statement stat = con.createStatement();
        ResultSet rs = stat.executeQuery(sql);
        ArrayList<Taken_books> takenBooks = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt("id");
            int number = rs.getInt("number");
            String book_name = rs.getString("book_name");
            String user_name = rs.getString("user_name");
            int user_id = rs.getInt("user_id");
            Taken_books books = new Taken_books(id, number, book_name, user_name, user_id);
            takenBooks.add(books);
        }
        for (Taken_books boook : takenBooks) {
            System.out.println(boook);
        }
    }

    @Override
    public String toString() {
        return "↪\uFE0E ID " + id + " number: " + number + " || book_name: " + book_name + " || user_name: " + user_name + " || user_id: " + user_id;
    }
}
