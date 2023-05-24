package it.prova.pokeronline.web.api.exception;

public class UtenteNonSedutoException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public UtenteNonSedutoException(String message) {
		super(message);
	}
}
