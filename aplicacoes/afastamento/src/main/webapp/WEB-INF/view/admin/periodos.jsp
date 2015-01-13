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
			
			<div class="title"> Periodos : </div>
			<span class="line"></span>

			<div>
				<div class="col-sm-1">
					<input id="filtroAno" name="ano" type="text" class="form-control">
				</div>
				<select id="filtroSemestre" name="semestre" class="selectpicker filtroSemestre" data-width="auto">
					<option value="">Semestre</option>
					<option value="1">1</option>
					<option value="2">2</option>
				</select>
			</div>
			
			
			<div id="viewPeriodo"  class="form-horizontal" align="center">
				<form:form id="update-periodo" commandName="periodo" action="/afastamento/administracao/update-periodo" method="POST" >
					<form:hidden id="chave" path="id"/>
					<form:hidden id="anoHidden" path="ano"/>
					<form:hidden id="semestre" path="semestre"/>
					<form:hidden id="encerramentoHidden" path="encerramento"/>					
					<div class="form-group center">
						<label class="control-label">Periodo:</label>
						<label class="control-label value-label">${periodo.ano }.${periodo.semestre }</label>					
					</div>
					
					<div class="form-group center">
						<label class="col-sm-1 control-label">Status:</label>
						<div class="col-sm-2">
							<form:select id="status" name="status" path="status" class="form-control selectpicker" >
								<form:options items="${status}" itemLabel="descricao" />
							</form:select>
							<div class="error-validation">
								<form:errors path="status"></form:errors>
							</div>
						</div>

						<label for="vagas" class="col-sm-2 control-label">Vagas:</label>
						<div class="col-sm-2">
							<form:input id="vagas" name="nome" type="number" path="vagas" min="0" size="3" cssClass="form-control"/>
							<div class="error-validation">
								<form:errors path="vagas"></form:errors>
							</div>
						</div>
						<label for="encerramento" class="col-sm-2 control-label">Encerramento:</label>
						<div class="col-sm-2">
							<fmt:formatDate type="both" value="${periodo.encerramento}" var="dataFormatada" />
							<span>dataFormatada : ${dataFormatada}</span>
							<input id="encerramento" type="text" value="${dataFormatada}" class="form-control" />
							<div class="error-validation">
								<span>${errorData }</span>
							</div>
						</div>
					</div>

				<div class="controls">
					<input name="Cadastrar" type="submit" class="btn btn-primary" value="Cadastrar" />
					<a href="<c:url value="#"></c:url>" class="btn btn-default">Cancelar</a>
					<a  id="updatePeriodo" class="btn btn-primary">Update</a>
				</div>
					
				</form:form>
			</div>

		</div>
		<jsp:include page="../modulos/footer.jsp" />
	</div>
</body>
</html>

