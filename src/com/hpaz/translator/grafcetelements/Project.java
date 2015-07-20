package com.hpaz.translator.grafcetelements;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.hpaz.translator.grafcetelements.constants.GrafcetTagsConstants;
import com.hpaz.translator.output.Output;
import com.hpaz.translator.output.PostProcess;

public class Project {
	private String name;
	private String language;
	private String program;
	/** PL7PRO TSX, TwinCat Beckch, PCWorx PLCOpen */
	private LinkedList<Grafcet> listG;
	
	//Etapas de la emergencia
	private String stop;
	private String init;
	private LinkedList<String> listEmergency;
		

	private static Project project = new Project();

	private Project() {
		this.name = "";
		this.language = "";
		this.program = "";
		this.listG = new LinkedList<Grafcet>();
		setStop(null);
		setInit(null);
	}

	public static Project getProject() {
		return project;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getProgram() {
		return program;
	}

	public void setProgram(String program) {
		this.program = program;
	}

	public LinkedList<Grafcet> getListG() {
		return listG;
	}

	public void addGragcet(Grafcet g) {
		this.listG.add(g);
	}

	public LinkedList<String> printVarGlobal() {
		LinkedList<String> vG = new LinkedList<String>();

		for (Grafcet g : listG) {
			vG.addAll(g.printGrafcetVarGlobalStages());
		}
		vG.add("\n\t(*---Señales---*)\n\n");
		for (Grafcet g : listG) {
			vG.addAll(g.printGrafcetVarGlobalSignals());
		}

		return vG;
	}

	public void print(String pCompatibility) {
		if (pCompatibility.equals("T")) {
			try {
				Output.getOutput().exportarFichero(
						PostProcess.getPostProcess().getGlobalVar(
								printVarGlobal()), this.name);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (pCompatibility.equals("PL")) {

		} else if (pCompatibility.equals("PC")) {

		}

	}

	public void printProject() {
		System.out.println("----- PROJECT ------");
		System.out.println("Nombre: " + this.name);
		System.out.println("Lenguaje: " + this.language);
		System.out.println("Compatibilidad: " + this.program);
		for (Grafcet g : listG) {
			g.printGrafcet();
		}
		
	}

	/** Este metodo devuelve */
	public void print() {
		LinkedList<String> aux = new LinkedList<String>();
		if (program.equals(GrafcetTagsConstants.PROGRAM_OPT1)) { // Twincat
			// Program Main
			generarListaProgramMain();
			
		} else if (program.equals(GrafcetTagsConstants.PROGRAM_OPT2)) {// PL7PRO

		} else if (program.equals(GrafcetTagsConstants.PROGRAM_OPT3)) {// PCWorx

		}
	}

	public void generarListaProgramMain() {
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
		PostProcess.getPostProcess().getProgramMain(aux, listEmergency, actionStepMap, this.name);
				
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

}
