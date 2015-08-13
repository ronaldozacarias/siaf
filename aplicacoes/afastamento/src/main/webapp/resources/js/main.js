$(document).ready(function() {
	
	// Geral
//_____________________________________________________________________________________________________
	
	$('.ano').mask('9999', {placeholder:" "});
	$('.conceito').mask('9',{placeholder:" "});
	
	$('.selectpicker').selectpicker();
	
	$(".ano").datepicker({
        format: " yyyy",
        language: 'pt-BR',
		autoclose: true,
        viewMode: "years", 
        minViewMode: "years",
        startDate: 'today'
    });
	
	$(".ano-all").datepicker({
        format: " yyyy",
        language: 'pt-BR',
		autoclose: true,
        viewMode: "years", 
        minViewMode: "years",
    });
		
	
	$(".data").datepicker({
		language: 'pt-BR',
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
	
	
	$("#encerramento").mask("99/99/9999");
	
//_____________________________________________________________________________________________________	
	
	// Página de Gerenciar Professores
	$('#tableProfessores').DataTable({
		"pageLength" : 10,
		"order" : [[ 1, 'asc' ]],
		"columnDefs" : [ 
            {"targets" : 0, "orderable" : false},
            {"targets" : 2, "orderable" : false},
            {"targets" : 4, "orderable" : false},
		],

		"language" : {
			"sEmptyTable" : "Nenhum registro encontrado.",
			"sInfo" : "Mostrando de _START_ até _END_ de _TOTAL_ registros",
			"sInfoEmpty" : "Mostrando 0 até 0 de 0 registros",
			"sInfoFiltered" : "(Filtrados de _MAX_ registros)",
			"sInfoPostFix" : "",
			"sInfoThousands" : ".",
			"sLengthMenu" : "Resultados por página:  _MENU_",
			"sLoadingRecords" : "Carregando...",
			"sProcessing" : "Processando...",
			"sZeroRecords" : "Nenhum registro encontrado.",
			"sSearch" : "",
			"searchPlaceholder": "Pesquisar...",
			"oPaginate" : {
				"sNext" : "Próximo",
				"sPrevious" : "Anterior",
				"sFirst" : "Primeiro",
				"sLast" : "Último"
			},
			"oAria" : {
				"sSortAscending" : ": Ordenar colunas de forma ascendente",
				"sSortDescending" : ": Ordenar colunas de forma descendente"
			}
		}
	});
	
	
//_____________________________________________________________________________________________________	
	
	
	
	
	
	/*function loadPeriodo(ano, semestre) {
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
	}*/
	
	/*var editVagas = false;
	var editEncerramento = false;
	var guardaEncerramento = null;
	var guardaVagas = null;
	
	$('.editPeriodo').on('click', function(event) {
		var id = '';
		if($(this).data('id')){
			id = $(this).data('id');
		}
		$.ajax({
			type: 'POST',
			data: {
				'id': id, 
			},
			url: '/siaf/administracao/edit-periodo.json',
		})
		.success(function(result) {
			editVagas = result.editVagas;
			editEncerramento = result.editEncerramento;
			$('#options'+id).attr('data-encerramento', result.editEncerramento);
			$('#options'+id).attr('data-vagas', result.editVagas);

			if(result.editEncerramento){
			    encerramento = $('#encerramento'+id ).text();
			    guardaEncerramento = encerramento;
			    
				$('#encerramento'+id).empty();
				$('#encerramento'+id).append('<input name="inputEncerramento" class="form-control data" value="'+encerramento+'" />');

				$(".data").datepicker({
					language: 'pt-BR',
					format: "dd/mm/yyyy",	
					autoclose: true,
				});
			}
			
			if(result.editVagas){
				vagas = $('#vagas'+id ).text();
				guardaVagas = vagas;
				
				$('#vagas'+id).empty();
				$('#vagas'+id).append('<input name="inputVagas" class="form-control inputVagas" size="2" value="'+vagas+'"/>');
			}
			
			if(result.editEncerramento || result.editVagas){
			    $('#tablePeriodos').find('#options' + id).removeClass( 'hide' ).addClass('show');
			    $('#editPeriodo'+id).removeClass( 'show' ).addClass('hide');
			}

			if(!result.editEncerramento && !result.editVagas){
				$("#info-periodo .modal-body b").text('Não é possível editar as informações deste período.');
				$("#info-periodo").modal('show');
				
			}
	
		});
		event.stopPropagation();
	});*/

	/*$('.salvarPeriodo').click(function() {
		var id = $(this).data('id');

//		var vagas = $('#vagas'+id).text();
//		var encerramento = $('#encerramento'+id).text();
		
		if($('#options' + id).data('encerramento')){
			encerramento = $('#encerramento'+id+' input').val();
		}
		
		if($('#options' + id).data('vagas')){
			vagas = $('#vagas'+id+' input').val();
		}
		
		$.ajax({
			type: 'POST',
			data: {
				'id': id,
				'vagas' : vagas,
				'encerramento' : encerramento,
			},
			url: '/siaf/administracao/editar-periodo.json',
		}).success(function(result) {
			messagePeriodo(result);
		});
		
		$('#encerramento'+id).empty().text(encerramento);
		$('#vagas'+id).empty().text(vagas);

		
		$('.options').removeClass( "show" ).addClass('hide');
		$('.editPeriodo').removeClass( 'hide' ).addClass('show');
	});
	
	
	$('.cancelPeriodo').click(function(event) {
		var id = $(this).data('id');

		if($('#options' + id).data('encerramento')){
			var encerramento = $('#encerramento'+id+' input').val();
			$('#encerramento'+id).empty();
			$('#encerramento'+id).text(guardaEncerramento);
			guardaEncerramento = null;
		}
		
		if($('#options' + id).data('vagas')){
			var vagas = $('#vagas'+id+' input').val();
			$('#vagas'+id).empty();
			$('#vagas'+id).text(guardaVagas);
			guardaVagas = null;
		}

		$('#options' + id).removeClass( 'show' ).addClass('hide');
		$('#editPeriodo'+id).removeClass( 'hide' ).addClass('show');

		event.stopPropagation();
	});*/
	
	/*var guardaConceito = null;
	$('.editReserva').on('click', function(event) {
		var id = '';
		if($(this).data('id')){
			id = $(this).data('id');
		}
		
		$('#options'+id).attr('data-conceito', true);
  
		conceito = $('#conceito'+id ).text();
		
		guardaConceito = conceito;
		$('#conceito'+id).empty();
		$('#conceito'+id).append('<input name="inputConceito" class="form-control" size="1" value="'+conceito+'" maxlength="1" onKeyUp="validarConceito(this)"/>');

		$('#tableReservas').find('#options' + id).removeClass( 'hide' ).addClass('show');
		$('#editReserva'+id).removeClass( 'show' ).addClass('hide');
		
		event.stopPropagation();
	});*/

	/*$(".filtroSemestre").change(function(event) {
		filtroPeriodo();
	});
	
	$("#filtroAno").keyup(function (event) {
	    var maximoDigitosAno = 4;
	    var lengthAno = $(this).val().length;
	    if ( (lengthAno <= maximoDigitosAno || event.keyCode == 13) && !isNaN($(this).val()) ) {
	    	filtroPeriodo();
	    }
	});*/
	
	
//____________________________________________________________________________________________________________________________________________________	

		
	// Página de inclusão e edição de reserva
	$('#solicitarAfastamento, #formEditarReserva, #formEditarAdmissao').validate({
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
            }
        }
    });
	
	
	
	/*$(".filtroSemestre").selectpicker('refresh');*/
	
	

	
	
//____________________________________________________________________________________________________________________________________________________	
	
	
	// Página do Ranking
	
	$("#anoBuscado").datepicker({
        format: " yyyy", 
           viewMode: "years", 
           minViewMode: "years",
           startDate: $('#ano').val()
    });
	
	$("#warning-buscar-periodo").hide();
	$('#anoBuscado').click(function(){
		$("#warning-buscar-periodo").hide();
	});
	$('#semestreBuscado').click(function(){
		$("#warning-buscar-periodo").hide();
	});
	
	$('#buscar').click(function(){
		$("#warning-buscar-periodo").hide();
		var anoBuscado = parseInt($('#anoBuscado').val());
		var periodoAtualAno = parseInt($('#anoAtual').val());
		var semestreBuscado = parseInt($('#semestreBuscado').val());
		var periodoAtualSemestre = parseInt($('#semestreAtual').val());
		if (anoBuscado > periodoAtualAno) {
			getRanking($('#anoBuscado').val(), $('#semestreBuscado').val());			
		} else if (anoBuscado == periodoAtualAno) {
			if (semestreBuscado >= periodoAtualSemestre) {
				getRanking($('#anoBuscado').val(), $('#semestreBuscado').val());
				
			} else {
				$("#warning-buscar-periodo").show();
			}
		} else {
			$("#warning-buscar-periodo").show();
		}
	});
	
	$('#anterior').click(function(){
		getRanking($('#anoAnterior').val(), $('#semestreAnterior').val());
	});
	
	$('#posterior').click(function(){
		getRanking($('#anoPosterior').val(), $('#semestrePosterior').val());
	});
	
	$("#ranking-full").hide();
	
//____________________________________________________________________________________________________________________________________________________	

	// Página minhas reservas
	
	/*var guardaConceito = null;
	$('.editReserva').on('click', function(event) {
			var id = '';
			if($(this).data('id')){
				id = $(this).data('id');
			}
			var conceito = $('#con'+id ).text();
			
			$('#options'+id).attr('data-conceito', true);
			
			guardaConceito = conceito;
			
			$('#con'+id).empty();
			$('#con'+id).append('<input name="inputConceito" class="form-control" size="1" value="'+conceito+'" maxlength="1" onKeyUp="validarConceito(this)"/>');

			$('#tableReservas').find('#options' + id).removeClass( 'hide' ).addClass('show');
			$('#editReserva'+id).removeClass( 'show' ).addClass('hide');
			
			event.stopPropagation();
		});*/

		/*$(".filtroSemestre").change(function(event) {
			filtroPeriodo();
		});	
		
		$("#filtroAno").keyup(function (event) {
		    var maximoDigitosAno = 4;
		    var lengthAno = $(this).val().length;
		    if ( (lengthAno <= maximoDigitosAno || event.keyCode == 13) && !isNaN($(this).val()) ) {
		    	filtroPeriodo();
		    }
		});*/
	
	$('#excluir-reserva').on('show.bs.modal', function(e) {
		$(this).find('.modal-body').text('Tem certeza de que deseja excluir a reserva para o período \"' + $(e.relatedTarget).data('name') + '\"?');
		$(this).find('.btn-danger').attr('href', $(e.relatedTarget).data('href'));
	});
	
	$('#anoInicioReserva').datepicker({
        format: " yyyy", 
   	    viewMode: "years", 
   	    minViewMode: "years"
	});
	

	$('#anoTerminoReserva').datepicker({
        format: " yyyy", 
   	    viewMode: "years", 
   	    minViewMode: "years"
	});

//_____________________________________________________________________________________________________
	
	/*$('.salvarReserva').click(function() {
		var id = $(this).data('id');
		var conceito = $('#con'+id).text();
		if($('#options' + id).data('conceito')){
			conceito = $('#con'+id+' input').val();
		}
		$.ajax({
			type: 'POST',
			data: {
				'id': id,
				'conceito' : conceito,
			},
			url: '/siaf/administracao/atualizarConceito.json',
		}).success(function(result) {
			messageReservaEmAberto(result);
		});
		
		$('#con'+id).empty().text(conceito);
		
		$('.options').removeClass( "show" ).addClass('hide');
		$('.editReserva').removeClass( 'hide' ).addClass('show');
	});
	
	$('.cancelReserva').click(function() {
		var id = $(this).data('id');
		
		var conceito = $('#con'+id+' input').val();
		$('#con'+id).empty();
		$('#con'+id).text(guardaConceito);
		guardaConceito = null;
		$('#options' + id).removeClass( 'show' ).addClass('hide');
		$('#editReserva'+id).removeClass( 'hide' ).addClass('show');

		event.stopPropagation();
	});*/

	// Página gerenciar reservas
	
	var table = $('#tableReservas').DataTable({
				"pageLength" : 10,
				"order" : [[ 0, 'asc' ], [ 2, 'asc' ], [ 1, 'asc' ]],
				"columnDefs" : [ 
	                {"targets" : 5, "orderable" : false},
				],

				"language" : {
					"sEmptyTable" : "Nenhum registro encontrado.",
					"sInfo" : "Mostrando de _START_ até _END_ de _TOTAL_ registros",
					"sInfoEmpty" : "Mostrando 0 até 0 de 0 registros",
					"sInfoFiltered" : "(Filtrados de _MAX_ registros)",
					"sInfoPostFix" : "",
					"sInfoThousands" : ".",
					"sLengthMenu" : "Resultados por página:  _MENU_",
					"sLoadingRecords" : "Carregando...",
					"sProcessing" : "Processando...",
					"sZeroRecords" : "Nenhum registro encontrado.",
					"sSearch" : "",
					"searchPlaceholder": "Pesquisar...",
					"oPaginate" : {
						"sNext" : "Próximo",
						"sPrevious" : "Anterior",
						"sFirst" : "Primeiro",
						"sLast" : "Último"
					},
					"oAria" : {
						"sSortAscending" : ": Ordenar colunas de forma ascendente",
						"sSortDescending" : ": Ordenar colunas de forma descendente"
					}
				}
			});
	
	$("#tableReservas tfoot th").each( function ( i ) {
		if(i == 0 || i == 3 || i == 4) {
			var select = $('<select class="select-search '+i+'" title="TODOS"><option value="">TODOS</option></select>')
	        .appendTo( $(this).empty() )
	        .on( 'change', function () {
	            var val = $(this).val();
	
	            table.column( i ) //Only the first column
	                .search( val ? '^'+$(this).val()+'$' : val, true, false )
	                .draw();
	        });
	
		    table.column( i ).data().unique().sort().each( function ( d, j ) {
		    	if(i == 0) {
		    		var t = $.parseHTML( d );
		    		d = $(t).html();
		    	}
		        select.append( '<option value="'+d+'">'+d+'</option>' );
		    });
		}
    });
	
	$('#admin-excluir-reserva').on('show.bs.modal', function(e) {
		$(this).find('.modal-body').text('Tem certeza de que deseja excluir a reserva de ' + $(e.relatedTarget).data('professor') + ' para o período \"' + $(e.relatedTarget).data('name') + '\"?');
		$(this).find('.btn-danger').attr('href', $(e.relatedTarget).data('href'));
	});
	
//_____________________________________________________________________________________________________
	
	// Página gerenciar períodos

	$('#tablePeriodos').DataTable({
		 "pageLength": 10,
		 "order": [[ 1, 'asc' ], [ 2, 'asc' ]],
		 "columnDefs": [
		               { "orderable": false, "targets": 0 },
		               { "orderData": [ 1, 2 ],    "targets": 1 },
		               { "orderable": false, "targets": 2 },
		               { "orderable": false, "targets": 3 },
		               { "orderable": false, "targets": 4 },
		               { "orderable": false, "targets": 5 },
		],
		
		"language": {
		    "sEmptyTable": "Nenhum registro encontrado.",
		    "sInfo": "Mostrando de _START_ até _END_ de _TOTAL_ registros",
		    "sInfoEmpty": "Mostrando 0 até 0 de 0 registros",
		    "sInfoFiltered": "(Filtrados de _MAX_ registros)",
		    "sInfoPostFix": "",
		    "sInfoThousands": ".",
		    "sLengthMenu": "Resultados por página _MENU_",
		    "sLoadingRecords": "Carregando...",
		    "sProcessing": "Processando...",
		    "sZeroRecords": "Nenhum registro encontrado.",
		    "sSearch": "",
		    "searchPlaceholder": "Pesquisar...",
		    "oPaginate": {
		        "sNext": "Próximo",
		        "sPrevious": "Anterior",
		        "sFirst": "Primeiro",
		        "sLast": "Último"
		    },
		    "oAria": {
		        "sSortAscending": ": Ordenar colunas de forma ascendente",
		        "sSortDescending": ": Ordenar colunas de forma descendente"
		    }
		}
	});
	
	$('.salvar-periodo').click(function(){
		var id = $(this).data("id");
		$('#periodoId').val(id);
		$('#encerramento').val($('#encerramento-' + id).val());
		$('#vagas').val($('#vagas-' + id).val());
		
		$('#atualizarPeriodo').submit();
	});

//_____________________________________________________________________________________________________
	
	
	// Funções
	
	$('input').addClass('form-control');
	$('select').selectpicker();
	
	
	
		
});

function getRanking(ano, semestre) {
	$("tbody").remove();
	$("#ranking-full").hide();
	$('#load-siaf').show();
	$.ajax({
		type: "GET",
		url: '/siaf/reserva/ranking.json',
		data: {
        	ano : ano.trim(),
        	semestre : semestre.trim()
		}
	})
	.success(function(result) {
		$('#count-ranking').text('Estimativa de vagas restantes: ' + (result.periodoAtual.vagas - result.afastados.length));
		loadAfastados(result.afastados);
			
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
		
		$('#anoBuscado').val(result.periodoAtual.ano);
		$('#semestreBuscado').val(result.periodoAtual.semestre);
		$('#semestreBuscado').selectpicker('refresh');
		
		$('#periodoLabel').text(result.periodoAtual.ano + "." + result.periodoAtual.semestre);
		$('#vagas').text(result.periodoAtual.vagas);
		if(result.periodoAtual.encerramento != null) {
			$('#encerramento').text(moment(result.periodoAtual.encerramento, 'YYYY-MM-DD').format('DD/MM/YYYY'));
		} else {
			$('#encerramento').text('-');
		}
		$('#help-encerramento').attr('data-content', 'Data limite para solicitação de afastamento com início para o período ' + (result.periodoAtual.ano + 1) + '.' + result.periodoAtual.semestre);
		
		$('#load-siaf').hide();
		loadRanking(result.ranking.tuplas);
		
		$('table#ranking, table#afastados').removeClass('animated zoomIn').addClass('animated zoomIn').one('webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend', function(){
		      $(this).removeClass('animated zoomIn');
	    });
		$("#ranking-full").show();
		
	});
}

function loadRanking(result) {
	$('#ranking tbody').remove();
	$('#ranking').append('<tbody>');
	$.each(result, function(i, item) {
        var $tr = $('<tr class="' + item.status + '">').append(
        	$('<td class=\"align-center\">').text(i+1),
        	$('<td>').text(item.professor),
            $('<td class=\"align-center\">').text(item.t),
            $('<td class=\"align-center\">').text(item.a),
            $('<td class=\"align-center\">').text(item.s),
            $('<td class=\"align-center\">').text(item.p),
            $('<td class=\"align-center\">').text(item.ss),
            $('<td class=\"align-center\">').text(item.reserva.anoInicio + "." + item.reserva.semestreInicio + " a " + item.reserva.anoTermino + "." + item.reserva.semestreTermino),
            $('<td class=\"align-center\">').text(getPrograma(item.reserva.programa) + " / " + getConceito(item.reserva.conceitoPrograma)),
            $('<td class=\"pontuacao align-center\">').text(item.pontuacao.toFixed(2))
        ).appendTo('#ranking tbody');
    });
	
	if(result.length == 0) {
		$("#warning-ranking").show();
		$("#ranking").hide();
	} else {
		$("#warning-ranking").hide();
		$("#ranking").show();
	}
	
}

function loadAfastados(afastados) {
	$('#count-afastados').text('Quantidade de professores afastados: ' + afastados.length);
	$("#afastados tbody").remove();
	$('#afastados').append('<tbody>');
	$.each(afastados, function(i, tupla) {
        var $tr = $('<tr>').append(
        	$('<td>').text(tupla.professor),
            $('<td class=\"align-center\">').text(tupla.reserva.anoInicio + "." + tupla.reserva.semestreInicio + " a " + tupla.reserva.anoTermino + "." + tupla.reserva.semestreTermino),
            $('<td class=\"align-center\">').text(getPrograma(tupla.reserva.programa))
        ).appendTo('#afastados tbody');
    });
	if(afastados.length == 0) {
		$("#warning-afastados").show();
		$("#afastados").hide();
	} else {
		$("#warning-afastados").hide();
		$("#afastados").show();
	}
}

function getPrograma(programa) {
	if(programa == 'POS_DOUTORADO') {
		return "PÓS DOUTORADO";
	}
	return programa;
}

function getConceito(conceito) {
	if(conceito == 0) {
		return "-";
	}
	return conceito;
}

/*function filtroPeriodo(){
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
}*/

/*function showPeriodo(result) {
	$("#viewPeriodo").html($(result).find("#update-periodo"));

	if(isNaN($("#viewPeriodo #update-periodo #chave").val())){

	}else{
		$(".data").datepicker({
			language: 'pt-BR',
			autoclose: true,
			format: "dd/mm/yyyy"
		});		
		
		$("#status").selectpicker();
	}
	$('#periodo-heading').addClass('animated tada');
	$('#periodo-body').addClass('animated zoomIn');	
	
	$("#viewPeriodo").show();
}

function showPeriodoPost() {
	if(isNaN(parseInt($("#viewPeriodo #update-periodo #chave").val()))){
		$("#viewPeriodo").hide();
	}else{
		$(".data").datepicker({
			autoclose: true,
			format: "dd/mm/yyyy"
		});		
		
		$("#status").selectpicker();
		$("#viewPeriodo").show();
	}
}*/


/*function getPeriodos() {
	$.ajax({
		type: "GET",
		url: '/siaf/administracao/periodos.json',
	})
	.success(function(result) {
		loadPeriodos(result);
		$("#periodos tbody td").attr("class", "align-center")
	});
}*/

/*function loadPeriodos(result) {
	$("#periodos")
		.bootgrid({
			labels: {
	            all: "Todos",
	            infos: "Mostrando {{ctx.start}} - {{ctx.end}} de {{ctx.total}}",
	            loading: "Carregando...",
	            noResults: "Nenhum resultado encontrado!",
	            refresh: "Atualizar",
	            search: "Buscar"
	        },
	        columnSelection: false,
	        caseSensitive: false,
	        formatters: {
	        	"status": function(column, row) {
	        		if(row.status == 'ABERTO'){
	        			return "<span class=\"label label-success\">" + row.status + "</span>";
	        		}else{
	        			return "<span class=\"label label-danger\">" + row.status + "</span>";
	        		}
	        	},
	        }	        
		})
		.bootgrid("clear")
		.bootgrid("append", result);

}*/

/*function messagePeriodo(result) {

	if(result.erro && result.erro.length > 0){
		$(".messages #erroDiv").append("<div id=\"info\" " +
				"class=\"alert-danger margin-top hide\" " +
				"role=\"alert\"> " +
				"<button type=\"button\" class=\"close\" data-dismiss=\"alert\">" +
				"<span aria-hidden=\"true\">&times;</span>" +
				"<span class=\"sr-only\">Close</span>" +
				"</button><p></p></div>");
		$(".messages #erro p").text(result.erro);
		$(".messages #erro").removeClass( "hide" ).addClass('show');
	} 
	
	if(result.info &&  result.info.length > 0){
		$('.messages #infoDiv').empty();
		$(".messages #infoDiv").append("<div id=\"info\" " +
				"class=\"alert alert-info hide\" " +
				"role=\"alert\"> " +
				"<button type=\"button\" class=\"close\" data-dismiss=\"alert\">" +
				"<span aria-hidden=\"true\">&times;</span>" +
				"<span class=\"sr-only\">Close</span>" +
				"</button><p></p></div>");		
		$(".messages #infoDiv #info p").text(result.info);
		$(".messages #infoDiv #info").removeClass( "hide" ).addClass('show');
		
	} 
}*/

/*function messageReservaEmAberto(result) {

	if(result.erro && result.erro.length > 0){
		$(".messages #erroDiv").append("<div id=\"info\" " +
				"class=\"alert alert-danger margin-top hide\" " +
				"role=\"alert\"> " +
				"<button type=\"button\" class=\"close\" data-dismiss=\"alert\">" +
				"<span aria-hidden=\"true\">&times;</span>" +
				"<span class=\"sr-only\">Close</span>" +
				"</button><p></p></div>");
		$(".messages #erroDiv #erro p").text(result.erro);
	} 
	
	if(result.info &&  result.info.length > 0){
		$('.messages #infoDiv').empty();
		$(".messages #infoDiv").append("<div id=\"info\" " +
				"class=\"alert alert-info margin-top hide\" " +
				"role=\"alert\"> " +
				"<button type=\"button\" class=\"close\" data-dismiss=\"alert\">" +
				"<span aria-hidden=\"true\">&times;</span>" +
				"<span class=\"sr-only\">Close</span>" +
				"</button><p></p></div>");
		$(".messages #infoDiv #info p").text(result.info);
		$(".messages #infoDiv #info").removeClass( "hide" ).addClass('show');
	} 
}*/

/*function validarConceito(campo){
	var numero="34567"; //variáveis aceitas na função.
	var campo_temporario; //váriavel responsável pela verificação dos dados.
	for (var i=0;i<campo.value.length;i++){ 
		// A variável campo_temporario verifica se existe algum valor dentro do form.
		campo_temporario=campo.value.substring(i,i+1)
		// Caso esse campo seja diferente da variavel numero, ele irá limpar o form.
		if (numero.indexOf(campo_temporario)==-1){ 
			// Caso contrário, ele irá deixar inserir o número no form.	
			campo.value = campo.value.substring(0,i);
		}
	}
}*/

/*function somenteNumeros(campo){
	var numero="0123456789"; //variáveis aceitas na função.
	var campo_temporario; //váriavel responsável pela verificação dos dados.
	for (var i=0;i<campo.value.length;i++){ 
		// A variável campo_temporario verifica se existe algum valor dentro do form.
		campo_temporario=campo.value.substring(i,i+1)
		// Caso esse campo seja diferente da variavel numero, ele irá limpar o form.
		if (numero.indexOf(campo_temporario)==-1){ 
			// Caso contrário, ele irá deixar inserir o número no form.	
			campo.value = campo.value.substring(0,i);
		}
	}
}*/

// Teste

function relatorioReservas() {
	var filtro = {
			"anoInicio" : 2015,
			"semestreInicio" : 1,
			"anoTermino" : 2017,
			"semestreTermino" : 2
		};
		
		$.ajax({
			url: '/siaf/administracao/relatorio/reservas-by-periodo.json',
			type: "POST",
			dataType: "html",
			data: filtro,
			success: function(result) {
				var obj = $.parseJSON(result);
				var cat = [];
				var data = [];
				for (var key in obj.relatorio) {
					cat.push(key);
					data.push(obj.relatorio[key])
		        }
				$('#chart').highcharts({
			        title: {
			            text: 'Solicitação de Reservas'
			        },
			        xAxis: {
			        	title: {
			        		text: 'Períodos'
			        	},
			            categories: cat
			        },
			        yAxis: {
			            title: {
			                text: 'Total de Solicitações'
			            }
			        },
			        series: [{
			            name: 'Reservas',
			            data: data
			        }]
			    });
			},
			error: function(error) {
			}
		});
}

