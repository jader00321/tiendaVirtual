<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tienda Virtual de Abarrotes</title>
    
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="icon" href="${pageContext.request.contextPath}/img/favicon.png" type="image/png">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&family=Lato:wght@400;700&display=swap" rel="stylesheet">
</head>
<body>
    <header class="header">
        <div class="header__container">
            <div class="header__logo">
                <a href="${pageContext.request.contextPath}/index.jsp">
                    <img src="${pageContext.request.contextPath}/imagenes/logo-icon.png" alt="Logo Icono" class="header__logo-icon">
                    MiTienda
                </a>
            </div>
            <nav class="header__nav">
                <ul>
                    <li><a href="${pageContext.request.contextPath}/index.jsp">Inicio</a></li>
                    <li><a href="${pageContext.request.contextPath}/catalogo">Catalogo</a></li>
                    
                    <c:if test="${sessionScope.usuario.rol == 'ADMIN' or sessionScope.usuario.rol == 'TRABAJADOR'}">
                        <li><a href="${pageContext.request.contextPath}/inventario">Inventario</a></li>
                    </c:if>

                    <c:if test="${sessionScope.usuario.rol == 'ADMIN'}">
                        <li><a href="${pageContext.request.contextPath}/gestion">Gestion</a></li> 
                    </c:if>
                    
                    <c:choose>
                        <c:when test="${not empty sessionScope.usuario}">
                            <li><a href="${pageContext.request.contextPath}/perfil.jsp">Mi Perfil</a></li>
                            <li><a href="${pageContext.request.contextPath}/usuario?accion=logout" class="boton boton--secundario">Cerrar Sesion</a></li>
                        </c:when>
                        <c:otherwise>
                            <li><a href="${pageContext.request.contextPath}/login.jsp" class="boton boton--primario">Acceder</a></li>
                            <li><a href="${pageContext.request.contextPath}/registro.jsp" class="boton boton--secundario">Registrarse</a></li>
                        </c:otherwise>
                    </c:choose>
                </ul>
            </nav>
        </div>
    </header>
    <main class="contenedor-principal container">