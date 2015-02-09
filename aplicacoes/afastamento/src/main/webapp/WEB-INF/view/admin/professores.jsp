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
				               <th>Nº</th>
				               <th class="afas-tr-left">Siape</th>
				               <th id="orderName">Nome</th>
				               <th>E-mail</th>
				               <th>Admisão</th>
				               <th></th>
				               <!-- <th>Status</th> -->
				           </tr>
				       </thead>
				       <tbody id="contentProfessores">
						<c:forEach items="${professores}" var="professor" varStatus="count">
					           <tr>
					               <td>${count.count}</td>
					               <td>${professor.siape}</td>
					               <td>${professor.nome}</td>
					               <td>${professor.email}</td>
					               <td class="editProf" style="width: 10px;">
					        	       	<span class="anoEdit" data-name="anoAdmissaoEdit">${professor.anoAdmissao}</span>.<span class="semestreEdit">${professor.semestreAdmissao}</span>
									</td> 
									<td class="editAcao" style="width: 40px;" align="right">
					    	           	<button class="btn edit" data-id="${professor.id}"><i class="fa fa-pencil "></i></button>
					    	           	<div class="options options${professor.id} hide">
							                <button class="btn salvar" data-id="${professor.id}">salvar</button>
						    	           	<button class="btn cancel" data-id="${professor.id}"><i class="fa fa-times "></i></button>									
					    	           	</div>
					    	        </td>
					               <%-- <td class="habilitado"><a href="#" data-pk="${professor.id}" data-title="Desabilitar prof(a) ${professor.nome}">${professor.usuario.habilitado ? 'Habilitado':''}</a></td> --%>
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

