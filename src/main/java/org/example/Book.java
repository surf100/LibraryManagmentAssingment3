package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Book extends Main{
    private int number;
    private String name;
    private int year_of_publication;
    private String type;
    private int number_of_readers;
    private ArrayList<Book> books = new ArrayList<>();

    public Book(int number, String name, int year_of_publication, String type, int number_of_readers) {
        this.number = number;
        this.name = name;
        this.year_of_publication = year_of_publication;
        this.type = type;
        this.books = books;
        this.number_of_readers = number_of_readers;
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



    public void takeBook(int number, User user) throws SQLException {
        Connection conn = null;
        Scanner sc = new Scanner(System.in);
        System.out.println("Please, enter your email again: ");
        String email = sc.nextLine();
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/library", "postgres", "keklolloh123");
            String selectSqlBook = "SELECT name, number_of_readers FROM public.\"books\" WHERE number = ?";
            PreparedStatement selectStmt = conn.prepareStatement(selectSqlBook);
            selectStmt.setInt(1, number);
            ResultSet rs = selectStmt.executeQuery();
            String selectSqlUser = "SELECT name,id FROM public.\"user\" WHERE email = ?";
            PreparedStatement selectStmtUser = conn.prepareStatement(selectSqlUser);
            selectStmtUser.setString(1, email);
            ResultSet rs1 = selectStmtUser.executeQuery();
            if (rs.next()) {
                String bookName = rs.getString("name");
                int currentReaders = rs.getInt("number_of_readers");
                currentReaders++;
                String userName = "";
                int userId = 0;
                if (rs1.next()) {
                    userName = rs1.getString("name");
                    userId = rs1.getInt("id");
                } else {
                    System.out.println("User with email " + email + " not found.");
                    return;
                }
                String insertSql = "INSERT INTO public.\"taken_books\" (number, book_name, user_name,user_id) VALUES (?, ?, ?, ?)";
                PreparedStatement insertStmt = conn.prepareStatement(insertSql);
                insertStmt.setInt(1, number);
                insertStmt.setString(2, bookName);
                insertStmt.setString(3, userName);
                insertStmt.setInt(4, userId);
                insertStmt.executeUpdate();
                System.out.println("Book '" + bookName + "' with number " + number + " was taken by user '" + userName + "'.");
                String updateSql = "UPDATE public.\"books\" SET number_of_readers = ? WHERE number = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                updateStmt.setInt(1, currentReaders);
                updateStmt.setInt(2, number);
                updateStmt.executeUpdate();
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



    public void showRating() throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/library", "postgres", "keklolloh123");
        String sql = "SELECT * FROM public.books " + "ORDER BY number_of_readers DESC ";
        Statement stat = con.createStatement();
        ResultSet rs = stat.executeQuery(sql);
        while (rs.next()) {
            String name = rs.getString("name");
            int number = rs.getInt("number");
            int year_of_pub = rs.getInt("year_of_publication");
            String type = rs.getString("type");
            int number_of_readers = rs.getInt("number_of_readers");

            Book book = new Book(number, name, year_of_pub, type, number_of_readers);
            books.add(book);
        }
        int i = 0 ;
        for (Book bok : books) {
            System.out.println(bok);
            i++;
            if(i==0){
                break;
            }
        }
    }

    @Override
    public String toString() {
        return "↪\uFE0E Number of readers: " + number_of_readers + " name: " + name + " || type: " + type + " || number: " + number;
    }
}
