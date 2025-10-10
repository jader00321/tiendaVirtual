<%-- Ruta: /webapp/perfil.jsp --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/componentes/header.jsp" />

<div class="container mt-4">
    <h1 class="titulo-pagina">Mi Perfil</h1>
    <c:if test="${not empty sessionScope.mensaje_perfil}"><div class="alert alert-success">${sessionScope.mensaje_perfil}</div><c:remove var="mensaje_perfil" scope="session"/></c:if>
    <c:if test="${not empty requestScope.error_perfil}"><div class="alert alert-danger">${requestScope.error_perfil}</div></c:if>

    <div class="row justify-content-center"><div class="col-lg-8"><div class="card shadow-sm">
        <div class="card-header"><h4>Información de la Cuenta</h4></div>
        <div class="card-body">
            <form action="${pageContext.request.contextPath}/usuario" method="post" onsubmit="return validarPasswordPerfil();">
                <input type="hidden" name="accion" value="actualizarPerfil">
                <div class="mb-3 row"><label class="col-sm-3 col-form-label">Nombre:</label><div class="col-sm-9"><input type="text" class="form-control" name="nombre" value="${sessionScope.usuario.nombre}" required></div></div>
                <div class="mb-3 row"><label class="col-sm-3 col-form-label">Correo:</label><div class="col-sm-9"><input type="email" class="form-control" value="${sessionScope.usuario.email}" disabled readonly></div></div>
                <div class="mb-3 row"><label class="col-sm-3 col-form-label">Rol:</label><div class="col-sm-9"><input type="text" class="form-control" value="${sessionScope.usuario.rol}" disabled readonly></div></div>
                <hr><p class="text-muted">Dejar los campos de contraseña en blanco para no cambiarla.</p>
                <div class="mb-3 row">
                    <label for="passwordPerfil" class="col-sm-3 col-form-label">Nueva Contraseña:</label>
                    <div class="col-sm-9 input-group">
                        <input type="password" class="form-control" id="passwordPerfil" name="password">
                        <span class="input-group-text"><i class="bi bi-eye-slash" id="toggleIconPerfil1" onclick="togglePasswordVisibility('passwordPerfil', 'toggleIconPerfil1')" style="cursor: pointer;"></i></span>
                    </div>
                </div>
                <div class="mb-3 row">
                    <label for="confirmPasswordPerfil" class="col-sm-3 col-form-label">Confirmar:</label>
                    <div class="col-sm-9 input-group">
                        <input type="password" class="form-control" id="confirmPasswordPerfil" name="confirmPassword">
                        <span class="input-group-text"><i class="bi bi-eye-slash" id="toggleIconPerfil2" onclick="togglePasswordVisibility('confirmPasswordPerfil', 'toggleIconPerfil2')" style="cursor: pointer;"></i></span>
                    </div>
                     <div id="passwordErrorPerfil" class="col-sm-9 offset-sm-3 form-text text-danger d-none">Las contraseñas no coinciden.</div>
                </div>
                <div class="d-flex justify-content-end"><button type="submit" class="btn btn-primary">Guardar Cambios</button></div>
            </form>
        </div>
        <div class="card-footer"><a href="pedido?accion=historial" class="btn btn-outline-primary">Ver mi Historial de Compras</a></div>
    </div></div></div>
</div>

<script>
    function validarPasswordPerfil() {
        const password = document.getElementById('passwordPerfil').value;
        const confirmPassword = document.getElementById('confirmPasswordPerfil').value;
        const errorDiv = document.getElementById('passwordErrorPerfil');
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