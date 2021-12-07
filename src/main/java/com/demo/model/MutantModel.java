package com.demo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="mutant")
public class MutantModel {
	
	/**
	 * Identificador de la tabla
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;
	
	/**
	 * Secuencia de ADN
	 */
	@Column
	private String dna;
	
	/**
	 * identifica si es mutante 1 o no 0
	 */
	@Column
	private Integer mutant;

	
	//Getters and Setters
	
	/**
	 * Obtiene el id
	 * @return id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * guarda el id
	 * @param id - identificador
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Obtiene la secuencia de ADN
	 * @return secuencia de ADN
	 */
	public String getDna() {
		return dna;
	}

	/**
	 * guarda la secuencia de ADN
	 * @param dna - secuencia a guardar
	 */
	public void setDna(String dna) {
		this.dna = dna;
	}

	/**
	 * identifica si la secuencia de ADN es mutante o no
	 * @return 1- es mutante, 0 no es mutante
	 */
	public Integer getMutant() {
		return mutant;
	}

	/**
	 * Guarda el valor de si es mutante o no
	 * @param mutant - 1 es mutante, 0 no es mutante
	 */
	public void setMutant(Integer mutant) {
		this.mutant = mutant;
	}	

}
