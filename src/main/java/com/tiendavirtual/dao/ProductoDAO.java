package com.tiendavirtual.dao;

import com.tiendavirtual.modelo.Producto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    private Conexion con = new Conexion();

    private Producto extractProductoFromResultSet(ResultSet rs) throws SQLException {
        Producto producto = new Producto();
        producto.setId(rs.getInt("id"));
        producto.setNombre(rs.getString("nombre"));
        producto.setDescripcion(rs.getString("descripcion"));
        producto.setPrecio(rs.getDouble("precio"));
        producto.setStock(rs.getInt("stock"));
        producto.setImagenUrl(rs.getString("imagen_url"));
        producto.setCategoria(rs.getString("categoria"));
        return producto;
    }

    public List<Producto> listar() throws SQLException, ClassNotFoundException {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM productos ORDER BY id ASC";
        try (Connection conn = con.establecerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                productos.add(extractProductoFromResultSet(rs));
            }
        }
        return productos;
    }

    public List<Producto> listarPorCategoria(String categoria) throws SQLException, ClassNotFoundException {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM productos WHERE categoria = ? ORDER BY id ASC";
        try (Connection conn = con.establecerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, categoria);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    productos.add(extractProductoFromResultSet(rs));
                }
            }
        }
        return productos;
    }

    public Producto buscarPorId(int id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM productos WHERE id = ?";
        Producto producto = null;
        try (Connection conn = con.establecerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    producto = extractProductoFromResultSet(rs);
                }
            }
        }
        return producto;
    }

    public void agregar(Producto producto) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO productos (nombre, descripcion, precio, stock, imagen_url, categoria) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = con.establecerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, producto.getNombre());
            pstmt.setString(2, producto.getDescripcion());
            pstmt.setDouble(3, producto.getPrecio());
            pstmt.setInt(4, producto.getStock());
            pstmt.setString(5, producto.getImagenUrl());
            pstmt.setString(6, producto.getCategoria());
            pstmt.executeUpdate();
        }
    }

    public void actualizar(Producto producto) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE productos SET nombre = ?, descripcion = ?, precio = ?, stock = ?, imagen_url = ?, categoria = ? WHERE id = ?";
        try (Connection conn = con.establecerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, producto.getNombre());
            pstmt.setString(2, producto.getDescripcion());
            pstmt.setDouble(3, producto.getPrecio());
            pstmt.setInt(4, producto.getStock());
            pstmt.setString(5, producto.getImagenUrl());
            pstmt.setString(6, producto.getCategoria());
            pstmt.setInt(7, producto.getId());
            pstmt.executeUpdate();
        }
    }

    public List<String> listarCategorias() throws SQLException, ClassNotFoundException {
        List<String> categorias = new ArrayList<>();
        String sql = "SELECT DISTINCT categoria FROM productos ORDER BY categoria ASC";
        try (Connection conn = con.establecerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                categorias.add(rs.getString("categoria"));
            }
        }
        return categorias;
    }

    public void eliminar(int id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM productos WHERE id = ?";
        try (Connection conn = con.establecerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
}