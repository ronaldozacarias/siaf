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
										<c:when test="${reserva.status == 'ABERTO' or reserva.status == 'EM_ESPERA'}"><span class="label label-success">${reserva.status.descricao}</span></c:when>
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
								<td class="align-center">
									<a id="detalhes" title="Detalhes" data-toggle="modal"
										data-target="#detalhes-reserva" href="#"
										data-id="${reserva.id }">
										<button class="btn btn-primary">
											<i class="fa fa-info-circle"></i>
										</button>
									</a>
                                    <sec:authorize ifAnyGranted="ADMIN-SIAF">
                                   		<c:if test="${reserva.status eq 'ABERTO' or reserva.status == 'EM_ESPERA'}">
	                                   		<a title="Editar" href="<c:url value="/administracao/editar-reserva/${reserva.id }" />" class="btn btn-default">
												<i class="fa fa-pencil"></i>
	                                        </a>
                                        </c:if>
                                    	<c:if test="${reserva.status eq 'ABERTO' }">
											<a id="cancelar" title="Cancelar" data-toggle="modal"
												data-target="#cancelar-reserva" href="#"
												data-id="${reserva.id }">
												<button class="btn btn-danger">
													<i class="fa fa-times"></i>
												</button>
											</a>
                                        </c:if>
                                    	<c:if test="${reserva.status == 'EM_ESPERA'}">
	                                        <a id="excluir" title="Excluir" data-toggle="modal"
												data-target="#admin-excluir-reserva" href="#"
												data-href="<c:url value="/administracao/excluir-reserva/${reserva.id}"></c:url>"
												data-name="${reserva.anoInicio}.${reserva.semestreInicio} a ${reserva.anoTermino}.${reserva.semestreTermino}"
												data-professor="${reserva.professor.nome}">
												<button class="btn btn-danger">
													<i class="fa fa-trash-o"></i>
												</button>
											</a>
										</c:if>
                                    </sec:authorize>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<jsp:include page="../modulos/footer.jsp" />
	
	<!-- Modal Excluir Reserva -->
	<div class="modal fade" id="admin-excluir-reserva" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="excluirModalLabel">Excluir</h4>
				</div>
				<div class="modal-body"></div>
				<div class="modal-footer">
					<a href="#" class="btn btn-danger">Excluir</a>
					<button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- Modal Cancelar Reserva -->
		<div class="modal fade" id="cancelar-reserva" tabindex="-1" role="dialog"
			aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<h4 class="modal-title" id="cancelarModalLabel">Cancelar</h4>
					</div>
					<form id="formCancelarReserva" action="/siaf/administracao/cancelar-reserva" method="POST">
						<div class="modal-body">
							Tem certeza de que deseja cancelar essa reserva?<br/><br/>
							<input type="hidden" id="reservaId" name="id" value=""/>
							<div class="form-group">
								<div class="form-item">
									<label class="control-label" for="motivo">Motivo:</label>
									<textarea class="form-control" id="motivo" name="motivo" required="required"></textarea>
								</div>
							</div>
						</div>
						<div class="modal-footer">
							<button type="submit" class="btn btn-danger">Sim</button>
							<button type="button" class="btn btn-default" data-dismiss="modal">Não</button>
						</div>
					</form>
				</div>
			</div>
		</div>
	
	<!-- Modal Detalhes Reserva -->
		<div class="modal fade" id="detalhes-reserva" tabindex="-1" role="dialog"
			aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog modal-details">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<h4 class="modal-title" id="excluirModalLabel">Detalhes</h4>
					</div>
					<div class="modal-body">
						<div id="load-siaf">
							<div class=loading-siaf></div>
							<div class="load-s"></div>
						</div>
						<div id="detalhes" class="form-horizontal" style="display: none">
							<div class="form-group">
								<label class="col-sm-2 control-label">Professor:</label>
								<div class="col-sm-8">
									<label id="detalhe-professor" class="control-label value-label"></label>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">Período:</label>
								<div class="col-sm-4">
									<label id="detalhe-periodo" class="control-label value-label"></label>
								</div>
								<label class="col-sm-2 control-label">Status:</label>
								<div class="col-sm-4">
									<label id="detalhe-status" class="control-label value-label"></label>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">Programa:</label>
								<div class="col-sm-4">
									<label id="detalhe-programa" class="control-label value-label"></label>
								</div>
								<label class="col-sm-2 control-label">Conceito:</label>
								<div class="col-sm-4">
									<label id="detalhe-conceito" class="control-label value-label"></label>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">Instituição:</label>
								<div class="col-sm-8">
									<label id="detalhe-instituicao" class="control-label value-label"></label>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">Solicitação:</label>
								<div class="col-sm-4">
									<label id="detalhe-solicitacao" class="control-label value-label"></label>
								</div>
								<label class="col-sm-2 control-label data-cancelamento">Cancelamento:</label>
								<div class="col-sm-4 data-cancelamento">
									<label id="detalhe-data-cancelamento" class="control-label value-label"></label>
								</div>
							</div>
							<div id="motivo" class="form-group">
								<label class="col-sm-2 control-label">Motivo:</label>
								<div class="col-sm-8">
									<label id="detalhe-motivo" class="control-label value-label"></label>
								</div>
							</div>
						</div>
						<div id="detalhe-erro" class="alert alert-danger alert-dismissible" role="alert" style="display: none">
							Ocorreu um erro ao buscar os detalhes da reserva.
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">Fechar</button>
					</div>
				</div>
			</div>
		</div>

	<script type="text/javascript">
		$('#menu-reservas').addClass('active');
		relatorioReservas();
	</script>
</body>
</html>
