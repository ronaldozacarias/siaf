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
			<div class="title"><br></div>
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

				<div id="periodo">
					<label id="periodoLabel">${ranking.periodo.ano }.${ranking.periodo.semestre }</label>
				</div>
				<div class="align-center">
					<label id="vagas">${ranking.periodo.vagas } vagas</label>
				</div>

				<div class="titulo-ranking title">
					<a data-toggle="collapse" href="#collapseDivRankingInReservasJSP">Ranking</a><br>
				</div>

				<div class="collapse in" id="collapseDivRankingInReservasJSP">
					<table id="tableRankingInReservasJSP" class="table">
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
								<tr class="${tupla.status }">
									<td class="align-center">${count.index + 1 }</td>
									<td>${tupla.professor }</td>
									<td class="align-center">${tupla.reserva.anoInicio }.${tupla.reserva.semestreInicio }a
										${tupla.reserva.anoTermino }.${tupla.reserva.semestreTermino }</td>
									<td class="align-center">${tupla.reserva.programa.descricao }</td>
									<td class="align-center">${tupla.pontuacao }</td>
									<td class="align-center"><c:choose>
											<c:when
												test="${tupla.status != 'DESCLASSIFICADO' and tupla.status != 'NAO_ACEITO' and tupla.reserva.anoInicio == ranking.periodo.ano and tupla.reserva.semestreInicio == ranking.periodo.semestre}">
												<c:set var="atualizar" value="true"></c:set>
												<form id="atualizarStatusReserva" action="/siaf/administracao/atualizarStatusReserva"
													method="POST">
													<input type="hidden" value="${tupla.reserva.id }" name="idReserva" /> <select
														id="${tupla.reserva.id }" name="status" class="form-control selectpicker">
														<option ${tupla.status == 'CLASSIFICADO' ? 'selected' : ''}
															value="${tupla.reserva.id }-ABERTO">CLASSIFICADO</option>
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
										</c:choose></td>
									<td class="align-center"><c:choose>
											<c:when
												test="${tupla.status != 'DESCLASSIFICADO' and tupla.status != 'NAO_ACEITO' and tupla.reserva.anoInicio == ranking.periodo.ano and tupla.reserva.semestreInicio == ranking.periodo.semestre}">
												<input name="reservar" type="submit" class="btn btn-siaf" value="salvar" />
											</c:when>
										</c:choose>
										</form></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>

				<div class="titulo-ranking title">
					<a data-toggle="collapse" href="#collapseDivReservasCanceladasNegadasInReservasJSP">Reservas
						Canceladas ou Negadas</a><br>
				</div>
				<div class="collapse in" id="collapseDivReservasCanceladasNegadasInReservasJSP">
					<c:if test="${not empty tuplasCanceladasNegadas }">
						<table id="tableReservasCanceladasNegadasInReservasJSP" class="table">
							<thead>
								<tr>
									<th>#</th>
									<th>Nome</th>
									<th>Período</th>
									<th>Programa</th>
									<th>Status</th>
									<th></th>
									<th></th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${tuplasCanceladasNegadas }" var="tupla" varStatus="count">
									<tr class="${tupla.status }">
										<td class="align-center">${count.index + 1 }</td>
										<td>${tupla.professor }</td>
										<td class="align-center">${tupla.reserva.anoInicio }.${tupla.reserva.semestreInicio }
											a ${tupla.reserva.anoTermino }.${tupla.reserva.semestreTermino }</td>
										<td class="align-center">${tupla.reserva.programa.descricao }</td>

										<td class="align-center"><c:choose>
												<c:when
													test="${tupla.status != 'DESCLASSIFICADO' and tupla.status != 'NAO_ACEITO' and tupla.status != 'CLASSIFICADO' and tupla.status != 'AFASTADO' and tupla.reserva.anoInicio == ranking.periodo.ano and tupla.reserva.semestreInicio == ranking.periodo.semestre}">
													<c:set var="atualizar" value="true"></c:set>
													<form id="atualizarStatusReserva" action="/siaf/administracao/atualizarStatusReserva"
														method="POST">
														<input type="hidden" value="${tupla.reserva.id }" name="idReserva" /> <select
															id="${tupla.reserva.id }" name="status" class="form-control selectpicker">
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
											</c:choose></td>
										<td class="align-center">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
										<td class="align-center"><c:choose>
												<c:when
													test="${tupla.status != 'DESCLASSIFICADO' and tupla.status != 'NAO_ACEITO' and tupla.status != 'CLASSIFICADO' and tupla.status != 'AFASTADO' and tupla.reserva.anoInicio == ranking.periodo.ano and tupla.reserva.semestreInicio == ranking.periodo.semestre}">
													<input name="reservar" type="submit" class="btn btn-siaf" value="salvar" />
												</c:when>
											</c:choose>
											</form></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</c:if>
					<c:if test="${empty tuplasCanceladasNegadas }">
						<div id="warning-tuplasCanceladasNegadas" class="alert" role="alert">Não há nenhuma
							reserva cancelada ou negada para este periodo.</div>
					</c:if>
				</div>
		</div>
		</c:if>



		<jsp:include page="../modulos/footer.jsp" />

	</div>
</body>
<script type="text/javascript">
	$('#menu-reservas').addClass('active');
</script>
</html>

