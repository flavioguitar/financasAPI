package com.flaviojunior.financas.api.resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flaviojunior.financas.api.dto.LancamentoDTO;
import com.flaviojunior.financas.exception.RegraNegocioExeption;
import com.flaviojunior.financas.model.entity.Lancamento;
import com.flaviojunior.financas.model.entity.Usuario;
import com.flaviojunior.financas.model.enums.StatusLancamento;
import com.flaviojunior.financas.model.enums.TipoLancamento;
import com.flaviojunior.financas.service.LancamentoService;
import com.flaviojunior.financas.service.UsuarioService;

@RestController
@RequestMapping("api/lancamentos")
public class LancamentoResource {
	
	private LancamentoService service;
	private UsuarioService usuarioservice;
	
	public LancamentoResource(LancamentoService service) {
		this.service = service;
	}
	
	@PostMapping
	public ResponseEntity salvar(@RequestBody LancamentoDTO dto) {
		
		// parei aqui
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
		lancamento.setTipo(TipoLancamento.valueOf(dto.getTipo()));		
		lancamento.setStatus(StatusLancamento.valueOf(dto.getTipo()));
		
		return lancamento;
	}

}
