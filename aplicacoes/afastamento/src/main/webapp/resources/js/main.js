$(document).ready(function() {

	$('.selectpicker').selectpicker();
	
	$("#filtroSemestre").val(sessionStorage.getItem('semestre'));
	$("#filtroAno").val(sessionStorage.getItem('ano'));
	$(".filtroSemestre").selectpicker('refresh');
	
	
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

function filtroPeriodo(){
	var ano = $("#filtroAno").val();
	var semestre = $("#filtroSemestre").val();

	sessionStorage.setItem("ano", ano);
	sessionStorage.setItem("semestre", semestre);

	if( (ano.length > 3 && !isNaN(ano)) && (!isNaN(semestre) && (semestre == '1' || semestre == '2')) ){
		console.log("loading...");
		loadPeriodo(ano, semestre);
	}

}

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
