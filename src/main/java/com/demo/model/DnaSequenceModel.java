package com.demo.model;

import java.util.ArrayList;

public class DnaSequenceModel {
	
	private ArrayList<String> dna;

	/**
	 * Obtiene la lista de la secuencia de ADN
	 * @return Lista de ADN
	 */
	public ArrayList<String> getDna() {
		return dna;
	}

	/**
	 * Carga la lista de ADN
	 * @param dna - secuencia de ADN
	 */
	public void setDna(ArrayList<String> dna) {
		this.dna = dna;
	}

}
