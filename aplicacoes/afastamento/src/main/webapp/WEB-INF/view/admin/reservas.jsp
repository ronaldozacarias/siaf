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

<title>Reservas</title>
</head>
<body>

	<div id="wrapper" class="container">
		<jsp:include page="../modulos/header.jsp" />

		<div id="content">
		<!-- <div id="chart" style="width:100%; height:400px;"></div> -->
			<c:if test="${not empty erro}">
				<br />
				<div class="alert alert-danger alert-dismissible" role="alert">
					<button type="button" class="close" data-dismiss="alert">
						<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
					</button>
					<c:out value="${erro}"></c:out>
				</div>
			</c:if>
			<c:if test="${not empty info}">
				<br />
				<div class="alert alert-info alert-dismissible" role="alert">
					<button type="button" class="close" data-dismiss="alert">
						<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
					</button>
					<c:out value="${info}"></c:out>
				</div>
			</c:if>
			<div>
				<table id="tableReservas" class="display">
					<thead>
						<tr>
							<th style="width: 15%">Status</th>
							<th>Professor</th>
							<th>Período</th>
							<th style="width: 17%">Programa</th>
							<th>Conceito</th>
							<th></th>
						</tr>
					</thead>
					
					<tfoot>
						<tr>
							<th>Status</th>
							<th>Professor</th>
							<th>Período</th>
							<th>Programa</th>
							<th>Conceito</th>
							<th></th>
						</tr>
					</tfoot>
					
					<tbody id="bodyReservas">
						<c:forEach items="${reservas}" var="reserva" varStatus="cont">
							<tr id="reserva${reserva.id}">
								<fmt:formatDate value="${reserva.dataSolicitacao}" pattern="dd/MM/yyyy" var="data" />
								<td class="align-center">
									<c:choose>
										<c:when test="${reserva.status == 'ABERTO'}"><span class="label label-success">${reserva.status.descricao}</span></c:when>
										<c:when test="${reserva.status == 'ENCERRADO'}"><span class="label label-default">${reserva.status.descricao}</span></c:when>
										<c:when test="${reserva.status == 'AFASTADO'}"><span class="label label-primary">${reserva.status.descricao}</span></c:when>
										<c:when test="${reserva.status == 'NAO_ACEITO'}"><span class="label label-warning">${reserva.status.descricao}</span></c:when>
										<c:otherwise><span class="label label-danger">${reserva.status}</span></c:otherwise>
									</c:choose>
								</td>
								<td>${reserva.professor.nome}</td>
								<td>${reserva.anoInicio}.${reserva.semestreInicio} a ${reserva.anoTermino }.${reserva.semestreTermino }</td>

								<td class="align-center">${reserva.programa.descricao}</td>
								<td class="align-center">${reserva.conceitoPrograma}</td>
								<td>
									<c:if test="${reserva.status == 'ABERTO' }">
										<a title="Editar" href="<c:url value="/administracao/editar-reserva/${reserva.id }" />" class="btn btn-default">
											<i class="fa fa-pencil"></i>
                                        </a>
									</c:if>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<jsp:include page="../modulos/footer.jsp" />
	<%-- <script src="<c:url value="/resources/js/jquery.dataTables.min.js" />"></script> --%>

	<script type="text/javascript">
		$('#menu-reservasEmAberto').addClass('active');
		relatorioReservas();
	</script>
</body>
</html>
