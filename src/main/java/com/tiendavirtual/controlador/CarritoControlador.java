package com.tiendavirtual.controlador;

import com.tiendavirtual.dao.ProductoDAO;
import com.tiendavirtual.modelo.CarritoDeCompras;
import com.tiendavirtual.modelo.Producto;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/carrito")
public class CarritoControlador extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProductoDAO productoDAO;

    @Override
    public void init() {
        productoDAO = new ProductoDAO();
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "mostrar"; // Por defecto, mostramos el carrito
        }

        try {
            switch (accion) {
                case "agregar":
                    agregarAlCarrito(request, response);
                    break;
                case "actualizar":
                    actualizarCantidad(request, response);
                    break;
                case "eliminar":
                    eliminarDelCarrito(request, response);
                    break;
                default:
                    mostrarCarrito(request, response);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new ServletException("Error de base de datos en el carrito", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // La vista del carrito se muestra a través de GET
        try {
            mostrarCarrito(request, response);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    /**
     * Obtiene el carrito de la sesión, o crea uno nuevo si no existe.
     */
    private CarritoDeCompras getCarrito(HttpServletRequest request) {
        HttpSession session = request.getSession(true); // true para crear la sesión si no existe
        CarritoDeCompras carrito = (CarritoDeCompras) session.getAttribute("carrito");
        if (carrito == null) {
            carrito = new CarritoDeCompras();
            session.setAttribute("carrito", carrito);
        }
        return carrito;
    }
    
    private void agregarAlCarrito(HttpServletRequest request, HttpServletResponse response) throws SQLException, ClassNotFoundException, IOException {
        int productoId = Integer.parseInt(request.getParameter("productoId"));
        int cantidad = Integer.parseInt(request.getParameter("cantidad"));
        
        Producto producto = productoDAO.buscarPorId(productoId);
        if (producto != null && producto.getStock() >= cantidad) {
            CarritoDeCompras carrito = getCarrito(request);
            carrito.agregarItem(producto, cantidad);
        }
        
        // Redirigir de vuelta a la página de donde vino el usuario
        String referer = request.getHeader("Referer");
        response.sendRedirect(referer != null ? referer : "catalogo");
    }

    private void actualizarCantidad(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int productoId = Integer.parseInt(request.getParameter("productoId"));
        int cantidad = Integer.parseInt(request.getParameter("cantidad"));
        
        CarritoDeCompras carrito = getCarrito(request);
        carrito.actualizarCantidad(productoId, cantidad);
        
        response.sendRedirect("carrito");
    }

    private void eliminarDelCarrito(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int productoId = Integer.parseInt(request.getParameter("productoId"));
        
        CarritoDeCompras carrito = getCarrito(request);
        carrito.eliminarItem(productoId);
        
        response.sendRedirect("carrito");
    }
    
    private void mostrarCarrito(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Solo necesita reenviar a la JSP, ya que la JSP leerá el carrito desde la sesión.
        RequestDispatcher dispatcher = request.getRequestDispatcher("carrito.jsp");
        dispatcher.forward(request, response);
    }
}