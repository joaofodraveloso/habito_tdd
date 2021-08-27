package br.com.dextra.core.ports.incoming;

import br.com.dextra.core.models.ObterUsuarioQuery;
import br.com.dextra.core.models.UsuarioDto;

public interface ObterUsuario {

	UsuarioDto executar(ObterUsuarioQuery capture);
}
