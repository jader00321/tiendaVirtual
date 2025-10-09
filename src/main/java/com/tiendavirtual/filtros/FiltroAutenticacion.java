package com.tiendavirtual.filtros;

import com.tiendavirtual.modelo.Usuario;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter(urlPatterns = {"/inventario"})
public class FiltroAutenticacion implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest solicitudHttp = (HttpServletRequest) request;
        HttpServletResponse respuestaHttp = (HttpServletResponse) response;
        HttpSession sesion = solicitudHttp.getSession(false);

        boolean sesionIniciada = sesion != null && sesion.getAttribute("usuario") != null;

        if (sesionIniciada) {
            Usuario usuario = (Usuario) sesion.getAttribute("usuario");
            String rol = usuario.getRol();

            if ("ADMIN".equals(rol) || "TRABAJADOR".equals(rol)) {
                chain.doFilter(request, response);
            } else {
                respuestaHttp.sendRedirect(solicitudHttp.getContextPath() + "/index");
            }
        } else {
            respuestaHttp.sendRedirect(solicitudHttp.getContextPath() + "/login.jsp");
        }
    }
}
