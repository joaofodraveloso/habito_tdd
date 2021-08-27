package br.com.dextra.app.models;

import java.util.ArrayList;
import java.util.Collection;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ErroResponse {

	private final Integer codigo;
	private Collection<String> erros = new ArrayList<>();
	
	public ErroResponse adicionarErro(String erro) {
		
		this.erros.add(erro);
		return this;
	}
}
