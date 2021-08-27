package br.com.dextra.core;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import br.com.dextra.core.exceptions.UsuarioNaoExisteException;
import br.com.dextra.core.exceptions.UsuarioNaoPossuiIdadeMinimaException;
import br.com.dextra.core.models.CadastrarUsuarioCommand;
import br.com.dextra.core.models.ObterUsuarioQuery;
import br.com.dextra.core.models.UsuarioDto;
import br.com.dextra.core.ports.incoming.CadastrarUsuario;
import br.com.dextra.core.ports.incoming.ObterUsuario;
import br.com.dextra.core.ports.outgoing.UsuarioPersistence;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioFacade implements CadastrarUsuario, ObterUsuario {

	private static final Integer IDADE_MINIMA = 16;

	private final UsuarioPersistence usuarioPersistence;

	@Override
	public void executar(CadastrarUsuarioCommand command) {

		LocalDate dataNascimentoMaximo = LocalDate.now().minusYears(IDADE_MINIMA);
		if (command.getNascimento().isAfter(dataNascimentoMaximo)) {

			throw new UsuarioNaoPossuiIdadeMinimaException();
		}

		usuarioPersistence.cadastrarUsuario(command);
	}

	@Override
	public UsuarioDto executar(ObterUsuarioQuery query) {

		return usuarioPersistence.obterUsuarioPeloId(query.getId()).orElseThrow(UsuarioNaoExisteException::new);
	}
}