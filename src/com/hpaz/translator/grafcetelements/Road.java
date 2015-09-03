package com.hpaz.translator.grafcetelements;

import java.util.LinkedList;
import java.util.Map;

public class Road {
	/**
	 * En esta clase registraremos las convergencias y divergencias de una
	 * secuencia a otra
	 */

	private String type;
	private int seqIni;

	/*
	 * BEDE SER UNA LISTA YA QUE PUEDE TENER MAS SEQ private int seqOne; private
	 * int seqTwo;
	 */

	private LinkedList<Integer> sequences;

	public Road() {
		this.sequences = new LinkedList<Integer>();
	}
	public void fillAttributes(Map<String, String> pAttributes) {
		// Si hay una convergencia o divergencia estara en la etiketa hlink,
					// guardaremos si es diver o conver
					// guardo en road el tipo, de q secuencia viene
		addSeqIni (Integer.parseInt(pAttributes.get("seqid"))-1);
		addType(pAttributes.get("type"));	
	}

	private void addType(String pString) {
		this.type=pString;
		
	}
	private void addSeqIni(int pNumber) {
		this.seqIni =pNumber;
		
	}
	public String getType() {
		return type;
	}


	public int getSeqIni() {
		return seqIni;
	}


	/**Devuelve una lista con el numero de cada secuencia */
	public LinkedList<Integer> getSequences() {
		return sequences;
	}
	/**Guarda el numero de cada secuencia -1 para que corresponda con su posicion en la lista*/
	public void addSequences(Integer pSequence) {
		this.sequences.add(pSequence - 1);
	}


	
}
