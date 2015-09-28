<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<html>
<head>
<jsp:include page="../modulos/header-estrutura.jsp" />
<title>Relatórios</title>
</head>
<body>
	<div id="wrapper">
		<jsp:include page="../modulos/header.jsp" />
		<div id="content">						
			<div class="title"> Relatório de Reservas : </div>
			<span class="line"></span>
			<form id="relatorioReserva" action="/siaf/administracao/relatorio/reservas" method="POST" class="form-horizontal">
				<div class="form-group">
					<div class="form-item">
						<label for="anoInicio" class="col-sm-2 control-label">Início:</label>
						<div class="col-sm-4">
							<input id="anoInicio" name="anoInicio" type="text" class="form-control ano-all" size="10" placeholder="ano" value="${reserva.anoInicio }" required="required"/>
							<select id="semestreInicio" name="semestreInicio" class="form-control selectpicker">
								<option value="1" ${reserva.semestreInicio == 1 ? 'selected' : ''}>1</option>
								<option value="2" ${reserva.semestreInicio == 2 ? 'selected' : ''}>2</option>
							</select>
						</div>
					</div>
					<div class="form-item">
						<label for="anoTermino" class="col-sm-2 control-label">Término:</label>
						<div class="col-sm-4">
							<input id="anoTermino" type="text" name="anoTermino" class="form-control ano-all" size="10" placeholder="ano" value="${reserva.anoTermino }" required="required"/>
							<select id="semestreTermino" name="semestreTermino" class="form-control selectpicker">
								<option value="1" ${reserva.semestreTermino == 1 ? 'selected' : ''}>1</option>
								<option value="2" ${reserva.semestreTermino == 2 ? 'selected' : ''}>2</option>
							</select>
						</div>
					</div>
				</div>
				
				<div class="form-group">
					<div class="form-item">
						<label for="programa" class="col-sm-2 control-label">Programa:</label>
						<div class="col-sm-4">
							<select id="programa" name="programa" class="form-control selectpicker">
								<option value="TODOS">TODOS</option>
								<c:forEach items="${programa}" var="prog">
									<option value="${prog }">${prog.descricao }</option>
								</c:forEach>
							</select>
						</div>
					</div>
					<div class="form-item">
						<label for="status" class="col-sm-2 control-label">Status:</label>
						<div class="col-sm-4">
							<select id="status" name="status" class="form-control selectpicker">
								<option value="TODOS">TODOS</option>
								<c:forEach items="${status}" var="st">
									<option value="${st }">${st.descricao }</option>
								</c:forEach>
							</select>
						</div>
					</div>
				</div>
				
				<div class="form-group form-item">
					<label for="professor" class="col-sm-2 control-label">Professor:</label>
					<div class="col-sm-8">
						<input id="professor" name="professor" type="text" class="form-control" placeholder="professor" style="width: 100% !important"/>
					</div>
				</div>
				
				<div class="controls">
					<input type="submit" class="btn btn-siaf" value="Gerar Relatório" />
				</div>
			</form>
		</div>
	</div>
	
	<jsp:include page="../modulos/footer.jsp" />
	
	<script type="text/javascript">
		$('#menu-relatorios').addClass('active');
	</script>
</body>
</html>
