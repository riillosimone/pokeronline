package it.prova.pokeronline.repository.tavolo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import it.prova.pokeronline.model.Tavolo;

public interface TavoloRepository extends PagingAndSortingRepository<Tavolo, Long>, JpaSpecificationExecutor<Tavolo>,CustomTavoloRepository{

	@Query("select t from Tavolo t left join fetch t.giocatori g where t.id =?1")
	Tavolo findByIdEager(Long id);
	
	@Query("select distinct t from Tavolo t left join fetch t.giocatori g")
	List<Tavolo> findAllEager();
	
	Tavolo findByDenominazione(String denominazione);
	
	
//	@Query(value = "SELECT t.* " + "FROM tavolo t inner join t.utenteCreazione u "
//			+ "WHERE ((:denominazione IS NULL OR LOWER(t.denominazione) LIKE %:denominazione%)  "
//			+ "AND (:esperienzamin IS NULL OR t.esperienzamin > :esperienzamin) "
//			+ "AND (:ciframin IS NULL OR t.ciframin >= :ciframin) "
//			+ "AND (:datacreazione IS NULL OR t.datacreazione >= :datacreazione)) "
//			+ "AND (:username IS NULL OR LOWER (u.username) like %:username%) "
//			
//			, countQuery = "SELECT t.* " + "FROM tavolo t "
//					+ "WHERE ((:denominazione IS NULL OR LOWER(t.denominazione) LIKE %:denominazione%)  "
//					+ "AND (:esperienzamin IS NULL OR t.esperienzamin > :esperienzamin) "
//					+ "AND (:ciframin IS NULL OR t.ciframin >= :ciframin) "
//					+ "AND (:datacreazione IS NULL OR t.datacreazione >= :datacreazione))"
//					, nativeQuery = true)
//	Page<Tavolo> findByExampleNativeWithPagination(@Param("denominazione") String denominazione,
//			@Param("esperienzamin") Integer esperienzaMin, @Param("ciframin") Double cifraMin,
//			@Param("datacreazione") LocalDate dataCreazione,@Param("username") String username, Pageable pageable);
}
