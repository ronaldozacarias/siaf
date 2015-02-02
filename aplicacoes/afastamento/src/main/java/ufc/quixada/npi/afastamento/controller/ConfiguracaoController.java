package ufc.quixada.npi.afastamento.controller;

import java.util.Date;
import java.util.UUID;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ufc.quixada.npi.afastamento.model.RecuperacaoSenha;
import ufc.quixada.npi.afastamento.model.Usuario;
import ufc.quixada.npi.afastamento.service.RecuperacaoSenhaService;
import ufc.quixada.npi.afastamento.service.UserService;
import ufc.quixada.npi.afastamento.util.Constants;

@Controller
@RequestMapping("configuracao")
public class ConfiguracaoController {
	
	@Inject
	private UserService usuarioService;
	
	@Inject
	private RecuperacaoSenhaService recuperacaoService;
	
	@RequestMapping(value = "/alterar-senha", method = RequestMethod.GET)
	public String getAlterarSenha() {
		return "alterarSenha";
	}
	
	@RequestMapping(value = "/alterar-senha", method = RequestMethod.POST)
	public String alterarSenha(@RequestParam("senhaAnterior") String senhaAnterior, @RequestParam("novaSenha") String novaSenha, 
			@RequestParam("novaSenhaVerify") String novaSenhaVerify, Model model, HttpSession session) {
		ShaPasswordEncoder encoder = new ShaPasswordEncoder(256);
		Usuario usuario = getUsuarioLogado(session);
		if(!usuario.getPassword().equals(encoder.encodePassword(senhaAnterior, ""))) {
			model.addAttribute("erro", "A senha atual digitada é inválida");
		} else if(!novaSenha.equals(novaSenhaVerify)) {
			model.addAttribute("erro", "Digite a mesma senha nos dois campos da nova senha");
		} else {
			usuario.setPassword(encoder.encodePassword(novaSenha, ""));
			usuarioService.update(usuario);
			model.addAttribute("info", "Senha alterada com sucesso");
		}
		return "alterarSenha";
	}
	
	@RequestMapping(value = "/recuperar-senha", method = RequestMethod.POST)
	public String recuperarSenha(HttpServletRequest request, Model model, HttpSession session) {
		String email = request.getParameter("email");
		Usuario usuario = usuarioService.getUsuarioByEmail(email);
		if(usuario == null) {
			model.addAttribute("erro", "O email " + email + " não foi encontrado.");
		} else {
			RecuperacaoSenha recuperacao = new RecuperacaoSenha();
			recuperacao.setCodigo(UUID.randomUUID().toString());
			recuperacao.setUsuario(usuario);
			recuperacao.setDataSolicitacao(new Date());
			recuperacaoService.recuperarSenha(recuperacao);
			model.addAttribute("info", "Os passos para recuperação de senha foi enviado para o email " + email);
		}
		return "login";
	}
	
	@RequestMapping(value = "/recuperacao/{codigo}", method = RequestMethod.GET)
	public String recuperacaoSenha(@PathVariable("codigo") String codigo, Model model) {
		RecuperacaoSenha recuperacao = recuperacaoService.getRecuperacaoByCodigo(codigo);
		if(recuperacao != null) {
			model.addAttribute("codigo", recuperacao.getCodigo());
			return "recuperacaoSenha";
		}
		return "login";
	}
	
	@RequestMapping(value = "/nova-senha", method = RequestMethod.POST)
	public String getNovaSenha(@RequestParam("novaSenha") String novaSenha, @RequestParam("novaSenhaVerify") String novaSenhaVerify, 
			@RequestParam("codigo") String codigo, Model model) {
		if(novaSenha.isEmpty()) {
			model.addAttribute("erro", "Digite a nova senha");
			return "recuperacaoSenha";
		} else if(!novaSenha.equals(novaSenhaVerify)) {
			model.addAttribute("erro", "As senhas digitadas não conferem");
			return "recuperacaoSenha";
		}
		RecuperacaoSenha recuperacao = recuperacaoService.getRecuperacaoByCodigo(codigo);
		if(recuperacao == null) {
			model.addAttribute("erro", "Sua solicitação de recuperação de senha expirou");
		} else {
			Usuario usuario = recuperacao.getUsuario();
			ShaPasswordEncoder encoder = new ShaPasswordEncoder(256);
			usuario.setPassword(encoder.encodePassword(novaSenha, ""));
			usuarioService.update(usuario);
			recuperacaoService.delete(recuperacao);
			model.addAttribute("info", "Senha alterada com sucesso");
		}
		return "login";
	}
	
	private Usuario getUsuarioLogado(HttpSession session) {
		if (session.getAttribute(Constants.USUARIO_LOGADO) == null) {
			Usuario usuario = usuarioService
					.getUsuarioByLogin(SecurityContextHolder.getContext()
							.getAuthentication().getName());
			session.setAttribute(Constants.USUARIO_LOGADO, usuario);
		}
		return (Usuario) session.getAttribute(Constants.USUARIO_LOGADO);
	}

}
