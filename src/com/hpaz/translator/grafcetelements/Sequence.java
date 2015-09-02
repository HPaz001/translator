package com.hpaz.translator.grafcetelements;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Sequence {
	private int idSeq;
	private LinkedList<Object> listTransitionOrStep;// puede tener tanto
													// transiciones como pasos
	private LinkedList<String> signals;

	/** Se guarda el indice de las secuencias anteriores a esta */
	private LinkedList<Integer> previousSequencesList;
	/** Se guarda el indice de las secuencias siguientes a esta */
	private LinkedList<Integer> nextSequencesList;

	/**Para saber la etepa de la emergencia, en ellos guardare el indice de la
	 * etapa*/
	private int stepStopEmergency;
	private int stepStartEmergency;

	public Sequence() {
		this.listTransitionOrStep = new LinkedList<Object>();
		this.signals = new LinkedList<String>();
		this.previousSequencesList = new LinkedList<Integer>();
		this.nextSequencesList = new LinkedList<Integer>();
		this.stepStopEmergency = -1;
		this.stepStartEmergency = -1;
	}
	public void fillAttributes(Map<String, String> pAttributes) {
		// anado el id
		addIdSeq(Integer.parseInt(pAttributes.get("id")));			
	}

	
	private void addIdSeq(int pNumber) {
		this.idSeq=pNumber;
		
	}
	public int getIdSeq() {
		return idSeq;
	}

	public LinkedList<Object> getListTransitionOrStep() {
		return listTransitionOrStep;
	}

	public LinkedList<String> getSignals() {
		return signals;
	}

	public int getStepStopEmergency() {
		return stepStopEmergency;
	}

	public int getStepStartEmergency() {
		return stepStartEmergency;
	}
	//a�ade una lista de se�ales
	private void  addSignals( LinkedList<String> pSignals){
		this.signals.addAll(pSignals);
	}
	//a�ade una unica se�al
	private void  addSignal( String pSignal){
		this.signals.add(pSignal);
	}
	/**Este metodo a�ade una transicion o un step a la lista listTransitionOrStep
	 * Pero antes de esto genera y guarda las se�ales correspondientes */
	public void addTransitionOrStep(Object pTransitionOrStep) {
		
		if (pTransitionOrStep instanceof Transition) {
			
			((Transition) pTransitionOrStep).analyzeReviews();
			addSignals(((Transition) pTransitionOrStep).getConditionSep());
			
		} else if (pTransitionOrStep instanceof Step) {
			
			for (Action action : ((Step) pTransitionOrStep).getMyActions()) {
				
				String act = action.getText();
				
				//si hay una accion y NO es una emergencia
				if (act != null && !action.isEmergency()) {
					String actionName = action.getText().trim();
					
					//TODO NO SE PORQ NO LO DETECTA LA REGEX ESTA BIEN
					Pattern patTemp = Pattern.compile("^Temp.*=.*");
					Matcher matTemp = patTemp.matcher(actionName);
					
					//TODO ?�?�? compruebo que el tiempo del contador es el correcto ?�
					//int time = Integer.parseInt(aux.replaceAll("=|[a-z A-Z]", ""));
					//aux = aux.substring(0, aux.indexOf("="));
					
					//Si no es un temporizador
					if(!matTemp.matches()){
						
						//Expresion regular para detectar una asignacion de contador
						Pattern patCount = Pattern.compile("^Cont.*=[0-9]$");
						Matcher matCount = patCount.matcher(actionName.trim());
						
						//Si es un contador
						if(matCount.matches()){	
							//TODO tengo q pedir ejemplo de contador en la programacion para saber si esto esta bien 
							
							//Me quedo solo con el nombre del contador
							actionName = actionName.substring(0, actionName.indexOf("=")).trim();
							String stepName= ((Step) pTransitionOrStep).getName();
							//Compruebo si el contador existe en la lista
							int index = Project.getProject().equalsCount(actionName);
							
							//Si index == -1 es q no encontro un contador con ese nombre
							if(index == (-1)){
								//Creo el contador y lo relleno
								Counter cont = new Counter();
								cont.addNameCounter(actionName);
								cont.addStepCountes(stepName);
								Project.getProject().addCounter(cont);
								
							}else{//Si index != -1 es q habia un contador con ese nombre, asi q guardo la etapa.
								Project.getProject().getListCounters().get(index).addStepCountes(stepName);
							}
						//Sino es un temporizador, ni un contador, ni emergencia
						}else{
							addSignal(actionName);
						}
						
					}
					// si hay acction y es emergencia
				}else if (act != null && action.isEmergency()){
					if (((Step) pTransitionOrStep).isStartEmergency()) {
						addStepStartEmergency(listTransitionOrStep.size());
					} else if (((Step) pTransitionOrStep).isStopEmergency()) {
						addStepStopEmergency(listTransitionOrStep.size());
					}
				}
			}
		}
		this.listTransitionOrStep.add(pTransitionOrStep);
	}

	private void addStepStopEmergency(int pNumber) {
		this.stepStopEmergency=pNumber;
		
	}
	private void addStepStartEmergency(int pNumber) {
		this.stepStartEmergency=pNumber;
		
	}
	public LinkedList<String> getVarGlobalStages(String pTypeProgram) {

		//LinkedList<String> auxSignals = new LinkedList<String>();
		LinkedList<String> auxStages = new LinkedList<String>();

		//auxSignals.add("\n\t(*---Se�ales---*)\n\n");
		/* puede ser una transition o un step */
		for (Object st : listTransitionOrStep) {
			if (st instanceof Step) {
				auxStages.add(((Step) st).printStepGlobalVar(pTypeProgram));
			}
		}

		return auxStages;

	}
	public LinkedList<String> getStepExternalVars() {
		
		LinkedList<String> externalVars = new LinkedList<String>();

		/* puede ser una transition o un step */
		for (Object st : listTransitionOrStep) {
			if (st instanceof Step) {
				externalVars.add(((Step) st).printExternalVars());
			}
		}

		return externalVars;

	}

	public LinkedList<String> getStepExternalVarsMain(boolean pEmergency) {
		LinkedList<String> externalVars = new LinkedList<String>();

		/* puede ser una transition o un step */
		for (Object st : listTransitionOrStep) {
			//si es un step
			if (st instanceof Step) {
				//Si es una etapa inicial y no es emergencia o tiene una o mas acciones asociadas
				if((((Step) st).getType().equals("initial") && !pEmergency)||(!((Step) st).getMyActions().isEmpty())){
					externalVars.add(((Step) st).printExternalVars());	
				}

			}
		}

		return externalVars;
	}

	/**
	 * Devuelve un listado con los set y reset correspondientes a cada objeto de
	 * la secuencia
	 */
	public LinkedList<String> getSetReset() {
		return null;
	}

/*	public void printSequence() {
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
		
	}*/

	public Map<String, String> getActionStepMap() {
		/*
		 * SE DEBEN A�ADIR EL RE PARO Y RE MARCHA Y ALGUNA MAS PREGUNTAR EN ESTE
		 * CASO DEPENDEMOS DE LO Q NOS DIGA EL USUARIO EN LAS SEÑALES
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
						// añado la accion
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

/*	public void getEmergency() {
		 Por cada step de la secuencia 
		for (int i = 0; i < getListTransitionOrStep().size(); i++) {
			Object step = getListTransitionOrStep().get(i);
			if (step instanceof Step) {
				
				 * llamo a get emergency del step para que rellene los datos
				 * correspondientes en caso de que sea un step de emergencia
				 
				// ((Step) step).getEmergency();
				// si es una etapa de emergencia guardo en el tipo la etapa
				if (((Step) step).isStartEmergency()) {
					addStepStartEmergency(i);
				} else if (((Step) step).isStopEmergency()) {
					addStepStopEmergency(i);
				}
			}
		}
	}*/

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
