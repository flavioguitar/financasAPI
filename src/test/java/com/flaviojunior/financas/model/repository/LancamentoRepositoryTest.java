package com.flaviojunior.financas.model.repository;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.flaviojunior.financas.model.entity.Lancamento;
import com.flaviojunior.financas.model.enums.StatusLancamento;
import com.flaviojunior.financas.model.enums.TipoLancamento;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("teste")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class LancamentoRepositoryTest {
	
	@Autowired
	LancamentoRepository repository;
	
	@Autowired
	TestEntityManager entityManager;
	
	@Test
	public void deveSalvarUmLancamento() {
		
		Lancamento lancamento = criarLancamento();
		
		lancamento = repository.save(lancamento);
		
		assertThat(lancamento.getId()).isNotNull();
		
	}
	
	@Test	
	public void deveDeletarLancamento() {
		
		Lancamento lancamento = criarLancamento();		
		lancamento = entityManager.persist(lancamento);
		
		lancamento = entityManager.find(Lancamento.class, lancamento.getId());
		
		repository.delete(lancamento);
		
		Lancamento lancamentoInexistente = entityManager.find(Lancamento.class, lancamento.getId());
		
		assertThat(lancamentoInexistente).isNull();
		
	}
	
	@Test
	public void deveAtualizarUmLancamento() {
		
		Lancamento lancamento = criarLancamento();
		lancamento = entityManager.persist(lancamento);
		
		lancamento.setAno(2017);
		lancamento.setMes(06);
		
		repository.save(lancamento);
		
		Lancamento lancamentoAtualizar = entityManager.find(Lancamento.class, lancamento.getId());
		
		assertThat(lancamentoAtualizar.getAno()).isEqualTo(2017);
		assertThat(lancamentoAtualizar.getMes()).isEqualTo(06);
		
	
		
	}
	
	public static Lancamento criarLancamento() {
		
		Lancamento lancamento = Lancamento.builder()
				.ano(2019)
				.descricao("Lancamento tester")
				.status(StatusLancamento.PENDENTE)
				.tipo(TipoLancamento.RECEITA)
				.mes(05)
				.valor(BigDecimal.valueOf(10))
				.DataCadastro(LocalDate.now())
				.build();
		
		return lancamento;
	}
	

}
