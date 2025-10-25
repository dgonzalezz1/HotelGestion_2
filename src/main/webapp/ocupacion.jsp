<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.hotel.dao.*" %>
<%@ page import="com.hotel.modelo.*" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Control de Ocupación</title>
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
        
        .btn-danger {
            background: #dc3545;
            color: white;
        }
        
        .btn:hover {
            opacity: 0.9;
        }
        
        .section {
            background: white;
            padding: 25px;
            border-radius: 10px;
            margin-bottom: 20px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
        }
        
        .stats-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 20px;
            margin-bottom: 30px;
        }
        
        .stat-card {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 20px;
            border-radius: 10px;
            text-align: center;
        }
        
        .stat-number {
            font-size: 3em;
            font-weight: bold;
            margin: 10px 0;
        }
        
        .stat-label {
            font-size: 1em;
            opacity: 0.9;
        }
        
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 15px;
        }
        
        th {
            background: #667eea;
            color: white;
            padding: 12px;
            text-align: left;
        }
        
        td {
            padding: 12px;
            border-bottom: 1px solid #ddd;
        }
        
        tr:hover {
            background: #f8f9fa;
        }
        
        .alert {
            padding: 15px;
            border-radius: 5px;
            margin-bottom: 20px;
        }
        
        .alert-success {
            background: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }
        
        .alert-error {
            background: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }
        
        .estado-badge {
            padding: 5px 10px;
            border-radius: 15px;
            font-size: 0.85em;
            font-weight: bold;
        }
        
        .estado-pendiente {
            background: #fff3cd;
            color: #856404;
        }
        
        .estado-checkin {
            background: #d4edda;
            color: #155724;
        }
        
        .estado-checkout {
            background: #d1ecf1;
            color: #0c5460;
        }
        
        .acciones {
            display: flex;
            gap: 5px;
        }
        
        .modal {
            display: none;
            position: fixed;
            z-index: 1000;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0,0,0,0.5);
        }
        
        .modal-content {
            background-color: white;
            margin: 10% auto;
            padding: 30px;
            border-radius: 10px;
            width: 90%;
            max-width: 500px;
        }
        
        .close {
            color: #aaa;
            float: right;
            font-size: 28px;
            font-weight: bold;
            cursor: pointer;
        }
        
        .close:hover {
            color: black;
        }
        
        .form-group {
            margin-bottom: 15px;
        }
        
        label {
            display: block;
            margin-bottom: 5px;
            font-weight: 600;
        }
        
        input {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 5px;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>📊 Control de Ocupación</h1>
            <a href="index.jsp" class="btn btn-secondary">← Volver al Inicio</a>
        </div>
        
        <% 
            String mensaje = request.getParameter("mensaje");
            String error = request.getParameter("error");
            
            if (mensaje != null) {
        %>
            <div class="alert alert-success"><%= mensaje %></div>
        <% } %>
        
        <% if (error != null) { %>
            <div class="alert alert-error"><%= error %></div>
        <% } %>
        
        <!-- Estadísticas -->
        <%
            HabitacionDAO habitacionDAO = new HabitacionDAO();
            ReservaDAO reservaDAO = new ReservaDAO();
            
            List<Habitacion> todasHabitaciones = habitacionDAO.listarHabitaciones();
            List<Habitacion> disponibles = habitacionDAO.listarDisponibles();
            List<Reserva> reservasActivas = reservaDAO.listarReservasActivas();
            
            int totalHab = todasHabitaciones.size();
            int habDisponibles = disponibles.size();
            int habOcupadas = totalHab - habDisponibles;
            int totalReservas = reservaDAO.listarReservas().size();
        %>
        
        <div class="stats-grid">
            <div class="stat-card">
                <div class="stat-label">Total Habitaciones</div>
                <div class="stat-number"><%= totalHab %></div>
            </div>
            
            <div class="stat-card">
                <div class="stat-label">Disponibles</div>
                <div class="stat-number"><%= habDisponibles %></div>
            </div>
            
            <div class="stat-card">
                <div class="stat-label">Ocupadas</div>
                <div class="stat-number"><%= habOcupadas %></div>
            </div>
            
            <div class="stat-card">
                <div class="stat-label">Check-in Activos</div>
                <div class="stat-number"><%= reservasActivas.size() %></div>
            </div>
        </div>
        
        <!-- Reservas Pendientes de Check-in -->
        <div class="section">
            <h2>⏳ Reservas Pendientes de Check-in</h2>
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Cliente</th>
                        <th>Habitación</th>
                        <th>Entrada</th>
                        <th>Salida</th>
                        <th>Total</th>
                        <th>Anticipo</th>
                        <th>Saldo</th>
                        <th>Estado</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        ClienteDAO clienteDAO = new ClienteDAO();
                        List<Reserva> reservas = reservaDAO.listarReservas();
                        boolean hayPendientes = false;
                        
                        for (Reserva r : reservas) {
                            if ("Pendiente".equals(r.getEstado()) || "Confirmada".equals(r.getEstado())) {
                                hayPendientes = true;
                                Cliente cliente = clienteDAO.buscarPorId(r.getClienteId());
                                Habitacion habitacion = habitacionDAO.buscarPorId(r.getHabitacionId());
                                double saldo = r.getTotal() - r.getAnticipo();
                    %>
                        <tr>
                            <td><%= r.getId() %></td>
                            <td><%= cliente != null ? cliente.getNombreCompleto() : "N/A" %></td>
                            <td><%= habitacion != null ? habitacion.getNumero() : "N/A" %></td>
                            <td><%= r.getFechaEntrada() %></td>
                            <td><%= r.getFechaSalida() %></td>
                            <td>Q <%= String.format("%.2f", r.getTotal()) %></td>
                            <td>Q <%= String.format("%.2f", r.getAnticipo()) %></td>
                            <td>Q <%= String.format("%.2f", saldo) %></td>
                            <td><span class="estado-badge estado-pendiente"><%= r.getEstado() %></span></td>
                            <td>
                                <div class="acciones">
                                    <form action="ReservaServlet" method="post" style="margin: 0;">
                                        <input type="hidden" name="accion" value="checkin">
                                        <input type="hidden" name="reservaId" value="<%= r.getId() %>">
                                        <button type="submit" class="btn btn-success">Check-in</button>
                                    </form>
                                </div>
                            </td>
                        </tr>
                    <%
                            }
                        }
                        
                        if (!hayPendientes) {
                    %>
                        <tr>
                            <td colspan="10" style="text-align: center; color: #666;">
                                No hay reservas pendientes de check-in
                            </td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
        
        <!-- Habitaciones Ocupadas (Check-in) -->
        <div class="section">
            <h2>🏨 Habitaciones Ocupadas (Check-in Activo)</h2>
            <table>
                <thead>
                    <tr>
                        <th>ID Reserva</th>
                        <th>Cliente</th>
                        <th>Habitación</th>
                        <th>Entrada</th>
                        <th>Salida</th>
                        <th>Total</th>
                        <th>Pagado</th>
                        <th>Saldo</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        if (reservasActivas.isEmpty()) {
                    %>
                        <tr>
                            <td colspan="9" style="text-align: center; color: #666;">
                                No hay habitaciones ocupadas actualmente
                            </td>
                        </tr>
                    <%
                        } else {
                            for (Reserva r : reservasActivas) {
                                Cliente cliente = clienteDAO.buscarPorId(r.getClienteId());
                                Habitacion habitacion = habitacionDAO.buscarPorId(r.getHabitacionId());
                                double saldo = r.getTotal() - r.getAnticipo();
                    %>
                        <tr>
                            <td><%= r.getId() %></td>
                            <td><%= cliente != null ? cliente.getNombreCompleto() : "N/A" %></td>
                            <td><%= habitacion != null ? habitacion.getNumero() : "N/A" %></td>
                            <td><%= r.getFechaEntrada() %></td>
                            <td><%= r.getFechaSalida() %></td>
                            <td>Q <%= String.format("%.2f", r.getTotal()) %></td>
                            <td>Q <%= String.format("%.2f", r.getAnticipo()) %></td>
                            <td style="<%= saldo > 0 ? "color: red; font-weight: bold;" : "color: green;" %>">
                                Q <%= String.format("%.2f", saldo) %>
                            </td>
                            <td>
                                <div class="acciones">
                                    <button onclick="abrirModalPago(<%= r.getId() %>, <%= saldo %>)" 
                                            class="btn btn-primary">Pagar</button>
                                    <form action="ReservaServlet" method="post" style="margin: 0;">
                                        <input type="hidden" name="accion" value="checkout">
                                        <input type="hidden" name="reservaId" value="<%= r.getId() %>">
                                        <button type="submit" class="btn btn-danger" 
                                                onclick="return confirm('¿Confirmar check-out?')">Check-out</button>
                                    </form>
                                </div>
                            </td>
                        </tr>
                    <%
                            }
                        }
                    %>
                </tbody>
            </table>
        </div>
    </div>
    
    <!-- Modal para agregar pago -->
    <div id="modalPago" class="modal">
        <div class="modal-content">
            <span class="close" onclick="cerrarModal()">&times;</span>
            <h2>Agregar Pago</h2>
            <form action="ReservaServlet" method="post">
                <input type="hidden" name="accion" value="agregarPago">
                <input type="hidden" id="reservaIdPago" name="reservaId">
                
                <div class="form-group">
                    <label>Saldo Pendiente: Q <span id="saldoPendiente">0.00</span></label>
                </div>
                
                <div class="form-group">
                    <label for="monto">Monto a Pagar*</label>
                    <input type="number" id="monto" name="monto" step="0.01" min="0" required>
                </div>
                
                <button type="submit" class="btn btn-success">Procesar Pago</button>
            </form>
        </div>
    </div>
    
    <script>
        function abrirModalPago(reservaId, saldo) {
            document.getElementById('modalPago').style.display = 'block';
            document.getElementById('reservaIdPago').value = reservaId;
            document.getElementById('saldoPendiente').textContent = saldo.toFixed(2);
            document.getElementById('monto').value = saldo.toFixed(2);
            document.getElementById('monto').max = saldo.toFixed(2);
        }
        
        function cerrarModal() {
            document.getElementById('modalPago').style.display = 'none';
        }
        
        window.onclick = function(event) {
            var modal = document.getElementById('modalPago');
            if (event.target === modal) {
                modal.style.display = 'none';
            }
        };
    </script>
</body>
</html>
