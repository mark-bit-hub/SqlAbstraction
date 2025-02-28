import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class SqlAbstraction {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Map<Integer, String> sqlCommands = new HashMap<>();

        // Predefined SQL commands
        sqlCommands.put(1, "SELECT * FROM users;");
        sqlCommands.put(2, "SELECT * FROM products WHERE price > 50.00;");
        sqlCommands.put(3, "INSERT INTO orders (user_id, product_id, quantity) VALUES (1, 2, 3);");
        sqlCommands.put(4, "UPDATE users SET active = 1 WHERE user_id = 1;");
        sqlCommands.put(5, "DELETE FROM products WHERE product_id = 10;");

        System.out.println("Welcome to the SQL Command Executor!");
        System.out.println("You can execute predefined commands or enter custom SQL queries.");

        while (true) {
            System.out.println("\nAvailable Predefined SQL Commands:");
            for (Map.Entry<Integer, String> entry : sqlCommands.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }

            System.out.println("\nOptions:");
            System.out.println("1. Execute a predefined SQL command");
            System.out.println("2. Enter a custom SQL query");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (choice == 1) {
                // Execute predefined SQL command
                while (true) {
                    System.out.print("Enter the number of the SQL command to execute (or 0 to go back): ");
                    int commandNumber = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    if (commandNumber == 0) {
                        break; // Go back to the main menu
                    } else if (sqlCommands.containsKey(commandNumber)) {
                        String sql = sqlCommands.get(commandNumber);
                        System.out.println("Executing: " + sql);
                        executeSql(sql);
                    } else {
                        System.out.println("Invalid command number. Please try again.");
                    }
                }
            } else if (choice == 2) {
                // Execute custom SQL query
                while (true) {
                    System.out.print("Enter your custom SQL query (or type 'back' to go back): ");
                    String customSql = scanner.nextLine();

                    if (customSql.equalsIgnoreCase("back")) {
                        break; // Go back to the main menu
                    } else {
                        System.out.println("Executing: " + customSql);
                        executeSql(customSql);
                    }
                }
            } else if (choice == 3) {
                // Exit the program
                System.out.println("Exiting the program. Goodbye!");
                break;
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }

        scanner.close();
    }

    private static void executeSql(String sql) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/group_db";
        String username = "root";
        String password = "Beracahone7245&";

        System.out.println("Executing SQL: " + sql);

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
             ResultSet resultSet = sql.trim().toUpperCase().startsWith("SELECT") ? statement.executeQuery(sql) : null) {

            if (resultSet != null) {
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();

                // Calculate column widths
                int[] columnWidths = new int[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    columnWidths[i - 1] = Math.max(metaData.getColumnName(i).length(), 10);
                }

                resultSet.beforeFirst();
                while (resultSet.next()) {
                    for (int i = 1; i <= columnCount; i++) {
                        String value = resultSet.getString(i);
                        if (value != null) {
                            columnWidths[i - 1] = Math.max(columnWidths[i - 1], value.length());
                        }
                    }
                }

                // Print column names
                for (int i = 1; i <= columnCount; i++) {
                    System.out.printf("%-" + (columnWidths[i - 1] + 2) + "s", metaData.getColumnName(i));
                }
                System.out.println();

                // Print rows
                resultSet.beforeFirst();
                while (resultSet.next()) {
                    for (int i = 1; i <= columnCount; i++) {
                        System.out.printf("%-" + (columnWidths[i - 1] + 2) + "s", resultSet.getString(i));
                    }
                    System.out.println();
                }
            } else {
                int rowsAffected = statement.executeUpdate(sql);
                System.out.println(rowsAffected + " rows affected.");
            }

        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Vendor Error Code: " + e.getErrorCode());
            e.printStackTrace();
        }
    }
}