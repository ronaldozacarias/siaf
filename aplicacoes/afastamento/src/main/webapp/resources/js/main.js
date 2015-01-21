$(document).ready(function() {
	
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
	
	console.log('atualizando');
	
	$('.ano').mask('9999', {placeholder:" "});
	$('.conceito').mask('9',{placeholder:" "});
	
	$('.selectpicker').selectpicker();
	
	$("#filtroSemestre").val(sessionStorage.getItem('semestre'));
	$("#filtroAno").val(sessionStorage.getItem('ano'));
	$(".filtroSemestre").selectpicker('refresh');
	
	if(sessionStorage.getItem('ano') && sessionStorage.getItem('semestre')){
		//filtroPeriodo();
	}
	
	$("#dataNascimento").datepicker({
		 autoclose: true,
		 format: "dd/M/yyyy"
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
	showPeriodoPost();
	
	$(".filtroSemestre").change(function(event) {
		filtroPeriodo();
	});	
	
	$("#filtroAno").keyup(function (event) {
	    var maximoDigitosAno = 4;
	    var lengthAno = $(this).val().length;
	    console.log(lengthAno + " - console - " + maximoDigitosAno );
	    if ( (lengthAno <= maximoDigitosAno || event.keyCode == 13) && !isNaN($(this).val()) ) {
	    	filtroPeriodo();
	    }
	});	
	
	
});

function getRanking(ano, semestre) {
	$.ajax({
		type: "POST",
		url: '/afastamento/reserva/ranking.json',
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
		
		loadTable(result.ranking.tuplas, "ranking");
		
	});
}

function loadTable(result, table) {
	$("tbody").remove();
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
	
}

function filtroPeriodo(){
	console.log("filtrofiltrofiltrofiltrofiltrofiltrofiltrofiltro");
	var ano = $("#filtroAno").val();
	var semestre = $("#filtroSemestre").val();

	sessionStorage.setItem("ano", ano);
	sessionStorage.setItem("semestre", semestre);

	console.log(isNaN(ano) + " - " + semestre);
	
	if( (ano.length > 3 && !isNaN(ano)) && (!isNaN(semestre) && (semestre == '1' || semestre == '2')) ){
		console.log("loading...");
		loadPeriodo(ano, semestre);
	}

}

function loadPeriodo(ano, semestre) {
	console.log("loadPeriodoloadPeriodoloadPeriodoloadPeriodoloadPeriodo");
	var filtro = {
		"ano" : ano,
		"semestre" : semestre,
	};
	
	$.ajax({
		url: '/afastamento/administracao/periodo',
		type: "POST",
		dataType: "html",
		data: filtro,
		success: function(result) {
			showPeriodo(result);
			//cosole.log(result);
		},
		error: function(error) {
			console.log('Error loadPeriodo: ' + error);
			$('viewPeriodos').hide();
		}
	});
}

function showPeriodo(result) {
	console.log("showPeriodoshowPeriodoshowPeriodoshowPeriodoshowPeriodoshowPeriodo");
	$("#viewPeriodo").html($(result).find("#update-periodo"));

	if(isNaN($("#viewPeriodo #update-periodo #chave").val())){
		console.log("não é numero");
	}else{
		console.log('é numero');
		
		$("#encerramento").datepicker({
			autoclose: true,
			format: "dd/mm/yyyy"
		});		
		
		$("#status").selectpicker();
	}
	$("#viewPeriodo").show();
}
function showPeriodoPost() {
	if(isNaN(parseInt($("#viewPeriodo #update-periodo #chave").val()))){
		console.log("não é numero = " + $("#viewPeriodo #update-periodo #chave").empty());
		$("#viewPeriodo").hide();
	}else{
		console.log('é d numero = ' + $("#viewPeriodo #update-periodo #chave").length);
		
		$("#encerramento").datepicker({
			autoclose: true,
			format: "dd/mm/yyyy"
		});		
		
		$("#status").selectpicker();
		$("#viewPeriodo").show();
	}
}
