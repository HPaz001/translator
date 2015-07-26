package com.hpaz.translator.grafcetelements;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import com.hpaz.translator.grafcetelements.constants.GrafcetTagsConstants;
import com.hpaz.translator.output.Output;

public class Project {
	
	/**Nombre del fichero que se ha importado, sin extension*/
	private String name;
	
	/** IL --> Lista de instrucciones.
		ST --> Texto estructurado.
		LD --> Diagrama de escalera.
		FBD --> Diagrama de bloques funcionales.
		SFC --> Grafico de funciones secuenciales.*/
	private String language;
	
	/** PL-->PL7PRO TSX, T--> TwinCat Beckch, PC--> PCWorx PLCOpen */
	private String program;
	
	private LinkedList<Grafcet> listG;
	
	/**Etepa de forzar parada de emergencia*/
	private String stop;
	
	/**etapa de forzar inicio despues de emergencia*/
	private String init;
	
	/**Lista de grafcets que se fuerzan en la emergencia*/
	private LinkedList<String> listEmergency;
		

	private static Project project = new Project();

	private Project() {
		this.name = null;
		this.language = null;
		this.program = null;
		this.listG = new LinkedList<Grafcet>();
		setStop(null);
		setInit(null);
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

	public void addGragcet(Grafcet g) {
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

		return vG;
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

	private LinkedList<String> generarProgramMain() {
		//LinkedList<String> listaProgramMain = new LinkedList<String>();
		LinkedList<String> aux = new LinkedList<String>();

		Map<String, String> actionStepMap = new HashMap<String, String>();

		
		for (Grafcet grafcet : listG) {
			//guardo solo el nombre del grafcet q sera Gxxxxxxxxx
			aux.add(grafcet.getName());
			
			
			//en este hashMap voy guardando las acciones
			Map<String, String> auxMap = grafcet.getActionStepMap();
			for (String action : auxMap.keySet()) {
				if (actionStepMap.get(action) == null)
					actionStepMap.put(action, auxMap.get(action));
				else
					actionStepMap.put(action, actionStepMap.get(action)
							+ " OR " + auxMap.get(action));
			}
		}
		
		//llamada al postprocess
		return getProgramMain(aux, listEmergency, actionStepMap);
				
	}

	public String getStop() {
		return stop;
	}

	public void setStop(String stop) {
		this.stop = stop;
	}

	public String getInit() {
		return init;
	}

	public void setInit(String init) {
		this.init = init;
	}

	public LinkedList<String> getListEmergency() {
		return listEmergency;
	}

	public void addListEmergency(LinkedList<String> pListEmergency) {
		this.listEmergency=pListEmergency;
	}

	/**Este metodo se encargara de obtener la parte combinacional correspondientes a TwinCAT
	 * Pre:
	 * Post: devolvera una lista de String*/
	private LinkedList<String> getProgramMain(LinkedList<String> pNames, LinkedList<String> pEmergency, Map<String, String> pInit){
		//PROGRAMAR 
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
		
		
		/*Aqui deberian estar las señales de entrada y salida q se preguntaran al usuario*/
		
		
		/*Ejemplo
		 * 	InitMovil:=X35;
			ResetMovil:=X33;
			Movil(Init:=(XInit OR InitMovil), Reset:=(XReset OR ResetMovil));
		*/
		for (String emerg : pEmergency) {
			listaProgramMain.add("\n\tInit"+emerg+":="+Project.getProject().getInit()+";\n");
			listaProgramMain.add("\tReset"+emerg+":="+Project.getProject().getStop()+";\n");
			listaProgramMain.add("\t"+emerg+"(Init:=(XInit OR Init"+emerg+"), Reset:=(XReset OR Reset"+emerg+"));\n");
		}
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
		t.addAll(removeDuplicates(text));
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
}
