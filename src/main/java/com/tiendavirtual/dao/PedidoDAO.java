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

    public int guardarPedido(Pedido pedido, List<PedidoItem> items) throws SQLException, ClassNotFoundException {
        String sqlPedido = "INSERT INTO pedidos (usuario_id, total, codigo_boleta, fecha) VALUES (?, ?, ?, ?)";
        String sqlItem = "INSERT INTO pedido_items (pedido_id, producto_id, cantidad, precio_unitario) VALUES (?, ?, ?, ?)";

        Connection conn = null;
        int pedidoId = -1;
        try {
            conn = con.establecerConexion();
            conn.setAutoCommit(false);

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

            conn.commit();
            return pedidoId;

        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

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

    public List<Pedido> buscarPorUsuarioId(int usuarioId) throws SQLException, ClassNotFoundException {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT * FROM pedidos WHERE usuario_id = ? ORDER BY fecha DESC";
        try (Connection conn = con.establecerConexion();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, usuarioId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Pedido pedido = new Pedido();
                    pedido.setId(rs.getInt("id"));
                    pedido.setUsuarioId(rs.getInt("usuario_id"));
                    pedido.setFecha(rs.getTimestamp("fecha"));
                    pedido.setTotal(rs.getDouble("total"));
                    pedido.setCodigoBoleta(rs.getString("codigo_boleta"));
                    pedidos.add(pedido);
                }
            }
        }
        return pedidos;
    }
}