package br.com.dextra.core;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.dextra.core.exceptions.UsuarioNaoExisteException;
import br.com.dextra.core.exceptions.UsuarioNaoPossuiIdadeMinimaException;
import br.com.dextra.core.models.CadastrarUsuarioCommand;
import br.com.dextra.core.models.ObterUsuarioQuery;
import br.com.dextra.core.models.UsuarioDto;
import br.com.dextra.core.ports.outgoing.UsuarioPersisence;

@DisplayName("Core: Usuário")
@ExtendWith(MockitoExtension.class)
class UsuarioFacadeTest {

	@Mock
	private UsuarioPersisence usuarioPersistence;

	@InjectMocks
	private UsuarioFacade facade;

	@Test
	@DisplayName("Tenta cadastrar usuário")
	void cadastrarUsuario() {

		CadastrarUsuarioCommand command = new CadastrarUsuarioCommand("email@provedor.com",
				LocalDate.parse("1991-08-20"), "PREMIUM");
		
		assertDoesNotThrow(() -> facade.executar(command));
		
		verify(usuarioPersistence).cadastrarUsuario(command);
	}
	
	@Test
	@DisplayName("Tenta cadastrar usuário com menos de 16 anos")
	void cadastrarUsuarioComMenosDe16Ano() {
		
		CadastrarUsuarioCommand command = new CadastrarUsuarioCommand("email@provedor.com",
				LocalDate.now().minusYears(15), "PREMIUM");
		
		assertThrows(UsuarioNaoPossuiIdadeMinimaException.class, () -> facade.executar(command));
		
		verify(usuarioPersistence, times(0)).cadastrarUsuario(command);
	}
	
	@Test
	@DisplayName("Tenta obter usuario pelo id")
	void obterUsuarioPeloId() {
		
		ObterUsuarioQuery query = new ObterUsuarioQuery(4L);
		
		UsuarioDto usuarioDto = new UsuarioDto(4L, "email@provedor.com", LocalDate.now().minusYears(17), "");
		
		doReturn(Optional.of(usuarioDto)).when(usuarioPersistence).obterUsuarioPeloId(any(Long.class));
		
		UsuarioDto resultado = facade.executar(query);
		
		verify(usuarioPersistence).obterUsuarioPeloId(Mockito.any(Long.class));
		
		assertEquals(usuarioDto.getId(), resultado.getId());
		assertEquals(usuarioDto.getEmail(), resultado.getEmail());
		assertEquals(usuarioDto.getNascimento(), resultado.getNascimento());
		assertEquals(usuarioDto.getTipoUsuario(), resultado.getTipoUsuario());
	}
	
	@Test
	@DisplayName("Tenta obter usuário pelo ID que não existe")
	void obterUsuarioComIdQueNaoExiste() {
		
		ObterUsuarioQuery query = new ObterUsuarioQuery(999L);
		
		doReturn(Optional.empty()).when(usuarioPersistence).obterUsuarioPeloId(any(Long.class));
		
		assertThrows(UsuarioNaoExisteException.class, () -> facade.executar(query));
		
		verify(usuarioPersistence).obterUsuarioPeloId(Mockito.any(Long.class));
	}
}
