package com.hotel.dao;

import com.hotel.modelo.Habitacion;
import com.hotel.util.ConexionDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase DAO para gestionar operaciones CRUD de Habitación
 */
public class HabitacionDAO {
    
    /**
     * Listar todas las habitaciones
     */
    public List<Habitacion> listarHabitaciones() {
        List<Habitacion> habitaciones = new ArrayList<>();
        String sql = "SELECT * FROM habitaciones ORDER BY numero";
        
        try (Connection conn = ConexionDB.getConexion();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                Habitacion hab = new Habitacion();
                hab.setId(rs.getInt("id"));
                hab.setNumero(rs.getString("numero"));
                hab.setTipo(rs.getString("tipo"));
                hab.setPrecioNoche(rs.getDouble("precio_noche"));
                hab.setCapacidad(rs.getInt("capacidad"));
                hab.setEstado(rs.getString("estado"));
                hab.setDescripcion(rs.getString("descripcion"));
                
                habitaciones.add(hab);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al listar habitaciones: " + e.getMessage());
            e.printStackTrace();
        }
        return habitaciones;
    }
    
    /**
     * Listar habitaciones disponibles
     */
    public List<Habitacion> listarDisponibles() {
        List<Habitacion> habitaciones = new ArrayList<>();
        String sql = "SELECT * FROM habitaciones WHERE estado = 'Disponible' ORDER BY numero";
        
        try (Connection conn = ConexionDB.getConexion();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                Habitacion hab = new Habitacion();
                hab.setId(rs.getInt("id"));
                hab.setNumero(rs.getString("numero"));
                hab.setTipo(rs.getString("tipo"));
                hab.setPrecioNoche(rs.getDouble("precio_noche"));
                hab.setCapacidad(rs.getInt("capacidad"));
                hab.setEstado(rs.getString("estado"));
                hab.setDescripcion(rs.getString("descripcion"));
                
                habitaciones.add(hab);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al listar habitaciones disponibles: " + e.getMessage());
            e.printStackTrace();
        }
        return habitaciones;
    }
    
    /**
     * Buscar habitación por ID
     */
    public Habitacion buscarPorId(int id) {
        String sql = "SELECT * FROM habitaciones WHERE id = ?";
        
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                Habitacion hab = new Habitacion();
                hab.setId(rs.getInt("id"));
                hab.setNumero(rs.getString("numero"));
                hab.setTipo(rs.getString("tipo"));
                hab.setPrecioNoche(rs.getDouble("precio_noche"));
                hab.setCapacidad(rs.getInt("capacidad"));
                hab.setEstado(rs.getString("estado"));
                hab.setDescripcion(rs.getString("descripcion"));
                return hab;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar habitación: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Cambiar estado de una habitación
     */
    public boolean cambiarEstado(int id, String nuevoEstado) {
        String sql = "UPDATE habitaciones SET estado = ? WHERE id = ?";
        
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setString(1, nuevoEstado);
            pst.setInt(2, id);
            
            return pst.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al cambiar estado de habitación: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Bloquear habitación
     */
    public boolean bloquearHabitacion(int id) {
        return cambiarEstado(id, "Bloqueada");
    }
    
    /**
     * Desbloquear habitación
     */
    public boolean desbloquearHabitacion(int id) {
        return cambiarEstado(id, "Disponible");
    }
    
    /**
     * Marcar habitación como ocupada
     */
    public boolean ocuparHabitacion(int id) {
        return cambiarEstado(id, "Ocupada");
    }
    
    /**
     * Liberar habitación
     */
    public boolean liberarHabitacion(int id) {
        return cambiarEstado(id, "Disponible");
    }
    
    /**
     * Registrar una nueva habitación
     */
    public boolean registrarHabitacion(Habitacion habitacion) {
        String sql = "INSERT INTO habitaciones (numero, tipo, precio_noche, capacidad, estado, descripcion) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setString(1, habitacion.getNumero());
            pst.setString(2, habitacion.getTipo());
            pst.setDouble(3, habitacion.getPrecioNoche());
            pst.setInt(4, habitacion.getCapacidad());
            pst.setString(5, "Disponible");
            pst.setString(6, habitacion.getDescripcion());
            
            return pst.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al registrar habitación: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Buscar habitaciones por tipo
     */
    public List<Habitacion> buscarPorTipo(String tipo) {
        List<Habitacion> habitaciones = new ArrayList<>();
        String sql = "SELECT * FROM habitaciones WHERE tipo = ? AND estado = 'Disponible'";
        
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setString(1, tipo);
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                Habitacion hab = new Habitacion();
                hab.setId(rs.getInt("id"));
                hab.setNumero(rs.getString("numero"));
                hab.setTipo(rs.getString("tipo"));
                hab.setPrecioNoche(rs.getDouble("precio_noche"));
                hab.setCapacidad(rs.getInt("capacidad"));
                hab.setEstado(rs.getString("estado"));
                hab.setDescripcion(rs.getString("descripcion"));
                
                habitaciones.add(hab);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar habitaciones por tipo: " + e.getMessage());
            e.printStackTrace();
        }
        return habitaciones;
    }
}