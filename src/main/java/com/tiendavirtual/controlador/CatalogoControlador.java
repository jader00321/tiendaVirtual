// Ruta: src/main/java/com/tiendavirtual/controlador/CatalogoControlador.java
package com.tiendavirtual.controlador;

import com.tiendavirtual.dao.CategoriaDAO;
import com.tiendavirtual.dao.ProductoDAO;
import com.tiendavirtual.modelo.Categoria;
import com.tiendavirtual.modelo.Producto;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet("/catalogo")
public class CatalogoControlador extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProductoDAO productoDAO;
    private CategoriaDAO categoriaDAO;

    @Override
    public void init() {
        productoDAO = new ProductoDAO();
        categoriaDAO = new CategoriaDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String categoriaNombre = request.getParameter("categoria");
            
            List<Producto> productosAMostrar;
            List<Categoria> todasLasCategorias = categoriaDAO.listar();

            // Si se filtra por nombre de categoría
            if (categoriaNombre != null && !categoriaNombre.isEmpty()) {
                productosAMostrar = productoDAO.listarPorNombreCategoria(categoriaNombre);
                request.setAttribute("tituloCatalogo", "Categoría: " + categoriaNombre);
            } else {
                // Si no hay filtro, listar todos los productos
                productosAMostrar = productoDAO.listar(null, null);
                request.setAttribute("tituloCatalogo", "Nuestro Catálogo Completo");
            }
            
            // Agrupamos los productos obtenidos por su categoría para la nueva vista
            Map<String, List<Producto>> productosPorCategoria = productosAMostrar.stream()
                .filter(p -> p.getNombreCategoria() != null)
                .collect(Collectors.groupingBy(Producto::getNombreCategoria));

            request.setAttribute("productosPorCategoria", productosPorCategoria);
            request.setAttribute("listaCategorias", todasLasCategorias); // Para la barra de filtros

            RequestDispatcher dispatcher = request.getRequestDispatcher("catalogo.jsp");
            dispatcher.forward(request, response);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new ServletException("Error al recuperar productos para el catálogo", e);
        }
    }
}