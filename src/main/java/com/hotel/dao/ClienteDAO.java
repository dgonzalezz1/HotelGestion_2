package com.hotel.dao;

import com.hotel.modelo.Cliente;
import com.hotel.util.ConexionDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase DAO para gestionar operaciones CRUD de Cliente
 * Data Access Object - Patrón de diseño
 */
public class ClienteDAO {
    
    /**
     * Registrar un nuevo cliente en la base de datos
     */
    public boolean registrarCliente(Cliente cliente) {
        String sql = "INSERT INTO clientes (nombre, apellido, dpi, telefono, email, direccion) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pst.setString(1, cliente.getNombre());
            pst.setString(2, cliente.getApellido());
            pst.setString(3, cliente.getDpi());
            pst.setString(4, cliente.getTelefono());
            pst.setString(5, cliente.getEmail());
            pst.setString(6, cliente.getDireccion());
            
            int filasAfectadas = pst.executeUpdate();
            
            // Obtener el ID generado
            if (filasAfectadas > 0) {
                ResultSet rs = pst.getGeneratedKeys();
                if (rs.next()) {
                    cliente.setId(rs.getInt(1));
                }
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al registrar cliente: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Obtener todos los clientes
     */
    public List<Cliente> listarClientes() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM clientes ORDER BY id DESC";
        
        try (Connection conn = ConexionDB.getConexion();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("id"));
                cliente.setNombre(rs.getString("nombre"));
                cliente.setApellido(rs.getString("apellido"));
                cliente.setDpi(rs.getString("dpi"));
                cliente.setTelefono(rs.getString("telefono"));
                cliente.setEmail(rs.getString("email"));
                cliente.setDireccion(rs.getString("direccion"));
                cliente.setFechaRegistro(rs.getTimestamp("fecha_registro"));
                
                clientes.add(cliente);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al listar clientes: " + e.getMessage());
            e.printStackTrace();
        }
        return clientes;
    }
    
    /**
     * Buscar cliente por ID
     */
    public Cliente buscarPorId(int id) {
        String sql = "SELECT * FROM clientes WHERE id = ?";
        
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("id"));
                cliente.setNombre(rs.getString("nombre"));
                cliente.setApellido(rs.getString("apellido"));
                cliente.setDpi(rs.getString("dpi"));
                cliente.setTelefono(rs.getString("telefono"));
                cliente.setEmail(rs.getString("email"));
                cliente.setDireccion(rs.getString("direccion"));
                cliente.setFechaRegistro(rs.getTimestamp("fecha_registro"));
                return cliente;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar cliente: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Buscar cliente por DPI
     */
    public Cliente buscarPorDPI(String dpi) {
        String sql = "SELECT * FROM clientes WHERE dpi = ?";
        
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setString(1, dpi);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("id"));
                cliente.setNombre(rs.getString("nombre"));
                cliente.setApellido(rs.getString("apellido"));
                cliente.setDpi(rs.getString("dpi"));
                cliente.setTelefono(rs.getString("telefono"));
                cliente.setEmail(rs.getString("email"));
                cliente.setDireccion(rs.getString("direccion"));
                cliente.setFechaRegistro(rs.getTimestamp("fecha_registro"));
                return cliente;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar cliente por DPI: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Actualizar datos de un cliente
     */
    public boolean actualizarCliente(Cliente cliente) {
        String sql = "UPDATE clientes SET nombre=?, apellido=?, telefono=?, email=?, direccion=? " +
                     "WHERE id=?";
        
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setString(1, cliente.getNombre());
            pst.setString(2, cliente.getApellido());
            pst.setString(3, cliente.getTelefono());
            pst.setString(4, cliente.getEmail());
            pst.setString(5, cliente.getDireccion());
            pst.setInt(6, cliente.getId());
            
            return pst.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar cliente: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Buscar clientes por nombre o apellido
     */
    public List<Cliente> buscarPorNombre(String busqueda) {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM clientes WHERE nombre LIKE ? OR apellido LIKE ?";
        
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            String patron = "%" + busqueda + "%";
            pst.setString(1, patron);
            pst.setString(2, patron);
            
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("id"));
                cliente.setNombre(rs.getString("nombre"));
                cliente.setApellido(rs.getString("apellido"));
                cliente.setDpi(rs.getString("dpi"));
                cliente.setTelefono(rs.getString("telefono"));
                cliente.setEmail(rs.getString("email"));
                cliente.setDireccion(rs.getString("direccion"));
                cliente.setFechaRegistro(rs.getTimestamp("fecha_registro"));
                
                clientes.add(cliente);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar clientes: " + e.getMessage());
            e.printStackTrace();
        }
        return clientes;
    }
}