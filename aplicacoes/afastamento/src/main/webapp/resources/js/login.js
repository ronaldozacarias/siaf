$(document).ready(function() {
	
	var widget_login;
	var widget_recuperacao;
	window.onload = function() {
		widget_login = grecaptcha.render('captcha-login', {
          'sitekey' : '6Ld8JwETAAAAAJO7YwQhpEjZOJZphzh0PfvinsZ5',
        });
		widget_recuperacao = grecaptcha.render('captcha-recuperacao', {
          'sitekey' : '6Ld8JwETAAAAAJO7YwQhpEjZOJZphzh0PfvinsZ5',
        });
	}
	$('a#esqueceu-senha').click(function(){
		$('#login-form').hide();
		$('h1').text('Recuperar Senha');
		$('#recuperar-senha-form').show();
		grecaptcha.reset(
			widget_login
		);
	});
	
	$('a#retorna-login').click(function(){
		$('#recuperar-senha-form').hide();
		$('h1').text('Login');
		$('#login-form').show();
		grecaptcha.reset(
			widget_recuperacao
		);
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
	
	$('#login-form').submit(function(){
		if(grecaptcha.getResponse() == '') {
			$('.g-recaptcha').after('<span id="captcha-erro" class="help-block">Selecione a opção "Não sou um robô"</span>');
			return false;
		}
	});
	
});