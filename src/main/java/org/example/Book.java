package org.example;\eqwewqewqe

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Book extends Main{
    private int number;
    private String name;
    private int year_of_publication;
    private String type;
    private int number_of_readers;
    private float price;
    private boolean has_a_price;
    private ArrayList<Book> books = new ArrayList<>();

    public Book(int number, String name, int year_of_publication, String type, int number_of_readers,float price,boolean has_a_price) {
        this.number = number;
        this.name = name;
        this.year_of_publication = year_of_publication;
        this.type = type;
        this.books = books;
        this.number_of_readers = number_of_readers;
        this.has_a_price = has_a_price;
        this.price = price;
    }

    public int getNumber_of_readers() {
        return number_of_readers;
    }

    public float getPrice() {
        return price;
    }

    public boolean isHas_a_price() {
        return has_a_price;
    }

    public void setNumber_of_readers(int number_of_readers) {
        this.number_of_readers = number_of_readers;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setHas_a_price(boolean has_a_price) {
        this.has_a_price = has_a_price;
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

        System.out.print("Enter genre of the book: ");
        String type = scanner.nextLine();
        while(!type.matches("[a-zA-Z]+\\.?")){
            System.out.println("It should only contain letters,enter your genre again: ");
            type = scanner.nextLine();
        }

        System.out.print("Enter year of publication: ");
        String y = scanner.nextLine();
        int year_of_publication;
        while (true) {
            if (y.matches("[0-9]+")) {
                year_of_publication = Integer.parseInt(y);
                break;
            } else {
                System.out.println("Enter only non-negative numbers, try again:");
                y = scanner.nextLine();
            }
        }

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


    //Take book
    public void takeBook(int number, User user) throws SQLException {
        Connection conn = null;
        Scanner sc = new Scanner(System.in);
        boolean emailFound = false;
        String email = "";
        while (!emailFound) {
            System.out.println("Please, enter your email: ");
            email = sc.nextLine();
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/library", "postgres", "keklolloh123");
            String selectSqlUser = "SELECT name,id FROM public.\"user\" WHERE email = ?";
            PreparedStatement selectStmtUser = conn.prepareStatement(selectSqlUser);
            selectStmtUser.setString(1, email);
            ResultSet rs1 = selectStmtUser.executeQuery();
            if (rs1.next()) {
                emailFound = true;
            } else {
                System.out.println("Wrong email, try again: ");
            }
        }

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
            Book book = new Book(number, name, year_of_pub, type, number_of_readers,price,has_a_price);
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

    public void buyBook(int number) throws SQLException {
        Scanner sc = null;
        Connection conn = null;
        PreparedStatement stmt1 = null, stmt2 = null, insertStmt = null, updateStmt1 = null, updateStmt2 = null;
        ResultSet rs1 = null, rs2 = null;

        try {
            sc = new Scanner(System.in);
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/library", "postgres", "keklolloh123");

            String email = "";
            boolean emailFound = false;
            while (!emailFound) {
                System.out.println("Please,enter your email again: ");
                email = sc.nextLine();

                String selectSqlUser = "SELECT name,id,balance FROM public.\"user\" WHERE email = ?";
                PreparedStatement selectStmtUser = conn.prepareStatement(selectSqlUser);
                selectStmtUser.setString(1, email);
                rs2 = selectStmtUser.executeQuery();

                if (rs2.next()) {
                    emailFound = true;
                } else {
                    System.out.println("Wrong email,try again");
                }
            }
            String sql1 = "SELECT number, name, has_a_price, price FROM public.books WHERE number = ?";
            stmt1 = conn.prepareStatement(sql1);
            stmt1.setInt(1, number);
            rs1 = stmt1.executeQuery();

            if (rs1.next() && rs2.next()) {
                int bookNumber = rs1.getInt("number");
                String bookName = rs1.getString("name");
                boolean hasAPrice = rs1.getBoolean("has_a_price");
                float bookPrice = rs1.getFloat("price");
                int userId = rs2.getInt("id");
                String userName = rs2.getString("name");
                float userBalance = rs2.getFloat("balance");
                if (hasAPrice && userBalance >= bookPrice) {
                    String insertSql = "INSERT INTO public.taken_books (number, book_name, user_name, user_id) VALUES (?, ?, ?, ?)";
                    insertStmt = conn.prepareStatement(insertSql);
                    insertStmt.setInt(1, bookNumber);
                    insertStmt.setString(2, bookName);
                    insertStmt.setString(3, userName);
                    insertStmt.setInt(4, userId);
                    insertStmt.executeUpdate();

                    String selectSqlBook = "SELECT name, number_of_readers FROM public.\"books\" WHERE number = ?";
                    updateStmt1 = conn.prepareStatement(selectSqlBook);
                    updateStmt1.setInt(1, bookNumber);
                    rs1 = updateStmt1.executeQuery();

                    int currentReaders = 0;
                    if (rs1.next()) {
                        currentReaders = rs1.getInt("number_of_readers");
                        currentReaders++;
                    }

                    String updateSql = "UPDATE public.\"books\" SET number_of_readers = ? WHERE number = ?";
                    updateStmt2 = conn.prepareStatement(updateSql);
                    updateStmt2.setInt(1, currentReaders);
                    updateStmt2.setInt(2, bookNumber);
                    updateStmt2.executeUpdate();

                    sql1 = "UPDATE public.\"user\" SET balance = (balance - ?) WHERE email = ?";
                    updateStmt1 = conn.prepareStatement(sql1);
                    updateStmt1.setFloat(1, bookPrice);
                    updateStmt1.setString(2, email);
                    updateStmt1.executeUpdate();

                    System.out.println("Book '" + bookName + "' with number " + number + " was bought.");
                } else {
                    if (!hasAPrice) {
                        System.out.println("This book is free.");
                    } else {
                        System.out.println("You don't have enough cash!");
                    }
                }
            } else {
                if (!rs1.next()) {
                    System.out.println("Book number " + number + " not found.");
                } else if (!rs2.next()) {
                    System.out.println("User with email '" + email + "' not found.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error accessing database: " + e.getMessage());
        }
    }

    public void addBookWithPrice(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter name of the book: ");
        String name = scanner.nextLine();

        System.out.print("Enter genre of the book: ");
        String type = scanner.nextLine();
        while(!type.matches("[a-zA-Z]+\\.?")){
            System.out.println("It should only contain letters,enter your genre again: ");
            type = scanner.nextLine();
        }

        System.out.print("Enter year of publication: ");
        String y = scanner.nextLine();
        int year_of_publication;
        while (true) {
            if (y.matches("[0-9]+")) {
                year_of_publication = Integer.parseInt(y);
                break;
            } else {
                System.out.println("Enter only non-negative numbers, try again:");
                y = scanner.nextLine();
            }
        }

        System.out.print("Enter price: ");
        String f = scanner.nextLine();
        float price;
        while (true) {
            if (f.matches("^([+-]?\\d*\\.?\\d*)$") && Float.parseFloat(f) >= 0) {
                price = Float.parseFloat(f);
                break;
            } else {
                System.out.println("Enter only non-negative numbers, try again:");
                f = scanner.nextLine();
            }
        }

        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/library", "postgres", "keklolloh123")) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO public.\"books\" (name, year_of_publication, type, price, has_a_price) VALUES (?, ?, ?, ?, ?)");
            stmt.setString(1, name);
            stmt.setInt(2, year_of_publication);
            stmt.setString(3, type);
            stmt.setFloat(4, price);
            stmt.setBoolean(5, true);
            stmt.executeUpdate();
            System.out.println("Book added successfully!");
        } catch (SQLException e) {
            System.err.println("Error adding book: " + e.getMessage());
        }
    }


    public void showCatalogueOfFree() throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/library", "postgres", "keklolloh123");
        String sql = "SELECT * FROM public.books WHERE has_a_price = false";
        Statement stat = con.createStatement();
        ResultSet rs = stat.executeQuery(sql);
        boolean hasPrice = false;

        ArrayList<Book> newBooks = new ArrayList<>();

        while (rs.next()) {
            String name = rs.getString("name");
            int number = rs.getInt("number");
            int year_of_pub = rs.getInt("year_of_publication");
            String type = rs.getString("type");
            int number_of_readers = rs.getInt("number_of_readers");
            float price = rs.getFloat("price");
            Book book = new Book(number, name, year_of_pub, type, number_of_readers, price, hasPrice);
            newBooks.add(book);
        }

        for (Book book : newBooks) {
            if (!book.isHas_a_price()) {
                System.out.println("↪\uFE0E Number of readers: " + book.getNumber_of_readers() + " name: " + book.getName() + " || type: " + book.getType() + " || number: " + book.getNumber());
            }
        }

        books.addAll(newBooks);
    }

    public void showCatalogueOfPayable() throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/library", "postgres", "keklolloh123");
        String sql = "SELECT * FROM public.books WHERE has_a_price = true";
        Statement stat = con.createStatement();
        ResultSet rs = stat.executeQuery(sql);
        boolean hasPrice = true;

        ArrayList<Book> newBooks = new ArrayList<>();

        while (rs.next()) {
            String name = rs.getString("name");
            int number = rs.getInt("number");
            int year_of_pub = rs.getInt("year_of_publication");
            String type = rs.getString("type");
            int number_of_readers = rs.getInt("number_of_readers");
            float price = rs.getFloat("price");
            Book book = new Book(number, name, year_of_pub, type, number_of_readers, price, hasPrice);
            newBooks.add(book);
        }

        for (Book book : newBooks) {
            if (book.isHas_a_price()) {
                System.out.println("↪\uFE0E Number of readers: " + book.getNumber_of_readers() + " name: " + book.getName() + " || type: " + book.getType() + " || number: " + book.getNumber()
                        + "|| price" + book.getPrice());
            }
        }

        books.addAll(newBooks);
    }
    public void returnBook(int number) throws SQLException{
        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter your email again: ");
        String email = sc.nextLine();
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/library", "postgres", "keklolloh123");
            String selectSqlTakenBook = "SELECT * FROM public.taken_books WHERE number = ?";
            PreparedStatement selectStmt = conn.prepareStatement(selectSqlTakenBook);
            selectStmt.setInt(1, number);
            ResultSet rs = selectStmt.executeQuery();


            String selectSqlUser = "SELECT * FROM public.\"user\" WHERE email = ?";
            PreparedStatement selectStmtUser = conn.prepareStatement(selectSqlUser);
            selectStmtUser.setString(1, email);
            ResultSet rs1 = selectStmtUser.executeQuery();


            if (rs.next() && rs1.next()) {

                int selectnumber = rs.getInt("number");
                String selectusername = rs1.getString("name");

                String deleteSql = "DELETE FROM taken_books WHERE number = ? AND user_name = ?;";
                PreparedStatement deleteStmt = conn.prepareStatement(deleteSql);

                deleteStmt.setInt(1, selectnumber);
                deleteStmt.setString(2, selectusername);
                deleteStmt.executeUpdate();
                System.out.println("Book with number " + number + " was return");


            }else {
                System.out.println("User with email " + email + " not found.");
                return;
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    public void IWANNAMONEY(float money) throws SQLException{
        Scanner sc = null;
        Connection conn = null;
        PreparedStatement stmt1 = null, stmt2 = null, insertStmt = null, updateStmt1 = null, updateStmt2 = null;
        ResultSet rs1 = null, rs2 = null;
        try{
            sc = new Scanner(System.in);
            String email= "";
            boolean emailFound = false;
            while (!emailFound) {
                System.out.println("Please, enter your email: ");
                email = sc.nextLine();

                conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/library", "postgres", "keklolloh123");

                String selectbalance = "SELECT name,id,balance FROM public.\"user\" WHERE email = ?";
                PreparedStatement updatebalance = conn.prepareStatement(selectbalance);
                updatebalance.setString(1, email);
                rs2 = updatebalance.executeQuery();

                if(rs2.next()){
                    emailFound = true;
                }
                else{
                    System.out.println("Email does not exist,try again");
                }
            }
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/library", "postgres", "keklolloh123");

            String selectbalance = "SELECT name,id,balance FROM public.\"user\" WHERE email = ?";
            PreparedStatement updatebalance = conn.prepareStatement(selectbalance);
            updatebalance.setString(1, email);
            rs2 = updatebalance.executeQuery();

            selectbalance = "UPDATE public.\"user\" SET balance = (balance + ?) WHERE email = ?";
            updateStmt1 = conn.prepareStatement(selectbalance);
            updateStmt1.setFloat(1, money);
            updateStmt1.setString(2, email);
            updateStmt1.executeUpdate();
            System.out.println("Money has been putted in successful!");
        }catch (SQLException e) {
            System.err.println("Error accessing database: " + e.getMessage());
        }
    }



    @Override
    public String toString() {
        return "↪\uFE0E Number of readers: " + number_of_readers + " name: " + name + " || type: " + type + " || number: " + number + "has a price" + (has_a_price ? "Its not free" : "Its free");
    }
}
