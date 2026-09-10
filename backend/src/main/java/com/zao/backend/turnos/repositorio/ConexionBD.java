package com.zao.backend.turnos.repositorio;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static final String URL = System.getenv("ZAO_DB_URL");
    private static final String USUARIO = System.getenv("ZAO_DB_USER");
    private static final String CLAVE = System.getenv("ZAO_DB_PASSWORD");

    public static Connection obtenerConexion() throws SQLException {
        if (URL == null || USUARIO == null || CLAVE == null) {
            throw new IllegalStateException(
                "Faltan variables de entorno ZAO_DB_URL, ZAO_DB_USER o ZAO_DB_PASSWORD.");
        }
        return DriverManager.getConnection(URL, USUARIO, CLAVE);
    }
}