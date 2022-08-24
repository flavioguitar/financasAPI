package com.flaviojunior.financas.api.resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flaviojunior.financas.api.dto.UsuarioDTO;
import com.flaviojunior.financas.exception.ErroAutenticacaoException;
import com.flaviojunior.financas.exception.RegraNegocioExeption;
import com.flaviojunior.financas.model.entity.Usuario;
import com.flaviojunior.financas.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioResource {

	private UsuarioService service;
	
	public UsuarioResource(UsuarioService service) {
		this.service = service;
	}
	
	@PostMapping("/autenticar")
	public ResponseEntity autenticar( @RequestBody UsuarioDTO dto ) {
		try {
			
			Usuario usuarioUutenticado = service.autenticar(dto.getEmail(),dto.getSenha());
			return ResponseEntity.ok(usuarioUutenticado);
		} catch (ErroAutenticacaoException e) {
			
			return ResponseEntity.badRequest().body(e.getMessage());
		}	
	}	
	
	@PostMapping
	public ResponseEntity salvar( @RequestBody UsuarioDTO dto ) {
		
		Usuario usuario = Usuario.builder()
				.nome(dto.getNome())
				.email(dto.getEmail())
				.senha(dto.getSenha())
				.build();
		
		try {
			
			Usuario usuarioSalvo = service.salvarUsuario(usuario);
			return new ResponseEntity(usuarioSalvo, HttpStatus.CREATED);
		} catch (RegraNegocioExeption e) {
			
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}
}
