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
            
            // 2. Llama al nuevo método del DAO para obtener solo 4 productos destacados.
            List<Producto> productosDestacados = productoDAO.listarDestacados(4); 
            
            // 3. Envía ambas listas a la vista como atributos del request.
            request.setAttribute("listaCategorias", listaCategorias);
            request.setAttribute("listaProductosDestacados", productosDestacados);

            // 4. Reenvía la petición a la vista JSP.
            RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
            dispatcher.forward(request, response);
            
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace(); // Imprime el error detallado en la consola del servidor.
            throw new ServletException("Error al recuperar los datos para la página de inicio", e);
        }
    }
}