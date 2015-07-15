package com.hpaz.translator.grafcetelements;

public class Jump {
  private int fromSeq;
  private int toSeq;
  
  	public Jump() {
	
  	}

	public int getFromSeq() {
		return fromSeq;
	}
	public void setFromSeq(int fromSeq) {
		this.fromSeq = fromSeq;
	}
	public int getToSeq() {
		return toSeq;
	}
	public void setToSeq(int toSeq) {
		this.toSeq = toSeq;
	}
	public void printJump(){
		System.out.println("----- JUMP ------");
		System.out.println("Desde la secuencia : "+this.fromSeq);
		System.out.println("A la secuencia: "+this.toSeq);
	}
  
}
