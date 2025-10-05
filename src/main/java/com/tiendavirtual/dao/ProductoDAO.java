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

    /**
     * Convierte una fila de un ResultSet a un objeto Producto.
     * Este método es el corazón de la lectura de datos y está actualizado con todos los campos nuevos.
     */
    private Producto extractProductoFromResultSet(ResultSet rs) throws SQLException {
        Producto producto = new Producto();
        producto.setId(rs.getInt("id"));
        producto.setCodigo(rs.getString("codigo")); // Lee el nuevo campo 'codigo'
        producto.setNombre(rs.getString("nombre"));
        producto.setDescripcion(rs.getString("descripcion"));
        producto.setPrecioVenta(rs.getDouble("precio_venta"));
        producto.setStock(rs.getInt("stock"));
        producto.setImagenUrl(rs.getString("imagen_url"));
        producto.setCategoriaId((Integer) rs.getObject("categoria_id"));
        producto.setPrecioCosto(rs.getDouble("precio_costo"));
        producto.setProveedorId((Integer) rs.getObject("proveedor_id"));

        // Lee el nombre de la categoría del JOIN de forma segura
        try {
            if (rs.findColumn("nombre_categoria") > 0) {
                producto.setNombreCategoria(rs.getString("nombre_categoria"));
            }
        } catch (SQLException e) {
            // Ignora el error si la columna no está presente en la consulta
        }
        return producto;
    }

    /**
     * Lista productos con filtros opcionales y ordenados por el más nuevo.
     * @param categoriaIdFiltro ID de la categoría para filtrar (puede ser null).
     * @param terminoBusqueda Texto para buscar en el nombre (puede ser null).
     * @return Lista de productos.
     */
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

        sql.append(" ORDER BY p.id DESC"); // Ordenar por ID descendente

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
    
    /**
     * Lista productos filtrados por el nombre de la categoría (usado por el Catálogo).
     */
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

    /**
     * Busca un producto específico por su ID.
     */
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

    /**
     * Agrega un nuevo producto a la base de datos (actualizado con 'codigo').
     */
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

    /**
     * Actualiza un producto existente en la base de datos (actualizado con 'codigo').
     */
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

    /**
     * Elimina un producto de la base de datos por su ID.
     */
    public void eliminar(int id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM productos WHERE id = ?";
        try (Connection conn = con.establecerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
}