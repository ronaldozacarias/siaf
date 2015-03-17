<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<html>
<head>
	<jsp:include page="../modulos/header-estrutura.jsp" />
	<title>Ranking</title>
</head>
<body>
	<div id="wrapper">
		<jsp:include page="../modulos/header.jsp" />
		<div id="content">
			<c:if test="${not empty erro}">
					<div class="alert alert-danger alert-dismissible" role="alert">
					<button type="button" class="close" data-dismiss="alert">
						<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
					</button>
					<c:out value="${erro}"></c:out>
				</div>
			</c:if>
			<c:if test="${not empty info}">
				<div class="alert alert-info alert-dismissible" role="alert">
					<button type="button" class="close" data-dismiss="alert">
						<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
					</button>
					<c:out value="${info}"></c:out>
				</div>
			</c:if>
			
			<input type="hidden" id="ano" value="${periodoAtual.ano }"/>
			<input type="hidden" id="semestre" value="${periodoAtual.semestre }"/>
			<input type="hidden" id="anoPosterior" value="${periodoPosterior.ano }"/>
			<input type="hidden" id="semestrePosterior" value="${periodoPosterior.semestre }"/>
			<input type="hidden" id="anoAnterior" value="${periodoAnterior.ano }"/>
			<input type="hidden" id="semestreAnterior" value="${periodoAnterior.semestre }"/>
			
			<div id="periodo">
				<i id="anterior" class="fa fa-arrow-circle-left fa-2x"></i>
				<label id="periodoLabel">${periodoAtual.ano }.${periodoAtual.semestre }</label>
				<i id="posterior" class="fa fa-arrow-circle-right fa-2x"></i>
			</div>
			<div class="align-center">
				<label id="encerramento"></label><br>
				<label id="vagas"></label>
			</div>
			
			<table id="ranking" class="table">
				<thead>
					<tr>
						<th>#</th>
						<th>Nome</th>
						<th><span data-toggle="tooltip" data-placement="top" title="Número de semestres desde a contratação na UFC até o início do afastamento">T</span></th>
						<th><span data-toggle="tooltip" data-placement="top" title="Número de semestres em que o docente já esteve afastado">A</span></th>
						<th><span data-toggle="tooltip" data-placement="top" title="Número de semestres do afastamento reservado/solicitado. No caso de primeiro afastamento, a variável S terá valor dois (2)">S</span></th>
						<th><span data-toggle="tooltip" data-placement="top" title="Número de semestres que faltam para o docente completar três (3) anos de contratação">P</span></th>
						<th><span data-toggle="tooltip" data-placement="top" title="Número de semestres solicitados">SS</span></th>
			            <th>Período</th>
			            <th>Programa / Conceito</th>
			            <th>Pontuação</th>
					</tr>
				</thead>
			</table>
			
			<div id="warning-ranking" class="alert alert-warning alert-dismissible" role="alert">
				Não há nenhuma reserva para esse período.
			</div>
			
			<div id="load-siaf">
				<div class=loading-siaf></div>
				<div class="load-s"></div>
			</div>

			<div id="legenda">
				<label><span class="aceito">&nbsp;&nbsp;&nbsp;&nbsp;</span>&nbsp;Aceito (afastado)</label>
				<label><span class="nao_aceito">&nbsp;&nbsp;&nbsp;&nbsp;</span>&nbsp;Não Aceito (anteriormente)</label>
				<label><span class="classificado">&nbsp;&nbsp;&nbsp;&nbsp;</span>&nbsp;Classificado</label>
				<label><span class="desclassificado">&nbsp;&nbsp;&nbsp;&nbsp;</span>&nbsp;Não classificado</label>
				<label><span class="cancelado">&nbsp;&nbsp;&nbsp;&nbsp;</span>&nbsp;Cancelado</label>
				<label><span class="encerrado">&nbsp;&nbsp;&nbsp;&nbsp;</span>&nbsp;Encerrado</label><br/>
				<label><span>Fórmula:</span> R = (T – A) / (5 x A + S + P)</label><br/>
				<label><span>T:</span> Número de semestres desde a contratação na UFC até o início do afastamento, iniciando no primeiro semestre em que o solicitante teve disciplina alocada no Campus Quixadá.</label><br/>
				<label><span>A:</span> Número de semestres em que o docente já esteve afastado para programas de pós-graduação stricto sensu ou pós-doutorados.</label><br/>
				<label><span>S:</span> Número de semestres do afastamento reservado/solicitado. (No caso de primeiro afastamento, a variável S terá valor dois (2) independente da duração do período reservado/solicitado).</label><br/>
				<label><span>P:</span> Número de semestres que faltam para o docente completar três (3) anos de contratação na UFC Quixadá (vale zero se já cumpriu este período).</label><br/>
				<label><span>SS:</span> Número de semestres do afastamento reservado/solicitado. </label>
				<label><span>Obs:</span> Será considerado como um afastamento com duração de um (1) semestre, para efeito de cálculo da ordem de prioridade das reservas/solicitações, a solicitação cancelada de acordo com os critérios da <a href="<c:url value="/resources/files/Resolucaoo_01-2014.pdf" />">resolução</a>.</label><br><br>
				<label>Em caso de empate na ordem de prioridade, serão considerados os critérios abaixo, na ordem indicada:</label>
				<label><span>I:</span> Mestrado tem maior prioridade que doutorado, e doutorado tem maior prioridade que pós-doutorado.</label><br>
				<label><span>II:</span> Prioridade para programas com melhor conceito.</label><br>
				<label><span>III:</span> Prioridade para o candidato mais velho.</label>
			</div>
		</div>
		
		<jsp:include page="../modulos/footer.jsp" />
		
	</div>
</body>
<script type="text/javascript">
	getRanking($('#ano').val(), $('#semestre').val());
	$('#menu-ranking').addClass('active');
</script>
</html>

