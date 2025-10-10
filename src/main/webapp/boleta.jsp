<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:include page="/componentes/header.jsp" />

<div class="container mt-4">
    <c:choose>
        <c:when test="${not empty pedido and not empty sessionScope.usuario and pedido.usuarioId == sessionScope.usuario.id}">
            
            <div id="boleta-contenido">
                <div class="card shadow-sm">
                    <div class="card-header bg-success text-white text-center">
                        <h2>¡Gracias por tu compra!</h2>
                        <p class="lead mb-0">Tu pedido ha sido procesado exitosamente.</p>
                    </div>
                    <div class="card-body p-4">
                        <div class="row mb-4">
                            <div class="col-md-6">
                                <h4>Boleta de Venta</h4>
                                <p class="mb-1"><strong>Código de Boleta:</strong> <span class="text-primary fw-bold">${pedido.codigoBoleta}</span></p>
                                <p class="mb-1"><strong>Fecha de Compra:</strong> <fmt:formatDate value="${pedido.fecha}" pattern="dd/MM/yyyy HH:mm:ss" /></p>
                            </div>
                            <div class="col-md-6 text-md-end">
                                <h4>Cliente</h4>
                                <p class="mb-1"><strong>Nombre:</strong> <c:out value="${sessionScope.usuario.nombre}"/></p>
                                <p class="mb-1"><strong>Email:</strong> <c:out value="${sessionScope.usuario.email}"/></p>
                            </div>
                        </div>
                        
                        <h5 class="mt-4 border-bottom pb-2">Detalles del Pedido</h5>
                        <table class="table align-middle">
                            <thead>
                                <tr>
                                    <th>Producto</th><th></th>
                                    <th class="text-center">Cant.</th>
                                    <th class="text-center">P. Unit.</th>
                                    <th class="text-end">Subtotal</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="item" items="${items}">
                                    <tr>
                                        <td style="width: 80px;"><img src="imagenes/${item.producto.imagenUrl}" class="img-fluid rounded"></td>
                                        <td><c:out value="${item.producto.nombre}"/></td>
                                        <td class="text-center">${item.cantidad}</td>
                                        <td class="text-center"><fmt:formatNumber value="${item.precioUnitario}" type="currency" currencySymbol="S/ "/></td>
                                        <td class="text-end fw-bold"><fmt:formatNumber value="${item.cantidad * item.precioUnitario}" type="currency" currencySymbol="S/ "/></td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                            <tfoot>
                                <tr class="table-light">
                                    <td colspan="4" class="text-end fs-4"><strong>Total General:</strong></td>
                                    <td class="text-end fs-4 fw-bold text-success"><fmt:formatNumber value="${pedido.total}" type="currency" currencySymbol="S/ "/></td>
                                </tr>
                            </tfoot>
                        </table>
                    </div>
                </div>
            </div>

            <div class="text-center mt-4 d-flex justify-content-between">
                <a href="catalogo" class="btn btn-primary">Volver al Catálogo</a>
                <button onclick="descargarPDF()" class="btn btn-danger">Descargar como PDF</button>
            </div>

        </c:when>
        <c:otherwise>
            <div class="alert alert-danger text-center">
                <h4>Acceso Denegado</h4><p>No se encontró el pedido o no tienes permiso para verlo.</p>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="/componentes/footer.jsp" />

<script src="https://cdnjs.cloudflare.com/ajax/libs/html2canvas/1.4.1/html2canvas.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jspdf/2.5.1/jspdf.umd.min.js"></script>

<script>
    function descargarPDF() {
        const boletaContenido = document.getElementById('boleta-contenido');
        const codigoBoleta = "${pedido.codigoBoleta}"; // Obtenemos el código de la boleta desde JSP

        html2canvas(boletaContenido, { scale: 2 }).then(canvas => {
            const imgData = canvas.toDataURL('image/png');
            
            // Usamos window.jspdf para acceder a la librería
            const { jsPDF } = window.jspdf;
            
            // Creamos un PDF en orientación vertical (portrait), usando milímetros y tamaño A4
            const pdf = new jsPDF('p', 'mm', 'a4');
            
            const pdfWidth = pdf.internal.pageSize.getWidth();
            const pdfHeight = (canvas.height * pdfWidth) / canvas.width;
            
            pdf.addImage(imgData, 'PNG', 0, 0, pdfWidth, pdfHeight);
            pdf.save('boleta-' + codigoBoleta + '.pdf');
        });
    }
</script>