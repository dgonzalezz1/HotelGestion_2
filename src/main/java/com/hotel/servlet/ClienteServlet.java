package com.hotel.servlet;

import com.hotel.dao.ClienteDAO;
import com.hotel.modelo.Cliente;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet para gestionar operaciones de Cliente
 */
@WebServlet("/ClienteServlet")
public class ClienteServlet extends HttpServlet {
    
    private ClienteDAO clienteDAO;
    
    @Override
    public void init() throws ServletException {
        clienteDAO = new ClienteDAO();
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Configurar codificación UTF-8
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        String accion = request.getParameter("accion");
        
        if ("registrar".equals(accion)) {
            registrarCliente(request, response);
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String accion = request.getParameter("accion");
        
        if ("buscar".equals(accion)) {
            buscarCliente(request, response);
        }
    }
    
    /**
     * Método para registrar un nuevo cliente
     */
    private void registrarCliente(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        try {
            // Obtener parámetros del formulario
            String nombre = request.getParameter("nombre");
            String apellido = request.getParameter("apellido");
            String dpi = request.getParameter("dpi");
            String telefono = request.getParameter("telefono");
            String email = request.getParameter("email");
            String direccion = request.getParameter("direccion");
            
            // Validar que los campos obligatorios no estén vacíos
            if (nombre == null || nombre.trim().isEmpty() ||
                apellido == null || apellido.trim().isEmpty() ||
                dpi == null || dpi.trim().isEmpty() ||
                telefono == null || telefono.trim().isEmpty()) {
                
                response.sendRedirect("clientes.jsp?error=Todos los campos obligatorios deben estar completos");
                return;
            }
            
            // Verificar si el DPI ya existe
            Cliente clienteExistente = clienteDAO.buscarPorDPI(dpi);
            if (clienteExistente != null) {
                response.sendRedirect("clientes.jsp?error=El DPI ya está registrado en el sistema");
                return;
            }
            
            // Crear objeto Cliente
            Cliente nuevoCliente = new Cliente(nombre, apellido, dpi, telefono, email, direccion);
            
            // Registrar en la base de datos
            boolean registrado = clienteDAO.registrarCliente(nuevoCliente);
            
            if (registrado) {
                response.sendRedirect("clientes.jsp?mensaje=Cliente registrado exitosamente");
            } else {
                response.sendRedirect("clientes.jsp?error=Error al registrar el cliente");
            }
            
        } catch (Exception e) {
            System.err.println("Error en registrarCliente: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect("clientes.jsp?error=Error del sistema: " + e.getMessage());
        }
    }
    
    /**
     * Método para buscar un cliente
     */
    private void buscarCliente(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String dpi = request.getParameter("dpi");
        Cliente cliente = clienteDAO.buscarPorDPI(dpi);
        
        request.setAttribute("cliente", cliente);
        request.getRequestDispatcher("resultadoBusqueda.jsp").forward(request, response);
    }
}