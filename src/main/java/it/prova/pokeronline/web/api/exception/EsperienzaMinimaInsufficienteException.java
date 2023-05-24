package it.prova.pokeronline.web.api.exception;

public class EsperienzaMinimaInsufficienteException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public EsperienzaMinimaInsufficienteException(String message) {
		super(message);
	}
}
