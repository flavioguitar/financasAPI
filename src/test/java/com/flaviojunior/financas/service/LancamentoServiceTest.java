package com.flaviojunior.financas.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.flaviojunior.financas.exception.RegraNegocioExeption;
import com.flaviojunior.financas.model.entity.Lancamento;
import com.flaviojunior.financas.model.entity.Usuario;
import com.flaviojunior.financas.model.enums.StatusLancamento;
import com.flaviojunior.financas.model.repository.LancamentoRepository;
import com.flaviojunior.financas.model.repository.LancamentoRepositoryTest;
import com.flaviojunior.financas.service.impl.LancamentoServiceImpl;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("teste")
public class LancamentoServiceTest {
	
	@SpyBean
	LancamentoServiceImpl service;
		
	@MockBean
	LancamentoRepository repository;
	
	@Test
	public void deveSalvarumLancamento() {
		
		Lancamento lancamentoAsalvar = LancamentoRepositoryTest.criarLancamento();
		
		Mockito.doNothing().when(service).validar(lancamentoAsalvar);
		
		Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
		
		lancamentoSalvo.setId(1l);
		lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);
		Mockito.when(repository.save(lancamentoAsalvar)).thenReturn(lancamentoSalvo);
		
		Lancamento lancamento = service.salvar(lancamentoAsalvar);
		
		Assertions.assertThat(lancamento.getId()).isEqualTo(lancamentoSalvo.getId());
		Assertions.assertThat(lancamento.getStatus()).isEqualTo(StatusLancamento.PENDENTE);
		
	}
	
	@Test
	public void deveFiltrarLancamentos() {
		
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(1l);
		
		List<Lancamento> lista = Arrays.asList(lancamento);
		Mockito.when( repository.findAll(Mockito.any(Example.class)) ).thenReturn(lista);
		
		
		List<Lancamento> resutado = service.buscar(lancamento);
		
		Assertions
			.assertThat(resutado)
			.isNotEmpty()
			.hasSize(1)
			.contains(lancamento);
		
	}
	
	@Test 
	public void deveAtualizarStatus() {
		
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(1l);
		lancamento.setStatus(StatusLancamento.PENDENTE);	
		
		StatusLancamento novoStatus = StatusLancamento.EFETIVADO;
		Mockito.doReturn(lancamento).when(service).atualizar(lancamento);
		
		service.atualizarStatus(lancamento, novoStatus);
		
		
		Assertions.assertThat(lancamento.getStatus()).isEqualTo(novoStatus);
		Mockito.verify(service).atualizar(lancamento);
	}
	
	@Test
	public void deveObterLancamentoPorID() {
		Long id = 1l;
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(id);
		
		Mockito.when(repository.findById(id)).thenReturn(Optional.of(lancamento));
		
		Optional<Lancamento> resultado = service.obterPorId(id);
		
		Assertions.assertThat(resultado.isPresent()).isTrue();			
		
		
	}
	
	@Test
	public void deveRetornarVazioQuandoLancamentoNaoExiste() {
		Long id = 1l;
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(id);
		
		Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
		
		Optional<Lancamento> resultado = service.obterPorId(id);
		
		Assertions.assertThat(resultado.isPresent()).isFalse();		
	}
	
	
	@Test
	public void deveLancarErrosAoValidarUmLancamento() {
	
		Lancamento lancamento = new Lancamento();
		
		
		
		//Mockito.when(repository.findById(id) ).thenReturn(Optional.empty());
		
		Throwable exception = Assertions
				.catchThrowable( () -> service.validar(lancamento) );
		
		Assertions.assertThat(exception)
		.isInstanceOf(RegraNegocioExeption.class)
		.hasMessage("Informe uma descrição válido.");
		
		lancamento.setDescricao("teste");
		
		exception = Assertions
				.catchThrowable( () -> service.validar(lancamento) );
		
		Assertions.assertThat(exception)
		.isInstanceOf(RegraNegocioExeption.class)
		.hasMessage("Informe um Mês válido.");
		
		lancamento.setMes(01);
		
		exception = Assertions
				.catchThrowable( () -> service.validar(lancamento) );
		
		Assertions.assertThat(exception)
		.isInstanceOf(RegraNegocioExeption.class)
		.hasMessage("Informe um Ano válido.");
		
		lancamento.setAno(2022);
		
		exception = Assertions
				.catchThrowable( () -> service.validar(lancamento) );
		
		Assertions.assertThat(exception)
		.isInstanceOf(RegraNegocioExeption.class)
		.hasMessage("Informe um Usuário válido.");
		
		Usuario usuario = new Usuario();
		usuario.setId(1l);
		
		lancamento.setUsuario(usuario);
		
		exception = Assertions
				.catchThrowable( () -> service.validar(lancamento) );
		
		Assertions.assertThat(exception)
		.isInstanceOf(RegraNegocioExeption.class)
		.hasMessage("Informe um Valor válido.");
		
		lancamento.setValor(BigDecimal.valueOf(2.00));
		
		exception = Assertions
				.catchThrowable( () -> service.validar(lancamento) );
		
		Assertions.assertThat(exception)
		.isInstanceOf(RegraNegocioExeption.class)
		.hasMessage("Informe um Tipo de Lançamento.");
	}
	

}
