package ufc.quixada.npi.afastamento.service.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.MessagingException;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import ufc.quixada.npi.afastamento.model.Notificacao;
import ufc.quixada.npi.afastamento.model.Reserva;
import ufc.quixada.npi.afastamento.service.NotificacaoService;
import br.ufc.quixada.npi.model.Email;
import br.ufc.quixada.npi.service.EmailService;

@Named
public class NotificacaoServiceImpl implements NotificacaoService {

	@Inject
	private EmailService emailService;

	private String ASSUNTO = "email.assunto";
	
	private String RESERVA_INCLUIDA = "email.corpo.reserva_incluida";
	private String RESERVA_EXCLUIDA = "email.corpo.reserva_excluida";
	private String RESERVA_CANCELADA = "email.corpo.reserva_cancelada";
	private String RESERVA_ALTERADA = "email.corpo.reserva_alterada";
	private String RESERVA_HOMOLOGADA = "email.corpo.reserva_homologada";
	private String ADMISSAO_ALTERADA = "email.corpo.admissao_alterada";
	
	private String PROFESSOR = "#PROFESSOR#";
	private String INICIO_PERIODO = "#INICIO_PERIODO#";
	private String TERMINO_PERIODO = "#TERMINO_PERIODO#";
	private String PROGRAMA = "#PROGRAMA#";
	private String CONCEITO = "#CONCEITO#";
	private String INSTITUICAO = "#INSTITUICAO#";
	private String SOLICITACAO = "#SOLICITACAO#";
	private String EXCLUSAO = "#EXCLUSAO#";
	private String CANCELAMENTO = "#CANCELAMENTO#";
	private String MOTIVO = "#MOTIVO#";
	private String STATUS = "#STATUS#";
	private String ANO_ADMISSAO = "#ANO_ADMISSAO#";
	private String SEMESTRE_ADMISSAO = "#SEMESTRE_ADMISSAO#";
	
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
			
			String inicioPeriodo = reserva.getAnoInicio() + "."	+ reserva.getSemestreInicio();
			String terminoPeriodo = reserva.getAnoTermino() + "." + reserva.getSemestreTermino();
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");

			if (tipoNotificacao == Notificacao.RESERVA_INCLUIDA) {
				String texto = properties.getProperty(RESERVA_INCLUIDA).replaceAll(PROFESSOR, reserva.getProfessor().getNome())
						.replaceAll(INICIO_PERIODO, inicioPeriodo).replaceAll(TERMINO_PERIODO, terminoPeriodo)
						.replaceAll(PROGRAMA, reserva.getPrograma().getDescricao())
						.replaceAll(CONCEITO, reserva.getConceitoPrograma().toString())
						.replaceAll(SOLICITACAO, dateFormat.format(reserva.getDataSolicitacao()));
				if (reserva.getInstituicao() != null) {
					texto.replaceAll(INSTITUICAO, reserva.getInstituicao());
				} else {
					texto.replaceAll(INSTITUICAO, "-");
				}
				email.setText(texto);
				email.setTo(reserva.getProfessor().getEmail());
				emailService.sendEmail(email);
			} else if (tipoNotificacao == Notificacao.RESERVA_EXCLUIDA) {
				String texto = properties.getProperty(RESERVA_EXCLUIDA).replaceAll(PROFESSOR, reserva.getProfessor().getNome())
						.replaceAll(INICIO_PERIODO, inicioPeriodo).replaceAll(TERMINO_PERIODO, terminoPeriodo)
						.replaceAll(PROGRAMA, reserva.getPrograma().getDescricao())
						.replaceAll(CONCEITO, reserva.getConceitoPrograma().toString())
						.replaceAll(SOLICITACAO, dateFormat.format(reserva.getDataSolicitacao()))
						.replaceAll(EXCLUSAO, dateFormat.format(new Date()));
				if (reserva.getInstituicao() != null) {
					texto.replaceAll(INSTITUICAO, reserva.getInstituicao());
				} else {
					texto.replaceAll(INSTITUICAO, "-");
				}
				email.setText(texto);
				email.setTo(reserva.getProfessor().getEmail());
				emailService.sendEmail(email);
			} else if (tipoNotificacao == Notificacao.RESERVA_CANCELADA) {
				String texto = properties.getProperty(RESERVA_CANCELADA).replaceAll(PROFESSOR, reserva.getProfessor().getNome())
						.replaceAll(INICIO_PERIODO, inicioPeriodo).replaceAll(TERMINO_PERIODO, terminoPeriodo)
						.replaceAll(PROGRAMA, reserva.getPrograma().getDescricao())
						.replaceAll(CONCEITO, reserva.getConceitoPrograma().toString())
						.replaceAll(SOLICITACAO, dateFormat.format(reserva.getDataSolicitacao()))
						.replaceAll(CANCELAMENTO, dateFormat.format(reserva.getDataCancelamento()))
						.replaceAll(MOTIVO, reserva.getMotivoCancelamento());
				if (reserva.getInstituicao() != null) {
					texto.replaceAll(INSTITUICAO, reserva.getInstituicao());
				} else {
					texto.replaceAll(INSTITUICAO, "-");
				}
				email.setText(texto);
				email.setTo(reserva.getProfessor().getEmail());
				emailService.sendEmail(email);
			} else if (tipoNotificacao == Notificacao.RESERVA_ALTERADA) {
				/*String texto = properties.getProperty(RESERVA_ALTERADA).replaceAll(PROFESSOR, reserva.getProfessor().getNome())
						.replaceAll(INICIO_PERIODO, inicioPeriodo).replaceAll(TERMINO_PERIODO, terminoPeriodo)
						.replaceAll(PROGRAMA, reserva.getPrograma().getDescricao())
						.replaceAll(CONCEITO, reserva.getConceitoPrograma().toString())
						.replaceAll(SOLICITACAO, dateFormat.format(reserva.getDataSolicitacao()));
				if (reserva.getInstituicao() != null) {
					texto.replaceAll(INSTITUICAO, reserva.getInstituicao());
				} else {
					texto.replaceAll(INSTITUICAO, "-");
				}
				email.setText(texto);
				email.setTo(reserva.getProfessor().getEmail());
				emailService.sendEmail(email);*/
			} else if (tipoNotificacao == Notificacao.RESERVA_HOMOLOGADA) {
				String texto = properties.getProperty(RESERVA_HOMOLOGADA).replaceAll(PROFESSOR, reserva.getProfessor().getNome())
						.replaceAll(INICIO_PERIODO, inicioPeriodo).replaceAll(TERMINO_PERIODO, terminoPeriodo)
						.replaceAll(PROGRAMA, reserva.getPrograma().getDescricao())
						.replaceAll(CONCEITO, reserva.getConceitoPrograma().toString())
						.replaceAll(SOLICITACAO, dateFormat.format(reserva.getDataSolicitacao()))
						.replaceAll(STATUS, reserva.getStatus().getDescricao());
				if (reserva.getInstituicao() != null) {
					texto.replaceAll(INSTITUICAO, reserva.getInstituicao());
				} else {
					texto.replaceAll(INSTITUICAO, "-");
				}
				email.setText(texto);
				email.setTo(reserva.getProfessor().getEmail());
				emailService.sendEmail(email);
			} else if (tipoNotificacao == Notificacao.ADMISSAO_ALTERADA) {
				String texto = properties.getProperty(ADMISSAO_ALTERADA).replaceAll(PROFESSOR, reserva.getProfessor().getNome())
						.replaceAll(ANO_ADMISSAO, reserva.getProfessor().getAnoAdmissao().toString())
						.replaceAll(SEMESTRE_ADMISSAO, reserva.getProfessor().getSemestreAdmissao().toString());
				email.setText(texto);
				email.setTo(reserva.getProfessor().getEmail());
				emailService.sendEmail(email);
			}
		}
	}

}
