package br.com.dextra.core.exceptions;

public class UsuarioNaoPossuiIdadeMinimaException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UsuarioNaoPossuiIdadeMinimaException() {
		super("Usuário não possui idade minima para cadastro");
	}
}