package com.tiendavirtual.dao;

import com.tiendavirtual.modelo.Pedido;
import com.tiendavirtual.modelo.PedidoItem;
import com.tiendavirtual.modelo.Producto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {
    private final Conexion con = new Conexion();

    /**
     * Guarda un pedido y sus ítems en la base de datos dentro de una transacción.
     * @param pedido El objeto Pedido a guardar.
     * @param items La lista de PedidoItem asociados al pedido.
     * @return El ID del pedido generado.
     */
    public int guardarPedido(Pedido pedido, List<PedidoItem> items) throws SQLException, ClassNotFoundException {
        String sqlPedido = "INSERT INTO pedidos (usuario_id, total, codigo_boleta, fecha) VALUES (?, ?, ?, ?)";
        String sqlItem = "INSERT INTO pedido_items (pedido_id, producto_id, cantidad, precio_unitario) VALUES (?, ?, ?, ?)";
        
        Connection conn = null;
        int pedidoId = -1;
        try {
            conn = con.establecerConexion();
            // Iniciar transacción para asegurar la integridad de los datos
            conn.setAutoCommit(false);

            // Guardar el pedido principal y obtener el ID generado
            try (PreparedStatement pstmtPedido = conn.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS)) {
                pstmtPedido.setInt(1, pedido.getUsuarioId());
                pstmtPedido.setDouble(2, pedido.getTotal());
                pstmtPedido.setString(3, pedido.getCodigoBoleta());
                pstmtPedido.setTimestamp(4, pedido.getFecha());
                pstmtPedido.executeUpdate();

                try (ResultSet generatedKeys = pstmtPedido.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        pedidoId = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("No se pudo obtener el ID del pedido generado.");
                    }
                }
            }

            // Guardar cada ítem del pedido usando un batch para eficiencia
            try (PreparedStatement pstmtItem = conn.prepareStatement(sqlItem)) {
                for (PedidoItem item : items) {
                    pstmtItem.setInt(1, pedidoId);
                    pstmtItem.setInt(2, item.getProductoId());
                    pstmtItem.setInt(3, item.getCantidad());
                    pstmtItem.setDouble(4, item.getPrecioUnitario());
                    pstmtItem.addBatch();
                }
                pstmtItem.executeBatch();
            }

            // Si todo fue exitoso, confirmar la transacción
            conn.commit();
            return pedidoId;

        } catch (SQLException e) {
            // Si algo falla, deshacer todos los cambios de esta transacción
            if (conn != null) {
                conn.rollback();
            }
            throw e; // Relanzar la excepción para que el controlador la maneje
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    /**
     * Busca un pedido por su ID.
     * @param pedidoId El ID del pedido a buscar.
     * @return El objeto Pedido encontrado, o null si no existe.
     */
    public Pedido buscarPedidoPorId(int pedidoId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM pedidos WHERE id = ?";
        Pedido pedido = null;
        try (Connection conn = con.establecerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, pedidoId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    pedido = new Pedido();
                    pedido.setId(rs.getInt("id"));
                    pedido.setUsuarioId(rs.getInt("usuario_id"));
                    pedido.setFecha(rs.getTimestamp("fecha"));
                    pedido.setTotal(rs.getDouble("total"));
                    pedido.setCodigoBoleta(rs.getString("codigo_boleta"));
                }
            }
        }
        return pedido;
    }

    /**
     * Busca todos los ítems de un pedido específico, incluyendo detalles del producto.
     * @param pedidoId El ID del pedido del cual se quieren los ítems.
     * @return Una lista de objetos PedidoItem.
     */
    public List<PedidoItem> buscarItemsPorPedidoId(int pedidoId) throws SQLException, ClassNotFoundException {
        List<PedidoItem> items = new ArrayList<>();
        String sql = "SELECT pi.*, p.nombre, p.imagen_url FROM pedido_items pi JOIN productos p ON pi.producto_id = p.id WHERE pi.pedido_id = ?";
        try (Connection conn = con.establecerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, pedidoId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    PedidoItem item = new PedidoItem();
                    item.setId(rs.getInt("id"));
                    item.setPedidoId(rs.getInt("pedido_id"));
                    item.setProductoId(rs.getInt("producto_id"));
                    item.setCantidad(rs.getInt("cantidad"));
                    item.setPrecioUnitario(rs.getDouble("precio_unitario"));

                    // Crear un objeto Producto asociado para mostrar en la boleta
                    Producto producto = new Producto();
                    producto.setId(rs.getInt("producto_id"));
                    producto.setNombre(rs.getString("nombre"));
                    producto.setImagenUrl(rs.getString("imagen_url"));
                    item.setProducto(producto);

                    items.add(item);
                }
            }
        }
        return items;
    }
}