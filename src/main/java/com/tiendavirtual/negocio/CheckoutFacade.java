// Ruta: src/main/java/com/tiendavirtual/negocio/CheckoutFacade.java
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

    /**
     * Procesa la finalización de una compra.
     * @param carrito El carrito de compras del usuario.
     * @param usuario El usuario que realiza la compra.
     * @return El ID del pedido generado si la compra es exitosa.
     * @throws SQLException Si hay un error en la base de datos.
     * @throws ClassNotFoundException Si no se encuentra el driver de la BD.
     * @throws IllegalStateException Si no hay suficiente stock para algún producto.
     */
    public int finalizarCompra(CarritoDeCompras carrito, Usuario usuario) throws SQLException, ClassNotFoundException, IllegalStateException {
        // --- 1. Verificación de Stock ---
        // Antes de hacer cualquier cambio, nos aseguramos de que haya stock para todo.
        for (CarritoItem item : carrito.getItems()) {
            Producto productoDB = productoDAO.buscarPorId(item.getProducto().getId());
            if (productoDB == null || productoDB.getStock() < item.getCantidad()) {
                throw new IllegalStateException("No hay suficiente stock para el producto: " + item.getProducto().getNombre());
            }
        }

        // --- 2. Crear los objetos Pedido y PedidoItem ---
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

        // --- 3. Actualizar el Stock en la Base de Datos ---
        for (CarritoItem item : carrito.getItems()) {
            Producto producto = item.getProducto();
            producto.setStock(producto.getStock() - item.getCantidad());
            productoDAO.actualizar(producto);
        }

        // --- 4. Guardar el Pedido en la Base de Datos ---
        // El PedidoDAO se encargará de la transacción para asegurar que todo se guarde correctamente.
        return pedidoDAO.guardarPedido(pedido, pedidoItems);
    }
}