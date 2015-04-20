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

			<div class="title">Reservas em aberto :</div>
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
									<button class="btn editReserva" id="editReserva${reserva.id}" data-id="${reserva.id}">
										<i class="fa fa-pencil "></i>
									</button>
									<div class="options hide" id="options${reserva.id}" style="width: 105px;" align="right">
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
		$('#tableReservas')
				.DataTable(
						{
							"pageLength" : 50,
							"order" : [ [ 1, 'asc' ], [ 2, 'asc' ] ],
							"columnDefs" : [ {
								"orderable" : false,
								"targets" : 0
							}, {
								"orderData" : [ 1, 2 ],
								"targets" : 1
							}, {
								"orderable" : false,
								"targets" : 2
							}, {
								"orderable" : false,
								"targets" : 3
							}, {
								"orderable" : false,
								"targets" : 4
							}, {
								"orderable" : false,
								"targets" : 5
							}, ],

							"language" : {
								"sEmptyTable" : "Nenhum registro encontrado",
								"sInfo" : "Mostrando de _START_ até _END_ de _TOTAL_ registros",
								"sInfoEmpty" : "Mostrando 0 até 0 de 0 registros",
								"sInfoFiltered" : "(Filtrados de _MAX_ registros)",
								"sInfoPostFix" : "",
								"sInfoThousands" : ".",
								"sLengthMenu" : "resultados por página _MENU_",
								"sLoadingRecords" : "Carregando...",
								"sProcessing" : "Processando...",
								"sZeroRecords" : "Nenhum registro encontrado",
								"sSearch" : "",
								"oPaginate" : {
									"sNext" : "Próximo",
									"sPrevious" : "Anterior",
									"sFirst" : "Primeiro",
									"sLast" : "Último"
								},
								"oAria" : {
									"sSortAscending" : ": Ordenar colunas de forma ascendente",
									"sSortDescending" : ": Ordenar colunas de forma descendente"
								}
							}
						});

		$('select').selectpicker();
		$('input').attr('placeholder', 'Pesquisar...');
		$('input').addClass('form-inline form-control');
	</script>

	<script type="text/javascript">
		$('#edit-reservas').addClass('active');
	</script>
</body>
</html>