package com.hpaz.translator.grafcetelements;

import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hpaz.translator.grafcetelements.constants.GrafcetTagsConstants;

public class Transition {
	private String condition; // texto de condicion
	private LinkedList<String> conditionSep;
	private String comment;
	//private boolean assignmentSignal;
	//private Map<String,String> assignments;


	public Transition() {
		this.condition = "";
		this.conditionSep = new LinkedList<String>();
		this.comment = null;
		//this.assignmentSignal=false;
		
	}

	public String getCondition() {
		return condition;
	}

/*	public void addAssignmentSignal(boolean assignmentSignal) {
		this.assignmentSignal = assignmentSignal;
	}*/

	public void addCondition(String pCondition) {
		
		//Si no es 1 o true
		if (!(pCondition.equals("=1")) && !(pCondition.equals("true"))) {
			//Quito parentesos y negados
			String auxConditionSep = pCondition.replaceAll("\\(|\\)| NOT ", "");
			//Quito solo parentesis
			String auxConditionCompl = pCondition.replaceAll("\\(|\\)", "");
			
			//esta la uso para el RE y FE
			String auxString = auxConditionSep;
			
			//Si tiene signos lo mando a la funcion de separar
			if (auxString.contains("+") || auxString.contains("*")) {
				addListConditionSep(removeSigns(auxString));
				
				//Si no tiene signos
			}else {
				
				Pattern patCondSep = Pattern.compile("^Temp.*/X[0-9]./[0-9].*");
				Matcher matCondSep = patCondSep.matcher(auxConditionSep.trim());
				//Si es un temporizador lo mando a la funcio que lo trata
				if (matCondSep.matches()) {
					addTempToListProject(auxConditionSep.trim());
					
				//Si no es temporizador
				}else{				
					//añado a las lista d señales por separado sin el RE, FE
					addListConditionSep(auxConditionSep.replaceAll(" RE | FE ", "").trim());
				}			
			}
			
			Pattern patRE_FE = Pattern.compile(".* RE .*| .* FE .*");
			Matcher matRE_FE = patRE_FE.matcher(auxConditionCompl);
			//Si la palabra contiene un RE o FE
			if(matRE_FE.matches()){
				String aux_FE_RE = auxConditionCompl.replace(" ", "");
				if(!Project.getProject().getList_FE_and_RE().contains(aux_FE_RE)){
					Project.getProject().add_FE_and_RE(aux_FE_RE);
				}
				if(Project.getProject().getProgram().equalsIgnoreCase(GrafcetTagsConstants.PROGRAM_OPT1)){
					auxConditionCompl= aux_FE_RE+".Q";
				}
			}
			//Añado la condicion sin quitarle nada
			this.condition = getCondition() + " " + changeSign(auxConditionCompl);
			
		}
	}

	public String getComment() {
		return comment;
	}

	public void addComment(String pComment) {
		// le quito los espacios en blanco antes de guardarlo
		if (getComment() == null){
			this.comment = pComment;
		}else {
			this.comment = getComment() + " " + pComment;
		}

	}

	public LinkedList<String> getConditionSep() {
		return conditionSep;
	}

	private void addListConditionSep(LinkedList<String> l) {
		this.conditionSep.addAll(l);
	}

	private void addListConditionSep(String l) {
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

/*	public void printTransition() {
		System.out.println("----- TRANSITION ------");
		System.out.println("Condicion completa: " + this.condition);
		System.out.println("Condicion por partes: ");
		for (String s : conditionSep) {
			System.out.println(s);
		}
		System.out.println("Comentario: " + this.comment);
	}
*/
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
		if (this.comment != null) {
			/* le quito los */
			String aux = this.comment.replaceAll("\\(\\*|\\*\\)", "");
			// Separo por coma, es decir en cada linea habra una unica asignacion
			String[] list = aux.split(",");
			/* analizo el comentario al añadirlo para saber si contiene señales
			 nuevas a añadir a la lista*/
			for (int i = 0; i < list.length; i++) {
				
				Pattern pat = Pattern.compile(".*:=.*");
				Matcher mat = pat.matcher(list[i]);
				//Si tiene una asignacion parte1:=parte2
				if (mat.matches()) {
					//guardo la parte1
					String string1 = list[i].substring(0, list[0].indexOf(":=")).trim();
					//guardo la parte2
					String string2 = list[i].substring(list[0].indexOf(":=")+2, list[0].length()).trim();
					
					//le cambio los signos por AND u OR
					string1=changeSign(string1);
					string2=changeSign(string2);
					
					//Guardo en la lista de asignaciones del proyecto
					Project.getProject().addAssignments(string1,string2);
					
					//Separo ambas partes para añadir en la lista de señales 
					String [] listSep = list[i].trim().split(":=|==|\\*|\\+|\\·");
					
					for (int j = 0; j < listSep.length; j++) {
						
						Pattern patS = Pattern.compile("^[0-9]{1,}|^X[0-9]{1,}$");
						String auxString =listSep[j];
						Matcher matS = patS.matcher(auxString.trim());
						//Si no es un numero solo o una XNumero
						if(!matS.matches()){
							//Para deterctar RE y FE
							Pattern patRE_FE = Pattern.compile(".* RE .*| .* FE .*");
							Matcher matRE_FE = patRE_FE.matcher(listSep[j]);
							//Si contiene un RE o FE
							if(matRE_FE.matches()){
								//Quito los espacios
								String aux_FE_RE = auxString.replace(" ", "");
								
								//Si el elemento no esta en la lista de FE-RE del proyecto
								if(!Project.getProject().getList_FE_and_RE().contains(aux_FE_RE)){
									//Añado a la lista de FE_and_RE del proyecto
									Project.getProject().add_FE_and_RE(aux_FE_RE);
								}
							}
							//Quito los NOT, RE o FE para añadir a la lista de señales del proyecto
							String signal = listSep[j].replaceAll(" NOT | RE | FE ", "");
							addListConditionSep(signal.trim());
						}
						
					}
							
				}
			}

		}
	}
	/**Este metodo creara un Map de asignaciones en caso de que la transicion tenga
	private void assignment(String pSignal, String pAssignment) {
		// TODO REVISAR 
		if(this.assignments==null){
			this.assignments = new HashMap<String, String>();
			this.assignmentSignal=true;
		}
		this.assignments.put(pSignal, pAssignment);
		
	}

	public boolean isAssignmentSignal() {
		return assignmentSignal;
	}*/

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
	/**Modifico los signos + * por OR AND*/
	private String changeSign (String pString){
		String s = pString;
		s = s.replace(" * ", " AND ");
		s = s.replace(" + ", " OR ");
		return s;
	}
	
	/**Separa la transicion del temporizador y crea el temporizador con los valores correspondientes*/
	private void addTempToListProject(String pString) {
		
		String[] listTemp = pString.split("/");
		
		// busco el indice del temp si ya esta en la lista
		int index = Project.getProject().equalsTimer(listTemp[0]);
		// Si el temporizador ya esta en la lista solo añado la etapa
		if (index != (-1)) {
			Project.getProject().getListTimers().get(index).addStepNameTimer(listTemp[1]);
			// Si no esta en la lista de temporizadores creo uno nuevo y lo
			// añado
		} else {
			Timer timer = new Timer();
			timer.addNameTimer(listTemp[0]);
			timer.fillTimer(listTemp);
			Project.getProject().addTimer(timer);

		}
	}
}
