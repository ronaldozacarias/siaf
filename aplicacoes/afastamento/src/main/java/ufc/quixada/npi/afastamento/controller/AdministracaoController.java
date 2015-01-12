package ufc.quixada.npi.afastamento.controller;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ufc.quixada.npi.afastamento.model.Periodo;
import ufc.quixada.npi.afastamento.model.Professor;
import ufc.quixada.npi.afastamento.service.PeriodoService;
import ufc.quixada.npi.afastamento.service.ReservaService;
import br.ufc.quixada.npi.service.GenericService;

@Controller
@RequestMapping("administracao")
public class AdministracaoController {
	
	@Inject
	private GenericService<Professor> professorService;

	@Inject
	private PeriodoService periodoService;	
	
	@RequestMapping(value = "/professores", method = RequestMethod.GET)
	public String listarProfessores(Model model) {
		return "#admin/professores";
	}

	@RequestMapping(value = "/novo-professor", method = RequestMethod.GET)
	public String cadastroProfessor(Model model) {
		model.addAttribute("professor", new Professor());
		return "admin/novo-professor";
	}

	@RequestMapping(value = "/novo-professor", method = RequestMethod.POST)
	public String cadastroProfessor(@ModelAttribute("professor") Professor professor) {
		professorService.save(professor);
		return "admin/lista-professores";
	}
	
	@RequestMapping(value = "/periodos", method = RequestMethod.GET)
	public String listarPeriodos(Model model) {
		model.addAttribute("periodo", new Periodo());
		return "admin/periodos";
	}

	@RequestMapping(value = "/periodo", method = RequestMethod.POST)
	public String listarPeriodos(Model model, @RequestParam("ano") Integer ano, @RequestParam("semestre") Integer semestre) {
		
		model.addAttribute("periodo", periodoService.getPeriodo(ano, semestre));
		return "admin/periodos";
	}

}
