package br.com.dextra.core.exceptions;

public class UsuarioNaoExisteException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UsuarioNaoExisteException(String mensagem) {
		super(mensagem);
	}
}
