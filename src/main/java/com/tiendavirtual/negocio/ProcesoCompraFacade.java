// Ruta: src/main/java/com/tiendavirtual/negocio/ProcesoCompraFacade.java
package com.tiendavirtual.negocio;

import com.tiendavirtual.dao.ProductoDAO;
import com.tiendavirtual.modelo.Producto;
import java.sql.SQLException;

public class ProcesoCompraFacade {

    private ProductoDAO productoDAO;

    public ProcesoCompraFacade() {
        this.productoDAO = new ProductoDAO();
    }

    /**
     * Simula la compra de un producto, verificando y actualizando el stock.
     * @param productoId El ID del producto a comprar.
     * @return true si la compra fue exitosa, false si no hay stock o el producto no existe.
     */
    public boolean realizarCompra(int productoId) {
        try {
            // 1. Obtener el producto de la base de datos.
            Producto producto = productoDAO.buscarPorId(productoId);

            // 2. Verificar si el producto existe y si hay stock disponible.
            if (producto != null && producto.getStock() > 0) {
                // 3. Si hay stock, se resta una unidad.
                producto.setStock(producto.getStock() - 1);
                
                // 4. Se actualiza el producto en la base de datos con el nuevo stock.
                productoDAO.actualizar(producto);
                
                // 5. La operación fue exitosa.
                return true;
            }
        } catch (SQLException | ClassNotFoundException e) {
            // Manejo de errores de base de datos.
            e.printStackTrace(); 
        }
        
        // Si el producto no existe, no hay stock, o hubo un error, la compra falla.
        return false;
    }
}