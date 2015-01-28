$(document).ready(function() {
	
	$('a#esqueceu-senha').click(function(){
		$('#login-form').hide();
		$('h1').text('Recuperar Senha');
		$('#recuperar-senha-form').show();
	});
	
	$('a#retorna-login').click(function(){
		$('#recuperar-senha-form').hide();
		$('h1').text('Login');
		$('#login-form').show();
	});
	
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
	
	$('#recuperar-senha-form').validate({
        rules: {
        	email: {
            	email: true
            }
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
        	email:{
                required:"Campo obrigatório",
                email: "Digite um email válido"
            }
        }
    });
	
	/*$('#recuperar-senha-form').submit(function(e) {
		alert($('#email').val());
		$('#email').val('');
		$('#btn-recuperar').click(function(){
			var email = $('#email').val();
			$.ajax({
				type: "POST",
				url: '/siaf/configuracao/recuperar-senha',
				data: {
		        	email : email
				}
			})
			.success(function(result) {
				$('#recuperar-senha').modal('hide');
				if(result.resultado == 'erro') {
					$('img').insertAfter('<div class="alert alert-danger alert-dismissible" role="alert">' +
						'<button type="button" class="close" data-dismiss="alert">' +
						'<span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>' + 
						result.info + '</div>');
				} else {
					$('img').insertAfter('<div class="alert alert-success alert-dismissible" role="alert">' +
						'<button type="button" class="close" data-dismiss="alert">' +
						'<span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>' + 
						result.info + '</div>');
				}
			});
		});
	});*/
	
});