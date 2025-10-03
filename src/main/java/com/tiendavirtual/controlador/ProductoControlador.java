package com.tiendavirtual.controlador;

import com.tiendavirtual.dao.ProductoDAO;
import com.tiendavirtual.modelo.Producto;

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

@WebServlet("/inventario")
@MultipartConfig
public class ProductoControlador extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProductoDAO productoDAO;
    private String uploadPath;

    public void init() {
        productoDAO = new ProductoDAO();
        String userHome = System.getProperty("user.home");
        uploadPath = userHome + File.separator + "tienda_imagenes";
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "listar";
        }

        try {
            switch (accion) {
                case "agregar":
                    agregarProducto(request, response);
                    break;
                case "editar":
                    mostrarFormularioEdicion(request, response);
                    break;
                case "actualizar":
                    actualizarProducto(request, response);
                    break;
                case "eliminar":
                    eliminarProducto(request, response);
                    break;
                default:
                    listarProductos(request, response);
                    break;
            }
        } catch (SQLException | ClassNotFoundException ex) {
            throw new ServletException("Error de base de datos o de clase no encontrada", ex);
        }
    }

    private void listarProductos(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException, ClassNotFoundException {
        List<Producto> listaProductos = productoDAO.listar();
        List<String> listaCategorias = productoDAO.listarCategorias();
        request.setAttribute("listaProductos", listaProductos);
        request.setAttribute("listaCategorias", listaCategorias);
        RequestDispatcher dispatcher = request.getRequestDispatcher("inventario.jsp");
        dispatcher.forward(request, response);
    }

    private void mostrarFormularioEdicion(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException, ClassNotFoundException {
        int id = Integer.parseInt(request.getParameter("id"));
        Producto productoExistente = productoDAO.buscarPorId(id);
        request.setAttribute("producto", productoExistente);
        listarProductos(request, response);
    }

    private void agregarProducto(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException, ClassNotFoundException {
        String nombre = request.getParameter("nombre");
        String descripcion = request.getParameter("descripcion");
        String categoria = request.getParameter("categoria");
        double precio = 0;
        int stock = 0;

        try {
            precio = Double.parseDouble(request.getParameter("precio"));
            stock = Integer.parseInt(request.getParameter("stock"));
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("mensaje", "Error: Precio y stock deben ser números válidos.");
            response.sendRedirect("inventario");
            return;
        }

        Part filePart = request.getPart("imagen");
        String imagenUrl = guardarImagen(filePart);

        Producto nuevoProducto = new Producto();
        nuevoProducto.setNombre(nombre);
        nuevoProducto.setDescripcion(descripcion);
        nuevoProducto.setPrecio(precio);
        nuevoProducto.setStock(stock);
        nuevoProducto.setCategoria(categoria);
        nuevoProducto.setImagenUrl(imagenUrl);
        
        productoDAO.agregar(nuevoProducto);
        request.getSession().setAttribute("mensaje", "Producto agregado exitosamente.");
        response.sendRedirect("inventario");
    }

    private void actualizarProducto(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException, ClassNotFoundException {
        int id = Integer.parseInt(request.getParameter("id"));
        String nombre = request.getParameter("nombre");
        String descripcion = request.getParameter("descripcion");
        String categoria = request.getParameter("categoria");
        double precio = 0;
        int stock = 0;

        try {
            precio = Double.parseDouble(request.getParameter("precio"));
            stock = Integer.parseInt(request.getParameter("stock"));
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("mensaje", "Error: Precio y stock deben ser números válidos.");
            response.sendRedirect("inventario?accion=editar&id=" + id);
            return;
        }

        Part filePart = request.getPart("imagen");
        String nuevaImagenUrl = guardarImagen(filePart);
        String imagenUrl = (nuevaImagenUrl != null) ? nuevaImagenUrl : request.getParameter("imagenUrlActual");

        Producto producto = new Producto();
        producto.setId(id);
        producto.setNombre(nombre);
        producto.setDescripcion(descripcion);
        producto.setPrecio(precio);
        producto.setStock(stock);
        producto.setCategoria(categoria);
        producto.setImagenUrl(imagenUrl);

        productoDAO.actualizar(producto);
        request.getSession().setAttribute("mensaje", "Producto actualizado exitosamente.");
        response.sendRedirect("inventario");
    }

    private String guardarImagen(Part filePart) throws IOException {
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        if (fileName == null || fileName.isEmpty()) {
            return null;
        }
        String uniqueFileName = System.currentTimeMillis() + "_" + fileName;
        File file = new File(uploadPath, uniqueFileName);
        try (InputStream input = filePart.getInputStream()) {
            Files.copy(input, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        return uniqueFileName;
    }

    private void eliminarProducto(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ClassNotFoundException {
        int id = Integer.parseInt(request.getParameter("id"));
        
        Producto producto = productoDAO.buscarPorId(id);
        if (producto != null && producto.getImagenUrl() != null && !producto.getImagenUrl().isEmpty()) {
            File imageFile = new File(uploadPath, producto.getImagenUrl());
            if (imageFile.exists()) {
                imageFile.delete();
            }
        }
        
        productoDAO.eliminar(id);
        request.getSession().setAttribute("mensaje", "Producto eliminado exitosamente.");
        response.sendRedirect("inventario");
    }
}