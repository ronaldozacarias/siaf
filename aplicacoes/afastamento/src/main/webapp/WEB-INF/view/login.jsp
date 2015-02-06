<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
<link href="<c:url value="/webjars/bootstrap/3.3.2/css/bootstrap.min.css" />" rel="stylesheet" />
<link href="<c:url value="/resources/css/estilo.css" />" rel="stylesheet" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Página de Login</title>
</head>

<body>
	<section id="login">
	    <div class="container">
	    	<div class="row">
	    	    <div class="col-xs-12">
	        	    <div class="form-wrap">
	        	    	<img alt="Sistema de Afastamento de Professores" src="<c:url value="/resources/images/logo.png" />">
		                <c:if test="${not empty erro}">
							<div class="alert alert-danger alert-dismissible" role="alert">
								<button type="button" class="close" data-dismiss="alert">
									<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
								</button>
								<c:out value="${erro}"></c:out>
							</div>
						</c:if>
		                <h1>Login:</h1>
						<c:if test="${not empty info}">
							<div class="alert alert-success alert-dismissible" role="alert">
								<button type="button" class="close" data-dismiss="alert">
									<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
								</button>
								<c:out value="${info}"></c:out>
							</div>
						</c:if>
						
		                <!-- Login form -->
	                    <form:form role="form" servletRelativeAction="/j_spring_security_check"  method="post" id="login-form" autocomplete="off" class="form-horizontal">
	                        <div class="form-group">
	                            <label for="cpf" class="col-sm-2 control-label">cpf:</label>
	                            <div class="col-sm-8">
	                            	<input type="text" name="j_username" id="cpf" class="form-control" placeholder="cpf" required="required" style="width: 100% !important">
	                            </div>
	                        </div>
	                        <div class="form-group">
	                            <label for="key" class="col-sm-2 control-label">senha:</label>
	                            <div class="col-sm-8">
	                            	<input type="password" name="j_password" id="key" class="form-control" placeholder="senha" required="required" style="width: 100% !important">
	                            </div>
	                        </div>
	                        <div id="div-captcha-erro" class="form-group">
	                        	<div class="col-sm-8">
		                            <div id="captcha-login" class="col-sm-6"></div>
	                            </div>
	                        </div>
	                        
	                        <div class="controls" style="margin-top: 30px;">
	                       		<input type="submit" id="btn-login" class="btn btn-custom btn-lg btn-block" value="Login">
	                       	</div>
	                    </form:form>
	                    
	                    <hr>
	        	    </div>
	    		</div> <!-- /.col-xs-12 -->
	    	</div> <!-- /.row -->
	    </div> <!-- /.container -->
	</section>
	
	<footer>
		<img id="logo-campus" alt="Campus da UFC em Quixadá" src="<c:url value="/resources/images/logo-campus.png" />">
		<p>Universidade Federal do Ceará - Campus Quixadá.</p>
		<p>Todos os direitos reservados.</p>
	</footer>
	
	<script src="https://www.google.com/recaptcha/api.js" async defer></script>
	<script src="<c:url value="/webjars/jquery/2.1.0/jquery.min.js" />"></script>
	<script src="<c:url value="/webjars/bootstrap/3.3.2/js/bootstrap.min.js" />"></script>
	<script src="<c:url value="/resources/js/jquery.validate.min.js" />"></script>
	<script src="<c:url value="/resources/js/login.js" />"></script>	
</body>
</html>