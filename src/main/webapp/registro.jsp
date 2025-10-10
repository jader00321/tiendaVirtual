<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/componentes/header.jsp" />

<div class="container d-flex justify-content-center align-items-center" style="min-height: 70vh;">
    <div class="card shadow-lg" style="width: 100%; max-width: 500px;">
        <div class="card-body p-5">
            <h2 class="card-title text-center mb-4">Crear una Cuenta</h2>
            <c:if test="${not empty requestScope.error}"><div class="alert alert-danger"><c:out value="${requestScope.error}"/></div></c:if>

            <form action="${pageContext.request.contextPath}/usuario" method="post" onsubmit="return validarPassword();">
                <input type="hidden" name="accion" value="registrar">
                <div class="mb-3"><label for="nombre" class="form-label">Nombre Completo</label><input type="text" class="form-control" id="nombre" name="nombre" required></div>
                <div class="mb-3"><label for="email" class="form-label">Correo Electrónico</label><input type="email" class="form-control" id="email" name="email" required></div>
                <div class="mb-3">
                    <label for="password" class="form-label">Contraseña</label>
                    <div class="input-group">
                        <input type="password" class="form-control" id="password" name="password" required>
                        <span class="input-group-text"><i class="bi bi-eye-slash" id="toggleIcon1" onclick="togglePasswordVisibility('password', 'toggleIcon1')" style="cursor: pointer;"></i></span>
                    </div>
                </div>
                <div class="mb-4">
                    <label for="confirmPassword" class="form-label">Confirmar Contraseña</label>
                     <div class="input-group">
                        <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" required>
                        <span class="input-group-text"><i class="bi bi-eye-slash" id="toggleIcon2" onclick="togglePasswordVisibility('confirmPassword', 'toggleIcon2')" style="cursor: pointer;"></i></span>
                    </div>
                    <div id="passwordError" class="form-text text-danger d-none">Las contraseñas no coinciden.</div>
                </div>
                <div class="d-grid"><button type="submit" class="btn btn-primary btn-lg">Registrarse</button></div>
            </form>
            <div class="text-center mt-4"><p class="mb-0">¿Ya tienes una cuenta? <a href="login.jsp">Inicia sesión</a></p></div>
        </div>
    </div>
</div>

<script>
    function validarPassword() {
        const password = document.getElementById('password').value;
        const confirmPassword = document.getElementById('confirmPassword').value;
        const errorDiv = document.getElementById('passwordError');

        if (password !== confirmPassword) {
            errorDiv.classList.remove('d-none');
            return false;
        }
        errorDiv.classList.add('d-none');
        return true;
    }
</script>

<jsp:include page="/componentes/footer.jsp" />
<jsp:include page="/componentes/password_toggle_script.jsp" />