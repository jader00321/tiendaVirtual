<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:include page="/componentes/header.jsp" />

<h1 class="titulo-pagina"><c:out value="${tituloCatalogo}"/></h1>

<div class="cuadricula-productos">
    <c:forEach var="p" items="${listaProductos}">
        <div class="tarjeta-producto">
            <div class="tarjeta-producto__imagen">
                <c:choose>
                    <c:when test="${not empty p.imagenUrl}">
                        <img src="${pageContext.request.contextPath}/imagenes/${p.imagenUrl}" alt="<c:out value="${p.nombre}"/>">
                    </c:when>
                    <c:otherwise>
                        <img src="${pageContext.request.contextPath}/imagenes/producto_default.png" alt="<c:out value="${p.nombre}"/>">
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="tarjeta-producto__info">
                <h3 class="tarjeta-producto__nombre"><c:out value="${p.nombre}"/></h3>
                <p class="tarjeta-producto__precio"><fmt:formatNumber value="${p.precio}" type="currency" currencySymbol="S/ "/></p>
                <a href="${pageContext.request.contextPath}/usuario?accion=comprar&id=${p.id}" class="boton boton--primario">Comprar</a>
            </div>
        </div>
    </c:forEach>
    <c:if test="${empty listaProductos}">
        <p class="cuadricula-productos__mensaje-vacio">No hay productos disponibles en esta categoría.</p>
    </c:if>
</div>

<jsp:include page="/componentes/footer.jsp" />