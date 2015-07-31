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
		
		/*
		 * añado la condicion completa, lo q habia + lo nuevo y
		 * dejo los signos ya los cambiare en la propia transition
		 */
		this.condition = this.condition+ " " +pCondition;
		//transition.setConditionComp(transition.getConditionComp() + " " + text);

		/*
		 * la anado en la lista por separados para tener todas las
		 * senales por separado
		 */
		/*Le quito los espacios para luego poder usar 
		 * las expresiones regulares sin problema*/
		String aux = pCondition.trim();
		aux = aux.replaceAll("\\(|RE|\\)|NOT|FE", "");
		if (aux.contains("+") || aux.contains("*")) {			
			addListConditionSep(removeSigns(aux));
		} else {
			addListConditionSep(aux.trim());
		}
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
		String s="";
		LinkedList<String> aux= new LinkedList<String>();
		/*Para añadir expresiones regulares*/
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
		analyzeReviews();
		System.out.println("----- TRANSITION ------");
		System.out.println("Condicion completa: "+this.condition);
		System.out.println("Condicion por partes: ");
		for (String s : conditionSep) {
			System.out.println(s);
		}
		System.out.println("Comentario: "+this.comment);
	}
	/**Analiza el comentario de la transicion, lo descompone 
	 * para buscar posibles señales y asignaciones
	 * ARANTZA HA PEDIDO QUE SE PUEDAN COLOCAR ASIGNACONES EN LOS COMENTARIOS
	 * HABLAR, YA QUE EN EL CASO DE Q SE COLOQUE UNA ASIGNACION Y UN COMENTARIO JUNTOS
	 * HABRIA PROBLEMAS PARA DETECTARLO.*/
	public void analyzeReviews (){
		
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
		}
	}
	/** Quita el signo a el texto pasado y devuelve una lista */
	private LinkedList<String> removeSigns(String t) {

		LinkedList<String> aux = new LinkedList<String>();

		boolean itr = true;
		int posS = 0;
		String text = t.trim();

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

				/*
				 * Añado el texto desde la posicion 0 a la del signo ya q esta
				 * no se incluye
				 */
				aux.add(text.substring(0, posS).trim());

				/* Dejo en text el resto del string para seguirlo tratando */
				text = text.substring(posS + 1, text.length() + 1).trim();
			}
		}

		return aux;
	}

	
}
