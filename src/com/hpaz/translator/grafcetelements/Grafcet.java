package com.hpaz.translator.grafcetelements;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.text.WordUtils;

public class Grafcet {

	private String type;
	private String name;
	private String comment;
	private String owner; // propietario

	/* Variables que se usararn solo si el grafcet es de emergencia */
	private boolean emergency;
	/** Para saber las etapas de la emergencia */
	private String stepStopEmergency;
	private String stepStartEmergency;
	/** Lista de grafcets que se fuerzan en la emergencia */
	private LinkedList<String> listEmergencyStop;
	private LinkedList<String> listEmergencyStart;
	
	/* Lista de señales del grafcet */
	private LinkedList<String> signalsGrafcet;

	private LinkedList<Jump> jumpList;
	private LinkedList<Sequence> sequenceList;
	private LinkedList<Road> roadList;

	public Grafcet() {
		this.type = null;
		this.name = null;
		this.comment = null;
		this.owner = null;
		this.emergency = false;
		this.stepStartEmergency = null;
		this.stepStopEmergency= null;
		this.listEmergencyStop = new LinkedList<String>();
		this.listEmergencyStart = new LinkedList<String>();
		this.signalsGrafcet = new LinkedList<String>();
		this.jumpList = new LinkedList<Jump>();
		this.sequenceList = new LinkedList<Sequence>();
		this.roadList = new LinkedList<Road>();
	}
	
	public void fillAttributes (Map<String, String> pAttributes){
		this.type = pAttributes.get("type");
		this.name = pAttributes.get("name");
		this.comment = pAttributes.get("comment");
		this.owner = pAttributes.get("owner");
		
		Pattern pat = Pattern.compile("^GEmergencia.*|^GEmergency.*");
		Matcher mat = pat.matcher(getName().trim());
		if (mat.matches()) {
			this.emergency=true;
		}		
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

	public String getOwner() {
		return owner;
	}
	
	public LinkedList<Jump> getListJ() {
		return jumpList;
	}

	public LinkedList<Sequence> getListS() {
		return sequenceList;
	}
	
	public LinkedList<Road> getRoadList() {
		return roadList;
	}
	
	/** Si cambia si al anadir el nombre en el preproceso es GEmergencia */
	public boolean isEmergency() {
		return emergency;
	}

	public String getStepStopEmergency() {
		return stepStopEmergency;
	}

	

	public String getStepStartEmergency() {
		return stepStartEmergency;
	}
	
	public LinkedList<String> getListEmergencyStop() {
		return listEmergencyStop;
	}
	
	public LinkedList<String> getListEmergencyStart() {
		return listEmergencyStart;
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

/*	public void printGrafcet() {
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
	}*/

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
	
		Map<String, String> actionStepMap = new HashMap<String, String>();

		// por cada secuencia del grafcet
		for (Sequence sequence : sequenceList) {
			// obtengo el map de la secuencia
			Map<String, String> auxMap = sequence.getActionStepMap();
			// por cada accion de auxMap
			for (String action : auxMap.keySet()) {
				// si la key no existe en actionStepMap
				if (actionStepMap.get(action) == null) {
					// añado
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
	 * Recibe la secuencia y el numero rellenara la lista de seccuencias la
	 * posicion de la lista sera el numero de secuencia -1
	 */
	public void addSequence(Sequence pSequence) {
		/*
		 * obntengo su lista de señales de la secuencia para añadirla a la lista
		 * de señales del grafcet
		 */
		this.signalsGrafcet.addAll(pSequence.getSignals());
		/* la posicion de la lista sera el numero de secuencia -1 */
		this.sequenceList.add(/* pIndex-1, */pSequence);

	}

	public void addRoad(Road pRoad) {
		this.roadList.add(pRoad);
	}
	
	public void addJump(Jump pJ) {
		this.jumpList.add(pJ);
	}

	public void addStepStopEmergency(String pStepStopEmergency) {
		this.stepStopEmergency = pStepStopEmergency;
	}
	public void addStepStartEmergency(String pStepStartEmergency) {
		this.stepStartEmergency = pStepStartEmergency;
	}
	public void addListEmergencyStop(LinkedList<String> pListEmergencyStop) {
		this.listEmergencyStop = pListEmergencyStop;
	}
	public void addListEmergencyStart(LinkedList<String> pListEmergencyStart) {
		this.listEmergencyStart = pListEmergencyStart;
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

		}
		/* Por cada camino que tengamos */
		for (Road road : roadList) {
			// por cada secuencia de la lista de caminos
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

					if (road.getType().equals("conv and")) {
						// busco la primera etapa de la secuencia road.SeqInit y
						// activar setAnd=true;
						Step step = sequenceList.get(road.getSeqIni()).getFirstStep();
						step.setAnd(true);
					}
				}
			}
		}
	}

	public LinkedList<String> getPreviousStepAndTransitionFromSequence(LinkedList<Integer> sequenceIDList,
			String previousTransition) {
		LinkedList<String> previousStepAndTransitionList = new LinkedList<String>();
		// por cada lista de idSecuencias
		for (Integer sequenceID : sequenceIDList) {
			// obtengo la secuencia con ese ID
			Sequence sequence = this.sequenceList.get(sequenceID);
			// Creo una variable para la transicion y la inicializo a null
			Transition trans = null;
			String transName = null;
			// Si no me han pasado una transicion por parametro
			if (previousTransition == null) {
				trans = sequence.getLastTransition();
				if (trans != null) {
					transName = trans.getCondition();
				}
			} else {
				transName = previousTransition;
			}

			Step step = sequence.getLastStep();

			if (step == null && transName == null) {
				previousStepAndTransitionList.addAll(getPreviousStepAndTransitionFromSequence(
						sequence.getPreviousSequencesList(), previousTransition));
			} else if (step == null && transName != null) {
				previousStepAndTransitionList.addAll(
						getPreviousStepAndTransitionFromSequence(sequence.getPreviousSequencesList(), transName));
			} else if (step != null && transName != null) {
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

	/**
	 * Metodo que añade los set y reset a cada una de las distintas etapas que
	 * componen un grafcet
	 */
	public void addSetAndResetToStep() {
		//por cada secuencia de la lista
		for (Sequence seq : sequenceList) {
			//por cada lista de transiciones o paso de la secuencia
			for (int i = 0; i < seq.getListTransitionOrStep().size(); i++) {
				//si es un paso
				if (seq.getListTransitionOrStep().get(i) instanceof Step) {
					
					Step actualStep = (Step) seq.getListTransitionOrStep().get(i);

					// Rellenamos el Set
					
					//inicializo variables auxiliares
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
						if(!previousTransition.getCondition().equals("")){
							previousStepAndTransitions
							.add("(" + previousStep.getName() + " AND " + previousTransition.getCondition() + ")");
						}else{
							previousStepAndTransitions
							.add( previousStep.getName() );
						}
						
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

					// Relleno la lista con los Set-Reset
					functionBlock.add("\n(* Set -Reset ___________________________"
							+ "_____________________________________ " + actualStep + " *)");

					//compruebo si el set contienen la salida de un tep o un cont
					
					String set = ((Step) obj).getMySet();
					String reset = ((Step) obj).getMyReset();
					
					
					//TODO AQUI ESTABA SET RESET
					
					
						/* TODO Contadores como se escriben en el Function Bloc segun el tipo
						int index = Project.getProject().equalsCount(set.substring(set.indexOf("Cont"), set.indexOf("==")).trim());
						if(index != -1){
							String type = Project.getProject().getListCounters().get(index).getTypeCounter();
							if(type.equals(GrafcetTagsConstants.typeCounter.CTD)){
								set = set.replaceAll("/X[0-9]./[0-9]", "Q");
							}else if(type.equals(GrafcetTagsConstants.typeCounter.CTU)){
								set = set.replaceAll("/X[0-9]./[0-9]", "Q");
							}else if(type.equals(GrafcetTagsConstants.typeCounter.CTUD)){
								//no se cuando poner cada uno
								set = set.replaceAll("/X[0-9]./[0-9]", "QU"); 
								set = set.replaceAll("/X[0-9]./[0-9]", "QD");
							}				
						}*/
					
							
					
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
	
	public LinkedList<String> generateSequentialPart(String pAuxStepStartEmergency, String pAuxStepStopEmergency) {
		LinkedList<String> listSP = new LinkedList<String>();
		
		String actualStep = "", auxSet = "", auxReset = "";
				
		listSP.add("\n(*---------------------------------------\n" + getName().substring(1, getName().length())
				+ "\n" + getComment() + "\n-----------------------------------------------*)");
		
		// por cada seccuencia
		for (Sequence seq : sequenceList) {
			// recorro la lista de la secuencia
			// Object obj : seq.getList()
			for (Object obj : seq.getListTransitionOrStep()) {
				/* Si es una etapa */
				if (obj instanceof Step) {
					actualStep = ((Step) obj).getName();

					// Relleno la lista con los Set-Reset
					listSP.add("\n(* Set -Reset ___________________________"
							+ "_____________________________________ " + actualStep + " *)");

								
					String set = WordUtils.capitalize(((Step) obj).getMySet());
					
					set.replaceAll(" RE.*//.Q ", "");
					
					set.replaceAll(" Not ", " NOT ");
					set.replaceAll(" And ", " AND ");
					set.replaceAll(" Or ", " OR ");
					set.replaceAll(" Re ", " RE ");
					set.replaceAll(" Fe ", " FE ");
					
					String reset = WordUtils.capitalize(((Step) obj).getMyReset());
					
					reset.replaceAll(" Not ", " NOT ");
					reset.replaceAll(" And ", " AND ");
					reset.replaceAll(" Or ", " OR ");
					reset.replaceAll(" Re |Re ", " RE ");
					reset.replaceAll(" Fe |Fe  ", " FE ");
					
					//TODO AQUI ESTABA SET RESET
					
					
						/* TODO Contadores como se escriben en el Function Bloc segun el tipo
						int index = Project.getProject().equalsCount(set.substring(set.indexOf("Cont"), set.indexOf("==")).trim());
						if(index != -1){
							String type = Project.getProject().getListCounters().get(index).getTypeCounter();
							if(type.equals(GrafcetTagsConstants.typeCounter.CTD)){
								set = set.replaceAll("/X[0-9]./[0-9]", "Q");
							}else if(type.equals(GrafcetTagsConstants.typeCounter.CTU)){
								set = set.replaceAll("/X[0-9]./[0-9]", "Q");
							}else if(type.equals(GrafcetTagsConstants.typeCounter.CTUD)){
								//no se cuando poner cada uno
								set = set.replaceAll("/X[0-9]./[0-9]", "QU"); 
								set = set.replaceAll("/X[0-9]./[0-9]", "QD");
							}				
						}*/
					
							
					
					// si es una etapa inicial y no es el grafcet de emergencia
					if (((Step) obj).getType().equals("initial") && !isEmergency()) {
						auxSet = "\n\tIF (" + set + " OR Iniciografcets"+pAuxStepStartEmergency+") THEN\n\t\t" + actualStep + ":=1;";
						auxReset = "\n\tEND_IF;\n\tIF (" + reset + pAuxStepStopEmergency+") THEN\n\t\t" + actualStep + ":=0;";
					} else if (!((Step) obj).getType().equals("initial") && !isEmergency()){
						auxSet = "\n\tIF ( " + set + " ) THEN\n\t\t" + actualStep + ":=1;";
						auxReset = "\n\tEND_IF;\n\tIF (" + reset + " OR Iniciografcets)"+ pAuxStepStopEmergency+") THEN\n\t\t" + actualStep
								+ ":=0;";
					}else if (((Step) obj).getType().equals("initial") && isEmergency()){
						auxSet = "\n\tIF (" + set + " OR Iniciografcets) THEN\n\t\t" + actualStep + ":=1;";
						auxReset = "\n\tEND_IF;\n\tIF (" + reset + ") THEN\n\t\t" + actualStep
								+ ":=0;";
					}else if (!((Step) obj).getType().equals("initial") && isEmergency()){
						auxSet = "\n\tIF (" + set + ") THEN\n\t\t" + actualStep + ":=1;";
						auxReset = "\n\tEND_IF;\n\tIF (" + reset + ") THEN\n\t\t" + actualStep
								+ ":=0;";
					}
					listSP.add(auxSet);
					listSP.add(auxReset);
					listSP.add("\n\tEND_IF;\n");
				}
			}
		}
		return listSP;

	}

	/** Rellena las listas de emergencia */
	public void getEmergency() {

		for (Sequence s : getListS()) {

			//s.getEmergency();

			if (s.getStepStartEmergency() != -1) {
				Step stepStart = (Step) s.getListTransitionOrStep().get(s.getStepStartEmergency());
				addStepStartEmergency(stepStart.getName());
				addListEmergencyStart(stepStart.getGrafcetsStartEmergency());
				Project.getProject().addStepStartEmergency(stepStart.getName());
				Project.getProject().addListEmergencyStart(stepStart.getGrafcetsStartEmergency());

			} else if (s.getStepStopEmergency() != -1) {
				Step stepStop = (Step) s.getListTransitionOrStep().get(s.getStepStopEmergency());
				addStepStopEmergency(stepStop.getName());
				addListEmergencyStop(stepStop.getGrafcetsStopEmergency());
				Project.getProject().addStepStopEmergency(stepStop.getName());
				Project.getProject().addListEmergencyStop(stepStop.getGrafcetsStopEmergency());
				
			}

		}

	}

	public boolean compareStartAndStopLists() {
		Collections.sort(this.listEmergencyStop);
		Collections.sort(this.listEmergencyStart);
		return getListEmergencyStart().equals(getListEmergencyStop());
	}

	/** Esta lista se va rellenando al ir añadiendo una secuencia al grafcet */
	public LinkedList<String> getListSignalsGrafcet() {
		return this.signalsGrafcet;
	}
	/**Devuelve un grafcet comparando su nombre*/
	public Grafcet equals(String pName){
		Grafcet g = null;
		if (getName().equals(pName)){
			g =  this;
		}
		return g;
	}
}
