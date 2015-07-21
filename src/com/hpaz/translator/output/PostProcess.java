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
	
	

}
