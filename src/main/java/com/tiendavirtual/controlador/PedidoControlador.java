package com.tiendavirtual.controlador;

import com.tiendavirtual.modelo.CarritoDeCompras;
import com.tiendavirtual.modelo.Usuario;
import com.tiendavirtual.negocio.CheckoutFacade;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/pedido")
public class PedidoControlador extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CheckoutFacade checkoutFacade;
    
    @Override
    public void init() {
        checkoutFacade = new CheckoutFacade();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        // 1. Validar que el usuario haya iniciado sesión
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // 2. Validar que el carrito exista y no esté vacío
        CarritoDeCompras carrito = (CarritoDeCompras) session.getAttribute("carrito");
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (carrito == null || carrito.getItems().isEmpty()) {
            response.sendRedirect("carrito");
            return;
        }
        
        String accion = request.getParameter("accion");
        if ("finalizar".equals(accion)) {
            try {
                // 3. Llamar a la Fachada para que procese toda la lógica de compra
                int pedidoId = checkoutFacade.finalizarCompra(carrito, usuario);
                
                // 4. Si la compra es exitosa, limpiar el carrito de la sesión
                carrito.limpiar();
                session.setAttribute("carrito", carrito);
                
                // 5. Redirigir a la página de la boleta, pasando el ID del nuevo pedido
                response.sendRedirect("boleta.jsp?pedidoId=" + pedidoId);

            } catch (IllegalStateException e) {
                // Capturar error específico de falta de stock
                session.setAttribute("error_carrito", e.getMessage());
                response.sendRedirect("carrito");
            } catch (Exception e) {
                // Capturar cualquier otro error inesperado
                e.printStackTrace();
                session.setAttribute("error_carrito", "Ocurrió un error inesperado al procesar su pedido. Por favor, intente de nuevo.");
                response.sendRedirect("carrito");
            }
        } else {
             response.sendRedirect("carrito");
        }
    }
}