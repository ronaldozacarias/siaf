<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
		                <h1>Login:</h1>
		                <label class="erro">${error }</label>
	                    <form role="form" action="<c:url value='j_spring_security_check' />" method="post" id="login-form" autocomplete="off" class="form-horizontal">
	                        <div class="form-group">
	                            <label for="siape" class="col-sm-2 control-label">Siape:</label>
	                            <div class="col-sm-8">
	                            	<input type="text" name="j_username" id="siape" class="form-control" placeholder="siape" required="required" style="width: 100% !important">
	                            </div>
	                        </div>
	                        <div class="form-group">
	                            <label for="key" class="col-sm-2 control-label">Senha:</label>
	                            <div class="col-sm-8">
	                            	<input type="password" name="j_password" id="key" class="form-control" placeholder="senha" required="required" style="width: 100% !important">
	                            </div>
	                        </div>
	                        
	                        <div class="controls" style="margin-top: 30px;">
	                       		<input type="submit" id="btn-login" class="btn btn-custom btn-lg btn-block" value="Login">
	                       	</div>
	                    </form>
	                    <a href="javascript:;" class="forget" data-toggle="modal" data-target="#recuperar-senha">Esqueceu a senha?</a>
	                    <hr>
	        	    </div>
	    		</div> <!-- /.col-xs-12 -->
	    	</div> <!-- /.row -->
	    </div> <!-- /.container -->
	</section>
	
	<!-- Modal Recuperar Senha -->
	<!-- <div class="modal fade" id="recuperar-senha" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        			<h4 class="modal-title">Recuperar senha</h4>
				</div>
				<div class="modal-body">
					<p>Insira seu email:</p>
					<input type="text" name="email" id="recovery-siape" class="form-control" autocomplete="off">
				</div>
				<div class="modal-footer">
					<a href="#" class="btn btn-primary">Recuperar</a>
					<button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
				</div>
			</div>
		</div>
	</div> -->
	
	<!-- Modal Recuperar Senha -->
	<div id="recuperar-senha" class="modal fade forget-modal" tabindex="-1" role="dialog" aria-labelledby="myForgetModalLabel" aria-hidden="true">
		<div class="modal-dialog modal-sm">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
						<span class="sr-only">Close</span>
					</button>
					<h4 class="modal-title">Recuperar senha</h4>
				</div>
				<div class="modal-body">
					<p>Insira seu email:</p>
					<input type="text" name="email" id="email" class="form-control" autocomplete="off">
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
					<button id="btn-recuperar" class="btn btn-primary">Recuperar</button>
				</div>
			</div> <!-- / .modal-content -->
		</div> <!-- /.modal-dialog -->
	</div> <!-- /.modal -->
	
	<!-- Modal Sucesso Recuperação de Senha -->
	<div id="sucesso-senha" class="modal fade forget-modal" tabindex="-1" role="dialog" aria-labelledby="myForgetModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-body">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
						<span class="sr-only">Close</span>
					</button>
					<p>Os passos para recuperação de senha foram enviados para o email informado.</p>
				</div>
			</div> <!-- / .modal-content -->
		</div> <!-- /.modal-dialog -->
	</div> <!-- /.modal -->
	
	<!-- Modal Erro Recuperação de Senha -->
	<div id="erro-senha" class="modal fade forget-modal" tabindex="-1" role="dialog" aria-labelledby="myForgetModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-body">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
						<span class="sr-only">Close</span>
					</button>
					<p>O email informado não foi encontrado.</p>
				</div>
			</div> <!-- / .modal-content -->
		</div> <!-- /.modal-dialog -->
	</div> <!-- /.modal -->
	
	<script src="<c:url value="/webjars/jquery/2.1.0/jquery.min.js" />"></script>
	<script src="<c:url value="/webjars/bootstrap/3.3.2/js/bootstrap.min.js" />"></script>
	<script src="<c:url value="/resources/js/jquery.validate.min.js" />"></script>
	<script type="text/javascript">
		$('#login-form').validate({
			rules: {
	            
	        },
	        highlight: function(element) {
	            $(element).closest('.form-group').addClass('has-error');
	        },
	        unhighlight: function(element) {
	            $(element).closest('.form-group').removeClass('has-error');
	        },
	        errorElement: 'span',
	        errorClass: 'help-block',
	        errorPlacement: function(error, element) {
	            error.insertAfter(element.parent().children().last());
	        },
	        messages:{
	        	j_username:{
	                required:"Campo obrigatório",
	            },
	            j_password:{
	                required:"Campo obrigatório",
	            }
	        }
	    });
		
		$('#recuperar-senha').on('show.bs.modal', function(e) {
			$('#email').val('');
			$('#btn-recuperar').click(function(){
				var email = $('#email').val();
				$.ajax({
					type: "POST",
					url: '/siaf/recuperar-senha',
					data: {
			        	email : email
					}
				})
				.success(function(result) {
					$('#recuperar-senha').modal('hide');
					if(result.resultado == 'erro') {
						$('#erro-senha').modal('show');
					} else {
						$('#sucesso-senha').modal('show');
					}
				});
			});
		});
		
		
	</script>
</body>
</html>