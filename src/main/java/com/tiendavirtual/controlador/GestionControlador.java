package com.tiendavirtual.controlador;

import com.tiendavirtual.dao.CategoriaDAO;
import com.tiendavirtual.dao.ProveedorDAO;
import com.tiendavirtual.modelo.Categoria;
import com.tiendavirtual.modelo.Proveedor;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/gestion")
@MultipartConfig
public class GestionControlador extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private CategoriaDAO categoriaDAO;
    private ProveedorDAO proveedorDAO;
    private String uploadPath;

    @Override
    public void init() {
        categoriaDAO = new CategoriaDAO();
        proveedorDAO = new ProveedorDAO();
        String userHome = System.getProperty("user.home");
        uploadPath = userHome + File.separator + "tienda_imagenes";
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "listar";
        }
        
        try {
            switch (accion) {
                case "editarCategoria":
                    mostrarFormularioEdicionCategoria(request, response);
                    break;
                case "eliminarCategoria":
                    eliminarCategoria(request, response);
                    break;
                case "editarProveedor":
                    mostrarFormularioEdicionProveedor(request, response);
                    break;
                case "eliminarProveedor":
                    eliminarProveedor(request, response);
                    break;
                default:
                    mostrarPaginaGestion(request, response, null, null);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new ServletException("Error al procesar la acción GET en Gestión", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accion = request.getParameter("accion");
        if (accion == null) {
            doGet(request, response);
            return;
        }

        try {
            switch (accion) {
                case "agregarCategoria":
                    agregarCategoria(request, response);
                    break;
                case "actualizarCategoria":
                    actualizarCategoria(request, response);
                    break;
                case "agregarProveedor":
                    agregarProveedor(request, response);
                    break;
                case "actualizarProveedor":
                    actualizarProveedor(request, response);
                    break;
                default:
                    doGet(request, response);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new ServletException("Error al procesar la acción POST en Gestión", e);
        }
    }

    private void mostrarPaginaGestion(HttpServletRequest request, HttpServletResponse response, Categoria categoriaParaEditar, Proveedor proveedorParaEditar) throws SQLException, ClassNotFoundException, ServletException, IOException {
        List<Categoria> listaCategorias = categoriaDAO.listar();
        List<Proveedor> listaProveedores = proveedorDAO.listar();

        request.setAttribute("listaCategorias", listaCategorias);
        request.setAttribute("listaProveedores", listaProveedores);
        request.setAttribute("categoriaParaEditar", categoriaParaEditar);
        request.setAttribute("proveedorParaEditar", proveedorParaEditar);

        RequestDispatcher dispatcher = request.getRequestDispatcher("gestion.jsp");
        dispatcher.forward(request, response);
    }

    private void mostrarFormularioEdicionCategoria(HttpServletRequest request, HttpServletResponse response) throws NumberFormatException, SQLException, ClassNotFoundException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Categoria categoriaParaEditar = categoriaDAO.buscarPorId(id);
        mostrarPaginaGestion(request, response, categoriaParaEditar, null);
    }

    private void agregarCategoria(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, SQLException, ClassNotFoundException {
        String nombre = request.getParameter("nombreCategoria");
        Part filePart = request.getPart("imagenCategoria");
        String imagenUrl = guardarImagen(filePart);

        Categoria nuevaCategoria = new Categoria();
        nuevaCategoria.setNombre(nombre);
        nuevaCategoria.setImagenUrl(imagenUrl);
        
        categoriaDAO.agregar(nuevaCategoria);
        request.getSession().setAttribute("mensaje", "Categoría '" + nombre + "' agregada exitosamente.");
        response.sendRedirect("gestion");
    }

    private void actualizarCategoria(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, SQLException, ClassNotFoundException {
        int id = Integer.parseInt(request.getParameter("idCategoria"));
        String nombre = request.getParameter("nombreCategoria");
        Part filePart = request.getPart("imagenCategoria");
        
        String nuevaImagenUrl = guardarImagen(filePart);
        String imagenUrlActual = request.getParameter("imagenUrlActual");
        String imagenUrl = (nuevaImagenUrl != null && !nuevaImagenUrl.isEmpty()) ? nuevaImagenUrl : imagenUrlActual;

        Categoria categoria = new Categoria();
        categoria.setId(id);
        categoria.setNombre(nombre);
        categoria.setImagenUrl(imagenUrl);
        
        categoriaDAO.actualizar(categoria);
        request.getSession().setAttribute("mensaje", "Categoría '" + nombre + "' actualizada exitosamente.");
        response.sendRedirect("gestion");
    }

    private void eliminarCategoria(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, SQLException, ClassNotFoundException {
        int id = Integer.parseInt(request.getParameter("id"));
        Categoria categoria = categoriaDAO.buscarPorId(id);
        if (categoria != null && categoria.getImagenUrl() != null && !categoria.getImagenUrl().isEmpty()) {
            File imageFile = new File(uploadPath, categoria.getImagenUrl());
            if (imageFile.exists()) {
                imageFile.delete();
            }
        }
        
        try {
            categoriaDAO.eliminar(id);
            request.getSession().setAttribute("mensaje", "Categoría eliminada exitosamente.");
        } catch (SQLException e) {
            request.getSession().setAttribute("mensaje", "Error: No se puede eliminar la categoría porque está asignada a uno o más productos.");
        }
        
        response.sendRedirect("gestion");
    }
    
    private void mostrarFormularioEdicionProveedor(HttpServletRequest request, HttpServletResponse response) throws NumberFormatException, SQLException, ClassNotFoundException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Proveedor proveedorParaEditar = proveedorDAO.buscarPorId(id);
        mostrarPaginaGestion(request, response, null, proveedorParaEditar);
    }

    private void actualizarProveedor(HttpServletRequest request, HttpServletResponse response) throws SQLException, ClassNotFoundException, IOException {
        int id = Integer.parseInt(request.getParameter("idProveedor"));
        String nombre = request.getParameter("nombreProveedor");
        String contacto = request.getParameter("contactoProveedor");

        Proveedor proveedor = new Proveedor();
        proveedor.setId(id);
        proveedor.setNombre(nombre);
        proveedor.setContacto(contacto);

        proveedorDAO.actualizar(proveedor);
        request.getSession().setAttribute("mensaje", "Proveedor '" + nombre + "' actualizado exitosamente.");
        response.sendRedirect("gestion");
    }

    private void eliminarProveedor(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, SQLException, ClassNotFoundException {
        int id = Integer.parseInt(request.getParameter("id"));
        try {
            proveedorDAO.eliminar(id);
            request.getSession().setAttribute("mensaje", "Proveedor eliminado exitosamente.");
        } catch (SQLException e) {
            request.getSession().setAttribute("mensaje", "Error: No se puede eliminar el proveedor porque está asignado a uno o más productos.");
        }
        response.sendRedirect("gestion");
    }
    
    private String guardarImagen(Part filePart) throws IOException {
        if (filePart == null || filePart.getSize() == 0) {
            return null;
        }
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        if (fileName.isEmpty()) {
            return null;
        }
        String uniqueFileName = "cat_" + System.currentTimeMillis() + "_" + fileName;
        File file = new File(uploadPath, uniqueFileName);
        try (InputStream input = filePart.getInputStream()) {
            Files.copy(input, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        return uniqueFileName;
    }

    private void agregarProveedor(HttpServletRequest request, HttpServletResponse response) throws SQLException, ClassNotFoundException, IOException {
        String nombre = request.getParameter("nombreProveedor");
        String contacto = request.getParameter("contactoProveedor");

        if (nombre != null && !nombre.isEmpty()) {
            Proveedor nuevoProveedor = new Proveedor();
            nuevoProveedor.setNombre(nombre);
            nuevoProveedor.setContacto(contacto);
            proveedorDAO.agregar(nuevoProveedor);
            request.getSession().setAttribute("mensaje", "Proveedor agregado exitosamente.");
        } else {
            request.getSession().setAttribute("mensaje", "Error: El nombre del proveedor no puede estar vacío.");
        }
        response.sendRedirect("gestion");
    }
}