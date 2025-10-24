package com.hotel.dao;

import com.hotel.modelo.Reserva;
import com.hotel.util.ConexionDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase DAO para gestionar operaciones CRUD de Reservas
 */
public class ReservaDAO {
    
    /**
     * Crear una nueva reserva
     */
    public boolean crearReserva(Reserva reserva) {
        String sql = "INSERT INTO reservas (cliente_id, habitacion_id, fecha_entrada, fecha_salida, " +
                     "numero_personas, anticipo, total, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        Connection conn = null;
        try {
            conn = ConexionDB.getConexion();
            conn.setAutoCommit(false); // Iniciar transacción
            
            PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, reserva.getClienteId());
            pst.setInt(2, reserva.getHabitacionId());
            pst.setDate(3, reserva.getFechaEntrada());
            pst.setDate(4, reserva.getFechaSalida());
            pst.setInt(5, reserva.getNumeroPersonas());
            pst.setDouble(6, reserva.getAnticipo());
            pst.setDouble(7, reserva.getTotal());
            pst.setString(8, reserva.getEstado());
            
            int filasAfectadas = pst.executeUpdate();
            
            if (filasAfectadas > 0) {
                ResultSet rs = pst.getGeneratedKeys();
                if (rs.next()) {
                    reserva.setId(rs.getInt(1));
                }
                
                // Cambiar estado de la habitación a Ocupada
                HabitacionDAO habDAO = new HabitacionDAO();
                habDAO.ocuparHabitacion(reserva.getHabitacionId());
                
                conn.commit(); // Confirmar transacción
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al crear reserva: " + e.getMessage());
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback(); // Revertir cambios
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    
    /**
     * Listar todas las reservas
     */
    public List<Reserva> listarReservas() {
        List<Reserva> reservas = new ArrayList<>();
        String sql = "SELECT * FROM reservas ORDER BY fecha_reserva DESC";
        
        try (Connection conn = ConexionDB.getConexion();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                Reserva reserva = mapearReserva(rs);
                reservas.add(reserva);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al listar reservas: " + e.getMessage());
            e.printStackTrace();
        }
        return reservas;
    }
    
    /**
     * Buscar reserva por ID
     */
    public Reserva buscarPorId(int id) {
        String sql = "SELECT * FROM reservas WHERE id = ?";
        
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                return mapearReserva(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar reserva: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Buscar reservas por cliente
     */
    public List<Reserva> buscarPorCliente(int clienteId) {
        List<Reserva> reservas = new ArrayList<>();
        String sql = "SELECT * FROM reservas WHERE cliente_id = ? ORDER BY fecha_reserva DESC";
        
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setInt(1, clienteId);
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                Reserva reserva = mapearReserva(rs);
                reservas.add(reserva);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar reservas por cliente: " + e.getMessage());
            e.printStackTrace();
        }
        return reservas;
    }
    
    /**
     * Cambiar estado de una reserva
     */
    public boolean cambiarEstado(int id, String nuevoEstado) {
        String sql = "UPDATE reservas SET estado = ? WHERE id = ?";
        
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setString(1, nuevoEstado);
            pst.setInt(2, id);
            
            return pst.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al cambiar estado de reserva: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Realizar check-in
     */
    public boolean hacerCheckIn(int reservaId) {
        return cambiarEstado(reservaId, "Check-in");
    }
    
    /**
     * Realizar check-out
     */
    public boolean hacerCheckOut(int reservaId) {
        Connection conn = null;
        try {
            conn = ConexionDB.getConexion();
            conn.setAutoCommit(false);
            
            // Cambiar estado de la reserva
            if (cambiarEstado(reservaId, "Check-out")) {
                // Obtener la reserva para liberar la habitación
                Reserva reserva = buscarPorId(reservaId);
                if (reserva != null) {
                    HabitacionDAO habDAO = new HabitacionDAO();
                    habDAO.liberarHabitacion(reserva.getHabitacionId());
                }
                
                conn.commit();
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al hacer check-out: " + e.getMessage());
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    
    /**
     * Agregar pago a una reserva
     */
    public boolean agregarPago(int reservaId, double monto) {
        String sql = "UPDATE reservas SET anticipo = anticipo + ? WHERE id = ?";
        
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setDouble(1, monto);
            pst.setInt(2, reservaId);
            
            return pst.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al agregar pago: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Obtener reservas activas (Check-in)
     */
    public List<Reserva> obtenerReservasActivas() {
        List<Reserva> reservas = new ArrayList<>();
        String sql = "SELECT * FROM reservas WHERE estado = 'Check-in' ORDER BY fecha_entrada";
        
        try (Connection conn = ConexionDB.getConexion();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                Reserva reserva = mapearReserva(rs);
                reservas.add(reserva);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener reservas activas: " + e.getMessage());
            e.printStackTrace();
        }
        return reservas;
    }
    
    /**
     * Método auxiliar para mapear ResultSet a objeto Reserva
     */
    private Reserva mapearReserva(ResultSet rs) throws SQLException {
        Reserva reserva = new Reserva();
        reserva.setId(rs.getInt("id"));
        reserva.setClienteId(rs.getInt("cliente_id"));
        reserva.setHabitacionId(rs.getInt("habitacion_id"));
        reserva.setFechaEntrada(rs.getDate("fecha_entrada"));
        reserva.setFechaSalida(rs.getDate("fecha_salida"));
        reserva.setNumeroPersonas(rs.getInt("numero_personas"));
        reserva.setAnticipo(rs.getDouble("anticipo"));
        reserva.setTotal(rs.getDouble("total"));
        reserva.setEstado(rs.getString("estado"));
        reserva.setFechaReserva(rs.getTimestamp("fecha_reserva"));
        return reserva;
    }
}