package br.com.dextra.infra;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import br.com.dextra.core.models.CadastrarUsuarioCommand;
import br.com.dextra.core.models.UsuarioDto;
import br.com.dextra.infra.entities.UsuarioEntity;
import br.com.dextra.infra.repos.UsuarioRepository;

@DataJpaTest
@DisplayName("Infra: Usuário")
class UsuarioAdapterTest {

	@Autowired
	private TestEntityManager testEntityManager;

	@Autowired
	private UsuarioRepository usuarioRepository;

	private UsuarioAdapter usuarioAdapter;

	@BeforeEach
	void setup() {

		this.usuarioAdapter = new UsuarioAdapter(usuarioRepository);
	}

	@Test
	@DisplayName("Tenta cadastrar usuário")
	void cadastrarUsuario() {

		CadastrarUsuarioCommand command = new CadastrarUsuarioCommand("email@provedor.com",
				LocalDate.now().minusYears(15), "PREMIUM");

		UsuarioDto usuarioDto = this.usuarioAdapter.cadastrarUsuario(command);

		assertEquals(command.getEmail(), usuarioDto.getEmail());
		assertEquals(command.getNascimento(), usuarioDto.getNascimento());
		assertEquals(command.getTipoUsuario(), usuarioDto.getTipoUsuario());
	}

	@Test
	@DisplayName("Tenta obter usuário pelo id")
	void obterUsuarioPeloId() {

		UsuarioEntity usuarioEntity = testEntityManager
				.persistAndFlush(new UsuarioEntity("email@provedor.com", LocalDate.now().minusYears(18), "PREMIUM"));
		
		Optional<UsuarioDto> resultado = this.usuarioAdapter.obterUsuarioPeloId(usuarioEntity.getId());
		
		assertTrue(resultado.isPresent());
		
		UsuarioDto usuarioDto = resultado.orElseThrow();
		
		assertEquals(usuarioEntity.getId(), usuarioDto.getId());
		assertEquals(usuarioEntity.getEmail(), usuarioDto.getEmail());
		assertEquals(usuarioEntity.getNascimento(), usuarioDto.getNascimento());
		assertEquals(usuarioEntity.getTipoUsuario(), usuarioDto.getTipoUsuario());
	}
	
	@Test
	@DisplayName("Tenta obter usuário pelo id que não existe")
	void obterUsuarioPeloIdQueNaoExiste() {
		
		Optional<UsuarioDto> resultado = this.usuarioAdapter.obterUsuarioPeloId(999L);
		
		assertTrue(resultado.isEmpty());
	}
}
