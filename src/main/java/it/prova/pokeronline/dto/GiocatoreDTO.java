package it.prova.pokeronline.dto;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import it.prova.pokeronline.model.Utente;

public class GiocatoreDTO {

	@JsonIgnore(value = true)
	private Long id;

	@NotBlank(message = "{username.notblank}")
	@Size(min = 3, max = 15, message = "Il valore inserito '${validatedValue}' deve essere lungo tra {min} e {max} caratteri")
	private String username;

	@NotBlank(message = "{nome.notblank}")
	private String fullName;

	@Min(0)
	private Integer esperienzaAccumulata;
	@Min(0)
	private Double creditoAccumulato;

	public GiocatoreDTO(Long id,
			@NotBlank(message = "{username.notblank}") @Size(min = 3, max = 15, message = "Il valore inserito '${validatedValue}' deve essere lungo tra {min} e {max} caratteri") String username,
			@Min(0) Integer esperienzaAccumulata, @Min(0) Double creditoAccumulato) {
		super();
		this.id = id;
		this.username = username;
		this.esperienzaAccumulata = esperienzaAccumulata;
		this.creditoAccumulato = creditoAccumulato;
	}

	public GiocatoreDTO(
			@NotBlank(message = "{username.notblank}") @Size(min = 3, max = 15, message = "Il valore inserito '${validatedValue}' deve essere lungo tra {min} e {max} caratteri") String username,
			@NotBlank(message = "{nome.notblank}") String nome,
			@NotBlank(message = "{cognome.notblank}") String cognome) {
		super();
		this.username = username;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Integer getEsperienzaAccumulata() {
		return esperienzaAccumulata;
	}

	public void setEsperienzaAccumulata(Integer esperienzaAccumulata) {
		this.esperienzaAccumulata = esperienzaAccumulata;
	}

	public Double getCreditoAccumulato() {
		return creditoAccumulato;
	}

	public void setCreditoAccumulato(Double creditoAccumulato) {
		this.creditoAccumulato = creditoAccumulato;
	}

	public static GiocatoreDTO buildGiocatoreDTOFromUtenteModel(Utente utenteModel) {
		GiocatoreDTO result = new GiocatoreDTO(utenteModel.getId(), utenteModel.getUsername(),
				utenteModel.getEsperienzaAccumulata(), utenteModel.getCreditoAccumulato());
		result.setFullName(utenteModel.getNome() + " " + utenteModel.getCognome());

		return result;
	}

	public static List<GiocatoreDTO> createGiocatoreDTOListFromModelList(List<Utente> modelListInput) {
		return modelListInput.stream().map(utenteEntity -> {
			return GiocatoreDTO.buildGiocatoreDTOFromUtenteModel(utenteEntity);
		}).collect(Collectors.toList());
	}

	public static Set<GiocatoreDTO> createGiocatoreDTOSetFromModelSet(Set<Utente> modelListInput) {
		return modelListInput.stream().map(utenteEntity -> {
			return GiocatoreDTO.buildGiocatoreDTOFromUtenteModel(utenteEntity);
		}).collect(Collectors.toSet());
	}

}
