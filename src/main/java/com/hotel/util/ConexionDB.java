package com.hotel.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase para gestionar la conexión a la base de datos
 * Patrón Singleton para tener una única instancia de conexión
 */
public class ConexionDB {
    
    // Datos de conexión - MODIFICA ESTOS VALORES SEGÚN TU CONFIGURACIÓN
    private static final String URL = "jdbc:mysql://localhost:3306/hotel_gestion";
    private static final String USUARIO = "root";
    private static final String PASSWORD = ""; // Por defecto XAMPP no tiene password
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    
    private static Connection conexion = null;
    
    /**
     * Constructor privado para evitar instanciación
     */
    private ConexionDB() {
    }
    
    /**
     * Método para obtener la conexión a la base de datos
     * @return Connection objeto de conexión
     */
    public static Connection getConexion() {
        try {
            // Cargar el driver de MySQL
            if (conexion == null || conexion.isClosed()) {
                Class.forName(DRIVER);
                conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
                System.out.println("Conexión exitosa a la base de datos");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Error: Driver no encontrado");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Error al conectar con la base de datos");
            e.printStackTrace();
        }
        return conexion;
    }
    
    /**
     * Método para cerrar la conexión
     */
    public static void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("Conexión cerrada");
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión");
            e.printStackTrace();
        }
    }
    
    /**
     * Método de prueba de conexión
     */
    public static boolean probarConexion() {
        try {
            Connection conn = getConexion();
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}
