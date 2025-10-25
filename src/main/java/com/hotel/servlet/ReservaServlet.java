package com.hotel.servlet;

import com.hotel.dao.ReservaDAO;
import com.hotel.modelo.Reserva;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;

/**
 * Servlet para gestionar operaciones de Reservas
 */
@WebServlet("/ReservaServlet")
public class ReservaServlet extends HttpServlet {
    
    private ReservaDAO reservaDAO;
    
    @Override
    public void init() throws ServletException {
        reservaDAO = new ReservaDAO();
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        String accion = request.getParameter("accion");
        
        try {
            if ("crear".equals(accion)) {
                crearReserva(request, response);
            } else if ("checkin".equals(accion)) {
                realizarCheckIn(request, response);
            } else if ("checkout".equals(accion)) {
                realizarCheckOut(request, response);
            } else if ("agregarPago".equals(accion)) {
                agregarPago(request, response);
            }
        } catch (Exception e) {
            System.err.println("Error en ReservaServlet: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect("reservas.jsp?error=" + e.getMessage());
        }
    }
    
    /**
 * Crear una nueva reserva con manejo de errores mejorado
 */
private void crearReserva(HttpServletRequest request, HttpServletResponse response) 
        throws IOException {
    
    try {
        // Obtener parámetros con validación
        String clienteIdStr = request.getParameter("clienteId");
        String habitacionIdStr = request.getParameter("habitacionId");
        String fechaEntradaStr = request.getParameter("fechaEntrada");
        String fechaSalidaStr = request.getParameter("fechaSalida");
        String numPersonasStr = request.getParameter("numPersonas");
        String anticipoStr = request.getParameter("anticipo");
        
        // Log para debugging
        System.out.println("=== CREANDO RESERVA ===");
        System.out.println("Cliente ID: " + clienteIdStr);
        System.out.println("Habitación ID: " + habitacionIdStr);
        System.out.println("Fecha Entrada: " + fechaEntradaStr);
        System.out.println("Fecha Salida: " + fechaSalidaStr);
        System.out.println("Num Personas: " + numPersonasStr);
        System.out.println("Anticipo: " + anticipoStr);
        
        // Validar que no sean null
        if (clienteIdStr == null || habitacionIdStr == null || 
            fechaEntradaStr == null || fechaSalidaStr == null || 
            numPersonasStr == null) {
            System.err.println("Error: Parámetros faltantes");
            response.sendRedirect("reservas.jsp?error=Por favor complete todos los campos obligatorios");
            return;
        }
        
        // Convertir parámetros
        int clienteId = Integer.parseInt(clienteIdStr);
        int habitacionId = Integer.parseInt(habitacionIdStr);
        int numPersonas = Integer.parseInt(numPersonasStr);
        double anticipo = (anticipoStr != null && !anticipoStr.isEmpty()) ? 
                         Double.parseDouble(anticipoStr) : 0.0;
        
        // IMPORTANTE: Convertir fechas del formato HTML (YYYY-MM-DD) a java.sql.Date
        java.sql.Date fechaEntrada = java.sql.Date.valueOf(fechaEntradaStr);
        java.sql.Date fechaSalida = java.sql.Date.valueOf(fechaSalidaStr);
        
        // Validar que la fecha de entrada sea antes de la de salida
        if (!fechaEntrada.before(fechaSalida)) {
            System.err.println("Error: La fecha de entrada debe ser antes de la fecha de salida");
            response.sendRedirect("reservas.jsp?error=La fecha de entrada debe ser antes de la fecha de salida");
            return;
        }
        
        // Crear objeto Reserva
        Reserva reserva = new Reserva();
        reserva.setClienteId(clienteId);
        reserva.setHabitacionId(habitacionId);
        reserva.setFechaEntrada(fechaEntrada);
        reserva.setFechaSalida(fechaSalida);
        reserva.setNumeroPersonas(numPersonas);
        reserva.setEstado("Pendiente");
        
        System.out.println("Objeto Reserva creado: " + reserva);
        
        // Guardar en base de datos
        boolean exito = reservaDAO.crearReserva(reserva);
        System.out.println("Resultado de guardar: " + exito);
        
        if (exito) {
            // Si hay anticipo, agregarlo como pago
            if (anticipo > 0) {
                reservaDAO.agregarPago(reserva.getId(), anticipo);
                System.out.println("Anticipo agregado: Q" + anticipo);
            }
            
            System.out.println("✅ Reserva creada exitosamente con ID: " + reserva.getId());
            response.sendRedirect("reservas.jsp?mensaje=Reserva creada exitosamente");
        } else {
            System.err.println("❌ Error: No se pudo guardar la reserva en la BD");
            response.sendRedirect("reservas.jsp?error=Error al crear la reserva en la base de datos");
        }
        
    } catch (NumberFormatException e) {
        System.err.println("Error de formato numérico: " + e.getMessage());
        e.printStackTrace();
        response.sendRedirect("reservas.jsp?error=Error: Datos numéricos inválidos");
    } catch (IllegalArgumentException e) {
        System.err.println("Error en formato de fecha: " + e.getMessage());
        e.printStackTrace();
        response.sendRedirect("reservas.jsp?error=Error: Formato de fecha inválido. Use el calendario");
    } catch (Exception e) {
        System.err.println("Error general: " + e.getMessage());
        e.printStackTrace();
        response.sendRedirect("reservas.jsp?error=Error al crear la reserva: " + e.getMessage());
    }
}
    
    /**
     * Realizar check-in
     */
    private void realizarCheckIn(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        int reservaId = Integer.parseInt(request.getParameter("reservaId"));
        boolean exito = reservaDAO.hacerCheckIn(reservaId);
        
        if (exito) {
            response.sendRedirect("ocupacion.jsp?mensaje=Check-in realizado exitosamente");
        } else {
            response.sendRedirect("ocupacion.jsp?error=Error al realizar check-in");
        }
    }
    
    /**
     * Realizar check-out
     */
    private void realizarCheckOut(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        int reservaId = Integer.parseInt(request.getParameter("reservaId"));
        boolean exito = reservaDAO.hacerCheckOut(reservaId);
        
        if (exito) {
            response.sendRedirect("ocupacion.jsp?mensaje=Check-out realizado exitosamente. Habitación liberada");
        } else {
            response.sendRedirect("ocupacion.jsp?error=Error al realizar check-out");
        }
    }
    
    /**
     * Agregar pago a una reserva
     */
    private void agregarPago(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        int reservaId = Integer.parseInt(request.getParameter("reservaId"));
        double monto = Double.parseDouble(request.getParameter("monto"));
        
        boolean exito = reservaDAO.agregarPago(reservaId, monto);
        
        if (exito) {
            response.sendRedirect("ocupacion.jsp?mensaje=Pago agregado exitosamente");
        } else {
            response.sendRedirect("ocupacion.jsp?error=Error al agregar el pago");
        }
    }
}