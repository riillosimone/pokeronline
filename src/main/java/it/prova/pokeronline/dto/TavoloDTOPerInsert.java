package it.prova.pokeronline.dto;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.stream.Collectors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import it.prova.pokeronline.model.Tavolo;
import it.prova.pokeronline.model.Utente;

public class TavoloDTOPerInsert {

	private Long id;

	@NotBlank(message = "{denominazione.notblank}")
	private String denominazione;

	@Min(value = 0, message = "esperienza.message")
	private Integer esperienzaMin;
	@Min(value = 0, message = "credito.message")
	private Double creditoMin;

	private LocalDate dataCreazione;

	private Long[] utentiIds;

	
	
	public TavoloDTOPerInsert() {
		super();
	}

	public TavoloDTOPerInsert(Long id, @NotBlank(message = "{denominazione.notblank}") String denominazione,
			@Min(0) Integer esperienzaMin, @Min(0) Double creditoMin, LocalDate dataCreazione) {
		super();
		this.id = id;
		this.denominazione = denominazione;
		this.esperienzaMin = esperienzaMin;
		this.creditoMin = creditoMin;
		this.dataCreazione = dataCreazione;
	}

	public TavoloDTOPerInsert(@NotBlank(message = "{denominazione.notblank}") String denominazione,
			@Min(0) Integer esperienzaMin, @Min(0) Double creditoMin, LocalDate dataCreazione) {
		super();
		this.denominazione = denominazione;
		this.esperienzaMin = esperienzaMin;
		this.creditoMin = creditoMin;
		this.dataCreazione = dataCreazione;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDenominazione() {
		return denominazione;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
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

	public Long[] getUtentiIds() {
		return utentiIds;
	}

	public void setUtentiIds(Long[] utentiIds) {
		this.utentiIds = utentiIds;
	}

	public Tavolo buildTavoloModel(boolean includeGiocatori) {
		Tavolo result = new Tavolo(this.id, this.denominazione, this.esperienzaMin, this.creditoMin,
				this.dataCreazione);
		if (includeGiocatori && utentiIds != null) {
			result.setGiocatori(
					Arrays.asList(utentiIds).stream().map(id -> new Utente(id)).collect(Collectors.toSet()));
		}
		return result;
	}

	public static TavoloDTOPerInsert buildTavoloDTOFromModel(Tavolo tavoloModel) {
		TavoloDTOPerInsert result = new TavoloDTOPerInsert(tavoloModel.getId(), tavoloModel.getDenominazione(),
				tavoloModel.getEsperienzaMin(), tavoloModel.getCreditoMin(), tavoloModel.getDataCreazione());
		if (!tavoloModel.getGiocatori().isEmpty()) {
			result.utentiIds = tavoloModel.getGiocatori().stream().map(u -> u.getId()).collect(Collectors.toList())
					.toArray(new Long[] {});
		}
		return result;
	}

}
