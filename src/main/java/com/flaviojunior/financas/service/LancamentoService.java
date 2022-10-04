package com.flaviojunior.financas.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.flaviojunior.financas.model.entity.Lancamento;
import com.flaviojunior.financas.model.enums.StatusLancamento;


public interface LancamentoService {
	
	Lancamento salvar(Lancamento lancamento);
	
	Lancamento atualizar(Lancamento lancamento);
	
	void deletar(Lancamento lancamento);
	
	List<Lancamento> buscar(Lancamento lancamentoFiltro );
	
	void atualizarStatus(Lancamento lancamento, StatusLancamento status);
	
	void validar(Lancamento lancamento);
	
	Optional<Lancamento> obterPorId(Long id);
	
	BigDecimal obterSaldoUsuario(Long id);
	
	BigDecimal obterSaldoUsuarioMes(Long id); 
}
