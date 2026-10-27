import java.sql.*;
import java.util.Scanner;

public class EmployeeApp {

    static final String URL = "jdbc:mysql://127.0.0.1:3306/employee_db";

    static final String USER = "root";
    static final String PASSWORD = "password"; // Change if your MySQL password is different

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to database
            Connection con = DriverManager.getConnection(
                    URL, USER, PASSWORD);

            System.out.println("======================================");
            System.out.println("   EMPLOYEE DATABASE SEARCH SYSTEM");
            System.out.println("======================================");

            System.out.println("\n1. Search Employee by Name");
            System.out.println("2. Filter by Department");
            System.out.println("3. Search by Salary Range");
            System.out.println("4. Sort by Salary");

            System.out.print("\nEnter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            PreparedStatement ps = null;

            // Search by Name
            if (choice == 1) {

                System.out.print("Enter employee name: ");
                String name = sc.nextLine();

                String sql = "SELECT * FROM employees WHERE name LIKE ?";

                ps = con.prepareStatement(sql);
                ps.setString(1, "%" + name + "%");
            }

            // Filter by Department
            else if (choice == 2) {

                System.out.print("Enter department: ");
                String department = sc.nextLine();

                String sql = "SELECT * FROM employees WHERE department = ?";

                ps = con.prepareStatement(sql);
                ps.setString(1, department);
            }

            // Salary Range
            else if (choice == 3) {

                System.out.print("Enter minimum salary: ");
                double minSalary = sc.nextDouble();

                System.out.print("Enter maximum salary: ");
                double maxSalary = sc.nextDouble();

                String sql = "SELECT * FROM employees " +
                        "WHERE salary BETWEEN ? AND ?";

                ps = con.prepareStatement(sql);

                ps.setDouble(1, minSalary);
                ps.setDouble(2, maxSalary);
            }

            // Sort by Salary
            else if (choice == 4) {

                System.out.println("\n1. Low to High");
                System.out.println("2. High to Low");

                System.out.print("Enter choice: ");
                int sortChoice = sc.nextInt();

                String sql;

                if (sortChoice == 1) {
                    sql = "SELECT * FROM employees ORDER BY salary ASC";
                } else {
                    sql = "SELECT * FROM employees ORDER BY salary DESC";
                }

                ps = con.prepareStatement(sql);
            }

            else {
                System.out.println("Invalid choice!");
                con.close();
                sc.close();
                return;
            }

            // Execute SQL query
            ResultSet rs = ps.executeQuery();

            System.out.println("\n------------------------------------------------");
            System.out.printf("%-8s %-15s %-15s %-10s%n",
                    "ID", "NAME", "DEPARTMENT", "SALARY");
            System.out.println("------------------------------------------------");

            boolean found = false;

            while (rs.next()) {

                found = true;

                System.out.printf("%-8d %-15s %-15s %.2f%n",
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("department"),
                        rs.getDouble("salary"));
            }

            if (!found) {
                System.out.println("No employee found.");
            }

            // Close resources
            rs.close();
            ps.close();
            con.close();
            sc.close();

        } catch (ClassNotFoundException e) {

            System.out.println("MySQL JDBC Driver not found!");

        } catch (SQLException e) {

            System.out.println("Database Error: " + e.getMessage());
        }
    }
}