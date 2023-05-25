package it.prova.pokeronline.web.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.prova.pokeronline.dto.TavoloShowDTO;
import it.prova.pokeronline.dto.UtenteShowDTO;
import it.prova.pokeronline.model.Tavolo;
import it.prova.pokeronline.service.TavoloService;
import it.prova.pokeronline.service.UtenteService;
import it.prova.pokeronline.web.api.exception.TavoloNotFoundException;

@RestController
@RequestMapping("/api/tavolo")
public class TavoloPublicController {

	@Autowired
	private TavoloService tavoloService;
	@Autowired
	private UtenteService utenteService;

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
		
		
		return TavoloShowDTO.createTavoloShowDTOListFromModelList(
				tavoloService.findByExample(example.buildTavoloModel(true)), false);
	}

	@PostMapping("/searchWithPagination")
	public ResponseEntity<Page<TavoloShowDTO>> searchPaginated(@RequestBody TavoloShowDTO example,
			@RequestParam(defaultValue = "0") Integer pageNo, @RequestParam(defaultValue = "0") Integer pageSize,
			@RequestParam(defaultValue = "id") String sortBy) {
		Page<Tavolo> entityPageResults = tavoloService.findByExampleWithPagination(example.buildTavoloModel(true),
				pageNo, pageSize, sortBy);

		return new ResponseEntity<Page<TavoloShowDTO>>(TavoloShowDTO.fromModelPageToDTOPage(entityPageResults),
				HttpStatus.OK);
	}

//	@PostMapping("/searchNativeWithPagination")
//	public ResponseEntity<Page<TavoloShowDTO>> searchNativePaginated(@RequestBody TavoloShowDTO example,
//			@RequestParam(defaultValue = "0") Integer pageNo, @RequestParam(defaultValue = "0") Integer pageSize,
//			@RequestParam(defaultValue = "id") String sortBy) {
//
//		Page<Tavolo> entityPageResults = tavoloService.findByExampleNativeWithPagination(example.buildTavoloModel(true),
//				pageNo, pageSize, sortBy);
//
//		return new ResponseEntity<Page<TavoloShowDTO>>(TavoloShowDTO.fromModelPageToDTOPage(entityPageResults),
//				HttpStatus.OK);
//	}

	@GetMapping("/siediti/{id}")
	public TavoloShowDTO siediti(@PathVariable(value = "id", required = true) Long id) {
		return TavoloShowDTO.buildTavoloDTOFromModel(tavoloService.siediti(id), true);
	}

	@GetMapping("/alzati/{id}")
	public TavoloShowDTO alzati(@PathVariable(value = "id", required = true) Long id) {
		return TavoloShowDTO.buildTavoloDTOFromModel(tavoloService.alzati(id), true);
	}

	@GetMapping("/gioca/{id}")
	public UtenteShowDTO gioca(@PathVariable(value = "id", required = true) Long id) {
		return UtenteShowDTO.buildUtenteDTOFromModel(utenteService.gioca(id));
	}

}
