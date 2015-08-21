package com.hpaz.translator.grafcetelements;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hpaz.translator.grafcetelements.constants.GrafcetTagsConstants;


public class Step {
	

	private String type;
	private String name;
	private String comment;//comentario del step
	private String mySet;
	private String myReset;
	private boolean and;
	
	/**Para saber la etepa de la emergencia,
	 * Devuelve stop o start*/
	private boolean stopEmergency;
	private boolean startEmergency;
	private LinkedList<String> grafcetsStopEmergency;
	private LinkedList<String> grafcetsStartEmergency;


	private LinkedList<Action> myActions;

	public Step() {
		this.type = "";
		this.name = "";
		this.comment = "";
		this.mySet=null;
		this.myReset=null;
		this.myActions = new LinkedList<Action>();
		this.stopEmergency=false;
		this.startEmergency=false;
		this.setAnd(false);
	}
	
	public void fillAttributes(Map<String, String> pAttributes) {
		// anado tipo y nombre
		this.type= pAttributes.get("type");
		this.name= pAttributes.get("name");
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
	
	

	public String getMySet() {
		return mySet;
	}
	public void addMySet(String pMySet) {
		String set = pMySet;
		
		Pattern patTemp = Pattern.compile(".*Temp.*/X[0-9]{1,}/[0-9]{1,}[a-z A-Z]{1,}.*");
		Matcher matTemp = patTemp.matcher(pMySet);
		Pattern patCont = Pattern.compile(".*Cont.*==[0-9]{1,}.*");
		Matcher matCont = patCont.matcher(pMySet);
		
		
		if (matTemp.matches()) {
			set = pMySet.replaceAll("/X[0-9]{1,}/[0-9]{1,}[a-z A-Z]{1,}", "Q");
		}
		
		if (matCont.matches()) {
			//obtengo el nombre del contador
			String string = pMySet.substring(0,pMySet.indexOf("=="))+"Q";
			set = string + pMySet.substring(pMySet.indexOf("=="),pMySet.length());
		}
				
		if (this.mySet==null){
			
			this.mySet = set;
			
		}else if(!mySet.contains(set)){
			
			if(isAnd()){
				
				String newSet =null;
				
				/*Quito los parentesis*/
				String aux = set.replaceAll("\\(|\\)", "");
				
				/*Los separo y quito los AND para poder comparar el elemento y asi q no se repita*/
				String[] list  = aux.split("AND");
				
				//Por cada elemento de la lista que obtengo
				for (int i = 0; i < list.length; i++) {
					
					//Quito espacios en blanco delante y detras
					String s = list[i].trim();
					
					//Si el elemento no esta en el set lo añado
					if(!mySet.contains(s)){
						
						//Si hay set anteriores añado el AND
						if(newSet!=null){
							newSet= newSet + " AND " + s;
						}else{
							newSet= s;
						}
					}
				}
				this.mySet= mySet+" AND "+newSet;
			}else{
				this.mySet= mySet+" OR "+set;
			}
		}
	}

	public String getMyReset() {
		return myReset;
	}

	public void addMyReset(String pMyReset) {
		if (this.myReset==null){
			this.myReset = pMyReset;
		}else if(!myReset.contains(pMyReset)){
			this.myReset= myReset+" OR "+pMyReset;			
		}
	}

	public LinkedList<Action> getMyActions() {
		return myActions;
	}

	public void addAction(Action pAction) {
		this.myActions.add(pAction);
		/*Si el tipò de accion es de forcing order*/
		if (pAction.getType().equals(GrafcetTagsConstants.ACTION_FORCING_ORDER)){
			/*Llamo al get emergencia de la accion para rellenar los datos 
			 * de la emergencia en caso de que lo sea*/
			String aux = pAction.getEmergency();
			/*Si es una emergencia tendre que saber de que tipo es
			 * para rellenar las variables correspondientes*/
			if(aux.equalsIgnoreCase("stop")){
				setStopEmergency(true);
				setGrafcetsStopEmergency(pAction.getStopEmergency());
			}else if(aux.equalsIgnoreCase("start")){
				setStartEmergency(true);
				setGrafcetsStartEmergency(pAction.getStartEmergency());
			}
		}
	}

	public boolean isStopEmergency() {
		return stopEmergency;
	}

	public void setStopEmergency(boolean stopEmergency) {
		this.stopEmergency = stopEmergency;
	}
	public boolean isStartEmergency() {
		return startEmergency;
	}

	public void setStartEmergency(boolean startEmergency) {
		this.startEmergency = startEmergency;
	}

	public LinkedList<String> getGrafcetsStopEmergency() {
		return grafcetsStopEmergency;
	}

	public void setGrafcetsStopEmergency(LinkedList<String> grafcetsStopEmergency) {
		this.grafcetsStopEmergency = grafcetsStopEmergency;
	}

	public LinkedList<String> getGrafcetsStartEmergency() {
		return grafcetsStartEmergency;
	}

	public void setGrafcetsStartEmergency(LinkedList<String> grafcetsStartEmergency) {
		this.grafcetsStartEmergency = grafcetsStartEmergency;
	}
	
	public String printStepGlobalVar() {
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
		System.out.println("	MI SET");
		System.out.println("	"+this.mySet);
		System.out.println("	MI RESET");
		System.out.println("	"+this.myReset);
	}

	public Map<String, String> getActionStepMap() {
		Map<String, String> actionStepMap = new HashMap<String, String>();
		for (Action action : myActions) {
			if(!action.getText().equals("")){
				String aux = getName();
				if (action.getType().equals(GrafcetTagsConstants.ACTION_CONDITIONAL)){
					aux = "("+aux+" AND " + action.getCondition()+")";			
				}else{
					actionStepMap.put(action.getText(), aux);
				}
			}
		}
		return actionStepMap;
	}

	public boolean isAnd() {
		return and;
	}
	/**Se llama si la convergencia es and*/
	public void setAnd(boolean and) {
		this.and = and;
	}
}
