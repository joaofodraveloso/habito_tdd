package br.com.dextra.app;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import br.com.dextra.core.exceptions.UsuarioNaoExisteException;
import br.com.dextra.core.exceptions.UsuarioNaoPossuiIdadeMinimaException;
import br.com.dextra.core.models.CadastrarUsuarioCommand;
import br.com.dextra.core.models.ObterUsuarioQuery;
import br.com.dextra.core.models.UsuarioDto;
import br.com.dextra.core.ports.incoming.CadastrarUsuario;
import br.com.dextra.core.ports.incoming.ObterUsuario;
import br.com.dextra.util.JsonCreator;

@WebMvcTest(UsuarioController.class)
@DisplayName("App: Usuário")
class UsuarioControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CadastrarUsuario cadastrarUsuario;

	@MockBean
	private ObterUsuario obterUsuario;

	@Captor
	private ArgumentCaptor<CadastrarUsuarioCommand> cadastrarUsuarioCommand;

	@Captor
	private ArgumentCaptor<ObterUsuarioQuery> obterUsuarioQuery;

	@Test
	@DisplayName("Tenta cadastrar usuário")
	void cadastrarUsuario() throws Exception {

		final String email = "email@provedor.com";
		final String nascimento = "1991-05-02";
		final String tipoUsuario = "PREMIUM";

		// @formatter:off
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/usuario").contentType(MediaType.APPLICATION_JSON)
			.content(JsonCreator.startJson()
					.name("email").value(email)
					.name("nascimento").value(nascimento)
					.name("tipoUsuario").value(tipoUsuario)
					.endJson());
		// @formatter:on

		ResultActions resultado = mockMvc.perform(request);

		verify(cadastrarUsuario).executar(cadastrarUsuarioCommand.capture());

		resultado.andExpect(status().isCreated());

		CadastrarUsuarioCommand value = cadastrarUsuarioCommand.getValue();

		assertEquals(email, value.getEmail());
		assertEquals(LocalDate.parse(nascimento), value.getNascimento());
		assertEquals(tipoUsuario, value.getTipoUsuario());
	}

	@Test
	@DisplayName("Tenta cadastrar usuário com menos de 16 anos")
	void cadastrarUsuarioComMenosDe16Anos() throws Exception {

		final String email = "email@provedor.com";
		final String nascimento = LocalDate.now().minusYears(10).toString();
		final String tipoUsuario = "PREMIUM";

		// @formatter:off
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/usuario").contentType(MediaType.APPLICATION_JSON)
			.content(JsonCreator.startJson()
					.name("email").value(email)
					.name("nascimento").value(nascimento)
					.name("tipoUsuario").value(tipoUsuario)
					.endJson());
		// @formatter:on

		doThrow(new UsuarioNaoPossuiIdadeMinimaException()).when(cadastrarUsuario)
				.executar(any(CadastrarUsuarioCommand.class));

		ResultActions resultado = mockMvc.perform(request);

		verify(cadastrarUsuario).executar(any(CadastrarUsuarioCommand.class));

		resultado.andExpect(status().isBadRequest()).andExpect(jsonPath("codigo").value("400"))
				.andExpect(jsonPath("erros").value(hasItem("Usuário não possui idade minima para cadastro")));
	}

	@Test
	@DisplayName("Tenta cadastrar usuário com request invalido")
	void cadastrarUsuarioComRequestInvalido() throws Exception {

		final String email = "emailprovedor.com";

		// @formatter:off
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/usuario").contentType(MediaType.APPLICATION_JSON)
			.content(JsonCreator.startJson()
					.name("email").value(email)
					.name("nascimento").nullValue()
					.name("tipoUsuario").nullValue()
					.endJson());
		// @formatter:on

		ResultActions resultado = mockMvc.perform(request);

		verify(cadastrarUsuario, times(0)).executar(any(CadastrarUsuarioCommand.class));

		resultado.andExpect(status().isBadRequest()).andExpect(jsonPath("codigo").value("400"))
				.andExpect(jsonPath("erros").value(hasItem("Email preenchido é invalido")))
				.andExpect(jsonPath("erros").value(hasItem("A data de nascimento deve ser preenchida")))
				.andExpect(jsonPath("erros").value(hasItem("O tipo de usuário deve ser preenchido")));
	}

	@Test
	@DisplayName("Tenta obter o usuário pelo id")
	void obterUsuarioPeloId() throws Exception {

		final Long USUARIO = 6L;

		UsuarioDto usuarioDto = new UsuarioDto(2L, "email@provedor.com", LocalDate.now().minusYears(17), "PREMIUM");

		doReturn(usuarioDto).when(obterUsuario).executar(any(ObterUsuarioQuery.class));

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/usuario/{usuario}", USUARIO);

		ResultActions resultado = mockMvc.perform(request);

		verify(obterUsuario).executar(obterUsuarioQuery.capture());

		assertEquals(USUARIO, obterUsuarioQuery.getValue().getId());

		resultado.andExpect(status().isOk()).andExpect(jsonPath("id").value(usuarioDto.getId()))
				.andExpect(jsonPath("email").value(usuarioDto.getEmail()))
				.andExpect(jsonPath("nascimento").value(usuarioDto.getNascimento().toString()))
				.andExpect(jsonPath("tipoUsuario").value(usuarioDto.getTipoUsuario()));
	}

	@Test
	@DisplayName("Tenta obter usuário pelo id que não existe")
	void obterUsuarioComIdQueNaoExiste() throws Exception {

		final Long USUARIO = 6L;

		doThrow(new UsuarioNaoExisteException()).when(obterUsuario).executar(any(ObterUsuarioQuery.class));

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/usuario/{usuario}", USUARIO);

		ResultActions resultado = mockMvc.perform(request);

		verify(obterUsuario).executar(any(ObterUsuarioQuery.class));

		resultado.andExpect(status().isNotFound()).andExpect(jsonPath("codigo").value("404"))
				.andExpect(jsonPath("erros").value(hasItem("Usuário não existe")));
	}
}
