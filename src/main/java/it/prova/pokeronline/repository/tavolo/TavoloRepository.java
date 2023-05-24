package it.prova.pokeronline.repository.tavolo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.prova.pokeronline.model.Tavolo;

public interface TavoloRepository extends CrudRepository<Tavolo, Long>,CustomTavoloRepository{

	@Query("select t from Tavolo t left join fetch t.giocatori g where t.id =?1")
	Tavolo findByIdEager(Long id);
	
	@Query("select distinct t from Tavolo t left join fetch t.giocatori g")
	List<Tavolo> findAllEager();
	
	Tavolo findByDenominazione(String denominazione);
}
