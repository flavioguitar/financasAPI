package com.flaviojunior.financas.api.resource;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.flaviojunior.financas.api.dto.LancamentoDTO;
import com.flaviojunior.financas.exception.RegraNegocioExeption;
import com.flaviojunior.financas.model.entity.Lancamento;
import com.flaviojunior.financas.model.entity.Usuario;
import com.flaviojunior.financas.model.enums.StatusLancamento;
import com.flaviojunior.financas.model.enums.TipoLancamento;
import com.flaviojunior.financas.service.LancamentoService;
import com.flaviojunior.financas.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/lancamentos")
@RequiredArgsConstructor
public class LancamentoResource {
	
	private final LancamentoService service;
	private final UsuarioService usuarioservice;
	
	@GetMapping
	public ResponseEntity buscar(
					@RequestParam(value = "descricao", required = false) String descricao,
					@RequestParam(value = "mes",required = false) Integer mes,
					@RequestParam(value = "ano",required = false) Integer ano,
					@RequestParam("usuario") Long idUsuario
								 ) {
		
					Lancamento lancamentoFiltro = new Lancamento();					
					lancamentoFiltro.setDescricao(descricao);
					lancamentoFiltro.setMes(mes);
					lancamentoFiltro.setAno(ano);
					
					Optional<Usuario> usuario = usuarioservice.obterId(idUsuario);
					if(!usuario.isPresent()) {
						
						return ResponseEntity.badRequest().body("Usuário não encontrado");
					}else {
						lancamentoFiltro.setUsuario(usuario.get());
					}
					
					List<Lancamento> lancamentos = service.buscar(lancamentoFiltro);
					return ResponseEntity.ok(lancamentos);
	}
	
	@PostMapping
	public ResponseEntity salvar(@RequestBody LancamentoDTO dto) {
		
		try {
			
			Lancamento entidade = converter(dto);
			entidade = service.salvar(entidade);
			
			return new ResponseEntity(entidade,HttpStatus.CREATED);
			
		} catch (RegraNegocioExeption e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	
	}
	
	@PutMapping("{id}")
	public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody LancamentoDTO dto) {
	
		return service.obterPorId(id).map(entity -> {
			
			try {
			
				Lancamento lancamento = converter(dto);
				lancamento.setId(entity.getId());
				service.atualizar(lancamento);
				return ResponseEntity.ok(lancamento);
				
			}catch (RegraNegocioExeption e) {
				
				return ResponseEntity.badRequest().body(e.getMessage());
			}
			
		}).orElseGet( () -> new ResponseEntity("Lançamento não encontrado na base de dados.",HttpStatus.BAD_REQUEST) );
		
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity deletar(@PathVariable("id") Long id) {
		
		return service.obterPorId(id).map(entity -> {
			
			service.deletar(entity);
			return new ResponseEntity(HttpStatus.NO_CONTENT);
			
		}).orElseGet( () -> new ResponseEntity("Lançamento não encontrado na base de dados.",HttpStatus.BAD_REQUEST) );
	}
	
	private Lancamento converter(LancamentoDTO dto) {
		
		Lancamento lancamento = new  Lancamento();
		lancamento.setId(dto.getId());
		lancamento.setDescricao(dto.getDescricao());
		lancamento.setAno(dto.getAno());
		lancamento.setMes(dto.getMes());
		lancamento.setValor(dto.getValor());
		
		Usuario usuario = usuarioservice.obterId(dto.getUsuario())
		.orElseThrow( () -> new RegraNegocioExeption("Usuário não encontrado."));
		
		lancamento.setUsuario(usuario);
		
		if(dto.getTipo() != null ) {
			lancamento.setTipo(TipoLancamento.valueOf(dto.getTipo()));
		}

		if(dto.getStatus() != null ) {
			lancamento.setStatus(StatusLancamento.valueOf(dto.getStatus()));
		}
	
		
		return lancamento;
	}

}
