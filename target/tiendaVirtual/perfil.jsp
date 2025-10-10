<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/componentes/header.jsp" />

<div class="container mt-4">
    <h1 class="titulo-pagina">Mi Perfil</h1>

    <c:if test="${not empty sessionScope.mensaje_perfil}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <c:out value="${sessionScope.mensaje_perfil}"/>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <c:remove var="mensaje_perfil" scope="session" />
    </c:if>

    <div class="row justify-content-center">
        <div class="col-lg-8">
            <div class="card">
                <div class="card-header">
                    <h4>Información de la Cuenta</h4>
                </div>
                <div class="card-body">
                    <form action="${pageContext.request.contextPath}/usuario" method="post">
                        <input type="hidden" name="accion" value="actualizarPerfil">
                        <div class="mb-3 row">
                            <label class="col-sm-3 col-form-label">Nombre:</label>
                            <div class="col-sm-9">
                                <input type="text" class="form-control" name="nombre" value="${sessionScope.usuario.nombre}" required>
                            </div>
                        </div>
                        <div class="mb-3 row">
                            <label class="col-sm-3 col-form-label">Correo:</label>
                            <div class="col-sm-9">
                                <input type="email" class="form-control" value="${sessionScope.usuario.email}" disabled readonly>
                            </div>
                        </div>
                        <div class="mb-3 row">
                            <label class="col-sm-3 col-form-label">Rol:</label>
                            <div class="col-sm-9">
                                 <input type="text" class="form-control" value="${sessionScope.usuario.rol}" disabled readonly>
                            </div>
                        </div>
                        <hr>
                        <p class="text-muted">Dejar los campos de contraseña en blanco para no cambiarla.</p>
                        <div class="mb-3 row">
                            <label for="password" class="col-sm-3 col-form-label">Nueva Contraseña:</label>
                            <div class="col-sm-9">
                                <input type="password" class="form-control" id="password" name="password">
                            </div>
                        </div>
                        <div class="mb-3 row">
                            <label for="confirmPassword" class="col-sm-3 col-form-label">Confirmar Contraseña:</label>
                            <div class="col-sm-9">
                                <input type="password" class="form-control" id="confirmPassword" name="confirmPassword">
                            </div>
                        </div>
                        <div class="d-flex justify-content-end">
                            <button type="submit" class="btn btn-primary">Guardar Cambios</button>
                        </div>
                    </form>
                </div>
                <div class="card-footer">
                    <a href="pedido?accion=historial" class="btn btn-outline-primary">Ver mi Historial de Compras</a>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/componentes/footer.jsp" />