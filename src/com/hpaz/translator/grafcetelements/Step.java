package com.hpaz.translator.grafcetelements;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.hpaz.translator.grafcetelements.constants.GrafcetTagsConstants;
import com.hpaz.translator.output.PostProcess;

public class Step {
	/***/

	private String type;
	private String name;
	private String action;
	private String typeAction;
	private String comment;
	private String condition; // segun el tipo de accion tiene condicion o no


	public Step() {
		type = "";
		name = "";
		action = "";
		typeAction = "";
		comment = "";
		condition = "";
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

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getTypeAction() {
		return typeAction;
	}

	public void setTypeAction(String typeAction) {
		this.typeAction = typeAction;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String printStepVG() {
		String s = "\t" + this.name + "\t: BOOL;\n";
		return s;
	}

	public void printStep() {
		System.out.println("----- STEP ------");
		System.out.println("Nombre: " + this.name);
		System.out.println("Tipo: " + this.type);
		System.out.println("Accion: " + this.action);
		System.out.println("Tipo accion: " + this.typeAction);
		System.out.println("Condicion: " + this.condition);
		System.out.println("Comentario: " + this.comment);
	}

	public Map<String, String> getActionStepMap() {
		Map<String, String> actionStepMap = new HashMap<String, String>();
		if(!action.equals("")){
			String aux = name;
			if (this.typeAction.equals(GrafcetTagsConstants.ACTION_CONDITIONAL)){
				aux = "("+aux+" AND " + condition+")";
			}
			if (this.typeAction.equals(GrafcetTagsConstants.ACTION_FORCING_ORDER)){
				/*en este caso tengo que tratar lo q hay dentro porq seguramente se trata de la emergencia*/
				/*Miro si es de parada o de inicio*/
				/*si contiene los corchetes vacios es un stop, guardo la etapa en el project*/
				if(action.contains("{}")){
					Project.getProject().setStop(name);
					//guardo solo los que para, doy por hecho q los q inicia son los mismos
					Project.getProject().addListEmergency(processEmergency(action));
				}else{/*Si dentro del corchete hay una etapa no estara vacio, guardo la tapa en el proj*/
					Project.getProject().setInit(name);
				}
				
			}else{
				actionStepMap.put(action, aux);
			}
		}
		
		return actionStepMap;
	}
	
	/**Devuelve una lista con los grafcet q se paran e inician en a emergencia*/
	private LinkedList<String> processEmergency(String pAcction) {
		
		LinkedList<String> listAux= new LinkedList<String>();
		boolean opc = true;
		int posI, posF;
		
		posI=pAcction.indexOf("/");
		String aux = pAcction.substring(posI + 2 ,pAcction.length());
		
		
		
		/*Empiezo a descomponer la cadena de caracteres para obtener los 
		 * nombres de los grafcet que se paran y se activan en la emergencia*/
		
		
		while (opc) {
			// obtengo el nombre de grafcet
			posF = aux.indexOf(">");
			// guardo solo el nombre del grafcet (Gxx)
			listAux.add(aux.substring(0, posF));
			
			/*si no tiene barra quiere decir q no hay mas grafcet 
			despues de este*/
			posI=aux.indexOf("/");
			if (posI == -1) {
				opc = false;
			} else {
				//corto el string
				aux = aux.substring(posI + 2, aux.length());
			}	
		}
		
		return listAux;
	}
}
