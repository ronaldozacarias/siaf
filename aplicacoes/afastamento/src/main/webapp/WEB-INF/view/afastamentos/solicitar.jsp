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
			
			<div class="title"> Inclua sua reserva de afastamento : </div>
			<span class="line"></span>
			<form:form id="solicitar-afastamento" enctype="multipart/form-data" commandName="afastamento" action="/afastamento/afastamento/solicitar" method="POST" class="form-horizontal">
				
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
					<label for="ano-inicio" class="col-sm-2 control-label">Início:</label>
					<div class="col-sm-4">
						<input id="ano-inicio" name="ano-inicio" type="text" class="form-control" size="10">
						<select id="semestre-inicio" name="semestre-inicio" class="form-control selectpicker">
							<option value="1">1</option>
							<option value="2">2</option>
						</select>
					</div>
					<label for="ano-termino" class="col-sm-2 control-label">Término:</label>
					<div class="col-sm-4">
						<input id="ano-termino" type="text" name="ano-termino" class="form-control" size="10">
						<select id="semestre-termino" name="semestre-termino" class="form-control selectpicker" >
							<option value="1">1</option>
							<option value="2">2</option>
						</select>
					</div>
				</div>
				<div class="form-group">
					<label for="formacao" class="col-sm-2 control-label">Formação:</label>
					<div class="col-sm-4">
						<form:select id="formacao" path="" items="${formacao }" itemLabel="descricao" name="formacao" class="form-control selectpicker">
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label for="carta" class="col-sm-2 control-label">Carta de aceitação da instituição:</label>
					<div class="col-sm-4 files">
						<input type="file" id="carta" name="carta" class="file"></input>
					</div>
					<label for="termo" class="col-sm-2 control-label">Termo de compromisso e responsabilidade:</label>
					<div class="col-sm-4 files">
						<input type="file" id="termo" name="termo" class="file"></input>
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
</body>
</html>

