package com.hpaz.translator.grafcetelements;

import java.util.LinkedList;

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

	private LinkedList<Integer> mySequences;

	public Road() {
		this.mySequences = new LinkedList<Integer>();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getSeqIni() {
		return seqIni;
	}

	public void setSeqIni(int seqIni) {
		this.seqIni = seqIni - 1;
	}

	/*
	 * public int getSeqOne() { return seqOne; } public void setSeqOne(int
	 * seqOne) { this.seqOne = seqOne; } public int getSeqTwo() { return seqTwo;
	 * } public void setSeqTwo(int seqTwo) { this.seqTwo = seqTwo; }
	 */
	public LinkedList<Integer> getMySequences() {
		return mySequences;
	}

	public void addSequences(Integer pSequence) {
		this.mySequences.add(pSequence - 1);
	}

	public void printRoad() {
		System.out.println("----- ROAD ------");
		System.out.println("Secuencia inicial: " + this.seqIni);
		for (Integer seq : mySequences) {
			// uso el indexOf porque se que no se repiten los elementos
			System.out.println("Seccuencia " + (mySequences.indexOf(seq) + 1) + " : " + seq);
		}
		// System.out.println("Seccuencia 1 : "+this.seqOne);
		// System.out.println("Seccuencia 2 : "+this.seqTwo);
		System.out.println("Tipo camino: " + this.type);

	}
}
