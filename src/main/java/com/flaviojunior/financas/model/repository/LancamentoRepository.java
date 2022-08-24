package com.flaviojunior.financas.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flaviojunior.financas.model.entity.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {
	
	

}
