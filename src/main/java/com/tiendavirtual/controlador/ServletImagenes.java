package com.tiendavirtual.controlador;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/imagenes/*")
public class ServletImagenes extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private String rutaImagenes;

    public void init() throws ServletException {
        String userHome = System.getProperty("user.home");
        rutaImagenes = userHome + File.separator + "tienda_imagenes";
        File directorioImagenes = new File(rutaImagenes);
        if (!directorioImagenes.exists()) {
            directorioImagenes.mkdirs();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String imagenSolicitada = request.getPathInfo();

        if (imagenSolicitada == null || imagenSolicitada.equals("/")) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        File archivoImagen = new File(rutaImagenes, imagenSolicitada);

        if (!archivoImagen.exists() || archivoImagen.isDirectory()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String tipoContenido = getServletContext().getMimeType(archivoImagen.getName());
        if (tipoContenido == null) {
            tipoContenido = "application/octet-stream";
        }

        response.setContentType(tipoContenido);
        response.setContentLength((int) archivoImagen.length());

        Files.copy(archivoImagen.toPath(), response.getOutputStream());
    }
}
