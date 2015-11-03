package com.hpaz.translator.grafcetelements;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hpaz.translator.grafcetelements.constants.GrafcetTagsConstants;

public class Transition {
	/** texto de la condicion */
	private String condition;
	/**
	 * Lista de las condiciones individuales, ya que en una transicion puede
	 * haber mas de una condicion
	 */
	private LinkedList<String> conditionSep;
	/** comentario de la condicion */
	private String comment;

	public Transition() {
		this.condition = "";
		this.conditionSep = new LinkedList<String>();
		this.comment = null;
	}

	public String getCondition() {
		return condition;
	}

	public void addCondition(String pCondition) {
		// Si no es 1 o true
		if (!(pCondition.equals("=1")) && !(pCondition.equals("true"))) {
			// Quito parentesos y negados
			String auxConditionSep = pCondition.replaceAll("\\(|\\)| NOT ", "");
			// Quito solo parentesis
			String auxConditionCompl = pCondition.replaceAll("\\(|\\)", "");

			// esta la uso para el RE y FE
			String auxString = auxConditionSep;

			// Si tiene signos lo mando a la funcion de separar
			if (auxString.contains("+") || auxString.contains("*") || auxString.contains("·")
					|| auxString.contains(".")) {
				addListConditionSep(removeSigns(auxString));

				// Si no tiene signos
			} else {

				Pattern patCondSep = Pattern.compile("^TM\\-.*/X[0-9]./[0-9].*");
				Matcher matCondSep = patCondSep.matcher(auxConditionSep.trim());
				// Si No es un temporizador
				if (!matCondSep.matches()) {
					// añado a las lista d señales por separado sin el RE, FE
					addListConditionSep(auxConditionSep.replaceAll(" RE | FE ", "").trim());
				}
			}

			Pattern patRE_FE = Pattern.compile(".*. RE .*|.*. FE .*");
			Matcher matRE_FE = patRE_FE.matcher(auxConditionCompl);
			// Si la palabra contiene un RE o FE
			if (matRE_FE.matches()) {
				String aux_FE_RE = auxConditionCompl.replace(" ", "");
				if (!Project.getProject().getList_FE_and_RE().contains(aux_FE_RE)) {
					Project.getProject().add_FE_and_RE(aux_FE_RE);
				}
				if (Project.getProject().getProgram().equalsIgnoreCase(GrafcetTagsConstants.PROGRAM_OPT1)) {
					auxConditionCompl = aux_FE_RE + "Q";
				}
			}
			// Añado la condicion sin quitarle nada
			this.condition = getCondition() + " " + changeSign(auxConditionCompl).trim();

		}
	}

	public String getComment() {
		return comment;
	}

	public void addComment(String pComment) {
		if (getComment() == null) {
			this.comment = pComment;
		} else {
			this.comment = getComment() + " " + pComment;
		}

	}

	public LinkedList<String> getConditionSep() {
		return conditionSep;
	}

	public LinkedList<String> printTransVG() {
		String s = "";
		LinkedList<String> aux = new LinkedList<String>();
		/* Para aÃ±adir expresiones regulares */
		for (String cS : conditionSep) {
			if (!(cS.equals("=1")) && !(cS.equals("true"))) {
				s = "\t" + cS.trim() + "\t: BOOL;\n";
				aux.add(s);
			}

		}

		return aux;
	}

	/**
	 * Analiza el comentario de la transicion, lo descompone para buscar
	 * posibles se#ales y asignaciones si un comentario tiene mas de una
	 * asignacion debe ir separado por coma al igual que si tiene texto normal
	 * dentro del comentario despues de una asignacion
	 */
	public void analyzeReviews() {

		if (this.comment != null) {
			/* le quito los */
			String aux = this.comment.replaceAll("\\(|\\*|\\)", "");
			// Separo por coma, es decir en cada linea habra una unica
			// asignacion
			String[] list = aux.split(",");
			/*
			 * analizo el comentario al añadirlo para saber si contiene señales
			 * nuevas a añadir a la lista
			 */
			for (int i = 0; i < list.length; i++) {
				// para detectar asignaciones
				Pattern pat = Pattern.compile(".*.:=.*|.*.=.*");
				Matcher mat = pat.matcher(list[i]);
				// Si tiene una asignacion parte1:=parte2 o parte1=parte2
				if (mat.matches()) {
					int index = list[0].indexOf("=");
					// compruebo que el index no sea -1
					if (index != -1) {
						// guardo la parte1
						String string1 = list[i].substring(0, index);
						// guardo la parte2
						String string2 = list[i].substring(index + 1, list[0].length());

						// le cambio los signos por AND u OR
						string1 = changeSign(string1);
						string2 = changeSign(string2);

						// Guardo en la lista de asignaciones del proyecto
						Project.getProject().addAssignments(string1, string2);

						// Separo ambas partes para añadir en la lista de
						// señales individuales de la transicion
						String[] listSep = list[i].trim().split(":=|==|\\*|\\+|\\·|\\.");
						// por cada elemento del array
						for (int j = 0; j < listSep.length; j++) {
							String auxString = listSep[j].trim();
							//para comprobar si es una etapa
							Pattern patS = Pattern.compile("^[0-9]{1,}|^X[0-9]{1,}$");
							Matcher matS = patS.matcher(auxString);
							// Si no es una etapa
							if (!matS.matches()) {
								// Para deterctar RE y FE
								Pattern patRE_FE = Pattern.compile("^.*.RE .*|^.*.FE .*");
								Matcher matRE_FE = patRE_FE.matcher(listSep[j]);
								// Si contiene un RE o FE
								if (matRE_FE.matches()) {
									// Quito los espacios
									String aux_FE_RE = auxString.replace(" ", "");
									// Si el elemento no esta en la lista de
									// FE-RE  del proyecto
									if (!Project.getProject().getList_FE_and_RE().contains(aux_FE_RE)) {
										// Añado a la lista de FE_and_RE del proyecto
										Project.getProject().add_FE_and_RE(aux_FE_RE);
									}
								}
								// Quito los NOT, RE o FE para añadir a la lista
								// de señales del proyecto
								String signal = listSep[j].replaceAll(" NOT | RE | FE | ", "");
								if (!signal.equals("")) {
									addListConditionSep(signal);
								}

							}

						}

					}

				}
			}

		}
	}

	private void addListConditionSep(LinkedList<String> l) {
		this.conditionSep.addAll(l);
	}

	private void addListConditionSep(String l) {
		this.conditionSep.add(l);
	}

	/** Quita el signo a el texto pasado y devuelve una lista */
	private LinkedList<String> removeSigns(String pText) {

		LinkedList<String> aux = new LinkedList<String>();
		// Quito espacios
		String text = pText.trim();
		// Remplazo los signos por coma
		text = text.replaceAll("\\+|\\*|\\·|\\.", ",");
		// convierto en un array de string
		String[] list = text.split(",");
		// paso el array a la lista
		for (int i = 0; i < list.length; i++) {
			// Para comprobar si es un temporizador
			Pattern pat = Pattern.compile("^TM\\-.*/X[0-9]./[0-9].*");
			Matcher mat = pat.matcher(list[i]);
			
			// Para comprobar si es un contador
			Pattern pat1 = Pattern.compile("^CT\\-.*.\\>[0-9]|^CT\\-.*.\\>[0-9]|^CT\\-.*.\\<\\>[0-9]");
			Matcher mat1 = pat1.matcher(list[i]);
			
			// si no es temporizador ni contador
			if (!mat.matches() && !mat1.matches() ) {
				aux.add(list[i]);
			}
			
		}

		return aux;
	}

	/** Modifico los signos + * por OR AND */
	private String changeSign(String pString) {
		//quito los expacios del inicio y fin
		String s = pString.trim();
		s = s.replaceAll("\\*|\\·|\\.", " AND ");
		s = s.replace("+", " OR ");
		Pattern patS = Pattern.compile("^.*. AND $");
		Matcher matS = patS.matcher(s);
		// Si no es una etapa
		if (matS.matches()) {
			s = s.replaceAll(" AND $", "");
		}else {
			Pattern pat = Pattern.compile("^ AND .*");
			Matcher mat = pat.matcher(s);
			if(mat.matches()){
				s = s.replaceAll("^ AND", "");
			}
		}
		return s;
	}

	/**
	 * Separa la transicion del temporizador y crea el temporizador con los
	 * valores correspondientes
	 */
	/*private void addTempToListProject(String pString) {
		// creo un array
		String[] listTemp = pString.split("/");
		String nameTemp= listTemp[0].substring(2);
		// busco el indice del temp en la lista
		int index = Project.getProject().equalsTimer(nameTemp);
		// Si el temporizador ya esta en la lista solo añado la etapa
		if (index != (-1)) {
			Project.getProject().getListTimers().get(index).addStepNameTimer(listTemp[1]);
		} else {
			Timer timer = new Timer();
			timer.addNameTimer(listTemp[0]);
			timer.fillTimer(listTemp);
			Project.getProject().addTimer(timer);

		}
	}*/
}
