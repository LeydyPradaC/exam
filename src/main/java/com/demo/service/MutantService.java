package com.demo.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.dao.MutantDAO;
import com.demo.model.DnaSequenceModel;
import com.demo.model.MutantModel;

@Service
public class MutantService {

	@Autowired
	private MutantDAO mutantDAO;

	/**
	 * Lista de base nitrogenada del ADN
	 */
	private ArrayList<String> permitidas = new ArrayList<String>();

	/**
	 * Metodo que carga la lista de la base nitrogenada
	 */
	private void cargarPermitidas() {
		permitidas.add("A");
		permitidas.add("T");
		permitidas.add("C");
		permitidas.add("G");
	}

	/**
	 * Metodo que se encarga de determinar si la secuencia de ADN entregada 
	 * pertenece o no a un mutante
	 * @param dnaList - Secuencia de ADN a analizar
	 * @return true si es mutante, false en caso contrario
	 */
	public boolean isMutant(DnaSequenceModel dnaList) {

		System.out.println("Llego al servicio isMutant");
		ArrayList<String> dna = dnaList.getDna();

		cargarPermitidas();
		String cadenaDna = "";

		for (String stringDna : dnaList.getDna()) {
			if (cadenaDna.isEmpty())
				cadenaDna = stringDna;
			else
				cadenaDna = cadenaDna + "," + stringDna;
		}

		boolean noEsPermitido = esPermitida(dna);
		int total = 0;
		if (noEsPermitido) {
			int cantidadVerticales = verticales(dna);
			int cantidadHorizontales = horizontales(dna);
			int cantidadDiagonales = diagonales(dna);
			total = cantidadDiagonales + cantidadHorizontales + cantidadVerticales;
		}

		MutantModel mutant = new MutantModel();
		mutant.setDna(cadenaDna);

		boolean rtaEsMutante = false;

		if (total >= 2) {
			rtaEsMutante = true;
			mutant.setMutant(1);
		} else {
			mutant.setMutant(0);
		}

		if (!existMutantsByDNA(cadenaDna)) {
			this.mutantDAO.save(mutant);
		}
		return rtaEsMutante;
	}

	/**
	 * Metodo que verifica si la secuencia de ADN ya fue analizada
	 * @param dnaString - Cadena a analizar
	 * @return true si existe, false en caso contrario
	 */
	private boolean existMutantsByDNA(String dnaString) {

		ArrayList<MutantModel> mutants = mutantDAO.findByDna(dnaString);

		if (mutants.size() >= 1)
			return true;
		else
			return false;
	}

	/**
	 * Valida si los caracteres enviados en la secuencia de ADN pertenecen o no
	 * a la base nitrogenada del ADN
	 * @param dna - Secuencia de ADN
	 * @return true si la secuencia es valida, false en caso contrario
	 */
	private boolean esPermitida(ArrayList<String> dna) {
		boolean rta = true;

		for (String itemDna : dna) {
			for (String itemPermitidas : permitidas) {
				itemDna = itemDna.replace(itemPermitidas, "");
			}
			if (itemDna.length() > 0)
				return false;
		}
		return rta;
	}

	/**
	 * Realiza la validación de las verticales de la matriz de la secuencia de ADN
	 * para encontrar el patrón mutante
	 * @param dna - secuencia de ADN a validar
	 * @return cantidad de coincidencias verticales encontradas
	 */
	private int verticales(ArrayList<String> dna) {
		ArrayList<char[]> matriz = new ArrayList<>();
		for (String cadena : dna) {
			char[] arreglofila = ConvertirCadenaArray(cadena);
			matriz.add(arreglofila);
		}
		char[] cs = matriz.get(0);
		int size = cs.length;
		int coincidencias = 0;
		for (int i = 0; i < size; i++) {
			char[] letrasEvaluada = new char[size];
			for (int j = 0; j < size; j++) {
				char[] cs2 = matriz.get(j);
				letrasEvaluada[j] = cs2[i];
			}

			coincidencias = coincidencias + coincidencias(letrasEvaluada);
		}
		return coincidencias;
	}

	/**
	 * Realiza la validación de las horizontales de la matriz de la secuencia de ADN
	 * para encontrar el patrón mutante
	 * @param dna - secuencia de ADN a validar
	 * @return cantidad de coincidencias horizontales encontradas
	 */
	private int horizontales(ArrayList<String> dna) {
		ArrayList<char[]> matriz = new ArrayList<>();
		for (String cadena : dna) {
			char[] arreglofila = ConvertirCadenaArray(cadena);
			matriz.add(arreglofila);
		}
		char[] cs = matriz.get(0);
		int size = cs.length;
		int coincidencias = 0;

		for (int i = 0; i < size; i++) {
			coincidencias = coincidencias + coincidencias(matriz.get(i));
		}
		return coincidencias;
	}

	/**
	 * Realiza la validación de las diagonales de la matriz de la secuencia de ADN
	 * para encontrar el patrón mutante
	 * @param dna - secuencia de ADN a validar
	 * @return cantidad de coincidencias diagonales encontradas
	 */
	private int diagonales(ArrayList<String> dna) {
		ArrayList<char[]> matriz = new ArrayList<>();
		for (String cadena : dna) {
			char[] arreglofila = ConvertirCadenaArray(cadena);
			matriz.add(arreglofila);
		}
		char[] cs = matriz.get(0);
		int size = cs.length;
		int coincidencias = 0;
		for (int i = 0; i < size; i++) {
			char[] letrasEvaluada = new char[size];
			for (int k = 0; k < size; k++) {
				char[] cs2 = matriz.get(k);
				if (k < size && k + i < size)
					letrasEvaluada[k] = cs2[k + i];
			}
			coincidencias = coincidencias + coincidencias(letrasEvaluada);

		}

		return coincidencias;
	}

	/**
	 * Realiza la verificación de la secuencia de 4 de la base nitrogenada 
	 * para determinar un mutante
	 * @param letrasEvaluada - letras a evaluar
	 * @return cantidad de coincidencias
	 */
	private int coincidencias(char[] letrasEvaluada) {
		int coincidencias = 0;
		for (String itemPermitidas : permitidas) {
			String str = String.valueOf(letrasEvaluada);
			char c = itemPermitidas.charAt(0);
			int cantidad = contarCaracteres(str, c);
			if (cantidad >= 4) {
				coincidencias++;
			}
		}
		return coincidencias;
	}

	/**
	 * Cuenta la cantidad de caracteres igual y la posición en la que se encuentran
	 * para determinar si es una secuencia mutante o no
	 * @param cadena - cadena de base nitrogenada a evaluar
	 * @param caracter - base nitrogenada a evaluar
	 * @return cantidad de caracteres iguales consecutivos
	 */
	private int contarCaracteres(String cadena, char caracter) {
		int posicion, contador = 0, consecutivo = 0;
		int[] posiciones = new int[cadena.length()];
		posicion = cadena.indexOf(caracter);
		while (posicion != -1) {
			posiciones[posicion] = posicion;
			contador++;
			posicion = cadena.indexOf(caracter, posicion + 1);
		}

		for (int i = 1; i < posiciones.length; i++) {
			if ((posiciones[i] - 1) == posiciones[i - 1]) {
				consecutivo++;
			}
		}

		if (consecutivo < 3)
			contador = 0;

		return contador;
	}

	/**
	 * convierte un String a char
	 * @param dna - String a convertir
	 * @return char
	 */
	private char[] ConvertirCadenaArray(String dna) {
		char[] cadenaArray = dna.toCharArray();
		return cadenaArray;
	}

	/**
	 * Realiza la consulta de todos los mutantes registrados en base de datos
	 * @return lista de mutantes
	 */
	private ArrayList<MutantModel> getMutants() {
		return (ArrayList<MutantModel>) mutantDAO.findAll();
	}

	/**
	 * Consulta las estadisticas de las verificaciones de ADN
	 * @return estadisticas en formato JSON
	 */
	public String getStats() {
		ArrayList<MutantModel> li = (ArrayList<MutantModel>) getMutants();

		int count_mutant_dna = 0;
		int count_human_dna = 0;
		double ratio = 0;

		for (MutantModel mutantModel : li) {
			if (mutantModel.getMutant() == 1) {
				count_mutant_dna++;
			} else {
				count_human_dna++;
			}
		}

		if (count_human_dna > 0) {
			ratio = (double) count_mutant_dna / (double) count_human_dna;
		}

		String rta = "{“count_mutant_dna”:" + count_mutant_dna + ", “count_human_dna”:" + count_human_dna + ": “ratio”:"
				+ ratio + "}";
		return rta;
	}

}
