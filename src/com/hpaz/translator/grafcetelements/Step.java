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
		this.comment = null;
		this.mySet=null;
		this.myReset=null;
		this.myActions = new LinkedList<Action>();
		this.stopEmergency=false;
		this.startEmergency=false;
		this.setAnd(false);
	}
	
	public void fillAttributes(Map<String, String> pAttributes) {
		// anado tipo y nombre
		addType(pAttributes.get("type"));
		addName(pAttributes.get("name"));
	}

	public String getType() {
		return type;
	}

	private void addType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	private void addName(String name) {
		this.name = name;
	}

	public String getComment() {
		return comment;
	}

	public void addComment(String pComment) {
		if (getComment() == null){
			this.comment = pComment;
		}else {
			this.comment = getComment() + " " + pComment;
		}
	}
	
	

	public String getMySet() {
		return mySet;
	}
	public void addMySet(String pMySet) {
		String set = pMySet;
		//Para detectar temporizador
		Pattern patTemp = Pattern.compile(".*Temp.*/X[0-9]{1,}/[0-9]{1,}[a-z A-Z]{1,}.*");
		Matcher matTemp = patTemp.matcher(pMySet);
		//Para detectar contadores
		Pattern patCont = Pattern.compile(".*Cont.*==[0-9]{1,}.*");
		Matcher matCont = patCont.matcher(pMySet);
		
		//Si es un temporizador
		if (matTemp.matches()) {
			set = pMySet.replaceAll("/X[0-9]{1,}/[0-9]{1,}[a-z A-Z]{1,}", "Q");
		}
		//Si es un contador
		if (matCont.matches()) {
			//obtengo el nombre del contador
			String string = pMySet.substring(0,pMySet.indexOf("=="))+"Q";
			set = string + pMySet.substring(pMySet.indexOf("=="),pMySet.length());
		}
		//Si no se ha añadido un set se añade	
		if (this.mySet==null){
			this.mySet = set;
		
			//Si ya hay set añadidos compruebo que no exista  
		}else if(!mySet.contains(set)){
			//Si se une con una divergencia en AND
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
		if (getMyReset()==null){
			this.myReset = pMyReset;
		}else if(!getMyReset().contains(pMyReset)){
			this.myReset= getMyReset()+" OR "+pMyReset;			
		}
	}

	public LinkedList<Action> getMyActions() {
		return myActions;
	}

	public void addAction(Action pAction) {
		Action auxAction = pAction;
		Pattern pat = Pattern.compile("^F/G.*");
		Matcher mat = pat.matcher(auxAction.getText());
		//si la accion es una emergencia
		if(mat.matches() && auxAction.getType().equals(GrafcetTagsConstants.ACTION_FORCING_ORDER)){
			auxAction.addEmergency(true);
			//relleno las listas
			auxAction.getEmergency();
		
		/*Si la accion es de emergencia*/
		//if (auxAction.isEmergency()){
			/*Si la lista de start contiene elemento es forzado de inicio*/
			if(!auxAction.getStartEmergency().isEmpty()){
				addStartEmergency(true);
				addGrafcetsStartEmergency(pAction.getStartEmergency());
				/*Si la lista de stop contiene elemento es forzado de parada*/
			}else if(!auxAction.getStopEmergency().isEmpty()){
				addStopEmergency(true);
				addGrafcetsStopEmergency(pAction.getStopEmergency());
			}
		}
		this.myActions.add(auxAction);
	}

	public boolean isStopEmergency() {
		return stopEmergency;
	}

	public void addStopEmergency(boolean stopEmergency) {
		this.stopEmergency = stopEmergency;
	}
	public boolean isStartEmergency() {
		return startEmergency;
	}

	public void addStartEmergency(boolean startEmergency) {
		this.startEmergency = startEmergency;
	}

	public LinkedList<String> getGrafcetsStopEmergency() {
		return grafcetsStopEmergency;
	}

	public void addGrafcetsStopEmergency(LinkedList<String> pGrafcetsStopEmergency) {
		this.grafcetsStopEmergency = pGrafcetsStopEmergency;
	}

	public LinkedList<String> getGrafcetsStartEmergency() {
		return grafcetsStartEmergency;
	}

	public void addGrafcetsStartEmergency(LinkedList<String> pGrafcetsStartEmergency) {
		this.grafcetsStartEmergency = pGrafcetsStartEmergency;
	}
	
	public String printStepGlobalVar() {
		String s = "\t" + this.name + "\t: BOOL;\n";
		return s;
	}

	/*public void printStep() {
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
	}*/

	public Map<String, String> getActionStepMap() {
		Map<String, String> actionStepMap = new HashMap<String, String>();
		for (Action action : myActions) {
			if(!action.getText().equals("")){
				String aux = getName();
				//si la accion tiene una condicion
				if (action.getType().equals(GrafcetTagsConstants.ACTION_CONDITIONAL)){
					aux = "("+aux+" AND " + action.getCondition()+")";			
				}
				actionStepMap.put(action.getText(), aux);	
			}
		}
		return actionStepMap;
	}

	public boolean isAnd() {
		return and;
	}
	/**Se llama si la convergencia es and*/
	public void setAnd(boolean pAnd) {
		this.and = pAnd;
	}
}
