<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<html>
<head>
<jsp:include page="../modulos/header-estrutura.jsp" />
<title>Incluir Reserva</title>
</head>
<body>
	<div id="wrapper">
		<jsp:include page="../modulos/header.jsp" />
		<div id="content">
			<div class="title">Inclua sua reserva de afastamento :</div>
			<span class="line"></span>
			<form:form id="solicitarAfastamento" commandName="reserva" action="/siaf/reserva/incluir"
				method="POST" class="form-horizontal">
				<c:if test="${not empty erro}">
					<div class="alert alert-danger alert-dismissible margin-top" role="alert">
						<button type="button" class="close" data-dismiss="alert">
							<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
						</button>
						<c:out value="${erro}"></c:out>
					</div>
				</c:if>

				<div class="form-group">
					<label class="col-sm-2 control-label">Nome:</label>
					<div class="col-sm-4">
						<label class="control-label value-label">${professor.nome }</label>
					</div>
					<label class="col-sm-2 control-label">Siape:</label>
					<div class="col-sm-4">
						<label class="control-label value-label">${professor.siape }</label>
					</div>
				</div>
				<div class="form-group">
					<div class="form-item">
						<label for="anoInicio" class="col-sm-2 control-label"><span class="obrigatorio">*</span>
							Início:</label>
						<div class="col-sm-4">
							<input id="anoInicioReserva" name="anoInicio" type="text" class="form-control" size="10"
								placeholder="ano" value="${anoInicio }" required="required" onKeyUp="somenteNumeros(this)" />
							<select id="semestreInicio" name="semestreInicio" class="form-control selectpicker">
								<option value="1" ${semestreInicio == 1 ? 'selected' : ''}>1</option>
								<option value="2" ${semestreInicio == 2 ? 'selected' : ''}>2</option>
							</select>
						</div>
					</div>
					<div class="form-item">
						<label for="anoTermino" class="col-sm-2 control-label"><span class="obrigatorio">*</span>
							Término:</label>
						<div class="col-sm-4">
							<input id="anoTerminoReserva" type="text" name="anoTermino" class="form-control" size="10"
								placeholder="ano" value="${anoTermino }" required="required" onKeyUp="somenteNumeros(this)" />
							<select
								<select id="semestreTermino" name="semestreTermino" class="form-control selectpicker">
								<option value="1" ${semestreTermino == 1 ? 'selected' : ''}>1</option>
								<option value="2" ${semestreTermino == 2 ? 'selected' : ''}>2</option>
							</select>
						</div>
					</div>
				</div>
				<div class="form-group">
					<div class="form-item">
						<label for="programa" class="col-sm-2 control-label">Programa:</label>
						<div class="col-sm-4">
							<select id="programa" name="programa" class="form-control selectpicker">
								<c:forEach items="${programa}" var="prog">
									<option value="${prog }" ${programaSelecionado == prog ? 'selected' : ''}>${prog.descricao }</option>
								</c:forEach>
							</select>
						</div>
					</div>
					<div class="form-item">
						<label for="conceito" class="col-sm-2 control-label">Conceito do Programa:</label>
						<div class="col-sm-4">
						
						<select id="conceito" name="conceito" class="form-control selectpicker">
								<option value="3" ${conceito == 3 ? 'selected' : ''}>3</option>
								<option value="4" ${conceito == 4 ? 'selected' : ''}>4</option>
								<option value="5" ${conceito == 5 ? 'selected' : ''}>5</option>
								<option value="6" ${conceito == 6 ? 'selected' : ''}>6</option>
								<option value="7" ${conceito == 7 ? 'selected' : ''}>7</option>
							</select>
						</div>
					</div>
				</div>

				<div class="form-group form-item">
					<label for="instituicao" class="col-sm-2 control-label">Instituição:</label>
					<div class="col-sm-8">
						<input id="instituicao" name="instituicao" type="text" class="form-control"
							value="${instituicao }" placeholder="instituição" style="width: 100% !important" />
					</div>
				</div>

				<div class="form-group col-sm-2 control-label">
					<span class="obrigatorio">* Campos obrigatórios</span>
				</div>

				<div class="controls">
					<input name="reservar" type="submit" class="btn btn-siaf" value="Reservar" />
				</div>
			</form:form>
		</div>

		<jsp:include page="../modulos/footer.jsp" />

	</div>
	<script type="text/javascript">
		$('#menu-minhas-reservas').addClass('active');
	</script>
</body>
</html>

