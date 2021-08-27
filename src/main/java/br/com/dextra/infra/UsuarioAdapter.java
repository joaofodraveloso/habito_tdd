package br.com.dextra.infra;

import java.util.Optional;

import org.springframework.stereotype.Component;

import br.com.dextra.core.models.CadastrarUsuarioCommand;
import br.com.dextra.core.models.UsuarioDto;
import br.com.dextra.core.ports.outgoing.UsuarioPersistence;
import br.com.dextra.infra.entities.UsuarioEntity;
import br.com.dextra.infra.repos.UsuarioRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UsuarioAdapter implements UsuarioPersistence {

	private final UsuarioRepository usuarioRepository;

	@Override
	public UsuarioDto cadastrarUsuario(CadastrarUsuarioCommand command) {

		var usuarioEntity = usuarioRepository
				.save(new UsuarioEntity(command.getEmail(), command.getNascimento(), command.getTipoUsuario()));

		return mapper(usuarioEntity);
	}

	@Override
	public Optional<UsuarioDto> obterUsuarioPeloId(Long id) {

		return usuarioRepository.findById(id).map(this::mapper);
	}
	
	private UsuarioDto mapper(UsuarioEntity usuarioEntity) {
		
		return new UsuarioDto(usuarioEntity.getId(), usuarioEntity.getEmail(), usuarioEntity.getNascimento(),
				usuarioEntity.getTipoUsuario());
	}

}
