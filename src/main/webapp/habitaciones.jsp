<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.hotel.dao.HabitacionDAO" %>
<%@ page import="com.hotel.modelo.Habitacion" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestión de Habitaciones</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: #f0f2f5;
            padding: 20px;
        }
        
        .container {
            max-width: 1400px;
            margin: 0 auto;
        }
        
        .header {
            background: white;
            padding: 20px;
            border-radius: 10px;
            margin-bottom: 20px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        
        h1 {
            color: #667eea;
        }
        
        .btn {
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 14px;
            text-decoration: none;
            display: inline-block;
            transition: all 0.3s;
        }
        
        .btn-primary {
            background: #667eea;
            color: white;
        }
        
        .btn-secondary {
            background: #6c757d;
            color: white;
        }
        
        .btn-success {
            background: #28a745;
            color: white;
        }
        
        .btn-warning {
            background: #ffc107;
            color: #333;
        }
        
        .btn-danger {
            background: #dc3545;
            color: white;
        }
        
        .btn:hover {
            opacity: 0.9;
        }
        
        .habitaciones-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
            gap: 20px;
            margin-top: 20px;
        }
        
        .habitacion-card {
            background: white;
            border-radius: 10px;
            padding: 20px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
            transition: transform 0.3s;
        }
        
        .habitacion-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 5px 15px rgba(0,0,0,0.2);
        }
        
        .habitacion-numero {
            font-size: 2em;
            font-weight: bold;
            color: #667eea;
            margin-bottom: 10px;
        }
        
        .habitacion-tipo {
            font-size: 1.2em;
            color: #333;
            margin-bottom: 10px;
        }
        
        .habitacion-precio {
            font-size: 1.5em;
            color: #28a745;
            margin: 15px 0;
        }
        
        .habitacion-info {
            color: #666;
            margin: 5px 0;
        }
        
        .estado-badge {
            display: inline-block;
            padding: 5px 15px;
            border-radius: 20px;
            font-weight: bold;
            margin: 10px 0;
        }
        
        .estado-disponible {
            background: #d4edda;
            color: #155724;
        }
        
        .estado-ocupada {
            background: #f8d7da;
            color: #721c24;
        }
        
        .estado-bloqueada {
            background: #fff3cd;
            color: #856404;
        }
        
        .actions {
            margin-top: 15px;
            display: flex;
            gap: 10px;
        }
        
        .filter-section {
            background: white;
            padding: 20px;
            border-radius: 10px;
            margin-bottom: 20px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
        }
        
        .filter-buttons {
            display: flex;
            gap: 10px;
            flex-wrap: wrap;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>🛏️ Gestión de Habitaciones</h1>
            <a href="index.jsp" class="btn btn-secondary">← Volver al Inicio</a>
        </div>
        
        <!-- Filtros -->
        <div class="filter-section">
            <h3>Filtrar Habitaciones</h3>
            <div class="filter-buttons">
                <a href="habitaciones.jsp" class="btn btn-primary">Todas</a>
                <a href="habitaciones.jsp?filtro=disponible" class="btn btn-success">Disponibles</a>
                <a href="habitaciones.jsp?filtro=ocupada" class="btn btn-danger">Ocupadas</a>
                <a href="habitaciones.jsp?filtro=bloqueada" class="btn btn-warning">Bloqueadas</a>
            </div>
        </div>
        
        <!-- Grid de Habitaciones -->
        <div class="habitaciones-grid">
            <%
                HabitacionDAO habitacionDAO = new HabitacionDAO();
                String filtro = request.getParameter("filtro");
                List<Habitacion> habitaciones;
                
                if ("disponible".equals(filtro)) {
                    habitaciones = habitacionDAO.listarDisponibles();
                } else if (filtro != null) {
                    // Filtrar por estado específico
                    habitaciones = habitacionDAO.listarHabitaciones();
                    habitaciones.removeIf(h -> !h.getEstado().equalsIgnoreCase(filtro));
                } else {
                    habitaciones = habitacionDAO.listarHabitaciones();
                }
                
                if (habitaciones.isEmpty()) {
            %>
                <div style="grid-column: 1/-1; text-align: center; padding: 40px; color: #666;">
                    No hay habitaciones que mostrar
                </div>
            <%
                } else {
                    for (Habitacion hab : habitaciones) {
                        String estadoClass = "";
                        if ("Disponible".equals(hab.getEstado())) {
                            estadoClass = "estado-disponible";
                        } else if ("Ocupada".equals(hab.getEstado())) {
                            estadoClass = "estado-ocupada";
                        } else if ("Bloqueada".equals(hab.getEstado())) {
                            estadoClass = "estado-bloqueada";
                        }
            %>
                <div class="habitacion-card">
                    <div class="habitacion-numero">Hab. <%= hab.getNumero() %></div>
                    <div class="habitacion-tipo"><%= hab.getTipo() %></div>
                    <div class="habitacion-precio">Q <%= String.format("%.2f", hab.getPrecioNoche()) %> / noche</div>
                    
                    <div class="habitacion-info">
                        👥 Capacidad: <%= hab.getCapacidad() %> personas
                    </div>
                    
                    <div class="habitacion-info">
                        📝 <%= hab.getDescripcion() %>
                    </div>
                    
                    <span class="estado-badge <%= estadoClass %>">
                        <%= hab.getEstado() %>
                    </span>
                    
                    <div class="actions">
                        <% if ("Disponible".equals(hab.getEstado())) { %>
                            <form action="HabitacionServlet" method="post" style="margin: 0;">
                                <input type="hidden" name="accion" value="bloquear">
                                <input type="hidden" name="id" value="<%= hab.getId() %>">
                                <button type="submit" class="btn btn-warning">Bloquear</button>
                            </form>
                        <% } %>
                        
                        <% if ("Bloqueada".equals(hab.getEstado())) { %>
                            <form action="HabitacionServlet" method="post" style="margin: 0;">
                                <input type="hidden" name="accion" value="desbloquear">
                                <input type="hidden" name="id" value="<%= hab.getId() %>">
                                <button type="submit" class="btn btn-success">Desbloquear</button>
                            </form>
                        <% } %>
                    </div>
                </div>
            <%
                    }
                }
            %>
        </div>
    </div>
</body>
</html>
