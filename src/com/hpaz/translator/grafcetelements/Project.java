package com.hpaz.translator.grafcetelements;

import java.util.LinkedList;

import com.hpaz.translator.output.Output;
import com.hpaz.translator.output.PostProcess;


public class Project {
	private String name;
	private String language;
	private String program; /**PL7PRO TSX, TwinCat Beckch, PCWorx PLCOpen*/
	private LinkedList<Grafcet> listG;
	private static Project project = new Project();
	
	private Project() {
		listG = new LinkedList<Grafcet>();
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
		vG.add("\n\t(*---Se�ales---*)\n\n");
		for (Grafcet g : listG) {
			vG.addAll(g.printGrafcetVarGlobalSignals());
		}
		
		return vG;
	}
	public void print(String pCompatibility){
		if(pCompatibility.equals("T")){
			try {
				Output.getSalida().exportarFichero(PostProcess.getOutputProcess().getGlobalVar(printVarGlobal()), this.name);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(pCompatibility.equals("PL")){
			
		}else if(pCompatibility.equals("PC")){
			
		}
		
	}
	public void printProject(){
		System.out.println("----- PROJECT ------");
		System.out.println("Nombre: "+this.name);
		System.out.println("Lenguaje: "+this.language);
		System.out.println("Compatibilidad: "+this.program);
		for (Grafcet g : listG) {
			g.printGrafcet();
		}
			
	}

	
	
	

}
