<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<html>
<head>
<jsp:include page="../modulos/header-estrutura.jsp" />
<title>Relatório</title>
</head>
<body>
	<fmt:setLocale value="pt"/>
	<div id="wrapper" class="container">
		<jsp:include page="../modulos/header.jsp" />
		<div id="content">
			<table id="tableRelatorio" class="display nowrap">
				<thead>
					<tr>
						<th>Nome</th>
						<th>Período</th>
						<th>Programa</th>
						<th>Pontuação</th>
						<th>Status</th>
						<c:forEach items="${periodos}" var="periodo" varStatus="cont">
							<th>${periodo.ano }.${periodo.semestre }<br/>(${periodo.vagas })</th>
						</c:forEach>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${relatorio}" var="r">
						<tr class="${r.key.status }">
							<td class="align-left">${r.key.professor }</td>
							<td>${r.key.reserva.anoInicio }.${r.key.reserva.semestreInicio } a ${r.key.reserva.anoTermino }.${r.key.reserva.semestreTermino }</td>
							<td>${r.key.reserva.programa.descricao }</td>
							<td><fmt:formatNumber type="number" maxFractionDigits="2" value="${r.key.pontuacao }" /></td>
							<td>${r.key.status.descricao }</td>
							<c:forEach items="${periodos}" var="periodo">
								<c:forEach items="${r.value }" var="rp">
									<c:if test="${periodo.ano eq rp.ano and periodo.semestre eq rp.semestre}">
										<c:set var="teste" value="${rp.status.descricao }"></c:set>
									</c:if>
								</c:forEach>
								<td>
									<c:if test="${teste == 'CLASSIFICADO'}">
										C
									</c:if>
									<c:if test="${teste == 'DESCLASSIFICADO' }">
										<span>D</span>
									</c:if>
									<c:if test="${teste == 'AFASTADO' }">
										<span>A</span>
									</c:if>
									<c:if test="${teste == '' }">
										<span>-</span>
									</c:if>
								</td>
								<c:set var="teste" value=""></c:set>
							</c:forEach>
		           		</tr>
		           	</c:forEach>
				</tbody>
			</table>
		</div>

		<jsp:include page="../modulos/footer.jsp" />

	</div>
</body>
<script type="text/javascript">
	$('#menu-homologacao').addClass('active');
	
	$(function() {
		var text_export = 'Exportar para PDF';
		var tabela_dom = 'Bfrtip';
		var tabela_ext = 'print';
		var img_align = 'center';
		$('#tableRelatorio')
		.DataTable({
			"order": [ 0, 'asc' ],
			 "scrollX": true,
			  "paging": false,
			  "searching": false,
			"language": {
			    "sEmptyTable": "Nenhum registro encontrado.",
			    "sInfo": "Mostrando de _START_ até _END_ de _TOTAL_ registros",
			    "sInfoEmpty": "Mostrando 0 até 0 de 0 registros",
			    "sInfoFiltered": "(Filtrados de _MAX_ registros)",
			    "sInfoPostFix": "",
			    "sInfoThousands": ".",
			    "sLengthMenu": "Resultados por página: _MENU_",
			    "sLoadingRecords": "Carregando...",
			    "sProcessing": "Processando...",
			    "sZeroRecords": "Nenhum registro encontrado.",
			    "sSearch": "",
			    "searchPlaceholder": "Pesquisar...",
			    "oPaginate": {
			        "sNext": "Próximo",
			        "sPrevious": "Anterior",
			        "sFirst": "Primeiro",
			        "sLast": "Último"
			    },
			    "oAria": {
			        "sSortAscending": ": Ordenar colunas de forma ascendente",
			        "sSortDescending": ": Ordenar colunas de forma descendente"
			    }
			},
			dom : tabela_dom,
			buttons : [ {
				extend : tabela_ext,
				text : text_export,
				orientation: 'landscape',
				title : 'SIAF - Relatório de Reservas',
				message : 'Gerado em: 07/01/2016 às 11:25:30',
				customize : function(doc) {
					$(doc.document.body).find( 'table' )
                    .addClass( 'compact' )
                    .css( 'font-size', 'inherit' );
				}
			} ]
		});
	});
</script>
</html>

