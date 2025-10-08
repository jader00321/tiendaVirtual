<%-- Ruta: /webapp/inventario.jsp --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:include page="/componentes/header.jsp" />

<%-- Bloque para mensajes de sesión --%>
<c:if test="${not empty sessionScope.mensaje}">
    <div class="alert alert-success alert-dismissible fade show mt-3" role="alert">
        <c:out value="${sessionScope.mensaje}"/>
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
    <c:remove var="mensaje" scope="session" />
</c:if>

<h1 class="titulo-pagina">Gestión de Inventario</h1>

<%-- Formulario para Agregar/Editar Productos --%>
<div class="tarjeta-contenido card mb-4">
    <div class="tarjeta-contenido__encabezado card-header">
        <h2><c:out value="${not empty producto ? 'Editar Producto' : 'Agregar Nuevo Producto'}"/></h2>
    </div>
    <div class="tarjeta-contenido__cuerpo card-body">
        <form action="inventario" method="post" enctype="multipart/form-data">
            <c:choose>
                <c:when test="${not empty producto}">
                    <input type="hidden" name="accion" value="actualizar">
                    <input type="hidden" name="id" value="${producto.id}">
                    <input type="hidden" name="imagenUrlActual" value="${producto.imagenUrl}">
                </c:when>
                <c:otherwise>
                    <input type="hidden" name="accion" value="agregar">
                </c:otherwise>
            </c:choose>

            <div class="row g-3">
                <c:if test="${not empty producto}">
                    <div class="col-md-6">
                        <label for="codigo" class="form-label">Código de Producto</label>
                        <input type="text" id="codigo" name="codigo" value="${producto.codigo}" class="form-control" readonly>
                    </div>
                </c:if>
                <div class="col-md-${not empty producto ? '6' : '12'}">
                    <label for="nombre" class="form-label">Nombre del Producto</label>
                    <input type="text" id="nombre" name="nombre" value="${producto.nombre}" class="form-control" required>
                </div>

                <%-- El resto del formulario se mantiene igual que la versión anterior --%>
                <div class="col-md-6">
                    <label for="categoriaId" class="form-label">Categoría</label>
                    <select id="categoriaId" name="categoriaId" class="form-select" required>
                        <option value="">-- Seleccione --</option>
                        <c:forEach var="cat" items="${listaCategorias}">
                            <option value="${cat.id}" ${producto.categoriaId == cat.id ? 'selected' : ''}><c:out value="${cat.nombre}"/></option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-6">
                    <label for="proveedorId" class="form-label">Proveedor</label>
                    <select id="proveedorId" name="proveedorId" class="form-select">
                        <option value="">-- Sin Proveedor --</option>
                        <c:forEach var="prov" items="${listaProveedores}">
                            <option value="${prov.id}" ${producto.proveedorId == prov.id ? 'selected' : ''}><c:out value="${prov.nombre}"/></option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-4">
                    <label for="precioCosto" class="form-label">Precio de Costo (S/.)</label>
                    <input type="number" id="precioCosto" name="precioCosto" step="0.01" min="0" value="${producto.precioCosto}" class="form-control">
                </div>
                <div class="col-md-4">
                    <label for="precioVenta" class="form-label">Precio de Venta (S/.)</label>
                    <input type="number" id="precioVenta" name="precioVenta" step="0.01" min="0" value="${producto.precioVenta}" class="form-control" required>
                </div>
                <div class="col-md-4">
                    <label for="stock" class="form-label">Stock Disponible</label>
                    <input type="number" id="stock" name="stock" min="0" value="${producto.stock}" class="form-control" required>
                </div>
                <div class="col-12">
                    <label for="descripcion" class="form-label">Descripción</label>
                    <textarea id="descripcion" name="descripcion" rows="3" class="form-control">${producto.descripcion}</textarea>
                </div>
                <div class="col-12">
                    <label for="imagen" class="form-label">Imagen del Producto</label>
                    <input type="file" id="imagen" name="imagen" accept="image/*" class="form-control">
                    <c:if test="${not empty producto.imagenUrl}">
                        <div class="mt-2"><small>Imagen actual:</small><br><img src="${pageContext.request.contextPath}/imagenes/${producto.imagenUrl}" style="max-width: 100px;"></div>
                    </c:if>
                </div>
            </div>
            <div class="formulario-inventario-acciones mt-3">
                 <c:if test="${not empty producto}">
                    <a href="inventario" class="btn btn-secondary">Cancelar Edición</a>
                    <button type="submit" class="btn btn-success">Guardar Cambios</button>
                </c:if>
                <c:if test="${empty producto}">
                    <button type="submit" class="btn btn-primary">Agregar Producto</button>
                </c:if>
            </div>
        </form>
    </div>
</div>

<div class="tarjeta-contenido card mb-4">
    <div class="tarjeta-contenido__encabezado card-header"><h2>Listado de Productos</h2></div>
    <div class="tarjeta-contenido__cuerpo card-body">
        <form action="inventario" method="get" class="row g-3 mb-4 p-3 border rounded bg-light">
            <div class="col-md-5">
                <label for="busquedaNombre" class="form-label">Buscar por Nombre</label>
                <input type="text" class="form-control" id="busquedaNombre" name="busquedaNombre" value="${param.busquedaNombre}">
            </div>
            <div class="col-md-5">
                <label for="filtroCategoria" class="form-label">Filtrar por Categoría</label>
                <select id="filtroCategoria" name="filtroCategoria" class="form-select">
                    <option value="">Todas</option>
                    <c:forEach var="cat" items="${listaCategorias}">
                        <option value="${cat.id}" ${param.filtroCategoria == cat.id ? 'selected' : ''}><c:out value="${cat.nombre}"/></option>
                    </c:forEach>
                </select>
            </div>
            <div class="col-md-2 d-grid">
                <button type="submit" class="btn btn-primary mt-auto">Filtrar</button>
            </div>
        </form>

        <div class="table-responsive">
            <table class="tabla-contenido table table-striped table-hover">
                <thead>
                    <tr>
                        <th>#</th>
                        <th>Código</th>
                        <th>Nombre</th>
                        <th>Categoría</th>
                        <th>Proveedor</th>
                        <th>P. Venta</th>
                        <th>Stock</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="p" items="${listaProductos}" varStatus="loop">
                        <tr>
                            <td>${loop.count}</td>
                            <td><c:out value="${p.codigo}"/></td>
                            <td><c:out value="${p.nombre}"/></td>
                            <td><c:out value="${mapaCategorias[p.categoriaId]}"/></td>
                            <td><c:out value="${mapaProveedores[p.proveedorId]}"/></td>
                            <td><fmt:formatNumber value="${p.precioVenta}" type="currency" currencySymbol="S/ "/></td>
                            <td><c:out value="${p.stock}"/></td>
                            <td>
                                <a href="inventario?accion=editar&id=${p.id}" class="btn btn-warning btn-sm">Editar</a>
                                <a href="inventario?accion=eliminar&id=${p.id}" class="btn btn-danger btn-sm" onclick="return confirm('¿Está seguro?');">Eliminar</a>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty listaProductos}">
                        <tr>
                            <td colspan="8" class="text-center">No se encontraron productos con los filtros aplicados.</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>

<jsp:include page="/componentes/footer.jsp" />