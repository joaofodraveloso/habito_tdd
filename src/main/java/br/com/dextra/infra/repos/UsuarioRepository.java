package br.com.dextra.infra.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.dextra.infra.entities.UsuarioEntity;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long>{

}
