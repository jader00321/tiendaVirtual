package com.tiendavirtual.controlador;

import com.tiendavirtual.dao.ProductoDAO;
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

@WebServlet("/catalogo")
public class CatalogoControlador extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProductoDAO productoDAO;

    public void init() {
        productoDAO = new ProductoDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String categoria = request.getParameter("categoria");

        try {
            List<Producto> listaProductos;
            if (categoria != null && !categoria.isEmpty()) {
                listaProductos = productoDAO.listarPorCategoria(categoria);
                request.setAttribute("tituloCatalogo", "Categoría: " + categoria);
            } else {
                listaProductos = productoDAO.listar();
                request.setAttribute("tituloCatalogo", "Nuestro Catálogo");
            }
            request.setAttribute("listaProductos", listaProductos);
            RequestDispatcher dispatcher = request.getRequestDispatcher("catalogo.jsp");
            dispatcher.forward(request, response);
        } catch (SQLException | ClassNotFoundException e) {
            throw new ServletException("Error al recuperar productos para el catálogo", e);
        }
    }
}