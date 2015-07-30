package com.hpaz.translator.grafcetelements;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Sequence {
	private int idSeq;
	private LinkedList<Object> list;// puede tener tanto transiciones como pasos
	private LinkedList<String> signals; 	
	private LinkedList<String> previousList; 
	private LinkedList<String> nextLits; 
	
	/**Para saber la etepa de la emergencia,
	 * Devuelve stop o start*/
	private String stepStopEmergency;
	private String StepStartEmergency;
	
	/**
	 * 
	 */
	public Sequence() {
		this.list = new LinkedList<Object>();
		this.signals = new LinkedList<String>();
		this.previousList=new LinkedList<String>();
		this.nextLits= new LinkedList<String>();
		this.setStepStartEmergency(null);
		this.setStepStopEmergency(null);
	}
	public int getIdSeq() {
		return idSeq;
	}
	public void setIdSeq(int id) {
		this.idSeq = id;
	}
	public LinkedList<Object> getList() {
		return list;
	}
	public LinkedList<String> getSignals() {
		return signals;
	}
	/**puede ser una transition o un step*/
	public void addTorS(Object pTorS) {
		this.list.add(pTorS);
	}
	
	public LinkedList<String> printSeqVG(){
		LinkedList<String> textAux= new LinkedList<String>();
		for (Object st : list) {/*puede ser una transition o un step*/
			if (st instanceof Transition){
				signals.addAll(((Transition) st).printTransVG());
			}else if(st instanceof Step){
				textAux.add(((Step) st).printStepVG());
				for (Action action : ((Step) st).getMyActions()) {
					String act = action.getText();
					if(!act.equals("")){
						signals.add("\t"+action.getText()+"\t: BOOL;\n");
					}
				}
				
				
			}
		}
		return textAux;
		
	}
	/**Devuelve un listado con los set y reset correspondientes a cada objeto de la secuencia*/
	public LinkedList<String> getSetReset() {
		// TODO Auto-generated method stub
		return null;
	}
	public void printSequence(){
		System.out.println("----- SEQUENCE ------");
		System.out.println("ID secuencia : "+this.idSeq);
		for (Object o : list) {
			if(o instanceof Transition){
				((Transition) o).printTransition();
			}else if (o instanceof Step){
				((Step) o).printStep();
			}
		}
	}
	
	public Map<String, String> getActionStepMap(){
		/*SE DEBEN AÑADIR EL RE PARO Y RE MARCHA Y ALGUNA MAS PREGUNTAR
		 * EN ESTE CASO DEPENDEMOS DE LO Q NOS DIGA EL USUARIO EN LAS SEÑALES*/
		Map<String, String> actionStepMap = new HashMap<String, String>();
		
		//por cada objeto de la lista
		for (Object stepOrTransition : list) {
			//si es de tipo step
			if (stepOrTransition instanceof Step){
				//busco la acciones del step y la guardo en auxMap				
				Map<String, String> auxMap = ((Step) stepOrTransition).getActionStepMap();
				//por cada accion de auxMap 
				for (String action : auxMap.keySet()){		
					//si la accion no esta en actionStepMap
					if (actionStepMap.get(action) == null){
						//añado la accion 
						actionStepMap.put(action, auxMap.get(action));
					}else{
						//si la accion ya existe, solo modifico el value del actionStepMap
						actionStepMap.put(action, actionStepMap.get(action) + " OR " + auxMap.get(action));
					}
				}
				//si es una etapa de emergencia guardo en el tipo la etapa
				if(((Step) stepOrTransition).isStartEmergency()){
					setStepStartEmergency(((Step) stepOrTransition).getName());
				}else if(((Step) stepOrTransition).isStopEmergency()){
					setStepStopEmergency(((Step) stepOrTransition).getName());
				}
			}
		}
		//devuelvo el map unido 
		return actionStepMap;	
	}
	public LinkedList<String> getPreviousList() {
		return previousList;
	}
	public void addPreviousSeq(String previousSeq) {
		this.previousList.add(previousSeq);
	}
	public LinkedList<String> getNextLits() {
		return nextLits;
	}
	public void addNextSeq(String nextSeq) {
		this.nextLits.add(nextSeq);
	}
	public String getStepStopEmergency() {
		return stepStopEmergency;
	}
	public void setStepStopEmergency(String stepStopEmergency) {
		this.stepStopEmergency = stepStopEmergency;
	}
	public String getStepStartEmergency() {
		return StepStartEmergency;
	}
	public void setStepStartEmergency(String stepStartEmergency) {
		StepStartEmergency = stepStartEmergency;
	}

}
