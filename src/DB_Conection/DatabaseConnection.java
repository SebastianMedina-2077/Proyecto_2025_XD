package DB_Conection;

import java.sql.*;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;
    private static boolean primeraConexion = true;

    private final String URL = "jdbc:sqlserver://localhost:1433;database=BD_JUGUETERIA_SIMPLE;encrypt=false";
    private final String USERNAME = "sa";
    private final String PASSWORD = "12345";

    private DatabaseConnection() {
        conectar();
    }

    private void conectar() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            this.connection.setAutoCommit(false);

            if (primeraConexion) {
                System.out.println("Conexion a BD establecida exitosamente");
                primeraConexion = false;
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Error: Driver SQL Server no encontrado");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Error al conectar con la base de datos");
            System.err.println("Verifique que SQL Server este ejecutandose");
            e.printStackTrace();
        }
    }

    public static synchronized DatabaseConnection getInstance() {
        try {
            if (instance == null || instance.connection == null ||
                    instance.connection.isClosed() || !instance.connection.isValid(2)) {
                instance = new DatabaseConnection();
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar conexion: " + e.getMessage());
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed() || !connection.isValid(2)) {
                conectar();
            } else {
                try {
                    if (connection.getAutoCommit()) {
                        connection.setAutoCommit(false);
                    }
                } catch (SQLException e) {
                    System.err.println("Error al configurar autocommit: " + e.getMessage());
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al reconectar: " + e.getMessage());
            conectar();
        }
        return connection;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    if (!connection.getAutoCommit()) {
                        try {
                            connection.commit();
                        } catch (SQLException e) {
                            System.err.println("Error al hacer commit final: " + e.getMessage());
                        }
                    }
                    connection.close();
                    System.out.println("Conexion cerrada correctamente");
                }
            } catch (SQLException e) {
                System.err.println("Error al cerrar conexion: " + e.getMessage());
            }
        }
    }

    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed() && connection.isValid(2);
        } catch (SQLException e) {
            return false;
        }
    }

    public void refreshConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                if (!connection.getAutoCommit()) {
                    connection.commit();
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al refrescar conexion: " + e.getMessage());
        }
    }
}
