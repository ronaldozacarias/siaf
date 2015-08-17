<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<html>
<head>

<jsp:include page="../modulos/header-estrutura.jsp" />
<title>Detalhes Reserva</title>
</head>
<body>

<div id="wrapper">
		<jsp:include page="../modulos/header.jsp" />
		<div id="content" class="form-horizontal">
			<div class="title">Detalhes da reserva :</div>
			<span class="line"></span>
			<c:if test="${not empty erro}">
				<div class="alert alert-danger alert-dismissible margin-top" role="alert">
					<button type="button" class="close" data-dismiss="alert">
						<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
					</button>
					<c:out value="${erro}"></c:out>
				</div>
			</c:if>

			<div class="form-group">
				<label class="col-sm-2 control-label">Professor:</label>
				<div class="col-sm-4">
					<label class="control-label value-label">${reserva.professor.nome }</label>
				</div>
				<label class="col-sm-2 control-label">Siape:</label>
				<div class="col-sm-4">
					<label class="control-label value-label">${reserva.professor.siape }</label>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">Período:</label>
				<div class="col-sm-4">
					<label class="control-label value-label">${reserva.anoInicio }.${reserva.semestreInicio } a ${reserva.anoTermino }.${reserva.semestreTermino }</label>
				</div>
				<label class="col-sm-2 control-label">Status:</label>
				<div class="col-sm-4">
					<label class="control-label value-label">${reserva.status.descricao }</label>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">Programa:</label>
				<div class="col-sm-4">
					<label class="control-label value-label">${reserva.programa.descricao }</label>
				</div>
				<label class="col-sm-2 control-label">Conceito do programa:</label>
				<div class="col-sm-4">
					<label class="control-label value-label">${reserva.conceitoPrograma }</label>
				</div>
			</div>

			<div class="form-group">
				<label class="col-sm-2 control-label">Instituição:</label>
				<div class="col-sm-4">
					<c:if test="${empty reserva.instituicao}">
						<label class="control-label value-label">-</label>
					</c:if>
					<c:if test="${not empty reserva.instituicao}">
						<label class="control-label value-label">${reserva.instituicao }</label>
					</c:if>
				</div>
				<label class="col-sm-2 control-label">Data de solicitação:</label>
				<div class="col-sm-4">
					<label class="control-label value-label"><fmt:formatDate pattern="dd/MM/yyyy" value="${reserva.dataSolicitacao }" /></label>
				</div>
			</div>
		</div>
		<jsp:include page="../modulos/footer.jsp" />

	</div>
	<script type="text/javascript">
		$('#menu-reservas').addClass('active');
	</script>
</body>
</html>

