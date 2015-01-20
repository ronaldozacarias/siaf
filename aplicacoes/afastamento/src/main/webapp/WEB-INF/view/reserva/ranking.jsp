<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<html>
<head>
<jsp:include page="../modulos/header-estrutura.jsp" />
<title>SiAf - Ranking</title>
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
				<div class="alert alert-success alert-dismissible" role="alert">
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
			
			<table id="ranking" class="table">
				<thead>
					<tr>
						<th>Nome</th>
						<th>Semestres Ativos</th>
						<th>Semestres Afastados</th>
						<th>Semestres Solicitados</th>
			            <th>Pontuação</th>
			            <th>Status</th>
					</tr>
				</thead>
			</table>
			
		</div>
		
		<jsp:include page="../modulos/footer.jsp" />
		
	</div>
</body>
<script type="text/javascript">
	getRanking($('#ano').val(), $('#semestre').val());
</script>
</html>

