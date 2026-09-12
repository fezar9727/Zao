package com.zao.backend.web;

import com.zao.backend.turnos.modelo.Empleado;
import com.zao.backend.turnos.modelo.Rol;
import com.zao.backend.turnos.repositorio.ConexionBD;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet({"/empleados", "/empleados/nuevo"})
public class EmpleadoServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(EmpleadoServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (request.getServletPath().endsWith("/nuevo")) {
            request.getRequestDispatcher("/WEB-INF/formulario.jsp").forward(request, response);
            return;
        }

        List<Empleado> empleados = new ArrayList<>();
        String sql = "SELECT * FROM empleado";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Empleado e = new Empleado();
                e.setIdEmpleado(rs.getLong("id_empleado"));
                e.setNombre(rs.getString("nombre"));
                e.setCorreo(rs.getString("correo"));
                e.setRol(Rol.valueOf(rs.getString("rol")));
                empleados.add(e);
            }

        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error al consultar empleados", e);
            throw new ServletException("No se pudo cargar el listado de empleados.");
        }

        request.setAttribute("empleados", empleados);
        request.getRequestDispatcher("/WEB-INF/listado.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String nombre = request.getParameter("nombre");
        String correo = request.getParameter("correo");
        String rolParam = request.getParameter("rol");

        Rol rol;
        try {
            rol = Rol.valueOf(rolParam);
        } catch (IllegalArgumentException | NullPointerException ex) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Rol invalido.");
            return;
        }

        String sql = "INSERT INTO empleado (nombre, correo, rol) VALUES (?, ?, ?)";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setString(2, correo);
            ps.setString(3, rol.name());
            ps.executeUpdate();

        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error al registrar empleado", e);
            throw new ServletException("No se pudo registrar el empleado.");
        }

        response.sendRedirect(request.getContextPath() + "/empleados");
    }
}
