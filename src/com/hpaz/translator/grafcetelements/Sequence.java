package com.hpaz.translator.grafcetelements;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Sequence {
	private int idSeq;
	private LinkedList<Object> listTransitionOrStep;// puede tener tanto
													// transiciones como pasos
	private LinkedList<String> signals;
	/*
	 * private LinkedList<String> previousList; private LinkedList<String>
	 * nextLits;
	 */
	/** Se guarda el indice de las secuencias anteriores a esta */
	private LinkedList<Integer> previousSequencesList;
	/** Se guarda el indice de las secuencias siguientes a esta */
	private LinkedList<Integer> nextSequencesList;

	/**
	 * Para saber la etepa de la emergencia, en ellos guardare el indice de la
	 * etapa
	 */
	private int stepStopEmergency;
	private int StepStartEmergency;

	public Sequence() {
		this.listTransitionOrStep = new LinkedList<Object>();
		this.signals = new LinkedList<String>();
		this.previousSequencesList = new LinkedList<Integer>();
		this.nextSequencesList = new LinkedList<Integer>();
		this.stepStopEmergency = -1;
		this.StepStartEmergency = -1;
	}

	public int getIdSeq() {
		return idSeq;
	}

	public void setIdSeq(int id) {
		this.idSeq = id;
	}

	public LinkedList<Object> getListTransitionOrStep() {
		return listTransitionOrStep;
	}

	public LinkedList<String> getSignals() {
		return signals;
	}

	/*
	 * public LinkedList<String> getPreviousList() { return previousList; }
	 * public void addPreviousSeq(String previousSeq) {
	 * this.previousList.add(previousSeq); } public LinkedList<String>
	 * getNextLits() { return nextLits; } public void addNextSeq(String nextSeq)
	 * { this.nextLits.add(nextSeq); }
	 */
	public int getStepStopEmergency() {
		return stepStopEmergency;
	}

	public void setStepStopEmergency(int stepStopEmergency) {
		this.stepStopEmergency = stepStopEmergency;
	}

	public int getStepStartEmergency() {
		return StepStartEmergency;
	}

	public void setStepStartEmergency(int stepStartEmergency) {
		StepStartEmergency = stepStartEmergency;
	}

	/** puede ser una transition o un step */
	public void addTransitionOrStep(Object pTransitionOrStep) {
		if (pTransitionOrStep instanceof Transition) {
			signals.addAll(((Transition) pTransitionOrStep).getConditionSep());
		} else if (pTransitionOrStep instanceof Step) {
			for (Action action : ((Step) pTransitionOrStep).getMyActions()) {
				String act = action.getText();
				if (!act.equals("") && !action.isEmergency()) {
					signals.add(action.getText());
				}
			}
		}
		this.listTransitionOrStep.add(pTransitionOrStep);
	}

	public LinkedList<String> getVarGlobalStages() {

		LinkedList<String> auxSignals = new LinkedList<String>();
		LinkedList<String> auxStages = new LinkedList<String>();

		auxSignals.add("\n\t(*---Señales---*)\n\n");
		/* puede ser una transition o un step */
		for (Object st : listTransitionOrStep) {
			/*
			 * if (st instanceof Transition){ //signals.addAll(((Transition)
			 * st).printTransVG()); auxSignals.addAll(((Transition)
			 * st).printTransVG());
			 * 
			 * }else
			 */if (st instanceof Step) {
				auxStages.add(((Step) st).printStepGlobalVar());
				/*
				 * for (Action action : ((Step) st).getMyActions()) { String act
				 * = action.getText(); if(!act.equals("")){
				 * auxSignals.add("\t"+action.getText()+"\t: BOOL;\n"); } }
				 */

			}
		}

		// esto lo hago para que queden primero las señales y despues las etapas
		// auxSignals.addAll(auxStages);

		return auxStages;

	}

	/*
	 * public LinkedList<String> generateListSignals(){ for (Object st : list)
	 * {/*puede ser una transition o un step if (st instanceof Transition){
	 * signals.addAll(((Transition) st).getConditionSep()); }else if(st
	 * instanceof Step){ for (Action action : ((Step) st).getMyActions()) {
	 * String act = action.getText(); if(!act.equals("")){
	 * signals.add(action.getText()); } } } } return signals; }
	 */
	/**
	 * Devuelve un listado con los set y reset correspondientes a cada objeto de
	 * la secuencia
	 */
	public LinkedList<String> getSetReset() {
		return null;
	}

	public void printSequence() {
		System.out.println("----- SEQUENCE ------");
		System.out.println("ID secuencia : " + this.idSeq);
		System.out.println("----- PREVIOUS SequencesList");
		for (Integer i : previousSequencesList) {
			System.out.println(i);
		}
		System.out.println("----- NEXT SequencesList");
		for (Integer i : nextSequencesList) {
			System.out.println(i);
		}
		for (Object o : listTransitionOrStep) {
			if (o instanceof Transition) {
				((Transition) o).printTransition();
			} else if (o instanceof Step) {
				((Step) o).printStep();
			}
		}
		
	}

	public Map<String, String> getActionStepMap() {
		/*
		 * SE DEBEN AÑADIR EL RE PARO Y RE MARCHA Y ALGUNA MAS PREGUNTAR EN ESTE
		 * CASO DEPENDEMOS DE LO Q NOS DIGA EL USUARIO EN LAS SEÃ‘ALES
		 */
		Map<String, String> actionStepMap = new HashMap<String, String>();

		// por cada objeto de la lista
		for (Object stepOrTransition : listTransitionOrStep) {
			// si es de tipo step
			if (stepOrTransition instanceof Step) {
				// busco la acciones del step y la guardo en auxMap
				Map<String, String> auxMap = ((Step) stepOrTransition).getActionStepMap();
				// por cada accion de auxMap
				for (String action : auxMap.keySet()) {
					// si la accion no esta en actionStepMap
					if (actionStepMap.get(action) == null) {
						// aÃ±ado la accion
						actionStepMap.put(action, auxMap.get(action));
					} else {
						// si la accion ya existe, solo modifico el value del
						// actionStepMap
						actionStepMap.put(action, actionStepMap.get(action) + " OR " + auxMap.get(action));
					}
				}
			}
		}
		// devuelvo el map unido
		return actionStepMap;
	}

	public void getEmergency() {
		/* Por cada step de la secuencia */
		for (int i = 0; i < getListTransitionOrStep().size(); i++) {
			Object step = getListTransitionOrStep().get(i);
			if (step instanceof Step) {
				/*
				 * llamo a get emergency del step para que rellene los datos
				 * correspondientes en caso de que sea un step de emergencia
				 */
				// ((Step) step).getEmergency();
				// si es una etapa de emergencia guardo en el tipo la etapa
				if (((Step) step).isStartEmergency()) {
					setStepStartEmergency(i);
				} else if (((Step) step).isStopEmergency()) {
					setStepStopEmergency(i);
				}
			}
		}
	}

	public LinkedList<Integer> getPreviousSequencesList() {
		return previousSequencesList;
	}

	public void addPreviousSequencesList(Integer previousSequences) {
		
		if(previousSequencesList.isEmpty()){
			this.previousSequencesList.add(previousSequences);	
			// compruebo que no este para no repetir
		}else if (!previousSequencesList.equals(previousSequences)) {
			this.previousSequencesList.add(previousSequences);
		}

	}

	public LinkedList<Integer> getNextSequencesList() {
		return nextSequencesList;
	}

	public void addNextSequencesList(Integer nextSequences) {
		if(nextSequencesList.isEmpty()){
			this.nextSequencesList.add(nextSequences);
		}else if (!nextSequencesList.equals(nextSequences)) {
			this.nextSequencesList.add(nextSequences);
		}
	}

	public Transition getLastTransition() {
		
		for (int i = listTransitionOrStep.size() - 1; i >= 0; i--) {
			if (listTransitionOrStep.get(i) instanceof Transition) {
				return (Transition) listTransitionOrStep.get(i);
			}
		}
		return null;
	}

	public Step getLastStep() {
		for (int i = listTransitionOrStep.size() - 1; i >= 0; i--) {
			if (listTransitionOrStep.get(i) instanceof Step) {
				return (Step) listTransitionOrStep.get(i);
			}
		}
		return null;
	}

	public Step getFirstStep() {
		for (int i = 0; i < listTransitionOrStep.size(); i++) {
			if (listTransitionOrStep.get(i) instanceof Step) {
				return (Step) listTransitionOrStep.get(i);
			}
		}
		return null;
	}

}
