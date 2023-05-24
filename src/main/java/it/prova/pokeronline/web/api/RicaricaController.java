package it.prova.pokeronline.web.api;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.prova.pokeronline.dto.UtenteShowDTO;
import it.prova.pokeronline.model.Utente;
import it.prova.pokeronline.service.UtenteService;
import it.prova.pokeronline.web.api.exception.RicaricaNonAndataABuonFineException;

@RestController
@RequestMapping("/api/ricarica")
public class RicaricaController {

	@Autowired
	private UtenteService utenteService;
	
	@PostMapping
	public UtenteShowDTO ricarica(@RequestBody Map<String, Double> rawValue) {
		if (rawValue.get("ricarica") == null || rawValue.get("ricarica") <= 0) {
			throw new RicaricaNonAndataABuonFineException("La ricarica non è andata a buon fine");
		}
		Utente utente = utenteService.ricaricaCredito(rawValue.get("ricarica"));
		return UtenteShowDTO.buildUtenteDTOFromModel(utente);
	}
	
	
}
