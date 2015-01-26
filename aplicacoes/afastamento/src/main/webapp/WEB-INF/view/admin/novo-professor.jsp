<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<html>
<head>
<jsp:include page="../modulos/header-estrutura.jsp" />
<title>SiAf - Novo(a) Professor(a)</title>
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
			
			<div class="title"> Novo(a) Professor(a) : </div>
			<span class="line"></span>
			<form:form id="novo-professor" commandName="professor" action="/siaf/administracao/novo-professor" method="POST" class="form-horizontal">

				<!-- 1 LINHA -->
				<div class="form-group">
					<div class="form-item">
						<label for="nome" class="col-sm-2 control-label">Nome:</label>
						<div class="col-sm-4">
							<form:input id="nome" type="text" path="usuario.nome" size="50" cssClass="form-control" required="required"/>
							<div class="error-validation">
								<form:errors path="usuario.nome"></form:errors>
							</div>
						</div>
					</div>

					<div class="form-item">
						<label for="siape" class="col-sm-2 control-label">Siape:</label>
						<div class="col-sm-4">
							<form:input id="siape" type="text" path="siape" cssClass="form-control" required="required"/>
							<div class="error-validation">
								<form:errors path="siape"></form:errors>
							</div>
						</div>
					</div>
				</div>

				<!-- 2 LINHA -->
				<div class="form-group">
					<div class="form-item">
						<label for="email" class="col-sm-2 control-label">E-mail:</label>
						<div class="col-sm-4">
							<form:input id="email" type="text" path="usuario.email" size="50" cssClass="form-control" required="required"/>
							<div class="error-validation">
								<form:errors path="usuario.email"></form:errors>
							</div>
						</div>
					</div>

					<div class="form-item">
						<label for="cpf" class="col-sm-2 control-label">CPF:</label>
						<div class="col-sm-4">
							<form:input id="cpf" type="text" path="cpf" cssClass="form-control" placeholder="999.999.999-99" required="required"/>
							<div class="error-validation">
								<form:errors path="cpf"></form:errors>
							</div>
						</div>
					</div>
				</div>

				<!-- 3 LINHA -->
				<div class="form-group">
					<div class="form-item">
						<label for="anoAdmissao" class="col-sm-2 control-label">Admissão:</label>
						<div id="admissao" class="col-sm-4">
							<form:input id="anoAdmissao" type="text" path="anoAdmissao" size="4" placeholder="Ano" cssClass="form-control ano" required="required"/>
							<form:select id="semestreAdmissao" path="semestreAdmissao" class="form-control selectpicker" required="required">
								<option value="1">1</option>
								<option value="2">2</option>
							</form:select>
							<div class="error-validation">
								<form:errors path="anoAdmissao"></form:errors>
								<form:errors path="semestreAdmissao"></form:errors>
							</div>
						</div>
					</div>
					
					<div class="form-item">
						<label for="dataNascimento" class="col-sm-2 control-label">Data de Nascimento:</label>
						<div class="col-sm-4">
							<form:input id="dataNascimento" type="text" path="dataNascimento" cssClass="form-control"  placeholder="dd/mm/yyyy" required="required"/>
							<div class="error-validation">
								<form:errors path="dataNascimento"></form:errors>
							</div>
						</div>					
					</div>					
				</div>
				
				<div class="controls">
					<input name="Cadastrar" type="submit" class="btn btn-primary" value="Cadastrar" />
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

