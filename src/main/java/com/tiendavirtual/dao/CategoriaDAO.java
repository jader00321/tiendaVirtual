package com.tiendavirtual.dao;

import com.tiendavirtual.modelo.Categoria;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {

    private Conexion con = new Conexion();

    /**
     * Inserta una nueva categoría en la base de datos.
     * 
     * @param categoria El objeto Categoria con la información a guardar.
     */
    public void agregar(Categoria categoria) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO categorias (nombre, imagen_url) VALUES (?, ?)";
        try (Connection conn = con.establecerConexion();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, categoria.getNombre());
            pstmt.setString(2, categoria.getImagenUrl());
            pstmt.executeUpdate();
        }
    }

    /**
     * Actualiza una categoría existente en la base de datos.
     * 
     * @param categoria El objeto Categoria con el id y los nuevos datos.
     */
    public void actualizar(Categoria categoria) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE categorias SET nombre = ?, imagen_url = ? WHERE id = ?";
        try (Connection conn = con.establecerConexion();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, categoria.getNombre());
            pstmt.setString(2, categoria.getImagenUrl());
            pstmt.setInt(3, categoria.getId());
            pstmt.executeUpdate();
        }
    }

    /**
     * Elimina una categoría de la base de datos por su ID.
     * 
     * @param id El ID de la categoría a eliminar.
     */
    public void eliminar(int id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM categorias WHERE id = ?";
        try (Connection conn = con.establecerConexion();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    /**
     * Busca y devuelve una categoría por su ID.
     * 
     * @param id El ID de la categoría a buscar.
     * @return El objeto Categoria encontrado, o null si no existe.
     */
    public Categoria buscarPorId(int id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM categorias WHERE id = ?";
        Categoria categoria = null;
        try (Connection conn = con.establecerConexion();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    categoria = new Categoria();
                    categoria.setId(rs.getInt("id"));
                    categoria.setNombre(rs.getString("nombre"));
                    categoria.setImagenUrl(rs.getString("imagen_url"));
                }
            }
        }
        return categoria;
    }

    /**
     * Devuelve una lista con todas las categorías de la base de datos.
     * 
     * @return Una lista de objetos Categoria.
     */
    public List<Categoria> listar() throws SQLException, ClassNotFoundException {
        List<Categoria> categorias = new ArrayList<>();
        String sql = "SELECT * FROM categorias ORDER BY nombre ASC";
        try (Connection conn = con.establecerConexion();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Categoria categoria = new Categoria();
                categoria.setId(rs.getInt("id"));
                categoria.setNombre(rs.getString("nombre"));
                categoria.setImagenUrl(rs.getString("imagen_url"));
                categorias.add(categoria);
            }
        }
        return categorias;
    }
}