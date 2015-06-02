<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<html>
<head>
<jsp:include page="../modulos/header-estrutura.jsp" />
<link href="<c:url value="/resources/css/jquery.dataTables.min.css" />" rel="stylesheet" />

<title>SiAf - Reservas em Aberto</title>
</head>
<body>

	<div id="wrapper" class="container">
		<jsp:include page="../modulos/header.jsp" />

		<div id="content">

			<div class="title"><br></div>
			<span class="line"></span>

			<div class="messages">
				<div id="erroDiv"></div>
				<div id="infoDiv"></div>
			</div>
			<div>
				<table id="tableReservas" class="display" cellspacing="0" width="100%">
					<thead>
						<tr>
							<th>Status</th>
							<th>Professor</th>
							<th>Instituição</th>
							<th>Periodo</th>
							<th>Programa</th>
							<th>Conceito</th>
							<th></th>
						</tr>
					</thead>
					<tbody id="bodyReservas">
						<c:forEach items="${reservas}" var="reserva" varStatus="cont">
							<tr id="reserva${reserva.id}">
								<fmt:formatDate value="${reserva.dataSolicitacao}" pattern="dd/MM/yyyy" var="data" />
								<td><span class="label label-success">${reserva.status }</span></td>
								<td>${reserva.professor.nome}</td>
								<td>${reserva.instituicao}</td>
								<td>${reserva.anoInicio}.${reserva.semestreInicio} a ${reserva.anoTermino }.
									${reserva.semestreTermino }</td>

								<td id="programa" class="programa">${reserva.programa.descricao}</td>
								<td id="concept${reserva.id}" class="concept">${reserva.conceitoPrograma}</td>

								<td>
									<button title="Editar" class="btn editReserva" id="editReserva${reserva.id}" data-id="${reserva.id}">
										<i class="fa fa-pencil "></i>
									</button>
									<div class="options hide" id="options${reserva.id}">
										<button class="btn salvarReserva btn-primary" data-id="${reserva.id}">salvar</button>
										<button class="btn cancelReserva btn-danger" data-id="${reserva.id}">
											<i class="fa fa-times"></i>
										</button>
									</div>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<jsp:include page="../modulos/footer.jsp" />
	<script src="<c:url value="/resources/js/jquery.dataTables.min.js" />"></script>

	<script type="text/javascript">
		$('#menu-reservasEmAberto').addClass('active');
	</script>
</body>
</html>