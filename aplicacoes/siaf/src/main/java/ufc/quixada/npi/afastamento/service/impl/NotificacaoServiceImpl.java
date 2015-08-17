package ufc.quixada.npi.afastamento.service.impl;

import java.io.IOException;
import java.util.Properties;

import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.MessagingException;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import br.ufc.quixada.npi.model.Email;
import br.ufc.quixada.npi.service.EmailService;
import ufc.quixada.npi.afastamento.model.Notificacao;
import ufc.quixada.npi.afastamento.model.Professor;
import ufc.quixada.npi.afastamento.model.Reserva;
import ufc.quixada.npi.afastamento.service.NotificacaoService;
import ufc.quixada.npi.afastamento.service.ProfessorService;

@Named
public class NotificacaoServiceImpl implements NotificacaoService {

	@Inject
	private ProfessorService profService;

	@Inject
	private EmailService emailService;

	private String ASSUNTO = "email.assunto";
	private String RANKING = "email.corpo.ranking_atualizado";
	private String RESERVA_INCLUIDA = "email.corpo.reserva_incluida";
	private String RESERVA_EXCLUIDA = "email.corpo.reserva_excluida"; 
	private String GERENCIAMENTO_DE_RESERVAS = "email.corpo.gerenciamento_de_reservas";
	private String ALTERACAO_DADOS_CADASTRAIS = "email.corpo.alteracao_dados_cadastrais";
	private String LISTA_DOCENTES_ATUALIZADAS = "email.corpo.lista_docentes_atualizadas";
	private String ATUALIZACAO_CONCEITO = "email.corpo.atualizacao_conceito";
	private String ADMISSAO_ATUALIZADA = "email.corpo.admissao_atualizada";
	
	private String INICIO_PERIODO = "#INICIOPERIODO#";
	private String TERMINO_PERIODO = "#TERMINOPERIODO#";
	private String STATUS = "#STATUS#";
	private String NOME_PROFESSOR= "#PROFESSOR#";
	private String SIAP = "#SIAP#";
	private String CONCEITO = "#CONCEITO#";
	private String ADMISSAO = "#ADMISSAO#";
	
	@Override
	public void notificar(Reserva reserva, Notificacao tipoNotificacao)
			throws MessagingException {
		
		Resource resource = new ClassPathResource("/email.properties");
		Properties properties = null;
		try {
			properties = PropertiesLoaderUtils.loadProperties(resource);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (properties.getProperty("email.ativo").equals("true")) {
			
			Email email = new Email();
			email.setFrom("naoresponda@siaf.com");
			email.setSubject(properties.getProperty(ASSUNTO));

			if (tipoNotificacao == Notificacao.RANKING_ATUALIZADO) {
				String texto = properties.getProperty(RANKING);
				email.setText(texto);
				for (Professor prof : profService.findAtivos()) {
					email.setTo(prof.getEmail());
					emailService.sendEmail(email);
				}
			} else if (tipoNotificacao == Notificacao.RESERVA_INCLUIDA) {
				String inicioPeriodo = reserva.getAnoInicio() + "."	+ reserva.getSemestreInicio();
				String terminoPeriodo = reserva.getAnoTermino() + "." + reserva.getSemestreTermino();
				String texto = properties.getProperty(RESERVA_INCLUIDA).replaceAll(INICIO_PERIODO, inicioPeriodo)
						.replaceAll(TERMINO_PERIODO, terminoPeriodo);
				email.setText(texto);
				email.setTo(reserva.getProfessor().getEmail());
				emailService.sendEmail(email);
			} else if (tipoNotificacao == Notificacao.RESERVA_EXCLUIDA) {
				String inicioPeriodo = reserva.getAnoInicio() + "."	+ reserva.getSemestreInicio();
				String terminoPeriodo = reserva.getAnoTermino() + "." + reserva.getSemestreTermino();
				String texto = properties.getProperty(RESERVA_EXCLUIDA).replaceAll(INICIO_PERIODO, inicioPeriodo)
						.replaceAll(TERMINO_PERIODO, terminoPeriodo);
				email.setText(texto);
				email.setTo(reserva.getProfessor().getEmail());
				emailService.sendEmail(email);
			}else if (tipoNotificacao == Notificacao.GERENCIAMENTO_DE_RESERVAS) {
				String status = reserva.getStatus().getDescricao();
				String texto = properties.getProperty(GERENCIAMENTO_DE_RESERVAS).replaceAll(STATUS, status);
				email.setText(texto);
				email.setTo(reserva.getProfessor().getEmail());
				emailService.sendEmail(email);

			} else if (tipoNotificacao == Notificacao.ALTERACAO_DADOS_CADASTRAIS) {
				String texto = properties.getProperty(ALTERACAO_DADOS_CADASTRAIS);
				email.setText(texto);
				email.setTo(reserva.getProfessor().getEmail());
				emailService.sendEmail(email);

			} else if (tipoNotificacao == Notificacao.LISTA_DOCENTES_ATUALIZADAS) {
				String nomeProfessor = reserva.getProfessor().getNome();
				String siap = reserva.getProfessor().getSiape();
				String texto = properties.getProperty(LISTA_DOCENTES_ATUALIZADAS).replaceAll(NOME_PROFESSOR, nomeProfessor).replaceAll(SIAP, siap);
				email.setText(texto);
				email.setTo(reserva.getProfessor().getEmail());
				emailService.sendEmail(email);
			}else if(tipoNotificacao == Notificacao.ATUALIZACAO_CONCEITO){
				String inicioPeriodo = reserva.getAnoInicio() + "."	+ reserva.getSemestreInicio();
				String terminoPeriodo = reserva.getAnoTermino() + "." + reserva.getSemestreTermino();
				String conceito = String.valueOf(reserva.getConceitoPrograma());
				String texto = properties.getProperty(ATUALIZACAO_CONCEITO).replaceAll(INICIO_PERIODO, inicioPeriodo)
						.replaceAll(TERMINO_PERIODO, terminoPeriodo).replaceAll(CONCEITO, conceito);
				email.setText(texto);
				email.setTo(reserva.getProfessor().getEmail());
				emailService.sendEmail(email);
			}else if(tipoNotificacao == Notificacao.ADMISSAO_ATUALIZADA){
				String admissao = reserva.getProfessor().getAnoAdmissao() + "." + reserva.getProfessor().getSemestreAdmissao();
				String texto = properties.getProperty(ADMISSAO_ATUALIZADA).replaceAll(ADMISSAO, admissao);
				email.setText(texto);
				email.setTo(reserva.getProfessor().getEmail());
				emailService.sendEmail(email);
			}
		}
	}

}
