<%-- Ruta: /webapp/historial_pedidos.jsp --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:include page="/componentes/header.jsp" />

<div class="container mt-4">
    <h1 class="titulo-pagina">Mi Historial de Compras</h1>

    <div class="card">
        <div class="card-body">
            <c:choose>
                <c:when test="${not empty listaPedidos}">
                    <div class="table-responsive">
                        <table class="table table-hover">
                            <thead>
                                <tr>
                                    <th>#</th>
                                    <th>Código de Boleta</th>
                                    <th>Fecha</th>
                                    <th class="text-end">Total</th>
                                    <th class="text-center">Acciones</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="pedido" items="${listaPedidos}" varStatus="loop">
                                    <tr>
                                        <td>${loop.count}</td>
                                        <td><c:out value="${pedido.codigoBoleta}"/></td>
                                        <td><fmt:formatDate value="${pedido.fecha}" pattern="dd/MM/yyyy 'a las' HH:mm"/></td>
                                        <td class="text-end fw-bold"><fmt:formatNumber value="${pedido.total}" type="currency" currencySymbol="S/ "/></td>
                                        <td class="text-center">
                                            <a href="pedido?pedidoId=${pedido.id}" class="btn btn-primary btn-sm">Ver Boleta</a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="alert alert-info text-center">Aún no has realizado ninguna compra.</div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<jsp:include page="/componentes/footer.jsp" />