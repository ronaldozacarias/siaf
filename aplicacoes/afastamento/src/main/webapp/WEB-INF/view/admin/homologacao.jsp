<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<html>
<head>
<jsp:include page="../modulos/header-estrutura.jsp" />
<title>Homologação</title>
</head>
<body>
	<fmt:setLocale value="pt"/>
	<div id="wrapper">
		<jsp:include page="../modulos/header.jsp" />
		<div id="content">
			<c:if test="${not empty info}">
				<div class="alert alert-info" role="alert">
					<button type="button" class="close" data-dismiss="alert">
						<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
					</button>
					<c:out value="${info}"></c:out>
				</div>
			</c:if>
			<c:if test="${empty ranking}">
				<div class="alert alert-warning" role="alert">
					<button type="button" class="close" data-dismiss="alert">
						<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
					</button>
					Não há reservas para gerenciar no momento.
				</div>
			</c:if>
			<c:set var="atualizar" value="false"></c:set>

			<c:if test="${not empty ranking}">

				<div id="periodo" class="align-center orange">
					<label id="periodoLabel">${ranking.periodo.ano }.${ranking.periodo.semestre } - (${ranking.periodo.vagas } vagas)</label><br/>
				</div>

				<div class="titulo-ranking title">
					<a data-toggle="collapse" href="#collapseRanking" aria-expanded="false" aria-controls="#collapseRanking">Ranking</a><br/>
				</div>

				<div class="collapse in" id="collapseRanking">
					<table id="tableRanking" class="table">
						<thead>
							<tr>
								<th>#</th>
								<th>Nome</th>
								<th>Período</th>
								<th>Programa</th>
								<th>Pontuação</th>
								<th>Status</th>
								<th></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${ranking.tuplas }" var="tupla" varStatus="count">
								<form id="atualizarStatusReserva-${tupla.reserva.id }" action="/siaf/administracao/atualizarStatusReserva" method="POST">
									<tr class="${tupla.status }">
										<td class="align-center">${count.index + 1 }</td>
										<td>${tupla.professor }</td>
										<td class="align-center">${tupla.reserva.anoInicio }.${tupla.reserva.semestreInicio } a
											${tupla.reserva.anoTermino }.${tupla.reserva.semestreTermino }</td>
										<td class="align-center">${tupla.reserva.programa.descricao }</td>
										<td class="align-center"><fmt:formatNumber type="number" maxFractionDigits="2" value="${tupla.pontuacao }" /></td>
										<td class="align-center">
											<c:choose>
												<c:when
													test="${tupla.status != 'DESCLASSIFICADO' and tupla.status != 'NAO_ACEITO' and tupla.reserva.anoInicio == ranking.periodo.ano and tupla.reserva.semestreInicio == ranking.periodo.semestre}">
													<c:set var="atualizar" value="true"></c:set>
														<input type="hidden" value="${tupla.reserva.id }" name="idReserva" />
														<select id="${tupla.reserva.id }" name="status" class="form-control selectpicker">
															<option ${tupla.status == 'CLASSIFICADO' ? 'selected' : ''}
																value="${tupla.reserva.id }-ABERTO">ABERTO</option>
															<option ${tupla.status == 'AFASTADO' ? 'selected' : ''}
																value="${tupla.reserva.id }-AFASTADO">AFASTADO</option>
															<option ${tupla.status == 'CANCELADO' ? 'selected' : ''}
																value="${tupla.reserva.id }-CANCELADO">CANCELADO</option>
															<option ${tupla.status == 'CANCELADO_COM_PUNICAO' ? 'selected' : ''}
																value="${tupla.reserva.id }-CANCELADO_COM_PUNICAO">CANCELADO COM PUNIÇÃO</option>
															<option ${tupla.status == 'NEGADO' ? 'selected' : ''}
																value="${tupla.reserva.id }-NEGADO">NEGADO</option>
														</select>
												</c:when>
												<c:otherwise>
													${tupla.status.descricao }
												</c:otherwise>
											</c:choose>
										</td>
										<td class="align-center">
											<c:choose>
												<c:when
													test="${tupla.status != 'DESCLASSIFICADO' and tupla.status != 'NAO_ACEITO' and tupla.reserva.anoInicio == ranking.periodo.ano and tupla.reserva.semestreInicio == ranking.periodo.semestre}">
													<input name="reservar" type="submit" class="btn btn-siaf" value="salvar" />
												</c:when>
											</c:choose>
											</td>
									</tr>
								</form>
							</c:forEach>
						</tbody>
					</table>
				</div>

				<div class="titulo-ranking title">
					<a data-toggle="collapse" href="#collapseCanceladas" aria-expanded="false" aria-controls="#collapseCanceladas">
						Reservas Canceladas ou Negadas</a><br>
				</div>
				<div class="collapse in" id="collapseCanceladas">
					<c:if test="${not empty tuplasCanceladasNegadas }">
						<table id="tableReservasCanceladas" class="table">
							<thead>
								<tr>
									<th>#</th>
									<th>Nome</th>
									<th>Período</th>
									<th>Programa</th>
									<th>Status</th>
									<th></th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${tuplasCanceladasNegadas }" var="tupla" varStatus="count">
									<form id="atualizarStatusReserva" action="/siaf/administracao/atualizarStatusReserva" method="POST">
									<tr class="${tupla.status }">
										<td class="align-center">${count.index + 1 }</td>
										<td>${tupla.professor }</td>
										<td class="align-center">${tupla.reserva.anoInicio }.${tupla.reserva.semestreInicio } 
											a ${tupla.reserva.anoTermino }.${tupla.reserva.semestreTermino }</td>
										<td class="align-center">${tupla.reserva.programa.descricao }</td>

										<td class="align-center">
											<c:choose>
												<c:when
													test="${tupla.status != 'DESCLASSIFICADO' and tupla.status != 'NAO_ACEITO' and tupla.status != 'CLASSIFICADO' and tupla.status != 'AFASTADO' and tupla.reserva.anoInicio == ranking.periodo.ano and tupla.reserva.semestreInicio == ranking.periodo.semestre}">
													<c:set var="atualizar" value="true"></c:set>
														<input type="hidden" value="${tupla.reserva.id }" name="idReserva" /> <select
															id="${tupla.reserva.id }" name="status" class="form-control selectpicker">
															<option ${tupla.status == 'CLASSIFICADO' ? 'selected' : ''}
																value="${tupla.reserva.id }-ABERTO">ABERTO</option>
															<option ${tupla.status == 'AFASTADO' ? 'selected' : ''}
																value="${tupla.reserva.id }-AFASTADO">AFASTADO</option>
															<option ${tupla.status == 'CANCELADO' ? 'selected' : ''}
																value="${tupla.reserva.id }-CANCELADO">CANCELADO</option>
															<option ${tupla.status == 'CANCELADO_COM_PUNICAO' ? 'selected' : ''}
																value="${tupla.reserva.id }-CANCELADO_COM_PUNICAO">CANCELADO COM PUNIÇÃO</option>
															<option ${tupla.status == 'NEGADO' ? 'selected' : ''}
																value="${tupla.reserva.id }-NEGADO">NEGADO</option>
														</select>
												</c:when>
												<c:otherwise>
													${tupla.status.descricao }
												</c:otherwise>
											</c:choose>
										</td>
										<td class="align-center">
											<c:choose>
												<c:when
													test="${tupla.status != 'DESCLASSIFICADO' and tupla.status != 'NAO_ACEITO' and tupla.status != 'CLASSIFICADO' and tupla.status != 'AFASTADO' and tupla.reserva.anoInicio == ranking.periodo.ano and tupla.reserva.semestreInicio == ranking.periodo.semestre}">
													<input name="reservar" type="submit" class="btn btn-siaf" value="salvar" />
												</c:when>
											</c:choose>
										</td>
									</tr>
									</form>
								</c:forEach>
							</tbody>
						</table>
					</c:if>
					<c:if test="${empty tuplasCanceladasNegadas }">
						<div id="warning-ranking" class="alert warning alert-warning alert-dismissible" role="alert">
							Não há nenhuma reserva cancelada ou negada para este período.
						</div>
					</c:if>
				</div>
			</c:if>
		</div>

		<jsp:include page="../modulos/footer.jsp" />

	</div>
</body>
<script type="text/javascript">
	$('#menu-homologacao').addClass('active');
</script>
</html>

