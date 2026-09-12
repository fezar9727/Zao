<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Listado de empleados - Zao</title>
</head>
<body>
    <h1>Empleados registrados</h1>

    <table border="1" cellpadding="5">
        <tr>
            <th>ID</th>
            <th>Nombre</th>
            <th>Correo</th>
            <th>Rol</th>
        </tr>
        <c:forEach var="emp" items="${empleados}">
            <tr>
                <td><c:out value="${emp.idEmpleado}"/></td>
                <td><c:out value="${emp.nombre}"/></td>
                <td><c:out value="${emp.correo}"/></td>
                <td><c:out value="${emp.rol}"/></td>
            </tr>
        </c:forEach>
    </table>

    <p><a href="empleados/nuevo">Registrar nuevo empleado</a></p>
</body>
</html>
