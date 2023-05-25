package it.prova.pokeronline.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Predicate;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.prova.pokeronline.model.Tavolo;
import it.prova.pokeronline.model.Utente;
import it.prova.pokeronline.repository.tavolo.TavoloRepository;
import it.prova.pokeronline.web.api.exception.CreditoMinimoInsufficienteException;
import it.prova.pokeronline.web.api.exception.EsperienzaMinimaInsufficienteException;
import it.prova.pokeronline.web.api.exception.TavoloConGiocatoriException;
import it.prova.pokeronline.web.api.exception.TavoloNotFoundException;
import it.prova.pokeronline.web.api.exception.UtenteGiocatoreGiaSedutoException;
import it.prova.pokeronline.web.api.exception.UtenteNonAutorizzatoException;
import it.prova.pokeronline.web.api.exception.UtenteNonSedutoException;

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

		String username = SecurityContextHolder.getContext().getAuthentication().getName();

		// estraggo le info dal principal
		Utente utenteLoggato = utenteService.findByUsername(username);
		if (!utenteLoggato.isAdmin() && utenteLoggato != tavoloInstance.getUtenteCreazione()) {
			throw new UtenteNonAutorizzatoException("Non sei autorizzato ad eseguire questa operazione");
		}

		if (!tavoloInstance.getGiocatori().isEmpty()) {
			throw new TavoloConGiocatoriException("Attenzione! Tavolo ancora pieno. Prima rimuovi i giocatori");
		}

		if (tavoloInstance.getDataCreazione() == null) {
			tavoloInstance.setDataCreazione(LocalDate.now());
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
		Tavolo tavoloInstance = this.caricaSingoloElementoEager(idToRemove);

		String username = SecurityContextHolder.getContext().getAuthentication().getName();

		// estraggo le info dal principal
		Utente utenteLoggato = utenteService.findByUsername(username);
		if (!utenteLoggato.isAdmin() && utenteLoggato != tavoloInstance.getUtenteCreazione()) {
			throw new UtenteNonAutorizzatoException("Non sei autorizzato ad eseguire questa operazione");
		}

		if (!tavoloInstance.getGiocatori().isEmpty()) {
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

	public Page<Tavolo> findByExampleWithPagination(Tavolo example, Integer pageNo, Integer pageSize, String sortBy) {
		Specification<Tavolo> specificationCriteria = (root, query, cb) -> {

			List<Predicate> predicates = new ArrayList<Predicate>();

			if (StringUtils.isNotEmpty(example.getDenominazione()))
				predicates.add(cb.like(cb.upper(root.get("denominazione")),
						"%" + example.getDenominazione().toUpperCase() + "%"));

			if (example.getCreditoMin() != null)
				predicates.add(cb.greaterThanOrEqualTo(root.get("creditoMin"), example.getCreditoMin()));

			if (example.getEsperienzaMin() != null)
				predicates.add(cb.greaterThanOrEqualTo(root.get("esperienzaMin"), example.getEsperienzaMin()));

			if (example.getUtenteCreazione() != null
					&& StringUtils.isNotEmpty(example.getUtenteCreazione().getUsername()))
				predicates.add(cb.equal(root.join("utenteCreazione").get("username"),
						example.getUtenteCreazione().getUsername()));

			if (example.getDataCreazione() != null)
				predicates.add(cb.greaterThanOrEqualTo(root.get("dataCreazione"), example.getDataCreazione()));

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

//	@Override
//	@Transactional(readOnly = true)
//	public Page<Tavolo> findByExampleNativeWithPagination(Tavolo example, Integer pageNo, Integer pageSize,
//			String sortBy) {
//
//		return repository.findByExampleNativeWithPagination(example.getDenominazione(), example.getEsperienzaMin(),
//				example.getCreditoMin(), example.getDataCreazione(), example.getUtenteCreazione().getUsername(), PageRequest.of(pageNo, pageSize, Sort.by(sortBy)));
//	}

	@Override
	@Transactional
	public Tavolo siediti(Long idTavolo) {

		Tavolo tavolo = this.caricaSingoloElementoEager(idTavolo);
		if (tavolo == null) {
			throw new TavoloNotFoundException("Il tavolo che stai cercando non esiste");
		}

		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		// estraggo le info dal principal
		Utente utenteLoggato = utenteService.findByUsername(username);

		if (utenteLoggato.getCreditoAccumulato() == null) {
			utenteLoggato.setCreditoAccumulato(0d);
		}
		if (utenteLoggato.getCreditoAccumulato() < tavolo.getCreditoMin()) {
			throw new CreditoMinimoInsufficienteException("Non hai credito sufficiente per sederti a questo tavolo");
		}
		if (utenteLoggato.getEsperienzaAccumulata() == null) {
			utenteLoggato.setEsperienzaAccumulata(0);
		}
		if (utenteLoggato.getEsperienzaAccumulata() < tavolo.getEsperienzaMin()) {
			throw new EsperienzaMinimaInsufficienteException(
					"Non hai abbastanza esperienza per sederti a questo tavolo");
		}
		if (tavolo.getGiocatori().contains(utenteLoggato)) {
			throw new UtenteGiocatoreGiaSedutoException("Attenzione! Sei già seduto a questo tavolo");
		}

		List<Tavolo> listaTavoli = this.listAll(true);
		for (Tavolo tavoloItem : listaTavoli) {
			if (tavoloItem.getGiocatori().contains(utenteLoggato)) {
				throw new UtenteGiocatoreGiaSedutoException("Attenzione! Sei già seduto ad un altro tavolo");
			}
		}

		tavolo.getGiocatori().add(utenteLoggato);
		return tavolo;

	}

	@Override
	@Transactional
	public Tavolo alzati(Long idTavolo) {
		Tavolo tavolo = this.caricaSingoloElementoEager(idTavolo);
		if (tavolo == null) {
			throw new TavoloNotFoundException("Il tavolo che stai cercando non esiste");
		}
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		// estraggo le info dal principal
		Utente utenteLoggato = utenteService.findByUsername(username);
		if (!tavolo.getGiocatori().contains(utenteLoggato)) {
			throw new UtenteNonSedutoException("Attenzione! Non sei seduto a questo tavolo");
		}
		tavolo.getGiocatori().remove(utenteLoggato);
		return tavolo;
	}

	@Override
	public Tavolo lastGame() {
		
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Utente utenteInSessione = utenteService.findByUsername(username);
		
		List<Tavolo> tavoliPresenti = (List<Tavolo>) repository.findAll();
		
		for (Tavolo tavoloItem : tavoliPresenti) {
			if(tavoloItem.getGiocatori().contains(utenteInSessione))
				return tavoloItem;
		}
		
		throw new UtenteNonSedutoException("Attenzione! Non sei seduto ad alcun tavolo.");
	}
	
	

	public Page<Tavolo> cercaTavoliWithPagination(Tavolo example, Integer pageNo, Integer pageSize, String sortBy) {
		Specification<Tavolo> specificationCriteria = (root, query, cb) -> {

			String username = SecurityContextHolder.getContext().getAuthentication().getName();
			Utente utenteInSessione = utenteService.findByUsername(username);
			
			List<Predicate> predicates = new ArrayList<Predicate>();

			if (StringUtils.isNotEmpty(example.getDenominazione()))
				predicates.add(cb.like(cb.upper(root.get("denominazione")),
						"%" + example.getDenominazione().toUpperCase() + "%"));

			example.setCreditoMin(utenteInSessione.getCreditoAccumulato());
				predicates.add(cb.lessThanOrEqualTo(root.get("creditoMin"), example.getCreditoMin()));

			example.setEsperienzaMin(utenteInSessione.getEsperienzaAccumulata());;
				predicates.add(cb.lessThanOrEqualTo(root.get("esperienzaMin"), example.getEsperienzaMin()));

			if (example.getUtenteCreazione() != null
					&& StringUtils.isNotEmpty(example.getUtenteCreazione().getUsername()))
				predicates.add(cb.equal(root.join("utenteCreazione").get("username"),
						example.getUtenteCreazione().getUsername()));

			if (example.getDataCreazione() != null)
				predicates.add(cb.greaterThanOrEqualTo(root.get("dataCreazione"), example.getDataCreazione()));

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
}
