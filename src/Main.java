import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    private static final DatabaseManager db = new DatabaseManager();
    private static Scanner scanner;

    public static void main(String[] args) {
        System.out.println("=====================");
        System.out.println("E-Games: Digital Game Marketplace");
        System.out.println("=====================");

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
        String userType = getStringInput("Enter User Type (Admin/Guest): ");
        String username = getStringInput("Enter Username: ");
        String email = getStringInput("Enter Email: ");
        String password = getStringInput("Enter Password: ");
        
        db.addUser(userType, username, email, password);
    }

    private static void addSeller() {
        System.out.println("\n--- Add Seller ---");
        String sellerName = getStringInput("Enter Seller Name: ");
        String contactInfo = getStringInput("Enter Contact Info: ");
        
        db.addSeller(sellerName, contactInfo);
    }

    private static void addGame() {
        System.out.println("\n--- Add Game ---");
        String gameName = getStringInput("Enter Game Name: ");
        int sellerID = getIntInput("Enter Seller ID: ");
        String category = getStringInput("Enter Category: ");
        double price = getDoubleInput("Enter Price: ");
        String developer = getStringInput("Enter Developer: ");
        int yearPublished = getIntInput("Enter Year Published: ");
        
        db.addGame(gameName, sellerID, category, price, developer, yearPublished);
    }

    private static void addTransaction() {
        System.out.println("\n--- Add Transaction ---");
        int userID = getIntInput("Enter User ID: ");
        int gameID = getIntInput("Enter Game ID: ");
        String purchaseDate = getStringInput("Enter Purchase Date (YYYY-MM-DD): ");
        double totalAmount = getDoubleInput("Enter Total Amount: ");
        
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
                    int userID = getIntInput("Enter User ID to update: ");
                    String newEmail = getStringInput("Enter New Email: ");
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
                    int userID = getIntInput("Enter User ID to delete: ");
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
        return scanner.nextLine();
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
            }
        }
    }
}