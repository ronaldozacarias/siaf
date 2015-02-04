package ufc.quixada.npi.afastamento.controller;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;

import org.joda.time.LocalDate;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ufc.quixada.npi.afastamento.model.Periodo;
import ufc.quixada.npi.afastamento.model.Professor;
import ufc.quixada.npi.afastamento.model.StatusPeriodo;
import ufc.quixada.npi.afastamento.service.PeriodoService;
import ufc.quixada.npi.afastamento.service.ProfessorService;


@Controller
@RequestMapping("administracao")
public class AdministracaoController {
	
	@Inject
	private ProfessorService professorService;
	
	/*@Inject
	private PapelService papelService;

	@Inject
	private GenericService<Usuario> usuarioService;*/

	@Inject
	private PeriodoService periodoService;	
	
	@RequestMapping(value = "/professores", method = RequestMethod.GET)
	public String listarProfessores(Model model) {
		List<Professor> professors = professorService.findAtivos();
		model.addAttribute("professores", professors);
		return "admin/professores";
	}

	/*@RequestMapping(value = "/novo-professor", method = RequestMethod.GET)
	public String cadastroProfessor(Model model) {
		model.addAttribute("professor", new Professor());
		return "admin/novo-professor";
	}*/

	/*@RequestMapping(value = "/novo-professor", method = RequestMethod.POST)
	public String cadastroProfessor(
			@Valid @ModelAttribute("professor") Professor professor, BindingResult result, Model model) {

		if (result.hasErrors()) {
			return "admin/novo-professor";
		}
		
		Usuario usuario = professor.getUsuario();
		
		Usuario
		ShaPasswordEncoder encoder = new ShaPasswordEncoder(256);
		usuario.setLogin(professor.getSiape());
		usuario.setPassword(encoder.encodePassword(professor.getSiape(), ""));
		usuario.setHabilitado(true);

		List<Papel> papeis = new ArrayList<Papel>();
		papeis.add(papelService.getPapel("ROLE_PROFESSOR"));
		
		usuario.setPapeis(papeis);
		
		usuarioService.save(usuario);

		Professor
		professor.setUsuario(usuario);
		professorService.update(professor);
		
		//int totalProfessores = professorService.getTotalProfessores(); 
		//int vagas = (int) (totalProfessores * 0.15);
		//periodoService.updateVagas(vagas);
		
		return "redirect:/administracao/professores";
	}*/
	
	@RequestMapping(value = "/periodos.json", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<Periodo> periodos() {
		return periodoService.find(Periodo.class);
	}
	
	@RequestMapping(value = "/periodo", method = RequestMethod.GET)
	public String listarPeriodos(Model model) {
		model.addAttribute("periodo", new Periodo());
		return "admin/periodo";
	}

	@RequestMapping(value = "/periodo", method = RequestMethod.POST)
	public String listarPeriodos(Model model, @RequestParam("ano") Integer ano, @RequestParam("semestre") Integer semestre) {
		Periodo periodo = periodoService.getPeriodo(ano, semestre);
		
		if(!notNull(periodo)){
			model.addAttribute("message", "Periodo " + ano + "." + semestre + " não está cadastrado.");
			return "admin/periodo";
		} else if (periodo.getStatus().equals(StatusPeriodo.ENCERRADO)) {
			model.addAttribute("permitirUpdate", false);
			model.addAttribute("periodo", periodo);
			return "admin/periodo";
		}

		boolean permitirUpdate = false;
		boolean permitirUpdateEncerramento = false;
		boolean permitirUpdateVagas = false;
		
		Periodo periodoSolicitacao = periodoService.getPeriodo(periodo.getAno()-1, periodo.getSemestre());	

		if(periodo.getStatus().equals(StatusPeriodo.ABERTO)){
			permitirUpdateEncerramento = true;
		}

		if(periodoSolicitacao.getStatus().equals(StatusPeriodo.ABERTO)){
			permitirUpdateVagas = true;
		}
				
		if(permitirUpdateVagas || permitirUpdateEncerramento){
			permitirUpdate = true;
		}
		
		model.addAttribute("permitirUpdateVagas", 		 permitirUpdateVagas);
		model.addAttribute("permitirUpdateEncerramento", permitirUpdateEncerramento);
		model.addAttribute("permitirUpdate", 			 permitirUpdate);
		model.addAttribute("periodo", periodo);
		return "admin/periodo";
	}
	
	private boolean notNull(Object object){
		return object != null ?  true : false;
	}
	
	//Verifica utilidade?
	private boolean isEnceramentoValido(Date date) {
		LocalDate now = new LocalDate();
		LocalDate enceramento = notNull(date) ? new LocalDate(date): null;

		return (enceramento.isAfter(now) || enceramento.isEqual(now)) ? true : false;
	}

	@RequestMapping(value = "/update-periodo", method = RequestMethod.POST)
	public String listarPeriodos(Model model, RedirectAttributes redirectAttributes, @Valid @ModelAttribute("periodo") Periodo periodoEmAtualizacao, BindingResult result) {

		boolean permitirUpdateEncerramento = false;
		boolean permitirUpdateVagas = false;
		boolean permitirUpdate = false;

		Periodo periodoEmAtualizacaoDoBanco = periodoService.find(Periodo.class, periodoEmAtualizacao.getId());
		Periodo periodoSolicitacao = periodoService.getPeriodo(periodoEmAtualizacao.getAno()-1, periodoEmAtualizacao.getSemestre());	

		if(periodoEmAtualizacaoDoBanco.getStatus().equals(StatusPeriodo.ABERTO)){
			permitirUpdateEncerramento = true;
		}

		if(periodoSolicitacao.getStatus().equals(StatusPeriodo.ABERTO)){
			permitirUpdateVagas = true;
		}
				
		if(permitirUpdateVagas || permitirUpdateEncerramento){
			permitirUpdate = true;
		}
		
		model.addAttribute("permitirUpdate", permitirUpdate);
		model.addAttribute("permitirUpdateVagas", permitirUpdateVagas);
		model.addAttribute("permitirUpdateEncerramento", permitirUpdateEncerramento);

		if(!isEnceramentoValido(periodoEmAtualizacao.getEncerramento())){
			periodoEmAtualizacaoDoBanco.setEncerramento(periodoEmAtualizacao.getEncerramento());
			model.addAttribute("periodo", periodoEmAtualizacaoDoBanco);
			model.addAttribute("errorData", "Informe uma data futura.");
			return "admin/periodo";
		}

		if (result.hasErrors()) {
			return "admin/periodo";
		}

		if(permitirUpdateEncerramento){
			periodoEmAtualizacaoDoBanco.setEncerramento(periodoEmAtualizacao.getEncerramento());
		}

		if(permitirUpdateVagas){
			periodoEmAtualizacaoDoBanco.setVagas(periodoEmAtualizacao.getVagas());
		}
			
		if(permitirUpdateEncerramento || permitirUpdateVagas){
			periodoService.update(periodoEmAtualizacaoDoBanco);
			model.addAttribute("periodo", periodoEmAtualizacaoDoBanco);
			model.addAttribute("info","Periodo " +periodoEmAtualizacaoDoBanco.getAno() + "." + periodoEmAtualizacaoDoBanco.getSemestre() + " atualizado com sucesso!");
		}
		
		return "admin/periodo";
	}

	/*@RequestMapping(value = "/desabilita", method = RequestMethod.POST)
	public String habilitar(@RequestParam("pk") Long id, RedirectAttributes redirect) {

		Professor professor = professorService.find(Professor.class, id);
		professor.setDataRemocao(new Date());
		professor.getUsuario().setHabilitado(false);
		professorService.update(professor);
		
		redirect.addFlashAttribute("info", "Prof(a).: " + professor.getUsuario().getNome() + " desabilitado com sucesso.");
		return "redirect:/administracao/professores";
	}*/

}
