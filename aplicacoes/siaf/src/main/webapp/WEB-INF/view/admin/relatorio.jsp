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
						<th></th>
						<th>Nome</th>
						<th>Pont.</th>
						<c:forEach items="${periodos}" var="periodo" varStatus="cont">
							<th>${periodo.ano }.${periodo.semestre }<br/>(${periodo.vagas })</th>
						</c:forEach>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${relatorio}" var="r">
						<tr class="${r.key.status }">
							<td>
								<c:if test="${r.key.status == 'CLASSIFICADO' or r.key.status == 'AFASTADO'}">
									<i class="fa fa-check"></i>
								</c:if>
								<c:if test="${r.key.status == 'DESCLASSIFICADO' }">
									<i class="fa fa-times"></i>
								</c:if>
							</td>
							<td class="align-left">
								${r.key.professor }<br/>
								${r.key.reserva.anoInicio }.${r.key.reserva.semestreInicio } a ${r.key.reserva.anoTermino }.${r.key.reserva.semestreTermino }<br/>
								${r.key.reserva.programa.descricao }<br/>
							</td>
							<td>
								<fmt:formatNumber type="number" maxFractionDigits="2" value="${r.key.pontuacao }" />
							</td>
							<c:forEach items="${periodos}" var="periodo">
								<c:forEach items="${r.value }" var="rp">
									<c:if test="${periodo.ano eq rp.ano and periodo.semestre eq rp.semestre}">
										<c:set var="teste" value="${rp.status.descricao }"></c:set>
									</c:if>
								</c:forEach>
								<td>
									<c:if test="${teste == 'CLASSIFICADO' or teste == 'AFASTADO'}">
										<i class="fa fa-check"></i>
									</c:if>
									<c:if test="${teste == 'DESCLASSIFICADO' }">
										<i class="fa fa-times"></i>
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
		$('#tableRelatorio').DataTable({
			"order": [ 1, 'asc' ],
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
			}
		});
	});
</script>
</html>

