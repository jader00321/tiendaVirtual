package com.tiendavirtual.controlador;

import com.tiendavirtual.dao.CategoriaDAO;
import com.tiendavirtual.dao.ProductoDAO;
import com.tiendavirtual.dao.ProveedorDAO;
import com.tiendavirtual.modelo.Categoria;
import com.tiendavirtual.modelo.Producto;
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
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet("/inventario")
@MultipartConfig
public class ProductoControlador extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProductoDAO productoDAO;
    private ProveedorDAO proveedorDAO;
    private CategoriaDAO categoriaDAO;
    private String uploadPath;

    public void init() {
        productoDAO = new ProductoDAO();
        proveedorDAO = new ProveedorDAO();
        categoriaDAO = new CategoriaDAO();
        String userHome = System.getProperty("user.home");
        uploadPath = userHome + File.separator + "tienda_imagenes";
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String accion = request.getParameter("accion");
        procesarPeticion(request, response, accion);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accion = request.getParameter("accion");
        procesarPeticion(request, response, accion);
    }

    private void procesarPeticion(HttpServletRequest request, HttpServletResponse response, String accion) throws ServletException, IOException {
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
            ex.printStackTrace();
            throw new ServletException("Error de base de datos o de clase no encontrada", ex);
        }
    }
    
    private void listarProductos(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException, ClassNotFoundException {
        String busquedaNombre = request.getParameter("busquedaNombre");
        String filtroCategoriaStr = request.getParameter("filtroCategoria");
        
        Integer filtroCategoriaId = null;
        if (filtroCategoriaStr != null && !filtroCategoriaStr.isEmpty()) {
            try {
                filtroCategoriaId = Integer.parseInt(filtroCategoriaStr);
            } catch (NumberFormatException e) {
                filtroCategoriaId = null;
            }
        }
        
        List<Producto> listaProductos = productoDAO.listar(filtroCategoriaId, busquedaNombre);
        List<Proveedor> listaProveedores = proveedorDAO.listar();
        List<Categoria> listaCategorias = categoriaDAO.listar();
        
        Map<Integer, String> mapaProveedores = listaProveedores.stream().collect(Collectors.toMap(Proveedor::getId, Proveedor::getNombre));
        Map<Integer, String> mapaCategorias = listaCategorias.stream().collect(Collectors.toMap(Categoria::getId, Categoria::getNombre));

        request.setAttribute("listaProductos", listaProductos);
        request.setAttribute("listaProveedores", listaProveedores);
        request.setAttribute("listaCategorias", listaCategorias);
        request.setAttribute("mapaProveedores", mapaProveedores);
        request.setAttribute("mapaCategorias", mapaCategorias);
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("inventario.jsp");
        dispatcher.forward(request, response);
    }

    private void mostrarFormularioEdicion(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException, ClassNotFoundException {
        int id = Integer.parseInt(request.getParameter("id"));
        Producto productoExistente = productoDAO.buscarPorId(id);
        request.setAttribute("producto", productoExistente);
        listarProductos(request, response);
    }

    private void agregarProducto(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, ClassNotFoundException {
        try {
            Producto nuevoProducto = new Producto();

            String codigo = "PROD-" + System.currentTimeMillis();
            nuevoProducto.setCodigo(codigo);
            
            nuevoProducto.setNombre(request.getParameter("nombre"));
            nuevoProducto.setDescripcion(request.getParameter("descripcion"));
            nuevoProducto.setPrecioVenta(Double.parseDouble(request.getParameter("precioVenta")));
            nuevoProducto.setStock(Integer.parseInt(request.getParameter("stock")));
            
            String precioCostoStr = request.getParameter("precioCosto");
            if (precioCostoStr != null && !precioCostoStr.isEmpty()) {
                nuevoProducto.setPrecioCosto(Double.parseDouble(precioCostoStr));
            }
            
            String categoriaIdStr = request.getParameter("categoriaId");
            if (categoriaIdStr != null && !categoriaIdStr.isEmpty()) {
                nuevoProducto.setCategoriaId(Integer.parseInt(categoriaIdStr));
            }

            String proveedorIdStr = request.getParameter("proveedorId");
            if (proveedorIdStr != null && !proveedorIdStr.isEmpty()) {
                nuevoProducto.setProveedorId(Integer.parseInt(proveedorIdStr));
            }

            Part filePart = request.getPart("imagen");
            String imagenUrl = guardarImagen(filePart);
            nuevoProducto.setImagenUrl(imagenUrl);
            
            productoDAO.agregar(nuevoProducto);
            request.getSession().setAttribute("mensaje", "Producto '" + nuevoProducto.getNombre() + "' agregado con código " + codigo);
        } catch (NumberFormatException | ServletException e) {
            request.getSession().setAttribute("mensaje", "Error: Los campos numéricos deben ser válidos.");
        }
        response.sendRedirect("inventario");
    }

    private void actualizarProducto(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, ClassNotFoundException {
        try {
            Producto producto = new Producto();
            producto.setId(Integer.parseInt(request.getParameter("id")));
            producto.setCodigo(request.getParameter("codigo"));
            producto.setNombre(request.getParameter("nombre"));
            producto.setDescripcion(request.getParameter("descripcion"));
            producto.setPrecioVenta(Double.parseDouble(request.getParameter("precioVenta")));
            producto.setStock(Integer.parseInt(request.getParameter("stock")));

            String precioCostoStr = request.getParameter("precioCosto");
            if (precioCostoStr != null && !precioCostoStr.isEmpty()) {
                producto.setPrecioCosto(Double.parseDouble(precioCostoStr));
            }

            String categoriaIdStr = request.getParameter("categoriaId");
            if (categoriaIdStr != null && !categoriaIdStr.isEmpty()) {
                producto.setCategoriaId(Integer.parseInt(categoriaIdStr));
            }
            
            String proveedorIdStr = request.getParameter("proveedorId");
            if (proveedorIdStr != null && !proveedorIdStr.isEmpty()) {
                producto.setProveedorId(Integer.parseInt(proveedorIdStr));
            }
            
            Part filePart = request.getPart("imagen");
            String nuevaImagenUrl = guardarImagen(filePart);
            String imagenUrlActual = request.getParameter("imagenUrlActual");
            producto.setImagenUrl((nuevaImagenUrl != null && !nuevaImagenUrl.isEmpty()) ? nuevaImagenUrl : imagenUrlActual);

            productoDAO.actualizar(producto);
            request.getSession().setAttribute("mensaje", "Producto con código '" + producto.getCodigo() + "' actualizado exitosamente.");
        } catch (NumberFormatException | ServletException e) {
            request.getSession().setAttribute("mensaje", "Error: Los campos numéricos deben ser válidos al actualizar.");
        }
        response.sendRedirect("inventario");
    }

    private String guardarImagen(Part filePart) throws IOException {
        if (filePart == null || filePart.getSize() == 0) {
            return null;
        }
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        if (fileName.isEmpty()) {
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