package com.flaviojunior.financas.service.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flaviojunior.financas.exception.RegraNegocioExeption;
import com.flaviojunior.financas.model.entity.Lancamento;
import com.flaviojunior.financas.model.enums.StatusLancamento;
import com.flaviojunior.financas.model.enums.TipoLancamento;
import com.flaviojunior.financas.model.repository.LancamentoRepository;
import com.flaviojunior.financas.service.LancamentoService;

@Service
public class LancamentoServiceImpl implements LancamentoService {
	
	
	private LancamentoRepository repository;
	
	public LancamentoServiceImpl(LancamentoRepository repository) {
		
		this.repository = repository;
	}

	@Override
	@Transactional
	public Lancamento salvar(Lancamento lancamento) {
		// TODO Auto-generated method stub
		validar(lancamento);
		lancamento.setStatus(StatusLancamento.PENDENTE);
		return repository.save(lancamento);
	}

	@Override
	@Transactional
	public Lancamento atualizar(Lancamento lancamento) {
		
		validar(lancamento);
		
		Objects.requireNonNull(lancamento.getId());
		
		return repository.save(lancamento);
	}

	@Override
	@Transactional
	public void deletar(Lancamento lancamento) {
		
		Objects.requireNonNull(lancamento.getId());
		
		repository.delete(lancamento);
		
	}

	@Override
	@Transactional(readOnly = true)
	public List<Lancamento> buscar(Lancamento lancamentoFiltro) {
		
		Example example = Example.of(lancamentoFiltro,ExampleMatcher.matching()
				.withIgnoreCase()
				.withStringMatcher(StringMatcher.CONTAINING));
		
		return repository.findAll(example);
	}

	@Override
	public void atualizarStatus(Lancamento lancamento, StatusLancamento status) {
		lancamento.setStatus(status);
		atualizar(lancamento);
		
	}

	@Override
	public void validar(Lancamento lancamento) {
		
		if(lancamento.getDescricao() == null || lancamento.getDescricao() .trim().equals("") ) {
			throw new RegraNegocioExeption("Informe uma descrição válido.");
		}
			
		if(lancamento.getMes() == null || lancamento.getMes() < 1 ) {
			throw new RegraNegocioExeption("Informe um Mês válido.");
		}
		
		if(lancamento.getAno() == null || lancamento.getAno().toString().length() != 4 ) {
			throw new RegraNegocioExeption("Informe um Ano válido.");
		}
		
		if(lancamento.getUsuario() == null || lancamento.getUsuario().getId() == null ) {
			throw new RegraNegocioExeption("Informe um Usuário válido.");
		}
		
		if(lancamento.getValor() == null || lancamento.getValor().compareTo(BigDecimal.ZERO) < 1  ) {
			throw new RegraNegocioExeption("Informe um Valor válido.");
		}
		
		if(lancamento.getTipo() == null ) {
			throw new RegraNegocioExeption("Informe um Tipo de Lançamento.");
		}
			
	}

	@Override
	public Optional<Lancamento> obterPorId(Long id) {
		// TODO Auto-generated method stub
		return repository.findById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public BigDecimal obterSaldoUsuario(Long id) {
		
		BigDecimal receitas = repository.obterSaldoPorTipoLancamentoEUsuario(id, TipoLancamento.RECEITA);
		BigDecimal despesas = repository.obterSaldoPorTipoLancamentoEUsuario(id, TipoLancamento.DESPESA);
		
		if(receitas == null) {
			receitas=BigDecimal.ZERO;
		}
		
		if(despesas == null) {
			despesas=BigDecimal.ZERO;
		}
		
		
		return receitas.subtract(despesas);
	}

	
	@Override
	@Transactional(readOnly = true)
	public BigDecimal obterSaldoUsuarioMes(Long id) {
		
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		
		int ano = calendar.get(Calendar.YEAR);
		int mes = calendar.get(Calendar.MONTH);
		mes = mes+1;
		
		System.out.print("mes"+mes);
		System.out.print("mes"+ano);
		BigDecimal receitas = repository.obterSaldoPorTipoLancamentoEUsuarioMesAtual(id, TipoLancamento.RECEITA,mes,ano,StatusLancamento.EFETIVADO);
		BigDecimal despesas = repository.obterSaldoPorTipoLancamentoEUsuarioMesAtual(id, TipoLancamento.DESPESA,mes,ano,StatusLancamento.EFETIVADO);
		
		if(receitas == null) {
			receitas=BigDecimal.ZERO;
		}
		
		if(despesas == null) {
			despesas=BigDecimal.ZERO;
		}
		
		
		return receitas.subtract(despesas);
	}
}
