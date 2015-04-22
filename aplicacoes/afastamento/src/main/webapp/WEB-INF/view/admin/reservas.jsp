<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<html>
<head>
	<jsp:include page="../modulos/header-estrutura.jsp" />
	<title>Gerenciar Reservas</title>
</head>
<body>
	<div id="wrapper">
		<jsp:include page="../modulos/header.jsp" />
		<div id="content">
		<div class="title"> Gerenciar reservas : </div>
		<span class="line"></span>
			<c:if test="${not empty info}">
				<div class="alert alert-info margin-top" role="alert">
					<button type="button" class="close" data-dismiss="alert">
						<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
					</button>
					<c:out value="${info}"></c:out>
				</div>
			</c:if>
			<c:if test="${empty ranking}">
				<div class="alert alert-warning margin-top" role="alert">
					<button type="button" class="close" data-dismiss="alert">
						<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
					</button>
					Não há reservas para gerenciar no momento.
				</div>
			</c:if>
			<c:set var="atualizar" value="false"></c:set>
			
			<c:if test="${not empty ranking}">
				<form id="atualizarRanking" action="/siaf/administracao/atualizar-ranking" method="POST">
					<input type="hidden" value="${ranking.periodo.ano }" name="ano"/>
					<input type="hidden" value="${ranking.periodo.semestre }" name="semestre"/>
					<div id="periodo">
						<label id="periodoLabel">${ranking.periodo.ano }.${ranking.periodo.semestre }</label>
					</div>
					<div class="align-center"><label id="vagas">${ranking.periodo.vagas } vagas</label></div>
					
					<table id="ranking" class="table">
						<thead>
							<tr>
								<th>#</th>
								<th>Nome</th>
					            <th>Período</th>
					            <th>Programa</th>
					            <th>Pontuação</th>
					            <th>Status</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${ranking.tuplas }" var="tupla" varStatus="count">
								<tr class="${tupla.status }">
									<td class="align-center">${count.index + 1 }</td>
									<td>${tupla.professor }</td>
									<td class="align-center">${tupla.reserva.anoInicio }.${tupla.reserva.semestreInicio } a ${tupla.reserva.anoTermino }.${tupla.reserva.semestreTermino }</td>
									<td class="align-center">${tupla.reserva.programa.descricao }</td>
									<td class="align-center">${tupla.pontuacao }</td>
									<td class="align-center">
										<c:choose>
											<c:when test="${tupla.status != 'DESCLASSIFICADO' and tupla.status != 'NAO_ACEITO' and tupla.reserva.anoInicio == ranking.periodo.ano and tupla.reserva.semestreInicio == ranking.periodo.semestre}">
												<c:set var="atualizar" value="true"></c:set>
												<select id="${tupla.reserva.id }" name="status" class="form-control selectpicker">
													<option ${tupla.status == 'CLASSIFICADO' ? 'selected' : ''} value="${tupla.reserva.id }-ABERTO">CLASSIFICADO</option>
													<option ${tupla.status == 'ACEITO' ? 'selected' : ''} value="${tupla.reserva.id }-ACEITO">ACEITO</option>
													<option ${tupla.status == 'CANCELADO' ? 'selected' : ''} value="${tupla.reserva.id }-CANCELADO">CANCELADO</option>
													<option ${tupla.status == 'CANCELADO_COM_PUNICAO' ? 'selected' : ''} value="${tupla.reserva.id }-CANCELADO_COM_PUNICAO">CANCELADO COM PUNIÇÃO</option>
													<option ${tupla.status == 'NEGADO' ? 'selected' : ''} value="${tupla.reserva.id }-NEGADO">NEGADO</option>
												</select>
											</c:when>
											<c:otherwise>
												${tupla.status.descricao }
											</c:otherwise>
										</c:choose>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					<c:if test="${atualizar }">
						<div class="controls">
							<input name="reservar" type="submit" class="btn btn-siaf" value="Atualizar Ranking" />
						</div>
					</c:if>
				</form>
			</c:if>
		</div>
		
		<jsp:include page="../modulos/footer.jsp" />
		
	</div>
</body>
<script type="text/javascript">
	$('#menu-reservas').addClass('active');
</script>
</html>

