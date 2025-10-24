<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.hotel.dao.ClienteDAO" %>
<%@ page import="com.hotel.modelo.Cliente" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestión de Clientes</title>
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
        
        .btn-primary:hover {
            background: #5568d3;
        }
        
        .btn-secondary {
            background: #6c757d;
            color: white;
        }
        
        .form-container {
            background: white;
            padding: 30px;
            border-radius: 10px;
            margin-bottom: 20px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
        }
        
        .form-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 15px;
            margin-bottom: 20px;
        }
        
        .form-group {
            display: flex;
            flex-direction: column;
        }
        
        label {
            margin-bottom: 5px;
            font-weight: 600;
            color: #333;
        }
        
        input {
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: 14px;
        }
        
        input:focus {
            outline: none;
            border-color: #667eea;
        }
        
        .table-container {
            background: white;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
            overflow-x: auto;
        }
        
        table {
            width: 100%;
            border-collapse: collapse;
        }
        
        th {
            background: #667eea;
            color: white;
            padding: 12px;
            text-align: left;
            font-weight: 600;
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
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>👥 Gestión de Clientes</h1>
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
        
        <!-- Formulario de Registro -->
        <div class="form-container">
            <h2>Registrar Nuevo Cliente</h2>
            <form action="ClienteServlet" method="post">
                <input type="hidden" name="accion" value="registrar">
                
                <div class="form-grid">
                    <div class="form-group">
                        <label for="nombre">Nombre*</label>
                        <input type="text" id="nombre" name="nombre" required>
                    </div>
                    
                    <div class="form-group">
                        <label for="apellido">Apellido*</label>
                        <input type="text" id="apellido" name="apellido" required>
                    </div>
                    
                    <div class="form-group">
                        <label for="dpi">DPI*</label>
                        <input type="text" id="dpi" name="dpi" pattern="[0-9]{13}" 
                               title="Ingrese 13 dígitos" required>
                    </div>
                    
                    <div class="form-group">
                        <label for="telefono">Teléfono*</label>
                        <input type="text" id="telefono" name="telefono" required>
                    </div>
                    
                    <div class="form-group">
                        <label for="email">Email</label>
                        <input type="email" id="email" name="email">
                    </div>
                    
                    <div class="form-group">
                        <label for="direccion">Dirección</label>
                        <input type="text" id="direccion" name="direccion">
                    </div>
                </div>
                
                <button type="submit" class="btn btn-primary">Registrar Cliente</button>
            </form>
        </div>
        
        <!-- Tabla de Clientes -->
        <div class="table-container">
            <h2>Lista de Clientes Registrados</h2>
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nombre Completo</th>
                        <th>DPI</th>
                        <th>Teléfono</th>
                        <th>Email</th>
                        <th>Dirección</th>
                        <th>Fecha Registro</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        ClienteDAO clienteDAO = new ClienteDAO();
                        List<Cliente> clientes = clienteDAO.listarClientes();
                        
                        if (clientes.isEmpty()) {
                    %>
                        <tr>
                            <td colspan="7" style="text-align: center; color: #666;">
                                No hay clientes registrados
                            </td>
                        </tr>
                    <%
                        } else {
                            for (Cliente cliente : clientes) {
                    %>
                        <tr>
                            <td><%= cliente.getId() %></td>
                            <td><%= cliente.getNombreCompleto() %></td>
                            <td><%= cliente.getDpi() %></td>
                            <td><%= cliente.getTelefono() %></td>
                            <td><%= cliente.getEmail() != null ? cliente.getEmail() : "-" %></td>
                            <td><%= cliente.getDireccion() != null ? cliente.getDireccion() : "-" %></td>
                            <td><%= cliente.getFechaRegistro() %></td>
                        </tr>
                    <%
                            }
                        }
                    %>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>
