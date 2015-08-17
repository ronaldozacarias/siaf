<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<html>
<head>

<jsp:include page="../modulos/header-estrutura.jsp" />
<title>Editar Admissão</title>
</head>
<body>

<div id="wrapper">
		<jsp:include page="../modulos/header.jsp" />
		<div id="content">
			<div class="title">Editar admissão de professor:</div>
			<span class="line"></span>
			<form:form id="formEditarAdmissao" commandName="reserva" action="/siaf/administracao/editar-admissao"
				method="POST"  class="form-horizontal">
				<input type="hidden" id="id" name="id" value="${professor.id }"/>
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
					<label class="col-sm-2 control-label">Email:</label>
					<div class="col-sm-4">
						<label class="control-label value-label">${professor.email }</label>
					</div>
					<label class="col-sm-2 control-label">Data de nascimento:</label>
					<div class="col-sm-4">
						<label class="control-label value-label"><fmt:formatDate pattern="dd/MM/yyyy" value="${professor.dataNascimento }" /></label>
					</div>
				</div>
				<div class="form-group form-item">
					<label for="ano" class="col-sm-2 control-label"><span class="obrigatorio">*</span> Admissão:</label>
					<div class="col-sm-4">
						<input id="ano" name="ano" type="text" class="form-control ano-all" size="10" placeholder="ano" value="${professor.anoAdmissao }" required="required"/>
						<select id="semestre" name="semestre" class="form-control selectpicker">
							<option value="1" ${professor.semestreAdmissao == 1 ? 'selected' : ''}>1</option>
							<option value="2" ${professor.semestreAdmissao == 2 ? 'selected' : ''}>2</option>
						</select>
					</div>
				</div>

				<div class="form-group col-sm-2 control-label obrigatorio">
					<span>* Campos obrigatórios</span>
				</div>

				<div class="controls">
					<input name="atualizar" type="submit" class="btn btn-siaf" value="Atualizar" />
				</div>
			</form:form>
			
			
			
		</div>
		<jsp:include page="../modulos/footer.jsp" />

	</div>
	<script type="text/javascript">
		$('#menu-professores').addClass('active');
	</script>
</body>
</html>

