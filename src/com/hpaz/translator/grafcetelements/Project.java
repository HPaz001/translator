package com.hpaz.translator.grafcetelements;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hpaz.translator.grafcetelements.constants.GrafcetTagsConstants;
import com.hpaz.translator.output.Output;

public class Project {

	/** Nombre del fichero que se ha importado, sin extension */
	private String name;

	/**
	 * IL --> Lista de instrucciones. ST --> Texto estructurado. LD --> Diagrama
	 * de escalera. FBD --> Diagrama de bloques funcionales. SFC --> Grafico de
	 * funciones secuenciales.
	 * 
	 * LADDER DIAGRAM (LD) o lenguaje (diagrama) de contactos; FUNCTION BLOCK
	 * DIAGRAM (FBD) o esquema de bloques funcionales; INSTRUCTION LIST (IL) o
	 * lista de instrucciones; STRUCTURED TEXT (ST) o lenguaje textual
	 * estructurado; SEQUENTIAL FUNCTION CHART (SFC) o diagrama funcional de
	 * secuencias (basado en el GRAFCET).
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
	//PAra guardar las variables con flancos de subida o bajada
	private LinkedList<String> list_FE_and_RE;

	private Map<String, String> listUI;

	LinkedList<String> signalsProject;
	
	private static Project project = new Project();

	private Project() {
		this.name = null;
		this.language = null;
		this.program = null;
		this.listGrafcet = new LinkedList<Grafcet>();
		this.listTimers = new LinkedList<Timer>();
		this.listCounters = new LinkedList<Counter>();
		this.listCountersUI = new LinkedList<String>();
		this.listTimersUI = new LinkedList<String>();
		this.listUI = null;
		this.signalsProject = new LinkedList<String>();
		this.list_FE_and_RE =new LinkedList<String>();
	}

	public static Project getProject() {
		return project;
	}

	public void setOutputPath(String outputDir) {
		this.outputDir = outputDir;
	}

	private String getName() {
		return this.name;
	}

	public void addName(String name) {
		if (this.name == null)
			this.name = name;
	}

	private String getLanguage() {
		return this.language;
	}

	public void addLanguage(String language) {
		if (this.language == null)
			this.language = language;
	}

	private String getProgram() {
		return this.program;
	}

	public void addProgram(String program) {
		if (this.program == null)
			this.program = program;
	}

	public LinkedList<Grafcet> getListG() {
		return listGrafcet;
	}

	/**
	 * Anado el grafcet, pero antes genero las listas de previousAnNext y
	 * losSetAnReset
	 */
	public void addGrafcet(Grafcet g) {
		// g.fillPreviousNextListsSequences();
		// g.generateSetAndReset();
		g.fillPreviousAndNextSequencesLists();
		g.addSetAndResetToStep();
		this.listGrafcet.add(g);
	}

	public LinkedList<Timer> getListTimers() {
		return listTimers;
	}

	public void addTimer(Timer pTimer) {
		this.listTimersUI.add(pTimer.getNameTimer());
		this.listTimers.add(pTimer);
	}

	public LinkedList<Counter> getListCounters() {
		return listCounters;
	}

	public void addCounter(Counter pCounters) {
		this.listCountersUI.add(pCounters.getNameCounter());
		this.listCounters.add(pCounters);
	}
	public LinkedList<String> getListTimersUI() {
		return listTimersUI;
	}

	public LinkedList<String> getListCountersUI() {
		return listCountersUI;
	}

	public LinkedList<String> generateGlobalVars() {
		LinkedList<String> vG = new LinkedList<String>();

		for (Grafcet g : listGrafcet) {
			vG.addAll(g.getGrafcetVarGlobalStages());
		}
		vG.add("\n\t(*---Se�ales---*)\n\n");
		//TODO AQUI LAS SE�ALES
		/*for (Grafcet g : listGrafcet) {
			vG.addAll(g.grafcetVarGlobalSignals());
		}*/

		vG.add("\tINIT\t:BOOL;\n\tRESET\t:BOOL;\n");
		
		for (String string : this.list_FE_and_RE) {
			vG.add("\t"+string+"\t:"+string.charAt(0)+"_TRIG;\n");
		}
		
		
		//por cada una de las se�ales del proyecto
		for (String string : this.signalsProject) {
			//Si esta en el hashMap t la UI 
			if(this.listUI.containsKey(string)){
				//la uso
				String type = this.listUI.get(string);
				
				Pattern pat = Pattern.compile(".*:$");
				Matcher mat = pat.matcher(type);
				
				//Si el tipo indica que es una se�al
				if (!mat.matches()) {
					String [] typeDiv = type.split(":");
					String typeData=typeDiv[0];
					String typeVar=typeDiv[1];
					//ya tengo el tipo de dato y de variable, ahora por cada una de ellas se excribe distinto
					if(typeData.equals("Entrada")){
						typeData = "I";
					}else if(typeData.equals("Salida")){
						typeData = "Q";
					}else if(typeData.equals("Memoria")){
						typeData = "M";
					}else if(typeData.equals("Constante")){
						typeData = "C";
					}else if(typeData.equals("Sistema")){
						typeData = "S";
					}
					
					vG.add("\t" + string + " AT %"+ typeData +"* : "+typeVar+";\n");
				}
				//la eimino del HashMap
				this.listUI.remove(string);
			}
		}
		vG.add("\n\t(*---Temporizadores---*)\n\n");
		//Si aun quedan elementos en el HashMAp
		if(!this.listUI.isEmpty()){
			//Por cada temporizador
			for (int i = 0; i < this.listTimers.size(); i++) {
				String type = this.listUI.get(this.listTimers.get(i).getNameTimer());
				this.listTimers.get(i).addTypeTimer(type);
				vG.add(this.listTimers.get(i).getGlobalsVarTimer());
			}
			vG.add("\n\t(*---Contadores---*)\n\n");
			//Por cada Contador
			for (int j = 0; j < this.listCounters.size(); j++) {
				System.out.println("Contador");
				String type = this.listUI.get(this.listCounters.get(j).getNameCounter());
				this.listCounters.get(j).addTypeCounter(type);
				vG.add(this.listCounters.get(j).getGlobalsVarCounter());
			}	
		
		}
		
		//TODO mirar si es necesario este removeDuplicates
		return removeDuplicates(vG);
	}
	
	public void setProjectVariablesFromUserInterface(Map<String,String> variablesMap){
		//TODO pasar el mapa de variables a donde sea
		this.listUI = variablesMap;
		for (String key : variablesMap.keySet()){
			System.out.println("key -> " + key + ", value -> " + variablesMap.get(key));
		}
		
	}

	public void printProject() {
		System.out.println("----- PROJECT ------");
		System.out.println("Nombre: " + getName());
		System.out.println("Lenguaje: " + getLanguage());
		System.out.println("Compatibilidad: " + getProgram());
		for (Grafcet g : listGrafcet) {
			g.printGrafcet();
		}

	}

	/** Este metodo devuelve */
	public void print() {
		generateEmergencyData();
		if (program.equalsIgnoreCase(GrafcetTagsConstants.PROGRAM_OPT1)) { // Twincat
			try {

				// printProject();
				// Program Main
				Output.getOutput().exportFile(generateProgramMain(), getName() + "_PROGRAM_MAIN", outputDir);

				// Var Global
				Output.getOutput().exportFile(getGlobalVars(generateGlobalVars()), getName() + "_VAR_GLOBAL",
						outputDir);

				// Function Block --> uno por cada grafcet
				for (Grafcet g : listGrafcet) {
					Output.getOutput().exportFile(g.generateFunctionBlock(), "FUNCTION_BLOCK_" + g.getName(),
							outputDir);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if (program.equalsIgnoreCase(GrafcetTagsConstants.PROGRAM_OPT2)) {// PL7PRO

		} else if (program.equalsIgnoreCase(GrafcetTagsConstants.PROGRAM_OPT3)) {// PCWorx

		}
	}

	/**
	 * @return
	 */
	private LinkedList<String> generateProgramMain() {

		LinkedList<String> aux = new LinkedList<String>();
		LinkedList<String> listEmergency = new LinkedList<String>();

		Map<String, String> actionStepMap = new HashMap<String, String>();
		String gName;

		for (Grafcet grafcet : listGrafcet) {
			// guardo solo el nombre del grafcet q sera Gxxxxxxxxx
			gName = grafcet.getName();
			aux.add(gName);

			// en este hashMap voy guardando las acciones
			Map<String, String> auxMap = grafcet.getActionStepMap();
			for (String action : auxMap.keySet()) {
				if (actionStepMap.get(action) == null)
					actionStepMap.put(action, auxMap.get(action));
				else
					actionStepMap.put(action, actionStepMap.get(action) + " OR " + auxMap.get(action));
			}

			// si el grafcet es el de emergencia relleno la lista de emergencia
			if (grafcet.isEmergency()) {
				String stepStop = grafcet.getStepStopEmergency();
				String stepStart = grafcet.getStepStartEmergency();
				boolean bol = false;
				if (grafcet.compareStartAndStopLists()) {
					bol = true;
				}
				listEmergency.addAll(generateListEmergency(grafcet.getListEmergencyStart(),
						grafcet.getListEmergencyStop(), bol, stepStop, stepStart));

				/*
				 * //Emergencia(Init:=(XInit OR IntitEmergencia) ,
				 * Reset:=ResetEmergencia );
				 */
				String gEmergency = gName.substring(1, gName.length());
				listEmergency.add("\n\t" + gEmergency + "(Init:=(XInit OR Init" + gEmergency + "), Reset:=(Reset"
						+ gEmergency + "));\n");
			}

		}

		return getProgramMain(aux, listEmergency, actionStepMap);

	}

	private LinkedList<String> generateListEmergency(LinkedList<String> pListStop, LinkedList<String> pListStart,
			boolean pEquals, String pStepStop, String pStepStart) {
		LinkedList<String> aux = new LinkedList<String>();
		String emerg = "";
		// quiere decir que las listas de stop y start son iguales
		if (pEquals) {
			for (String e : pListStop) {
				emerg = e.trim();
				aux.add("\n\tInit" + emerg.trim() + ":=" + pStepStart + ";\n");
				aux.add("\tReset" + emerg + ":=" + pStepStop + ";\n");
				aux.add("\t" + emerg + "(Init:=(XInit OR Init" + emerg + "), Reset:=(XReset OR Reset" + emerg
						+ "));\n");
			}
		} else {
			/* Si no son iguales comparo a ver cual es la mas grande */
			if (pListStop.size() > pListStart.size()) {
				for (String e : pListStop) {
					// Por cada elemento d la lista miro si este se encuentra en
					// la otra lista
					if (pListStart.contains(e)) {
						emerg = e.trim();
						aux.add("\n\tInit" + emerg + ":=" + pStepStart + ";\n");
						aux.add("\tReset" + emerg + ":=" + pStepStop + ";\n");
						aux.add("\t" + emerg + "(Init:=(XInit OR Init" + emerg + "), Reset:=(XReset OR Reset" + emerg
								+ "));\n");
					} else {
						emerg = e.trim();
						// aux.add("\n\tInit"+emerg+":="+pStepStart+";\n");
						aux.add("\tReset" + emerg + ":=" + pStepStop + ";\n");
						aux.add("\t" + emerg + "(Init:=(XInit), Reset:=(XReset OR Reset" + emerg + "));\n");
					}
				}
			} else {
				for (String e : pListStart) {
					// Por cada elemento d la lista miro si este se encuentra en
					// la otra lista
					if (pListStop.contains(e)) {
						emerg = e.trim();
						aux.add("\n\tInit" + emerg + ":=" + pStepStart + ";\n");
						aux.add("\tReset" + emerg + ":=" + pStepStop + ";\n");
						aux.add("\t" + emerg + "(Init:=(XInit OR Init" + emerg + "), Reset:=(XReset OR Reset" + emerg
								+ "));\n");
					} else {
						emerg = e.trim();
						aux.add("\n\tInit" + emerg + ":=" + pStepStart + ";\n");
						// aux.add("\tReset"+emerg+":="+pStepStop+";\n");
						aux.add("\t" + emerg + "(Init:=(XInit OR Init" + emerg + "), Reset:=(XReset));\n");
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
	private LinkedList<String> getProgramMain(LinkedList<String> pNames, LinkedList<String> pEmergency,
			Map<String, String> pInit) {

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

		listaProgramMain.add("\n\t\tXInit\t:BOOL;\n\t\tXReset\t:BOOL;\n\n\tEND_VAR\n");
		listaProgramMain.add("\n\t(*---------------------------------------------*)\n");
		listaProgramMain.add("\n\tXInit:=INIT;\n\tXReset:=RESET;\n\n");

		/*
		 * Aqui deberian estar las señales de entrada y salida q se preguntaran
		 * al usuario
		 */

		/* Anado la lista de emergencia */
		listaProgramMain.addAll(pEmergency);

		listaProgramMain.add("\n\n");
		for (String action : pInit.keySet()) {
			listaProgramMain.add("\t" + action + ":=" + pInit.get(action) + ";\n");
		}

		listaProgramMain.add("\nEND_PROGRAM\n");

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
			if (!listwithoutduplicates.contains(string)) {
				listwithoutduplicates.add(string);
			}
		}

		return listwithoutduplicates;
	}

	public void generateEmergencyData() {
		for (Grafcet g : getListG()) {
			// si el grafcet es el de emergencia relleno la lista de emergencia
			if (g.isEmergency()) {
				g.getEmergency();
			}
		}
	}
	
	
	private void generateSignalsProject() {
		LinkedList<String> signals = new LinkedList<String>();
		
		for (Grafcet g : getListG()) {
			// si el grafcet es el de emergencia relleno la lista de emergencia
			signals.addAll(g.getListSignalsGrafcet());
		}
		//signals.addAll(this.listGrafcet.get(0).getListSignalsGrafcet());
		signals = removeDuplicates(signals);
		
		this.signalsProject = signals;
	

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
		while (indexCounter == (-1) && indexAux < listTimers.size()) {
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






}
