// Ruta: src/main/java/com/tiendavirtual/controlador/AdminControlador.java
package com.tiendavirtual.controlador;

import com.tiendavirtual.dao.UsuarioDAO;
import com.tiendavirtual.modelo.Usuario;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/admin/usuarios")
public class AdminControlador extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UsuarioDAO usuarioDAO;

    @Override
    public void init() {
        usuarioDAO = new UsuarioDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // El método GET siempre mostrará la lista actualizada de usuarios
        try {
            List<Usuario> listaUsuarios = usuarioDAO.listarTodos();
            request.setAttribute("listaUsuarios", listaUsuarios);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/gestion_usuarios.jsp");
            dispatcher.forward(request, response);
        } catch (SQLException | ClassNotFoundException e) {
            throw new ServletException("Error al listar usuarios para administración", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accion = request.getParameter("accion");
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            
            if ("cambiarRol".equals(accion)) {
                String nuevoRol = request.getParameter("rol");
                usuarioDAO.cambiarRol(id, nuevoRol);
                request.getSession().setAttribute("mensaje", "Rol del usuario ID " + id + " actualizado a " + nuevoRol + ".");
            } else if ("cambiarEstado".equals(accion)) {
                // Para cambiar el estado, primero buscamos el usuario para saber su estado actual
                Usuario usuario = usuarioDAO.buscarPorId(id);
                if (usuario != null) {
                    usuarioDAO.cambiarEstado(id, !usuario.isActivo());
                    String nuevoEstado = !usuario.isActivo() ? "activada" : "desactivada";
                    request.getSession().setAttribute("mensaje", "Cuenta del usuario ID " + id + " ha sido " + nuevoEstado + ".");
                }
            }
        } catch (SQLException | ClassNotFoundException | NumberFormatException e) {
            request.getSession().setAttribute("mensaje", "Error al procesar la solicitud: " + e.getMessage());
        }
        // Redirigimos de vuelta a la página de gestión para ver los cambios
        response.sendRedirect(request.getContextPath() + "/admin/usuarios");
    }
}