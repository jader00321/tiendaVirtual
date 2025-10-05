// Ruta: src/main/java/com/tiendavirtual/dao/ProveedorDAO.java
package com.tiendavirtual.dao;

import com.tiendavirtual.modelo.Proveedor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProveedorDAO {

    private Conexion con = new Conexion();

    public Proveedor buscarPorId(int id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM proveedores WHERE id = ?";
        Proveedor proveedor = null;
        try (Connection conn = con.establecerConexion();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    proveedor = new Proveedor();
                    proveedor.setId(rs.getInt("id"));
                    proveedor.setNombre(rs.getString("nombre"));
                    proveedor.setContacto(rs.getString("contacto"));
                }
            }
        }
        return proveedor;
    }

    public List<Proveedor> listar() throws SQLException, ClassNotFoundException {
        List<Proveedor> proveedores = new ArrayList<>();
        String sql = "SELECT * FROM proveedores ORDER BY nombre ASC";
        try (Connection conn = con.establecerConexion();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Proveedor proveedor = new Proveedor();
                proveedor.setId(rs.getInt("id"));
                proveedor.setNombre(rs.getString("nombre"));
                proveedor.setContacto(rs.getString("contacto"));
                proveedores.add(proveedor);
            }
        }
        return proveedores;
    }

    public void agregar(Proveedor proveedor) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO proveedores (nombre, contacto) VALUES (?, ?)";
        try (Connection conn = con.establecerConexion();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, proveedor.getNombre());
            pstmt.setString(2, proveedor.getContacto());
            pstmt.executeUpdate();
        }
    }

    public void actualizar(Proveedor proveedor) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE proveedores SET nombre = ?, contacto = ? WHERE id = ?";
        try (Connection conn = con.establecerConexion();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Asigna los valores a los parámetros (?) de la consulta SQL.
            pstmt.setString(1, proveedor.getNombre());
            pstmt.setString(2, proveedor.getContacto());
            pstmt.setInt(3, proveedor.getId());

            // Ejecuta la actualización.
            pstmt.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM proveedores WHERE id = ?";
        try (Connection conn = con.establecerConexion();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Asigna el ID al parámetro (?) de la consulta SQL.
            pstmt.setInt(1, id);

            // Ejecuta la eliminación.
            pstmt.executeUpdate();
        }
    }
}