package br.com.dextra.app.models;

import java.time.LocalDate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.Getter;

@Getter
public class CadastrarUsuarioRequest {

	@Email(message = "Email preenchido é invalido")
	@NotEmpty(message = "O Email deve ser preenchido")
	private String email;
	
	@NotNull(message = "A data de nascimento deve ser preenchida")
	private LocalDate nascimento;
	
	@NotEmpty(message = "O tipo de usuário deve ser preenchido")
	@Pattern(regexp = "DEFAULT|PREMIUM", message = "Tipo de usuário informado é invalido")
	private String tipoUsuario;
}
