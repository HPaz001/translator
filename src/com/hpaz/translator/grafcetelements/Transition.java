package com.hpaz.translator.grafcetelements;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Transition {
	private String condition; //texto de condicion
	private LinkedList<String> conditionSep;
	private String comment;
	
	public Transition() {
		this.condition="";
		this.conditionSep = new LinkedList<String>();
		this.comment="";
	}
	public String getCondition() {
		return condition;
	}
	public void addCondition(String pCondition) {
		if(!(pCondition.equals("=1")) && !(pCondition.equals("true"))){
				/*
				 * aÃ±ado la condicion completa, lo q habia + lo nuevo y
				 * dejo los signos ya los cambiare en la propia transition
				 */
				this.condition = this.condition+ " " +pCondition;
		
				/*Añado las señales por separado a la lista*/
				/*Quito los espacios */
				String aux = pCondition.trim();
				/*Quito los prefijos*/
				aux = aux.replaceAll("\\(|RE|\\)|NOT|FE", "");
				if (aux.contains("+") || aux.contains("*")) {			
					addListConditionSep(removeSigns(aux));
				} else {
					addListConditionSep(aux.trim());
				}			
		}
	}
	
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		//le quito los espacios en blanco antes de guardarlo
		this.comment = comment.trim();
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
		String s="";
		LinkedList<String> aux= new LinkedList<String>();
		/*Para aÃ±adir expresiones regulares*/
		Pattern pat = Pattern.compile("^Temp.*");
		for (String cS : conditionSep) {
			if(!(cS.equals("=1")) && !(cS.equals("true"))){
				Matcher mat = pat.matcher(cS);
				if(!mat.matches()){
					s="\t"+cS+"\t: BOOL;\n";
					aux.add(s);
				}
			}
			
		}
		
		return aux;
	}
	
	
	public void printTransition(){
		System.out.println("----- TRANSITION ------");
		System.out.println("Condicion completa: "+this.condition);
		System.out.println("Condicion por partes: ");
		for (String s : conditionSep) {
			System.out.println(s);
		}
		System.out.println("Comentario: "+this.comment);
	}
	/**Analiza el comentario de la transicion, lo descompone 
	 * para buscar posibles seÃ±ales y asignaciones
	 * ARANTZA HA PEDIDO QUE SE PUEDAN COLOCAR ASIGNACONES EN LOS COMENTARIOS
	 * HABLAR, YA QUE EN EL CASO DE Q SE COLOQUE UNA ASIGNACION Y UN COMENTARIO JUNTOS
	 * HABRIA PROBLEMAS PARA DETECTARLO.*/
	public void analyzeReviews (){
		/*TODO si un comentario tiene mas de una asignacion debe ir separado por coma
		al igual que si tiene texto normal deltro del comentario despues de una asignacion*/
		if(!this.comment.isEmpty()){
			/*le quito los */
			String aux = this.comment.replaceAll("\\(\\*|\\*\\)", "");
			//Si tiene coma separo en un array co slip
			String [] list = aux.split(",");
			//analizo el comentario al añadirlo para saber si contiene señales nuevas a añadir a la lista
			for (int i = 0; i < list.length; i++) {
				Pattern pat = Pattern.compile(".*:=.*");
				Matcher mat = pat.matcher(list[i]);
				if(mat.matches()){
					//si tiene una asignacion añado a la lista cond sep
					addListConditionSep(list[i].trim());					
				}
			}
			
		}
		
		/*
		if(!getComment().isEmpty()){
			//Si es un comentario llevara obligatoriamente (* comentario *) por lo q le quito
			String aux = getComment().substring(3, getComment().length()-2);
			int index =aux.indexOf(":=");
			
			if(index > (-1)){
				System.err.println(aux);
				String aux1= aux.substring(1, index);
				aux=aux.substring(index+2,aux.length());
				System.err.println(aux1);
				System.err.println(aux);
			}	
		}*/
	}
	/** Quita el signo a el texto pasado y devuelve una lista */
	private LinkedList<String> removeSigns(String t) {

		LinkedList<String> aux = new LinkedList<String>();
		//Quito espacios
		String text = t.trim();
		//Remplazo los signos por coma
		text = text.replaceAll("\\+|\\*|\\·", ",");
		//convierto en un array de string
		String[] list  = text.split(",");
		//paso el array a la lista
		for (int i = 0; i < list.length; i++) {
			aux.add(list[i]);
			
		}
		/*
		 * boolean itr = true;
		int posS = 0;
		while (itr) {
			
			if (text.indexOf("+") == (-1) || text.indexOf("*") == (-1)) {

				itr = false;
				aux.add(text);

			} else {
				// si es un signo mas coloco en poss la posicion donde esta
				if (text.indexOf("+") != (-1)) {
					posS = text.indexOf("+");

					// si es un signo por coloco en poss la posicion donde esta
				} else if (text.indexOf("*") != (-1)) {
					posS = text.indexOf("*");
				}


				aux.add(text.substring(0, posS).trim());

				// Dejo en text el resto del string para seguirlo tratando 
				text = text.substring(posS + 1, text.length() + 1).trim();
			}
		}*/

		return aux;
	}

	
}
