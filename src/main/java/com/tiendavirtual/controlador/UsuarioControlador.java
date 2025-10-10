package com.tiendavirtual.controlador;

import com.tiendavirtual.dao.UsuarioDAO;
import com.tiendavirtual.modelo.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/usuario")
public class UsuarioControlador extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UsuarioDAO usuarioDAO;

    public void init() {
        usuarioDAO = new UsuarioDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");
        if (accion == null) {
            response.sendRedirect("index");
            return;
        }

        try {
            switch (accion) {
                case "login":
                    login(request, response);
                    break;
                case "registrar":
                    registrar(request, response);
                    break;
                default:
                    response.sendRedirect("index");
            }
        } catch (SQLException | ClassNotFoundException ex) {
            throw new ServletException(ex);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");
        if ("logout".equals(accion)) {
            logout(request, response);
        } else {
            doPost(request, response);
        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException, ClassNotFoundException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        Usuario usuario = usuarioDAO.buscarPorEmail(email);

        if (usuario != null && usuario.getPassword().equals(password)) {
            // --- NUEVA VALIDACIÓN DE ESTADO ---
            if (!usuario.isActivo()) {
                request.setAttribute("error", "Su cuenta ha sido desactivada. Por favor, contacte al administrador.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
                return;
            }
        
            HttpSession session = request.getSession();
            session.setAttribute("usuario", usuario);
            response.sendRedirect("index");
        } else {
            request.setAttribute("error", "Credenciales incorrectas. Por favor, intente de nuevo.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
    private void registrar(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ClassNotFoundException {
        String nombre = request.getParameter("nombre");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (usuarioDAO.buscarPorEmail(email) != null) {
            request.setAttribute("error", "El correo electrónico ya está registrado.");
            try {
                request.getRequestDispatcher("registro.jsp").forward(request, response);
            } catch (ServletException e) {
                throw new IOException(e);
            }
            return;
        }

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(nombre);
        nuevoUsuario.setEmail(email);
        nuevoUsuario.setPassword(password);
        nuevoUsuario.setRol("CLIENTE");

        usuarioDAO.agregar(nuevoUsuario);

        Usuario usuarioRegistrado = usuarioDAO.buscarPorEmail(email);
        if (usuarioRegistrado != null) {
            HttpSession session = request.getSession();
            session.setAttribute("usuario", usuarioRegistrado);
        }

        response.sendRedirect("index");
    }

    private void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect("index");
    }

}
