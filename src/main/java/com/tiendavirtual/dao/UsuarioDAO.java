// Ruta: src/main/java/com/tiendavirtual/dao/UsuarioDAO.java
package com.tiendavirtual.dao;

import com.tiendavirtual.modelo.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    private final Conexion con = new Conexion();

    private Usuario extractUsuarioFromResultSet(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getInt("id"));
        usuario.setNombre(rs.getString("nombre"));
        usuario.setEmail(rs.getString("email"));
        usuario.setPassword(rs.getString("password"));
        usuario.setRol(rs.getString("rol"));
        usuario.setActivo(rs.getBoolean("activo")); // Leer el nuevo campo
        return usuario;
    }

    public Usuario buscarPorId(int id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM usuarios WHERE id = ?";
        Usuario usuario = null;
        try (Connection conn = con.establecerConexion(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    usuario = extractUsuarioFromResultSet(rs);
                }
            }
        }
        return usuario;
    }
    
    public Usuario buscarPorEmail(String email) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM usuarios WHERE email = ?";
        Usuario usuario = null;
        try (Connection conn = con.establecerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    usuario = extractUsuarioFromResultSet(rs);
                }
            }
        }
        return usuario;
    }

    public void agregar(Usuario usuario) throws SQLException, ClassNotFoundException {
        // La columna 'activo' no se incluye aquí porque tiene un valor por defecto en la BD
        String sql = "INSERT INTO usuarios (nombre, email, password, rol) VALUES (?, ?, ?, ?)";
        try (Connection conn = con.establecerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, usuario.getNombre());
            pstmt.setString(2, usuario.getEmail());
            pstmt.setString(3, usuario.getPassword());
            pstmt.setString(4, usuario.getRol());
            pstmt.executeUpdate();
        }
    }

    // --- NUEVOS MÉTODOS PARA GESTIÓN DE USUARIOS ---

    public List<Usuario> listarTodos() throws SQLException, ClassNotFoundException {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios ORDER BY id ASC";
        try (Connection conn = con.establecerConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                // No necesitamos el password para la lista de gestión
                Usuario u = new Usuario();
                u.setId(rs.getInt("id"));
                u.setNombre(rs.getString("nombre"));
                u.setEmail(rs.getString("email"));
                u.setRol(rs.getString("rol"));
                u.setActivo(rs.getBoolean("activo"));
                lista.add(u);
            }
        }
        return lista;
    }

    public void cambiarEstado(int id, boolean activo) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE usuarios SET activo = ? WHERE id = ?";
        try (Connection conn = con.establecerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, activo);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    public void cambiarRol(int id, String rol) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE usuarios SET rol = ? WHERE id = ?";
        try (Connection conn = con.establecerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, rol);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    public void actualizar(Usuario usuario) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE usuarios SET nombre = ?, email = ?, rol = ?, activo = ? WHERE id = ?";
        try (Connection conn = con.establecerConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getEmail());
            ps.setString(3, usuario.getRol());
            ps.setBoolean(4, usuario.isActivo());
            ps.setInt(5, usuario.getId());
            ps.executeUpdate();
        }
    }
    
    public void actualizarPerfil(int id, String nombre, String password) throws SQLException, ClassNotFoundException {
        // Este método solo actualiza nombre y, opcionalmente, la contraseña
        StringBuilder sql = new StringBuilder("UPDATE usuarios SET nombre = ?");
        if (password != null && !password.isEmpty()) {
            sql.append(", password = ?");
        }
        sql.append(" WHERE id = ?");
        
        try (Connection conn = con.establecerConexion(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            ps.setString(1, nombre);
            if (password != null && !password.isEmpty()) {
                ps.setString(2, password);
                ps.setInt(3, id);
            } else {
                ps.setInt(2, id);
            }
            ps.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        try (Connection conn = con.establecerConexion(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
}