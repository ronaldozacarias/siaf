package ufc.quixada.npi.afastamento.controller;

import java.io.IOException;
import java.util.Date;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ufc.quixada.npi.afastamento.model.Afastamento;
import ufc.quixada.npi.afastamento.model.Documento;
import ufc.quixada.npi.afastamento.model.Formacao;
import ufc.quixada.npi.afastamento.model.Professor;
import ufc.quixada.npi.afastamento.service.AfastamentoService;
import ufc.quixada.npi.afastamento.service.UsuarioService;
import ufc.quixada.npi.afastamento.util.Constants;

@Controller
@RequestMapping("afastamento")
public class AfastamentoController {
	
	@Inject
	private UsuarioService usuarioService;
	
	@Inject
	private AfastamentoService afastamentoService;
	
	@RequestMapping(value = "/ranking", method = RequestMethod.GET)
	public String ranking(Model model, HttpSession session) {
		return "afastamentos/ranking";
	}
	
	@RequestMapping(value = "/solicitar", method = RequestMethod.GET)
	public String solicitarForm(Model model, HttpSession session) {
		model.addAttribute("afastamento", new Afastamento());
		model.addAttribute("professor", getUsuarioLogado(session));
		model.addAttribute("formacao", Formacao.values());
		return "afastamentos/solicitar";
	}
	
	@RequestMapping(value = "/solicitar", method = RequestMethod.POST)
	public String solicitar(@RequestParam("ano-inicio") Integer anoInicio, @RequestParam("semestre-inicio") Integer semestreInicio,
			@RequestParam("ano-termino") Integer anoTermino, @RequestParam("semestre-termino") Integer semestreTermino,
			@RequestParam("formacao") Formacao formacao, @RequestParam("carta") MultipartFile carta, 
			@RequestParam("termo") MultipartFile termo, Model model, RedirectAttributes redirect, HttpSession session) {
		Documento cartaDocumento = null;
		Documento termoDocumento = null;
		try {
			if (carta != null && carta.getBytes() != null && carta.getBytes().length != 0) {
				cartaDocumento = new Documento();
				cartaDocumento.setArquivo(carta.getBytes());
				cartaDocumento.setNome("CARTA_DE_ACEITACAO_DA_INSTITUICAO");
				cartaDocumento.setExtensao(carta.getContentType());
			}
			if (termo != null && termo.getBytes() != null && termo.getBytes().length != 0) {
				termoDocumento = new Documento();
				termoDocumento.setArquivo(termo.getBytes());
				termoDocumento.setNome("TERMO_DE_COMPROMISSO_E_RESPONSABILIDADE");
				termoDocumento.setExtensao(termo.getContentType());
			}
		} catch (IOException e) {
			model.addAttribute("erro", "Erro no upload de arquivos");
			return "afastamentos/solicitar";
		}
		
		Afastamento afastamento = new Afastamento();
		afastamento.setAnoInicio(anoInicio);
		afastamento.setSemestreInicio(semestreInicio);
		afastamento.setAnoTermino(anoTermino);
		afastamento.setSemestreTermino(semestreTermino);
		afastamento.setCartaAceitacao(cartaDocumento);
		afastamento.setTermoCompromisso(termoDocumento);
		afastamento.setDataSolicitacao(new Date());
		afastamento.setFormacao(formacao);
		afastamento.setProfessor(getUsuarioLogado(session));
		
		afastamentoService.salvar(afastamento);
		
		return "afastamentos/ranking";
	}
	
	private Professor getUsuarioLogado(HttpSession session) {
		if (session.getAttribute(Constants.USUARIO_LOGADO) == null) {
			Professor professor = usuarioService
					.getUsuarioByLogin(SecurityContextHolder.getContext()
							.getAuthentication().getName());
			session.setAttribute(Constants.USUARIO_LOGADO, professor);
		}
		return (Professor) session.getAttribute(Constants.USUARIO_LOGADO);
	}
	

}
