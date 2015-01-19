$(document).ready(function() {
	
	$('.selectpicker').selectpicker();
	
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
	
	getRanking($('#ano').val(), $('#semestre').val());
	
	$('#anterior').click(function(){
		getRanking($('#anoAnterior').val(), $('#semestreAnterior').val());
	});
	
	$('#posterior').click(function(){
		getRanking($('#anoPosterior').val(), $('#semestrePosterior').val());
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
		
		loadBootgrid(result.ranking.tuplas, "ranking");
		
	})
	.error(function(error){
		alert('erro');
	});
}

function loadBootgrid(result, table) {
	$("tbody").remove();
	$('#ranking').append('<tbody>');
	$.each(result, function(i, item) {
        var $tr = $('<tr class="' + item.status + '">').append(
            $('<td>').text(item.professor),
            $('<td>').text(item.semestresAtivos),
            $('<td>').text(item.semestresAfastados),
            $('<td>').text(item.semestresSolicitados),
            $('<td>').text(item.pontuacao),
            $('<td>').text(item.status)
        ).appendTo('tbody');
    });
	
}