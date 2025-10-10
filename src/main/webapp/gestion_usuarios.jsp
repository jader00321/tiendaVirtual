<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="/componentes/header.jsp" />

<div class="container mt-4">
    <h1 class="titulo-pagina">Gestión de Usuarios</h1>

    <c:if test="${not empty sessionScope.mensaje}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <c:out value="${sessionScope.mensaje}"/>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <c:remove var="mensaje" scope="session" />
    </c:if>

    <div class="card shadow-sm">
        <div class="card-header">
            <h5>Listado de Usuarios Registrados</h5>
        </div>
        <div class="card-body">
            <div class="table-responsive">
                <table class="table table-striped table-hover align-middle">
                    <thead class="table-dark">
                        <tr>
                            <th>#</th>
                            <th>ID</th>
                            <th>Nombre</th>
                            <th>Email</th>
                            <th>Rol</th>
                            <th class="text-center">Estado</th>
                            <th class="text-center">Acción de Estado</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="u" items="${listaUsuarios}" varStatus="loop">
                            <tr>
                                <td>${loop.count}</td>
                                <td>${u.id}</td>
                                <td><c:out value="${u.nombre}"/></td>
                                <td><c:out value="${u.email}"/></td>
                                <td>
                                    <%-- Formulario para cambiar el rol --%>
                                    <form action="${pageContext.request.contextPath}/admin/usuarios" method="post" class="d-flex align-items-center gap-2">
                                        <input type="hidden" name="accion" value="cambiarRol">
                                        <input type="hidden" name="id" value="${u.id}">
                                        <select name="rol" class="form-select form-select-sm" style="min-width: 120px;" ${sessionScope.usuario.id == u.id ? 'disabled' : ''}>
                                            <option value="ADMIN" ${u.rol == 'ADMIN' ? 'selected' : ''}>ADMIN</option>
                                            <option value="TRABAJADOR" ${u.rol == 'TRABAJADOR' ? 'selected' : ''}>TRABAJADOR</option>
                                            <option value="CLIENTE" ${u.rol == 'CLIENTE' ? 'selected' : ''}>CLIENTE</option>
                                        </select>
                                        <c:if test="${sessionScope.usuario.id != u.id}">
                                            <button type="submit" class="btn btn-primary btn-sm">Guardar</button>
                                        </c:if>
                                    </form>
                                </td>
                                <td class="text-center">
                                    <span class="badge ${u.activo ? 'bg-success' : 'bg-danger'}">
                                        ${u.activo ? 'Activo' : 'Inactivo'}
                                    </span>
                                </td>
                                <td class="text-center">
                                    <%-- Un admin no puede desactivarse a sí mismo --%>
                                    <c:if test="${sessionScope.usuario.id != u.id}">
                                        <%-- Formulario para cambiar el estado --%>
                                        <form action="${pageContext.request.contextPath}/admin/usuarios" method="post">
                                            <input type="hidden" name="accion" value="cambiarEstado">
                                            <input type="hidden" name="id" value="${u.id}">
                                            <button type="submit" class="btn ${u.activo ? 'btn-warning' : 'btn-success'} btn-sm">
                                                ${u.activo ? 'Desactivar' : 'Activar'}
                                            </button>
                                        </form>
                                    </c:if>
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