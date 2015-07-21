package com.hpaz.translator.output;

import java.util.LinkedList;
import java.util.Map;

import com.hpaz.translator.grafcetelements.Project;

public class PostProcess {
	
		
	private static PostProcess myPostProcess = new PostProcess();
	
	public PostProcess(){}

	public static PostProcess getPostProcess() {
		return myPostProcess;
	}
		
	/**Este metodo se encargara de obtener la parte secuencial (set-reset) 
	 * de "UN GRAFCET" correspondientes al TwinCAT
	 * Pre:
	 * Post: devolvera una lista de String*/
	public LinkedList<String> getFunctionBlock(){
		//PROGRAMAR 
		return null;	
	}
	/**Este metodo se encargara de obtener las variables globales correspondientes a TwinCAT
	 * Pre:
	 * Post: devolvera una lista de String*/
	public LinkedList<String> getGlobalVar(LinkedList<String> text){
		LinkedList<String> t = new LinkedList<String>();
		t.add("VAR_GLOBAL\n");
		t.addAll(removeDuplicates(text));
		t.add("\nEND_VAR\n");
		return null;
	}
	/**Este metodo se encargara de obtener la parte combinacional correspondientes a TwinCAT
	 * Pre:
	 * Post: devolvera una lista de String*/
	public void getProgramMain(LinkedList<String> pNames, LinkedList<String> pEmergency, Map<String, String> pInit, String pProjecName){
		//PROGRAMAR 
		LinkedList<String> listaProgramMain = new LinkedList<String>();
		
		listaProgramMain.add("PROGRAM MAIN\n\tVAR");
		
		for (String name : pNames) {
			/*Ejemplo:
			 * 	SecPrincipal		: GSecPrincipal;
				InitSecPrincipal	:BOOL;
				ResetSecPrincipal	:BOOL;
			 * */
			String n = name.substring(1, 0);
			listaProgramMain.add("\n\t\t"+n+"\t:"+name+";\n");
			listaProgramMain.add("\t\tInit"+n+"\t: BOOL;\n");
			listaProgramMain.add("\t\tReset"+n+"\t: BOOL;\n");
		}	
		
		listaProgramMain.add("\n\tXInit\t:BOOL;\n\tXReset\t:BOOL;\n\n\tEND_VAR\n");
		
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
			listaProgramMain.add("\n\tReset"+emerg+":="+Project.getProject().getStop()+";\n");
			listaProgramMain.add("\n\t"+emerg+"(Init:=(XInit OR Init"+emerg+"), Reset:=(XReset OR Reset"+emerg+"));\n\n");
		}

		for (String action : pInit.keySet()) {
			listaProgramMain.add("\t"+action+":="+pInit.get(action)+";\n");
		}
		
		listaProgramMain.add("END_PROGRAM\n");
		
		try {
			Output.getOutput().exportarFichero(listaProgramMain, pProjecName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	/**Este metodo se encargara de obtener la parte secuencial correspondientes a PL7PRO
	 * Pre:
	 * Post: devolvera una lista de String*/
	public LinkedList<String> getSequentialPart(){
		//PROGRAMAR 
		return null;
	}
	/**Este metodo se encargara de obtener la parte combinacional correspondientes a PL7PRO
	 * Pre:
	 * Post: devolvera una lista de String*/
	public LinkedList<String> getCombinationalPart(){
		//PROGRAMAR 
		return null;
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
