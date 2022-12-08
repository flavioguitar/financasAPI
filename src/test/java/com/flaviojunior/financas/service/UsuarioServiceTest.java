package com.flaviojunior.financas.service;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.flaviojunior.financas.exception.ErroAutenticacaoException;
import com.flaviojunior.financas.exception.RegraNegocioExeption;
import com.flaviojunior.financas.model.entity.Usuario;
import com.flaviojunior.financas.model.repository.UsuarioRepository;
import com.flaviojunior.financas.service.impl.UsuarioServiceImpl;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("teste")
public class UsuarioServiceTest {	
	
	@SpyBean
	UsuarioServiceImpl service;
		
	@MockBean
	UsuarioRepository repository;
	
	
	@Test
	public void deveSalvarUsuario() {
		
		Mockito.doNothing().when(service).validarEmail(Mockito.anyString());
		Usuario usuario = Usuario.builder()
				.id(1l)
				.nome("nome")
				.email("email@email.com")
				.senha("senha").build();
		Mockito.when(repository.save(Mockito.any(Usuario.class))).thenReturn(usuario);
		
		Usuario usuariosalvo = service.salvarUsuario(new Usuario());
		
		Assertions.assertThat(usuariosalvo).isNotNull();
		Assertions.assertThat(usuariosalvo.getId()).isEqualTo(1l);
		Assertions.assertThat(usuariosalvo.getNome()).isEqualTo("nome");
		Assertions.assertThat(usuariosalvo.getEmail()).isEqualTo("email@email.com");
		Assertions.assertThat(usuariosalvo.getSenha()).isEqualTo("senha");
		
		
	}
	
	
	@Test
	public void deveAutenticarUmUsuarioComSucesso() {
		
		String email = "email@email.com";
		String senha = "senha";
		
		Usuario usuario = Usuario.builder().email(email).senha(senha).id(1l).build();
		
		Mockito.when(repository.findByEmail(email) ).thenReturn(Optional.of(usuario));
		
		Usuario result =  service.autenticar(email, senha);
		
		Assertions.assertThat(result).isNotNull();
		
	}
	
	@Test
	public void deveLancarErroQuandoNaoEncontrarUsuarioCadastrado() {
		
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
				
		Throwable exception = Assertions.catchThrowable( () -> service.autenticar("email@email.com", "Senha") );
		
		Assertions.assertThat(exception)
			.isInstanceOf(ErroAutenticacaoException.class)
			.hasMessage("Usuario não encontrado.");
		
		
	}
	
	
	@Test
	public void naoDeveSalvarUmUsuarioComEmailJaCadastrado() {
		
		String email = "email@email.com";
		
		Usuario usuario = Usuario.builder().email(email).build();
		Mockito.doThrow(RegraNegocioExeption.class)
					.when(service)
					.validarEmail(email);
		
		org.junit.jupiter.api.Assertions.
				assertThrows(RegraNegocioExeption.class,() -> service.salvarUsuario(usuario) );
		
		Mockito.verify(repository,Mockito.never()).save(usuario);				
		
	}	
	@Test
	public void deveValidarEmail() {
		
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);
					
		service.validarEmail("email2@email.com");
		
	}
	
	@Test
	public void deveLancarErroQuandoExistirEmailCadastrado() {
		
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);
		
		org.junit.jupiter.api.Assertions.
			assertThrows(RegraNegocioExeption.class, () -> service.validarEmail("email2@email.com"));
	}
	
	
	
}
