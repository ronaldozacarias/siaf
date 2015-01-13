$(document).ready(function() {
	
	console.log('atualizando');
	
	$('.selectpicker').selectpicker();
	
	$("#filtroSemestre").val(sessionStorage.getItem('semestre'));
	$("#filtroAno").val(sessionStorage.getItem('ano'));
	$(".filtroSemestre").selectpicker('refresh');
	
	if(sessionStorage.getItem('ano') && sessionStorage.getItem('semestre')){
		filtroPeriodo();
	}
	
	$("#dataNascimento").datepicker({
		 autoclose: true,
	});
	
	$("#encerramento").datepicker({
		autoclose: true,
	}).on("changeDate", function(e){
		console.log("Selected date: " + this.value);
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
	
	$(".filtroSemestre").change(function(event) {
		filtroPeriodo();
	});	
	
});

function filtroPeriodo(){
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

function submitPeriodo(periodo) {
	$.ajax({
		url: '/afastamento/administracao/update-periodo2',
		type: "POST",
		dataType: "html",
		data: periodo,
		success: function(result) {
			console.log('Update');
		},
		error: function(error) {
			console.log('ErrorPeriodo: ' + error);
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
		}).on("changeDate", function(e){
			$("#encerramentoHidden").val(this.value);
			console.log("Selected date: " + this.value);
	    });

		$("#updatePeriodo").on('click', function(event) {
			var id = $("#chave").val();
			var ano = $("#anoHidden").val();
			var semestre = $("#semestre").val();
			var encerramento = $("#encerramentoHidden").val();
			var status = $("#status").val();
			var vagas = $("#vagas").val();
			
			var periodo = {
				"id" : id,
				"ano" : ano,
				"semestre" : semestre,
				"encerramento" : encerramento,
				"status" : status,
				"vagas" : vagas,
			};
			
			console.log("Log encerramento = " + periodo.encerramento);
			submitPeriodo(periodo);
			
		});
		
		
		
		$("#status").selectpicker();
		$("#viewPeriodo").show();
	}
}
