<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:include page="/componentes/header.jsp" />

<div class="container mt-4">
    <div class="row">
        <div class="col-lg-3">
            <div class="card position-sticky" style="top: 80px;">
                <div class="card-header"><h5>Filtrar por Categoría</h5></div>
                <div class="list-group list-group-flush">
                    <a href="${pageContext.request.contextPath}/catalogo" class="list-group-item list-group-item-action ${empty param.categoria ? 'active' : ''}">Todas las Categorías</a>
                    <c:forEach var="cat" items="${listaCategorias}">
                        <a href="${pageContext.request.contextPath}/catalogo?categoria=${cat.nombre}" class="list-group-item list-group-item-action ${param.categoria == cat.nombre ? 'active' : ''}"><c:out value="${cat.nombre}"/></a>
                    </c:forEach>
                </div>
            </div>
        </div>

        <div class="col-lg-9">
            <h1 class="titulo-pagina mb-4"><c:out value="${tituloCatalogo}"/></h1>
            <c:if test="${not empty sessionScope.mensaje}"><div class="alert alert-success">${sessionScope.mensaje}</div><c:remove var="mensaje" scope="session"/></c:if>
            
            <c:choose>
                <c:when test="${not empty productosPorCategoria}">
                    <c:forEach var="entry" items="${productosPorCategoria}">
                        <h3 class="mb-3 border-bottom pb-2">${entry.key}</h3>
                        <div class="cuadricula-productos mb-5">
                            <c:forEach var="p" items="${entry.value}">
                                <div class="tarjeta-producto card h-100">
                                    <img src="${pageContext.request.contextPath}/imagenes/${not empty p.imagenUrl ? p.imagenUrl : 'producto_default.png'}" class="card-img-top" alt="<c:out value="${p.nombre}"/>">
                                    <div class="tarjeta-producto__info card-body d-flex flex-column">
                                        <h5 class="tarjeta-producto__nombre card-title"><c:out value="${p.nombre}"/></h5>
                                        <p class="tarjeta-producto__precio card-text fs-4 fw-bold"><fmt:formatNumber value="${p.precioVenta}" type="currency" currencySymbol="S/ "/></p>
                                        <div class="mt-auto d-grid gap-2">
                                            <a href="${pageContext.request.contextPath}/usuario?accion=comprar&id=${p.id}" class="btn btn-primary">Comprar</a>
                                            <button type="button" class="btn btn-secondary" data-product-id="${p.id}" onclick="mostrarDetalles(this)">Ver Detalles</button>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <div class="alert alert-info">No hay productos disponibles que coincidan con su búsqueda.</div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<div class="modal fade" id="detalleProductoModal" tabindex="-1" aria-labelledby="modalLabel" aria-hidden="true">
  <div class="modal-dialog modal-lg">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title fs-3" id="modalNombreProducto"></h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        <div class="row">
            <div class="col-md-6"><img id="modalImagen" src="" class="img-fluid rounded border"></div>
            <div class="col-md-6">
                <p id="modalDescripcion" class="lead"></p><hr>
                <p class="fs-5"><strong>Precio de Venta:</strong> <span id="modalPrecioVenta" class="fw-bold text-success"></span></p>
                <p><strong>Stock disponible:</strong> <span id="modalStock" class="badge bg-primary rounded-pill fs-6"></span></p>
                <p><strong>Categoría:</strong> <span id="modalCategoria" class="badge bg-info rounded-pill fs-6"></span></p><hr>
                <h6 class="text-muted">Información Adicional</h6>
                <p><small><strong>Proveedor:</strong> <span id="modalProveedor"></span></small></p>
                <p><small><strong>Precio de Costo:</strong> <span id="modalPrecioCosto"></span></small></p>
            </div>
        </div>
      </div>
      <div class="modal-footer"><button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cerrar</button></div>
    </div>
  </div>
</div>

<jsp:include page="/componentes/footer.jsp" />

<script>
    var detalleModal = new bootstrap.Modal(document.getElementById('detalleProductoModal'));
    function mostrarDetalles(buttonElement) {
        const productoId = buttonElement.getAttribute('data-product-id');
        fetch('productoDetalle?id=' + productoId)
            .then(response => {
                if (!response.ok) { throw new Error('Producto no encontrado'); }
                return response.json();
            })
            .then(data => {
                document.getElementById('modalNombreProducto').innerText = data.nombre;
                document.getElementById('modalDescripcion').innerText = data.descripcion || 'No hay descripción disponible.';
                document.getElementById('modalPrecioVenta').innerText = 'S/ ' + (data.precioVenta ? data.precioVenta.toFixed(2) : '0.00');
                document.getElementById('modalStock').innerText = data.stock;
                document.getElementById('modalCategoria').innerText = data.nombreCategoria || 'N/A';
                document.getElementById('modalProveedor').innerText = data.nombreProveedor || 'No especificado';
                document.getElementById('modalPrecioCosto').innerText = 'S/ ' + (data.precioCosto ? data.precioCosto.toFixed(2) : '0.00');
                if (data.imagenUrl) {
                    document.getElementById('modalImagen').src = '${pageContext.request.contextPath}/imagenes/' + data.imagenUrl;
                } else {
                    document.getElementById('modalImagen').src = '${pageContext.request.contextPath}/imagenes/producto_default.png';
                }
                detalleModal.show();
            })
            .catch(error => {
                console.error('Error al cargar los detalles del producto:', error);
                alert('No se pudieron cargar los detalles del producto. Por favor, intente de nuevo.');
            });
    }
</script>