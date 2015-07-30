package com.hpaz.translator.grafcetelements;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hpaz.translator.grafcetelements.constants.GrafcetTagsConstants;
import com.hpaz.translator.output.PostProcess;

public class Step {
	

	private String type;
	private String name;
	private String comment;//comentario del step
	private String mySet;
	private String myReset;
	
	/**Para saber la etepa de la emergencia,
	 * Devuelve stop o start*/
	private boolean stopEmergency;
	private boolean startEmergency;
	
	
	
	public boolean isStartEmergency() {
		return startEmergency;
	}

	public void setStartEmergency(boolean startEmergency) {
		this.startEmergency = startEmergency;
	}

	private LinkedList<Action> myActions;

	public Step() {
		this.type = "";
		this.name = "";
		this.comment = "";
		this.mySet=null;
		this.myReset=null;
		this.myActions = new LinkedList<Action>();
		this.setStopEmergency(false);
		this.startEmergency=false;
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
	
	public String printStepVG() {
		String s = "\t" + this.name + "\t: BOOL;\n";
		return s;
	}

	public void printStep() {
		System.out.println("----- STEP ------");
		System.out.println("Nombre: " + this.name);
		System.out.println("Tipo: " + this.type);
		for (Action action : myActions) {
			action.printAction();
		}
		System.out.println("Comentario: " + this.comment);
	}

	public Map<String, String> getActionStepMap() {
		Map<String, String> actionStepMap = new HashMap<String, String>();
		for (Action action : myActions) {
			if(!action.getText().equals("")){
				String aux = getName();
				if (action.getType().equals(GrafcetTagsConstants.ACTION_CONDITIONAL)){
					aux = "("+aux+" AND " + action.getCondition()+")";
				}
				if (action.getType().equals(GrafcetTagsConstants.ACTION_FORCING_ORDER)){
					if(action.getEmergency().equalsIgnoreCase("stop")){
						setStopEmergency(true);
					}else if(action.getEmergency().equalsIgnoreCase("start")){
						setStartEmergency(true);
					}
					/*en este caso tengo que tratar lo q hay dentro porq seguramente se trata de la emergencia
					Miro si es de parada o de inicio
					si contiene los corchetes vacios es un stop, guardo la etapa en el project
					
					Para añadir expresiones regulares
					Emergencia forzado a stop
					Pattern pat = Pattern.compile("^F/G.*.>\\{\\}$");
					Matcher mat = pat.matcher(action.getText());
					Emergencia forzado a start
					Pattern pat1 = Pattern.compile("^F/G.*.>\\{.*.\\}$");
					Matcher mat1 = pat1.matcher(action.getText());
				
					if(mat.matches()){
						Project.getProject().setStop(getName());
						
						Tengo q mirar cuales se paran y cuales se iniician, pueden no ser los mismos
						Project.getProject().addListEmergency(processEmergency(action.getText()));
					}else{Si dentro del corchete hay una etapa, guardo la etapa en el proj
						if(mat1.matches())
							Project.getProject().setInit(getName());
					}*/
					
				}else{
					actionStepMap.put(action.getText(), aux);
				}
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

	public String getMySet() {
		return mySet;
	}

	public void addMySet(String mySet) {
		if (this.mySet==null){
			this.mySet = mySet;
		}
	}

	public String getMyReset() {
		return myReset;
	}

	public void addMyReset(String myReset) {
		this.myReset = myReset;
	}

	public LinkedList<Action> getMyActions() {
		return myActions;
	}

	public void addAction(Action pAction) {
		this.myActions.add(pAction);
	}

	public boolean isStopEmergency() {
		return stopEmergency;
	}

	public void setStopEmergency(boolean stopEmergency) {
		this.stopEmergency = stopEmergency;
	}
}
