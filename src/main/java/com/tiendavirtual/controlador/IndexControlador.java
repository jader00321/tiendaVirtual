// Ruta: src/main/java/com/tiendavirtual/controlador/IndexControlador.java
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

@WebServlet(urlPatterns = {"/index", ""})
public class IndexControlador extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private CategoriaDAO categoriaDAO;
    private ProductoDAO productoDAO;

    @Override
    public void init() {
        categoriaDAO = new CategoriaDAO();
        productoDAO = new ProductoDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // 1. Obtiene la lista de objetos Categoria desde la base de datos.
            List<Categoria> listaCategorias = categoriaDAO.listar();
            
            // 2. Obtiene todos los productos (ordenados por el más nuevo).
            List<Producto> todosLosProductos = productoDAO.listar(null, null); 
            
            // 3. Selecciona hasta 4 productos para destacar en la portada.
            List<Producto> productosDestacados = todosLosProductos.subList(0, Math.min(todosLosProductos.size(), 4));
            
            // 4. Envía ambas listas a la vista como atributos del request.
            request.setAttribute("listaCategorias", listaCategorias);
            request.setAttribute("listaProductosDestacados", productosDestacados);

            // 5. Reenvía la petición a la vista JSP.
            RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
            dispatcher.forward(request, response);
            
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace(); // Imprime el error detallado en la consola del servidor.
            throw new ServletException("Error al recuperar los datos para la página de inicio", e);
        }
    }
}