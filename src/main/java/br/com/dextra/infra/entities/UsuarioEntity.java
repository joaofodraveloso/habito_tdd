package br.com.dextra.infra.entities;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Getter;

@Getter
@Entity
public class UsuarioEntity {

	@Id
	@GeneratedValue
	private Long id;
	private String email;
	private LocalDate nascimento;
	private String tipoUsuario;
	
	public UsuarioEntity(String email, LocalDate nascimento, String tipoUsuario) {
		this.email = email;
		this.nascimento = nascimento;
		this.tipoUsuario = tipoUsuario;
	}
}
