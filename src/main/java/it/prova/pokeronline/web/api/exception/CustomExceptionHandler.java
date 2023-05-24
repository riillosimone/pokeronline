package it.prova.pokeronline.web.api.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;



@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler{

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("status", status.value());

		// Get all errors
		List<String> errors = ex.getBindingResult().getFieldErrors().stream().map(x -> x.getDefaultMessage())
				.collect(Collectors.toList());

		body.put("errors", errors);

		return new ResponseEntity<>(body, headers, status);
	}
	
	@ExceptionHandler(IdNotNullForInsertException.class)
	public ResponseEntity<Object> handleIdNotNullForInsertException(IdNotNullForInsertException ex,
			WebRequest request) {

		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("message", ex.getMessage());
		body.put("status", HttpStatus.UNPROCESSABLE_ENTITY);

		return new ResponseEntity<>(body, HttpStatus.UNPROCESSABLE_ENTITY);
	}
	
	@ExceptionHandler(TavoloConGiocatoriException.class)
	public ResponseEntity<Object> handleTavoloConGiocatoriException(TavoloConGiocatoriException ex,
			WebRequest request) {

		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("message", ex.getMessage());
		body.put("status", HttpStatus.UNPROCESSABLE_ENTITY);

		return new ResponseEntity<>(body, HttpStatus.UNPROCESSABLE_ENTITY);
	}
	
	
	@ExceptionHandler(TavoloNotFoundException.class)
	public ResponseEntity<Object> handleTavoloNotFoundException(TavoloNotFoundException ex,
			WebRequest request) {

		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("message", ex.getMessage());
		body.put("status", HttpStatus.NOT_FOUND);

		return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
	}
	@ExceptionHandler(UtenteNonAutorizzatoException.class)
	public ResponseEntity<Object> handleUtenteNonAutorizzatoException(UtenteNonAutorizzatoException ex,
			WebRequest request) {

		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("message", ex.getMessage());
		body.put("status", HttpStatus.FORBIDDEN);

		return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler(RicaricaNonAndataABuonFineException.class)
	public ResponseEntity<Object> handleRicaricaNonAndataABuonFineException(RicaricaNonAndataABuonFineException ex,
			WebRequest request) {

		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("message", ex.getMessage());
		body.put("status", HttpStatus.UNPROCESSABLE_ENTITY);

		return new ResponseEntity<>(body, HttpStatus.UNPROCESSABLE_ENTITY);
	}
	
	@ExceptionHandler(UtenteGiocatoreGiaSedutoException.class)
	public ResponseEntity<Object> handleUtenteGiocatoreGiaSedutoException(UtenteGiocatoreGiaSedutoException ex,
			WebRequest request) {

		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("message", ex.getMessage());
		body.put("status", HttpStatus.UNPROCESSABLE_ENTITY);

		return new ResponseEntity<>(body, HttpStatus.UNPROCESSABLE_ENTITY);
	}
	
	@ExceptionHandler(CreditoMinimoInsufficienteException.class)
	public ResponseEntity<Object> handleCreditoMinimoInsufficienteException(CreditoMinimoInsufficienteException ex,
			WebRequest request) {

		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("message", ex.getMessage());
		body.put("status", HttpStatus.UNPROCESSABLE_ENTITY);

		return new ResponseEntity<>(body, HttpStatus.UNPROCESSABLE_ENTITY);
	}
	
	@ExceptionHandler(EsperienzaMinimaInsufficienteException.class)
	public ResponseEntity<Object> handleEsperienzaMinimaInsufficienteException(EsperienzaMinimaInsufficienteException ex,
			WebRequest request) {

		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("message", ex.getMessage());
		body.put("status", HttpStatus.UNPROCESSABLE_ENTITY);

		return new ResponseEntity<>(body, HttpStatus.UNPROCESSABLE_ENTITY);
	}
	
	@ExceptionHandler(UtenteNonSedutoException.class)
	public ResponseEntity<Object> handleUtenteNonSedutoException(UtenteNonSedutoException ex,
			WebRequest request) {

		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("message", ex.getMessage());
		body.put("status", HttpStatus.UNPROCESSABLE_ENTITY);

		return new ResponseEntity<>(body, HttpStatus.UNPROCESSABLE_ENTITY);
	}
}
