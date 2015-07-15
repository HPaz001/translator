package com.hpaz.translator.grafcetelements;


import java.util.LinkedList;


public class Transition {
	//private boolean fs;//flanco de subida
	//private boolean fb;// flanco de bajada
	private String conditionComp; //texto de condicion
	/**Implementa la interfaz Set. Se trata de una estructura de datos 
	 * desordenada que no permite duplicados. HashSet destaca por su eficiencia y 
	 * alta velocidad de acceso. Internamente utiliza una tabla Hash.*/
	private LinkedList<String> conditionSep;
	private String comment;
	//pensar en la negacion q puede estar en condition
	
	public Transition() {
		//this.fs=false;
		//this.fb=false;
		this.conditionComp="";
		this.conditionSep = new LinkedList<String>();
		this.comment="";
	}
/*	public boolean getFs() {
		return fs;
	}
	public void activateFs() {
		this.fs=true;
	}
	public boolean getFb() {
		return fb;
	}
	public void activateFb() {
		this.fb=true;
	}*/
	public String getConditionComp() {
		return conditionComp;
	}
	public void setConditionComp(String condition) {
		this.conditionComp = condition;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public LinkedList<String> getConditionSep() {
		return conditionSep;
	}
	public void addListConditionSep(LinkedList<String> l) {
		this.conditionSep.addAll(l);
	}
	public void addListConditionSep(String l) {
		this.conditionSep.add(l);
	}
	public LinkedList<String> printTransVG() {
		
		LinkedList<String> aux= new LinkedList<String>();
		/**/
		for (String cS : conditionSep) {
			if(!(cS.equals("=1")) && !(cS.equals("true"))){
				aux.add("\t"+cS+"\t: BOOL;\n");
			}
			
		}
		
		return aux;
	}
	/**Este metodo cambia los simbolos + o * del texto por AND y OR */
	private String changeSigns(String text) {

		if (text.indexOf("+") != -1) {
			text = text.replace("+", " OR ");
		}
		if (text.indexOf("*") != -1) {
			text = text.replace("*", " AND ");
		}

		return text;

	}
	public void printTransition(){
		System.out.println("----- TRANSITION ------");
		System.out.println("Condicion completa: "+this.conditionComp);
		System.out.println("Comentario: "+this.comment);
	}
	
}
