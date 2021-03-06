package com.hpaz.translator.grafcetelements;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.text.WordUtils;
import com.hpaz.translator.grafcetelements.constants.GrafcetTagsConstants;
import com.hpaz.translator.output.Output;

public class Project {

	/** Nombre del fichero que se ha importado, sin extension */
	private String name;
	/**
	 * IL --> Lista de instrucciones. ST --> Texto estructurado. LD --> Diagrama
	 * de escalera. FBD --> Diagrama de bloques funcionales. SFC --> Grafico de
	 * funciones secuenciales.
	 */
	private String language;
	/**
	 * PL -> PLC TSX Micro (PL7Pro), T -> PLC Beckhoff (TwinCAT), PC -> PLCOpen
	 */
	private String program;
	private String outputDir;
	private LinkedList<Grafcet> listGrafcet;
	private LinkedList<Timer> listTimers;
	private LinkedList<Counter> listCounters;
	private LinkedList<String> listTimersUI;
	private LinkedList<String> listCountersUI;
	/**Para guardar las variables con flancos de subida o bajada**/
	private LinkedList<String> list_FE_and_RE;
	private Map<String, String> assignments;
	private Map<String, String> listUI;
	/**Se usa para guardar las asignaciones de las acciones*/
	private Map<String, String> actionStepMap;
	/** Lista de grafcets que se fuerzan en la emergencia */
	private LinkedList<String> listEmergencyStop;
	private LinkedList<String> listEmergencyStart;
	/** Para saber las etapas de la emergencia */
	private String stepStopEmergency;
	private String stepStartEmergency;
	/**se�ales existentes en todo el proyecto*/
	private LinkedList<String> signalsProject;

	private static Project project = new Project();

	private Project() {
		this.name = null;
		this.language = null;
		this.program = null;
		this.outputDir = null;
		this.listGrafcet = new LinkedList<Grafcet>();
		this.listTimers = new LinkedList<Timer>();
		this.listCounters = new LinkedList<Counter>();
		this.listCountersUI = new LinkedList<String>();
		this.listTimersUI = new LinkedList<String>();
		this.listUI = null;
		this.signalsProject = new LinkedList<String>();
		this.list_FE_and_RE = new LinkedList<String>();
		this.actionStepMap = new HashMap<String, String>();
		this.listEmergencyStart = new LinkedList<String>();
		this.listEmergencyStop = new LinkedList<String>();
		this.stepStopEmergency = null;
		this.stepStartEmergency = null;

	}

	public String getProgram() {
		return program;
	}

	public static Project getProject() {
		return project;
	}

	public void addOutputPath(String pOutputDir) {
		this.outputDir = pOutputDir;
	}

	public void addName(String name) {
		if (this.name == null)
			this.name = name;
	}

	public void addLanguage(String language) {
		if (this.language == null)
			this.language = language;
	}

	public void addProgram(String program) {
		if (this.program == null)
			this.program = program;
	}

	/**
	 * Anado el grafcet
	 */
	public void addGrafcet(Grafcet pGrafcet) {
		this.listGrafcet.add(pGrafcet);
	}

	public LinkedList<Timer> getListTimers() {
		return listTimers;
	}

	public void addTimer(Timer pTimer) {
		addListTimersUI(pTimer.getNameTimer());
		addListTimers(pTimer);

	}

	public LinkedList<Counter> getListCounters() {
		return listCounters;
	}

	public void addCounter(Counter pCounters) {
		addListCountersUI(pCounters.getNameCounter());
		addListCounters(pCounters);
	}

	public LinkedList<String> getListTimersUI() {
		return listTimersUI;
	}

	public LinkedList<String> getListCountersUI() {
		return listCountersUI;
	}

	public void addStepStopEmergency(String stepStopEmergency) {
		this.stepStopEmergency = stepStopEmergency;
	}

	public void addStepStartEmergency(String stepStartEmergency) {
		this.stepStartEmergency = stepStartEmergency;
	}

	public void addListEmergencyStop(LinkedList<String> pListEmergencyStop) {
		this.listEmergencyStop.addAll(pListEmergencyStop);
	}

	public void addListEmergencyStart(LinkedList<String> pListEmergencyStart) {
		this.listEmergencyStart.addAll(pListEmergencyStart);
	}

	public void addProjectVariablesFromUserInterface(Map<String, String> variablesMap) {
		addListUI(variablesMap);

	}

	/** Genera un mapa a�adiendo cada accion y las etapas en las q se usa dic */
	public void generateActionStepMap(Map<String, String> auxMap) {

		// Map<String, String> auxMap = pGrafcet.getActionStepMap();
		for (String action : auxMap.keySet()) {
			if (getActionStepMap().get(action) == null)
				addActionStepMap(action, auxMap.get(action));
			else
				addActionStepMap(action, actionStepMap.get(action) + " OR " + auxMap.get(action));
		}
	}

	/**
	 * Este metodo llama al de exportar ficheros dependiendo del software de
	 * compatibilidad
	 */
	public void print() {
		try {
			if (program.equalsIgnoreCase(GrafcetTagsConstants.PROGRAM_OPT1)) { // Twincat
				// Var Global
				Output.getOutput().exportFile(getGlobalVars(generateGlobalVars()), getName() + "_VAR_GLOBAL", outputDir,
						".EXP");
				// Program Main
				Output.getOutput().exportFile(generateProgramMain(), getName() + "_PROGRAM_MAIN", outputDir, ".EXP");
				// Function Block --> uno por cada grafcet
				for (Grafcet g : listGrafcet) {
					Output.getOutput().exportFile(g.generateFunctionBlock(), "FUNCTION_BLOCK_" + g.getName(), outputDir,
							".EXP");
				}

			} else if (program.equalsIgnoreCase(GrafcetTagsConstants.PROGRAM_OPT2)) {// PL7PRO

				Output.getOutput().exportFile(getProgramTSXMicroSP(), getName() + "SequePart", outputDir, ".txt");
				Output.getOutput().exportFile(getProgramTSXMicroCP(), getName() + "CombiPart", outputDir, ".txt");

			} else if (program.equalsIgnoreCase(GrafcetTagsConstants.PROGRAM_OPT3)) {// PLCOpen
																						// //
																						// PCWorx
				Output.getOutput().exportFile(generatePousPLCOpen(), "POUS_" + getName() + "PLCOpen", outputDir,
						".xml");
				Output.getOutput().exportFile(generateVarGlobalPLCOpen(), "VarGlobal" + getName() + "PLCOpen",
						outputDir, ".xml");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public LinkedList<String> getSignalsProject() {
		generateSignalsProject();
		return this.signalsProject;
	}

	/**
	 * Busca en la lista de temporizadores si existe devuelve su indice de lo
	 * contrario devuelve -1
	 */
	public int equalsTimer(String pNameTimer) {
		int indexTimer = -1;
		int indexAux = 0;
		while (indexTimer == (-1) && indexAux < listTimers.size()) {
			if (listTimers.get(indexAux).equals(pNameTimer)) {
				indexTimer = indexAux;

			}
			indexAux++;
		}
		return indexTimer;
	}

	/**
	 * Busca en la lista de contadores si existe devuelve su indice de lo
	 * contrario devuelve -1
	 */
	public int equalsCount(String pNameCounter) {
		int indexCounter = -1;
		int indexAux = 0;
		while (indexCounter == (-1) && indexAux < listCounters.size()) {
			if (listCounters.get(indexAux).equals(pNameCounter)) {
				indexCounter = indexAux;
			}
			indexAux++;
		}
		return indexCounter;
	}

	public LinkedList<String> getList_FE_and_RE() {
		return list_FE_and_RE;
	}

	public void add_FE_and_RE(String p_FE_and_RE) {
		this.list_FE_and_RE.add(p_FE_and_RE);
	}

	/**
	 * Este metodo creara un Map de asignaciones en caso de que la transicion
	 * tenga
	 */
	public void addAssignments(String pSignal, String pAssignment) {
		// TODO REVISAR
		if (this.assignments == null) {
			this.assignments = new HashMap<String, String>();
		}
		this.assignments.put(pSignal, pAssignment);

	}

	private String getName() {
		return this.name;
	}

	private LinkedList<Grafcet> getListG() {
		return listGrafcet;
	}

	private LinkedList<String> getListEmergencyStop() {
		return listEmergencyStop;
	}

	private LinkedList<String> getListEmergencyStart() {
		return listEmergencyStart;
	}

	private String getStepStopEmergency() {
		return stepStopEmergency;
	}

	private String getStepStartEmergency() {
		return stepStartEmergency;
	}

	private Map<String, String> getListUI() {
		return listUI;
	}

	private Map<String, String> getActionStepMap() {
		return actionStepMap;
	}

	private void addActionStepMap(String pKey, String pValue) {
		this.actionStepMap.put(pKey, pValue);
	}

	private void addListTimers(Timer pTimer) {
		this.listTimers.add(pTimer);
	}

	private void addListTimersUI(String pNameTimer) {
		this.listTimersUI.add(pNameTimer);

	}

	private void addListCounters(Counter pCounters) {
		this.listCounters.add(pCounters);
	}

	private void addListCountersUI(String pNameCounter) {
		this.listCountersUI.add(pNameCounter);
	}

	private Map<String, String> getAssignments() {
		return assignments;
	}

	private LinkedList<String> generateGlobalVars() {
		LinkedList<String> listReturnVarGlobals = new LinkedList<String>();

		for (Grafcet g : getListG()) {
			listReturnVarGlobals.addAll(g.getGrafcetVarGlobalStages(this.program));
		}
		listReturnVarGlobals.add("\n\t(*---Se�ales---*)\n\n");

		listReturnVarGlobals.add("\tINIT\t:BOOL;\n\tRESET\t:BOOL;\n");

		for (String string : getList_FE_and_RE()) {
			char firstCharacter = string.charAt(0);
			String nameVar = string;
			listReturnVarGlobals.add("\t" + nameVar + "\t:" + firstCharacter + "_TRIG;\n");
			listReturnVarGlobals.add("\t" + nameVar + "Q\t:BOOL;\n");
		}

		// creo una lista para cada tipo de senal
		LinkedList<String> signalI = new LinkedList<String>();
		LinkedList<String> signalQ = new LinkedList<String>();
		LinkedList<String> signalM = new LinkedList<String>();
		LinkedList<String> signalK = new LinkedList<String>();
		LinkedList<String> signalS = new LinkedList<String>();

		// por cada una de las se�ales del proyecto
		for (String string : getSignalsProject()) {
			// Si esta en el hashMap de la UI
			if (getListUI().containsKey(string)) {
				// la uso
				String type = this.listUI.get(string);
				// si no tiene nada despues de las dos puntos : es un contador o
				// temporizador
				Pattern pat = Pattern.compile(".*:$");
				Matcher mat = pat.matcher(type);
				String auxString = string.trim();
				// Si el tipo indica que es una se�al
				if (!mat.matches()) {
					String[] typeDiv = type.split(":");
					String typeData = typeDiv[0];
					String typeVar = typeDiv[1];

					// ya tengo el tipo de dato y de variable, ahora por cada
					// una de ellas se excribe distinto
					if (typeData.equals("Entrada")) {
						// typeData = "I";
						signalI.add("\t" + auxString + " AT %I* : " + typeVar + ";\n");
					} else if (typeData.equals("Salida")) {
						// typeData = "Q";
						signalQ.add("\t" + auxString + " AT %Q* : " + typeVar + ";\n");
					} else if (typeData.equals("Memoria")) {
						// typeData = "M";
						signalM.add("\t" + auxString + " AT %M* : " + typeVar + ";\n");
					} else if (typeData.equals("Constante")) {
						// typeData = "K";
						signalK.add("\t" + auxString + " AT %K* : " + typeVar + ";\n");
					} else if (typeData.equals("Sistema")) {
						// typeData = "S";
						signalS.add("\t" + auxString + " AT %S* : " + typeVar + ";\n");
					}

					// vG.add("\t" + string + " AT %"+ typeData +"* :
					// "+typeVar+";\n");
				}
				// la eimino del HashMap
				this.listUI.remove(string);
			}
		}
		// uno las listas de las distintas se�ales
		listReturnVarGlobals.add("\n\t\t(*---Entradas---*)\n\n");
		listReturnVarGlobals.addAll(signalI);
		listReturnVarGlobals.add("\n\t\t(*---Salidas---*)\n\n");
		listReturnVarGlobals.addAll(signalQ);
		listReturnVarGlobals.add("\n\t\t(*---Memoria---*)\n\n");
		listReturnVarGlobals.addAll(signalM);
		listReturnVarGlobals.add("\n\t\t(*---Sistema---*)\n\n");
		listReturnVarGlobals.addAll(signalS);
		listReturnVarGlobals.add("\n\t\t(*---Constantes---*)\n\n");
		listReturnVarGlobals.addAll(signalK);

		// Si aun quedan elementos en el HashMAp
		if (!this.listUI.isEmpty()) {
			listReturnVarGlobals.add("\n\t(*---Temporizadores---*)\n\n");
			// Por cada temporizador
			for (int i = 0; i < getListTimers().size(); i++) {
				// Miro si el tempo esta en la lista de la IU y si es asi guardo
				// el tipo que selecciono el usuario
				String type = this.listUI.get(this.listTimers.get(i).getNameTimer());
				// A�ado el tipo de temporizador q he obtenido por la IU
				this.listTimers.get(i).addTypeTimer(type);
				listReturnVarGlobals.add(this.listTimers.get(i).getGlobalsVarTimer(this.program));
			}
			listReturnVarGlobals.add("\n\t(*---Contadores---*)\n\n");
			// Por cada Contador
			for (int j = 0; j < getListCounters().size(); j++) {
				// Miro si el cont esta en la lista de la IU y si es asi guardo
				// el tipo que selecciono el usuario
				String type = this.listUI.get(this.listCounters.get(j).getNameCounter());
				// A�ado el tipo de contador que ha seleccionado el usuario
				//this.listCounters.get(j).addTypeCounter(type);
				listReturnVarGlobals.add(this.listCounters.get(j).getGlobalsVarCounter(this.program, type));
			}
		}
		return listReturnVarGlobals;
	}

	private LinkedList<String> generateVarGlobalPLCOpen() {
		LinkedList<String> listReturnVarGlobals = new LinkedList<String>();

		// TODO NECESITO AQUI LA FECHA DE LA SIGUIENTE MANERA
		// 2015-07-23T13:03:20
		String fech = "2015-07-23T13:03:20";

		listReturnVarGlobals.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<project xmlns:xhtml=\"http://www.w3.org/1999/xhtml\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://www.kw-software.com/xml/PLCopen/TC6_XML_V10_KW.xsd\">"
				+ "	<fileHeader companyName=\"TFG UPV\" companyURL=\"\" productName=\"translator\" productVersion=\"0.1\" productRelease=\"\" creationDateTime=\""
				+ fech + "\" contentDescription=\"\" />	" + "	<contentHeader name=\"" + this.name
				+ "\" version=\"\" modificationDateTime=\"\">" + "		<coordinateInfo>"
				+ "			<fbd><scaling x=\"2\" y=\"2\" /></fbd>" + "			<ld><scaling x=\"2\" y=\"2\" /></ld>"
				+ "			<sfc><scaling x=\"2\" y=\"2\" /></sfc>" + "		</coordinateInfo>" + "	</contentHeader>"
				+ "	<types><dataTypes /><pous /></types>"
				+ "	<instances><configurations><configuration name=\"STD_CNF\"><resource name=\"STD_RES\"><globalVars name=\"\" retain=\"false\">");

		// El init y reset q pongo yo
		listReturnVarGlobals.add("<variable name=\"INIT\" address=\"%IX0.0\"><type><BOOL /></type></variable>");
		listReturnVarGlobals.add("<variable name=\"RESET\" address=\"%IX0.1\"><type><BOOL /></type></variable>");

		// consigo las se�ales con las ediciones de la IU
		// el address="%IX0.2 tiene q empezar aqui con el 2

		// por cada una de las se�ales del proyecto
		for (String nameSignal : getSignalsProject()) {
			// Si esta en el hashMap de la UI
			if (getListUI().containsKey(nameSignal)) {
				// la uso
				String type = this.listUI.get(nameSignal);

				Pattern pat = Pattern.compile(".*:$");
				Matcher mat = pat.matcher(type);

				// Si el tipo indica que es una se�al, ya que puede no serlo y
				// se ignoraria
				if (!mat.matches()) {

					String[] typeDiv = type.split(":");
					String typeData = typeDiv[0];
					String typeVar = typeDiv[1];

					// Inicializo un contador por cada tipo de variable para
					// poder enumerarlas
					int contI = 2, contQ = 0, contM = 0, contK = 0, contS = 0;

					// ya tengo el tipo de dato y de variable, ahora por cada
					// una de ellas se escribe distinto
					if (typeData.equals("Entrada")) {
						typeData = "%IX0." + contI;
						contI++;
					} else if (typeData.equals("Salida")) {
						typeData = "%QX0." + contQ;
						contQ++;
					} else if (typeData.equals("Memoria")) {
						typeData = "%MX0." + contM;
						contM++;
					} else if (typeData.equals("Constante")) {
						typeData = "%KX0." + contK;
						contK++;
					} else if (typeData.equals("Sistema")) {
						typeData = "%SX0." + contS;
						contS++;
					}

					listReturnVarGlobals.add("<variable name=\"" + nameSignal + "\" address=\"" + typeData
							+ "\"><type><" + typeVar + " /></type></variable>");
				}
				// la elimino del HashMap para de esta manera cada vez mirar
				// menos
				this.listUI.remove(nameSignal);
			}
		}

		// Si aun quedan elementos en el HashMAp
		if (!this.listUI.isEmpty()) {
			// Por cada temporizador
			for (int i = 0; i < getListTimers().size(); i++) {
				// Miro si el tempo esta en la lista de la IU y si es asi guardo
				// el tipo que selecciono el usuario
				String type = this.listUI.get(this.listTimers.get(i).getNameTimer());
				// A�ado el tipo de temporizador q he obtenido por la IU
				this.listTimers.get(i).addTypeTimer(type);
				listReturnVarGlobals.add(this.listTimers.get(i).getGlobalsVarTimer(this.program));
			}
			listReturnVarGlobals.add("\n\t(*---Contadores---*)\n\n");
			// Por cada Contador
			for (int j = 0; j < getListCounters().size(); j++) {
				// Miro si el cont esta en la lista de la IU y si es asi guardo
				// el tipo que selecciono el usuario
				String type = this.listUI.get(this.listCounters.get(j).getNameCounter());
				// A�ado el tipo de contador que ha seleccionado el usuario
				//this.listCounters.get(j).addTypeCounter(type);
				listReturnVarGlobals.add(this.listCounters.get(j).getGlobalsVarCounter(this.program, type));
			}
		}

		// Consigo las estapas
		for (Grafcet g : getListG()) {
			listReturnVarGlobals.addAll(g.getGrafcetVarGlobalStages(this.program));
		}

		listReturnVarGlobals.add("</globalVars></resource></configuration></configurations></instances></project>");

		return listReturnVarGlobals;
	}

	private void addListUI(Map<String, String> variablesMap) {
		this.listUI = variablesMap;

	}

	/** Devuelve una lista con la parte combinacional */
	private LinkedList<String> getProgramTSXMicroCP() {
		// TODO FALLOS primera letra en mayusculas y el resto en minusculas
		LinkedList<String> listCP = new LinkedList<String>();
		listCP.add("(*Programacion de la parte combinacional del sistema*)\n");

		// si hay comentarios con asignaciones en la transicion
		if (assignments != null) {
			/*
			 * SolModoAuto:=REMarcha.Q; FinProces:=X20;
			 */
			// Asignaciones que estan en la transition
			for (String assig : getAssignments().keySet()) {
				/*
				 * El PL7Pro solo permite variables que tenga la primera letra
				 * en mayusculas y el resto en minusculas
				 */
				String auxString = assignments.get(assig);

				// elimino las mayusculas del texto, solo las dejo en la primera
				// letra
				// input.substring(0, 1).toUpperCase() +
				// input.substring(1).toLowerCase();
				auxString = WordUtils.capitalize(auxString);

				auxString.replaceAll(" Not ", " NOT ");
				auxString.replaceAll(" And ", " AND ");
				auxString.replaceAll(" Or ", " OR ");
				auxString.replaceAll(" Re ", " RE ");
				auxString.replaceAll(" Fe ", " FE ");

				listCP.add("\n\t" + WordUtils.capitalize(assig) + ":=" + auxString + ";");
			}
		}

		for (String action : getActionStepMap().keySet()) {
			// Dejo solo la primera letra en mayusculas
			String aux = WordUtils.capitalize(action.trim());

			String auxString = WordUtils.capitalize(this.actionStepMap.get(action));

			auxString.replaceAll(" Not ", " NOT ");
			auxString.replaceAll(" And ", " AND ");
			auxString.replaceAll(" Or ", " OR ");
			auxString.replaceAll(" Re ", " RE ");
			auxString.replaceAll(" Fe ", " FE ");

			// temporizador
			Pattern patTemp = Pattern.compile("^Temp.*=[0-9]{1,}[a-z A-Z]{1,}");
			Matcher matTemp = patTemp.matcher(aux);

			// contador
			Pattern patCont = Pattern.compile("^Cont.*=[0-9]{1,}$|^Cont.*=Cont.*\\+[0-9]|^Cont.*=Cont.*\\-[0-9]");
			Matcher matCont = patCont.matcher(aux);

			// forzado de emergencia
			Pattern patEmer = Pattern.compile("^F/G.*");
			Matcher matEmer = patEmer.matcher(aux);

			// Para el getName de con o timer
			String name = "";

			// si no es temp, cont, o forzado de emergencia
			if (!matEmer.matches() && !matCont.matches() && !matTemp.matches()) {
				listCP.add("\n\t" + aux + ":=" + auxString + ";");

			} else if (matTemp.matches()) {

				aux = aux.replaceAll("=[0-9]{1,}[a-z A-Z]{1,}", "").trim();
				// Busco el indice del temporizador
				int index = equalsTimer(aux);
				// Obtengo el Temporizador
				Timer timer = getListTimers().get(index);
				name = timer.getNameTimer();
				String auxStepNameTimer = WordUtils.capitalize(timer.getStepNameTimer());

				auxString.replaceAll(" Not ", " NOT ");
				auxString.replaceAll(" And ", " AND ");
				auxString.replaceAll(" Or ", " OR ");
				auxString.replaceAll(" Re ", " RE ");

				listCP.add("\n\n(*Activaci�n y desactivaci�n del temporizador*)\n" + "\nIF(" + auxStepNameTimer
						+ ")THEN (*RE = flanco de subida*)" + "\n\tSTART " + name + ";" + "\nELSIF("
						+ auxStepNameTimer.replaceAll(" RE |RE ", " FE ") + ")THEN (*FE = flanco de bajada*)"
						+ "\n\tDOWN " + name + ";" + "\nEND_IF;\n");

			} else if (matCont.matches()) {
				// TODO PROGRAM MAIN si es contador aun no se q hacer
				// Busco el indice del contador
				// int index = equalsCount(aux);
				// Obtengo el contador
				// Counter count = getListCounters().get(index);
				// name = count.getNameCounter();
				// String auxStepNameCount =
				// WordUtils.capitalize(count.getStepCountes());
				// listCP.add("\n\n(*Contador*)\n" + "\nIF(" + auxStepNameCount
				// + ")THEN" + "\n\t " + count + ";"
				// + "\nELSIF(" + auxStepNameCount + ")THEN" + "\n\t " + count +
				// ";" + "\nEND_IF;\n");
			}

		}

		return listCP;
	}

	/** Devuelve una lista con la parte secuencial */
	private LinkedList<String> getProgramTSXMicroSP() {
		// TODO FALLOS primera letra en mayusculas y el resto en minusculas
		LinkedList<String> listSP = new LinkedList<String>();
		listSP.add("\n(*----La se�al de InicioGrafcets se crea para  poner los estados iniciales a 1 ------*)\n");
		// Obtengo el grafcet de emergencia para usar las listas y etapas

		String auxStepStartEmergency = "";
		String auxStepStopEmergency = "";

		for (Grafcet grafcet : listGrafcet) {
			String nameGrafcet = grafcet.getName().trim();
			nameGrafcet = nameGrafcet.substring(1);
			// en la lista de emergencia se guarda el nombre del grafcet sin la
			// G delante
			// si el grafcet se encuentra en la lista de emergencia de stop
			if (listEmergencyStart.contains(nameGrafcet)) {
				auxStepStartEmergency = " OR " + stepStartEmergency;
			}
			// si el grafcet se encuentra en la lista de emergencia de start
			if (listEmergencyStop.contains(nameGrafcet)) {
				auxStepStopEmergency = " OR " + stepStopEmergency;
			}

			listSP.addAll(grafcet.generateSequentialPart(auxStepStartEmergency, auxStepStopEmergency));
		}
		return listSP;
	}

	/**
	 * @return
	 */
	private LinkedList<String> generateProgramMain() {

		LinkedList<String> aux = new LinkedList<String>();
		LinkedList<String> listEmergency = new LinkedList<String>();

		String gName;
		// por cada grafcet del proyecto
		for (Grafcet grafcet : listGrafcet) {
			// guardo solo el nombre del grafcet q sera Gxxxxxxxxx
			gName = grafcet.getName();
			aux.add(gName);

			// en este hashMap voy guardando las acciones
			/*
			 * Map<String, String> auxMap = grafcet.getActionStepMap(); for
			 * (String action : auxMap.keySet()) { if (actionStepMap.get(action)
			 * == null) actionStepMap.put(action, auxMap.get(action)); else
			 * actionStepMap.put(action, actionStepMap.get(action) + " OR " +
			 * auxMap.get(action)); }
			 */
			// si el grafcet es el de emergencia relleno la lista de forzados
			/*
			 * TODO COMENTADO PARA PROBAR SI VALE CON LAS LISTA DE EMERGENCIA
			 * SOLO EN EL PROJECT EN ESTE CASO SE TIENE Q DAR POR HECHO Q HABRA
			 * SOLO UN GRAFCET DE ENERGENCIA EN EL PROYECTO if
			 * (grafcet.isEmergency()) { String stepStop =
			 * grafcet.getStepStopEmergency(); String stepStart =
			 * grafcet.getStepStartEmergency(); boolean bol = false; if
			 * (grafcet.compareStartAndStopLists()) { bol = true; } // Anado los
			 * forzados de cad agrafcet
			 * listEmergency.addAll(generateListEmergency(grafcet.
			 * getListEmergencyStart(), grafcet.getListEmergencyStop(), bol,
			 * stepStop, stepStart));
			 */
			// si el grafcet es el de emergencia relleno la lista de forzados
			if (grafcet.isEmergency()) {
				// String stepStop = getStepStopEmergency();
				// String stepStart = getStepStartEmergency();
				/*
				 * boolean bol = false; if (compareStartAndStopLists()) { bol =
				 * true; }
				 */
				// Anado los forzados de cad agrafcet
				listEmergency.addAll(
						generateListEmergency(/*
												 * getListEmergencyStart(),
												 * getListEmergencyStop(),
												 * bol,stepStop, stepStart)
												 */));

				/*
				 * ESTO ES SOLO PARA EL DE EMERGENCIA //Emergencia(Init:=(XInit
				 * OR IntitEmergencia) , Reset:=ResetEmergencia );
				 */
				String gEmergency = gName.substring(1, gName.length());
				listEmergency.add("\n\t" + gEmergency + "(Init:=(XInit OR Init" + gEmergency + "), Reset:=(Reset"
						+ gEmergency + "));\n");
			}

		}

		return getProgramMain(aux, listEmergency);

	}

	// compara las dos listas de emergencia
	private boolean compareStartAndStopLists() {
		Collections.sort(this.listEmergencyStop);
		Collections.sort(this.listEmergencyStart);
		return getListEmergencyStart().equals(getListEmergencyStop());
	}

	/** Genera las paradas e inicios de los forzados de emergencia */
	private LinkedList<String> generateListEmergency() {
		LinkedList<String> aux = new LinkedList<String>();
		String emerg = "";

		LinkedList<String> listStop = getListEmergencyStop();
		LinkedList<String> listStart = getListEmergencyStart();

		String stepStop = getStepStopEmergency();
		String stepStart = getStepStartEmergency();

		// Si las listas de stop y start son iguales
		if (compareStartAndStopLists()) {
			for (String e : listStop) {
				emerg = e.trim();
				aux.add("\n\tInit" + emerg.trim() + ":=" + stepStart + ";");
				aux.add("\n\tReset" + emerg + ":=" + stepStop + ";\n");
				aux.add("\n\t" + emerg + "(Init:=(XInit OR Init" + emerg + "), Reset:=(XReset OR Reset" + emerg
						+ "));\n");
			}
		} else {
			/* Si no son iguales comparo a ver cual es la mas grande */
			if (listStop.size() > listStart.size()) {
				for (String e : listStop) {
					// Por cada elemento d la lista miro si este se encuentra en
					// la otra lista
					if (listStart.contains(e)) {
						emerg = e.trim();
						aux.add("\n\tInit" + emerg + ":=" + stepStart + ";");
						aux.add("\n\tReset" + emerg + ":=" + stepStop + ";");
						aux.add("\n\t" + emerg + "(Init:=(XInit OR Init" + emerg + "), Reset:=(XReset OR Reset" + emerg
								+ "));\n");
					} else {
						emerg = e.trim();
						// aux.add("\n\tInit"+emerg+":="+pStepStart+";\n");
						aux.add("\n\tReset" + emerg + ":=" + stepStop + ";");
						aux.add("\n\t" + emerg + "(Init:=(XInit), Reset:=(XReset OR Reset" + emerg + "));\n");
					}
				}
			} else {
				for (String e : listStart) {
					// Por cada elemento d la lista miro si este se encuentra en
					// la otra lista
					if (listStop.contains(e)) {
						emerg = e.trim();
						aux.add("\n\tInit" + emerg + ":=" + stepStart + ";");
						aux.add("\n\tReset" + emerg + ":=" + stepStop + ";");
						aux.add("\n\t" + emerg + "(Init:=(XInit OR Init" + emerg + "), Reset:=(XReset OR Reset" + emerg
								+ "));\n");
					} else {
						emerg = e.trim();
						aux.add("\n\tInit" + emerg + ":=" + stepStart + ";");
						// aux.add("\tReset"+emerg+":="+pStepStop+";\n");
						aux.add("\n\t" + emerg + "(Init:=(XInit OR Init" + emerg + "), Reset:=(XReset));\n");
					}
				}
			}
		}

		return aux;
	}

	private LinkedList<String> generateListEmergencyMainPLCOpen(LinkedList<String> pListStop,
			LinkedList<String> pListStart, boolean pEquals, String pStepStop, String pStepStart) {
		LinkedList<String> aux = new LinkedList<String>();
		String emerg = "";
		// quiere decir que las listas de stop y start son iguales
		if (pEquals) {
			for (String e : pListStop) {
				emerg = e.trim();
				aux.add("<br />Init" + emerg.trim() + ":=" + pStepStart + ";");
				aux.add("<br />Reset" + emerg + ":=" + pStepStop + ";");
				aux.add("<br />" + emerg + "(Init:=(XInit OR Init" + emerg + "), Reset:=(XReset OR Reset" + emerg
						+ "));");
			}
		} else {
			/* Si no son iguales comparo a ver cual es la mas grande */
			if (pListStop.size() > pListStart.size()) {
				for (String e : pListStop) {
					// Por cada elemento d la lista miro si este se encuentra en
					// la otra lista
					if (pListStart.contains(e)) {
						emerg = e.trim();
						aux.add("<br />Init" + emerg + ":=" + pStepStart + ";");
						aux.add("<br />Reset" + emerg + ":=" + pStepStop + ";");
						aux.add("<br />" + emerg + "(Init:=(XInit OR Init" + emerg + "), Reset:=(XReset OR Reset"
								+ emerg + "));");
					} else {
						emerg = e.trim();
						// aux.add("\n\tInit"+emerg+":="+pStepStart+";\n");
						aux.add("<br />Reset" + emerg + ":=" + pStepStop + ";");
						aux.add("<br />" + emerg + "(Init:=(XInit), Reset:=(XReset OR Reset" + emerg + "));");
					}
				}
			} else {
				for (String e : pListStart) {
					// Por cada elemento d la lista miro si este se encuentra en
					// la otra lista
					if (pListStop.contains(e)) {
						emerg = e.trim();
						aux.add("<br />Init" + emerg + ":=" + pStepStart + ";");
						aux.add("<br />Reset" + emerg + ":=" + pStepStop + ";");
						aux.add("<br />" + emerg + "(Init:=(XInit OR Init" + emerg + "), Reset:=(XReset OR Reset"
								+ emerg + "));");
					} else {
						emerg = e.trim();
						aux.add("<br />Init" + emerg + ":=" + pStepStart + ";");
						// aux.add("\tReset"+emerg+":="+pStepStop+";\n");
						aux.add("<br />" + emerg + "(Init:=(XInit OR Init" + emerg + "), Reset:=(XReset));");
					}
				}
			}
		}

		return aux;
	}

	/**
	 * Este metodo se encargara de obtener la parte combinacional
	 * correspondientes a TwinCAT Pre: Post: devolvera una lista de String
	 */
	private LinkedList<String> getProgramMain(LinkedList<String> pNames, LinkedList<String> pEmergency) {

		LinkedList<String> listaProgramMain = new LinkedList<String>();

		listaProgramMain.add("PROGRAM MAIN\n\n\tVAR\n");

		for (String name : pNames) {
			/*
			 * Ejemplo: SecPrincipal : GSecPrincipal; InitSecPrincipal :BOOL;
			 * ResetSecPrincipal :BOOL;
			 */
			String n = name.substring(1, name.length());
			listaProgramMain.add("\n\t\t" + n + "\t:" + name + ";\n");
			listaProgramMain.add("\t\tInit" + n + "\t: BOOL;\n");
			listaProgramMain.add("\t\tReset" + n + "\t: BOOL;\n");
		}

		listaProgramMain.add("\n\t\tXInit\t:BOOL;\n\t\tXReset\t:BOOL;\n");

		/* Por cada temporizador y contador */
		for (Timer timer : this.listTimers) {
			listaProgramMain.add("\n\t\t" + timer.getNameTimer() + "\t:" + timer.getTypeTimer() + ";\n");
		}
		for (Counter count : this.listCounters) {
			listaProgramMain.add("\n\t\t" + count.getNameCounter() + "\t:" + count.getTypeCounter() + ";\n");
		}
		listaProgramMain.add("\n\tEND_VAR\n");

		listaProgramMain.add("\n\t(*---------------------------------------------*)\n");

		listaProgramMain.add("\n\tXInit:=INIT;\n\tXReset:=RESET;\n\n");

		for (String string : this.list_FE_and_RE) {
			String stringAux = string;
			listaProgramMain.add("\n\t" + stringAux + "(CLK:=" + string.substring(2, string.length()) + " , Q=> "
					+ stringAux + "Q );");
		}
		// si hay comentarios con asignaciones en la transicion
		if (assignments != null) {
			/*
			 * SolModoAuto:=REMarcha.Q; FinProces:=X20;
			 */
			// Asignaciones que estan en la transition
			for (String assig : getAssignments().keySet()) {
				String auxString = assignments.get(assig);
				// Si la palabra contiene un RE o FE
				Pattern patRE_FE = Pattern.compile(".*RE .*| .*FE .*");
				Matcher matRE_FE = patRE_FE.matcher(auxString);
				if (matRE_FE.matches()) {
					auxString = auxString.replace(" ", "");
					auxString = auxString + "Q";
				}

				listaProgramMain.add("\n\t" + assig.trim() + ":=" + auxString + ";");
			}
		}

		/* Anado la lista de emergencia y forzados */
		listaProgramMain.addAll(pEmergency);

		listaProgramMain.add("\n\n");

		// por cada elemento de la lista de acciones
		for (String action : actionStepMap.keySet()) {
			String aux = action.trim();
			// temporizador
			Pattern patTemp = Pattern.compile("^Temp.*=[0-9]{1,}[a-z A-Z]{1,}");
			Matcher matTemp = patTemp.matcher(aux);

			// contador
			Pattern patCont = Pattern.compile("^Cont.*=[0-9]{1,}$|^Cont.*=Cont.*\\+[0-9]|^Cont.*=Cont.*\\-[0-9]");
			Matcher matCont = patCont.matcher(aux);

			// forzado de emergencia
			Pattern patEmer = Pattern.compile("^F/G.*");
			Matcher matEmer = patEmer.matcher(aux);

			// si no es temp, cont, o forzado de emergencia
			if (!matEmer.matches() && !matCont.matches() && !matTemp.matches()) {
				listaProgramMain.add("\n\t" + aux + ":=" + actionStepMap.get(action) + ";");
			} else if (matTemp.matches()) {
				aux = aux.replaceAll("=[0-9]{1,}[a-z A-Z]{1,}", "").trim();
				int index = equalsTimer(aux);
				Timer timer = getListTimers().get(index);
				String nameTimer = timer.getNameTimer();
				listaProgramMain.add("\n\t" + nameTimer + "IN:=" + actionStepMap.get(action) + ";");
				listaProgramMain.add(timer.getProgramMainTimer());
				/*
				 * listaProgramMain .add("\n\t" + nameTimer + "PT:=T#" +
				 * timer.getTime() + timer.getTypeTime() + ";");
				 * listaProgramMain.add("\n\t" + nameTimer+ "(IN:=" + nameTimer
				 * + "IN , PT:=" + nameTimer + "PT , Q=>" + nameTimer +
				 * "Q , ET=> " + nameTimer + "ET);");
				 */
			} else if (matCont.matches()) {
				// TODO PROGRAM MAIN si es contador el el.PV para comparar
				// int index = equalsCount(aux);
				// Counter count = getListCounters().get(index);
				// String nameCount = count.getNameCounter();
				// listaProgramMain.add("\n\t" + nameCount + "IN:=" +
				// actionStepMap.get(action) + ";");
				// listaProgramMain.add(count.getProgramMainCounter());

			}

		}

		// listaProgramMain.add("\nEND_PROGRAM\n");

		return listaProgramMain;
	}

	/**
	 * Este metodo se encargara de obtener las variables globales
	 * correspondientes a TwinCAT Pre: Post: devolvera una lista de String
	 */
	private LinkedList<String> getGlobalVars(LinkedList<String> text) {
		LinkedList<String> t = new LinkedList<String>();
		t.add("VAR_GLOBAL\n");
		t.addAll(text);
		t.add("\nEND_VAR\n");
		return t;
	}

	/**
	 * Devuelve la lista que le pasan por parametro pero sin elementos repetidos
	 */
	private LinkedList<String> removeDuplicates(LinkedList<String> listDuplicate) {

		LinkedList<String> listwithoutduplicates = new LinkedList<String>();

		for (String string : listDuplicate) {
			String varString = string.trim();
			if (!listwithoutduplicates.contains(varString) && !varString.equals("")) {
				listwithoutduplicates.add(varString);
			}
		}

		return listwithoutduplicates;
	}

	/*
	 * public void generateEmergencyData() { for (Grafcet g : getListG()) { //
	 * si el grafcet es el de emergencia relleno la lista de emergencia if
	 * (g.isEmergency()) { g.getEmergency(); } } }
	 */

	private void generateSignalsProject() {
		LinkedList<String> signals = new LinkedList<String>();

		for (Grafcet g : getListG()) {
			signals.addAll(g.getListSignalsGrafcet());
		}
		signals = removeDuplicates(signals);

		this.signalsProject = signals;

	}

	private LinkedList<String> generateBodyMain(LinkedList<String> pEmergency) {
		LinkedList<String> bodyMain = new LinkedList<String>();
		bodyMain.add("<br />XInit:=INIT;<br />XReset:=RESET;\n\n");

		for (String string : this.list_FE_and_RE) {
			bodyMain.add("<br />" + string + "(CLK:=" + string.substring(2, string.length()) + " , Q=> );");
		}
		// si hay comentarios con asignaciones en la transicion
		if (assignments != null) {
			/*
			 * SolModoAuto:=REMarcha.Q; FinProces:=X20;
			 */
			// Asignaciones que estan en la transition
			for (String assig : getAssignments().keySet()) {
				String auxString = assignments.get(assig);

				// Si la palabra contiene un RE o FE
				Pattern patRE_FE = Pattern.compile(".* RE .*| .* FE .*");
				Matcher matRE_FE = patRE_FE.matcher(auxString);

				if (matRE_FE.matches()) {
					auxString = auxString.replace(" ", "");
					auxString = auxString + ".Q";
				}

				bodyMain.add("<br />" + assig.trim() + ":=" + auxString + ";");
			}
		}

		/* Anado la lista de emergencia y forzados */
		bodyMain.addAll(pEmergency);

		for (String action : actionStepMap.keySet()) {
			String aux = action.trim();
			// temporizador
			Pattern patTemp = Pattern.compile("^Temp.*=[0-9]{1,}[a-z A-Z]{1,}");
			Matcher matTemp = patTemp.matcher(aux);

			// contador
			Pattern patCont = Pattern.compile("^Cont.*=[0-9]{1,}$|^Cont.*=Cont.*\\+[0-9]|^Cont.*=Cont.*\\-[0-9]");
			Matcher matCont = patCont.matcher(aux);

			// forzado de emergencia
			Pattern patEmer = Pattern.compile("^F/G.*");
			Matcher matEmer = patEmer.matcher(aux);

			// si no es temp, cont, o forzado de emergencia
			if (!matEmer.matches() && !matCont.matches() && !matTemp.matches()) {
				bodyMain.add("<br />" + aux + ":=" + actionStepMap.get(action) + ";");
			} else if (matTemp.matches()) {
				aux = aux.replaceAll("=[0-9]{1,}[a-z A-Z]{1,}", "").trim();
				int index = equalsTimer(aux);
				Timer timer = getListTimers().get(index);
				String nameTimer = timer.getNameTimer();
				bodyMain.add("<br />" + nameTimer + "IN:=" + actionStepMap.get(action) + ";");
				bodyMain.add(timer.getProgramMainTimer());
				/*
				 * bodyMain .add("<br />" + nameTimer + "PT:=T#" +
				 * timer.getTime() + timer.getTypeTime() + ";"); bodyMain.add(
				 * "<br />" + nameTimer+ "(IN:=" + nameTimer + "IN , PT:=" +
				 * nameTimer + "PT , Q=>" + nameTimer + "Q , ET=> " + nameTimer
				 * + "ET);");
				 */
			} else if (matCont.matches()) {
				// TODO PROGRAM MAIN si es contador aun no se q hacer
				// int index = equalsCount(aux);
				// Counter count = getListCounters().get(index);
				// String nameCount = count.getNameCounter();
				// bodyMain.add("<br />" + nameCount + "IN:=" +
				// actionStepMap.get(action) + ";");
				// bodyMain.add(count.getProgramMainCounter());

			}

		}
		return bodyMain;
	}

	private LinkedList<String> generatePOUMain() {
		LinkedList<String> listReturnPousPLCOpen = new LinkedList<String>();
		LinkedList<String> localVars = new LinkedList<String>();
		LinkedList<String> externalVars = new LinkedList<String>();
		LinkedList<String> listEmergency = new LinkedList<String>();

		// TODO NECESITO AQUI LA FECHA DE LA SIGUIENTE MANERA
		// 2015-07-23T13:03:20
		String fech = "2015-07-23T13:03:20";

		// pou interface
		listReturnPousPLCOpen.add(
				"<pou name=\"" + this.language + "_Main\" pouType=\"program\" lastChange=\"" + fech + "\"><interface>");

		// localVars
		// Las variables init y reset q las pongo yo
		localVars.add("<localVars retain=\"false\">");
		// el xInit y XReset
		localVars.add("<variable name=\"XInit\" group=\"Default\"><type><BOOL /></type></variable>");
		localVars.add("<variable name=\"XReset\" group=\"Default\"><type><BOOL /></type></variable>");

		externalVars.add("<externalVars retain=\"false\">");
		// el Init y Reset q pongo yo
		externalVars.add("<variable name=\"INIT\" group=\"Default\"><type><BOOL /></type></variable>"
				+ "<variable name=\"RESET\" group=\"Default\"><type><BOOL /></type></variable>");

		// por cada grafcet ------------------------
		for (Grafcet grafcet : getListG()) {
			String gName = grafcet.getName();
			String name = gName.substring(1, gName.length());
			localVars.add("<variable name=\"" + name + "\" group=\"Default\"><type><derived name=\"" + gName
					+ "\" />></type></variable>" + "<variable name=\"Init" + name
					+ "\" group=\"Default\"><type><BOOL /></type></variable>" + "<variable name=\"Reset" + name
					+ "\" group=\"Default\"><type><BOOL /></type></variable>");

			// por cada grafcet para el body
			// NECESITO: Las etapas iniciales excepto la de emergencia y las
			// etapas que tiene una o mas acciones asociadas.
			// #########.addAll(grafcet.getGrafcetExternalVarsMain());
			// Si el grafcet es de emergencia relleno la lista de forzados para
			// el bodyMain
			if (grafcet.isEmergency()) {
				String stepStop = getStepStopEmergency();
				String stepStart = getStepStartEmergency();
				boolean bol = false;
				if (compareStartAndStopLists()) {
					bol = true;
				}
				// Anado los forzados de cad agrafcet
				listEmergency.addAll(generateListEmergencyMainPLCOpen(getListEmergencyStart(), getListEmergencyStop(),
						bol, stepStop, stepStart));

				/*
				 * //Emergencia(Init:=(XInit OR IntitEmergencia) ,
				 * Reset:=ResetEmergencia );
				 */
				String gEmergency = gName.substring(1, gName.length());
				listEmergency.add("<br />" + gEmergency + "(Init:=(XInit OR Init" + gEmergency + "), Reset:=(Reset"
						+ gEmergency + "));");
			}

		}

		// por cada temporizador
		for (Timer timer : getListTimers()) {
			String nameTimer = timer.getNameTimer();
			String typeTimer = timer.getTypeTimer();
			localVars.add("<variable name=\"" + nameTimer + "\" group=\"Default\"><type><derived name=\"" + typeTimer
					+ "\" />></type></variable>");
			externalVars.add(timer.getGlobalsVarTimer(this.program));
		}
		// por cada Contador
		for (Counter counter : getListCounters()) {
			String nameCount = counter.getNameCounter();
			String typeCount = counter.getTypeCounter();
			localVars.add("<variable name=\"" + nameCount + "\" group=\"Default\"><type><derived name=\"" + typeCount
					+ "\" />></type></variable>");
			// TODO HAY QUE MIRAR PORQ DA ERROR ESTO
			// externalVars.add(counter.getGlobalsVarCounter(typeCount));

		}
		// por cada RE y FE
		for (String string : getList_FE_and_RE()) {
			localVars.add("<variable name=\"" + string + "\" group=\"Default\"><type><derived name=\""
					+ string.charAt(0) + "_TRIG\" />></type></variable>");
		}

		localVars.add("</localVars>");

		// externalVars

		// por cada se�al del project
		for (String signal : getSignalsProject()) {
			externalVars.add("<variable name=\"" + signal + "\" group=\"Default\"><type><BOOL /></type></variable>");
		}

		// cierro interface
		externalVars.add("</externalVars></interface>");

		// antes d ecomenzar con el body a�ado las variables
		listReturnPousPLCOpen.addAll(localVars);
		listReturnPousPLCOpen.addAll(externalVars);

		// body

		// inicio body
		listReturnPousPLCOpen.add(
				"<body><ST><worksheet name=\"" + this.language + "_Main\"><html xmlns=\"http://www.w3.org/1999/xhtml\">"
						+ "<p xmlns=\"http://www.w3.org/1999/xhtml\" xml:space=\"preserve\">");

		listReturnPousPLCOpen.addAll(generateBodyMain(listEmergency));

		// fin body
		listReturnPousPLCOpen.add("</p></html></worksheet></ST></body></pou>");

		return listReturnPousPLCOpen;

	}

	private LinkedList<String> generatePousPLCOpen() {
		LinkedList<String> listReturnPousPLCOpen = new LinkedList<String>();

		// TODO NECESITO AQUI LA FECHA DE LA SIGUIENTE MANERA
		// 2015-07-23T13:03:20
		String fech = "2015-07-23T13:03:20";

		// Inicio documento
		listReturnPousPLCOpen.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<project xmlns:xhtml=\"http://www.w3.org/1999/xhtml\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://www.kw-software.com/xml/PLCopen/TC6_XML_V10_KW.xsd\">"
				+ "	<fileHeader companyName=\"TFG UPV\" companyURL=\"\" productName=\"translator\" productVersion=\"0.1\" productRelease=\"\" creationDateTime=\""
				+ fech + "\" contentDescription=\"\" />	" + "	<contentHeader name=\"" + this.name
				+ "\" version=\"\" modificationDateTime=\"\">" + "		<coordinateInfo>"
				+ "			<fbd><scaling x=\"2\" y=\"2\" /></fbd>" + "			<ld><scaling x=\"2\" y=\"2\" /></ld>"
				+ "			<sfc><scaling x=\"2\" y=\"2\" /></sfc>" + "		</coordinateInfo>" + "	</contentHeader>"
				+ "	<types><dataTypes /><pous>");

		// un pou por cada grafcet tiene interface, body y documentation(No es
		// obligatoria asi q la quito)

		for (Grafcet grafcet : getListG()) {

			String nameG = grafcet.getName();
			// TODO NECESITO AQUI LA FECHA DE LA SIGUIENTE MANERA
			// 2015-07-23T13:03:20

			// pou interface
			listReturnPousPLCOpen.add(
					"<pou name=\"" + nameG + "\" pouType=\"functionBlock\" lastChange=\"" + fech + "\"><interface>");

			// imputvars
			// Las variables init y reset q las pongo yo
			listReturnPousPLCOpen.add("<inputVars retain=\"false\">"
					+ "<variable name=\"Init\" group=\"Default\"><type><BOOL /></type></variable>"
					+ "<variable name=\"Reset\" group=\"Default\"><type><BOOL /></type></variable>" + "</inputVars>");

			// externalVars
			listReturnPousPLCOpen.add("<externalVars retain=\"false\">");
			// Consigo las estapas y se�ales del grafcet
			// mirar que salga el tempQ y asi
			listReturnPousPLCOpen.addAll(grafcet.getGrafcetExternalVars());

			// cierro interface
			listReturnPousPLCOpen.add("</externalVars></interface>");

			// ***** body *****
			// inicio body
			listReturnPousPLCOpen
					.add("<body><ST><worksheet name=\"" + nameG + "\"><html xmlns=\"http://www.w3.org/1999/xhtml\">"
							+ "<p xmlns=\"http://www.w3.org/1999/xhtml\" xml:space=\"preserve\">");

			// set y reset
			listReturnPousPLCOpen.add("(*<br /> ----------------------<br /> " + nameG
					+ ":  <br />---------------------------------*)<br />");

			listReturnPousPLCOpen.addAll(grafcet.generateBody());

			// fin body
			listReturnPousPLCOpen.add("</p></html></worksheet></ST></body></pou>");
		}

		// *********** aqui ahora el pou main
		listReturnPousPLCOpen.addAll(generatePOUMain());

		// Fin documento
		listReturnPousPLCOpen.add("</pous></types><instances><configurations /></instances></project>");

		return listReturnPousPLCOpen;
	}

	/*
	 * private LinkedList<String> partBodyPLCOpen() {
	 * 
	 * LinkedList<String> listProgramBody = new LinkedList<String>();
	 * 
	 * 
	 * XInit:=INIT; <br />XReset:=RESET; .. <br />Cizquierda:=X12 OR X23 OR (X31
	 * AND NOT F0); <br /><br />TempIN:=X22 OR X24; <br />TempPT:=T#2s; <br
	 * />Temp(IN:=TempIN, PT:=TempPT); <br />TempQ:=Temp.Q; <br
	 * />TempET:=Temp.ET; <br /><br />
	 * 
	 * 
	 * listProgramBody.add("<br />XInit:=INIT;<br />XReset:=RESET;");
	 * 
	 * for (String string : this.list_FE_and_RE) { listProgramBody.add("<br />"
	 * + string + "(CLK:=" + string.substring(2, string.length()) + " , Q=> );"
	 * ); }
	 * 
	 * SolModoAuto:=REMarcha.Q; FinProces:=X20;
	 * 
	 * // Asignaciones que estan en la transition for (String assig :
	 * getAssignments().keySet()) { String auxString = assignments.get(assig);
	 * 
	 * // Si la palabra contiene un RE o FE Pattern patRE_FE = Pattern.compile(
	 * ".* RE .*| .* FE .*"); Matcher matRE_FE = patRE_FE.matcher(auxString);
	 * 
	 * if (matRE_FE.matches()) { auxString = auxString.replace(" ", "");
	 * auxString = auxString + ".Q"; }
	 * 
	 * listProgramBody.add("<br />" + assig.trim() + ":=" + auxString + ";"); }
	 * 
	 * for (String action : actionStepMap.keySet()) { String aux =
	 * action.trim(); // temporizador Pattern patTemp = Pattern.compile(
	 * "^Temp.*=[0-9]{1,}[a-z A-Z]{1,}"); Matcher matTemp =
	 * patTemp.matcher(aux);
	 * 
	 * // contador Pattern patCont = Pattern.compile(
	 * "^Cont.*=[0-9]{1,}$|^Cont.*=Cont.*\\+[0-9]|^Cont.*=Cont.*\\-[0-9]");
	 * Matcher matCont = patCont.matcher(aux);
	 * 
	 * // forzado de emergencia Pattern patEmer = Pattern.compile("^F/G.*");
	 * Matcher matEmer = patEmer.matcher(aux);
	 * 
	 * // si no es temp, cont, o forzado de emergencia if (!matEmer.matches() &&
	 * !matCont.matches() && !matTemp.matches()) { listProgramBody.add("<br />"
	 * + aux + ":=" + actionStepMap.get(action) + ";"); } else if
	 * (matTemp.matches()) { aux = aux.replaceAll("=[0-9]{1,}[a-z A-Z]{1,}",
	 * "").trim(); int index = equalsTimer(aux); Timer timer =
	 * listTimers.get(index); listProgramBody.add(timer.getBodyPLCOpen()); }
	 * else if (matCont.matches()) { // TODO PLCOpensi es contador aun no se q
	 * hacer int index = equalsCount(aux); Counter count =
	 * listCounters.get(index); listProgramBody.add(count.getBodyPLCOpen());
	 * 
	 * } } return listProgramBody; }
	 */

	/** Genera las paradas e inicios de los forzados de emergencia *//*
																		 * private
																		 * LinkedList
																		 * <
																		 * String>
																		 * getlistEmergencyBody
																		 * (
																		 * LinkedList
																		 * <
																		 * String>
																		 * pListStop,
																		 * LinkedList
																		 * <
																		 * String>
																		 * pListStart,
																		 * boolean
																		 * pEquals)
																		 * {
																		 * LinkedList
																		 * <
																		 * String>
																		 * aux =
																		 * new
																		 * LinkedList
																		 * <
																		 * String
																		 * >();
																		 * String
																		 * emerg
																		 * = "";
																		 * String
																		 * stepStop
																		 * =
																		 * getStepStopEmergency
																		 * ();
																		 * String
																		 * stepStart
																		 * =
																		 * getStepStartEmergency
																		 * ();
																		 * //
																		 * quiere
																		 * decir
																		 * que
																		 * las
																		 * listas
																		 * de
																		 * stop
																		 * y
																		 * start
																		 * son
																		 * iguales
																		 * if
																		 * (pEquals)
																		 * { for
																		 * (String
																		 * e :
																		 * pListStop)
																		 * {
																		 * emerg
																		 * = e.
																		 * trim(
																		 * );
																		 * aux.
																		 * add(
																		 * "<br />Init"
																		 * +
																		 * emerg
																		 * .trim
																		 * () +
																		 * ":="
																		 * +
																		 * stepStart
																		 * +
																		 * ";");
																		 * aux.
																		 * add(
																		 * "<br />Reset"
																		 * +
																		 * emerg
																		 * +
																		 * ":="
																		 * +
																		 * stepStop
																		 * +
																		 * ";");
																		 * aux.
																		 * add(
																		 * "<br />"
																		 * +
																		 * emerg
																		 * +
																		 * "(Init:=(XInit OR Init"
																		 * +
																		 * emerg
																		 * +
																		 * "), Reset:=(XReset OR Reset"
																		 * +
																		 * emerg
																		 * +
																		 * "));"
																		 * ); }
																		 * }
																		 * else
																		 * { Si
																		 * no
																		 * son
																		 * iguales
																		 * comparo
																		 * a ver
																		 * cual
																		 * es la
																		 * mas
																		 * grande
																		 * if
																		 * (pListStop
																		 * .size
																		 * () >
																		 * pListStart
																		 * .size
																		 * ()) {
																		 * for
																		 * (String
																		 * e :
																		 * pListStop)
																		 * { //
																		 * Por
																		 * cada
																		 * elemento
																		 * d la
																		 * lista
																		 * miro
																		 * si
																		 * este
																		 * se
																		 * encuentra
																		 * en //
																		 * la
																		 * otra
																		 * lista
																		 * if
																		 * (pListStart
																		 * .
																		 * contains
																		 * (e))
																		 * {
																		 * emerg
																		 * = e.
																		 * trim(
																		 * );
																		 * aux.
																		 * add(
																		 * "<br />Init"
																		 * +
																		 * emerg
																		 * +
																		 * ":="
																		 * +
																		 * stepStart
																		 * +
																		 * ";");
																		 * aux.
																		 * add(
																		 * "<br />Reset"
																		 * +
																		 * emerg
																		 * +
																		 * ":="
																		 * +
																		 * stepStop
																		 * +
																		 * ";");
																		 * aux.
																		 * add(
																		 * "<br />"
																		 * +
																		 * emerg
																		 * +
																		 * "(Init:=(XInit OR Init"
																		 * +
																		 * emerg
																		 * +
																		 * "), Reset:=(XReset OR Reset"
																		 * +
																		 * emerg
																		 * +
																		 * "));"
																		 * ); }
																		 * else
																		 * {
																		 * emerg
																		 * = e.
																		 * trim(
																		 * ); //
																		 * aux.
																		 * add(
																		 * "\n\tInit"
																		 * +
																		 * emerg
																		 * +":="
																		 * +
																		 * pStepStart
																		 * +
																		 * ";\n"
																		 * );
																		 * aux.
																		 * add(
																		 * "<br />Reset"
																		 * +
																		 * emerg
																		 * +
																		 * ":="
																		 * +
																		 * stepStop
																		 * +
																		 * ";");
																		 * aux.
																		 * add(
																		 * "<br />"
																		 * +
																		 * emerg
																		 * +
																		 * "(Init:=(XInit), Reset:=(XReset OR Reset"
																		 * +
																		 * emerg
																		 * +
																		 * "));"
																		 * ); }
																		 * } }
																		 * else
																		 * { for
																		 * (String
																		 * e :
																		 * pListStart)
																		 * { //
																		 * Por
																		 * cada
																		 * elemento
																		 * d la
																		 * lista
																		 * miro
																		 * si
																		 * este
																		 * se
																		 * encuentra
																		 * en //
																		 * la
																		 * otra
																		 * lista
																		 * if
																		 * (pListStop
																		 * .
																		 * contains
																		 * (e))
																		 * {
																		 * emerg
																		 * = e.
																		 * trim(
																		 * );
																		 * aux.
																		 * add(
																		 * "<br />Init"
																		 * +
																		 * emerg
																		 * +
																		 * ":="
																		 * +
																		 * stepStart
																		 * +
																		 * ";");
																		 * aux.
																		 * add(
																		 * "<br />Reset"
																		 * +
																		 * emerg
																		 * +
																		 * ":="
																		 * +
																		 * stepStop
																		 * +
																		 * ";");
																		 * aux.
																		 * add(
																		 * "<br />"
																		 * +
																		 * emerg
																		 * +
																		 * "(Init:=(XInit OR Init"
																		 * +
																		 * emerg
																		 * +
																		 * "), Reset:=(XReset OR Reset"
																		 * +
																		 * emerg
																		 * +
																		 * "));"
																		 * ); }
																		 * else
																		 * {
																		 * emerg
																		 * = e.
																		 * trim(
																		 * );
																		 * aux.
																		 * add(
																		 * "<br />Init"
																		 * +
																		 * emerg
																		 * +
																		 * ":="
																		 * +
																		 * stepStart
																		 * +
																		 * ";");
																		 * //
																		 * aux.
																		 * add(
																		 * "\tReset"
																		 * +
																		 * emerg
																		 * +":="
																		 * +
																		 * pStepStop
																		 * +
																		 * ";\n"
																		 * );
																		 * aux.
																		 * add(
																		 * "<br />"
																		 * +
																		 * emerg
																		 * +
																		 * "(Init:=(XInit OR Init"
																		 * +
																		 * emerg
																		 * +
																		 * "), Reset:=(XReset));"
																		 * ); }
																		 * } } }
																		 * 
																		 * return
																		 * aux;
																		 * }
																		 */
	/*** Genera el body del pou Main ***/

}
