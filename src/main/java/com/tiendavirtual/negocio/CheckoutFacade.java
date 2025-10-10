package com.tiendavirtual.negocio;

import com.tiendavirtual.dao.PedidoDAO;
import com.tiendavirtual.dao.ProductoDAO;
import com.tiendavirtual.modelo.CarritoDeCompras;
import com.tiendavirtual.modelo.CarritoItem;
import com.tiendavirtual.modelo.Pedido;
import com.tiendavirtual.modelo.PedidoItem;
import com.tiendavirtual.modelo.Producto;
import com.tiendavirtual.modelo.Usuario;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CheckoutFacade {

    private final ProductoDAO productoDAO;
    private final PedidoDAO pedidoDAO;

    public CheckoutFacade() {
        this.productoDAO = new ProductoDAO();
        this.pedidoDAO = new PedidoDAO();
    }

    public int finalizarCompra(CarritoDeCompras carrito, Usuario usuario) throws SQLException, ClassNotFoundException, IllegalStateException {
        for (CarritoItem item : carrito.getItems()) {
            Producto productoDB = productoDAO.buscarPorId(item.getProducto().getId());
            if (productoDB == null || productoDB.getStock() < item.getCantidad()) {
                throw new IllegalStateException("No hay suficiente stock para el producto: " + item.getProducto().getNombre());
            }
        }

        Pedido pedido = new Pedido();
        pedido.setUsuarioId(usuario.getId());
        pedido.setTotal(carrito.getTotal());
        pedido.setFecha(new Timestamp(System.currentTimeMillis()));
        pedido.setCodigoBoleta("BOL-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());

        List<PedidoItem> pedidoItems = new ArrayList<>();
        for (CarritoItem item : carrito.getItems()) {
            PedidoItem pedidoItem = new PedidoItem();
            pedidoItem.setProductoId(item.getProducto().getId());
            pedidoItem.setCantidad(item.getCantidad());
            pedidoItem.setPrecioUnitario(item.getProducto().getPrecioVenta());
            pedidoItems.add(pedidoItem);
        }

        for (CarritoItem item : carrito.getItems()) {
            Producto producto = item.getProducto();
            producto.setStock(producto.getStock() - item.getCantidad());
            productoDAO.actualizar(producto);
        }

        return pedidoDAO.guardarPedido(pedido, pedidoItems);
    }
}