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

	/**
	 * Para saber la etepa de la emergencia, en ellos guardare el indice de la
	 * etapa
	 */
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

	/**
	 * Este metodo a#ade una transicion o un step a la lista
	 * listTransitionOrStep Pero antes de esto genera y guarda las se�ales
	 * correspondientes
	 */
	public void addTransitionOrStep(Object pTransitionOrStep) {
		// si es una transicion
		if (pTransitionOrStep instanceof Transition) {
			Transition transition = (Transition) pTransitionOrStep;
			// si la transicion tiene un comentario lo analizo
			if (transition.getComment() != null) {
				((Transition) pTransitionOrStep).analyzeReviews();
			}
			addSignals(((Transition) pTransitionOrStep).getConditionSep());
			// si es un step
		} else if (pTransitionOrStep instanceof Step) {
			// por cada accion que hay en el step
			for (Action action : ((Step) pTransitionOrStep).getMyActions()) {

				String act = action.getText();

				// si hay una accion y NO es una emergencia
				if (act != null && !action.isEmergency()) {
					String actionName = action.getText().trim();
					// para comprobar si es un temporizador
					Pattern patTemp = Pattern.compile("^TM\\-.*.=[0-9].*");
					Matcher matTemp = patTemp.matcher(actionName);

					// temporizador
					if (matTemp.matches()) {
						// Quito el TM-
						actionName = actionName.replaceAll("^TM\\-", "");
						// Me quedo solo con el nombre del temp
						String aux = actionName.substring(0, actionName.indexOf("="));
						// busco el indice del temp en la lista si esta
						int index = Project.getProject().equalsTimer(aux);
						// Si el temporizador no esta lo creo y a�ado
						if (index == (-1)) {
							Timer timer = new Timer();
							timer.addNameTimer(aux);
							timer.addStepNameTimer(((Step) pTransitionOrStep).getName());
							timer.fillTimer(actionName.substring(actionName.indexOf("=")+1));
							Project.getProject().addTimer(timer);

						}
					} else {

						// Expresion regular para detectar una asignacion de
						// contador
						Pattern patCountAsig = Pattern.compile("^CT\\-.*.=[0-9]$");
						Matcher matCountAsig = patCountAsig.matcher(actionName.trim());

						// Si es un contador
						if (matCountAsig.matches()) {
							// Quito el CT-
							actionName = actionName.replaceAll("^CT\\-", "");
							// Me quedo solo con el nombre del contador
							actionName = actionName.substring(0, actionName.indexOf("="));
							// lo convierto en numero para verificar que
							// efectivamente solo coje los numeros
							// TODO esto deveria de ser un numero comprobar
							String initialValue = actionName.substring(actionName.indexOf("=") + 1,
									actionName.length());
							String stepName = ((Step) pTransitionOrStep).getName();
							// Compruebo si el contador existe en la lista
							int index = Project.getProject().equalsCount(actionName);
							// Si index == -1 es q no encontro un contador con
							// ese nombre
							if (index == (-1)) {
								// Creo el contador y lo relleno
								Counter cont = new Counter();
								// le agrego el nombre y el valor inicial
								cont.addNameCounter(actionName);
								cont.addInitialValue(initialValue);
								cont.addStepInitialValue(stepName);
								Project.getProject().addCounter(cont);

							} else {// Si index != -1 es q habia un contador con
									// ese nombre, asi q guardo la etapa.
								Project.getProject().getListCounters().get(index).addInitialValue(stepName);
							}
							// Sino es un temporizador, ni un contador, ni
							// emergencia
						} else {
							// si es un mensaje
							String message = action.getMessage();
							if (message != null) {
								actionName = message;
							}
							addSignal(actionName);
						}

					}
					// si hay acction y es emergencia
				} else if (act != null && action.isEmergency()) {
					// si es una action se q estoy en un step
					Step auxStep = (Step) pTransitionOrStep;
					// int stepNumber =
					// Integer.parseInt(auxStep.getName().substring(1));
					// guardo el indice de la etapa de la emergencia
					// utilizo el .size ya que este elemento sera a�adido en la
					// ultima posicion de la lista
					int stepIndex = listTransitionOrStep.size();
					if (auxStep.isStartEmergency()) {
						addStepStartEmergency(stepIndex);
						// addStepStartEmergency(stepNumber);
					} else if (auxStep.isStopEmergency()) {
						addStepStopEmergency(stepIndex);
						// addStepStopEmergency(stepNumber);
					}
				}
			}
		}
		this.listTransitionOrStep.add(pTransitionOrStep);
	}

	public LinkedList<String> getVarGlobalStages(String pTypeProgram) {

		// LinkedList<String> auxSignals = new LinkedList<String>();
		LinkedList<String> auxStages = new LinkedList<String>();

		// auxSignals.add("\n\t(*---Se�ales---*)\n\n");
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
			// si es un step
			if (st instanceof Step) {
				// Si es una etapa inicial y no es emergencia o tiene una o mas
				// acciones asociadas
				if ((((Step) st).getType().equals("initial") && !pEmergency)
						|| (!((Step) st).getMyActions().isEmpty())) {
					externalVars.add(((Step) st).printExternalVars());
				}

			}
		}

		return externalVars;
	}

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

	public LinkedList<Integer> getPreviousSequencesList() {
		return previousSequencesList;
	}

	public void addPreviousSequencesList(Integer previousSequences) {

		if (previousSequencesList.isEmpty()) {
			this.previousSequencesList.add(previousSequences);
			// compruebo que no este para no repetir
		} else if (!previousSequencesList.equals(previousSequences)) {
			this.previousSequencesList.add(previousSequences);
		}

	}

	public LinkedList<Integer> getNextSequencesList() {
		return nextSequencesList;
	}

	public void addNextSequencesList(Integer nextSequences) {
		if (nextSequencesList.isEmpty()) {
			this.nextSequencesList.add(nextSequences);
		} else if (!nextSequencesList.equals(nextSequences)) {
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

	private void addIdSeq(int pNumber) {
		this.idSeq = pNumber;
	}

	// a�ade una lista de se�ales
	private void addSignals(LinkedList<String> pSignals) {
		this.signals.addAll(pSignals);
	}

	// a�ade una unica se�al
	private void addSignal(String pSignal) {
		this.signals.add(pSignal);
	}

	private void addStepStopEmergency(int pNumber) {
		this.stepStopEmergency = pNumber;

	}

	private void addStepStartEmergency(int pNumber) {
		this.stepStartEmergency = pNumber;

	}

}
