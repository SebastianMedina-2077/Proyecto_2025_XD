package DB_Conection;

import java.sql.*;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;

    private final String URL = "jdbc:sqlserver://localhost:1433;database=BD_JUGUETERIA_SIMPLE;encrypt=false";
    private final String USERNAME = "sa";
    private final String PASSWORD = "12345";

    // Constructor privado para Singleton
    private DatabaseConnection() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Conexión establecida exitosamente");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error al establecer conexión: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null || !isConnectionValid()) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    private static boolean isConnectionValid() {
        try {
            return instance != null &&
                    instance.connection != null &&
                    !instance.connection.isClosed() &&
                    instance.connection.isValid(5);
        } catch (SQLException e) {
            return false;
        }
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            }
        } catch (SQLException e) {
            System.err.println("Error al reconectar: " + e.getMessage());
        }
        return connection;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Conexión cerrada correctamente");
            } catch (SQLException e) {
                System.err.println("Error al cerrar conexión: " + e.getMessage());
            }
        }
    }
}
