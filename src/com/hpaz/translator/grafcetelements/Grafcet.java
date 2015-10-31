package com.hpaz.translator.grafcetelements;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.text.WordUtils;

import com.hpaz.translator.grafcetelements.constants.GrafcetTagsConstants;

public class Grafcet {

	private String type;
	private String name;
	private String comment;
	private String owner; // propietario

	/* Variables que se usararn solo si el grafcet es de emergencia */
	private boolean emergency;
	/*Para saber las etapas de la emergencia private String stepStopEmergency;
	 * private String stepStartEmergency;
	 * Lista de grafcets que se fuerzan en la emergencia private LinkedList
	 * <String> listEmergencyStop; private LinkedList
	 * <String> listEmergencyStart; */

	/* Lista de se�ales del grafcet */
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
		/*
		 * this.stepStartEmergency = null; this.stepStopEmergency= null;
		 * this.listEmergencyStop = new LinkedList<String>();
		 * this.listEmergencyStart = new LinkedList<String>();
		 */
		this.signalsGrafcet = new LinkedList<String>();
		this.jumpList = new LinkedList<Jump>();
		this.sequenceList = new LinkedList<Sequence>();
		this.roadList = new LinkedList<Road>();
	}

	public void fillAttributes(Map<String, String> pAttributes) {
		addType(pAttributes.get("type"));
		addName(pAttributes.get("name"));
		addComment(pAttributes.get("comment"));
		addOwner(pAttributes.get("owner"));

		Pattern pat = Pattern.compile("^GEmergencia.*|^GEmergency.*");
		Matcher mat = pat.matcher(getName().trim());
		if (mat.matches()) {
			addEmergency(true);
		}
	}

	public String getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	/*
	 * private String getComment() { return comment; }
	 */

	public String getOwner() {
		return owner;
	}

	public LinkedList<Jump> getJumpList() {
		return jumpList;
	}

	public LinkedList<Sequence> getSequenceList() {
		return sequenceList;
	}

	public LinkedList<Road> getRoadList() {
		return roadList;
	}

	/** Si cambia si al anadir el nombre en el preproceso es GEmergencia */
	public boolean isEmergency() {
		return emergency;
	}
	/*
	 * public String getStepStopEmergency() { return stepStopEmergency; }
	 * 
	 * public String getStepStartEmergency() { return stepStartEmergency; }
	 * 
	 * public LinkedList<String> getListEmergencyStop() { return
	 * listEmergencyStop; }
	 * 
	 * public LinkedList<String> getListEmergencyStart() { return
	 * listEmergencyStart; }
	 */

	public LinkedList<String> getGrafcetVarGlobalStages(String pTypeProgram) {
		LinkedList<String> listReturnVarGlovalStages = new LinkedList<String>();

		if (pTypeProgram.equals(GrafcetTagsConstants.PROGRAM_OPT1)) {// TwinCat
			listReturnVarGlovalStages.add("\n\t(*---" + this.name + "---*)\n\n");
		}

		// por cada secuencia de la lista
		for (Sequence s : sequenceList) {
			listReturnVarGlovalStages.addAll(s.getVarGlobalStages(pTypeProgram));
		}
		return listReturnVarGlovalStages;
	}

	public LinkedList<String> getGrafcetExternalVars() {
		LinkedList<String> externalVars = new LinkedList<String>();
		// por cada secuencia de la lista para obtener la etapa
		for (Sequence sequence : getSequenceList()) {
			externalVars.addAll(sequence.getStepExternalVars());
		}
		// por cada se�al del grafcet
		// quito los repetidos
		LinkedList<String> listSignals = removeDuplicates(getListSignalsGrafcet());
		for (String signal : listSignals) {
			externalVars.add("<variable name=\"" + signal + "\" group=\"Default\"><type><BOOL /></type></variable>");
		}

		return externalVars;
	}

	public LinkedList<String> grafcetVarGlobalSignals() {
		LinkedList<String> vG = new LinkedList<String>();
		for (String signal : this.signalsGrafcet) {
			vG.add("\t" + signal + "\t: BOOL;\n");
		}

		return vG;
	}

	// genera un listado con los set y reset listos para exportar
	/*
	 * private LinkedList<String> printSetReset() { LinkedList<String> sR = new
	 * LinkedList<String>(); for (Sequence s : sequenceList) {
	 * sR.addAll(s.getSetReset()); } return sR; }
	 */

	/*
	 * public void printGrafcet() { System.out.println("----- GRAFCET ------");
	 * System.out.println("Nombre: " + this.name); System.out.println("Tipo: " +
	 * this.type); System.out.println("Propietario: " + this.owner);
	 * System.out.println("Comentario: " + this.comment);
	 * 
	 * for (Jump j : jumpList) { j.printJump(); } for (Road r : roadList) {
	 * r.printRoad(); } for (Sequence s : sequenceList) { s.printSequence(); } }
	 */

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
					// a�ado
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
		 * obntengo laa lista de se#ales de la secuencia para a#adirla a la
		 * lista de se#ales del grafcet
		 */
		this.signalsGrafcet.addAll(pSequence.getSignals());
		/* la posicion de la lista sera el numero de secuencia -1 */
		this.sequenceList.add(pSequence.getIdSeq() - 1, pSequence);

	}

	public void addRoad(Road pRoad) {
		this.roadList.add(pRoad);
	}

	public void addJump(Jump pJ) {
		this.jumpList.add(pJ);
	}

	/*
	 * public void addStepStopEmergency(String pStepStopEmergency) {
	 * this.stepStopEmergency = pStepStopEmergency; } public void
	 * addStepStartEmergency(String pStepStartEmergency) {
	 * this.stepStartEmergency = pStepStartEmergency; } public void
	 * addListEmergencyStop(LinkedList<String> pListEmergencyStop) {
	 * this.listEmergencyStop = pListEmergencyStop; } public void
	 * addListEmergencyStart(LinkedList<String> pListEmergencyStart) {
	 * this.listEmergencyStart = pListEmergencyStart; }
	 */

	/**
	 * Esta funcion rellenara las listas PreviousStepLits,
	 * PreviousTransitionList y getNextStepLits de cada secuencia, que nos
	 * serviran luego a la hora de rellenar los set y reset de cada etapa
	 */
	public void fillPreviousAndNextSequencesLists() {
		/* por cada salto */
		for (Jump j : getJumpList()) {
			/*
			 * FromSeq salta a ToSeq por lo que la seq(fromToSeq-1) tendra en
			 * next la ToSeq-1 la seq(Toseq-1) tendra en previous fromToSeq-1
			 */
			int auxNumberFromSequence = j.getFromSeq();
			int auxNumberToSequence = j.getToSeq();
			sequenceList.get(auxNumberFromSequence).addNextSequencesList(auxNumberToSequence);
			sequenceList.get(auxNumberToSequence).addPreviousSequencesList(auxNumberFromSequence);

		}
		/* Por cada camino que tengamos */
		for (Road road : getRoadList()) {
			// por cada secuencia de la lista de caminos
			for (Integer numSequencesRoad : road.getSequences()) {
				int seqIniRoad = road.getSeqIni();
				String typeRoad = road.getType();
				if (road.getType().equals("div or") || road.getType().equals("div and")) {
					/*
					 * Guardo en la secuencia roadIni.next la secuencia
					 * numSequencesRoad y en la secuencia
					 * numSequencesRoad.previous guardo roadIni
					 */
					sequenceList.get(seqIniRoad).addNextSequencesList(numSequencesRoad);
					sequenceList.get(numSequencesRoad).addPreviousSequencesList(seqIniRoad);

				} else if (typeRoad.equals("conv or") || typeRoad.equals("conv and")) {
					/*
					 * Guardo en la secuencia roadIni.previous la secuencia
					 * numSequencesRoad y en la secuencia numSequencesRoad.next
					 * guardo roadIni
					 */
					sequenceList.get(seqIniRoad).addPreviousSequencesList(numSequencesRoad);
					sequenceList.get(numSequencesRoad).addNextSequencesList(seqIniRoad);

					if (typeRoad.equals("conv and")) {
						// busco la primera etapa de la secuencia road.SeqInit y
						// activar setAnd=true;
						Step step = sequenceList.get(seqIniRoad).getFirstStep();
						step.addAnd(true);
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
	 * Metodo que a#ade los set y reset a cada una de las distintas etapas que
	 * componen un grafcet
	 */
	public void addSetAndResetToStep() {
		// por cada secuencia de la lista
		for (Sequence seq : getSequenceList()) {
			int sizeListTransitionOrStep = seq.getListTransitionOrStep().size();
			// por cada lista de transiciones o paso de la secuencia
			for (int i = 0; i < sizeListTransitionOrStep; i++) {
				// si es un paso
				if (seq.getListTransitionOrStep().get(i) instanceof Step) {
					Step actualStep = (Step) seq.getListTransitionOrStep().get(i);

					// Buscamos el Set

					// inicializo variables auxiliares
					LinkedList<String> previousStepAndTransitions = new LinkedList<String>();
					Transition previousTransition = null;
					Step previousStep = null;
					/*
					 * Busca en los anteriores elementos de la lista de
					 * getListTransitionOrStep hasta encontrar una transicion y
					 * el step del set de la etapa en la q estamos
					 */
					/*
					 * TODO mirar porq estaria mejor hacer un while para q deje
					 * de mirar cuando: previousStep y previousTransition sean
					 * distintos de null
					 */
					for (int previousIndex = i - 1; previousIndex >= 0; previousIndex--) {
						// busca recursivamente
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
						String auxCondition = previousTransition.getCondition();
						String auxNameStep = previousStep.getName();
						if (!auxCondition.equals("")) {
							previousStepAndTransitions.add("(" + auxNameStep + " AND " + auxCondition + ")");
						} else {
							previousStepAndTransitions.add(auxNameStep);
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

					// Buscamos el Reset
					LinkedList<String> nextSteps = new LinkedList<String>();
					Step nextStep = null;
					/*
					 * Buscamos en los siguientes elementos de la
					 * getListTransitionOrStep() de la secuencia
					 */
					
					for (int nextIndex = i + 1; nextIndex < sizeListTransitionOrStep; nextIndex++) {
						Object stepOrTransition =seq.getListTransitionOrStep().get(nextIndex);
						if (stepOrTransition instanceof Step && nextStep == null) {
							nextStep = (Step) stepOrTransition;
						}
					}
					
					// si tenemos en la secuencia el siguiente step lo
					// guardamos, sino los buscamos.
					if (nextStep != null) {
						nextSteps.add(nextStep.getName());
					} else {// Buscamos en las siguiente secuencias
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

	/*private LinkedList<String> generateFunctionBlockPLCOpen() {
		// TODO FUNCTION PLCOPEN
		LinkedList<String> functionBlock = new LinkedList<String>();
		String actualStep = "", auxSet = "", auxReset = "";
		functionBlock.add("<pou name=\"" + getName() + "\" pouType=\"functionBlock\" lastChange=\"\">" + "<interface>"
				+ "<inputVars retain=\"false\">" + "<variable name=\"Init\" group=\"Default\">"
				+ "<type><BOOL /></type>" + "</variable>" + "<variable name=\"Reset\" group=\"Default\">"
				+ "<type><BOOL /></type>" + "</variable>" + "</inputVars>");
		functionBlock.add("<externalVars retain=\"false\">");
		// quito los elementos duplicados de la lista de señales del grafcet
		LinkedList<String> listSignals = removeDuplicates(getListSignalsGrafcet());
		for (String signalGraf : listSignals) {
			// las señales de ese grafcet
			functionBlock.add("<variable name=\"" + signalGraf + "\" group=\"Default\">" + "<type><BOOL /></type>"
					+ "</variable>");
		}
		functionBlock.add("</externalVars></interface>");

		functionBlock.add(
				"<body><ST><worksheet name=\"" + getName() + "\">" + "<html xmlns=\"http://www.w3.org/1999/xhtml\">"
						+ "<p xmlns=\"http://www.w3.org/1999/xhtml\" xml:space=\"preserve\">"
						+ "(*<br /> ----------------------<br /> " + getName()
						+ ":  <br />---------------------------------*)<br />");
		// por cada seccuencia
		for (Sequence seq : sequenceList) {
			// recorro la lista de etapas y transiciones la secuencia
			for (Object obj : seq.getListTransitionOrStep()) {
				 Si es una etapa 
				if (obj instanceof Step) {
					actualStep = ((Step) obj).getName();
					// Relleno la lista con los Set-Reset

					functionBlock.add("<br /> (* Set - Reset ___________________________"
							+ "_____________________________________ " + actualStep + " *)");
					// Obtengo los Set-Reset
					String set = ((Step) obj).getMySet();
					String reset = ((Step) obj).getMyReset();

					// si es una etapa inicial
					if (((Step) obj).getType().equals("initial")) {
						auxSet = "<br /><br />IF ( " + set + " OR Init ) THEN<br />" + actualStep + ":=1;"
								+ "<br />END_IF;";
						auxReset = "<br /><br /><br />IF ( " + reset + " OR Reset ) THEN<br />" + actualStep + ":=0;"
								+ "<br />END_IF;";
					} else {
						auxSet = "<br /><br />IF ( " + set + " ) THEN<br />" + actualStep + ":=1;" + "<br />END_IF;";
						auxReset = "<br /><br />IF ( " + reset + " OR Init OR Reset ) THEN<br />" + actualStep + ":=0;"
								+ "<br />END_IF;";
					}
					functionBlock.add(auxSet);
					functionBlock.add(auxReset);
				}
			}
		}

		functionBlock.add("</p></html>" + "</worksheet>" + "</ST>" + "</body>" + "<documentation>"
				+ "<html xmlns=\"http://www.w3.org/1999/xhtml\">"
				+ "<div xmlns=\"http://www.w3.org/1999/xhtml\" xml:space=\"preserve\""
				+ "id=\"MWTDESCRIPTION\" wsName=\"" + getName() + "T\" />" + "</html>" + "</documentation>" + "</pou>");

		return functionBlock;
	}*/

	public LinkedList<String> generateFunctionBlock() {
		LinkedList<String> functionBlock = new LinkedList<String>();
		String actualStep = "", auxSet = "", auxReset = "";

		functionBlock.add("\nFUNCTION_BLOCK " + this.name + "\n\tVAR_INPUT"
				+ "\n\t\tInit\t:BOOL;\n\t\tReset\t:BOOL;\n\tEND_VAR" + "\n\tVAR_OUTPUT\n\tEND_VAR\n\tVAR\n\tEND_VAR");

		functionBlock.add("\n(*---------------------------------------\n" + this.name.substring(1, this.name.length())
				+ "\n" + this.comment + "\n-----------------------------------------------*)");

		// por cada seccuencia
		for (Sequence seq : getSequenceList()) {
			// recorro la lista de la secuencia
			// Object obj : seq.getList()
			for (Object obj : seq.getListTransitionOrStep()) {
				/* Si es una etapa */
				if (obj instanceof Step) {
					actualStep = ((Step) obj).getName();

					// Relleno la lista con los Set-Reset
					functionBlock.add("\n(* Set -Reset ___________________________"
							+ "_____________________________________ " + actualStep + " *)");

					// compruebo si el set contienen la salida de un tep o un
					// cont

					String set = ((Step) obj).getMySet();
					String reset = ((Step) obj).getMyReset();
					
				
					// TODO AQUI ESTABA SET RESET

					/*
					 * TODO Contadores como se escriben en el Function Bloc
					 * segun el tipo int index =
					 * Project.getProject().equalsCount(set.substring(set.
					 * indexOf("Cont"), set.indexOf("==")).trim()); if(index !=
					 * -1){ String type =
					 * Project.getProject().getListCounters().get(index).
					 * getTypeCounter();
					 * if(type.equals(GrafcetTagsConstants.typeCounter.CTD)){
					 * set = set.replaceAll("/X[0-9]./[0-9]", "Q"); }else
					 * if(type.equals(GrafcetTagsConstants.typeCounter.CTU)){
					 * set = set.replaceAll("/X[0-9]./[0-9]", "Q"); }else
					 * if(type.equals(GrafcetTagsConstants.typeCounter.CTUD)){
					 * //no se cuando poner cada uno set =
					 * set.replaceAll("/X[0-9]./[0-9]", "QU"); set =
					 * set.replaceAll("/X[0-9]./[0-9]", "QD"); } }
					 */

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
		//functionBlock.add("\nEND_FUNCTION_BLOCK");
		return functionBlock;

	}

	public LinkedList<String> generateSequentialPart(String pAuxStepStartEmergency, String pAuxStepStopEmergency) {
		LinkedList<String> listSP = new LinkedList<String>();

		String actualStep = "", auxSet = "", auxReset = "";
		listSP.add("\n(*---------------------------------------\n" + this.name.substring(1, this.name.length()) + "\n"
				+ this.comment + "\n-----------------------------------------------*)");

		// por cada seccuencia
		for (Sequence seq : getSequenceList()) {
			// recorro la lista de la secuencia
			// Object obj : seq.getList()
			for (Object obj : seq.getListTransitionOrStep()) {
				/* Si es una etapa */
				if (obj instanceof Step) {
					actualStep = ((Step) obj).getName();

					// Relleno la lista con los Set-Reset
					listSP.add("\n(* Set -Reset ___________________________" + "_____________________________________ "
							+ actualStep + " *)");

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

					// TODO AQUI ESTABA SET RESET

					/*
					 * TODO Contadores como se escriben en el Function Bloc
					 * segun el tipo int index =
					 * Project.getProject().equalsCount(set.substring(set.
					 * indexOf("Cont"), set.indexOf("==")).trim()); if(index !=
					 * -1){ String type =
					 * Project.getProject().getListCounters().get(index).
					 * getTypeCounter();
					 * if(type.equals(GrafcetTagsConstants.typeCounter.CTD)){
					 * set = set.replaceAll("/X[0-9]./[0-9]", "Q"); }else
					 * if(type.equals(GrafcetTagsConstants.typeCounter.CTU)){
					 * set = set.replaceAll("/X[0-9]./[0-9]", "Q"); }else
					 * if(type.equals(GrafcetTagsConstants.typeCounter.CTUD)){
					 * //no se cuando poner cada uno set =
					 * set.replaceAll("/X[0-9]./[0-9]", "QU"); set =
					 * set.replaceAll("/X[0-9]./[0-9]", "QD"); } }
					 */

					String type = ((Step) obj).getType();
					boolean gEmergency = isEmergency();
					// si es una etapa inicial y no es el grafcet de emergencia
					if (type.equals("initial") && !gEmergency) {
						auxSet = "\n\tIF (" + set + " OR Iniciografcets" + pAuxStepStartEmergency + ") THEN\n\t\t"
								+ actualStep + ":=1;";
						auxReset = "\n\tEND_IF;\n\tIF (" + reset + pAuxStepStopEmergency + ") THEN\n\t\t" + actualStep
								+ ":=0;";
					} else if (!type.equals("initial") && gEmergency) {
						auxSet = "\n\tIF (" + set + ") THEN\n\t\t" + actualStep + ":=1;";
						auxReset = "\n\tEND_IF;\n\tIF (" + reset + ") THEN\n\t\t" + actualStep + ":=0;";
					} else if (!type.equals("initial") && !gEmergency) {
						auxSet = "\n\tIF ( " + set + " ) THEN\n\t\t" + actualStep + ":=1;";
						auxReset = "\n\tEND_IF;\n\tIF (" + reset + " OR Iniciografcets)" + pAuxStepStopEmergency
								+ ") THEN\n\t\t" + actualStep + ":=0;";
					} else if (type.equals("initial") && gEmergency) {
						auxSet = "\n\tIF (" + set + " OR Iniciografcets) THEN\n\t\t" + actualStep + ":=1;";
						auxReset = "\n\tEND_IF;\n\tIF (" + reset + ") THEN\n\t\t" + actualStep + ":=0;";
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

		for (Sequence s : getSequenceList()) {
			
			int auxNumberStepStarEmergency = s.getStepStartEmergency();
			int auxNumberStepStopEmergency = s.getStepStopEmergency();
			
			if (auxNumberStepStarEmergency != -1) {
				Step stepStart = (Step) s.getListTransitionOrStep().get(auxNumberStepStarEmergency);
				String nameStep = stepStart.getName();
				LinkedList<String>  listAux = stepStart.getGrafcetsStartEmergency();
				/*addStepStartEmergency(nameStep);
				 * addListEmergencyStart(listAux);*/
				Project.getProject().addStepStartEmergency(nameStep);
				Project.getProject().addListEmergencyStart(listAux);
			} 
			if (auxNumberStepStopEmergency != -1) {
					Step stepStop = (Step) s.getListTransitionOrStep().get(auxNumberStepStopEmergency);
					String nameStep = stepStop.getName();
					LinkedList<String>  listAux = stepStop.getGrafcetsStopEmergency();
					/*addStepStopEmergency(stepStop.getName());
					 * addListEmergencyStop(stepStop.getGrafcetsStopEmergency()); */
					Project.getProject().addStepStopEmergency(nameStep);
					Project.getProject().addListEmergencyStop(listAux);
			}

		}

	}

	/*
	 * public boolean compareStartAndStopLists() {
	 * Collections.sort(this.listEmergencyStop);
	 * Collections.sort(this.listEmergencyStart); return
	 * getListEmergencyStart().equals(getListEmergencyStop()); }
	 */

	/**
	 * Esta lista se va rellenando al ir a�adiendo una secuencia al grafcet
	 */
	public LinkedList<String> getListSignalsGrafcet() {
		return this.signalsGrafcet;
	}

	/** Devuelve un grafcet comparando su nombre */
	public Grafcet equals(String pName) {
		Grafcet g = null;
		if (getName().equals(pName)) {
			g = this;
		}
		return g;
	}

	/***
	 * Me devuelve por cada grafcet Las etapas iniciales excepto la de
	 * emergencia y las etapas que tiene una o mas acciones asociadas.
	 */
	public LinkedList<String> getGrafcetExternalVarsMain() {
		LinkedList<String> externalVars = new LinkedList<String>();
		// por cada secuencia de la lista para obtener la etapa
		for (Sequence sequence : getSequenceList()) {
			externalVars.addAll(sequence.getStepExternalVarsMain(this.emergency));
		}

		return externalVars;
	}

	public LinkedList<String> generateBody() {
		LinkedList<String> functionBlock = new LinkedList<String>();
		String auxSet, auxReset;
		// por cada seccuencia
		for (Sequence seq : getSequenceList()) {
			// recorro la lista de etapas y transiciones la secuencia
			for (Object obj : seq.getListTransitionOrStep()) {
				/* Si es una etapa */
				if (obj instanceof Step) {
					String actualStep = ((Step) obj).getName();
					// Relleno la lista con los Set-Reset

					// Obtengo los Set-Reset
					String set = ((Step) obj).getMySet();
					String reset = ((Step) obj).getMyReset();

					// si es una etapa inicial
					if (((Step) obj).getType().equals("initial")) {
						auxSet = "<br /><br />IF ( " + set + " OR Init ) THEN" + "<br />" + actualStep + ":=1;"
								+ "<br />END_IF;";
						auxReset = "<br /><br />IF ( " + reset + " OR Reset ) THEN" + "<br />" + actualStep + ":=0;"
								+ "<br />END_IF;";
					} else {
						auxSet = "<br /><br />IF ( " + set + " ) THEN" + "<br />" + actualStep + ":=1;"
								+ "<br />END_IF;";
						auxReset = "<br />IF ( " + reset + " OR Init OR Reset ) THEN" + "<br />" + actualStep + ":=0;"
								+ "<br />END_IF;";
					}
					functionBlock.add(auxSet);
					functionBlock.add(auxReset);
				}
			}
		}
		return functionBlock;
	}

	private void addEmergency(boolean emergency) {
		this.emergency = emergency;
	}

	private void addType(String type) {
		this.type = type;
	}

	private void addName(String name) {
		this.name = name;
	}

	private void addComment(String comment) {
		this.comment = comment;
	}

	private void addOwner(String owner) {
		this.owner = owner;
	}

	/**
	 * Devuelve la lista que le pasan por parametro pero sin elementos repetidos
	 */
	private LinkedList<String> removeDuplicates(LinkedList<String> listDuplicate) {

		LinkedList<String> listwithoutduplicates = new LinkedList<String>();

		for (String string : listDuplicate) {
			if (!listwithoutduplicates.contains(string)) {
				listwithoutduplicates.add(string);
			}
		}

		return listwithoutduplicates;
	}

}
