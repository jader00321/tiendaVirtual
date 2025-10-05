<%--
  Este archivo contiene la estructura HTML del modal de detalles del producto
  y el script de JavaScript necesario para que funcione.
  Se incluye en las páginas que necesitan mostrar los detalles de un producto.
--%>

<div class="modal fade" id="detalleProductoModal" tabindex="-1" aria-labelledby="modalLabel" aria-hidden="true">
  <div class="modal-dialog modal-lg">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title fs-3" id="modalNombreProducto"></h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        <div class="row">
            <div class="col-md-6">
                <img id="modalImagen" src="" class="img-fluid rounded border">
            </div>
            <div class="col-md-6">
                <p id="modalDescripcion" class="lead"></p>
                <hr>
                <p class="fs-5"><strong>Precio de Venta:</strong> <span id="modalPrecioVenta" class="fw-bold text-success"></span></p>
                <p><strong>Stock disponible:</strong> <span id="modalStock" class="badge bg-primary rounded-pill fs-6"></span></p>
                <p><strong>Categoría:</strong> <span id="modalCategoria" class="badge bg-info rounded-pill fs-6"></span></p>
                <hr>
                <h6 class="text-muted">Información Adicional</h6>
                <p><small><strong>Proveedor:</strong> <span id="modalProveedor"></span></small></p>
                <p><small><strong>Precio de Costo:</strong> <span id="modalPrecioCosto"></span></small></p>
            </div>
        </div>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cerrar</button>
      </div>
    </div>
  </div>
</div>
<script>
    // Se obtiene una referencia al modal de Bootstrap para poder controlarlo.
    var detalleModal = new bootstrap.Modal(document.getElementById('detalleProductoModal'));

    function mostrarDetalles(buttonElement) {
        // Obtenemos el ID del producto desde el atributo 'data-product-id' del botón.
        const productoId = buttonElement.getAttribute('data-product-id');

        // Usamos 'fetch' para hacer una llamada asíncrona a nuestro servlet de detalles.
        fetch('productoDetalle?id=' + productoId)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Producto no encontrado. Código: ' + response.status);
                }
                return response.json(); // Convierte la respuesta del servlet a un objeto JSON.
            })
            .then(data => {
                // Una vez que tenemos los datos, los usamos para rellenar el HTML del modal.
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

                // Finalmente, le decimos a Bootstrap que muestre el modal.
                detalleModal.show();
            })
            .catch(error => {
                console.error('Error al cargar los detalles del producto:', error);
                alert('No se pudieron cargar los detalles del producto. Por favor, intente de nuevo.');
            });
    }
</script>