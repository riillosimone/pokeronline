package it.prova.pokeronline.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ruolo")
public class Ruolo {
	
	public static final String ROLE_ADMIN = "ROLE_ADMIN";
	public static final String ROLE_CLASSIC_PLAYER = "ROLE_CLASSIC_PLAYER";
	public static final String ROLE_SPECIAL_PLAYER = "ROLE_SPECIAL_PLAYER";
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	@Column(name = "descrizione")
	private String descrizione;
	@Column(name = "codice")
	private String codice;
	public Ruolo() {
		super();
	}
	public Ruolo(Long id) {
		super();
		this.id = id;
	}
	public Ruolo(String descrizione, String codice) {
		super();
		this.descrizione = descrizione;
		this.codice = codice;
	}
	public Ruolo(Long id, String descrizione, String codice) {
		super();
		this.id = id;
		this.descrizione = descrizione;
		this.codice = codice;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public String getCodice() {
		return codice;
	}
	public void setCodice(String codice) {
		this.codice = codice;
	}
	public static String getRoleAdmin() {
		return ROLE_ADMIN;
	}
	public static String getRoleClassicPlayer() {
		return ROLE_CLASSIC_PLAYER;
	}
	public static String getRoleSpecialPlayer() {
		return ROLE_SPECIAL_PLAYER;
	}
	
	
	

}
