package com.flaviojunior.financas.model.repository;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.flaviojunior.financas.model.entity.Usuario;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("teste")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UsuarioRepositoryTest {

	@Autowired
	UsuarioRepository repository;
	
	@Autowired
	TestEntityManager entityManager;
	
	@Test
	public void VerificarEmailExistente() {
		
		Usuario usuario = criarUsuario();
		entityManager.persist(usuario);

		boolean result = repository.existsByEmail("email@email.com");
		
		Assertions.assertThat(result).isTrue();
	}
	
	@Test
	public void deveRetornarFalsoQuandoNaoHouverUsuarioCadastradoComOEmail() {
				
		boolean result = repository.existsByEmail("email@email.com");
		Assertions.assertThat(result).isFalse();
	}
	
	@Test
	public void devePersistirUsuarioNaBase() {
		
		Usuario usuario = criarUsuario();
		
		Usuario usuariosalvo = repository.save(usuario);
		
		Assertions.assertThat(usuariosalvo.getId()).isNotNull();
	}
	
	@Test
	public void deveBuscarUsuarioEmail() {
		
		Usuario usuario = criarUsuario();
		entityManager.persist(usuario);
		
		Optional<Usuario> result = repository.findByEmail("email@email.com");
		
		Assertions.assertThat(result.isPresent()).isTrue();
		
	}
	
	@Test
	public void deveRetornarVazioUsuarioEmailNaoExistenteNaBase() {
		
		Optional<Usuario> result = repository.findByEmail("email@email.com");
		
		Assertions.assertThat(result.isPresent()).isFalse();
		
	}

	public static Usuario criarUsuario() {
		return Usuario.builder()
				.nome("usuario")
				.email("email@email.com")
				.senha("senha")
				.build();
	}
	

}
