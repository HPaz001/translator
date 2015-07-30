package com.hpaz.translator.grafcetelements;


import java.awt.SystemColor;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Transition {
	private String conditionComp; //texto de condicion
	private LinkedList<String> conditionSep;
	private String comment;
	
	public Transition() {
		this.conditionComp="";
		this.conditionSep = new LinkedList<String>();
		this.comment="";
	}
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
		System.out.println("Condicion completa: "+this.conditionComp);
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
	
}
