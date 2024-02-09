package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Book extends Main{
    private int number;
    private String name;
    private int year_of_publication;
    private String type;
    private ArrayList<Book> books = new ArrayList<>();

    public Book(int number, String name, int year_of_publication, String type) {
        this.number = number;
        this.name = name;
        this.year_of_publication = year_of_publication;
        this.type = type;
        this.books = books;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setYear_of_publication(int year_of_publication) {
        this.year_of_publication = year_of_publication;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public int getYear_of_publication() {
        return year_of_publication;
    }

    public String getType() {
        return type;
    }
    @Override
    public void deleteElement(int number) throws SQLException {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/library","postgres","keklolloh123");
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM public.\"user\" WHERE ID = ?");
            stmt.setInt(1, number);
            stmt.executeUpdate();
        }catch (Exception e){
            System.out.println("Book with such number does not exist: " + e.getMessage());
        }
    }
    @Override
    public void addElement() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter name of the book: ");
        String name = scanner.nextLine();
        System.out.print("Enter type: ");
        String type = scanner.nextLine();
        System.out.print("Enter year of publication: ");
        int year_of_publication = scanner.nextInt();
        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/library", "postgres", "keklolloh123")) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO public.\"books\" (name, year_of_publication, type) VALUES (?, ?, ?)");
            stmt.setString(1, name);
            stmt.setInt(2, year_of_publication);
            stmt.setString(3,type);
            stmt.executeUpdate();
            System.out.println("Book added successfully!");
        } catch (SQLException e) {
            System.err.println("Error adding book: " + e.getMessage());
        }
    }
    public void takeBook(int number) throws SQLException {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/library","postgres","keklolloh123");
            String sql = "SELECT name FROM public.\"books\" WHERE number = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, number);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String bookName = rs.getString("name");
                String insertSql= "INSERT INTO public.\"taken_books\" (number, book_name) VALUES (?, ?)";
                stmt = conn.prepareStatement(insertSql);
                stmt.setInt(1, number);
                stmt.setString(2, bookName);
                stmt.executeUpdate();
                System.out.println("Book '" + bookName + "' with number " + number + " was taken.");
            } else {
                System.out.println("Book not found.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }
    @Override
    public String toString() {
        return "Book" +
                " number=" + number +
                ", name='" + name + '\'' +
                ", year_of_publication=" + year_of_publication +
                ", type='" + type + "'";
    }
}
