package ufc.quixada.npi.afastamento.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;

import org.joda.time.LocalDate;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ufc.quixada.npi.afastamento.model.Papel;
import ufc.quixada.npi.afastamento.model.Periodo;
import ufc.quixada.npi.afastamento.model.Professor;
import ufc.quixada.npi.afastamento.model.StatusPeriodo;
import ufc.quixada.npi.afastamento.model.StatusReserva;
import ufc.quixada.npi.afastamento.service.PeriodoService;
import ufc.quixada.npi.afastamento.service.ProfessorService;
import br.ufc.quixada.npi.service.GenericService;


@Controller
@RequestMapping("administracao")
public class AdministracaoController {
	
	@Inject
	private ProfessorService professorService;
	
	@Inject
	private GenericService<Papel> papelService;

	@Inject
	private PeriodoService periodoService;	
	
	@RequestMapping(value = "/professores", method = RequestMethod.GET)
	public String listarProfessores(Model model) {
		List<Professor> professors = professorService.findOrder();
		model.addAttribute("professores", professors);
		return "admin/professores";
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
		
		return "redirect:/administracao/professores";
	}
	
	@RequestMapping(value = "/periodo", method = RequestMethod.GET)
	public String listarPeriodos(Model model) {
		model.addAttribute("periodo", new Periodo());
		return "admin/periodo";
	}

	@RequestMapping(value = "/periodo", method = RequestMethod.POST)
	public String listarPeriodos(Model model, @RequestParam("ano") Integer ano, @RequestParam("semestre") Integer semestre) {
		Periodo periodo = periodoService.getPeriodo(ano, semestre);
		boolean permitirUpdate = false;

		if(periodo == null){
			model.addAttribute("message", "Periodo " + ano + "." + semestre + " não está cadastrado.");
			return "admin/periodo";
		}
		
		Periodo periodoSolicitacao = periodoService.getPeriodo(periodo.getAno()-1, periodo.getSemestre());
		
		
		if(periodo.getEncerramento() != null){
			permitirUpdate = updateEncerramento(periodo.getEncerramento());
			model.addAttribute("permitirUpdate", permitirUpdate);
		}else{
			permitirUpdate = true;
			//model.addAttribute("permitirUpdate", true);
		}

		if(notNull(periodoSolicitacao) && periodoSolicitacao.getStatus().equals(StatusReserva.ENCERRADO)){
			permitirUpdate = false;
			//model.addAttribute("permitirUpdate", false);
		}

		model.addAttribute("permitirUpdate", permitirUpdate);
		model.addAttribute("periodo", periodo);
		return "admin/periodo";
	}
	
	private boolean notNull(Object object){
		return object != null ?  true : false;
	}
	
	private boolean updateEncerramento(Date date) {
		LocalDate now = new LocalDate();
		
		LocalDate enceramento = date != null ? new LocalDate(date): null;

		if (enceramento.isAfter(now) || enceramento.isEqual(now)) {
			return true;
		}
		return false;
	}

	@RequestMapping(value = "/update-periodo", method = RequestMethod.POST)
	public String listarPeriodos(Model model, RedirectAttributes redirectAttributes, @Valid @ModelAttribute("periodo") Periodo periodoAtualizado, BindingResult result) {

		model.addAttribute("permitirUpdate", true);
		if (result.hasErrors()) {
			return "admin/periodo";
		}

		if(periodoAtualizado.getId() != null){
			Periodo periodoAtual = periodoService.find(Periodo.class, periodoAtualizado.getId());
			
			if ( periodoAtual.getStatus().equals(StatusPeriodo.ABERTO) && updateEncerramento(periodoAtualizado.getEncerramento()) ) {
				periodoService.update(periodoAtualizado);
				redirectAttributes.addFlashAttribute("info","Periodo " +periodoAtualizado.getAno() + "." + periodoAtualizado.getSemestre() + " atualizado com sucesso!");
				model.addAttribute("info","Periodo " +periodoAtualizado.getAno() + "." + periodoAtualizado.getSemestre() + " atualizado com sucesso!");
			}else{
				model.addAttribute("errorData","Informe uma data futura.");
			}

		}

		return "admin/periodo";
	}

}
