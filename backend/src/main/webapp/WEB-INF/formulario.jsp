<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Registrar empleado - Zao</title>
</head>
<body>
    <h1>Registrar nuevo empleado</h1>

    <form action="/empleados" method="post">
        <label for="nombre">Nombre:</label><br>
        <input type="text" id="nombre" name="nombre" required><br><br>

        <label for="correo">Correo:</label><br>
        <input type="email" id="correo" name="correo" required><br><br>

        <label for="rol">Rol:</label><br>
        <select id="rol" name="rol" required>
            <option value="ADMINISTRADOR">Administrador</option>
            <option value="CHEF_EJECUTIVO">Chef ejecutivo</option>
            <option value="COCINERO_LINEA">Cocinero de línea</option>
            <option value="PERSONAL_SALON">Personal de salón</option>
            <option value="CAJERO">Cajero</option>
        </select><br><br>

        <button type="submit">Registrar</button>
    </form>

    <p><a href="empleados">Ver listado de empleados</a></p>
</body>
</html>
