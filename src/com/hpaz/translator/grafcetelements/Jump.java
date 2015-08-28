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

	private void addFromSeq(int pFromSeq) {
		this.fromSeq = pFromSeq;
	}

	public int getToSeq() {
		return toSeq;
	}

	private void addToSeq(int pToSeq) {
		this.toSeq = pToSeq;
	}

	public void fillAttributes(Map<String, String> pAttributes) {
		// anado desde y a donde
		addFromSeq(Integer.parseInt(pAttributes.get("seqid_from"))-1);
		addToSeq (Integer.parseInt(pAttributes.get("seqid_to"))-1);
	}

	/*public void printJump() {
		System.out.println("----- JUMP ------");
		System.out.println("Desde la secuencia : " + getFromSeq());
		System.out.println("A la secuencia: " + getToSeq());
	}*/

}
