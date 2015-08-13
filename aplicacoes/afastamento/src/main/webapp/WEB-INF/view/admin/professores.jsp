<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<html>
<head>
<jsp:include page="../modulos/header-estrutura.jsp" />
<title>Professores</title>
</head>
<body>
	<div class="agroup">
		<div id="wrapper">
			<jsp:include page="../modulos/header.jsp" />

			<div id="content">						
				<div class="container" id="message">
					<c:if test="${not empty erro}">
						<div class="alert alert-danger alert-dismissible" role="alert">
							<button type="button" class="close" data-dismiss="alert">
								<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
							</button>
							<c:out value="${erro}"></c:out>
						</div>
					</c:if>
					<c:if test="${not empty info}">
						<div class="alert alert-info alert-dismissible"
							role="alert">
							<button type="button" class="close" data-dismiss="alert">
								<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
							</button>
							<c:out value="${info}"></c:out>
						</div>
					</c:if>
				</div>

				<div class="controls">
					<a id="atualizarLista" href="<c:url value="/administracao/atualizar-professores" />" class="btn btn-siaf">Atualizar Lista</a>	
				</div>
				<br>
				<div class="container">
					<table id="tableProfessores" class="table table-striped">
						<thead>
							<tr>
								<th>Siape</th>
								<th>Nome</th>
								<th>Email</th>
								<th>Admissão</th>
								<th></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${professores}" var="professor">
								<input type="hidden" value="${professor.semestreAdmissao }" id="semestreAdmissao"/>
								<tr>
									<td class="align-center">${professor.siape}</td>
									<td>${professor.nome}</td>
									<td>${professor.email}</td>
									<td  class="align-center">${professor.anoAdmissao}.${professor.semestreAdmissao}</td>
									<td  class="align-center">
										<a title="Editar" href="<c:url value="/administracao/editar-admissao/${professor.id }" />" class="btn btn-default">
											<i class="fa fa-pencil"></i>
                                        </a>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
				<jsp:include page="../modulos/footer.jsp" />
			</div>
		</div>
	</div>
	<script type="text/javascript">
		$('#menu-professores').addClass('active');
	</script>
</body>
</html>
