package com.hpaz.translator.grafcetelements;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Transition {
	private String condition; // texto de condicion
	private LinkedList<String> conditionSep;
	private String comment;

	public Transition() {
		this.condition = "";
		this.conditionSep = new LinkedList<String>();
		this.comment = "";
	}

	public String getCondition() {
		return condition;
	}

	public void addCondition(String pCondition) {
		if (!(pCondition.equals("=1")) && !(pCondition.equals("true"))) {
			/*
			 * aÃ±ado la condicion completa, lo q habia + lo nuevo y dejo los
			 * signos ya los cambiare en la propia transition
			 */
			this.condition = this.condition + " " + pCondition;

			/* Añado las señales por separado a la lista */
			/* Quito los espacios */
			String aux = pCondition.trim();
			/* Quito los prefijos */
			aux = aux.replaceAll("\\(|RE|\\)|NOT|FE", "");
			if (aux.contains("+") || aux.contains("*")) {
				addListConditionSep(removeSigns(aux));
			} else {
				Pattern pat = Pattern.compile("^Temp.*/X[0-9]./[0-9].*");
				Matcher mat = pat.matcher(aux.trim());
				if (mat.matches()) {
					addTempToListProject(aux.trim());
				}
				addListConditionSep(aux.trim());
			}
		}
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		// le quito los espacios en blanco antes de guardarlo
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
		String s = "";
		LinkedList<String> aux = new LinkedList<String>();
		/* Para aÃ±adir expresiones regulares */
		for (String cS : conditionSep) {
			if (!(cS.equals("=1")) && !(cS.equals("true"))) {
				s = "\t" + cS + "\t: BOOL;\n";
				aux.add(s);
			}

		}

		return aux;
	}

	public void printTransition() {
		System.out.println("----- TRANSITION ------");
		System.out.println("Condicion completa: " + this.condition);
		System.out.println("Condicion por partes: ");
		for (String s : conditionSep) {
			System.out.println(s);
		}
		System.out.println("Comentario: " + this.comment);
	}

	/**
	 * Analiza el comentario de la transicion, lo descompone para buscar
	 * posibles seÃ±ales y asignaciones ARANTZA HA PEDIDO QUE SE PUEDAN COLOCAR
	 * ASIGNACONES EN LOS COMENTARIOS HABLAR, YA QUE EN EL CASO DE Q SE COLOQUE
	 * UNA ASIGNACION Y UN COMENTARIO JUNTOS HABRIA PROBLEMAS PARA DETECTARLO.
	 */
	public void analyzeReviews() {
		/*
		 * TODO si un comentario tiene mas de una asignacion debe ir separado
		 * por coma al igual que si tiene texto normal deltro del comentario
		 * despues de una asignacion
		 */
		if (!this.comment.isEmpty()) {
			/* le quito los */
			String aux = this.comment.replaceAll("\\(\\*|\\*\\)", "");
			// Si tiene coma separo en un array co slip
			String[] list = aux.split(",");
			// analizo el comentario al añadirlo para saber si contiene señales
			// nuevas a añadir a la lista
			for (int i = 0; i < list.length; i++) {
				Pattern pat = Pattern.compile(".*:=.*");
				Matcher mat = pat.matcher(list[i]);
				if (mat.matches()) {
					// si tiene una asignacion añado a la lista cond sep
					String [] listSep = list[i].trim().split(":=|==|\\*");//TODO REVISAR
					for (int j = 0; j < listSep.length; j++) {
						Pattern patt = Pattern.compile("[0-9]|X[0-9]|[0-9][a-z A-Z]");
						Matcher matt = pat.matcher(listSep[i]);
						if(!matt.matches()){
							addListConditionSep(listSep[i]);
						}
						
					}
							
				}
			}

		}

		/*
		 * if(!getComment().isEmpty()){ //Si es un comentario llevara
		 * obligatoriamente (* comentario *) por lo q le quito String aux =
		 * getComment().substring(3, getComment().length()-2); int index
		 * =aux.indexOf(":=");
		 * 
		 * if(index > (-1)){ System.err.println(aux); String aux1=
		 * aux.substring(1, index); aux=aux.substring(index+2,aux.length());
		 * System.err.println(aux1); System.err.println(aux); } }
		 */
	}

	/** Quita el signo a el texto pasado y devuelve una lista */
	private LinkedList<String> removeSigns(String t) {

		LinkedList<String> aux = new LinkedList<String>();
		// Quito espacios
		String text = t.trim();
		// Remplazo los signos por coma
		text = text.replaceAll("\\+|\\*|\\·", ",");
		// convierto en un array de string
		String[] list = text.split(",");
		// paso el array a la lista
		for (int i = 0; i < list.length; i++) {
			Pattern pat = Pattern.compile("^Temp.*/X[0-9]./[0-9].*");
			Matcher mat = pat.matcher(list[i]);
			if (mat.matches()) {
				addTempToListProject(list[i]);
			}
			aux.add(list[i]);
		}

		return aux;
	}

	private void addTempToListProject(String pString) {
		Timer timer = new Timer();
		String[] listTemp = pString.split("/");
		timer.addNameTimer(listTemp[0]);
		// busco el indice del temp si ya esta en la lista
		int index = Project.getProject().equalsTimer(timer);
		// Si el temporizador ya esta en la lista solo añado la etapa
		if (index != (-1)) {
			Project.getProject().getListTimers().get(index).addStepNameTimer(listTemp[1]);
			// Si no esta en la lista de temporizadores creo uno nuevo y lo
			// añado
		} else {
			timer.fillTimer(listTemp);
			Project.getProject().addTimer(timer);

		}
	}
}
