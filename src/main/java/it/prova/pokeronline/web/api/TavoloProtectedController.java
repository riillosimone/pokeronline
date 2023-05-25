package it.prova.pokeronline.web.api;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import it.prova.pokeronline.dto.TavoloDTOPerInsert;
import it.prova.pokeronline.model.Tavolo;
import it.prova.pokeronline.service.TavoloService;
import it.prova.pokeronline.web.api.exception.IdNotNullForInsertException;
import it.prova.pokeronline.web.api.exception.TavoloNotFoundException;

@RestController
@RequestMapping("api/protected/tavolo")
public class TavoloProtectedController {

	@Autowired
	private TavoloService tavoloService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED) 
	public TavoloDTOPerInsert createNew(@Valid @RequestBody TavoloDTOPerInsert tavoloInput) {
		if (tavoloInput.getId() != null) {
			throw new IdNotNullForInsertException("Non è ammesso fornire un id per la creazione");
		}
		Tavolo tavoloInserito = tavoloService.inserisciNuovo(tavoloInput.buildTavoloModel(true));
		return TavoloDTOPerInsert.buildTavoloDTOFromModel(tavoloInserito);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable(required = true) Long id) {
		tavoloService.rimuovi(id);
	}

	@PutMapping("/{id}")
	public TavoloDTOPerInsert update(@Valid @RequestBody TavoloDTOPerInsert tavoloInput,
			@PathVariable(required = true) Long id) {
		Tavolo tavolo = tavoloService.caricaSingoloElemento(id);	
		if (tavolo == null) {
			throw new TavoloNotFoundException("Tavolo not found con id: " + id);
		}
		tavoloInput.setId(id);
		Tavolo tavoloAggiornato = tavoloService.aggiorna(tavoloInput.buildTavoloModel(false));
		return TavoloDTOPerInsert.buildTavoloDTOFromModel(tavoloAggiornato);
	}

}
