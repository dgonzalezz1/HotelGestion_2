package com.hotel.dao;

import com.hotel.modelo.Reserva;
import com.hotel.modelo.Habitacion;
import com.hotel.util.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para gestionar operaciones de Reservas
 * VERSIÓN COMPATIBLE con columna 'total' (en lugar de 'total_estimado')
 */
public class ReservaDAO {
    
    /**
     * Crear una nueva reserva usando la columna 'total'
     */
    public boolean crearReserva(Reserva reserva) {
        System.out.println("\n=== INICIANDO CREACIÓN DE RESERVA ===");
        
        // PASO 1: Obtener información de la habitación
        HabitacionDAO habDao = new HabitacionDAO();
        Habitacion habitacion = habDao.buscarPorId(reserva.getHabitacionId());
        
        if (habitacion == null) {
            System.err.println("❌ Error: Habitación no encontrada con ID: " + reserva.getHabitacionId());
            return false;
        }
        
        System.out.println("✅ Habitación encontrada: " + habitacion.getNumero() + " - Q" + habitacion.getPrecioNoche());
        
        // PASO 2: Calcular total
        long diffInMillies = Math.abs(reserva.getFechaSalida().getTime() - reserva.getFechaEntrada().getTime());
        long dias = diffInMillies / (1000 * 60 * 60 * 24);
        if (dias == 0) dias = 1;
        
        double totalCalculado = habitacion.getPrecioNoche() * dias;
        System.out.println("Cálculo: " + dias + " días x Q" + habitacion.getPrecioNoche() + " = Q" + totalCalculado);
        
        // PASO 3: INSERT usando la columna 'total'
        String sql = "INSERT INTO reservas (cliente_id, habitacion_id, fecha_entrada, " +
                     "fecha_salida, numero_personas, estado, total) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConexionDB.getConexion();
            
            if (conn == null) {
                System.err.println("❌ Error: No se pudo obtener conexión");
                return false;
            }
            
            System.out.println("✅ Conexión obtenida para INSERT");
            
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            pstmt.setInt(1, reserva.getClienteId());
            pstmt.setInt(2, reserva.getHabitacionId());
            pstmt.setDate(3, reserva.getFechaEntrada());
            pstmt.setDate(4, reserva.getFechaSalida());
            pstmt.setInt(5, reserva.getNumeroPersonas());
            pstmt.setString(6, reserva.getEstado());
            pstmt.setDouble(7, totalCalculado); // Usando 'total' en lugar de 'total_estimado'
            
            System.out.println("Ejecutando INSERT...");
            
            int filasAfectadas = pstmt.executeUpdate();
            System.out.println("Filas afectadas: " + filasAfectadas);
            
            if (filasAfectadas > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    int idGenerado = rs.getInt(1);
                    reserva.setId(idGenerado);
                    System.out.println("✅ ID generado: " + idGenerado);
                }
                
                System.out.println("Actualizando estado de habitación...");
                boolean cambioEstado = habDao.cambiarEstado(reserva.getHabitacionId(), "Reservada");
                
                if (cambioEstado) {
                    System.out.println("✅ Habitación actualizada a 'Reservada'");
                } else {
                    System.err.println("⚠️  Advertencia: No se pudo actualizar el estado");
                }
                
                System.out.println("✅ ¡RESERVA CREADA EXITOSAMENTE!");
                System.out.println("   Total guardado: Q" + totalCalculado);
                return true;
                
            } else {
                System.err.println("❌ Error: No se insertó ninguna fila");
                return false;
            }
            
        } catch (SQLException e) {
            System.err.println("❌ ERROR SQL:");
            System.err.println("   Código: " + e.getErrorCode());
            System.err.println("   Mensaje: " + e.getMessage());
            e.printStackTrace();
            return false;
            
        } catch (Exception e) {
            System.err.println("❌ ERROR GENERAL: " + e.getMessage());
            e.printStackTrace();
            return false;
            
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
                System.out.println("Conexión cerrada correctamente");
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos: " + e.getMessage());
            }
        }
    }
    
    /**
     * Listar TODAS las reservas
     */
    public List<Reserva> listarReservas() {
        List<Reserva> reservas = new ArrayList<>();
        String sql = "SELECT * FROM reservas ORDER BY fecha_entrada DESC";
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConexionDB.getConexion();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                Reserva reserva = new Reserva();
                reserva.setId(rs.getInt("id"));
                reserva.setClienteId(rs.getInt("cliente_id"));
                reserva.setHabitacionId(rs.getInt("habitacion_id"));
                reserva.setFechaEntrada(rs.getDate("fecha_entrada"));
                reserva.setFechaSalida(rs.getDate("fecha_salida"));
                reserva.setNumeroPersonas(rs.getInt("numero_personas"));
                reserva.setEstado(rs.getString("estado"));
                reservas.add(reserva);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al listar todas las reservas: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos: " + e.getMessage());
            }
        }
        
        return reservas;
    }
    
    public List<Reserva> listarReservasPendientes() {
        List<Reserva> reservas = new ArrayList<>();
        String sql = "SELECT * FROM reservas WHERE estado = 'Pendiente' ORDER BY fecha_entrada";
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConexionDB.getConexion();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                Reserva reserva = new Reserva();
                reserva.setId(rs.getInt("id"));
                reserva.setClienteId(rs.getInt("cliente_id"));
                reserva.setHabitacionId(rs.getInt("habitacion_id"));
                reserva.setFechaEntrada(rs.getDate("fecha_entrada"));
                reserva.setFechaSalida(rs.getDate("fecha_salida"));
                reserva.setNumeroPersonas(rs.getInt("numero_personas"));
                reserva.setEstado(rs.getString("estado"));
                reservas.add(reserva);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al listar reservas pendientes: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos: " + e.getMessage());
            }
        }
        
        return reservas;
    }
    
    public List<Reserva> listarReservasActivas() {
        List<Reserva> reservas = new ArrayList<>();
        String sql = "SELECT * FROM reservas WHERE estado = 'Check-in' ORDER BY fecha_entrada";
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConexionDB.getConexion();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                Reserva reserva = new Reserva();
                reserva.setId(rs.getInt("id"));
                reserva.setClienteId(rs.getInt("cliente_id"));
                reserva.setHabitacionId(rs.getInt("habitacion_id"));
                reserva.setFechaEntrada(rs.getDate("fecha_entrada"));
                reserva.setFechaSalida(rs.getDate("fecha_salida"));
                reserva.setNumeroPersonas(rs.getInt("numero_personas"));
                reserva.setEstado(rs.getString("estado"));
                reservas.add(reserva);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al listar reservas activas: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos: " + e.getMessage());
            }
        }
        
        return reservas;
    }
    
    public Reserva obtenerReserva(int id) {
        String sql = "SELECT * FROM reservas WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConexionDB.getConexion();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Reserva reserva = new Reserva();
                reserva.setId(rs.getInt("id"));
                reserva.setClienteId(rs.getInt("cliente_id"));
                reserva.setHabitacionId(rs.getInt("habitacion_id"));
                reserva.setFechaEntrada(rs.getDate("fecha_entrada"));
                reserva.setFechaSalida(rs.getDate("fecha_salida"));
                reserva.setNumeroPersonas(rs.getInt("numero_personas"));
                reserva.setEstado(rs.getString("estado"));
                return reserva;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener reserva: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos: " + e.getMessage());
            }
        }
        
        return null;
    }
    
    public boolean hacerCheckIn(int reservaId) {
        String sql = "UPDATE reservas SET estado = 'Check-in' WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = ConexionDB.getConexion();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, reservaId);
            int filasAfectadas = pstmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                Reserva reserva = obtenerReserva(reservaId);
                if (reserva != null) {
                    HabitacionDAO habDao = new HabitacionDAO();
                    habDao.ocuparHabitacion(reserva.getHabitacionId());
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error al hacer check-in: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos: " + e.getMessage());
            }
        }
        return false;
    }
    
    public boolean hacerCheckOut(int reservaId) {
        String sql = "UPDATE reservas SET estado = 'Finalizada' WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = ConexionDB.getConexion();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, reservaId);
            int filasAfectadas = pstmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                Reserva reserva = obtenerReserva(reservaId);
                if (reserva != null) {
                    HabitacionDAO habDao = new HabitacionDAO();
                    habDao.liberarHabitacion(reserva.getHabitacionId());
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error al hacer check-out: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos: " + e.getMessage());
            }
        }
        return false;
    }
    
    public boolean agregarPago(int reservaId, double monto) {
        String sql = "INSERT INTO pagos (reserva_id, monto, fecha_pago) VALUES (?, ?, NOW())";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = ConexionDB.getConexion();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, reservaId);
            pstmt.setDouble(2, monto);
            
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println("Error al agregar pago: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos: " + e.getMessage());
            }
        }
    }
    
    public double obtenerTotalPagado(int reservaId) {
        String sql = "SELECT COALESCE(SUM(monto), 0) as total FROM pagos WHERE reserva_id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConexionDB.getConexion();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, reservaId);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener total pagado: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos: " + e.getMessage());
            }
        }
        return 0.0;
    }
    
    public boolean cancelarReserva(int reservaId) {
        String sql = "UPDATE reservas SET estado = 'Cancelada' WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = ConexionDB.getConexion();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, reservaId);
            int filasAfectadas = pstmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                Reserva reserva = obtenerReserva(reservaId);
                if (reserva != null) {
                    HabitacionDAO habDao = new HabitacionDAO();
                    habDao.liberarHabitacion(reserva.getHabitacionId());
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error al cancelar reserva: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos: " + e.getMessage());
            }
        }
        return false;
    }
}