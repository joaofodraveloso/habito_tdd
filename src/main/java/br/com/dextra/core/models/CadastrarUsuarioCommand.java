package br.com.dextra.core.models;

import java.time.LocalDate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CadastrarUsuarioCommand {

	private final String email;
	private final LocalDate nascimento;
	private final String tipoUsuario;
}
