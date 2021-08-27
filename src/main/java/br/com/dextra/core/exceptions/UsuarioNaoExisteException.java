package br.com.dextra.core.exceptions;

public class UsuarioNaoExisteException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UsuarioNaoExisteException() {
		super("Usuário não existe");
	}
}
