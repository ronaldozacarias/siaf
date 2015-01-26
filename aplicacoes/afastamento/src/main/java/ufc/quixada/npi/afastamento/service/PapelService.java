package ufc.quixada.npi.afastamento.service;

import ufc.quixada.npi.afastamento.model.Papel;
import br.ufc.quixada.npi.service.GenericService;

public interface PapelService extends GenericService<Papel>{
	
	Papel getPapel(String papel);
}
