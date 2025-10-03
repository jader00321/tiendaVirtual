package com.tiendavirtual.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private String url = "jdbc:postgresql://localhost:5432/tiendavirtual_db";
    private String usuario = "tienda_user";
    private String clave = "admin003";

    public Connection establecerConexion() throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        Connection conexion = DriverManager.getConnection(url, usuario, clave);
        return conexion;
    }
}