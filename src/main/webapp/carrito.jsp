<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:include page="/componentes/header.jsp" />

<div class="container mt-4">
    <h1 class="titulo-pagina">Mi Carrito de Compras</h1>

    <c:choose>
        <c:when test="${not empty sessionScope.carrito and not empty sessionScope.carrito.items}">
            <div class="card">
                <div class="table-responsive">
                    <table class="table table-hover align-middle mb-0">
                        <thead>
                            <tr class="table-light">
                                <th style="width: 10%;">Producto</th>
                                <th style="width: 35%;"></th>
                                <th class="text-center" style="width: 15%;">Precio</th>
                                <th class="text-center" style="width: 20%;">Cantidad</th>
                                <th class="text-center" style="width: 15%;">Subtotal</th>
                                <th style="width: 5%;"></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="item" items="${sessionScope.carrito.items}">
                                <tr>
                                    <td>
                                        <img src="${pageContext.request.contextPath}/imagenes/${item.producto.imagenUrl}" class="img-fluid rounded" style="max-width: 80px;">
                                    </td>
                                    <td>
                                        <h5><c:out value="${item.producto.nombre}"/></h5>
                                        <small class="text-muted">Stock: ${item.producto.stock}</small>
                                    </td>
                                    <td class="text-center">
                                        <fmt:formatNumber value="${item.producto.precioVenta}" type="currency" currencySymbol="S/ "/>
                                    </td>
                                    <td class="text-center">
                                        <form action="carrito" method="post" class="d-flex justify-content-center align-items-center">
                                            <input type="hidden" name="accion" value="actualizar">
                                            <input type="hidden" name="productoId" value="${item.producto.id}">
                                            <input type="number" name="cantidad" value="${item.cantidad}" min="1" max="${item.producto.stock}" class="form-control form-control-sm" style="width: 80px;">
                                            <button type="submit" class="btn btn-outline-secondary btn-sm ms-2">✓</button>
                                        </form>
                                    </td>
                                    <td class="text-center fw-bold">
                                        <fmt:formatNumber value="${item.subtotal}" type="currency" currencySymbol="S/ "/>
                                    </td>
                                    <td class="text-center">
                                         <form action="carrito" method="post">
                                            <input type="hidden" name="accion" value="eliminar">
                                            <input type="hidden" name="productoId" value="${item.producto.id}">
                                            <button type="submit" class="btn btn-danger btn-sm">&times;</button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
                <div class="card-footer d-flex justify-content-between align-items-center">
                    <div>
                        <a href="${pageContext.request.contextPath}/catalogo" class="btn btn-secondary">Seguir Comprando</a>
                    </div>
                    <div class="text-end">
                        <h4>Total: <span class="text-success fw-bold"><fmt:formatNumber value="${sessionScope.carrito.total}" type="currency" currencySymbol="S/ "/></span></h4>
                        
                        <%-- Este botón llevará a la Fase 3 --%>
                        <form action="pedido" method="post">
                             <input type="hidden" name="accion" value="finalizar">
                             <button type="submit" class="btn btn-success btn-lg">Finalizar Compra</button>
                        </form>
                    </div>
                </div>
            </div>
        </c:when>
        <c:otherwise>
            <div class="alert alert-info text-center">
                <p class="fs-4">Tu carrito de compras está vacío.</p>
                <a href="${pageContext.request.contextPath}/catalogo" class="btn btn-primary">Ir al Catálogo</a>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="/componentes/footer.jsp" />