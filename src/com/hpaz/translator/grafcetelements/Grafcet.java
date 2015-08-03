package com.hpaz.translator.grafcetelements;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Grafcet {

	private String type;
	private String name;
	private String comment;
	private String owner; // propietario

	/* Variables que se usararn solo si el grafcet es de emergencia */
	private boolean emergency;
	/** Para saber las etepas de la emergencia */
	private String stepStopEmergency;
	private String StepStartEmergency;
	/** Lista de grafcets que se fuerzan en la emergencia */
	private LinkedList<String> listEmergencyStop;
	private LinkedList<String> listEmergencyStart;
	/* Lista de señales del grafcet */
	private LinkedList<String> signalsGrafcet;

	private LinkedList<Jump> jumpList;
	private LinkedList<Sequence> sequenceList;
	private LinkedList<Road> roadList;

	public Grafcet() {
		this.type = "";
		this.name = "";
		this.comment = "";
		this.owner = "";
		this.setStepStartEmergency(null);
		this.setStepStopEmergency(null);
		this.setEmergency(false);
		this.jumpList = new LinkedList<Jump>();
		this.sequenceList = new LinkedList<Sequence>();
		this.roadList = new LinkedList<Road>();
		this.signalsGrafcet = new LinkedList<String>();
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

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public LinkedList<Jump> getListJ() {
		return jumpList;
	}

	public void addJump(Jump pJ) {
		this.jumpList.add(pJ);
	}

	public LinkedList<Sequence> getListS() {
		return sequenceList;
	}

	/**
	 * Recibe la secuencia y el numero rellenara la lista de seccuencias la
	 * posicion de la lista sera el numero de secuencia -1
	 */
	public void addSequence(Sequence pSequence, int pIndex) {
		/*
		 * obntengo su lista de señales de la secuencia para añadirla a la lista
		 * de señales del grafcet
		 */
		this.signalsGrafcet.addAll(pSequence.getSignals());
		/* la posicion de la lista sera el numero de secuencia -1 */
		this.sequenceList.add(/* pIndex-1, */pSequence);

	}

	public LinkedList<Road> getRoadList() {
		return roadList;
	}

	public void addRoad(Road pRoad) {
		this.roadList.add(pRoad);
	}

	/** Si cambia si al anadir el nombre en el preproceso es GEmergencia */
	public boolean isEmergency() {
		return emergency;
	}

	public void setEmergency(boolean emergency) {
		this.emergency = emergency;
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

	public LinkedList<String> getListEmergencyStop() {
		return listEmergencyStop;
	}

	public void setListEmergencyStop(LinkedList<String> listEmergencyStop) {
		this.listEmergencyStop = listEmergencyStop;
	}

	public LinkedList<String> getListEmergencyStart() {
		return listEmergencyStart;
	}

	public void setListEmergencyStart(LinkedList<String> listEmergencyStart) {
		this.listEmergencyStart = listEmergencyStart;
	}

	public LinkedList<String> getGrafcetVarGlobalStages() {
		LinkedList<String> vG = new LinkedList<String>();
		vG.add("\n\t(*---" + this.name + "---*)\n\n");
		// por cada secuencia de la lista
		for (Sequence s : sequenceList) {
			vG.addAll(s.getVarGlobalStages());
		}
		return vG;
	}

	public LinkedList<String> grafcetVarGlobalSignals() {
		LinkedList<String> vG = new LinkedList<String>();

		for (String signal : this.signalsGrafcet) {
			vG.add("\t" + signal + "\t: BOOL;\n");
		}

		return vG;
	}

	// genera un listado con los set y reset listos para exportar
	public LinkedList<String> printSetReset() {
		LinkedList<String> sR = new LinkedList<String>();
		for (Sequence s : sequenceList) {
			sR.addAll(s.getSetReset());
		}
		return sR;
	}

	public void printGrafcet() {
		System.out.println("----- GRAFCET ------");
		System.out.println("Nombre: " + this.name);
		System.out.println("Tipo: " + this.type);
		System.out.println("Propietario: " + this.owner);
		System.out.println("Comentario: " + this.comment);

		for (Jump j : jumpList) {
			j.printJump();
		}
		for (Road r : roadList) {
			r.printRoad();
		}
		for (Sequence s : sequenceList) {
			s.printSequence();
		}
	}

	/** Este metodo devuelve la primera parte del PROGRAM MAIN */
	public String printVar() {
		String n = this.name.substring(1, 0);

		/*
		 * Ejemplo: SecPrincipal : GSecPrincipal; InitSecPrincipal :BOOL;
		 * ResetSecPrincipal :BOOL;
		 */
		String v = "\t\t" + n + "\t:" + this.name + ";\n";
		v = v + "\t\tInit" + n + "\t: BOOL;\n";
		v = v + "\t\tReset" + n + "\t: BOOL;\n";

		return v;
	}

	/** Este metodo devuelve la segunda parte del PROGRAM MAIN */
	public Map<String, String> getActionStepMap() {
		/*
		 * AQUI MIRO CADA SECUENCIA DEL GRAFCET Y SUS TRANSICIONES Y STEP EN
		 * STEP --> ACTION:=NAME; EN TRANSITION --> TRANSITION MIRAR Y HABLAR
		 * SOLO PARACE Q NECESITO LA MARCHA Y EL PARO
		 */

		Map<String, String> actionStepMap = new HashMap<String, String>();

		// por cada secuencia del grafcet
		for (Sequence sequence : sequenceList) {
			// obtengo el map de la secuencia
			Map<String, String> auxMap = sequence.getActionStepMap();
			// por cada accion de auxMap
			for (String action : auxMap.keySet()) {
				// si la key no existe en actionStepMap
				if (actionStepMap.get(action) == null) {
					// aÃ±ado
					actionStepMap.put(action, auxMap.get(action));
				} else {
					// si existe solo modifico el value
					actionStepMap.put(action, actionStepMap.get(action) + " OR " + auxMap.get(action));
				}
			}
		}

		return actionStepMap;
	}

	/**
	 * Esta funcion rellenara las listas PreviousStepLits,
	 * PreviousTransitionList y getNextStepLits de cada secuencia, que nos
	 * serviran luego a la hora de rellenar los set y reset de cada etapa
	 */
	public void fillPreviousAndNextSequencesLists() {
		/* por cada salto */
		for (Jump j : jumpList) {
			/*
			 * FromSeq salta a ToSeq por lo que la seq(fromToSeq-1) tendra en
			 * next la ToSeq-1 la seq(Toseq-1) tendra en previous fromToSeq-1
			 */

			sequenceList.get(j.getFromSeq()).addNextSequencesList(j.getToSeq());
			sequenceList.get(j.getToSeq()).addPreviousSequencesList(j.getFromSeq());

			/*
			 * Sequence s = sequenceList.get(j.getFromSeq()-1); int sizeTrans =
			 * s.getListTransitionOrStep().size()-1; int sizeStep =
			 * s.getListTransitionOrStep().size()-2; Transition t = (Transition)
			 * s.getListTransitionOrStep().get(sizeTrans); Step step = (Step)
			 * s.getListTransitionOrStep().get(sizeStep); Se guardara la ultima
			 * transision y etapa de la seq(fromSeq) en la seq(toSeq)
			 * sequenceList.get(j.getToSeq()-1).addPreviousSeq("("+step.getName(
			 * )+" AND "+t.getCondition()+")");
			 */

		}
		/* Por cada camino que tengamos */
		for (Road road : roadList) {
			// por cada secuencia de la lista de caminos
			//TODO MIRAR QUE CADA SECUENCIA RELLENE LA LISTA DE ID SEQ NEXT Y PREVIOUS SI NO SOLUCIONAR
			for (Integer numSequencesRoad : road.getMySequences()) {
				if (road.getType().equals("div or") || road.getType().equals("div and")) {
					/*
					 * Guardo en la secuencia roadIni.next la secuencia
					 * numSequencesRoad y en la secuencia
					 * numSequencesRoad.previous guardo roadIni
					 */
					sequenceList.get(road.getSeqIni()).addNextSequencesList(numSequencesRoad);
					sequenceList.get(numSequencesRoad).addPreviousSequencesList(road.getSeqIni());

				} else if (road.getType().equals("conv or") || road.getType().equals("conv and")) {
					/*
					 * Guardo en la secuencia roadIni.previous la secuencia
					 * numSequencesRoad y en la secuencia numSequencesRoad.next
					 * guardo roadIni
					 */
					sequenceList.get(road.getSeqIni()).addPreviousSequencesList(numSequencesRoad);
					sequenceList.get(numSequencesRoad).addNextSequencesList(road.getSeqIni());
					
					if(road.getType().equals("conv and")){
						//busco la primera etapa de la secuencia road.SeqInit y activar setAnd=true;
						Step step = sequenceList.get(road.getSeqIni()).getFirstStep();
						step.setAnd(true);
					}
				}
				/*
				 * Sequence sequenceRoadIni =
				 * sequenceList.get(road.getSeqIni()-1); Sequence
				 * sequenceNextOrPreviousRoadIni =
				 * sequenceList.get(numSequencesRoad);
				 * 
				 * Si el tipo es div or, a la secuencia (sIni) le siguen 2 o mas
				 * secuencias (sR) q ambas comienzan en trans
				 * if(road.getType().equals("div or")){
				 * 
				 * int sizeStep =
				 * sequenceRoadIni.getListTransitionOrStep().size()-1; Step step
				 * = (Step)
				 * sequenceRoadIni.getListTransitionOrStep().get(sizeStep);
				 * sequenceList.get(numSequencesRoad).addPreviousSeq(step.
				 * getName()); //Si la secuencia suguiente tiene mas de un
				 * elemento entonces guardo la primera etapa en el nex de la
				 * seqIni
				 * if(sequenceNextOrPreviousRoadIni.getListTransitionOrStep().
				 * size()>1){ Step step1 = (Step)
				 * sequenceNextOrPreviousRoadIni.getListTransitionOrStep().get(1
				 * );
				 * sequenceList.get(road.getSeqIni()-1).addNextSeq(step1.getName
				 * ()); }
				 * 
				 * Si el tipo es div and, La seqIni finaliza con una transicion
				 * y las SeqSiguientes empiezan con etapa }else
				 * if(road.getType().equals("div and")){
				 * 
				 * int sizeStep =
				 * sequenceRoadIni.getListTransitionOrStep().size()-2; Step step
				 * = (Step)
				 * sequenceRoadIni.getListTransitionOrStep().get(sizeStep);
				 * 
				 * int sizeTrans =
				 * sequenceRoadIni.getListTransitionOrStep().size()-1;
				 * Transition transition = (Transition)
				 * sequenceRoadIni.getListTransitionOrStep().get(sizeTrans);
				 * 
				 * //guardo en el previous de la seqSiguiente la ultima trans y
				 * etapa de la seqIni
				 * sequenceList.get(numSequencesRoad).addPreviousSeq("("+step.
				 * getName()+" AND "+transition.getCondition()+")");
				 * 
				 * 
				 * Step step0 = (Step)
				 * sequenceNextOrPreviousRoadIni.getListTransitionOrStep().get(0
				 * ); Guardo en el Nex de la seqIni la primera etapa de la
				 * seqSiguiente
				 * sequenceList.get(road.getSeqIni()-1).addNextSeq(step0.getName
				 * ());
				 * 
				 * 
				 * Si el tipo es conv or, las seqAnteriores terminan en
				 * transicion y la SeqIni comienza en etapa }else if
				 * (road.getType().equals("conv or")){
				 * 
				 * String aux=""; En el previous de seqINi guardo la ultima
				 * transicion y etapa(si la hay) si no la busco en el previous
				 * de la SeqAnterior if(sequenceList.get(road.getSeqIni()-1).
				 * getListTransitionOrStep().size()>1){ int sizeStep =
				 * sequenceNextOrPreviousRoadIni.getListTransitionOrStep().size(
				 * )-2; Step step = (Step)
				 * sequenceNextOrPreviousRoadIni.getListTransitionOrStep().get(
				 * sizeStep);
				 * 
				 * int sizeTrans =
				 * sequenceNextOrPreviousRoadIni.getListTransitionOrStep().size(
				 * )-1; Transition transition = (Transition)
				 * sequenceNextOrPreviousRoadIni.getListTransitionOrStep().get(
				 * sizeTrans);
				 * 
				 * //guardo en el previous de la seqSiguiente la ultima trans y
				 * etapa de la seqIni
				 * sequenceList.get(road.getSeqIni()-1).addPreviousSeq("("+step.
				 * getName()+" AND "+transition.getCondition()+")");
				 * 
				 * }else{ int sizeTrans =
				 * sequenceNextOrPreviousRoadIni.getListTransitionOrStep().size(
				 * )-1; Transition transition = (Transition)
				 * sequenceRoadIni.getListTransitionOrStep().get(sizeTrans);
				 * sequenceList.get(road.getSeqIni()-1).addPreviousSeq(
				 * transition); }
				 * 
				 * Step step = (Step)
				 * sequenceRoadIni.getListTransitionOrStep().get(0);
				 * sequenceList.get(numSequencesRoad).addNextSeq(step.getName())
				 * ;
				 * 
				 * 
				 * Si el tipo es conv or } else if(road.getType().equals(
				 * "conv and")){
				 * 
				 * Guardo en la secuencia(sR) la primera etapa de la
				 * secuencia(r.seqIni) Step step = (Step)
				 * sequenceRoadIni.getListTransitionOrStep().get(0);
				 * sequenceList.get(numSequencesRoad).addNextSeq(step.getName())
				 * ;
				 * 
				 * Guardar en la secuencia(r.SeqIni) la ultima etapa de
				 * secuencia(sR) int sizeStep =
				 * sequenceNextOrPreviousRoadIni.getListTransitionOrStep().size(
				 * )-1; Step step0 = (Step)
				 * sequenceNextOrPreviousRoadIni.getListTransitionOrStep().get(
				 * sizeStep);
				 * sequenceList.get(road.getSeqIni()-1).addPreviousSeq(step0.
				 * getName()); }
				 */
			}
		}
	}

	/*
	 * public void generateSetAndReset() { // TODO COMENTADO PARA VOLVER A HACER
	 * DEBAJO //por cada seccuencia for (Sequence seq : sequenceList) {
	 * //recorro la lista de transiciones o pasos de la secuencia for (int i =
	 * 0; i < seq.getListTransitionOrStep().size(); i++) { Si es una etapa
	 * Object obj = seq.getListTransitionOrStep().get(i); if(obj instanceof
	 * Step){
	 * 
	 * String aux="";
	 * 
	 * Busco el Set a una etapa //Si es el primer elemento d la lista if(i==0){
	 * //por cada elemento de de la lista previous for (String pre :
	 * seq.getPreviousList()){ aux=aux+" OR "+pre; } Si no es el primer elemento
	 * de la lista, pero es el segundo quiere decir q no tiene etapa antes }else
	 * if (i==1){ Transition tAux= (Transition)
	 * seq.getListTransitionOrStep().get(i-1); Step sAux= (Step)
	 * seq.getListTransitionOrStep().get(i); aux="("+sAux.getName()+" AND "
	 * +tAux.getCondition()+")"; }else{ Step sAux= (Step)
	 * seq.getListTransitionOrStep().get(i-2); Transition tAux= (Transition)
	 * seq.getListTransitionOrStep().get(i-1); aux="("+sAux.getName()+" AND "
	 * +tAux.getCondition()+")"; } Anado el Set a una etapa ((Step)
	 * seq.getListTransitionOrStep().get(i)).addMySet(aux);
	 * 
	 * 
	 * Busco el Reset a una etapa aux =""; //si hay dos objetos mas en la lista
	 * if(seq.getListTransitionOrStep().size() > (i+2)){ //si ese segundo objeto
	 * es una etapa if(seq.getListTransitionOrStep().get(i+2) instanceof Step){
	 * Step sAux= (Step) seq.getListTransitionOrStep().get(i+2);
	 * aux=sAux.getName(); } }else{ //por cada elemento de de la lista next for
	 * (String next : seq.getNextLits()){ aux = aux + " OR " + next; } } AÃ±adir
	 * el Reset a una etapa ((Step)
	 * seq.getListTransitionOrStep().get(i)).addMyReset(aux); } } } }
	 */

	public LinkedList<String> getPreviousStepAndTransitionFromSequence(LinkedList<Integer> sequenceIDList,
			String previousTransition) {
		LinkedList<String> previousStepAndTransitionList = new LinkedList<String>();
		//por cada lista de idSecuencias 
		for (Integer sequenceID : sequenceIDList) {
			//obtengo la secuencia con ese ID
			Sequence sequence = this.sequenceList.get(sequenceID);
			//Creo una variable para la transicion y la inicializo a null
			Transition trans = null;
			String transName = null;
			//Si no me han pasado una transicion por parametro
			if (previousTransition == null) {
				trans = sequence.getLastTransition();
				if(trans!=null){
					transName=trans.getCondition();
				}
			}else{
				transName = previousTransition;
			}
			
			Step step = sequence.getLastStep();
			
			if (step == null && transName==null ) {
				previousStepAndTransitionList.addAll(getPreviousStepAndTransitionFromSequence(
						sequence.getPreviousSequencesList(), previousTransition));
			} else if (step == null && transName!=null ) {
				previousStepAndTransitionList.addAll(getPreviousStepAndTransitionFromSequence(
						sequence.getPreviousSequencesList(), transName));
			}else if (step != null && transName!=null ){
				previousStepAndTransitionList.add("(" + step.getName() + " AND " + transName + ")");
			}
		}

		return previousStepAndTransitionList;
	}

	public LinkedList<String> getNextStepFromSequence(LinkedList<Integer> sequenceIDList) {
		LinkedList<String> nextStepList = new LinkedList<String>();

		for (Integer sequenceID : sequenceIDList) {
			Sequence sequence = this.sequenceList.get(sequenceID);
			Step step = sequence.getFirstStep();
			if (step == null) {
				nextStepList.addAll(getNextStepFromSequence(sequence.getNextSequencesList()));
			} else {
				nextStepList.add(step.getName());
			}
		}
		return nextStepList;
	}

	// TODO AGREGAR SET Y RESET A UN STEP
	public void addSetAndResetToStep() {

		//String trans = null;
		for (Sequence seq : sequenceList) {
			for (int i = 0; i < seq.getListTransitionOrStep().size(); i++) {

				if (seq.getListTransitionOrStep().get(i) instanceof Step) {

					Step actualStep = (Step) seq.getListTransitionOrStep().get(i);

					// Rellenamos el Set
					LinkedList<String> previousStepAndTransitions = new LinkedList<String>();
					Transition previousTransition = null;
					Step previousStep = null;
					for (int previousIndex = i - 1; previousIndex >= 0; previousIndex--) {
						if (seq.getListTransitionOrStep().get(previousIndex) instanceof Step && previousStep == null) {
							previousStep = (Step) seq.getListTransitionOrStep().get(previousIndex);
						} else if (seq.getListTransitionOrStep().get(previousIndex) instanceof Transition
								&& previousTransition == null) {
							previousTransition = (Transition) seq.getListTransitionOrStep().get(previousIndex);
						}
					}
					// si tenemos en la secuencia los anteriores lo generamos,
					// sino lo buscamos
					if (previousTransition != null && previousStep != null) {
						previousStepAndTransitions
								.add("(" + previousStep.getName() + " AND " + previousTransition.getCondition() + ")");
					} else {
						String transitionName = null;
						if (previousTransition != null) {
							transitionName = previousTransition.getCondition();
						}
						previousStepAndTransitions = getPreviousStepAndTransitionFromSequence(
								seq.getPreviousSequencesList(), transitionName);
					}
					// fijamos el set al step
					for (String setString : previousStepAndTransitions) {
						actualStep.addMySet(setString);
					}

					// Rellenamos el Reset
					LinkedList<String> nextSteps = new LinkedList<String>();
					Step nextStep = null;
					for (int nextIndex = i + 1; nextIndex < seq.getListTransitionOrStep().size(); nextIndex++) {
						if (seq.getListTransitionOrStep().get(nextIndex) instanceof Step && nextStep == null) {
							nextStep = (Step) seq.getListTransitionOrStep().get(nextIndex);
						}
					}
					// si tenemos en la secuencia el siguiente step lo
					// guardamos, sino los buscamos.
					if (nextStep != null) {
						nextSteps.add(nextStep.getName());
					} else {
						nextSteps = getNextStepFromSequence(seq.getNextSequencesList());
					}
					// fijamos el reset al step
					for (String resetString : nextSteps) {
						actualStep.addMyReset(resetString);
					}
				}

				/*
				 * if(seq.getListTransitionOrStep().size()>2){
				 * 
				 * 
				 * //si el primero es un paso }else
				 * if(seq.getListTransitionOrStep().size()==1){ //si solo hay un
				 * elemento en la secuencia, hago algo si este es step
				 * if(seq.getListTransitionOrStep().get(i) instanceof Step){
				 * Step step = (Step) seq.getListTransitionOrStep().get(i);
				 * LinkedList<String> previous =
				 * getPreviousStepAndTransitionFromSequence(seq.
				 * getPreviousSequencesList(), trans); for (String stepTran :
				 * previous) { step.addMySet(stepTran); } LinkedList<String>
				 * next = getNextStepFromSequence(seq.getNextSequencesList());
				 * for (String stepNext : next) { step.addMyReset(stepNext); } }
				 * trans =null; }else
				 * if(seq.getListTransitionOrStep().size()==2){ //si hay 2
				 * elementos en la secuencia //si el primero es un step,
				 * if(seq.getListTransitionOrStep().get(i) instanceof Step &&
				 * i==0){ Step step = (Step)
				 * seq.getListTransitionOrStep().get(i); LinkedList<String>
				 * previous = getPreviousStepAndTransitionFromSequence(seq.
				 * getPreviousSequencesList(), trans); for (String stepTran :
				 * previous) { step.addMySet(stepTran); } LinkedList<String>
				 * next = getNextStepFromSequence(seq.getNextSequencesList());
				 * for (String stepNext : next) { step.addMyReset(stepNext); }
				 * }else if (seq.getListTransitionOrStep().get(i) instanceof
				 * Transition && i==0 ){ Step step = (Step)
				 * seq.getListTransitionOrStep().get(i+1); Transition t =
				 * (Transition) seq.getListTransitionOrStep().get(i); trans=
				 * t.getCondition(); LinkedList<String> previous =
				 * getPreviousStepAndTransitionFromSequence(seq.
				 * getPreviousSequencesList(), trans); for (String stepTran :
				 * previous) { step.addMySet(stepTran); } LinkedList<String>
				 * next = getNextStepFromSequence(seq.getNextSequencesList());
				 * for (String stepNext : next) { step.addMyReset(stepNext); } }
				 * trans =null; }
				 */
			}
		}
	}

	public LinkedList<String> generateFunctionBlock() {
		LinkedList<String> functionBlock = new LinkedList<String>();
		String actualStep = "", auxSet = "", auxReset = "";

		functionBlock.add("\nFUNCTION_BLOCK " + getName() + "\n\tVAR_INPUT"
				+ "\n\t\tInit\t:BOOL;\n\t\tReset\t:BOOL;\n\tEND_VAR" + "\n\tVAR_OUTPUT\n\tEND_VAR\n\tVAR\n\tEND_VAR");

		functionBlock.add("\n(*---------------------------------------\n" + getName().substring(1, getName().length())
				+ "\n" + getComment() + "\n-----------------------------------------------*)");

		// por cada seccuencia
		for (Sequence seq : sequenceList) {
			// recorro la lista de la secuencia
			// Object obj : seq.getList()
			for (Object obj : seq.getListTransitionOrStep()) {
				/* Si es una etapa */
				if (obj instanceof Step) {
					actualStep = ((Step) obj).getName();
					/*
					 * String aux="";
					 * 
					 * Busco el Set a una etapa //Si es el primer elemento d la
					 * lista if(i==0){ //por cada elemento de de la lista
					 * previous for (String pre : seq.getPreviousList()){
					 * aux=aux+" OR "+pre; } Si no es el primer elemento de la
					 * lista, pero es el segundo quiere decir q no tiene etapa
					 * antes }else if (i==1){ Transition tAux= (Transition)
					 * seq.getListTransitionOrStep().get(i-1); Step sAux= (Step)
					 * seq.getListTransitionOrStep().get(i);
					 * aux="("+sAux.getName()+" AND "+tAux.getCondition()+")";
					 * }else{ Step sAux= (Step)
					 * seq.getListTransitionOrStep().get(i-2); Transition tAux=
					 * (Transition) seq.getListTransitionOrStep().get(i-1);
					 * aux="("+sAux.getName()+" AND "+tAux.getCondition()+")"; }
					 * Anado el Set a una etapa ((Step)
					 * seq.getListTransitionOrStep().get(i)).addMySet(aux);
					 * 
					 * 
					 * Busco el Reset a una etapa aux =""; //si hay dos objetos
					 * mas en la lista if(seq.getListTransitionOrStep().size() >
					 * (i+2)){ //si ese segundo objeto es una etapa
					 * if(seq.getListTransitionOrStep().get(i+2) instanceof
					 * Step){ Step sAux= (Step)
					 * seq.getListTransitionOrStep().get(i+2);
					 * aux=sAux.getName(); } }else{ //por cada elemento de de la
					 * lista next for (String next : seq.getNextLits()){ aux =
					 * aux + " OR " + next; } } AÃ±adir el Reset a una etapa
					 * ((Step)
					 * seq.getListTransitionOrStep().get(i)).addMyReset(aux);
					 */

					// Relleno la lista con los Set-Reset
					functionBlock.add("\n(* Set -Reset ___________________________"
							+ "_____________________________________ " + actualStep + " *)");

					String set = ((Step) obj).getMySet();
					String reset = ((Step) obj).getMyReset();

					// si es una etapa inicial
					if (((Step) obj).getType().equals("initial")) {
						auxSet = "\n\tIF ( " + set + " OR Init ) THEN\n\t\t" + actualStep + ":=1;";
						auxReset = "\n\tEND_IF;\n\tIF ( " + reset + " OR Reset ) THEN\n\t\t" + actualStep + ":=0;";
					} else {
						auxSet = "\n\tIF ( " + set + " ) THEN\n\t\t" + actualStep + ":=1;";
						auxReset = "\n\tEND_IF;\n\tIF ( " + reset + " OR Init OR Reset ) THEN\n\t\t" + actualStep
								+ ":=0;";
					}
					functionBlock.add(auxSet);
					functionBlock.add(auxReset);
					functionBlock.add("\n\tEND_IF;\n");
				}
			}
		}
		functionBlock.add("\nEND_FUNCTION_BLOCK");
		return functionBlock;

	}

	/** Rellena las listas de emergencia */
	public void getEmergency() {

		for (Sequence s : getListS()) {

			s.getEmergency();

			if (s.getStepStartEmergency() != -1) {
				Step stepStart = (Step) s.getListTransitionOrStep().get(s.getStepStartEmergency());
				setStepStartEmergency(stepStart.getName());
				setListEmergencyStart(stepStart.getGrafcetsStartEmergency());

			} else if (s.getStepStopEmergency() != -1) {
				Step stepStop = (Step) s.getListTransitionOrStep().get(s.getStepStopEmergency());
				setStepStopEmergency(stepStop.getName());
				setListEmergencyStop(stepStop.getGrafcetsStopEmergency());
			}

		}

	}

	public boolean compareStartAndStopLists() {
		Collections.sort(this.listEmergencyStop);
		Collections.sort(this.listEmergencyStart);
		return getListEmergencyStart().equals(getListEmergencyStop());
	}

	public LinkedList<String> getListSignalsGrafcet() {
		/*
		 * Esta lista se va rellenando al ir añadiendo una secuencia al grafcet
		 */
		return this.signalsGrafcet;
	}

}
