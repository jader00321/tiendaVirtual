package com.tiendavirtual.filtros;

import com.tiendavirtual.modelo.Usuario;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = {"/inventario", "/gestion", "/admin/*"})
public class FiltroAutenticacion implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        if (session != null && session.getAttribute("usuario") != null) {
            Usuario usuario = (Usuario) session.getAttribute("usuario");
            String rol = usuario.getRol();
            String path = httpRequest.getServletPath();

            if (path.startsWith("/admin")) {
                if ("ADMIN".equals(rol)) {
                    chain.doFilter(request, response); 
                } else {
                    httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado.");
                }
            } else if (path.startsWith("/inventario") || path.startsWith("/gestion")) {
                if ("ADMIN".equals(rol) || "TRABAJADOR".equals(rol)) {
                    chain.doFilter(request, response);
                } else {
                    httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado.");
                }
            } else {
                chain.doFilter(request, response);
            }
        } else {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.jsp");
        }
    }
}