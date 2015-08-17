<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<html>
<head>
	<jsp:include page="modulos/header-estrutura.jsp" />
	<title>Erro</title>
</head>
<body>
	<div id="wrapper">
		<jsp:include page="modulos/header.jsp" />
		<div id="content">
			<div class="error-code">Ooops! Algo deu errado. Entre em contato com o administrador do sistema.</div>
			<img alt="500 - Erro Interno" src="<c:url value="/resources/images/500.png" />" class="error"/>
		</div>
		
		<jsp:include page="modulos/footer.jsp" />
		
	</div>
</body>
</html>

