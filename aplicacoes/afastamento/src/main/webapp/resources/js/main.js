$(document).ready(function() {
	
	function loadPeriodo(ano, semestre) {
		var filtro = {
			"ano" : ano,
			"semestre" : semestre,
		};
		
		$.ajax({
			url: '/siaf/administracao/periodo',
			type: "POST",
			dataType: "html",
			data: filtro,
			success: function(result) {
				showPeriodo(result);
			},
			error: function(error) {
				$('viewPeriodos').hide();
			}
		});
	}
	$('.habilitado a').editable({
	    type: 'checklist',
	    url: '/afastamento/administracao/desabilita',
	    source: [
	             {value: 1, text: 'Desabilitar'}
	   		],
   		success: function(response, newValue) {
   			var elem = $("<div>").append(response);
   			$('#wrapper').empty();
   			$('#wrapper').html($(elem).find("#wrapper").html());
   		}	
	});
	
	$('#solicitarAfastamento').validate({
        rules: {
            
        },
        highlight: function(element) {
            $(element).closest('.form-item').addClass('has-error');
        },
        unhighlight: function(element) {
            $(element).closest('.form-item').removeClass('has-error');
        },
        errorElement: 'span',
        errorClass: 'help-block',
        errorPlacement: function(error, element) {
            error.insertAfter(element.parent().children().last());
        },
        messages:{
        	anoInicio:{
                required:"Campo obrigatório",
            },
            anoTermino:{
                required:"Campo obrigatório",
            },
            conceito:{
                required:"Campo obrigatório",
            },
            instituicao:{
                required:"Campo obrigatório",
            }
        }
    });
	
	$('#novo-professor').validate({
        rules: {
        	semestreAdmissao: {
        		required: true
            },
            'usuario.email': {
            	email: true
            }
        },
        
        highlight: function(element) {
            $(element).closest('.form-item').addClass('has-error');
        },
        unhighlight: function(element) {
            $(element).closest('.form-item').removeClass('has-error');
        },
        errorElement: 'span',
        errorClass: 'help-block',
        errorPlacement: function(error, element) {
            	error.insertAfter(element.parent().children().last());
        },
        
        messages:{
        	"usuario.nome":{
                required:"Campo obrigatório",
            },
            siape:{
                required:"Campo obrigatório",
            },
            'usuario.email':{
            	required:"Campo obrigatório",
            },
            anoAdmissao:{
            	required:"Campo obrigatório",
            },
            semestreAdmissao:{
            	required:"Selecione o semestre",
            },
            cpf:{
                required:"Campo obrigatório",
            },
            dataNascimento:{
                required:"Campo obrigatório",
            }
        },
    });

	$('.ano').mask('9999', {placeholder:" "});
	$('#siape').mask('9999999', {placeholder:" "});
	$('.conceito').mask('9',{placeholder:" "});
	
	$('.selectpicker').selectpicker();
	
	$(".filtroSemestre").selectpicker('refresh');
	
	
	$("#dataNascimento").datepicker({
		 autoclose: true,
		 format: "dd/mm/yyyy"
	});
	
	$("#encerramento").datepicker({
		autoclose: true,
		format: "dd/mm/yyyy"
	});		
		
	
	$(".file").fileinput({
		showUpload: false,
		overwriteInitial: false,
		initialCaption: "Selecione...",
		browseLabel: "Buscar",
		browseClass: "btn btn-default",
		removeLabel: "Excluir",
		msgSelected: "{n} arquivos selecionados",
		msgLoading: "Carregando arquivo {index} de {files} &hellip;"
	});
	
	$('#anterior').click(function(){
		getRanking($('#anoAnterior').val(), $('#semestreAnterior').val());
	});
	
	$('#posterior').click(function(){
		getRanking($('#anoPosterior').val(), $('#semestrePosterior').val());
	});
	
	$("#viewPeriodo").hide();
	$("#warning-ranking").hide();
	showPeriodoPost();
	
	$(".filtroSemestre").change(function(event) {
		filtroPeriodo();
	});	
	
	$("#filtroAno").keyup(function (event) {
	    var maximoDigitosAno = 4;
	    var lengthAno = $(this).val().length;
	    if ( (lengthAno <= maximoDigitosAno || event.keyCode == 13) && !isNaN($(this).val()) ) {
	    	filtroPeriodo();
	    }
	});
	

	 $("#encerramento").mask("99/99/9999");

	 $("#cpf").mask("999.999.999-99",{placeholder:" "});

	$('#excluir-reserva').on('show.bs.modal', function(e) {
		$(this).find('.modal-body').text('Tem certeza de que deseja excluir a reserva para o período \"' + $(e.relatedTarget).data('name') + '\"?');
		$(this).find('.btn-danger').attr('href', $(e.relatedTarget).data('href'));
	});	
	

});

function getRanking(ano, semestre) {
	$("tbody").remove();
	$("#warning-ranking").hide();
	$('#img-load').show();
	$.ajax({
		type: "POST",
		url: '/siaf/reserva/ranking.json',
		data: {
        	ano : ano,
        	semestre : semestre
		}
	})
	.success(function(result) {
		
		$('i#anterior').show();
		$('i#posterior').show();
		if(result.periodoAnterior == null) {
			$('i#anterior').hide();
		} else {
			$('#anoAnterior').val(result.periodoAnterior.ano);
			$('#semestreAnterior').val(result.periodoAnterior.semestre);
		}
		
		if(result.periodoPosterior == null) {
			$('i#posterior').hide();
		} else {
			$('#anoPosterior').val(result.periodoPosterior.ano);
			$('#semestrePosterior').val(result.periodoPosterior.semestre);
		}
		
		$('#ano').val(result.periodoAtual.ano);
		$('#semestre').val(result.periodoAtual.semestre);
		
		$('#periodoLabel').text(result.periodoAtual.ano + "." + result.periodoAtual.semestre);
		$('#vagas').text("Vagas: " + result.periodoAtual.vagas);
		
		$('#img-load').hide();
		loadTable(result.ranking.tuplas, "ranking");
		
		$('table#ranking').removeClass('animated zoomIn').addClass('animated zoomIn').one('webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend', function(){
		      $(this).removeClass('animated zoomIn');
	    });
		
	});
}

function loadTable(result, table) {
	$('#ranking').append('<tbody>');
	$.each(result, function(i, item) {
        var $tr = $('<tr class="' + item.status + '">').append(
        	$('<td class=\"align-center\">').text(i+1),
        	$('<td>').text(item.professor),
            $('<td class=\"align-center\">').text(item.semestresAtivos),
            $('<td class=\"align-center\">').text(item.semestresAfastados),
            $('<td class=\"align-center\">').text(item.semestresSolicitados),
            $('<td class=\"align-center\">').text(item.reserva.anoInicio + "." + item.reserva.semestreInicio + " a " + item.reserva.anoTermino + "." + item.reserva.semestreTermino),
            $('<td class=\"pontuacao align-center\">').text(item.pontuacao)
        ).appendTo('tbody');
    });
	if(result.length == 0) {
		$("#warning-ranking").show();
	}
}

function filtroPeriodo(){
	var ano = $("#filtroAno").val();
	var semestre = $("#filtroSemestre").val();

	sessionStorage.setItem("ano", ano);
	sessionStorage.setItem("semestre", semestre);

	if( (ano.length > 3 && !isNaN(ano)) && (!isNaN(semestre) && (semestre == '1' || semestre == '2')) ){
		loadPeriodo(ano, semestre);
	}

}

function loadPeriodo(ano, semestre) {
	var filtro = {
		"ano" : ano,
		"semestre" : semestre,
	};
	
	$.ajax({
		url: '/siaf/administracao/periodo',
		type: "POST",
		dataType: "html",
		data: filtro,
		success: function(result) {
			showPeriodo(result);
		},
		error: function(error) {
			$('viewPeriodos').hide();
		}
	});
}

function showPeriodo(result) {
	$("#viewPeriodo").html($(result).find("#update-periodo"));

	if(isNaN($("#viewPeriodo #update-periodo #chave").val())){

	}else{
		$("#encerramento").datepicker({
			autoclose: true,
			format: "dd/mm/yyyy"
		});		
		
		$("#status").selectpicker();
	}
	$('#periodo-heading').addClass('animated tada');
	$('#periodo-body').addClass('animated fadeInUp');

	$("#form-periodo").validate({
        rules: {
            
        },
        highlight: function(element) {
            $(element).closest('.form-item').addClass('has-error');
        },
        unhighlight: function(element) {
            $(element).closest('.form-item').removeClass('has-error');
        },
        errorElement: 'span',
        errorClass: 'help-block',
        errorPlacement: function(error, element) {
            error.insertAfter(element.parent().children().last());
        },
        messages:{
        	vagas:{
                required:"Campo obrigatório",
            },
            encerramento:{
                required:"Campo obrigatório",
            }
        }
    });	
	
	$("#viewPeriodo").show();
}

function showPeriodoPost() {
	if(isNaN(parseInt($("#viewPeriodo #update-periodo #chave").val()))){
		$("#viewPeriodo").hide();
	}else{
		$("#encerramento").datepicker({
			autoclose: true,
			format: "dd/mm/yyyy"
		});		
		
		$("#status").selectpicker();
		$("#viewPeriodo").show();
	}
}
