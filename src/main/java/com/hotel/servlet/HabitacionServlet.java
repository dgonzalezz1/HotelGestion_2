package com.hotel.servlet;

import com.hotel.dao.HabitacionDAO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet para gestionar operaciones de Habitaciones
 */
@WebServlet("/HabitacionServlet")
public class HabitacionServlet extends HttpServlet {
    
    private HabitacionDAO habitacionDAO;
    
    @Override
    public void init() throws ServletException {
        habitacionDAO = new HabitacionDAO();
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        String accion = request.getParameter("accion");
        
        try {
            if ("bloquear".equals(accion)) {
                bloquearHabitacion(request, response);
            } else if ("desbloquear".equals(accion)) {
                desbloquearHabitacion(request, response);
            }
        } catch (Exception e) {
            System.err.println("Error en HabitacionServlet: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect("habitaciones.jsp?error=" + e.getMessage());
        }
    }
    
    /**
     * Bloquear una habitación
     */
    private void bloquearHabitacion(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        int id = Integer.parseInt(request.getParameter("id"));
        boolean bloqueada = habitacionDAO.bloquearHabitacion(id);
        
        if (bloqueada) {
            response.sendRedirect("habitaciones.jsp?mensaje=Habitación bloqueada correctamente");
        } else {
            response.sendRedirect("habitaciones.jsp?error=No se pudo bloquear la habitación");
        }
    }
    
    /**
     * Desbloquear una habitación
     */
    private void desbloquearHabitacion(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        int id = Integer.parseInt(request.getParameter("id"));
        boolean desbloqueada = habitacionDAO.desbloquearHabitacion(id);
        
        if (desbloqueada) {
            response.sendRedirect("habitaciones.jsp?mensaje=Habitación desbloqueada correctamente");
        } else {
            response.sendRedirect("habitaciones.jsp?error=No se pudo desbloquear la habitación");
        }
    }
}