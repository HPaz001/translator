package com.hpaz.translator.output;

import java.util.LinkedList;

public class PostProcess {
	
		
	private static PostProcess myOutputProcess = new PostProcess();
	
	public PostProcess(){}

	public static PostProcess getOutputProcess() {
		return myOutputProcess;
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
	public LinkedList<String> getProgramMain(){
		//PROGRAMAR 
		LinkedList<String> listaProgramMain = new LinkedList<String>();
		
		
		
		
		
		return listaProgramMain;
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
