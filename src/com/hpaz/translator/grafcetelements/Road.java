package com.hpaz.translator.grafcetelements;

public class Road {
	/**En esta clase registraremos las convergencias y divergencias de una secuencia a otra*/
	
	private String typeRoad;
	private int seqIni;
	private int seqOne;
	private int seqTwo;
	
	public Road() {
		//los inicializao a 0 para poder comparar ya que las secuencias siempre empezaran en 1
		seqOne=0;
		seqTwo=0;
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
}
