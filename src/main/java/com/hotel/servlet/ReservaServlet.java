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
     * Crear una nueva reserva
     */
    private void crearReserva(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        try {
            // Obtener parámetros
            int clienteId = Integer.parseInt(request.getParameter("clienteId"));
            int habitacionId = Integer.parseInt(request.getParameter("habitacionId"));
            Date fechaEntrada = Date.valueOf(request.getParameter("fechaEntrada"));
            Date fechaSalida = Date.valueOf(request.getParameter("fechaSalida"));
            int numeroPersonas = Integer.parseInt(request.getParameter("numeroPersonas"));
            double anticipo = Double.parseDouble(request.getParameter("anticipo"));
            double total = Double.parseDouble(request.getParameter("total"));
            
            // Validar fechas
            if (fechaSalida.before(fechaEntrada)) {
                response.sendRedirect("reservas.jsp?error=La fecha de salida debe ser posterior a la de entrada");
                return;
            }
            
            // Crear objeto Reserva
            Reserva nuevaReserva = new Reserva();
            nuevaReserva.setClienteId(clienteId);
            nuevaReserva.setHabitacionId(habitacionId);
            nuevaReserva.setFechaEntrada(fechaEntrada);
            nuevaReserva.setFechaSalida(fechaSalida);
            nuevaReserva.setNumeroPersonas(numeroPersonas);
            nuevaReserva.setAnticipo(anticipo);
            nuevaReserva.setTotal(total);
            nuevaReserva.setEstado("Pendiente");
            
            // Guardar en base de datos
            boolean creada = reservaDAO.crearReserva(nuevaReserva);
            
            if (creada) {
                response.sendRedirect("reservas.jsp?mensaje=Reserva creada exitosamente. ID: " + nuevaReserva.getId());
            } else {
                response.sendRedirect("reservas.jsp?error=Error al crear la reserva");
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect("reservas.jsp?error=Datos inválidos: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            response.sendRedirect("reservas.jsp?error=Formato de fecha inválido");
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