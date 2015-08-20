$(document).ready(function() {
	
	// Geral
	
	$('.ano').mask('9999', {placeholder:" "});
	$('.conceito').mask('9',{placeholder:" "});
	
	$('[data-toggle="popover"]').popover({ trigger: "hover" });
	
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
	
	
	// Página de homologação de reserva
	$('.homologar').click(function(){
		var idReserva = $(this).data('id');
		var status = $('#homologar-reserva-' + idReserva).find('#status-' + idReserva).val();
		if (status.contains('CANCELADO') || status.contains('NEGADO')) {
			$('#cancelar-reserva').modal('show');
			$('#cancelar-reserva').find('#status').val(status);
			$('#cancelar-reserva').find('#idReserva').val(idReserva);
		} else {
			$('#homologar-reserva-' + $(this).data('id')).submit();
		}
	});
	/*$('.form-homologar').submit(function(){
		alert($(this).find('#idReserva').toSource());
		return false;
	});*/
	
	
//_____________________________________________________________________________________________________	
	
		
	// Página de inclusão e edição de reserva
	
	$('#solicitarAfastamento, #formEditarReserva, #formEditarAdmissao, #formCancelarReserva').validate({
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
            motivo:{
                required:"Campo obrigatório",
            }
        }
    });
	
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
			getRanking($('#anoBuscado').val(), $('#semestreBuscado').val(), false);			
		} else if (anoBuscado == periodoAtualAno) {
			if (semestreBuscado >= periodoAtualSemestre) {
				getRanking($('#anoBuscado').val(), $('#semestreBuscado').val(), false);
				
			} else {
				$("#warning-buscar-periodo").show();
			}
		} else {
			$("#warning-buscar-periodo").show();
		}
	});
	
	$('#anterior').click(function(){
		getRanking($('#anoAnterior').val(), $('#semestreAnterior').val(), false);
	});
	
	$('#posterior').click(function(){
		getRanking($('#anoPosterior').val(), $('#semestrePosterior').val(), false);
	});
	
	$("#ranking-full").hide();
	
	// Simulador
	
	$('#buscar-simulador').click(function(){
		$("#warning-buscar-periodo").hide();
		var anoBuscado = parseInt($('#anoBuscado').val());
		var periodoAtualAno = parseInt($('#anoAtual').val());
		var semestreBuscado = parseInt($('#semestreBuscado').val());
		var periodoAtualSemestre = parseInt($('#semestreAtual').val());
		if (anoBuscado > periodoAtualAno) {
			getRanking($('#anoBuscado').val(), $('#semestreBuscado').val(), true);			
		} else if (anoBuscado == periodoAtualAno) {
			if (semestreBuscado >= periodoAtualSemestre) {
				getRanking($('#anoBuscado').val(), $('#semestreBuscado').val(), true);
				
			} else {
				$("#warning-buscar-periodo").show();
			}
		} else {
			$("#warning-buscar-periodo").show();
		}
	});
	
	$('#anterior-simulador').click(function(){
		getRanking($('#anoAnterior').val(), $('#semestreAnterior').val(), true);
	});
	
	$('#posterior-simulador').click(function(){
		getRanking($('#anoPosterior').val(), $('#semestrePosterior').val(), true);
	});
	
//____________________________________________________________________________________________________________________________________________________	

	// Página minhas reservas
	
	$('#excluir-reserva').on('show.bs.modal', function(e) {
		$(this).find('.modal-body').text('Tem certeza de que deseja excluir a reserva para o período \"' + $(e.relatedTarget).data('name') + '\"?');
		$(this).find('.btn-danger').attr('href', $(e.relatedTarget).data('href'));
	});
	
	$('#cancelar-reserva').on('show.bs.modal', function(e) {
		$(this).find('#reservaId').val($(e.relatedTarget).data('id'));
	});
	
	$('#detalhes-reserva').on('hide.bs.modal', function(e) {
		$('#detalhes-reserva').find('#motivo').hide();
		$('#detalhes-reserva').find('.data-cancelamento').hide();
		$('#detalhes-reserva').find('#detalhes').css('display', 'none');
		$('#detalhes-reserva').find('#load-siaf').css('display', 'block');
		$('#detalhes-reserva').find('#detalhe-professor').text('');
		$('#detalhes-reserva').find('#detalhe-periodo').text('');
		$('#detalhes-reserva').find('#detalhe-status').text('');
		$('#detalhes-reserva').find('#detalhe-data-cancelamento').text('');
		$('#detalhes-reserva').find('#detalhe-motivo').text('');
		$('#detalhes-reserva').find('#detalhe-solicitacao').text('');
		$('#detalhes-reserva').find('#detalhe-programa').text('');
		$('#detalhes-reserva').find('#detalhe-conceito').text('');
		$('#detalhes-reserva').find('#detalhe-instituicao').text('');
	});
	
	$('#detalhes-reserva').on('shown.bs.modal', function(e) {
		$('#detalhes-reserva').find('#motivo').hide();
		$('#detalhes-reserva').find('.data-cancelamento').hide();
		$.ajax({
			type: "GET",
			url: '/siaf/reserva/detalhes.json',
			data: {
	        	id : $(e.relatedTarget).data('id')
			}
		})
		.success(function(result) {
			if (result.status == 'sucesso') {
				if (result.reserva.dataCancelamento != null) {
					$('#detalhes-reserva').find('#motivo').show();
					$('#detalhes-reserva').find('.data-cancelamento').show();
				}
				$('#detalhes-reserva').find('#load-siaf').css('display', 'none');
				$('#detalhes-reserva').find('#detalhe-professor').text(result.professor);
				$('#detalhes-reserva').find('#detalhe-periodo').text(
						result.reserva.anoInicio + '.' + result.reserva.semestreInicio + ' a ' 
						+ result.reserva.anoTermino + '.' + result.reserva.semestreTermino);
				$('#detalhes-reserva').find('#detalhe-status').text(getStatus(result.reserva.status));
				$('#detalhes-reserva').find('#detalhe-data-cancelamento').text(moment(result.reserva.dataCancelamento).format('DD/MM/YYYY'));
				$('#detalhes-reserva').find('#detalhe-motivo').text(result.reserva.motivoCancelamento);
				$('#detalhes-reserva').find('#detalhe-solicitacao').text(moment(result.reserva.dataSolicitacao).format('DD/MM/YYYY'));
				$('#detalhes-reserva').find('#detalhe-programa').text(getPrograma(result.reserva.programa));
				$('#detalhes-reserva').find('#detalhe-conceito').text(result.reserva.conceitoPrograma);
				if (result.reserva.instituicao != null) {
					$('#detalhes-reserva').find('#detalhe-instituicao').text(result.reserva.instituicao);
				} else {
					$('#detalhes-reserva').find('#detalhe-instituicao').text('-');
				}
				$('#detalhes-reserva').find('#detalhes').css('display', 'block');
			} else {
				$('#detalhes-reserva').find('#detalhe-erro').css('display', 'block');
			}
		})
		.error(function(error) {
			$('#detalhes-reserva').find('#load-siaf').css('display', 'none');
			$('#detalhes-reserva').find('#detalhe-erro').css('display', 'block');
		});
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
	
	// Página Gerenciar Reservas
	
	var table = $('#tableReservas').DataTable({
				"pageLength" : 10,
				"order" : [[ 0, 'asc' ], [ 2, 'asc' ]],
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

	$('input').addClass('form-control');
	$('select').selectpicker();
		
});

//_____________________________________________________________________________________________________


//Funções

function getRanking(ano, semestre, simulador) {
	$("tbody").remove();
	$("#ranking-full").hide();
	$('#load-siaf').show();
	$.ajax({
		type: "GET",
		url: '/siaf/reserva/ranking.json',
		data: {
        	ano : ano.trim(),
        	semestre : semestre.trim(),
        	simulador : simulador
		}
	})
	.success(function(result) {
		$('#count-ranking').text('Estimativa de vagas restantes: ' + (result.periodoAtual.vagas - result.afastados.length));
		loadAfastados(result.afastados);
			
		$('i#anterior, i#anterior-simulador').show();
		$('i#posterior, i#posterior-simulador').show();
		if(result.periodoAnterior == null) {
			$('i#anterior, i#anterior-simulador').hide();
		} else {
			$('#anoAnterior').val(result.periodoAnterior.ano);
			$('#semestreAnterior').val(result.periodoAnterior.semestre);
		}
		
		if(result.periodoPosterior == null) {
			$('i#posterior, i#posterior-simulador').hide();
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
		$('#encerramento').attr('data-content', 'Data limite para solicitação de afastamento com início para o período ' + (result.periodoAtual.ano + 1) + '.' + result.periodoAtual.semestre);
		
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

function getStatus(reserva) {
	if(reserva == 'EM_ESPERA') {
		return "EM ESPERA";
	} else if(reserva == 'NAO_ACEITO') {
		return "NÃO ACEITO";
	} else if(reserva == 'CANCELADO_COM_PUNICAO') {
		return "CANCELADO COM PUNIÇÃO";
	}
	return reserva;
}

function getConceito(conceito) {
	if(conceito == 0) {
		return "-";
	}
	return conceito;
}

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

