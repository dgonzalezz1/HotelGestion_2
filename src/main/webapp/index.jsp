<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sistema de Gestión de Hotel</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 20px;
        }
        
        .container {
            background: white;
            border-radius: 20px;
            box-shadow: 0 20px 60px rgba(0,0,0,0.3);
            padding: 40px;
            max-width: 1200px;
            width: 100%;
        }
        
        h1 {
            color: #667eea;
            text-align: center;
            margin-bottom: 10px;
            font-size: 2.5em;
        }
        
        .subtitle {
            text-align: center;
            color: #666;
            margin-bottom: 40px;
            font-size: 1.1em;
        }
        
        .menu-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 25px;
            margin-top: 30px;
        }
        
        .menu-card {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border-radius: 15px;
            padding: 30px;
            text-align: center;
            cursor: pointer;
            transition: transform 0.3s, box-shadow 0.3s;
            text-decoration: none;
            color: white;
            display: block;
        }
        
        .menu-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 30px rgba(0,0,0,0.3);
        }
        
        .menu-icon {
            font-size: 3em;
            margin-bottom: 15px;
        }
        
        .menu-title {
            font-size: 1.3em;
            font-weight: bold;
            margin-bottom: 10px;
        }
        
        .menu-desc {
            font-size: 0.9em;
            opacity: 0.9;
        }
        
        .footer {
            text-align: center;
            margin-top: 40px;
            color: #666;
            font-size: 0.9em;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>🏨 Sistema de Gestión de Hotel</h1>
        <p class="subtitle">Bienvenido al sistema integral de administración hotelera</p>
        
        <div class="menu-grid">
            <a href="clientes.jsp" class="menu-card">
                <div class="menu-icon">👥</div>
                <div class="menu-title">Clientes</div>
                <div class="menu-desc">Registrar y gestionar clientes</div>
            </a>
            
            <a href="habitaciones.jsp" class="menu-card">
                <div class="menu-icon">🛏️</div>
                <div class="menu-title">Habitaciones</div>
                <div class="menu-desc">Ver y controlar habitaciones</div>
            </a>
            
            <a href="reservas.jsp" class="menu-card">
                <div class="menu-icon">📅</div>
                <div class="menu-title">Reservas</div>
                <div class="menu-desc">Nueva reserva y gestión</div>
            </a>
            
            <a href="ocupacion.jsp" class="menu-card">
                <div class="menu-icon">📊</div>
                <div class="menu-title">Ocupación</div>
                <div class="menu-desc">Estado y check-in/check-out</div>
            </a>
        </div>
        
        <div class="footer">
            <p>Universidad Mariano Gálvez de Guatemala</p>
            <p>Programación II - Proyecto Final 2025</p>
        </div>
    </div>
</body>
</html>
