<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/componentes/header.jsp" />

<div class="formulario-acceso">
    <div class="formulario-acceso__tarjeta">
        <h2>Iniciar Sesión</h2>
        <c:if test="${not empty requestScope.error}">
            <div class="alerta alerta--peligro">${requestScope.error}</div>
        </c:if>
        <form action="${pageContext.request.contextPath}/usuario" method="post">
            <input type="hidden" name="accion" value="login">
            <div class="formulario-acceso__grupo">
                <label for="email">Correo Electrónico</label>
                <input type="email" id="email" name="email" required>
            </div>
            <div class="formulario-acceso__grupo">
                <label for="password">Contraseña</label>
                <input type="password" id="password" name="password" required>
            </div>
            <button type="submit" class="boton boton--primario boton--bloque">Entrar</button>
        </form>
        <div class="formulario-acceso__pie">
            <p>¿No tienes una cuenta? <a href="${pageContext.request.contextPath}/registro.jsp">Regístrate aquí</a></p>
        </div>
    </div>
</div>

<jsp:include page="/componentes/footer.jsp" />