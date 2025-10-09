package com.tiendavirtual.negocio;

import com.tiendavirtual.dao.ProductoDAO;
import com.tiendavirtual.modelo.Producto;
import java.sql.SQLException;

public class ProcesoCompraFacade {

    private ProductoDAO productoDAO;

    public ProcesoCompraFacade() {
        this.productoDAO = new ProductoDAO();
    }

    public boolean realizarCompra(int productoId) {
        try {
            Producto producto = productoDAO.buscarPorId(productoId);
            if (producto != null && producto.getStock() > 0) {
                producto.setStock(producto.getStock() - 1);
                productoDAO.actualizar(producto);
                return true;
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace(); 
        }
        return false;
    }
}