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
				}else{
					actionStepMap.put(action.getText(), aux);
				}
			}
		}
		return actionStepMap;
	}
	
	/**Rellena las variablas de stoy y start emergencia*/
	public void getEmergency(){
		for (Action action : myActions) {
			if (action.getType().equals(GrafcetTagsConstants.ACTION_FORCING_ORDER)){
				action.getEmergency();
				if(action.getEmergency().equalsIgnoreCase("stop")){
					setStopEmergency(true);
					setGrafcetsStopEmergency(action.getStopEmergency());
				}else if(action.getEmergency().equalsIgnoreCase("start")){
					setStartEmergency(true);
					setGrafcetsStartEmergency(action.getStartEmergency());
				}
			}
		}	
	}


}
