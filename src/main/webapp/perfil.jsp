<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/componentes/header.jsp" />

<h1 class="titulo-pagina">Mi Perfil</h1>

<c:choose>
    <c:when test="${not empty sessionScope.usuario}">
        <div class="tarjeta-contenido">
            <div class="tarjeta-contenido__encabezado">
                <h2>Información de la Cuenta</h2>
            </div>
            <div class="tarjeta-contenido__cuerpo">
                <p><strong>Nombre:</strong> <c:out value="${sessionScope.usuario.nombre}"/></p>
                <p><strong>Correo Electrónico:</strong> <c:out value="${sessionScope.usuario.email}"/></p>
                <p><strong>Rol:</strong> <c:out value="${sessionScope.usuario.rol}"/></p>
                <hr>
                <a href="pedido?accion=historial" class="btn btn-outline-primary">Ver mi Historial de Compras</a>
            </div>
        </div>
    </c:when>
    <c:otherwise>
        <div class="alerta alerta--peligro">
            No has iniciado sesión. Por favor, <a href="login.jsp">inicia sesión</a> para ver tu perfil.
        </div>
    </c:otherwise>
</c:choose>

<jsp:include page="/componentes/footer.jsp" />