package it.prova.pokeronline.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.prova.pokeronline.model.Tavolo;
import it.prova.pokeronline.model.Utente;
import it.prova.pokeronline.repository.tavolo.TavoloRepository;
import it.prova.pokeronline.web.api.exception.TavoloConGiocatoriException;

@Service
@Transactional(readOnly = true)
public class TavoloServiceImpl implements TavoloService {

	@Autowired
	private TavoloRepository repository;
	@Autowired
	private UtenteService utenteService;

	@Override
	public List<Tavolo> listAll(boolean eager) {
		if (eager) {
			return (List<Tavolo>) repository.findAllEager();
		}
		return (List<Tavolo>) repository.findAll();
	}

	@Override
	public Tavolo caricaSingoloElemento(Long id) {
		return repository.findById(id).orElse(null);
	}

	@Override
	public Tavolo caricaSingoloElementoEager(Long id) {
		return repository.findByIdEager(id);
	}

	@Override
	@Transactional
	public Tavolo aggiorna(Tavolo tavoloInstance) {

		if (!tavoloInstance.getGiocatori().isEmpty()) {
			throw new TavoloConGiocatoriException("Attenzione! Tavolo ancora pieno. Prima rimuovi i giocatori");
		}
		return repository.save(tavoloInstance);
	}

	@Override
	@Transactional
	public Tavolo inserisciNuovo(Tavolo tavoloInstance) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();

		// estraggo le info dal principal
		Utente utenteLoggato = utenteService.findByUsername(username);
		tavoloInstance.setDataCreazione(LocalDate.now());
		tavoloInstance.setUtenteCreazione(utenteLoggato);

		return repository.save(tavoloInstance);
	}

	@Override
	@Transactional
	public void rimuovi(Long idToRemove) {
		if (!this.caricaSingoloElementoEager(idToRemove).getGiocatori().isEmpty()) {
			throw new TavoloConGiocatoriException("Attenzione! Tavolo ancora pieno. Prima rimuovi i giocatori");
		}
		repository.deleteById(idToRemove);

	}

	@Override
	public Tavolo findByDenominazione(String denominazione) {
		return repository.findByDenominazione(denominazione);
	}

	@Override
	public List<Tavolo> findByExample(Tavolo example) {
		return repository.findByExample(example);
	}

}
