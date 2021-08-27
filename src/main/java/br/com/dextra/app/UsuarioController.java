package br.com.dextra.app;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.dextra.app.models.CadastrarUsuarioRequest;
import br.com.dextra.app.models.ErroResponse;
import br.com.dextra.core.exceptions.UsuarioNaoExisteException;
import br.com.dextra.core.exceptions.UsuarioNaoPossuiIdadeMinimaException;
import br.com.dextra.core.models.CadastrarUsuarioCommand;
import br.com.dextra.core.models.ObterUsuarioQuery;
import br.com.dextra.core.models.UsuarioDto;
import br.com.dextra.core.ports.incoming.CadastrarUsuario;
import br.com.dextra.core.ports.incoming.ObterUsuario;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/usuario")
public class UsuarioController {

	private final CadastrarUsuario cadastrarUsuario;
	private final ObterUsuario obterUsuario;
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public void cadastrarUsuario(@RequestBody @Validated CadastrarUsuarioRequest request) {

		cadastrarUsuario.executar(
				new CadastrarUsuarioCommand(request.getEmail(), request.getNascimento(), request.getTipoUsuario()));
	}
	
	@GetMapping("/{usuario}")
	@ResponseStatus(HttpStatus.OK)
	public UsuarioDto obterUsuarioPeloId(@PathVariable("usuario") Long usuario) {
		
		return obterUsuario.executar(new ObterUsuarioQuery(usuario));
	}
	
	@ExceptionHandler(UsuarioNaoPossuiIdadeMinimaException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErroResponse usuarioNaoPossuiIdadeMinima(UsuarioNaoPossuiIdadeMinimaException exception) {
		
		return new ErroResponse(HttpStatus.BAD_REQUEST.value()).adicionarErro(exception.getMessage());
	}
	
	@ExceptionHandler(UsuarioNaoExisteException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErroResponse usuarioNaoExisteException(UsuarioNaoExisteException exception) {
		
		return new ErroResponse(HttpStatus.NOT_FOUND.value()).adicionarErro(exception.getMessage());
	}
}
