<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="/componentes/header.jsp" />

<div class="container mt-4">
    <%-- Bloque para mensajes --%>
    <c:if test="${not empty sessionScope.mensaje}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <c:out value="${sessionScope.mensaje}"/>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <c:remove var="mensaje" scope="session" />
    </c:if>

    <h1 class="titulo-pagina">Panel de Gestión</h1>

    <div class="tarjeta-contenido card mb-5">
        <div class="tarjeta-contenido__encabezado card-header"><h4>Gestión de Categorías</h4></div>
        <div class="tarjeta-contenido__cuerpo card-body">
            <form action="gestion" method="post" enctype="multipart/form-data" class="mb-4 p-3 border rounded bg-light">
                 <input type="hidden" name="accion" value="${not empty categoriaParaEditar ? 'actualizarCategoria' : 'agregarCategoria'}">
                <c:if test="${not empty categoriaParaEditar}">
                    <input type="hidden" name="idCategoria" value="${categoriaParaEditar.id}">
                    <input type="hidden" name="imagenUrlActual" value="${categoriaParaEditar.imagenUrl}">
                </c:if>
                <h5><c:out value="${not empty categoriaParaEditar ? 'Editando Categoría' : 'Agregar Nueva Categoría'}"/></h5>
                <div class="row g-3 align-items-end">
                    <div class="col-md-5"><input type="text" class="form-control" name="nombreCategoria" value="${categoriaParaEditar.nombre}" placeholder="Nombre de la categoría" required></div>
                    <div class="col-md-5"><input type="file" class="form-control" name="imagenCategoria" accept="image/*"></div>
                    <div class="col-md-2 d-grid">
                        <button type="submit" class="btn ${not empty categoriaParaEditar ? 'btn-success' : 'btn-primary'}">${not empty categoriaParaEditar ? 'Guardar' : 'Agregar'}</button>
                    </div>
                </div>
                 <c:if test="${not empty categoriaParaEditar}"><a href="gestion" class="d-block mt-2">Cancelar edición</a></c:if>
            </form>

            <div class="table-responsive">
                <table class="table table-striped table-hover">
                    <thead><tr><th>#</th><th>Nombre</th><th>Imagen</th><th class="text-end">Acciones</th></tr></thead>
                    <tbody>
                        <c:forEach var="cat" items="${listaCategorias}" varStatus="loop">
                            <tr>
                                <td>${loop.count}</td><td><c:out value="${cat.nombre}"/></td>
                                <td><img src="${pageContext.request.contextPath}/imagenes/${cat.imagenUrl}" style="width: 50px;"></td>
                                <td class="text-end">
                                    <a href="gestion?accion=editarCategoria&id=${cat.id}" class="btn btn-warning btn-sm">Editar</a>
                                    <form action="gestion" method="post" style="display:inline;" onsubmit="return confirm('¿Está seguro?');">
                                        <input type="hidden" name="accion" value="eliminarCategoria">
                                        <input type="hidden" name="id" value="${cat.id}">
                                        <button type="submit" class="btn btn-danger btn-sm">Eliminar</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <div class="tarjeta-contenido card mb-4">
        <div class="tarjeta-contenido__encabezado card-header"><h4>Gestión de Proveedores</h4></div>
        <div class="tarjeta-contenido__cuerpo card-body">
             <form action="gestion" method="post" class="mb-4 p-3 border rounded bg-light">
                <input type="hidden" name="accion" value="${not empty proveedorParaEditar ? 'actualizarProveedor' : 'agregarProveedor'}">
                <c:if test="${not empty proveedorParaEditar}"><input type="hidden" name="idProveedor" value="${proveedorParaEditar.id}"></c:if>
                <h5><c:out value="${not empty proveedorParaEditar ? 'Editando Proveedor' : 'Agregar Nuevo Proveedor'}"/></h5>
                <div class="row g-3 align-items-end">
                    <div class="col-md-5"><input type="text" class="form-control" name="nombreProveedor" value="${proveedorParaEditar.nombre}" placeholder="Nombre del proveedor" required></div>
                    <div class="col-md-5"><input type="text" class="form-control" name="contactoProveedor" value="${proveedorParaEditar.contacto}" placeholder="Contacto (email/teléfono)"></div>
                    <div class="col-md-2 d-grid"><button type="submit" class="btn ${not empty proveedorParaEditar ? 'btn-success' : 'btn-primary'}">${not empty proveedorParaEditar ? 'Guardar' : 'Agregar'}</button></div>
                </div>
                 <c:if test="${not empty proveedorParaEditar}"><a href="gestion" class="d-block mt-2">Cancelar edición</a></c:if>
            </form>

            <div class="table-responsive">
                <table class="table table-striped table-hover">
                    <thead><tr><th>#</th><th>Nombre</th><th>Contacto</th><th class="text-end">Acciones</th></tr></thead>
                    <tbody>
                        <c:forEach var="prov" items="${listaProveedores}" varStatus="loop">
                            <tr>
                                <td>${loop.count}</td><td><c:out value="${prov.nombre}"/></td><td><c:out value="${prov.contacto}"/></td>
                                <td class="text-end">
                                    <a href="gestion?accion=editarProveedor&id=${prov.id}" class="btn btn-warning btn-sm">Editar</a>
                                    <form action="gestion" method="post" style="display:inline;" onsubmit="return confirm('¿Está seguro?');">
                                        <input type="hidden" name="accion" value="eliminarProveedor">
                                        <input type="hidden" name="id" value="${prov.id}">
                                        <button type="submit" class="btn btn-danger btn-sm">Eliminar</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/componentes/footer.jsp" />