package com.hpaz.translator.grafcetelements;

import java.util.LinkedList;

public class Grafcet {
	
	private String type;
	private String name;
	private String comment;
	private String owner; // propietario
	private LinkedList<Jump> listJ;
	private LinkedList<Sequence> listS;
	private LinkedList<Road> listR;
	
	public Grafcet() {
		this.type="";
		this.name="";
		this.comment="";
		this.owner="";
		listJ = new LinkedList<Jump>();
		listS= new LinkedList<Sequence>();
		listR = new LinkedList<Road>();
		
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public LinkedList<Jump> getListJ() {
		return listJ;
	}
	public void addJump(Jump pJ) {
		this.listJ.add(pJ);
	}
	public LinkedList<Sequence> getListS() {
		return listS;
	}
	public void addSeq(Sequence pS) {
		this.listS.add(pS);
	}
	public LinkedList<Road> getListR() {
		return listR;
	}
	public void addRoad(Road pR) {
		this.listR.add(pR);
	}
	public LinkedList<String> printGrafcetVarGlobalStages() {
		LinkedList<String> vG = new LinkedList<String>();
		vG.add("\n\t(*---"+this.name+"---*)\n\n");
		/*if(!this.owner.equals("")){
			vG.add("(* Propietario: "+this.owner+"*)\n");
		}
		if(!this.comment.equals("")){
			vG.add("(*"+this.comment+"*)\n");
		}
		ESTO SE USARA EN EL SET Y RESET*/
		
		//por cada secuencia de la lista
		for (Sequence s : listS) {
			vG.addAll(s.printSeqVG());
		}
		
		return vG;
	}
	public LinkedList<String> printGrafcetVarGlobalSignals() {
		LinkedList<String> vG = new LinkedList<String>();
		for (Sequence s : listS) {
			vG.addAll(s.getSignals());
		}
		return vG;
	}
	//genera un listado con los set y reset listos para exportar
	public LinkedList<String> printSetReset() {
		LinkedList<String> sR = new LinkedList<String>();
		for (Sequence s : listS) {
			sR.addAll(s.getSetReset());
		}
		return sR;
	}
	
}
