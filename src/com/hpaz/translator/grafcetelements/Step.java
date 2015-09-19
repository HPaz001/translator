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
		this.and=false;
	}
	
	public void fillAttributes(Map<String, String> pAttributes) {
		// anado tipo y nombre
		addType(pAttributes.get("type"));
		addName(pAttributes.get("name"));
	}

	public String getType() {
		return type;
	}

	
	public String getName() {
		return name;
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
		//Si no se ha añadido un set se aï¿½ade	
		if (this.mySet==null){
			this.mySet = set;
		
			//Si ya hay set aï¿½adidos compruebo que no exista  
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
					
					//Si el elemento no esta en el set lo aï¿½ado
					if(!mySet.contains(s)){
						
						//Si hay set anteriores aï¿½ado el AND
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

	public boolean isStartEmergency() {
		return startEmergency;
	}

	public LinkedList<String> getGrafcetsStopEmergency() {
		return grafcetsStopEmergency;
	}
	
	public LinkedList<String> getGrafcetsStartEmergency() {
		return grafcetsStartEmergency;
	}
		
	//en type le indico si tiene q devolver la de PLCOpen o la de TwinCat
	public String printStepGlobalVar( String pTypeProgram) {
		String var ="";
		if (pTypeProgram.equals(GrafcetTagsConstants.PROGRAM_OPT1)){//TwinCat
			var ="\t" + this.name + "\t: BOOL;\n";
		}else if (pTypeProgram.equals(GrafcetTagsConstants.PROGRAM_OPT3)) {//PLCOpen
			var = "<variable name=\""+this.name+"\"><type><BOOL /></type></variable>";
		}
		
		return var;
	}
	
	public String printExternalVars() {
		String externalVar ="<variable name=\"" + this.name + "\" group=\"Default\">"
				+ "<type><BOOL /></type>"
				+ "</variable>";
		return externalVar;
	}

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
	
	/**Se llama si la convergencia es and*/
	public void addAnd(boolean pAnd) {
		this.and = pAnd;
	}
	
	private void addType(String type) {
		this.type = type;
	}

	private void addName(String name) {
		this.name = name;
	}
	
	private void addStopEmergency(boolean stopEmergency) {
		this.stopEmergency = stopEmergency;
	}
	
	private void addStartEmergency(boolean startEmergency) {
		this.startEmergency = startEmergency;
	}
	
	private void addGrafcetsStopEmergency(LinkedList<String> pGrafcetsStopEmergency) {
		this.grafcetsStopEmergency = pGrafcetsStopEmergency;
	}

	private void addGrafcetsStartEmergency(LinkedList<String> pGrafcetsStartEmergency) {
		this.grafcetsStartEmergency = pGrafcetsStartEmergency;
	}
	
	private boolean isAnd() {
		return and;
	}
	
}
