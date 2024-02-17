package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public abstract class Main {
    public static void main(String[] args) {
        String connectionString = "jdbc:postgresql://localhost:5432/library";
        ArrayList<User> users = new ArrayList<>();
        ArrayList<Book> books = new ArrayList<>();
        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(connectionString, "postgres", "keklolloh123");

            String sql = "SELECT id, name, surname, status, email, balance, password FROM public.\"user\" ORDER BY id";
            Statement stat = conn.createStatement();

            ResultSet rs = stat.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String surname = rs.getString("surname");
                boolean status = rs.getBoolean("status");
                String email = rs.getString("email");
                String password = rs.getString("password");
                float balance = rs.getFloat("balance");

                User user = new User(id, name, surname, status, email, password, balance);
                users.add(user);
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Couldn't connect to the database: " + e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.out.println("Failed to close the connection: " + e.getMessage());
                }
            }
        }

        try {
            User user = new User(1, "1", "1", true, "1", "1", 0);
            Book book = new Book(1, "1", 1, "1", 0, 0, false);
            Scanner sc = new Scanner(System.in);

            System.out.println("0.Log in\n" + "1.Sign up");
            String choic = sc.nextLine();
            int choice = -1;
            while (true) {
                if (choic.equals("1") || choic.equals("0")) {
                    choice = Integer.parseInt(choic);
                    break;
                } else {
                    System.out.println("Wrong, write 0 or 1: ");
                    choic = sc.nextLine();
                }
            }

            if (choice == 1) {
                user.addElement();
                conn.close();

                conn = DriverManager.getConnection(connectionString, "postgres", "keklolloh123");
                String sql = "SELECT id, name, surname, status, email, balance, password FROM public.\"user\" ORDER BY id";
                Statement stat = conn.createStatement();
                ResultSet rs = stat.executeQuery(sql);
                users.clear();

                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String surname = rs.getString("surname");
                    boolean status = rs.getBoolean("status");
                    String email = rs.getString("email");
                    String password = rs.getString("password");
                    float balance = rs.getFloat("balance");

                    User newUser = new User(id, name, surname, status, email, password, balance);
                    users.add(newUser);
                }

                boolean userStatus = users.get(users.size() - 1).isStatus();

                try {
                    int temp = 1;
                    if (userStatus) {
                        while (temp != 5) {
                            System.out.println("Chose one of the options: \n" +
                                    "0.Top 10 books \n" +
                                    "1.Take book \n" +
                                    "2.Buy book\n" +
                                    "3.Top up your balance\n" +
                                    "4.Return book\n" +
                                    "5.Exit");
                            temp = sc.nextInt();

                            if (temp == 1) {
                                System.out.println("Here is catalogue of free books that u can take: ");
                                book.showCatalogueOfFree();
                                System.out.print("Choose the number of book that u want to take: ");
                                String y = sc.nextLine();
                                int number;
                                while (true) {
                                    if (y.matches("[0-9]+")) {
                                        number = Integer.parseInt(y);
                                        break;
                                    } else {
                                        System.out.println("Enter only numbers, try again:");
                                        y = sc.nextLine();
                                    }
                                }
                                book.takeBook(number, user);
                            } else if (temp == 0) {
                                book.showRating();
                            } else if (temp == 2) {
                                System.out.println("Here is catalogue of books that u can buy: ");
                                book.showCatalogueOfPayable();
                                System.out.print("Choose the number of book that u want to buy: ");
                                String y = sc.nextLine();
                                int number;
                                while (true) {
                                    if (y.matches("[0-9]+")) {
                                        number = Integer.parseInt(y);
                                        break;
                                    } else {
                                        System.out.println("Enter only numbers, try again:");
                                        y = sc.nextLine();
                                    }
                                }
                                book.buyBook(number);

                            } else if (temp == 3) {
                                System.out.println("Write amount which u want to add: ");
                                String f = sc.nextLine();
                                float money;
                                while (true) {
                                    if (f.matches("^\\d*\\.?\\d*$") && Float.parseFloat(f) >= 0) {
                                        money = Float.parseFloat(f);
                                        break;
                                    } else {
                                        System.out.println("Enter only non-negative numbers, try again:");
                                        f = sc.nextLine();
                                    }
                                }
                                book.IWANNAMONEY(money);

                            } else if (temp == 4) {

                                Taken_books list = new Taken_books(1, 1, "1", "1", 1);
                                list.ShowTaken_books();
                                System.out.print("Choose the number of book that u want to return: ");
                                String y = sc.nextLine();
                                int number;
                                while (true) {
                                    if (y.matches("[0-9]+")) {
                                        number = Integer.parseInt(y);
                                        break;
                                    } else {
                                        System.out.println("Enter only numbers, try again:");
                                        y = sc.nextLine();
                                    }
                                }
                                book.returnBook(number);

                            } else if (temp == 5) {
                                break;
                            }
                        }
                    } else {
                        while (temp != 4) {
                            System.out.println("Chose one of the options: \n" +
                                    "0.Top 10 books \n" +
                                    "1.Add free book \n" +
                                    "2.Add book with price\n" +
                                    "3.Top up your balance\n" +
                                    "4.Exit");
                            temp = sc.nextInt();

                            if (temp == 0) {
                                System.out.println("Here is top 10 books of our web - service: ");
                                book.showRating();
                            } else if (temp == 1) {
                                System.out.println("Add characteristics of free book that u want to add: ");
                                book.addElement();
                            } else if (temp == 2) {
                                System.out.println("Add characteristics of buyable book that u want to add: ");
                                book.addBookWithPrice();
                            } else if (temp == 3) {
                                System.out.println("Write amount which u want to add: ");
                                String f = sc.nextLine();
                                float money;
                                while (true) {
                                    if (f.matches("^\\d*\\.?\\d*$") && Float.parseFloat(f) >= 0) {
                                        money = Float.parseFloat(f);
                                        break;
                                    } else {
                                        System.out.println("Enter only non-negative numbers, try again:");
                                        f = sc.nextLine();
                                    }
                                }
                                book.IWANNAMONEY(money);
                            } else if (temp == 4) {
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Oops! Something went wrong: " + e.getMessage());
                }

            } else if (choice == 0) {
                String email = "";
                String password = "";
                while (true) {
                    System.out.print("Enter your email: ");
                    email = sc.nextLine();
                    while (!email.matches("\\b[\\w.%-]+@[-.\\w]+\\.[A-Za-z]{2,4}\\b")) {
                        System.out.println("Wrong email, enter your email again: ");
                        email = sc.nextLine();
                    }

                    System.out.print("Enter your password: ");
                    password = sc.nextLine();
                    while (!password.matches("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}")) {
                        System.out.println("Wrong password, password should consist of:\n digit, lower and upper case and at least 8 symbols");
                        System.out.println("Try again:");
                        password = sc.nextLine();
                    }
                    if (!user.isValid(email, password, users)) {
                        System.out.println("Wrong password or email, try again");
                    } else {
                        break;
                    }
                }
                PreparedStatement stmt = null;
                ResultSet rs = null;

                try {

                    Connection con = DriverManager.getConnection(connectionString, "postgres", "keklolloh123");
                    String statusQuery = "SELECT status FROM public.\"user\" WHERE email = ?";
                    stmt = con.prepareStatement(statusQuery);
                    stmt.setString(1, email);
                    rs = stmt.executeQuery();

                    if (rs.next()) {

                        boolean userStatus = rs.getBoolean("status");
                        int temp = 1;
                        if (userStatus) {
                            while (temp != 5) {
                                System.out.println("Chose one of the options: \n" +
                                        "0.Top 10 books \n" +
                                        "1.Take book \n" +
                                        "2.Buy book\n" +
                                        "3.Top up your balance\n" +
                                        "4.Return book\n" +
                                        "5.Exit");
                                temp = sc.nextInt();

                                if (temp == 1) {
                                    System.out.println("Here is catalogue of free books that u can take: ");
                                    book.showCatalogueOfFree();
                                    System.out.print("Choose the number of book that u want to take: ");
                                    String y = sc.nextLine();
                                    int number;
                                    while (true) {
                                        if (y.matches("[0-9]+")) {
                                            number = Integer.parseInt(y);
                                            break;
                                        } else {
                                            System.out.println("Enter only numbers, try again:");
                                            y = sc.nextLine();
                                        }
                                    }
                                    book.takeBook(number, user);
                                } else if (temp == 0) {
                                    book.showRating();
                                } else if (temp == 2) {
                                    System.out.println("Here is catalogue of books that u can buy: ");
                                    book.showCatalogueOfPayable();
                                    System.out.print("Choose the number of book that u want to buy: ");
                                    String y = sc.nextLine();
                                    int number;
                                    while (true) {
                                        if (y.matches("[0-9]+")) {
                                            number = Integer.parseInt(y);
                                            break;
                                        } else {
                                            System.out.println("Enter only numbers, try again:");
                                            y = sc.nextLine();
                                        }
                                    }
                                    book.buyBook(number);

                                } else if (temp == 3) {
                                    System.out.println("Write amount which u want to add: ");
                                    String f = sc.nextLine();
                                    float money;
                                    while (true) {
                                        if (f.matches("^\\d*\\.?\\d*$") && Float.parseFloat(f) >= 0) {
                                            money = Float.parseFloat(f);
                                            break;
                                        } else {
                                            System.out.println("Enter only non-negative numbers, try again:");
                                            f = sc.nextLine();
                                        }
                                    }
                                    book.IWANNAMONEY(money);

                                } else if (temp == 4) {

                                    Taken_books list = new Taken_books(1, 1, "1", "1", 1);
                                    list.ShowTaken_books();
                                    System.out.print("Choose the number of book that u want to return: ");
                                    String y = sc.nextLine();
                                    int number;
                                    while (true) {
                                        if (y.matches("[0-9]+")) {
                                            number = Integer.parseInt(y);
                                            break;
                                        } else {
                                            System.out.println("Enter only numbers, try again:");
                                            y = sc.nextLine();
                                        }
                                    }
                                    book.returnBook(number);

                                } else if (temp == 5) {
                                    break;
                                }
                            }
                        } else if (!userStatus) {
                            while (temp != 4) {
                                System.out.println("Chose one of the options: \n" +
                                        "0.Top 10 books \n" +
                                        "1.Add free book\n" +
                                        "2.Add book with price\n" +
                                        "3.Top up your balance\n" +
                                        "4.Exit");
                                temp = sc.nextInt();

                                if (temp == 0) {
                                    System.out.println("Here is top 10 books of our web - service: ");
                                    book.showRating();
                                } else if (temp == 1) {
                                    System.out.println("Add characteristics of free book that u want to add: ");
                                    book.addElement();
                                } else if (temp == 2) {
                                    System.out.println("Add characteristics of buyable book that u want to add: ");
                                    book.addBookWithPrice();
                                } else if (temp == 3) {
                                    System.out.println("Write amount which u want to add: ");
                                    String f = sc.nextLine();
                                    float money;
                                    while (true) {
                                        if (f.matches("^\\d*\\.?\\d*$") && Float.parseFloat(f) >= 0) {
                                            money = Float.parseFloat(f);
                                            break;
                                        } else {
                                            System.out.println("Enter only non-negative numbers, try again:");
                                            f = sc.nextLine();
                                        }
                                    }
                                    book.IWANNAMONEY(money);
                                } else if (temp == 4) {
                                    break;
                                }
                            }
                        }
                    }
                } catch (SQLException e) {
                    throw new RuntimeException("Oops! Something went wrong with database: " + e.getMessage());
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public abstract void deleteElement(int id) throws SQLException;

    public abstract void addElement() throws SQLException;
}
