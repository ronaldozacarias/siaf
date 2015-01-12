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
	
	$("#viewPeriodo").hide();
	
	$(".filtroSemestre").change(function(event) {
		var ano = $("#filtroAno").val();
		var semestre = $("#filtroSemestre").val();
		console.log(isNaN(ano) + " - " + semestre);
		
		if( (ano.length > 3 && !isNaN(ano)) && (!isNaN(semestre) && (semestre == '1' || semestre == '2')) ){
			console.log("loading...");
			loadPeriodo(ano, semestre);
		}
		
	});
	
});


function loadPeriodo(ano, semestre) {
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
		},
		error: function(error) {
			console.log('Error loadPeriodo: ' + error);
			$('viewPeriodos').hide();
		}
	});
}

function showPeriodo(result) {
	$("#viewPeriodo").html($(result).find("#update-periodo"));

	if(isNaN($("#viewPeriodo #update-periodo #chave").val())){
		console.log("não é numero");
	}else{
		console.log('é numero');
		$("#viewPeriodo").show();
	}
}
