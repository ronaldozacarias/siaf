package ufc.quixada.npi.afastamento.controller;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ufc.quixada.npi.afastamento.model.Usuario;
import ufc.quixada.npi.afastamento.service.UsuarioService;
import ufc.quixada.npi.afastamento.util.Constants;

@Controller
public class ConfiguracaoController {
	
	@Inject
	private UsuarioService usuarioService;
	
	@RequestMapping(value = "alterar-senha", method = RequestMethod.GET)
	public String getAlterarSenha() {
		return "alterarSenha";
	}
	
	@RequestMapping(value = "alterar-senha", method = RequestMethod.POST)
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
	
	@RequestMapping(value = "recuperar-senha", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Model recuperarSenha(HttpServletRequest request, Model model, HttpSession session) {
		String email = request.getParameter("email");
		Usuario usuario = usuarioService.getUsuarioByEmail(email);
		if(usuario == null) {
			model.addAttribute("resultado", "erro");
			model.addAttribute("info", "O email " + email + " não foi encontrado.");
		} else {
			model.addAttribute("resultado", "ok");
			model.addAttribute("info", "Os passos para recuperação de senha foi enviado para o email " + email);
		}
		return model;
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
