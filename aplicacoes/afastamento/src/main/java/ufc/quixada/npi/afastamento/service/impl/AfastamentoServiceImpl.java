package ufc.quixada.npi.afastamento.service.impl;

import javax.inject.Inject;
import javax.inject.Named;

import ufc.quixada.npi.afastamento.model.Afastamento;
import ufc.quixada.npi.afastamento.service.AfastamentoService;
import br.ufc.quixada.npi.repository.GenericRepository;

@Named
public class AfastamentoServiceImpl implements AfastamentoService {

	@Inject
	private GenericRepository<Afastamento> afastamentoRepository;
	
	@Override
	public void salvar(Afastamento afastamento) {
		afastamentoRepository.save(afastamento);
		
	}

}
