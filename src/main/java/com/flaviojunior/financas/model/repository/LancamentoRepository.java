package com.flaviojunior.financas.model.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.flaviojunior.financas.model.entity.Lancamento;
import com.flaviojunior.financas.model.enums.StatusLancamento;
import com.flaviojunior.financas.model.enums.TipoLancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {
	
	@Query(value = "SELECT SUM(l.valor) FROM Lancamento l JOIN l.usuario u"
					+ " WHERE u.id = :idUsuario AND l.tipo = :tipo GROUP BY u")
	BigDecimal obterSaldoPorTipoLancamentoEUsuario(
			@Param("idUsuario") Long idUsuario,
			@Param("tipo") TipoLancamento tipo  );
	
	@Query(value = "SELECT SUM(l.valor) FROM Lancamento l JOIN l.usuario u"
			+ " WHERE u.id = :idUsuario AND l.tipo = :tipo "
			+ " AND l.mes = :mes "
			+ " AND l.ano = :ano "
			+ " AND l.status = :status GROUP BY u ")	
	BigDecimal obterSaldoPorTipoLancamentoEUsuarioMesAtual(
		@Param("idUsuario") Long idUsuario,
		@Param("tipo") TipoLancamento tipo,
		@Param("mes") Integer mes,
		@Param("ano") Integer ano,
		@Param("status") StatusLancamento status  );
}
