package com.hpaz.translator.grafcetelements;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.hpaz.translator.grafcetelements.constants.GrafcetTagsConstants;
import com.hpaz.translator.output.Output;

public class Project {
	
	//TODO Crear en project funciones de getVariables y setVariables
	
	/**Nombre del fichero que se ha importado, sin extension*/
	private String name;
	
	/** IL --> Lista de instrucciones.
		ST --> Texto estructurado.
		LD --> Diagrama de escalera.
		FBD --> Diagrama de bloques funcionales.
		SFC --> Grafico de funciones secuenciales.*/
	private String language;
	
	/** PL -> PLC TSX Micro (PL7Pro), T -> PLC Beckhoff (TwinCAT), PC -> PLCOpen */
	private String program;
	
	private LinkedList<Grafcet> listG;

	private static Project project = new Project();

	private Project() {
		this.name = null;
		this.language = null;
		this.program = null;
		this.listG = new LinkedList<Grafcet>();
	}

	public static Project getProject() {
		return project;
	}

	private String getName() {
		return this.name;
	}

	public void addName(String name) {
		if(this.name==null)
			this.name = name;
	}

	private String getLanguage() {
		return this.language;
	}

	public void addLanguage(String language) {
		if(this.language==null)
			this.language = language;
	}

	private String getProgram() {
		return this.program;
	}

	public void addProgram(String program) {
		if(this.program==null)
			this.program = program;
	}

	public LinkedList<Grafcet> getListG() {
		return listG;
	}

	public void addGrafcet(Grafcet g) {
		this.listG.add(g);
	}

	public LinkedList<String> globalVars() {
		LinkedList<String> vG = new LinkedList<String>();
			
		for (Grafcet g : listG) {
			vG.addAll(g.grafcetVarGlobalStages());
		}
		vG.add("\n\t(*---Señales---*)\n\n");
		for (Grafcet g : listG) {
			vG.addAll(g.grafcetVarGlobalSignals());
		}

		return removeDuplicates(vG);
	}

	public void printProject() {
		System.out.println("----- PROJECT ------");
		System.out.println("Nombre: " + getName());
		System.out.println("Lenguaje: " + getLanguage());
		System.out.println("Compatibilidad: " + getProgram());
		for (Grafcet g : listG) {
			g.printGrafcet();
		}
		
	}

	/** Este metodo devuelve */
	public void print() {
		generateEmergencyData();
		if (program.equalsIgnoreCase(GrafcetTagsConstants.PROGRAM_OPT1)) { // Twincat		
			try {
				
				// Program Main
				Output.getOutput().exportFile(generarProgramMain(), getName()+"_PROGRAM_MAIN");
				
				//Var Global
				Output.getOutput().exportFile(getGlobalVar(globalVars()), getName()+"_VAR_GLOBAL");
				
				//Function Block --> uno por cada grafcet
				for (Grafcet g : listG) {
					Output.getOutput().exportFile(g.generateFunctionBlock(),"FUNCTION_BLOCK_"+g.getName());
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
	private LinkedList<String> generarProgramMain() {
		
		LinkedList<String> aux = new LinkedList<String>();
		LinkedList<String> listEmergency = new LinkedList<String>();
		
		Map<String, String> actionStepMap = new HashMap<String, String>();
		String gName;
		
		for (Grafcet grafcet : listG) {
			//guardo solo el nombre del grafcet q sera Gxxxxxxxxx
			gName=grafcet.getName();
			aux.add(gName);
					
			//en este hashMap voy guardando las acciones
			Map<String, String> auxMap = grafcet.getActionStepMap();
			for (String action : auxMap.keySet()) {
				if (actionStepMap.get(action) == null)
					actionStepMap.put(action, auxMap.get(action));
				else
					actionStepMap.put(action, actionStepMap.get(action)
							+ " OR " + auxMap.get(action));
			}
		
			//si el grafcet es el de emergencia relleno la lista de emergencia
			if(grafcet.isEmergency()){
				String stepStop = grafcet.getStepStopEmergency();
				String stepStart =grafcet.getStepStartEmergency();
				boolean bol = false;
				if(grafcet.compareStartAndStopLists()){
					bol=true;
				}
				listEmergency.addAll(generateListEmergency(grafcet.getListEmergencyStart(),grafcet.getListEmergencyStop(),bol,stepStop,stepStart));
				
				/*//Emergencia(Init:=(XInit OR IntitEmergencia) , Reset:=ResetEmergencia );*/
				String gEmergency=gName.substring(1, gName.length());
				listEmergency.add("\n\t"+gEmergency+"(Init:=(XInit OR Init"+gEmergency+"), Reset:=(Reset"+gEmergency+"));\n");
			}
		
		}
		
		return getProgramMain(aux,listEmergency, actionStepMap);
				
	}

	private LinkedList<String> generateListEmergency(LinkedList<String> pListStop, LinkedList<String> pListStart, boolean pEquals, String pStepStop, String pStepStart) {
		LinkedList<String> aux = new LinkedList<String>();
		String emerg ="";
			//quiere decir que las listas de stop y start son iguales
			if(pEquals){
				for (String e : pListStop) {
					emerg=e.trim();
					aux.add("\n\tInit"+emerg.trim()+":="+pStepStart+";\n");
					aux.add("\tReset"+emerg+":="+pStepStop+";\n");
					aux.add("\t"+emerg+"(Init:=(XInit OR Init"+emerg+"), Reset:=(XReset OR Reset"+emerg+"));\n");
				}
			}else{
				/*Si no son iguales comparo a ver cual es la mas grande*/
				if(pListStop.size()>pListStart.size()){
					for (String e : pListStop) {
						//Por cada elemento d la lista miro si este se encuentra en la otra lista
						if(pListStart.contains(e)){
							emerg=e.trim();
							aux.add("\n\tInit"+emerg+":="+pStepStart+";\n");
							aux.add("\tReset"+emerg+":="+pStepStop+";\n");
							aux.add("\t"+emerg+"(Init:=(XInit OR Init"+emerg+"), Reset:=(XReset OR Reset"+emerg+"));\n");
						}else{
							emerg=e.trim();
							//aux.add("\n\tInit"+emerg+":="+pStepStart+";\n");
							aux.add("\tReset"+emerg+":="+pStepStop+";\n");
							aux.add("\t"+emerg+"(Init:=(XInit), Reset:=(XReset OR Reset"+emerg+"));\n");
						}
					}
				}else{
					for (String e : pListStart) {
						//Por cada elemento d la lista miro si este se encuentra en la otra lista
						if(pListStop.contains(e)){
							emerg=e.trim();
							aux.add("\n\tInit"+emerg+":="+pStepStart+";\n");
							aux.add("\tReset"+emerg+":="+pStepStop+";\n");
							aux.add("\t"+emerg+"(Init:=(XInit OR Init"+emerg+"), Reset:=(XReset OR Reset"+emerg+"));\n");
						}else{
							emerg=e.trim();
							aux.add("\n\tInit"+emerg+":="+pStepStart+";\n");
							//aux.add("\tReset"+emerg+":="+pStepStop+";\n");
							aux.add("\t"+emerg+"(Init:=(XInit OR Init"+emerg+"), Reset:=(XReset));\n");
						}
					}
				}
			}
	
		return aux;
	}

	/**Este metodo se encargara de obtener la parte combinacional correspondientes a TwinCAT
	 * Pre:
	 * Post: devolvera una lista de String*/
	private LinkedList<String> getProgramMain(LinkedList<String> pNames,LinkedList<String> pEmergency, Map<String, String> pInit){
	
		LinkedList<String> listaProgramMain = new LinkedList<String>();
		
		listaProgramMain.add("PROGRAM MAIN\n\n\tVAR\n");
		
		for (String name : pNames) {
			/*Ejemplo:
			 * 	SecPrincipal		: GSecPrincipal;
				InitSecPrincipal	:BOOL;
				ResetSecPrincipal	:BOOL;
			 * */
			String n = name.substring(1, name.length());
			listaProgramMain.add("\n\t\t"+n+"\t:"+name+";\n");
			listaProgramMain.add("\t\tInit"+n+"\t: BOOL;\n");
			listaProgramMain.add("\t\tReset"+n+"\t: BOOL;\n");
		}	
		
		listaProgramMain.add("\n\t\tXInit\t:BOOL;\n\t\tXReset\t:BOOL;\n\n\tEND_VAR\n");
		listaProgramMain.add("\n\t(*---------------------------------------------*)\n");
		listaProgramMain.add("\n\tXInit:=INIT;\n\tXReset:=RESET;\n\n");
		
		
		/*Aqui deberian estar las seÃ±ales de entrada y salida q se preguntaran al usuario*/
		
		
		/*Anado la lista de emergencia*/
		listaProgramMain.addAll(pEmergency);

		
		
		listaProgramMain.add("\n\n");
		for (String action : pInit.keySet()) {
			listaProgramMain.add("\t"+action+":="+pInit.get(action)+";\n");
		}
		
		listaProgramMain.add("\nEND_PROGRAM\n");
		
		return listaProgramMain;
	}
	
	
	/**Este metodo se encargara de obtener las variables globales correspondientes a TwinCAT
	 * Pre:
	 * Post: devolvera una lista de String*/
	private LinkedList<String> getGlobalVar(LinkedList<String> text){
		LinkedList<String> t = new LinkedList<String>();
		t.add("VAR_GLOBAL\n");
		t.addAll(text);
		t.add("\nEND_VAR\n");
		return t;
	}
	
	/**Devuelve la lista que le pasan por parametro pero sin elementos repetidos*/
	private LinkedList<String> removeDuplicates(LinkedList<String> listDuplicate){
		
		LinkedList<String>  listwithoutduplicates = new LinkedList<String>();
		LinkedList<String> aux = listDuplicate;
		
		for (String s : aux) {
           if(!listwithoutduplicates.contains(s)){
               	  listwithoutduplicates.add(s);
           }
		}
			
	    return listwithoutduplicates;
	}
	
	public void generateEmergencyData() {
		for (Grafcet g : getListG() ) {
			//si el grafcet es el de emergencia relleno la lista de emergencia
			if(g.isEmergency()){
				g.getEmergency();
			}
		}
	}

	public LinkedList<String> generateSignals() {
		LinkedList<String> signals = new LinkedList<String>();
		//TODO faltan las señales de los comentarios
		for (Grafcet g : getListG() ) {
			//si el grafcet es el de emergencia relleno la lista de emergencia
			signals.addAll(g.getListSignalsGrafcet());
		}
		signals = removeDuplicates(signals);
		//TODO Eliminar este for q imprime por consola
		for (String string : signals) {
			System.out.println(string);		
		}
		return signals;
		
	}
	
}
