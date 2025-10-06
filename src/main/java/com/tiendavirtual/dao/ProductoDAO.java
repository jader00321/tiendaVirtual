// Ruta: src/main/java/com/tiendavirtual/dao/ProductoDAO.java
package com.tiendavirtual.dao;

import com.tiendavirtual.modelo.Producto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    private final Conexion con = new Conexion();
    
    private Producto extractProductoFromResultSet(ResultSet rs) throws SQLException {
        Producto producto = new Producto();
        producto.setId(rs.getInt("id"));
        producto.setCodigo(rs.getString("codigo"));
        producto.setNombre(rs.getString("nombre"));
        producto.setDescripcion(rs.getString("descripcion"));
        producto.setPrecioVenta(rs.getDouble("precio_venta"));
        producto.setStock(rs.getInt("stock"));
        producto.setImagenUrl(rs.getString("imagen_url"));
        producto.setCategoriaId((Integer) rs.getObject("categoria_id"));
        producto.setPrecioCosto(rs.getDouble("precio_costo"));
        producto.setProveedorId((Integer) rs.getObject("proveedor_id"));
        
        try {
            if (rs.findColumn("nombre_categoria") > 0) {
               producto.setNombreCategoria(rs.getString("nombre_categoria"));
            }
        } catch (SQLException e) {
            // Ignorar si la columna no existe
        }
        return producto;
    }

    // --- NUEVO MÉTODO ESPECÍFICO PARA LA PÁGINA DE INICIO ---
    /**
     * Obtiene una lista limitada de los productos más nuevos para mostrarlos como destacados.
     * @param limite El número máximo de productos a devolver.
     * @return Una lista de productos destacados.
     */
    public List<Producto> listarDestacados(int limite) throws SQLException, ClassNotFoundException {
        List<Producto> productos = new ArrayList<>();
        // La consulta ahora incluye LIMIT para ser más eficiente
        String sql = "SELECT p.*, c.nombre AS nombre_categoria FROM productos p LEFT JOIN categorias c ON p.categoria_id = c.id ORDER BY p.id DESC LIMIT ?";
        
        try (Connection conn = con.establecerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, limite); // Se establece el límite en la consulta
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    productos.add(extractProductoFromResultSet(rs));
                }
            }
        }
        return productos;
    }
    
    // El resto de los métodos se mantienen para las otras funcionalidades

    public List<Producto> listar(Integer categoriaIdFiltro, String terminoBusqueda) throws SQLException, ClassNotFoundException {
        List<Producto> productos = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT p.*, c.nombre AS nombre_categoria FROM productos p LEFT JOIN categorias c ON p.categoria_id = c.id");
        List<Object> params = new ArrayList<>();
        boolean hasWhere = false;

        if (categoriaIdFiltro != null && categoriaIdFiltro > 0) {
            sql.append(" WHERE p.categoria_id = ?");
            params.add(categoriaIdFiltro);
            hasWhere = true;
        }

        if (terminoBusqueda != null && !terminoBusqueda.trim().isEmpty()) {
            sql.append(hasWhere ? " AND" : " WHERE");
            sql.append(" p.nombre ILIKE ?");
            params.add("%" + terminoBusqueda.trim() + "%");
        }

        sql.append(" ORDER BY p.id DESC");

        try (Connection conn = con.establecerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    productos.add(extractProductoFromResultSet(rs));
                }
            }
        }
        return productos;
    }
    
    public List<Producto> listarPorNombreCategoria(String nombreCategoria) throws SQLException, ClassNotFoundException {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT p.*, c.nombre AS nombre_categoria FROM productos p JOIN categorias c ON p.categoria_id = c.id WHERE c.nombre ILIKE ? ORDER BY p.id DESC";
        try (Connection conn = con.establecerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombreCategoria);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    productos.add(extractProductoFromResultSet(rs));
                }
            }
        }
        return productos;
    }

    public Producto buscarPorId(int id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT p.*, c.nombre AS nombre_categoria FROM productos p LEFT JOIN categorias c ON p.categoria_id = c.id WHERE p.id = ?";
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
        String sql = "INSERT INTO productos (nombre, descripcion, precio_venta, stock, imagen_url, categoria_id, precio_costo, proveedor_id, codigo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = con.establecerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, producto.getNombre());
            pstmt.setString(2, producto.getDescripcion());
            pstmt.setDouble(3, producto.getPrecioVenta());
            pstmt.setInt(4, producto.getStock());
            pstmt.setString(5, producto.getImagenUrl());
            pstmt.setObject(6, producto.getCategoriaId());
            pstmt.setDouble(7, producto.getPrecioCosto());
            pstmt.setObject(8, producto.getProveedorId());
            pstmt.setString(9, producto.getCodigo());
            pstmt.executeUpdate();
        }
    }

    public void actualizar(Producto producto) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE productos SET nombre = ?, descripcion = ?, precio_venta = ?, stock = ?, imagen_url = ?, categoria_id = ?, precio_costo = ?, proveedor_id = ?, codigo = ? WHERE id = ?";
        try (Connection conn = con.establecerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, producto.getNombre());
            pstmt.setString(2, producto.getDescripcion());
            pstmt.setDouble(3, producto.getPrecioVenta());
            pstmt.setInt(4, producto.getStock());
            pstmt.setString(5, producto.getImagenUrl());
            pstmt.setObject(6, producto.getCategoriaId());
            pstmt.setDouble(7, producto.getPrecioCosto());
            pstmt.setObject(8, producto.getProveedorId());
            pstmt.setString(9, producto.getCodigo());
            pstmt.setInt(10, producto.getId());
            pstmt.executeUpdate();
        }
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