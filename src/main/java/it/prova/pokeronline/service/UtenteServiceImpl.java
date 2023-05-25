package it.prova.pokeronline.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Predicate;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.prova.pokeronline.model.StatoUtente;
import it.prova.pokeronline.model.Tavolo;
import it.prova.pokeronline.model.Utente;
import it.prova.pokeronline.repository.utente.UtenteRepository;
import it.prova.pokeronline.web.api.exception.CreditoMinimoInsufficienteException;
import it.prova.pokeronline.web.api.exception.TavoloNotFoundException;
import it.prova.pokeronline.web.api.exception.UtenteNonSedutoException;

@Service
@Transactional(readOnly = true)
public class UtenteServiceImpl implements UtenteService {

	@Autowired
	private UtenteRepository repository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	@Lazy
	private TavoloService tavoloService;

	@Override
	public List<Utente> listAllUtenti() {

		return (List<Utente>) repository.findAll();
	}

	@Override
	public Utente caricaSingoloUtente(Long id) {
		return repository.findById(id).orElse(null);
	}

	@Override
	public Utente caricaSingoloUtenteConRuoli(Long id) {
		return repository.findByIdConRuoli(id).orElse(null);
	}

	@Override
	@Transactional
	public Utente aggiorna(Utente utenteInstance) {
		// deve aggiornare solo nome, cognome, username, ruoli
		Utente utenteReloaded = repository.findById(utenteInstance.getId()).orElse(null);
		if (utenteReloaded == null)
			throw new RuntimeException("Elemento non trovato");
		utenteReloaded.setNome(utenteInstance.getNome());
		utenteReloaded.setCognome(utenteInstance.getCognome());
		utenteReloaded.setUsername(utenteInstance.getUsername());
		utenteReloaded.setRuoli(utenteInstance.getRuoli());
		return repository.save(utenteReloaded);

	}

	@Override
	@Transactional
	public Utente inserisciNuovo(Utente utenteInstance) {
		utenteInstance.setStato(StatoUtente.CREATO);
		utenteInstance.setPassword(passwordEncoder.encode(utenteInstance.getPassword()));
		utenteInstance.setDataRegistrazione(LocalDate.now());
		return repository.save(utenteInstance);

	}

	@Override
	@Transactional
	public void rimuovi(Long idToRemove) {
		repository.deleteById(idToRemove);

	}

	public Page<Utente> findByExampleWithPagination(Utente example, Integer pageNo, Integer pageSize, String sortBy) {
		Specification<Utente> specificationCriteria = (root, query, cb) -> {

			List<Predicate> predicates = new ArrayList<Predicate>();

			if (StringUtils.isNotEmpty(example.getNome()))
				predicates.add(cb.like(cb.upper(root.get("nome")),
						"%" + example.getNome().toUpperCase() + "%"));
			if (StringUtils.isNotEmpty(example.getCognome()))
				predicates.add(cb.like(cb.upper(root.get("cognome")),
						"%" + example.getCognome().toUpperCase() + "%"));

			if (StringUtils.isNotEmpty(example.getUsername()))
				predicates.add(cb.like(cb.upper(root.get("username")),
						"%" + example.getUsername().toUpperCase() + "%"));
			
			if (StringUtils.isNotEmpty(example.getEmail()))
				predicates.add(cb.like(cb.upper(root.get("email")),
						"%" + example.getEmail().toUpperCase() + "%"));
			
			if (example.getDataRegistrazione() != null)
				predicates.add(cb.greaterThanOrEqualTo(root.get("dataRegistrazione"), example.getDataRegistrazione()));
			
			if (example.getCreditoAccumulato() != null)
				predicates.add(cb.greaterThanOrEqualTo(root.get("creditoAccumulato"), example.getCreditoAccumulato()));

			if (example.getEsperienzaAccumulata() != null)
				predicates.add(cb.greaterThanOrEqualTo(root.get("esperienzaAccumulata"), example.getEsperienzaAccumulata()));



			return cb.and(predicates.toArray(new Predicate[predicates.size()]));
		};

		Pageable paging = null;
		// se non passo parametri di paginazione non ne tengo conto
		if (pageSize == null || pageSize < 10)
			paging = Pageable.unpaged();
		else
			paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

		return repository.findAll(specificationCriteria, paging);
	}

	@Override
	public Utente findByUsernameAndPassword(String username, String password) {
		return repository.findByUsernameAndPassword(username, password);
	}

	@Override
	public Utente eseguiAccesso(String username, String password) {
		return repository.findByUsernameAndPasswordAndStato(username, password, StatoUtente.ATTIVO);
	}

	@Override
	@Transactional
	public Utente changeUserAbilitation(Long utenteInstanceId) {
		Utente utenteInstance = caricaSingoloUtente(utenteInstanceId);
		if (utenteInstance == null)
			throw new RuntimeException("Elemento non trovato.");

		if (utenteInstance.getStato() == null || utenteInstance.getStato().equals(StatoUtente.CREATO))
			utenteInstance.setStato(StatoUtente.ATTIVO);
		else if (utenteInstance.getStato().equals(StatoUtente.ATTIVO))
			utenteInstance.setStato(StatoUtente.DISABILITATO);
		else if (utenteInstance.getStato().equals(StatoUtente.DISABILITATO))
			utenteInstance.setStato(StatoUtente.ATTIVO);

		return utenteInstance;
	}

	@Override
	public Utente findByUsername(String username) {
		return repository.findByUsername(username).orElse(null);
	}

	@Override
	@Transactional
	public Utente ricaricaCredito(Double ricarica) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Utente utenteLoggato = this.findByUsername(username);
		if (utenteLoggato.getCreditoAccumulato() == null) {
			utenteLoggato.setCreditoAccumulato(0d);
		}
		utenteLoggato.setCreditoAccumulato(utenteLoggato.getCreditoAccumulato() + ricarica);
		return utenteLoggato;
	}

	@Transactional
	public Utente gioca(Long idTavolo) {
		Tavolo tavolo = tavoloService.caricaSingoloElementoEager(idTavolo);
		if (tavolo == null) {
			throw new TavoloNotFoundException("Il tavolo che stai cercando non esiste");
		}
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		// estraggo le info dal principal
		Utente utenteLoggato = this.findByUsername(username);

		if (!tavolo.getGiocatori().contains(utenteLoggato)) {
			throw new UtenteNonSedutoException("Attenzione! Prima di poter giocare, siediti al tavolo.");
		}

//		List<Tavolo> listaTavoli = tavoloService.listAll(true);
//		for (Tavolo tavoloItem : listaTavoli) {
//			if (tavoloItem.getGiocatori().contains(utenteLoggato)) {
//				throw new UtenteGiocatoreGiaSedutoException("Attenzione! Sei già seduto ad un altro tavolo");
//			}
//		}

		if (utenteLoggato.getCreditoAccumulato() == null || utenteLoggato.getCreditoAccumulato() <= 0d) {
			throw new CreditoMinimoInsufficienteException(
					"Attenzione! Credito insufficiente. Prima di poter giocare, ricarica il tuo credito");
		}

		double segno = Math.random();
		if (segno < 0.5) {
			segno = (-1);
		}
		int somma = (int) (Math.random() * 1000);
		int totale = (int) (segno * somma);

		Double nuovoCredito = utenteLoggato.getCreditoAccumulato() + totale;
		if (nuovoCredito < 0) {
			nuovoCredito = 0d;
		}

		utenteLoggato.setCreditoAccumulato(nuovoCredito);
		utenteLoggato.setEsperienzaAccumulata(utenteLoggato.getEsperienzaAccumulata() + 2);
		return utenteLoggato;
	}

}
