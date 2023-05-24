package it.prova.pokeronline.dto;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import it.prova.pokeronline.model.Tavolo;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TavoloShowDTO {
	
	@JsonIgnore(value = true)
	private Long id;
	@NotBlank(message = "{denominazione.notblank}")
	private String denominazione;
	@Min(value = 0, message = "esperienza.message")
	private Integer esperienzaMin;
	@Min(value = 0, message = "credito.message")
	private Double creditoMin;
	private LocalDate dataCreazione;
	
	@JsonIgnore(value = true)
	private UtenteDTO utenteCreazione;
	
	private Set<GiocatoreDTO> giocatori = new HashSet<>();

	public TavoloShowDTO() {
		super();
	}

	public TavoloShowDTO(Long id, @NotBlank(message = "{denominazione.notblank}") String denominazione,
			@Min(value = 0, message = "esperienza.message") Integer esperienzaMin,
			@Min(value = 0, message = "credito.message") Double creditoMin) {
		super();
		this.id = id;
		this.denominazione = denominazione;
		this.esperienzaMin = esperienzaMin;
		this.creditoMin = creditoMin;
	}

	
	public TavoloShowDTO(Long id, @NotBlank(message = "{denominazione.notblank}") String denominazione,
			@Min(value = 0, message = "esperienza.message") Integer esperienzaMin,
			@Min(value = 0, message = "credito.message") Double creditoMin, LocalDate dataCreazione) {
		super();
		this.id = id;
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

	public Set<GiocatoreDTO> getGiocatori() {
		return giocatori;
	}

	public void setGiocatori(Set<GiocatoreDTO> giocatori) {
		this.giocatori = giocatori;
	}
	
	
	
	public LocalDate getDataCreazione() {
		return dataCreazione;
	}

	public void setDataCreazione(LocalDate dataCreazione) {
		this.dataCreazione = dataCreazione;
	}

	public Tavolo buildTavoloModel() {
		Tavolo result = new Tavolo(this.id, this.denominazione, this.esperienzaMin, this.creditoMin, this.dataCreazione);
		if (this.utenteCreazione != null) {
			result.setUtenteCreazione(this.utenteCreazione.buildUtenteModel(false));
		}
		return result;
	}
	
	
	public static TavoloShowDTO buildTavoloDTOFromModel(Tavolo tavoloModel, boolean includeGiocatori) {
		TavoloShowDTO result = new TavoloShowDTO(tavoloModel.getId(), tavoloModel.getDenominazione(), tavoloModel.getEsperienzaMin(), tavoloModel.getCreditoMin(),tavoloModel.getDataCreazione());
		if (includeGiocatori) {
			result.setGiocatori(GiocatoreDTO.createGiocatoreDTOSetFromModelSet(tavoloModel.getGiocatori()));
		}
		return result;
	}
	
	public static List<TavoloShowDTO> createTavoloShowDTOListFromModelList(List<Tavolo> modelListInput,boolean includeGiocatori) {
		return modelListInput.stream().map(tavoloEntity -> {
			TavoloShowDTO result = TavoloShowDTO.buildTavoloDTOFromModel(tavoloEntity, includeGiocatori);
			if (includeGiocatori)
				result.setGiocatori(GiocatoreDTO.createGiocatoreDTOSetFromModelSet(tavoloEntity.getGiocatori()));
			return result;
		}).collect(Collectors.toList());
		}
}