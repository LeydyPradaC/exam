package com.demo.dao;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.demo.model.MutantModel;

public interface MutantDAO extends JpaRepository<MutantModel, Integer>{
	
	/**
	 * Consulta que permite traer la información de una secuencia si esta
	 * ya fue analizada
	 * @param dna - Secuencia de ADN a consultar
	 * @return lista de secuencias en la base de datos
	 */
	@Query("select u from MutantModel u where u.dna = ?1")
	ArrayList<MutantModel> findByDna(String dna);

}
