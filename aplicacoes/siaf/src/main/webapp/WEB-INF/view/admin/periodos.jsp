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

<title>SiAf - Períodos</title>
</head>
<body>

	<div id="wrapper" class="container">
		<jsp:include page="../modulos/header.jsp" />
		<form id="atualizarPeriodo" action="/siaf/administracao/atualizarPeriodo" method="POST">
			<input id="periodoId" type="hidden" name="periodoId" value="" />
			<input id="encerramento" type="hidden" name="encerramento" value="" />
			<input id="vagas" type="hidden" name="vagas" value="" />
		</form>
		
		<div id="content">
			<c:if test="${not empty erro}">
				<div class="alert alert-danger alert-dismissible margin-top" role="alert">
					<button type="button" class="close" data-dismiss="alert">
						<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
					</button>
					<c:out value="${erro}"></c:out>
				</div>
			</c:if>
			<c:if test="${not empty info}">
				<div class="alert alert-info alert-dismissible" role="alert">
					<button type="button" class="close" data-dismiss="alert">
						<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
					</button>
					<c:out value="${info}"></c:out>
				</div>
			</c:if>
					
			<div>
				<table id="tablePeriodos" class="display" cellspacing="0" width="100%">
					<thead>
						<tr>
							<th class="align-center">Status</th>
							<th class="align-center">Ano</th>
							<th class="align-center">Semestre</th>
							<th class="align-center">Encerramento</th>
							<th class="align-center">Vagas</th>
							<th class="align-center"></th>
						</tr>
					</thead>
					<tbody id="bodyPeriodos">
						<c:forEach items="${periodos}" var="periodo" varStatus="cont">
							<tr id="periodo${periodo.id}">
								<fmt:formatDate value="${periodo.encerramento}" pattern="dd/MM/yyyy" var="data"/>
								<td class="align-center"><span class="label ${periodo.status eq 'ABERTO' ? 'label-success':'label-danger'}">${periodo.status}</span></td>
								<td class="align-center">${periodo.ano}</td>
								<td class="align-center">${periodo.semestre}</td>
								<c:if test="${periodo.status eq 'ABERTO'}" >  
							    	<td class="align-center">
							    		<input id="encerramento-${periodo.id}" name="encerramento" type="text" class="form-control data data-periodo" placeholder="a definir" value="${data }"/>
							    	</td>
       						    </c:if>
       						    <c:if test="${periodo.status != 'ABERTO'}" >  
							    	<td class="align-center">${data}</td>
       						    </c:if>
								
								<c:choose>
									<c:when test="${periodo.ano - periodoAtual.ano > 1 
										or (periodo.ano - periodoAtual.ano == 1 and periodo.semestre - periodoAtual.semestre >= 0)}">
										<td class="align-center">
							    			<input id="vagas-${periodo.id}" name="vagas" type="text" class="form-control data-periodo" value="${periodo.vagas }"/>
							    		</td>
									</c:when>
									<c:otherwise>
										<td class="align-center">${periodo.vagas}</td>
									</c:otherwise>
								</c:choose>
									
								<td>
								    <c:if test="${periodo.status eq 'ABERTO'}" >  
								    	<input type="button" class="btn btn-siaf salvar-periodo" value="salvar" data-id="${periodo.id }"/>
        						    </c:if>  	
								</td>
			           		</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>	

	<jsp:include page="../modulos/footer.jsp" />
	<script src="<c:url value="/resources/js/jquery.dataTables.min.js" />"></script>

	<script type="text/javascript">
		$('#menu-periodos').addClass('active');
	</script>
</body>
</html>