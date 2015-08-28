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


	/*
	 * public int getSeqOne() { return seqOne; } public void setSeqOne(int
	 * seqOne) { this.seqOne = seqOne; } public int getSeqTwo() { return seqTwo;
	 * } public void setSeqTwo(int seqTwo) { this.seqTwo = seqTwo; }
	 */
	public LinkedList<Integer> getSequences() {
		return sequences;
	}

	public void addSequences(Integer pSequence) {
		this.sequences.add(pSequence - 1);
	}

/*	public void printRoad() {
		System.out.println("----- ROAD ------");
		System.out.println("Secuencia inicial: " + this.seqIni);
		for (Integer seq : getSequences()) {
			// uso el indexOf porque se que no se repiten los elementos
			System.out.println("Seccuencia " + (sequences.indexOf(seq) + 1) + " : " + seq);
		}
		// System.out.println("Seccuencia 1 : "+this.seqOne);
		// System.out.println("Seccuencia 2 : "+this.seqTwo);
		System.out.println("Tipo camino: " + this.type);

	}
*/
	
}
