package com.hpaz.translator.grafcetelements;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

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
		this.listJ = new LinkedList<Jump>();
		this.listS= new LinkedList<Sequence>();
		this.listR = new LinkedList<Road>();
		
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
	/**Añade la secuencia en la misma posición que el id de la seq*/
	public void addSeq(Sequence pS) {
		this.listS.add(pS.getIdSeq(), pS);;
	}
	public LinkedList<Road> getListR() {
		return listR;
	}
	public void addRoad(Road pR) {
		this.listR.add(pR);
	}
	public LinkedList<String> grafcetVarGlobalStages() {
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
	public LinkedList<String> grafcetVarGlobalSignals() {
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
	public void printGrafcet(){
		System.out.println("----- GRAFCET ------");
		System.out.println("Nombre: "+this.name);
		System.out.println("Tipo: "+this.type);
		System.out.println("Propietario: "+this.owner);
		System.out.println("Comentario: "+this.comment);
		for (Jump j : listJ) {
			j.printJump();
		}
		for (Road r : listR) {
			r.printRoad();		
		}
		for (Sequence s : listS) {
			s.printSequence();
		}
	}
	
	/**Este metodo devuelve la primera parte del PROGRAM MAIN */
	public String printVar(){
		String n = this.name.substring(1, 0);
		
		/*Ejemplo:
		 * 	SecPrincipal		: GSecPrincipal;
			InitSecPrincipal	:BOOL;
			ResetSecPrincipal	:BOOL;
		 * */                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 
		String v = "\t\t"+n+"\t:"+this.name+";\n";
		v=v+"\t\tInit"+n+"\t: BOOL;\n";
		v=v+"\t\tReset"+n+"\t: BOOL;\n";
		
		return v;
	}
	
	/**Este metodo devuelve la segunda parte del PROGRAM MAIN */
	public Map<String, String> getActionStepMap(){
		/*AQUI MIRO CADA SECUENCIA DEL GRAFCET Y SUS TRANSICIONES Y STEP
		 * EN STEP --> ACTION:=NAME;
		 * EN TRANSITION --> TRANSITION MIRAR Y HABLAR SOLO PARACE Q NECESITO LA MARCHA Y EL PARO*/
		
		Map<String, String> actionStepMap = new HashMap<String, String>();
		
		//por cada secuencia del grafcet
		for (Sequence sequence : listS) {
				//obtengo el map de la secuencia
				Map<String, String> auxMap =  sequence.getActionStepMap();
				//por cada accion de auxMap
				for (String action : auxMap.keySet()){
					//si la key no existe en actionStepMap
					if (actionStepMap.get(action) == null){
						//añado 
						actionStepMap.put(action, auxMap.get(action));
					}else{
						//si existe solo modifico el value
						actionStepMap.put(action, actionStepMap.get(action) + " OR " + auxMap.get(action));
					}
				}
		}
		
		return actionStepMap;	
	}
	
	private void setAndReset(){
		
		
		/*Miro si tiene caminos*/
		if(listR.size()!=0){
			//dependiendo de cuantos caminos tenga hare una cosa u otra?
			
		}
	}
	
	
}
