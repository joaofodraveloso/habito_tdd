package br.com.dextra.core.ports.incoming;

import br.com.dextra.core.models.CadastrarUsuarioCommand;

public interface CadastrarUsuario {

	void executar(CadastrarUsuarioCommand command);

}
