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
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM sqlite_master WHERE type='table' AND name='Customer';")) {
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
            stmt.execute("CREATE TABLE IF NOT EXISTS Customer (" +
                         "CustomerID INTEGER PRIMARY KEY, " +
                         "Username VARCHAR(50) NOT NULL UNIQUE, " +
                         "Email VARCHAR(100) NOT NULL UNIQUE, " +
                         "Password VARCHAR(100) NOT NULL, " +
                         "FullName VARCHAR(100) NOT NULL, " +
                         "Address VARCHAR(255) NOT NULL);");
                         
            stmt.execute("CREATE TABLE IF NOT EXISTS Seller (" +
                         "SellerID INTEGER PRIMARY KEY, " +
                         "SellerName VARCHAR(100) NOT NULL, " +
                         "ContactNum VARCHAR(20) NOT NULL);");
                         
            stmt.execute("CREATE TABLE IF NOT EXISTS Game (" +
                         "GameID INTEGER PRIMARY KEY, " +
                         "GameName VARCHAR(100) NOT NULL, " +
                         "SellerID INTEGER NOT NULL, " +
                         "Category VARCHAR(50) NOT NULL, " +
                         "Price DECIMAL(10,2) NOT NULL, " +
                         "Developer VARCHAR(100) NOT NULL, " +
                         "YearPublished INTEGER NOT NULL, " +
                         "FOREIGN KEY (SellerID) REFERENCES Seller(SellerID));");
                         
            stmt.execute("CREATE TABLE IF NOT EXISTS Transactions (" +
                         "TransactionID INTEGER PRIMARY KEY, " +
                         "CustomerID INTEGER NOT NULL, " +
                         "SellerID INTEGER NOT NULL, " +
                         "Date DATE NOT NULL, " +
                         "Game VARCHAR(100) NOT NULL, " +
                         "Price DECIMAL(10,2) NOT NULL, " +
                         "FOREIGN KEY (CustomerID) REFERENCES Customer(CustomerID), " +
                         "FOREIGN KEY (SellerID) REFERENCES Seller(SellerID));");
            
            System.out.println("✅ Database tables created successfully!");
        } catch (SQLException e) {
            System.out.println("❌ Error creating tables: " + e.getMessage());
        }
    }

    private void insertSampleData() {
        try (Connection conn = DriverManager.getConnection(URL);
            Statement stmt = conn.createStatement()) {
            // Insert sample customers
            stmt.executeUpdate("INSERT INTO Customer (CustomerID, Username, Email, Password, FullName, Address) VALUES " +
                    "(123, 'qwerty', 'rihecaw952@intady.com', '12365', 'Tristan Berto', 'Sulu'), " +
                    "(432, 'uiop', 'dhjsa@cam123', 'Ghf34', 'Bogart Smith', 'Pasay'), " +
                    "(356, 'yobo', 'bigblokcok@gmail.com', 'Dfsd36f', 'Jhay Mapagmahal', 'Manila'), " +
                    "(236, 'bogart', 'gga@internty.com', 'Dsj42', 'Alberto Ma', '45 Pasig'), " +
                    "(779, 'yuan', 'bbg@gmail.com', 'dsgfds', 'Malou Ang', '897 Manila');");
            
            // Insert sample sellers
            stmt.executeUpdate("INSERT INTO Seller (SellerID, SellerName, ContactNum) VALUES " +
                    "(865, 'Juan', '0123643'), " +
                    "(759, 'Bert', '0546686'), " +
                    "(345, 'Albert', '0145963'), " +
                    "(976, 'Tristan', '0132468'), " +
                    "(004, 'Mj', '0956423');");
            
            // Insert sample games
            stmt.executeUpdate("INSERT INTO Game (GameID, GameName, SellerID, Category, Price, Developer, YearPublished) VALUES " +
                    "(394, 'Valorant', 865, 'FPS', 2000, 'Riot', 2020), " +
                    "(097, 'Ros', 759, 'Survival', 1753, 'NetEase', 2017), " +
                    "(769, 'Pubg', 345, 'Survival', 1985, 'Krafton', 2017), " +
                    "(234, 'Call of duty', 976, 'Moba', 125, 'Activision', 2019), " +
                    "(657, 'Candy Crush', 004, 'Puzzle', 565, 'Microsoft', 2012);");
            
            // Insert sample transactions
            stmt.executeUpdate("INSERT INTO Transactions (TransactionID, CustomerID, SellerID, Date, Game, Price) VALUES " +
                    "(543, 123, 865, '2021', 'Valorant', 2000), " +
                    "(789, 432, 759, '2020', 'Ros', 1753), " +
                    "(453, 356, 345, '2022', 'Pubg', 1985), " +
                    "(869, 236, 976, '2019', 'Call of duty', 125), " +
                    "(078, 779, 004, '2015', 'Candy Crush', 565);");
            System.out.println("✅ Sample data inserted!");
        } catch (SQLException e) {
            System.out.println("❌ Error inserting data: " + e.getMessage());
        }
    }

    public void addCustomer(int customerID, String username, String email, String password, String fullName, String address) {
        String sql = "INSERT INTO Customer (CustomerID, Username, Email, Password, FullName, Address) VALUES (?, ?, ?, ?, ?, ?);";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, customerID);
            pstmt.setString(2, username);
            pstmt.setString(3, email);
            pstmt.setString(4, password);
            pstmt.setString(5, fullName);
            pstmt.setString(6, address);
            pstmt.executeUpdate();
            System.out.println("✅ Customer added successfully!");
        } catch (SQLException e) {
            System.out.println("❌ Error adding customer: " + e.getMessage());
        }
    }

    public void addSeller(int sellerID, String sellerName, String contactNum) {
        String sql = "INSERT INTO Seller (SellerID, SellerName, ContactNum) VALUES (?, ?, ?);";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, sellerID);
            pstmt.setString(2, sellerName);
            pstmt.setString(3, contactNum);
            pstmt.executeUpdate();
            System.out.println("✅ Seller added successfully!");
        } catch (SQLException e) {
            System.out.println("❌ Error adding seller: " + e.getMessage());
        }
    }
    
    public void addGame(int gameID, String gameName, int sellerID, String category, double price, String developer, int yearPublished) {
        String sql = "INSERT INTO Game (GameID, GameName, SellerID, Category, Price, Developer, YearPublished) VALUES (?, ?, ?, ?, ?, ?, ?);";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, gameID);
            pstmt.setString(2, gameName);
            pstmt.setInt(3, sellerID);
            pstmt.setString(4, category);
            pstmt.setDouble(5, price);
            pstmt.setString(6, developer);
            pstmt.setInt(7, yearPublished);
            pstmt.executeUpdate();
            System.out.println("✅ Game added successfully!");
        } catch (SQLException e) {
            System.out.println("❌ Error adding game: " + e.getMessage());
        }
    }

    public void addTransaction(int transactionID, int customerID, int sellerID, String date, String game, double price) {
        String sql = "INSERT INTO Transactions (TransactionID, CustomerID, SellerID, Date, Game, Price) VALUES (?, ?, ?, ?, ?, ?);";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, transactionID);
            pstmt.setInt(2, customerID);
            pstmt.setInt(3, sellerID);
            pstmt.setString(4, date);
            pstmt.setString(5, game);
            pstmt.setDouble(6, price);
            pstmt.executeUpdate();
            System.out.println("✅ Transaction added successfully!");
        } catch (SQLException e) {
            System.out.println("❌ Error adding transaction: " + e.getMessage());
        }
    }

    public void getCustomersSortedByName() {
        String sql = "SELECT CustomerID, FullName, Email, Address FROM Customer ORDER BY FullName ASC;";
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("CustomerID") + 
                                  ", Name: " + rs.getString("FullName") +
                                  ", Email: " + rs.getString("Email") +
                                  ", Address: " + rs.getString("Address"));
            }
        } catch (SQLException e) {
            System.out.println("❌ Error retrieving customers: " + e.getMessage());
        }
    }

    public void updateCustomerEmail(int customerID, String newEmail) {
        String sql = "UPDATE Customer SET Email = ? WHERE CustomerID = ?;";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newEmail);
            pstmt.setInt(2, customerID);
            pstmt.executeUpdate();
            System.out.println("✅ Customer email updated successfully!");
        } catch (SQLException e) {
            System.out.println("❌ Error updating customer email: " + e.getMessage());
        }
    }

    public void deleteCustomer(int customerID) {
        String sql = "DELETE FROM Customer WHERE CustomerID = ?;";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, customerID);
            pstmt.executeUpdate();
            System.out.println("✅ Customer deleted successfully!");
        } catch (SQLException e) {
            System.out.println("❌ Error deleting customer: " + e.getMessage());
        }
    }

    public void getTotalSpendingPerCustomer() {
        String sql = "SELECT c.FullName, SUM(t.Price) AS TotalSpent " +
                    "FROM Customer c " +
                    "JOIN Transactions t ON c.CustomerID = t.CustomerID " +
                    "GROUP BY c.CustomerID " +
                    "ORDER BY TotalSpent DESC;";
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println("Customer: " + rs.getString("FullName") + 
                                  " | Total Spent: $" + rs.getDouble("TotalSpent"));
            }
        } catch (SQLException e) {
            System.out.println("❌ Error calculating spending: " + e.getMessage());
        }
    }
    
    public void getGamesByCategory(String categoryName) {
        String sql = "SELECT g.GameName, g.Price, g.Developer, g.YearPublished " +
                    "FROM Game g " +
                    "WHERE g.Category = ? " +
                    "ORDER BY g.Price DESC;";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, categoryName);
            ResultSet rs = pstmt.executeQuery();
            System.out.println("Games in category: " + categoryName);
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
        String sql = "SELECT s.SellerName, s.ContactNum, COUNT(g.GameID) as GameCount " +
                    "FROM Seller s " +
                    "JOIN Game g ON s.SellerID = g.SellerID " +
                    "GROUP BY s.SellerID " +
                    "ORDER BY GameCount DESC;";
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println("Seller: " + rs.getString("SellerName") + 
                                  " | Contact: " + rs.getString("ContactNum") +
                                  " | Games Available: " + rs.getInt("GameCount"));
            }
        } catch (SQLException e) {
            System.out.println("❌ Error retrieving sellers: " + e.getMessage());
        }
    }

    public void getCustomersAboveAverageSpending() {
        String sql = "SELECT c.FullName, SUM(t.Price) AS TotalSpent " +
                    "FROM Customer c " +
                    "JOIN Transactions t ON c.CustomerID = t.CustomerID " +
                    "GROUP BY c.CustomerID " +
                    "HAVING TotalSpent > (SELECT AVG(Price) FROM Transactions);";
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println("Customer: " + rs.getString("FullName") + 
                                  " | Total Spent: $" + rs.getDouble("TotalSpent"));
            }
        } catch (SQLException e) {
            System.out.println("❌ Error retrieving customers above average spending: " + e.getMessage());
        }
    }

    public void getAllCustomersWithTransactions() {
        String sql = "SELECT c.FullName, t.TransactionID, t.Game, t.Price " +
                    "FROM Customer c " +
                    "LEFT JOIN Transactions t ON c.CustomerID = t.CustomerID;";
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println("Customer: " + rs.getString("FullName") + 
                                  " | Transaction ID: " + rs.getInt("TransactionID") +
                                  " | Game: " + rs.getString("Game") +
                                  " | Price: $" + rs.getDouble("Price"));
            }
        } catch (SQLException e) {
            System.out.println("❌ Error retrieving customers with transactions: " + e.getMessage());
        }
    }

    public void getAllSellersWithGames() {
        String sql = "SELECT s.SellerName, g.GameName " +
                    "FROM Seller s " +
                    "RIGHT JOIN Game g ON s.SellerID = g.SellerID;";
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