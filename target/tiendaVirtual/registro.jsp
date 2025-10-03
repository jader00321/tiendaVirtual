<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="/componentes/header.jsp" />

<div class="formulario-acceso">
    <div class="formulario-acceso__tarjeta">
        <h2>Crear una Cuenta</h2>
        <form action="${pageContext.request.contextPath}/usuario" method="post">
            <input type="hidden" name="accion" value="registrar">
            <div class="formulario-acceso__grupo">
                <label for="nombre">Nombre Completo</label>
                <input type="text" id="nombre" name="nombre" required>
            </div>
            <div class="formulario-acceso__grupo">
                <label for="email">Correo Electrónico</label>
                <input type="email" id="email" name="email" required>
            </div>
            <div class="formulario-acceso__grupo">
                <label for="password">Contraseña</label>
                <input type="password" id="password" name="password" required>
            </div>
            <button type="submit" class="boton boton--primario boton--bloque">Registrarse</button>
        </form>
        <div class="formulario-acceso__pie">
            <p>¿Ya tienes una cuenta? <a href="${pageContext.request.contextPath}/login.jsp">Inicia sesión</a></p>
        </div>
    </div>
</div>

<jsp:include page="/componentes/footer.jsp" />