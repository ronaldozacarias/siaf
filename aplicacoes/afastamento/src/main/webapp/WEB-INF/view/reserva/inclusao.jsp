<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<html>
<head>
<jsp:include page="../modulos/header-estrutura.jsp" />
<title>SiAf - Solicitar Afastamento</title>
</head>
<body>
	<div id="wrapper">
		<jsp:include page="../modulos/header.jsp" />
		<div id="content">
			<div class="title"> Inclua sua reserva de afastamento : </div>
			<span class="line"></span>
			<form:form id="solicitarAfastamento" commandName="reserva" action="/afastamento/reserva/incluir" method="POST" class="form-horizontal">
				<c:if test="${not empty erro}">
					<div class="alert alert-danger alert-dismissible" role="alert">
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
						<label for="anoInicio" class="col-sm-2 control-label">Início:</label>
						<div class="col-sm-4">
							<input id="anoInicio" name="anoInicio" type="text" class="form-control ano" size="10" value="${anoInicio }" required="required"/>
							<select id="semestreInicio" name="semestreInicio" class="form-control selectpicker">
								<option value="1" ${semestreInicio == 1 ? 'selected' : ''}>1</option>
								<option value="2" ${semestreInicio == 2 ? 'selected' : ''}>2</option>
							</select>
						</div>
					</div>
					<div class="form-item">
						<label for="anoTermino" class="col-sm-2 control-label">Término:</label>
						<div class="col-sm-4">
							<input id="anoTermino" type="text" name="anoTermino" class="form-control ano" size="10" value="${anoTermino }" required="required"/>
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
							<input id="conceito" name="conceito" type=text class="form-control conceito" size="19" value="${conceito }" required="required"/>
						</div>
					</div>
				</div>
				
				<div class="form-group form-item">
					<label for="instituicao" class="col-sm-2 control-label">Instituição:</label>
					<div class="col-sm-8">
						<input id="instituicao" name="instituicao" type="text" class="form-control" value="${instituicao }" required="required" style="width: 100% !important"/>
					</div>
				</div>
				
				<div class="controls">
					<input name="reservar" type="submit" class="btn btn-primary" value="Reservar" />
					<a href="<c:url value="/afastamento/ranking"></c:url>" class="btn btn-default">Cancelar</a>
				</div>
			</form:form>
		</div>
		
		<jsp:include page="../modulos/footer.jsp" />
		
	</div>
	<script type="text/javascript">
		$('#menu-add-reserva').addClass('active');
	</script>
</body>
</html>

