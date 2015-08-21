package com.hpaz.translator.grafcetelements;

import java.util.Map;

public class Jump {
	private int fromSeq;
	private int toSeq;

	public Jump() {
		this.fromSeq=0;
		this.toSeq=0;
	}

	public int getFromSeq() {
		return fromSeq;
	}

	public void setFromSeq(int pFromSeq) {
		this.fromSeq = pFromSeq - 1;
	}

	public int getToSeq() {
		return toSeq;
	}

	public void setToSeq(int pToSeq) {
		this.toSeq = pToSeq - 1;
	}

	public void fillAttributes(Map<String, String> pAttributes) {
		// anado desde y a donde
		this.fromSeq = Integer.parseInt(pAttributes.get("seqid_from"))-1;
		this.toSeq = Integer.parseInt(pAttributes.get("seqid_to"))-1;
	}

	/*public void printJump() {
		System.out.println("----- JUMP ------");
		System.out.println("Desde la secuencia : " + getFromSeq());
		System.out.println("A la secuencia: " + getToSeq());
	}*/

}
