package com.zao.backend.turnos.repositorio;

import com.zao.backend.turnos.modelo.Turno;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class TurnoRepository {

    public Turno insertar(Turno turno) throws SQLException {
        String sql = "INSERT INTO turno (id_empleado, id_estacion, fecha_turno, hora_inicio, hora_fin, checklist_completado) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, turno.getIdEmpleado());
            ps.setLong(2, turno.getIdEstacion());
            ps.setDate(3, Date.valueOf(turno.getFechaTurno()));
            ps.setTime(4, Time.valueOf(turno.getHoraInicio()));
            ps.setTime(5, Time.valueOf(turno.getHoraFin()));
            ps.setBoolean(6, turno.isChecklistCompletado());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    turno.setIdTurno(rs.getLong(1));
                }
            }
        }
        return turno;
    }

    public List<Turno> consultarTodos() throws SQLException {
        String sql = "SELECT * FROM turno";
        List<Turno> turnos = new ArrayList<>();
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                turnos.add(mapearTurno(rs));
            }
        }
        return turnos;
    }

    public Turno consultarPorId(Long idTurno) throws SQLException {
        String sql = "SELECT * FROM turno WHERE id_turno = ?";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, idTurno);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearTurno(rs);
                }
            }
        }
        return null;
    }

    public boolean actualizar(Turno turno) throws SQLException {
        String sql = "UPDATE turno SET id_empleado = ?, id_estacion = ?, fecha_turno = ?, hora_inicio = ?, hora_fin = ?, checklist_completado = ? WHERE id_turno = ?";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, turno.getIdEmpleado());
            ps.setLong(2, turno.getIdEstacion());
            ps.setDate(3, Date.valueOf(turno.getFechaTurno()));
            ps.setTime(4, Time.valueOf(turno.getHoraInicio()));
            ps.setTime(5, Time.valueOf(turno.getHoraFin()));
            ps.setBoolean(6, turno.isChecklistCompletado());
            ps.setLong(7, turno.getIdTurno());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(Long idTurno) throws SQLException {
        String sql = "DELETE FROM turno WHERE id_turno = ?";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, idTurno);
            return ps.executeUpdate() > 0;
        }
    }

    private Turno mapearTurno(ResultSet rs) throws SQLException {
        Turno turno = new Turno();
        turno.setIdTurno(rs.getLong("id_turno"));
        turno.setIdEmpleado(rs.getLong("id_empleado"));
        turno.setIdEstacion(rs.getLong("id_estacion"));
        turno.setFechaTurno(rs.getDate("fecha_turno").toLocalDate());
        turno.setHoraInicio(rs.getTime("hora_inicio").toLocalTime());
        turno.setHoraFin(rs.getTime("hora_fin").toLocalTime());
        turno.setChecklistCompletado(rs.getBoolean("checklist_completado"));
        return turno;
    }
}