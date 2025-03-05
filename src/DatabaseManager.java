import java.sql.*;

public class DatabaseManager {

    private static final String URL = "jdbc:sqlite:database.db";

    public DatabaseManager() {
        loadDriver();
        createDatabaseIfNotExists();
        if (isDatabaseEmpty()) {
            createSchema();
            insertSampleData();
        }
    }

    private void loadDriver() {
        try {
            Class.forName("org.sqlite.JDBC");
            System.out.println("✅ SQLite JDBC Driver loaded successfully!");
        } catch (ClassNotFoundException e) {
            System.out.println("❌ SQLite JDBC Driver not found!");
            System.err.println("Error loading SQLite JDBC Driver: " + e.getMessage());
        }
    }

    private void createDatabaseIfNotExists() {
        try (Connection conn = DriverManager.getConnection(URL)) {
            if (conn != null) {
                System.out.println("✅ Database file created or already exists!");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error creating database: " + e.getMessage());
        }
    }

    private boolean isDatabaseEmpty() {
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM sqlite_master WHERE type='table' AND name='users';")) {
            return !rs.next() || rs.getInt(1) == 0;
        } catch (SQLException e) {
            return true; 
        }
    }

    private void createSchema() {
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {

            stmt.execute("PRAGMA foreign_keys = ON;");

            // Create tables according to the documentation schema
            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                         "UserID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                         "UserType VARCHAR(20) NOT NULL, " +
                         "Username VARCHAR(50) NOT NULL UNIQUE, " +
                         "Email VARCHAR(100) UNIQUE, " +
                         "Password VARCHAR(100) NOT NULL);");

            stmt.execute("CREATE TABLE IF NOT EXISTS sellerinfo (" +
                         "SellerID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                         "SellerName VARCHAR(100), " +
                         "ContactInfo VARCHAR(100), " +
                         "GamesSold INTEGER UNSIGNED DEFAULT 0);");

            stmt.execute("CREATE TABLE IF NOT EXISTS gamesinfo (" +
                         "GameID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                         "GameName VARCHAR(100) NOT NULL, " +
                         "SellerID INTEGER, " +
                         "Category VARCHAR(50), " + 
                         "Price DECIMAL(10,2), " +
                         "Developer VARCHAR(100), " +
                         "YearPublished INTEGER, " +
                         "FOREIGN KEY (SellerID) REFERENCES sellerinfo(SellerID));");

            stmt.execute("CREATE TABLE IF NOT EXISTS playstoretransaction (" +
                         "TransactionID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                         "UserID INTEGER, " +
                         "GameID INTEGER, " +
                         "PurchaseDate TEXT, " +
                         "TotalAmount REAL, " +
                         "FOREIGN KEY (UserID) REFERENCES users(UserID) ON DELETE CASCADE, " +
                         "FOREIGN KEY (GameID) REFERENCES gamesinfo(GameID) ON DELETE CASCADE);");

            System.out.println("✅ Database tables created successfully!");
        } catch (SQLException e) {
            System.out.println("❌ Error creating tables: " + e.getMessage());
        }
    }

    private void insertSampleData() {
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            // Insert sample users
            stmt.executeUpdate("INSERT INTO users (UserType, Username, Email, Password) VALUES " +
                    "('Admin', 'GameSeller1', 'seller1@example.com', 'securepassword'), " +
                    "('Guest', 'Customer1', 'customer1@example.com', 'securepassword');");

            // Insert sample sellers
            stmt.executeUpdate("INSERT INTO sellerinfo (SellerName, ContactInfo) VALUES " +
                    "('Juan', '0123643'), " +
                    "('Bert', '0546686');");

            // Insert sample games
            stmt.executeUpdate("INSERT INTO gamesinfo (GameName, SellerID, Category, Price, Developer, YearPublished) VALUES " +
                    "('Cyber Adventure', 1, 'Action', 49.99, 'Riot', 2020), " +
                    "('Valorant', 1, 'FPS', 2000, 'Riot', 2020);");

            // Insert sample transactions
            stmt.executeUpdate("INSERT INTO playstoretransaction (UserID, GameID, PurchaseDate, TotalAmount) VALUES " +
                    "(2, 1, '2021-01-01', 49.99), " +
                    "(2, 2, '2021-02-01', 2000);");

            System.out.println("✅ Sample data inserted!");
        } catch (SQLException e) {
            System.out.println("❌ Error inserting data: " + e.getMessage());
        }
    }

    public void addUser(String userType, String username, String email, String password) {
        String sql = "INSERT INTO users (UserType, Username, Email, Password) VALUES (?, ?, ?, ?);";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userType);
            pstmt.setString(2, username);
            pstmt.setString(3, email);
            pstmt.setString(4, password);
            pstmt.executeUpdate();
            System.out.println("✅ User added successfully!");
        } catch (SQLException e) {
            System.out.println("❌ Error adding user: " + e.getMessage());
        }
    }

    public void addSeller(String sellerName, String contactInfo) {
        String sql = "INSERT INTO sellerinfo (SellerName, ContactInfo) VALUES (?, ?);";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, sellerName);
            pstmt.setString(2, contactInfo);
            pstmt.executeUpdate();
            System.out.println("✅ Seller added successfully!");
        } catch (SQLException e) {
            System.out.println("❌ Error adding seller: " + e.getMessage());
        }
    }

    public void addGame(String gameName, int sellerID, String category, double price, String developer, int yearPublished) {
        String sql = "INSERT INTO gamesinfo (GameName, SellerID, Category, Price, Developer, YearPublished) VALUES (?, ?, ?, ?, ?, ?);";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, gameName);
            pstmt.setInt(2, sellerID);
            pstmt.setString(3, category);
            pstmt.setDouble(4, price);
            pstmt.setString(5, developer);
            pstmt.setInt(6, yearPublished);
            pstmt.executeUpdate();
            System.out.println("✅ Game added successfully!");
        } catch (SQLException e) {
            System.out.println("❌ Error adding game: " + e.getMessage());
        }
    }

    public void addTransaction(int userID, int gameID, String purchaseDate, double totalAmount) {
        String sql = "INSERT INTO playstoretransaction (UserID, GameID, PurchaseDate, TotalAmount) VALUES (?, ?, ?, ?);";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userID);
            pstmt.setInt(2, gameID);
            pstmt.setString(3, purchaseDate);
            pstmt.setDouble(4, totalAmount);
            pstmt.executeUpdate();
            System.out.println("✅ Transaction added successfully!");
        } catch (SQLException e) {
            System.out.println("❌ Error adding transaction: " + e.getMessage());
        }
    }

    public void getUsersSortedByUsername() {
        String sql = "SELECT UserID, Username, Email FROM users ORDER BY Username ASC;";
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("UserID") + 
                                  ", Username: " + rs.getString("Username") +
                                  ", Email: " + rs.getString("Email"));
            }
        } catch (SQLException e) {
            System.out.println("❌ Error retrieving users: " + e.getMessage());
        }
    }

    public void updateUserEmail(int userID, String newEmail) {
        String sql = "UPDATE users SET Email = ? WHERE UserID = ?;";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newEmail);
            pstmt.setInt(2, userID);
            pstmt.executeUpdate();
            System.out.println("✅ User email updated successfully!");
        } catch (SQLException e) {
            System.out.println("❌ Error updating user email: " + e.getMessage());
        }
    }

    public void deleteUser(int userID) {
        String sql = "DELETE FROM users WHERE UserID = ?;";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userID);
            pstmt.executeUpdate();
            System.out.println("✅ User deleted successfully!");
        } catch (SQLException e) {
            System.out.println("❌ Error deleting user: " + e.getMessage());
        }
    }

    public void getTotalSpendingPerUser() {
        String sql = "SELECT u.Username, SUM(t.TotalAmount) AS TotalSpent " +
                    "FROM users u " +
                    "JOIN playstoretransaction t ON u.UserID = t.UserID " +
                    "GROUP BY u.UserID " +
                    "ORDER BY TotalSpent DESC;";
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println("User: " + rs.getString("Username") + 
                                  " | Total Spent: $" + rs.getDouble("TotalSpent"));
            }
        } catch (SQLException e) {
            System.out.println("❌ Error calculating spending: " + e.getMessage());
        }
    }

    public void getGamesByCategory(String category) {
        String sql = "SELECT GameName, Price, Developer, YearPublished " +
                    "FROM gamesinfo " +
                    "WHERE Category = ? " +
                    "ORDER BY Price DESC;";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, category);
            ResultSet rs = pstmt.executeQuery();
            System.out.println("Games in category: " + category);
            while (rs.next()) {
                System.out.println("Game: " + rs.getString("GameName") + 
                                  " | Price: $" + rs.getDouble("Price") +
                                  " | Developer: " + rs.getString("Developer") +
                                  " | Year: " + rs.getInt("YearPublished"));
            }
        } catch (SQLException e) {
            System.out.println("❌ Error retrieving games by category: " + e.getMessage());
        }
    }

    public void getSellersWithGames() {
        String sql = "SELECT s.SellerName, COUNT(g.GameID) as GameCount " +
                    "FROM sellerinfo s " +
                    "JOIN gamesinfo g ON s.SellerID = g.SellerID " +
                    "GROUP BY s.SellerID " +
                    "ORDER BY GameCount DESC;";
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println("Seller: " + rs.getString("SellerName") + 
                                  " | Games Available: " + rs.getInt("GameCount"));
            }
        } catch (SQLException e) {
            System.out.println("❌ Error retrieving sellers: " + e.getMessage());
        }
    }

    public void getUsersAboveAverageSpending() {
        String sql = "SELECT u.Username, SUM(t.TotalAmount) AS TotalSpent " +
                    "FROM users u " +
                    "JOIN playstoretransaction t ON u.UserID = t.UserID " +
                    "GROUP BY u.UserID " +
                    "HAVING TotalSpent > (SELECT AVG(TotalAmount) FROM playstoretransaction);";
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println("User: " + rs.getString("Username") + 
                                  " | Total Spent: $" + rs.getDouble("TotalSpent"));
            }
        } catch (SQLException e) {
            System.out.println("❌ Error retrieving users above average spending: " + e.getMessage());
        }
    }

    public void getAllUsersWithTransactions() {
        String sql = "SELECT u.Username, t.TransactionID, t.PurchaseDate, t.TotalAmount " +
                    "FROM users u " +
                    "LEFT JOIN playstoretransaction t ON u.UserID = t.UserID;";
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println("User: " + rs.getString("Username") + 
                                  " | Transaction ID: " + rs.getInt("TransactionID") +
                                  " | Purchase Date: " + rs.getString("PurchaseDate") +
                                  " | Total Amount: $" + rs.getDouble("TotalAmount"));
            }
        } catch (SQLException e) {
            System.out.println("❌ Error retrieving users with transactions: " + e.getMessage());
        }
    }

    public void getAllSellersWithGames() {
        String sql = "SELECT s.SellerName, g.GameName " +
                    "FROM sellerinfo s " +
                    "RIGHT JOIN gamesinfo g ON s.SellerID = g.SellerID;";
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println("Seller: " + rs.getString("SellerName") + 
                                  " | Game: " + rs.getString("GameName"));
            }
        } catch (SQLException e) {
            System.out.println("❌ Error retrieving sellers with games: " + e.getMessage());
        }
    }
}