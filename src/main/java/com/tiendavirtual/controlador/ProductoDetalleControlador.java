// Ruta: src/main/java/com/tiendavirtual/controlador/ProductoDetalleControlador.java
package com.tiendavirtual.controlador;

import com.tiendavirtual.dao.ProductoDAO;
import com.tiendavirtual.dao.ProveedorDAO;
import com.tiendavirtual.modelo.Producto;
import com.tiendavirtual.modelo.ProductoDetalleDTO;
import com.tiendavirtual.modelo.Proveedor;
import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/productoDetalle")
public class ProductoDetalleControlador extends HttpServlet {

    private ProductoDAO productoDAO;
    private ProveedorDAO proveedorDAO;
    private Gson gson = new Gson();

    @Override
    public void init() {
        productoDAO = new ProductoDAO();
        proveedorDAO = new ProveedorDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParam = request.getParameter("id");
        if (idParam == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Falta el ID del producto.");
            return;
        }

        try {
            int productoId = Integer.parseInt(idParam);
            // El método buscarPorId ahora devuelve el producto con 'nombreCategoria' ya incluido.
            Producto producto = productoDAO.buscarPorId(productoId);

            if (producto == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Producto no encontrado.");
                return;
            }

            Proveedor proveedor = null;
            if (producto.getProveedorId() != null) {
                proveedor = proveedorDAO.buscarPorId(producto.getProveedorId());
            }

            // Crear el DTO con la información combinada
            ProductoDetalleDTO dto = new ProductoDetalleDTO();
            dto.setId(producto.getId());
            dto.setNombre(producto.getNombre());
            dto.setDescripcion(producto.getDescripcion());
            dto.setPrecioVenta(producto.getPrecioVenta());
            dto.setStock(producto.getStock());
            dto.setImagenUrl(producto.getImagenUrl());
            dto.setPrecioCosto(producto.getPrecioCosto());
            dto.setNombreProveedor(proveedor != null ? proveedor.getNombre() : "No especificado");
            dto.setNombreCategoria(producto.getNombreCategoria() != null ? producto.getNombreCategoria() : "No especificada");

            // Convertir el DTO a JSON y enviarlo como respuesta
            String productoJson = this.gson.toJson(dto);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(productoJson);
            out.flush();

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "El ID del producto debe ser un número.");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new ServletException("Error al acceder a la base de datos", e);
        }
    }
}