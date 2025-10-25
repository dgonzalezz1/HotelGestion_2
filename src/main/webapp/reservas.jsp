<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.hotel.dao.*" %>
<%@ page import="com.hotel.modelo.*" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Nueva Reserva</title>
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
            max-width: 1200px;
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
        }
        
        .btn-primary {
            background: #667eea;
            color: white;
        }
        
        .btn-secondary {
            background: #6c757d;
            color: white;
        }
        
        .form-container {
            background: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
            margin-bottom: 20px;
        }
        
        .form-group {
            margin-bottom: 20px;
        }
        
        label {
            display: block;
            margin-bottom: 5px;
            font-weight: 600;
            color: #333;
        }
        
        input, select {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: 14px;
        }
        
        input:focus, select:focus {
            outline: none;
            border-color: #667eea;
        }
        
        .form-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 15px;
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
        
        .reservas-list {
            background: white;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
            margin-top: 20px;
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
        
        .estado-confirmada {
            background: #d1ecf1;
            color: #0c5460;
        }
        
        .estado-checkin {
            background: #d4edda;
            color: #155724;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>📅 Gestión de Reservas</h1>
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
        
        <form action="ReservaServlet" method="post" onsubmit="return validarFormulario()">
    <input type="hidden" name="accion" value="crear">
    
    <div class="form-group">
        <label for="clienteId">Cliente*</label>
        <select id="clienteId" name="clienteId" required>
            <option value="">Seleccione un cliente</option>
            <%
                ClienteDAO clienteDAO = new ClienteDAO();
                List<Cliente> clientes = clienteDAO.listarClientes();
                for (Cliente c : clientes) {
            %>
                <option value="<%= c.getId() %>">
                    <%= c.getNombreCompleto() %> - DPI: <%= c.getDpi() %>
                </option>
            <% } %>
        </select>
    </div>
    
    <div class="form-group">
        <label for="habitacionId">Habitación*</label>
        <select id="habitacionId" name="habitacionId" required>
            <option value="">Seleccione una habitación</option>
            <%
                HabitacionDAO habitacionDAO = new HabitacionDAO();
                List<Habitacion> habitaciones = habitacionDAO.listarDisponibles();
                for (Habitacion h : habitaciones) {
            %>
                <option value="<%= h.getId() %>" data-precio="<%= h.getPrecioNoche() %>">
                    Hab. <%= h.getNumero() %> - <%= h.getTipo() %> - Q <%= h.getPrecioNoche() %>/noche
                </option>
            <% } %>
        </select>
    </div>
    
    <div class="form-group">
        <label for="fechaEntrada">Fecha de Entrada*</label>
        <input type="date" id="fechaEntrada" name="fechaEntrada" required 
               min="<%= java.time.LocalDate.now() %>"
               onchange="calcularTotal()">
    </div>
    
    <div class="form-group">
        <label for="fechaSalida">Fecha de Salida*</label>
        <input type="date" id="fechaSalida" name="fechaSalida" required 
               min="<%= java.time.LocalDate.now().plusDays(1) %>"
               onchange="calcularTotal()">
    </div>
    
    <div class="form-group">
        <label for="numPersonas">Número de Personas*</label>
        <input type="number" id="numPersonas" name="numPersonas" 
               min="1" max="10" value="1" required>
    </div>
    
    <div class="form-group">
        <label for="anticipo">Anticipo (Q)</label>
        <input type="number" id="anticipo" name="anticipo" 
               min="0" step="0.01" value="0">
    </div>
    
    <div class="form-group">
        <label>Total Estimado:</label>
        <h3 id="totalEstimado" style="color: #2563eb;">Q 0.00</h3>
    </div>
    
    <button type="submit" class="btn btn-primary">Crear Reserva</button>
</form>

<script>
function validarFormulario() {
    const fechaEntrada = document.getElementById('fechaEntrada').value;
    const fechaSalida = document.getElementById('fechaSalida').value;
    
    if (!fechaEntrada || !fechaSalida) {
        alert('Por favor seleccione las fechas de entrada y salida');
        return false;
    }
    
    if (fechaEntrada >= fechaSalida) {
        alert('La fecha de salida debe ser posterior a la fecha de entrada');
        return false;
    }
    
    return true;
}

function calcularTotal() {
    const habitacionSelect = document.getElementById('habitacionId');
    const fechaEntrada = document.getElementById('fechaEntrada').value;
    const fechaSalida = document.getElementById('fechaSalida').value;
    
    if (!habitacionSelect.value || !fechaEntrada || !fechaSalida) return;
    
    const precio = parseFloat(habitacionSelect.options[habitacionSelect.selectedIndex].dataset.precio);
    
    const fecha1 = new Date(fechaEntrada);
    const fecha2 = new Date(fechaSalida);
    const noches = Math.ceil((fecha2 - fecha1) / (1000 * 60 * 60 * 24));
    
    if (noches > 0) {
        const total = precio * noches;
        document.getElementById('totalEstimado').textContent = 'Q ' + total.toFixed(2);
    }
}
</script>
        
        <!-- Lista de Reservas Recientes -->
        <div class="reservas-list">
            <h2>Reservas Recientes</h2>
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Cliente</th>
                        <th>Habitación</th>
                        <th>Entrada</th>
                        <th>Salida</th>
                        <th>Personas</th>
                        <th>Total</th>
                        <th>Anticipo</th>
                        <th>Saldo</th>
                        <th>Estado</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        ReservaDAO reservaDAO = new ReservaDAO();
                        List<Reserva> reservas = reservaDAO.listarReservas();
                        
                        if (reservas.isEmpty()) {
                    %>
                        <tr>
                            <td colspan="10" style="text-align: center; color: #666;">
                                No hay reservas registradas
                            </td>
                        </tr>
                    <%
                        } else {
                            for (Reserva r : reservas) {
                                Cliente cliente = clienteDAO.buscarPorId(r.getClienteId());
                                Habitacion habitacion = habitacionDAO.buscarPorId(r.getHabitacionId());
                                double saldo = r.getTotal() - r.getAnticipo();
                                
                                String estadoClass = "";
                                if ("Pendiente".equals(r.getEstado())) {
                                    estadoClass = "estado-pendiente";
                                } else if ("Confirmada".equals(r.getEstado())) {
                                    estadoClass = "estado-confirmada";
                                } else if ("Check-in".equals(r.getEstado())) {
                                    estadoClass = "estado-checkin";
                                }
                    %>
                        <tr>
                            <td><%= r.getId() %></td>
                            <td><%= cliente != null ? cliente.getNombreCompleto() : "N/A" %></td>
                            <td><%= habitacion != null ? habitacion.getNumero() : "N/A" %></td>
                            <td><%= r.getFechaEntrada() %></td>
                            <td><%= r.getFechaSalida() %></td>
                            <td><%= r.getNumeroPersonas() %></td>
                            <td>Q <%= String.format("%.2f", r.getTotal()) %></td>
                            <td>Q <%= String.format("%.2f", r.getAnticipo()) %></td>
                            <td>Q <%= String.format("%.2f", saldo) %></td>
                            <td><span class="estado-badge <%= estadoClass %>"><%= r.getEstado() %></span></td>
                        </tr>
                    <%
                            }
                        }
                    %>
                </tbody>
            </table>
        </div>
    </div>
    
    <script>
        // Calcular el total automáticamente
        function calcularTotal() {
            var fechaEntrada = document.getElementById('fechaEntrada').value;
            var fechaSalida = document.getElementById('fechaSalida').value;
            var habitacionSelect = document.getElementById('habitacionId');
            var selectedOption = habitacionSelect.options[habitacionSelect.selectedIndex];
            
            if (fechaEntrada && fechaSalida && selectedOption.value) {
                var precio = parseFloat(selectedOption.getAttribute('data-precio'));
                var entrada = new Date(fechaEntrada);
                var salida = new Date(fechaSalida);
                var diferencia = salida - entrada;
                var noches = Math.ceil(diferencia / (1000 * 60 * 60 * 24));
                
                if (noches > 0) {
                    var total = precio * noches;
                    document.getElementById('totalEstimado').textContent = 'Q ' + total.toFixed(2);
                    document.getElementById('total').value = total.toFixed(2);
                } else {
                    document.getElementById('totalEstimado').textContent = 'Q 0.00';
                    document.getElementById('total').value = '0';
                }
            }
        }
        
        function actualizarPrecio() {
            calcularTotal();
        }
        
        // Establecer fecha mínima como hoy
        var today = new Date().toISOString().split('T')[0];
        document.getElementById('fechaEntrada').setAttribute('min', today);
        document.getElementById('fechaSalida').setAttribute('min', today);
    </script>
</body>
</html>
