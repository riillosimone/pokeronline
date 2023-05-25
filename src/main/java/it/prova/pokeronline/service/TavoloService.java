package it.prova.pokeronline.service;

import java.util.List;

import org.springframework.data.domain.Page;

import it.prova.pokeronline.model.Tavolo;

public interface TavoloService {
	
	public List<Tavolo> listAll(boolean eager);
	
	public Tavolo caricaSingoloElemento(Long id);
	
	public Tavolo caricaSingoloElementoEager(Long id);
	
	public Tavolo aggiorna(Tavolo tavoloInstance);
	
	public Tavolo inserisciNuovo(Tavolo tavoloInstance);
	
	public void rimuovi(Long idToRemove);
	
	public Tavolo findByDenominazione(String denominazione);
	
	public List<Tavolo> findByExample(Tavolo example);
	
//	public Page<Tavolo> findByExampleNativeWithPagination(Tavolo example, Integer pageNo, Integer pageSize,
//			String sortBy);
	
	public Page<Tavolo> findByExampleWithPagination(Tavolo example, Integer pageNo, Integer pageSize, String sortBy);
	
	public Tavolo siediti(Long idTavolo);
	
	public Tavolo alzati(Long idTavolo);
	
	public Tavolo lastGame();
	
	public Page<Tavolo> cercaTavoliWithPagination(Tavolo example, Integer pageNo, Integer pageSize, String sortBy);

}
