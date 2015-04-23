package ufc.quixada.npi.afastamento.service;

import javax.mail.MessagingException;

import ufc.quixada.npi.afastamento.model.Reserva;
import ufc.quixada.npi.afastamento.model.Notificacao;

public interface NotificacaoService {

	void notificar(Reserva reserva, Notificacao tipoNotificacao) throws MessagingException;

}
