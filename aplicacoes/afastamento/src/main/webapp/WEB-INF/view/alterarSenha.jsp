<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<html>
<head>
<jsp:include page="modulos/header-estrutura.jsp" />
<title>SiAf - Alterar Senha</title>
</head>
<body>
	<div id="wrapper">
		<jsp:include page="modulos/header.jsp" />
		<div id="content">
			<div class="title"> Alterar senha : </div>
			<span class="line"></span>
			<form:form id="alterarSenha" commandName="reserva" servletRelativeAction="/configuracao/alterar-senha" method="POST" class="form-horizontal">
				<c:if test="${not empty erro}">
					<div class="alert alert-danger alert-dismissible" role="alert" style="margin-top: 70px;">
						<button type="button" class="close" data-dismiss="alert">
							<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
						</button>
						<c:out value="${erro}"></c:out>
					</div>
				</c:if>
				<c:if test="${not empty info}">
					<div class="alert alert-success alert-dismissible" role="alert" style="margin-top: 70px;">
						<button type="button" class="close" data-dismiss="alert">
							<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
						</button>
						<c:out value="${info}"></c:out>
					</div>
				</c:if>
				
				<div class="form-group">
					<div class="form-item">
						<label for="senhaAnterior" class="col-sm-2 control-label">Senha anterior:</label>
						<div class="col-sm-4">
							<input id="senhaAnterior" name="senhaAnterior" type="password" class="form-control" required="required"/>
						</div>
					</div>
				</div>
				<div class="form-group">
					<div class="form-item">
						<label for="novaSenha" class="col-sm-2 control-label">Nova senha:</label>
						<div class="col-sm-4">
							<input id="novaSenha" name="novaSenha" type="password" class="form-control" required="required"/>
						</div>
					</div>
				</div>
				<div class="form-group">
					<div class="form-item">
						<label for="novaSenhaVerify" class="col-sm-2 control-label">Repita a nova senha:</label>
						<div class="col-sm-4">
							<input id="novaSenhaVerify" name="novaSenhaVerify" type="password" class="form-control" required="required"/>
						</div>
					</div>
				</div>
				
				<div class="controls">
					<input name="reservar" type="submit" class="btn btn-primary" value="Alterar" />
				</div>
			</form:form>
		</div>
		
		<jsp:include page="modulos/footer.jsp" />
		
	</div>
</body>
</html>

