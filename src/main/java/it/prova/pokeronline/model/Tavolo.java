package it.prova.pokeronline.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tavolo")
public class Tavolo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	@Column(name = "denominazione")
	private String denominazione;
	@Column(name = "esperienzaMin")
	private Integer esperienzaMin;
	@Column(name = "creditoMin")
	private Double creditoMin;
	@Column(name = "dataCreazione")
	private LocalDate dataCreazione;

	@OneToMany(fetch = FetchType.LAZY)
	private Set<Utente> giocatori = new HashSet<>();

	@OneToOne(fetch = FetchType.LAZY)
	private Utente utenteCreazione;

	public Tavolo() {
		super();
	}

	public Tavolo(Long id, Integer esperienzaMin, Double creditoMin, LocalDate dataCreazione, Set<Utente> giocatori) {
		super();
		this.id = id;
		this.esperienzaMin = esperienzaMin;
		this.creditoMin = creditoMin;
		this.dataCreazione = dataCreazione;
		this.giocatori = giocatori;
	}

	
	public Tavolo(Long id, String denominazione, Integer esperienzaMin, Double creditoMin, LocalDate dataCreazione) {
		super();
		this.id = id;
		this.denominazione = denominazione;
		this.esperienzaMin = esperienzaMin;
		this.creditoMin = creditoMin;
		this.dataCreazione = dataCreazione;
	}

	public Tavolo(Long id, Integer esperienzaMin, Double creditoMin, LocalDate dataCreazione, Utente utenteCreazione) {
		super();
		this.id = id;
		this.esperienzaMin = esperienzaMin;
		this.creditoMin = creditoMin;
		this.dataCreazione = dataCreazione;
		this.utenteCreazione = utenteCreazione;
	}

	public Tavolo(Integer esperienzaMin, Double creditoMin, Utente utenteCreazione) {
		super();
		this.esperienzaMin = esperienzaMin;
		this.creditoMin = creditoMin;
		this.utenteCreazione = utenteCreazione;
	}

	public Tavolo(Integer esperienzaMin, Double creditoMin, LocalDate dataCreazione, Utente utenteCreazione) {
		super();
		this.esperienzaMin = esperienzaMin;
		this.creditoMin = creditoMin;
		this.dataCreazione = dataCreazione;
		this.utenteCreazione = utenteCreazione;
	}

	
	
	public String getDenominazione() {
		return denominazione;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getEsperienzaMin() {
		return esperienzaMin;
	}

	public void setEsperienzaMin(Integer esperienzaMin) {
		this.esperienzaMin = esperienzaMin;
	}

	public Double getCreditoMin() {
		return creditoMin;
	}

	public void setCreditoMin(Double creditoMin) {
		this.creditoMin = creditoMin;
	}

	public LocalDate getDataCreazione() {
		return dataCreazione;
	}

	public void setDataCreazione(LocalDate dataCreazione) {
		this.dataCreazione = dataCreazione;
	}

	public Set<Utente> getGiocatori() {
		return giocatori;
	}

	public void setGiocatori(Set<Utente> giocatori) {
		this.giocatori = giocatori;
	}

	public Utente getUtenteCreazione() {
		return utenteCreazione;
	}

	public void setUtenteCreazione(Utente utenteCreazione) {
		this.utenteCreazione = utenteCreazione;
	}

}
