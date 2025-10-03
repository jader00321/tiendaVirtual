<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:include page="/componentes/header.jsp" />

<c:if test="${not empty sessionScope.mensaje}">
    <div class="alerta alerta--exito">
        ${sessionScope.mensaje}
    </div>
    <c:remove var="mensaje" scope="session" />
</c:if>

<h1 class="titulo-pagina">Gestión de Inventario</h1>

<div class="tarjeta-contenido">
    <div class="tarjeta-contenido__encabezado">
        <h2><c:out value="${not empty producto ? 'Editar Producto' : 'Agregar Nuevo Producto'}"/></h2>
    </div>
    <div class="tarjeta-contenido__cuerpo">
        <form action="inventario" method="post" enctype="multipart/form-data">
            <c:if test="${not empty producto}">
                <input type="hidden" name="accion" value="actualizar">
                <input type="hidden" name="id" value="<c:out value='${producto.id}'/>">
                <input type="hidden" name="imagenUrlActual" value="<c:out value='${producto.imagenUrl}'/>">
            </c:if>
            <c:if test="${empty producto}">
                <input type="hidden" name="accion" value="agregar">
            </c:if>

            <div class="formulario-inventario-grid">
                <div class="formulario-acceso__grupo">
                    <label for="nombre">Nombre del Producto</label>
                    <input type="text" id="nombre" name="nombre" value="<c:out value='${producto.nombre}'/>" required>
                </div>
                <div class="formulario-acceso__grupo">
                    <label for="precio">Precio (S/.)</label>
                    <input type="number" id="precio" name="precio" step="0.01" min="0" value="<c:out value='${producto.precio}'/>" required>
                </div>
                <div class="formulario-acceso__grupo">
                    <label for="stock">Stock Disponible</label>
                    <input type="number" id="stock" name="stock" min="0" value="<c:out value='${producto.stock}'/>" required>
                </div>
                <div class="formulario-acceso__grupo">
                    <label for="categoria">Categoría</label>
                    <select id="categoria" name="categoria" required>
                        <option value="">-- Seleccione una categoría --</option>
                        <option value="Frutas" ${producto.categoria == 'Frutas' ? 'selected' : ''}>Frutas</option>
                        <option value="Verduras" ${producto.categoria == 'Verduras' ? 'selected' : ''}>Verduras</option>
                        <option value="Lacteos" ${producto.categoria == 'Lacteos' ? 'selected' : ''}>Lácteos</option>
                        <option value="Abarrotes" ${producto.categoria == 'Abarrotes' ? 'selected' : ''}>Abarrotes</option>
                    </select>
                </div>
                <div class="formulario-acceso__grupo formulario-grupo--ancho-completo">
                    <label for="descripcion">Descripción</label>
                    <textarea id="descripcion" name="descripcion" rows="3"><c:out value='${producto.descripcion}'/></textarea>
                </div>
                <div class="formulario-acceso__grupo formulario-grupo--ancho-completo">
                    <label for="imagen">Imagen del Producto</label>
                    <input type="file" id="imagen" name="imagen" accept="image/*">
                    <c:if test="${not empty producto.imagenUrl}">
                        <div style="margin-top: 10px;">
                            <p>Imagen actual:</p>
                            <img src="${pageContext.request.contextPath}/imagenes/${producto.imagenUrl}" alt="Imagen de ${producto.nombre}" style="max-width: 150px; max-height: 150px;">
                        </div>
                    </c:if>
                </div>
            </div>
            <div class="formulario-inventario-acciones">
                <c:if test="${not empty producto}">
                    <a href="inventario" class="boton boton--peligro">Cancelar Edición</a>
                    <button type="submit" class="boton boton--exito">Guardar Cambios</button>
                </c:if>
                <c:if test="${empty producto}">
                    <button type="submit" class="boton boton--primario">Agregar Producto</button>
                </c:if>
            </div>
        </form>
    </div>
</div>

<div class="tarjeta-contenido">
    <div class="tarjeta-contenido__encabezado"><h2>Listado de Productos</h2></div>
    <div class="tarjeta-contenido__cuerpo">
        <table class="tabla-contenido">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Nombre</th>
                    <th>Categoría</th>
                    <th>Precio</th>
                    <th>Stock</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="p" items="${listaProductos}">
                    <tr>
                        <td><c:out value="${p.id}"/></td>
                        <td><c:out value="${p.nombre}"/></td>
                        <td><c:out value="${p.categoria}"/></td>
                        <td><fmt:formatNumber value="${p.precio}" type="currency" currencySymbol="S/ "/></td>
                        <td><c:out value="${p.stock}"/></td>
                        <td>
                            <a href="inventario?accion=editar&id=${p.id}" class="boton boton--advertencia">Editar</a>
                            <a href="inventario?accion=eliminar&id=${p.id}" class="boton boton--peligro" onclick="return confirm('¿Está seguro de que desea eliminar este producto?');">Eliminar</a>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty listaProductos}">
                    <tr>
                        <td colspan="6" class="texto-centrado">No hay productos registrados.</td>
                    </tr>
                </c:if>
            </tbody>
        </table>
    </div>
</div>

<jsp:include page="/componentes/footer.jsp" />