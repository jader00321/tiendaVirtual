<%-- Ruta: /webapp/index.jsp --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:include page="/componentes/header.jsp" />

<div class="hero">
    <div class="hero__contenido">
        <h1>La frescura del campo, a la puerta de tu casa.</h1>
        <p>Calidad y buen precio en todos nuestros productos.</p>
        <a href="${pageContext.request.contextPath}/catalogo" class="btn btn-primary btn-lg">Ver Catálogo Completo</a>
    </div>
</div>

<div class="container mt-5">
    <div class="categorias-destacadas">
        <h2 class="titulo-pagina">Explora Nuestras Categorías</h2>
        <div class="cuadricula-categorias">
            <c:choose>
                <c:when test="${not empty listaCategorias}">
                    <c:forEach var="cat" items="${listaCategorias}">
                        <a href="${pageContext.request.contextPath}/catalogo?categoria=${cat.nombre}" class="tarjeta-categoria">
                            <img src="${pageContext.request.contextPath}/imagenes/${not empty cat.imagenUrl ? cat.imagenUrl : 'categoria_default.png'}" alt="<c:out value="${cat.nombre}"/>">
                            <div class="tarjeta-categoria__nombre"><c:out value="${cat.nombre}"/></div>
                        </a>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <p class="text-center w-100">No hay categorías disponibles para mostrar en este momento.</p>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

    <div class="productos-destacados mt-5">
        <h2 class="titulo-pagina">Nuestros Productos Destacados</h2>
        <div class="cuadricula-productos">
            <c:choose>
                <c:when test="${not empty listaProductosDestacados}">
                    <c:forEach var="p" items="${listaProductosDestacados}">
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
                </c:when>
                <c:otherwise>
                    <p class="cuadricula-productos__mensaje-vacio">No hay productos destacados disponibles.</p>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<div class="modal fade" id="detalleProductoModal" tabindex="-1" aria-labelledby="modalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg"><div class="modal-content"><div class="modal-header">
        <h5 class="modal-title fs-3" id="modalNombreProducto"></h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
    </div><div class="modal-body"><div class="row">
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
    </div></div><div class="modal-footer"><button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cerrar</button></div></div></div>
</div>

<jsp:include page="/componentes/footer.jsp" />

<script>
    var detalleModal = new bootstrap.Modal(document.getElementById('detalleProductoModal'));
    function mostrarDetalles(buttonElement) {
        const productoId = buttonElement.getAttribute('data-product-id');
        fetch('productoDetalle?id=' + productoId)
            .then(response => { if (!response.ok) { throw new Error('Producto no encontrado'); } return response.json(); })
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
                    document.getElementById('modalImagen').src = '${pageContext.request.contextPath}/img/producto_default.png';
                }
                detalleModal.show();
            })
            .catch(error => {
                console.error('Error al cargar los detalles del producto:', error);
                alert('No se pudieron cargar los detalles del producto. Por favor, intente de nuevo.');
            });
    }
</script>