<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/componentes/header.jsp" />

<div class="container d-flex justify-content-center align-items-center" style="min-height: 70vh;">
    <div class="card shadow-lg" style="width: 100%; max-width: 450px;">
        <div class="card-body p-5">
            <h2 class="card-title text-center mb-4">Iniciar Sesión</h2>
            
            <c:if test="${not empty requestScope.error}"><div class="alert alert-danger"><c:out value="${requestScope.error}"/></div></c:if>
            <c:if test="${param.mensaje_registro == 'exitoso'}"><div class="alert alert-success">¡Registro exitoso! Por favor, inicia sesión.</div></c:if>

            <form action="${pageContext.request.contextPath}/usuario" method="post">
                <input type="hidden" name="accion" value="login">
                <div class="mb-3">
                    <label for="email" class="form-label">Correo Electrónico</label>
                    <input type="email" class="form-control" id="email" name="email" required autofocus>
                </div>
                <div class="mb-4">
                    <label for="password" class="form-label">Contraseña</label>
                    <div class="input-group">
                        <input type="password" class="form-control" id="passwordLogin" name="password" required>
                        <span class="input-group-text">
                            <i class="bi bi-eye-slash" id="toggleIconLogin" onclick="togglePasswordVisibility('passwordLogin', 'toggleIconLogin')" style="cursor: pointer;"></i>
                        </span>
                    </div>
                </div>
                <div class="d-grid"><button type="submit" class="btn btn-primary btn-lg">Entrar</button></div>
            </form>
            <div class="text-center mt-4"><p class="mb-0">¿No tienes una cuenta? <a href="registro.jsp">Regístrate aquí</a></p></div>
        </div>
    </div>
</div>

<jsp:include page="/componentes/footer.jsp" />
<jsp:include page="/componentes/password_toggle_script.jsp" />