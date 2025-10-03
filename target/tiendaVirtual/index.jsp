<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="/componentes/header.jsp" />

<div class="hero">
    <div class="hero__contenido">
        <h1>La frescura del campo, a la puerta de tu casa.</h1>
        <p>Calidad y buen precio en todos nuestros productos.</p>
        <a href="${pageContext.request.contextPath}/catalogo" class="boton boton--primario boton--grande">Ver Catálogo Completo</a>
    </div>
</div>

<div class="categorias-destacadas">
    <h2 class="titulo-pagina">Categorías Destacadas</h2>
    <div class="cuadricula-categorias">
        <a href="${pageContext.request.contextPath}/catalogo?categoria=Frutas" class="tarjeta-categoria">
            <img src="${pageContext.request.contextPath}/imagenes/categoria_frutas.jpg" alt="Frutas Frescas">
            <div class="tarjeta-categoria__nombre">Frutas</div>
        </a>
        <a href="${pageContext.request.contextPath}/catalogo?categoria=Verduras" class="tarjeta-categoria">
            <img src="${pageContext.request.contextPath}/imagenes/categoria_verduras.jpg" alt="Verduras de Estación">
            <div class="tarjeta-categoria__nombre">Verduras</div>
        </a>
        <a href="${pageContext.request.contextPath}/catalogo?categoria=Lacteos" class="tarjeta-categoria">
            <img src="${pageContext.request.contextPath}/imagenes/categoria_lacteos.jpg" alt="Lácteos y Huevos">
            <div class="tarjeta-categoria__nombre">Lácteos</div>
        </a>
         <a href="${pageContext.request.contextPath}/catalogo?categoria=Abarrotes" class="tarjeta-categoria">
            <img src="${pageContext.request.contextPath}/imagenes/categoria_abarrotes.jpg" alt="Abarrotes Generales">
            <div class="tarjeta-categoria__nombre">Abarrotes</div>
        </a>
    </div>
</div>

<jsp:include page="/componentes/footer.jsp" />