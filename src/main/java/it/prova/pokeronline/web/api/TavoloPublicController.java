package it.prova.pokeronline.web.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.prova.pokeronline.dto.TavoloShowDTO;
import it.prova.pokeronline.model.Tavolo;
import it.prova.pokeronline.service.TavoloService;
import it.prova.pokeronline.web.api.exception.TavoloNotFoundException;

@RestController
@RequestMapping("/api/tavolo")
public class TavoloPublicController {

	@Autowired
	private TavoloService tavoloService;
	
	@GetMapping
	public List<TavoloShowDTO> getAll() {
		return TavoloShowDTO.createTavoloShowDTOListFromModelList(tavoloService.listAll(true), true);
	}
	
	@GetMapping("/{id}")
	public TavoloShowDTO findById(@PathVariable(value = "id", required = true) Long id) {
		Tavolo tavolo = tavoloService.caricaSingoloElementoEager(id);
		
		if (tavolo == null) {
			throw new TavoloNotFoundException("Tavolo not found con id: " + id);
		}
		return TavoloShowDTO.buildTavoloDTOFromModel(tavolo, true);
	}
	
	@PostMapping("/search")
	public List<TavoloShowDTO> search(@RequestBody TavoloShowDTO example) {
		return TavoloShowDTO.createTavoloShowDTOListFromModelList(tavoloService.findByExample(example.buildTavoloModel()), false);
	}
	
	
}
