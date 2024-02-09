package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public abstract class Main {
    public static void main(String[] args) throws SQLException {
        String connectionString = "jdbc:postgresql://localhost:5432/library";
        ArrayList<User> users = new ArrayList<>();
        ArrayList<Book> books = new ArrayList<>();
        Connection conn = null;
        try{
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(connectionString,"postgres","keklolloh123");

            String sql = "SELECT id, name, surname, status, email, password FROM public.\"user\" ORDER BY id";
            Statement stat = conn.createStatement();

            ResultSet rs = stat.executeQuery(sql);
            while (rs.next()){
                int    id = rs.getInt("id");
                String name = rs.getString("name");
                String surname = rs.getString("surname");
                boolean status = rs.getBoolean("status");
                String email = rs.getString("email");
                String password = rs.getString("password");

                User user = new User(id,name,surname,status,email,password);
                users.add(user);
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.out.println("could not close the connection: " + e.getMessage());
                }
            }
        }


        try {
            User user = new User(1,"1","1",true,"1","1");
            Book book = new Book(1, "1", 1, "1");
            Scanner sc = new Scanner(System.in);
            System.out.println("Do u want to Log in(l) or Sign up?(s):");
            String choice = sc.nextLine();
            if(choice.equals("s")){
                user.addElement();
                conn.close();
                conn = DriverManager.getConnection(connectionString, "postgres", "keklolloh123");
                String sql = "SELECT id, name, surname, status, email, password FROM public.\"user\" ORDER BY id";
                Statement stat = conn.createStatement();
                ResultSet rs = stat.executeQuery(sql);
                users.clear();
                while (rs.next()){
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String surname = rs.getString("surname");
                    boolean status = rs.getBoolean("status");
                    String email = rs.getString("email");
                    String password = rs.getString("password");

                    User newUser = new User(id, name, surname, status, email, password);
                    users.add(newUser);
                }
                boolean userStatus = users.get(users.size()-1).isStatus();
                try{
                    if (userStatus) {
                        System.out.println("Write number of the book that u want to take: ");
                        int number = sc.nextInt();
                        book.takeBook(number);
                    }else{
                        System.out.println("Add charachteristics of book that u want to add: ");
                        book.addElement();
                    }
                }catch (Exception e){
                    System.out.println("Book already exist" + e.getMessage());
                }

            }else if(choice.equals("l")) {
                System.out.println("Enter your email: ");
                String email = sc.nextLine();
                System.out.println("Enter your password: ");
                String password = sc.nextLine();
                if(user.isValid(email,password,users)) {
                    PreparedStatement stmt = null;
                    ResultSet rs = null;
                    try {
                        Connection con = DriverManager.getConnection(connectionString,"postgres","keklolloh123");
                        String statusQuery = "SELECT status FROM public.\"user\" WHERE email = ?";
                        /* Statment */ stmt = con.prepareStatement(statusQuery);
                        stmt.setString(1,email);
                        /* Resultset */rs = stmt.executeQuery();
                        if(rs.next()) {
                            boolean userStatus = rs.getBoolean("status");
                            if(userStatus) {
                                System.out.println("Write number of the book that you want to take: ");
                                int number = sc.nextInt();
                                book.takeBook(number);
                            }else if (!userStatus){
                                System.out.println("Add charachteristics of book that u want to add: ");
                                book.addElement();
                            }
                        }
                    } catch(SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

        }catch (SQLException e) {
            System.err.println(e.getMessage());
        }

    }

    public abstract void deleteElement(int id) throws SQLException;

    public abstract void addElement() throws SQLException;
}


/*else{

            System.out.print("Enter id of user that u want to delete: ");
            int usersToDelete = sc.nextInt();
            user.deleteElement(usersToDelete);

            for(int i = 0; i< users.size(); i++){
                if (users.get(i).getId()==usersToDelete){
                    users.remove(i);}
            }}
        System.out.println("User was deleted successfully.");


        package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public abstract class Main {
    public static void main(String[] args) throws SQLException {
        String connectionString = "jdbc:postgresql://localhost:5432/library";
        ArrayList<User> users = new ArrayList<>();
        ArrayList<Book> books = new ArrayList<>();
        Connection conn = null;
        try{
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(connectionString,"postgres","keklolloh123");

            String sql = "SELECT id, name, surname, status, email, password FROM public.\"user\" ORDER BY id";
            Statement stat = conn.createStatement();

            ResultSet rs = stat.executeQuery(sql);
            while (rs.next()){
                int    id = rs.getInt("id");
                String name = rs.getString("name");
                String surname = rs.getString("surname");
                boolean status = rs.getBoolean("status");
                String email = rs.getString("email");
                String password = rs.getString("password");

                User user = new User(id,name,surname,status,email,password);
                users.add(user);
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.out.println("could not close the connection: " + e.getMessage());
                }
            }
        }


        try {
            User user = new User(1,"1","1",true,"1","1");
            Scanner sc = new Scanner(System.in);
            System.out.println("Do u want to Log in(l) or Sign up?(s):");
            String choice = sc.nextLine();
            if(choice.equals("s")){
                user.addElement();
                boolean userStatus = users.get(users.size() - 1).isStatus();
                if (userStatus) {
                    System.out.println("Write number of the book that u want to take: ");
                    int number = sc.nextInt();
                    Book book = new Book(1,"1",1,"1");
                    book.takeBook(number);
                }

            }else if(choice.equals("l")){
                System.out.println("Enter your email: ");
                String email = sc.nextLine();
                System.out.println("Enter your password: ");
                String password = sc.nextLine();
                if(user.isValid(email,password,users)){
                    conn = DriverManager.getConnection(connectionString,"postgres","keklolloh123");
                    String status = "SELECT status FROM public.\"user\" WHERE email = ?";
                    PreparedStatement stmt = conn.prepareStatement(status);
                    stmt.setString(1,email);
                    ResultSet rs = stmt.executeQuery(status);
                    boolean userStatus = rs.getBoolean("status");
                    if(userStatus){
                        System.out.println("Write number of the book that u want to take: ");
                        int number = sc.nextInt();
                        Book book = new Book(1,"1",1,"1");
                        book.takeBook(number);
                    }

                }
            }
        }catch (SQLException e) {
            System.err.println(e.getMessage());
        }

    }
    public abstract void deleteElement(int id) throws SQLException;

    public abstract void addElement() throws SQLException;
}


/*else{

            System.out.print("Enter id of user that u want to delete: ");
            int usersToDelete = sc.nextInt();
            user.deleteElement(usersToDelete);

            for(int i = 0; i< users.size(); i++){
                if (users.get(i).getId()==usersToDelete){
                    users.remove(i);}
            }}
        System.out.println("User was deleted successfully.");*/
