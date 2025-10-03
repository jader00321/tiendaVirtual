package com.tiendavirtual.controlador;

import com.tiendavirtual.dao.ProductoDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = {"/index", ""})
public class IndexControlador extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProductoDAO productoDAO;

    public void init() {
        productoDAO = new ProductoDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<String> listaCategorias = productoDAO.listarCategorias();
            request.setAttribute("listaCategorias", listaCategorias);

            Map<String, String> imagenesCategorias = new HashMap<>();
            imagenesCategorias.put("Frutas", "categoria_frutas.jpg");
            imagenesCategorias.put("Verduras", "categoria_verduras.jpg");
            imagenesCategorias.put("Lacteos", "categoria_lacteos.jpg");
            imagenesCategorias.put("Abarrotes", "categoria_abarrotes.jpg");

            request.setAttribute("imagenesCategorias", imagenesCategorias);

            RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
            dispatcher.forward(request, response);
        } catch (SQLException  | ClassNotFoundException e) {
            throw new ServletException("Error al recuperar las categorías para el index", e);
        }
    }
}
