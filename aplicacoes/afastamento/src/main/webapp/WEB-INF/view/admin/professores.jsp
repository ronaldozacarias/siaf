<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<html>
<head>
<jsp:include page="../modulos/header-estrutura.jsp" />
<title>SiAf - Professores</title>
</head>
<body>
<div class="agroup">
	<div id="wrapper">
		<jsp:include page="../modulos/header.jsp" />
		<div id="content">

		<div class="title"> Professores : </div>
		<span class="line"></span>
		
			<div class="container">
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
			</div>
			
			<div class="container">
				<table id="professores" class="table table-striped">
					<thead>
						<tr class="afas-tr-left">
				               <th class="afas-tr-left">Siape</th>
				               <th id="orderName">Nome</th>
				               <th>E-mail</th>
				               <th>Admisão</th>
				               <th>Status</th>
				           </tr>
				       </thead>
				       <tbody>
						<c:forEach items="${professores}" var="professor">
					           <tr>
					               <td>${professor.siape}</td>
					               <td>${professor.usuario.nome}</td>
					               <td>${professor.usuario.email}</td>
					               <td>${professor.anoAdmissao}.${professor.semestreAdmissao}</td>
					               <td class="habilitado"><a href="#" data-pk="${professor.id}" data-title="Desabilitar prof(a) ${professor.usuario.nome}">${professor.usuario.habilitado ? 'Habilitado':''}</a></td>
					           </tr>
						</c:forEach>
				       </tbody>
				</table>
			</div>
		</div>
	<jsp:include page="../modulos/footer.jsp" />
	</div>
	
	
	<script type="text/javascript">
		$('#menu-professores').addClass('active');
	</script>
</div>
</body>
</html>

