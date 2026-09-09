import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {
    private static DatabaseManager db;
    private static Scanner scanner;

    public static void main(String[] args) {
        System.out.println("=====================");
        System.out.println("E-Games: Digital Game Marketplace");
        System.out.println("=====================");

        db = new DatabaseManager();

        try (Scanner sc = new Scanner(System.in)) {
            scanner = sc;
            while (true) {
                displayMainMenu();
                int choice = getIntInput("Enter your choice: ");
                
                switch (choice) {
                    case 1 -> createOperations();
                    case 2 -> readOperations();
                    case 3 -> updateOperations();
                    case 4 -> deleteOperations();
                    case 5 -> advancedQueries();
                    case 0 -> {
                        System.out.println("Exiting the system. Goodbye!");
                        return;
                    }
                    default -> System.out.println("Invalid choice! Please try again.");
                }
            }
        }
    }

    private static void displayMainMenu() {
        System.out.println("\n=== MAIN MENU ===");
        System.out.println("1. Create Operations (Add data)");
        System.out.println("2. Read Operations (Query data)");
        System.out.println("3. Update Operations");
        System.out.println("4. Delete Operations");
        System.out.println("5. Advanced Queries");
        System.out.println("0. Exit");
    }

    private static void createOperations() {
        while (true) {
            System.out.println("\n=== CREATE OPERATIONS ===");
            System.out.println("1. Add User");
            System.out.println("2. Add Seller");
            System.out.println("3. Add Game");
            System.out.println("4. Add Transaction");
            System.out.println("0. Back to Main Menu");
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1 -> addUser();
                case 2 -> addSeller();
                case 3 -> addGame();
                case 4 -> addTransaction();
                case 0 -> { return; }
                default -> System.out.println("Invalid choice! Please try again.");
            }
        }
    }

    private static void addUser() {
        System.out.println("\n--- Add User ---");
        String userType = getUserTypeInput("Enter User Type (Admin/Guest): ");
        String username = getNonEmptyStringInput("Enter Username: ");
        String email = getStringInput("Enter Email: ");
        String password = getNonEmptyStringInput("Enter Password: ");

        db.addUser(userType, username, email, password);
    }

    private static void addSeller() {
        System.out.println("\n--- Add Seller ---");
        String sellerName = getNonEmptyStringInput("Enter Seller Name: ");
        String contactInfo = getStringInput("Enter Contact Info: ");

        db.addSeller(sellerName, contactInfo);
    }

    private static void addGame() {
        System.out.println("\n--- Add Game ---");
        String gameName = getNonEmptyStringInput("Enter Game Name: ");
        int sellerID = getPositiveIntInput("Enter Seller ID: ");
        String category = getStringInput("Enter Category: ");
        double price = getNonNegativeDoubleInput("Enter Price: ");
        String developer = getStringInput("Enter Developer: ");
        int yearPublished = getPositiveIntInput("Enter Year Published: ");

        db.addGame(gameName, sellerID, category, price, developer, yearPublished);
    }

    private static void addTransaction() {
        System.out.println("\n--- Add Transaction ---");
        int userID = getPositiveIntInput("Enter User ID: ");
        int gameID = getPositiveIntInput("Enter Game ID: ");
        String purchaseDate = getDateInput("Enter Purchase Date (YYYY-MM-DD): ");
        double totalAmount = getNonNegativeDoubleInput("Enter Total Amount: ");

        db.addTransaction(userID, gameID, purchaseDate, totalAmount);
    }

    private static void readOperations() {
        while (true) {
            System.out.println("\n=== READ OPERATIONS ===");
            System.out.println("1. View All Users (Sorted by Username)");
            System.out.println("2. View Games by Category");
            System.out.println("3. View Sellers with Games Count");
            System.out.println("0. Back to Main Menu");
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1 -> db.getUsersSortedByUsername();
                case 2 -> {
                    String category = getStringInput("Enter Category Name: ");
                    db.getGamesByCategory(category);
                }
                case 3 -> db.getSellersWithGames();
                case 0 -> { return; }
                default -> System.out.println("Invalid choice! Please try again.");
            }
        }
    }

    private static void updateOperations() {
        while (true) {
            System.out.println("\n=== UPDATE OPERATIONS ===");
            System.out.println("1. Update User Email");
            System.out.println("0. Back to Main Menu");
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1 -> {
                    int userID = getPositiveIntInput("Enter User ID to update: ");
                    String newEmail = getNonEmptyStringInput("Enter New Email: ");
                    db.updateUserEmail(userID, newEmail);
                }
                case 0 -> { return; }
                default -> System.out.println("Invalid choice! Please try again.");
            }
        }
    }

    private static void deleteOperations() {
        while (true) {
            System.out.println("\n=== DELETE OPERATIONS ===");
            System.out.println("1. Delete User");
            System.out.println("0. Back to Main Menu");
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1 -> {
                    System.out.println("⚠️ Warning: This will delete the user record!");
                    int userID = getPositiveIntInput("Enter User ID to delete: ");
                    String confirm = getStringInput("Are you sure? (y/n): ");
                    if (confirm.equalsIgnoreCase("y")) {
                        db.deleteUser(userID);
                    } else {
                        System.out.println("Delete operation cancelled.");
                    }
                }
                case 0 -> { return; }
                default -> System.out.println("Invalid choice! Please try again.");
            }
        }
    }

    private static void advancedQueries() {
        while (true) {
            System.out.println("\n=== ADVANCED QUERIES ===");
            System.out.println("1. Total Spending Per User (Aggregate: SUM)");
            System.out.println("2. Users Above Average Spending (Subquery + Aggregate: AVG)");
            System.out.println("3. All Users With Transactions (LEFT JOIN)");
            System.out.println("4. All Sellers With Games (RIGHT JOIN)");
            System.out.println("0. Back to Main Menu");
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1 -> db.getTotalSpendingPerUser();
                case 2 -> db.getUsersAboveAverageSpending();
                case 3 -> db.getAllUsersWithTransactions();
                case 4 -> db.getAllSellersWithGames();
                case 0 -> { return; }
                default -> System.out.println("Invalid choice! Please try again.");
            }
        }
    }

    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        try {
            return scanner.nextLine();
        } catch (NoSuchElementException e) {
            exitOnEndOfInput();
            return null; // unreachable; exitOnEndOfInput terminates
        }
    }

    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int input = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                return input;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Consume invalid input
            } catch (NoSuchElementException e) {
                exitOnEndOfInput();
            }
        }
    }

    private static double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                double input = scanner.nextDouble();
                scanner.nextLine(); // Consume newline
                return input;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Consume invalid input
            } catch (NoSuchElementException e) {
                exitOnEndOfInput();
            }
        }
    }

    private static void exitOnEndOfInput() {
        System.out.println("\nNo more input. Exiting the system. Goodbye!");
        System.exit(0);
    }

    private static String getNonEmptyStringInput(String prompt) {
        while (true) {
            String value = getStringInput(prompt).trim();
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("Input cannot be empty. Please try again.");
        }
    }

    private static String getUserTypeInput(String prompt) {
        while (true) {
            String value = getStringInput(prompt).trim();
            if (value.equalsIgnoreCase("Admin")) {
                return "Admin";
            }
            if (value.equalsIgnoreCase("Guest")) {
                return "Guest";
            }
            System.out.println("User Type must be 'Admin' or 'Guest'.");
        }
    }

    private static int getPositiveIntInput(String prompt) {
        while (true) {
            int value = getIntInput(prompt);
            if (value > 0) {
                return value;
            }
            System.out.println("Value must be a positive number.");
        }
    }

    private static double getNonNegativeDoubleInput(String prompt) {
        while (true) {
            double value = getDoubleInput(prompt);
            if (value >= 0) {
                return value;
            }
            System.out.println("Value cannot be negative.");
        }
    }

    private static String getDateInput(String prompt) {
        while (true) {
            String value = getStringInput(prompt).trim();
            try {
                LocalDate.parse(value);
                return value;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date. Please use format YYYY-MM-DD.");
            }
        }
    }
}