package br.com.dextra.core.ports.outgoing;

import java.util.Optional;

import br.com.dextra.core.models.CadastrarUsuarioCommand;
import br.com.dextra.core.models.UsuarioDto;

public interface UsuarioPersistence {

	UsuarioDto cadastrarUsuario(CadastrarUsuarioCommand command);

	Optional<UsuarioDto> obterUsuarioPeloId(Long id);
}
