package com.flaviojunior.financas.api.resource;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flaviojunior.financas.api.dto.UsuarioDTO;
import com.flaviojunior.financas.exception.ErroAutenticacaoException;
import com.flaviojunior.financas.exception.RegraNegocioExeption;
import com.flaviojunior.financas.model.entity.Usuario;
import com.flaviojunior.financas.service.LancamentoService;
import com.flaviojunior.financas.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioResource {

	private final UsuarioService service;
	private final LancamentoService lancamentoService;
	

	
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
	
	@GetMapping("{id}/saldo")
	public ResponseEntity obterSaldo(@PathVariable("id") Long id) {
			
		Optional<Usuario> usuario = service.obterId(id);
		
		if(!usuario.isPresent()) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		
		BigDecimal saldo = lancamentoService.obterSaldoUsuario(id);
		return ResponseEntity.ok(saldo);
		
	}
	
	@GetMapping("{id}/saldoMes")
	public ResponseEntity obterSaldoMesAnoAtual(@PathVariable("id") Long id) {
		
		Optional<Usuario> usuario = service.obterId(id);
		
		if(!usuario.isPresent()) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		
		BigDecimal saldo = lancamentoService.obterSaldoUsuarioMes(id);
		return ResponseEntity.ok(saldo);
	}
}
