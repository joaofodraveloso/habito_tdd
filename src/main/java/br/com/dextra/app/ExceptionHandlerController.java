package br.com.dextra.app;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.dextra.app.models.ErroResponse;

@RestController
@ControllerAdvice
public class ExceptionHandlerController {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErroResponse methodArgumentNotValidException(MethodArgumentNotValidException exception) {

		var response = new ErroResponse(HttpStatus.BAD_REQUEST.value());
		exception.getAllErrors().forEach(erro -> response.adicionarErro(erro.getDefaultMessage()));
		return response;
	}
}
