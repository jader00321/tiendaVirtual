package com.tiendavirtual.controlador;

import com.tiendavirtual.dao.PedidoDAO;
import com.tiendavirtual.modelo.CarritoDeCompras;
import com.tiendavirtual.modelo.Pedido;
import com.tiendavirtual.modelo.Usuario;
import com.tiendavirtual.negocio.CheckoutFacade;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/pedido")
public class PedidoControlador extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CheckoutFacade checkoutFacade;
    private PedidoDAO pedidoDAO;
    
    @Override
    public void init() {
        checkoutFacade = new CheckoutFacade();
        pedidoDAO = new PedidoDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accion = request.getParameter("accion");
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        
        try {
            if ("historial".equals(accion)) {
                List<Pedido> listaPedidos = pedidoDAO.buscarPorUsuarioId(usuario.getId());
                request.setAttribute("listaPedidos", listaPedidos);
                RequestDispatcher dispatcher = request.getRequestDispatcher("historial_pedidos.jsp");
                dispatcher.forward(request, response);
            } else {
                int pedidoId = Integer.parseInt(request.getParameter("pedidoId"));
                Pedido pedido = pedidoDAO.buscarPedidoPorId(pedidoId);
                
                if (pedido != null && pedido.getUsuarioId() == usuario.getId()) {
                    request.setAttribute("pedido", pedido);
                    request.setAttribute("items", pedidoDAO.buscarItemsPorPedidoId(pedidoId));
                    RequestDispatcher dispatcher = request.getRequestDispatcher("boleta.jsp");
                    dispatcher.forward(request, response);
                } else {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "No tienes permiso para ver este pedido.");
                }
            }
        } catch (Exception e) {
            throw new ServletException("Error al recuperar datos del pedido", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        CarritoDeCompras carrito = (CarritoDeCompras) session.getAttribute("carrito");
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (carrito == null || carrito.getItems().isEmpty()) {
            response.sendRedirect("carrito");
            return;
        }
        
        String accion = request.getParameter("accion");
        if ("finalizar".equals(accion)) {
            try {
                int pedidoId = checkoutFacade.finalizarCompra(carrito, usuario);
                carrito.limpiar();
                session.setAttribute("carrito", carrito);
                response.sendRedirect("pedido?pedidoId=" + pedidoId);
            } catch (Exception e) {
                session.setAttribute("error_carrito", "Error al procesar el pedido: " + e.getMessage());
                response.sendRedirect("carrito");
            }
        } else {
             response.sendRedirect("carrito");
        }
    }
}