package com.hpaz.translator.grafcetelements;

import java.util.LinkedList;

public class Road {
	/**En esta clase registraremos las convergencias y divergencias de una secuencia a otra*/
	
	private String typeRoad;
	private int seqIni;
	
	/*BEDE SER UNA LISTA YA QUE PUEDE TENER MAS SEQ*/
	private int seqOne;
	private int seqTwo;
	
	private LinkedList<Integer> mySequences;
	
	public Road() {
		this.mySequences = new LinkedList<Integer>();
	}
	
	public String getTypeRoad() {
		return typeRoad;
	}
	public void setTypeRoad(String typeRoad) {
		this.typeRoad = typeRoad;
	}
	public int getSeqIni() {
		return seqIni;
	}
	public void setSeqIni(int seqIni) {
		this.seqIni = seqIni;
	}
	public int getSeqOne() {
		return seqOne;
	}
	public void setSeqOne(int seqOne) {
		this.seqOne = seqOne;
	}
	public int getSeqTwo() {
		return seqTwo;
	}
	public void setSeqTwo(int seqTwo) {
		this.seqTwo = seqTwo;
	}
	public LinkedList<Integer> getMySequences() {
		return mySequences;
	}

	public void addSequences(LinkedList<Integer> mySequences) {
		this.mySequences = mySequences;
	}
	
	public void printRoad(){
		System.out.println("----- ROAD ------");
		System.out.println("Secuencia inicial: "+this.seqIni);
		for (Integer integer : mySequences) {
			System.out.println("Seccuencia 1 : "+this.seqOne);
		}
		System.out.println("Seccuencia 1 : "+this.seqOne);
		System.out.println("Seccuencia 2 : "+this.seqTwo);
		System.out.println("Tipo camino: "+this.typeRoad);

	}
}
