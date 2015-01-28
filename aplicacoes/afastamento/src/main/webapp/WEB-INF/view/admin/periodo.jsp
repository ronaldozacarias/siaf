<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<html>
<head>
<jsp:include page="../modulos/header-estrutura.jsp" />
<title>SiAf - Período</title>
</head>
<body>
	<div id="wrapper" class="container">
		<jsp:include page="../modulos/header.jsp" />
		<div id="content">
					
			<div class="title"> Selecione o periodo : </div>
			<span class="line"></span>

			<c:if test="${not empty erro}">
					<div class="alert alert-danger alert-dismissible margin-top" role="alert">
					<button type="button" class="close" data-dismiss="alert">
						<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
					</button>
					<c:out value="${erro}"></c:out>
				</div>
			</c:if>

			<c:if test="${not empty info}">
				<div style="" class="alert alert-success alert-dismissible margin-top" role="alert">
					<button type="button" class="close" data-dismiss="alert">
						<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
					</button>
					<c:out value="${info}"></c:out>
				</div>
			</c:if>
			
			<div id="filtroPeriodo">
				<div class="col-sm-1">
					<input id="filtroAno" name="ano" type="text" class="form-control" placeholder="ano">
				</div>
				<select id="filtroSemestre" name="semestre" class="selectpicker filtroSemestre" data-width="auto">
					<option value="">Semestre</option>
					<option value="1">1</option>
					<option value="2">2</option>
				</select>
			</div>
			
			
			<div id="viewPeriodo"  class="form-horizontal" align="center">
				<c:if test="${not empty periodo}">
					<c:if test="${permitirUpdate}">

						<div id="update-periodo" class="panel panel-default">
							<div id="periodo-heading" class="panel-heading"><strong>Periodo: <label class="value-label">${periodo.ano }.${periodo.semestre }</label></strong></div>

							<div id="periodo-body" class="panel-body">
								<form:form id="form-periodo" commandName="periodo" action="/siaf/administracao/update-periodo" method="POST" >
									<form:hidden id="chave" path="id"/>
									<form:hidden id="anoHidden" path="ano"/>
									<form:hidden id="semestre" path="semestre"/>
									
									<div class="periodo">
			                            <div class="form-item form-group form-inline input-periodo">

											<c:if test="${permitirUpdateEncerramento}">
												<label for="encerramento" class="control-label">Encerramento:</label>
												<form:input id="encerramento" type="text" path="encerramento" name="encerramento" cssClass="form-control data" required="required"/>
												<div class="error-validation">
													<form:errors path="encerramento"></form:errors>
													<c:out value="${errorData}"></c:out>
												</div>

											</c:if>

											<c:if test="${not permitirUpdateEncerramento}">
												<label for="encerramento" class="control-label">Encerramento: ${periodo.encerramento}</label>
											</c:if>
			                            </div>
			
			                            <div class="form-item form-group form-inline input-periodo">
											<c:if test="${permitirUpdateVagas}">
												<label for="vagas" class="control-label">Vagas:</label>
												<form:input id="vagas" type="number" path="vagas" min="0" size="3" cssClass="form-control" required="required"/>
												<div class="error-validation">
													<form:errors path="vagas"></form:errors>
												</div>
											</c:if>

											<c:if test="${not permitirUpdateVagas}">
												<label for="vagas" class="control-label">Vagas: ${periodo.vagas}</label>
											</c:if>
			                            </div>
									</div>
									<div class="clear"></div>
									<div>
										<input id="btn-update" name="atualizar" type="submit" class="btn btn-primary" value="Atualizar" />
									</div>
								</form:form>
							</div>					
						</div>					
					
					</c:if>
	
					<c:if test="${not permitirUpdate}">
					
						<div id="update-periodo" class="panel panel-default">
							<input type="hidden" id="chave" value="${periodo.id }"/>
							<fmt:formatDate var="data" value="${periodo.encerramento }" pattern="dd/MM/yyyy" />

							<div id="periodo-heading" class="panel-heading"><strong>Periodo: <label class="value-label">${periodo.ano }.${periodo.semestre }</label></strong></div>

							<div id="periodo-body" class="panel-body">
									<div class="periodo">
			                            <div class="form-item form-group form-inline input-periodo">
											<label for="encerramento" class="control-label">Encerramento: ${data}</label>
			                            </div>
			
			                            <div class="form-item form-group form-inline input-periodo">
											<label for="vagas" class="control-label">Vagas: ${periodo.vagas}</label>
			                            </div>
									</div>
							</div>					
						</div>					
					</c:if>
				</c:if>
				
				<c:if test="${empty periodo}">
						<div id="update-periodo" class="alert alert-success alert-dismissible margin-top-10" role="alert">
							<button type="button" class="close" data-dismiss="alert">
								<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
							</button>
							<c:out value="${message}"></c:out>
						</div>
				</c:if>
			</div>
		</div>
		<jsp:include page="../modulos/footer.jsp" />
	</div>

	<div id="listaPeriodos" class="container">
	    <div class="panel-group" id="accordion">
	        <div class="panel panel-default">
	            <div class="panel-heading">
	                <h4 class="panel-title">
	                    <a data-toggle="collapse" data-parent="#accordion" href="#collapseOne"><span class="glyphicon glyphicon-link"></span>Veja todos os periodos</a>
	                </h4>
	            </div>
	            <div id="collapseOne" class="panel-collapse collapse">
					<div>
						<table id="periodos" class="table table-striped">
							<thead>
								<tr class="afas-tr-left">
						               <th data-column-id="ano" data-order="asc" data-align="center">Ano</th>
						               <th data-column-id="semestre" data-align="center">Semestre</th>
						               <th data-column-id="vagas" data-align="center">Vagas</th>
						               <th data-column-id="status" data-formatter="status" data-align="center">Status</th>
						           </tr>
						       </thead>
						</table>
					</div>
	            </div>
	        </div>
		</div>
	</div>

	<script type="text/javascript">
		getPeriodos();
		$('#menu-periodos').addClass('active');
	</script>
</body>
</html>

