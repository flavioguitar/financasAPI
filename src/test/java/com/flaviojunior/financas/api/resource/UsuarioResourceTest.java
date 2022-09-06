package com.flaviojunior.financas.api.resource;


import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flaviojunior.financas.api.dto.UsuarioDTO;
import com.flaviojunior.financas.exception.ErroAutenticacaoException;
import com.flaviojunior.financas.exception.RegraNegocioExeption;
import com.flaviojunior.financas.model.entity.Usuario;
import com.flaviojunior.financas.service.LancamentoService;
import com.flaviojunior.financas.service.UsuarioService;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("teste")
@WebMvcTest(controllers = UsuarioResource.class )
@AutoConfigureMockMvc
public class UsuarioResourceTest {
	
	static final String API = "/api/usuarios";
	static final MediaType JSON = MediaType.APPLICATION_JSON;
	
	@Autowired
	MockMvc mvc;
	
	@MockBean
	UsuarioService service;
	
	@MockBean
	LancamentoService serviceLancamento;
	
	@Test	
	public void deveAutenticarUmUsuario() throws Exception {
		
		String email = "usuario@email.com";
		String senha = "1234";
		
		UsuarioDTO dto = UsuarioDTO.builder()
				.email(email)
				.senha(senha).build();
		
		Usuario usuario = Usuario.builder()
				.id(1l).email(email)
				.senha(senha).build();
		
		Mockito.when(service.autenticar(email, senha)).thenReturn(usuario);
		
		String json = new ObjectMapper().writeValueAsString(dto);
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
												.post(API.concat("/autenticar"))
												.accept(JSON )
												.contentType(JSON)
												.content(json);
		
		mvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isOk() )
			.andExpect(MockMvcResultMatchers.jsonPath("id").value(usuario.getId()) )
			.andExpect(MockMvcResultMatchers.jsonPath("nome").value(usuario.getNome() ) ) 
			.andExpect(MockMvcResultMatchers.jsonPath("email").value(usuario.getEmail() ) )
			;
		
	}
	
	@Test	
	public void deveRetornarBadRequestAoObterErroDeAutenticacao() throws Exception {
		
		String email = "usuario@email.com";
		String senha = "1234";
		
		UsuarioDTO dto = UsuarioDTO.builder()
				.email(email)
				.senha(senha).build();
		
		Usuario usuario = Usuario.builder()
				.id(1l).email(email)
				.senha(senha).build();
		
		Mockito.when(service.salvarUsuario(Mockito.any(Usuario.class))).thenReturn(usuario);
		
		String json = new ObjectMapper().writeValueAsString(dto);
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
												.post(API)
												.accept(JSON )
												.contentType(JSON)
												.content(json);
		
		mvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isCreated() )
			.andExpect(MockMvcResultMatchers.jsonPath("id").value(usuario.getId()) )
			.andExpect(MockMvcResultMatchers.jsonPath("nome").value(usuario.getNome() ) ) 
			.andExpect(MockMvcResultMatchers.jsonPath("email").value(usuario.getEmail() ) )
			;
		
	}

	@Test	
	public void deveCriarUmNovoUsuario() throws Exception {
		
		String email = "usuario@email.com";
		String senha = "1234";
		
		UsuarioDTO dto = UsuarioDTO.builder()
				.email(email)
				.senha(senha).build();
		
		Mockito.when(service.autenticar(email, senha)).thenThrow(ErroAutenticacaoException.class);
		
		String json = new ObjectMapper().writeValueAsString(dto);
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
												.post(API.concat("/autenticar"))
												.accept(JSON )
												.contentType(JSON)
												.content(json);
		
		mvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isBadRequest() )
			;
		
	}
	
	@Test	
	public void deveRetornarBadRequestAoTentarCriarUmNovoUsuarioInvalido() throws Exception {
		
		
		String email = "usuario@email.com";
		String senha = "1234";
		
		UsuarioDTO dto = UsuarioDTO.builder()
				.email(email)
				.senha(senha).build();
		
		
		Mockito.when(service.salvarUsuario(Mockito.any(Usuario.class)))
			.thenThrow(RegraNegocioExeption.class);
		
		String json = new ObjectMapper().writeValueAsString(dto);
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
												.post(API)
												.accept(JSON )
												.contentType(JSON)
												.content(json);
		
		mvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isBadGateway() )
			;
		
	}
}
