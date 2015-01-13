package ufc.quixada.npi.afastamento.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ufc.quixada.npi.afastamento.model.Papel;
import ufc.quixada.npi.afastamento.model.Periodo;
import ufc.quixada.npi.afastamento.model.Professor;
import ufc.quixada.npi.afastamento.service.PeriodoService;
import br.ufc.quixada.npi.service.GenericService;


@Controller
@RequestMapping("administracao")
public class AdministracaoController {
	
	@Inject
	private GenericService<Professor> professorService;
	
	@Inject
	private GenericService<Papel> papelService;

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
	public String cadastroProfessor(@Valid @ModelAttribute("professor") Professor professor, BindingResult result, Model model) {

		if (result.hasErrors()) {
			return "admin/novo-professor";
		}

		ShaPasswordEncoder encoder = new ShaPasswordEncoder(256);
		professor.setPassword(encoder.encodePassword(professor.getSiape(), ""));
		professor.setHabilitado(true);
		List<Papel> papeis = new ArrayList<Papel>();
		papeis.add(papelService.find(Papel.class, 2L));
		professor.setPapeis(papeis);
		professorService.update(professor);
		
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

	@RequestMapping(value = "/update-periodo", method = RequestMethod.POST)
	public String listarPeriodos(Model model, @Valid @ModelAttribute("periodo") Periodo periodo, BindingResult result) {

		if (result.hasErrors()) {
			model.addAttribute("teste", "teste");
			return "admin/periodos";
		}
		
		periodoService.update(periodo);
		return "admin/periodos";
	}

}
