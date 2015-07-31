package com.hpaz.translator.grafcetelements;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


public class Grafcet {
	
	private String type;
	private String name;
	private String comment;
	private String owner; // propietario
	
	
	
	/*Variables que se usararn solo si el grafcet es de emergencia*/
	private boolean emergency;
	/**Para saber las etepas de la emergencia*/
	private String stepStopEmergency;
	private String StepStartEmergency;
	/**Lista de grafcets que se fuerzan en la emergencia*/	
	private LinkedList<String> listEmergencyStop;
	private LinkedList<String> listEmergencyStart;
	/*Lista de señales del grafcet*/
	private LinkedList<String> signalsGrafcet; 
	
	private LinkedList<Jump> listJ;
	private LinkedList<Sequence> listS;
	private LinkedList<Road> listR;
	

	
	public Grafcet() {
		this.type="";
		this.name="";
		this.comment="";
		this.owner="";
		this.setStepStartEmergency(null);
		this.setStepStopEmergency(null);
		this.setEmergency(false);
		this.listJ = new LinkedList<Jump>();
		this.listS= new LinkedList<Sequence>();
		this.listR = new LinkedList<Road>();
		this.signalsGrafcet = new LinkedList<String>();
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public LinkedList<Jump> getListJ() {
		return listJ;
	}
	public void addJump(Jump pJ) {
		this.listJ.add(pJ);
	}
	public LinkedList<Sequence> getListS() {
		return listS;
	}
	/**Recibe la secuencia y el numero rellenara la lista de seccuencias 
	 * la posicion de la lista sera el numero de secuencia -1 */
	public void addSeq(Sequence pS,int pIndex) {
		/*la posicion de la lista sera el numero de secuencia -1 */
		this.listS.add(pIndex-1, pS);
		/*al añadir la secuencial al grafcet obntengo su lista de señales
		 *  para añadirla a la lista de señales del grafcet*/
		this.signalsGrafcet.addAll(pS.getSignals());
	}
	public LinkedList<Road> getListR() {
		return listR;
	}
	public void addRoad(Road pR) {
		this.listR.add(pR);
	}
	
	/**Si cambia si al aÃ±adir el nombre en el preproceso es GEmergencia*/
	public boolean isEmergency() {
		return emergency;
	}

	public void setEmergency(boolean emergency) {
		this.emergency = emergency;
	}

	public String getStepStopEmergency() {
		return stepStopEmergency;
	}

	public void setStepStopEmergency(String stepStopEmergency) {
		this.stepStopEmergency = stepStopEmergency;
	}

	public String getStepStartEmergency() {
		return StepStartEmergency;
	}

	public void setStepStartEmergency(String stepStartEmergency) {
		StepStartEmergency = stepStartEmergency;
	}

	public LinkedList<String> getListEmergencyStop() {
		return listEmergencyStop;
	}

	public void setListEmergencyStop(LinkedList<String> listEmergencyStop) {
		this.listEmergencyStop = listEmergencyStop;
	}

	public LinkedList<String> getListEmergencyStart() {
		return listEmergencyStart;
	}

	public void setListEmergencyStart(LinkedList<String> listEmergencyStart) {
		this.listEmergencyStart = listEmergencyStart;
	}
	
	public LinkedList<String> grafcetVarGlobalStages() {
		LinkedList<String> vG = new LinkedList<String>();
		vG.add("\n\t(*---"+this.name+"---*)\n\n");
		//por cada secuencia de la lista
		for (Sequence s : listS) {
			vG.addAll(s.printSeqVG());
		}
		
		return vG;
	}
	public LinkedList<String> grafcetVarGlobalSignals() {
		LinkedList<String> vG = new LinkedList<String>();
		//for (Sequence s : listS) {
		
			for (String signal : this.signalsGrafcet) {
				vG.add("\t"+signal+"\t: BOOL;\n");
			}
		//	"\t"+action.getText()+"\t: BOOL;\n"
			//vG.addAll(s.getSignals());
		
		return vG;
	}
	//genera un listado con los set y reset listos para exportar
	public LinkedList<String> printSetReset() {
		LinkedList<String> sR = new LinkedList<String>();
		for (Sequence s : listS) {
			sR.addAll(s.getSetReset());
		}
		return sR;
	}
	public void printGrafcet(){
		System.out.println("----- GRAFCET ------");
		System.out.println("Nombre: "+this.name);
		System.out.println("Tipo: "+this.type);
		System.out.println("Propietario: "+this.owner);
		System.out.println("Comentario: "+this.comment);
		
		for (Jump j : listJ) {
			j.printJump();
		}
		for (Road r : listR) {
			r.printRoad();		
		}
		for (Sequence s : listS) {
			s.printSequence();
		}
	}
	
	/**Este metodo devuelve la primera parte del PROGRAM MAIN */
	public String printVar(){
		String n = this.name.substring(1, 0);
		
		/*Ejemplo:
		 * 	SecPrincipal		: GSecPrincipal;
			InitSecPrincipal	:BOOL;
			ResetSecPrincipal	:BOOL;
		 * */                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 
		String v = "\t\t"+n+"\t:"+this.name+";\n";
		v=v+"\t\tInit"+n+"\t: BOOL;\n";
		v=v+"\t\tReset"+n+"\t: BOOL;\n";
		
		return v;
	}
	
	/**Este metodo devuelve la segunda parte del PROGRAM MAIN */
	public Map<String, String> getActionStepMap(){
		/*AQUI MIRO CADA SECUENCIA DEL GRAFCET Y SUS TRANSICIONES Y STEP
		 * EN STEP --> ACTION:=NAME;
		 * EN TRANSITION --> TRANSITION MIRAR Y HABLAR SOLO PARACE Q NECESITO LA MARCHA Y EL PARO*/
		
		Map<String, String> actionStepMap = new HashMap<String, String>();
		
		//por cada secuencia del grafcet
		for (Sequence sequence : listS) {
				//obtengo el map de la secuencia
				Map<String, String> auxMap =  sequence.getActionStepMap();
				//por cada accion de auxMap
				for (String action : auxMap.keySet()){
					//si la key no existe en actionStepMap
					if (actionStepMap.get(action) == null){
						//aÃ±ado 
						actionStepMap.put(action, auxMap.get(action));
					}else{
						//si existe solo modifico el value
						actionStepMap.put(action, actionStepMap.get(action) + " OR " + auxMap.get(action));
					}
				}			
		}
		
		return actionStepMap;	
	}
	/**Esta funcion rellenara las listas PreviousStepLits, PreviousTransitionList
	 * y getNextStepLits se cada secuencia, que nos serviran luego
	 * a la hora de rellenar los set y reset de cada etapa*/
	public void fillPreviousNextListsSequences(){
		/*por cada salto */
		for (Jump j : listJ) {
			/*En un salto comienza siempre en una etapa por lo que guardo 
			 * FromSeq salta a  ToSeq */
			Sequence s = listS.get(j.getFromSeq());
			int sizeTrans = s.getList().size()-1;
			int sizeStep = s.getList().size()-2;
			Transition t = (Transition) s.getList().get(sizeTrans);
			Step step = (Step) s.getList().get(sizeStep);
			/*Se guardara la ultima transision y etapa de la seq(fromSeq) en la seq(toSeq)*/
			listS.get(j.getToSeq()).addPreviousSeq("("+step.getName()+" AND "+t.getCondition()+")");
		}
		/*Por cada camino que tengamos*/
		for (Road r : listR) {
			//por cada secuencia de la lista de caminos	
			for (Integer sR : r.getMySequences()) {

				Sequence sIni = listS.get(r.getSeqIni());
				Sequence sSR = listS.get(sR);	
				
				/*Si el tipo es div or*/
				if(r.getType().equals("div or")){
					
					/*Guardo en secuencia(sR) la ultima etapa de la secuencia(r.seqIni)*/
					int sizeStep = sIni.getList().size()-1;
					Step step = (Step) sIni.getList().get(sizeStep);
					listS.get(sR).addPreviousSeq(step.getName());
					
					/*Guardar en la secuencia(r.SeqIni) la primera etapa de la secuencia(sR) */
					Step step0 = (Step) sSR.getList().get(0);
					listS.get(r.getSeqIni()).addNextSeq(step0.getName());
					
					
					/*Si el tipo es div and*/
				}else if(r.getType().equals("div and")){
					
					/*Guardo en secuencia(sR) la ultima etapa y transicion de la secuencia(r.seqIni)*/
					int sizeTrans = sIni.getList().size()-1;				
					Transition t = (Transition) sIni.getList().get(sizeTrans);
					int sizeStep = sIni.getList().size()-2;
					Step step = (Step) sIni.getList().get(sizeStep);
					listS.get(sR).addPreviousSeq("("+step.getName()+" AND "+t.getCondition()+")");
					
					/*Guardar en la secuencia(r.SeqIni) la primera etapa de la secuencia(sR) */
					Step step0 = (Step) sSR.getList().get(0);
					listS.get(r.getSeqIni()).addNextSeq(step0.getName());
					
					
					/*Si el tipo es conv or*/
				}else if (r.getType().equals("conv or")){

					/*Guardo en la secuencia(sR) la primera etapa de la secuencia(r.seqIni)*/
					Step step = (Step) sIni.getList().get(0);
					listS.get(sR).addNextSeq(step.getName());
					
					/*Guardar en la secuencia(r.SeqIni) la ultima transicion de secuencia(sR) */
					int sizeTrans = sSR.getList().size()-1;				
					Transition t = (Transition) sIni.getList().get(sizeTrans);
					listS.get(r.getSeqIni()).addPreviousSeq(t.getCondition());
					
					
					
					/*Si el tipo es conv or*/
				} else if(r.getType().equals("conv and")){
					
					/*Guardo en la secuencia(sR) la primera etapa de la secuencia(r.seqIni)*/
					Step step = (Step) sIni.getList().get(0);
					listS.get(sR).addNextSeq(step.getName());
					
					/*Guardar en la secuencia(r.SeqIni) la ultima etapa de secuencia(sR) */
					int sizeStep = sSR.getList().size()-1;
					Step step0 = (Step) sSR.getList().get(sizeStep);
					listS.get(r.getSeqIni()).addPreviousSeq(step0.getName());
				}
			}
		}
	}
	public LinkedList<String> generateFunctionBlock(){
		LinkedList<String> functionBlock = new LinkedList<String>();
		String actualStep="", auxSet="",auxReset="";
		functionBlock.add("\nFUNCTION_BLOCK "+getName()+"\n\tVAR_INPUT"
				+ "\n\t\tInit\t:BOOL;\n\t\tReset\t:BOOL;\n\tEND_VAR"
				+ "\n\tVAR_OUTPUT\n\tEND_VAR\n\tVAR\n\tEND_VAR");
		
		functionBlock.add("\n(*---------------------------------------\n"
				+getName().substring(1, getName().length())+"\n"
				+getComment()
				+ "\n-----------------------------------------------*)");
		//por cada seccuencia
		for (Sequence seq : listS) {
			//recorro la lista de la secuencia
			//Object obj : seq.getList()
			for (int i = 0; i < seq.getList().size(); i++) {	
				/*Si es una etapa*/
				if(seq.getList().get(i) instanceof Step){
					actualStep=((Step) seq.getList().get(i)).getName();
					//int index=seq.getList().indexOf(seq.getList().get(i));
					String aux="";
					
					/*Busco el Set a una etapa*/
					//Si es el primer elemento d la lista
					if(i==0){
						//por cada elemento de de la lista previous
						for (String pre : seq.getPreviousList()){
							aux=aux+" OR "+pre;
						}				
					/*Si no es el primer elemento de la lista
					 * pero es el segundo quiere decir q no tiene etapa antes*/
					}else if (i==1){
						Step sAux= (Step) seq.getList().get(i);
						Transition tAux= (Transition) seq.getList().get(i-1);
						aux="("+sAux.getName()+" AND "+tAux.getCondition()+")";
					}else{
						Step sAux= (Step) seq.getList().get(i-2);
						Transition tAux= (Transition) seq.getList().get(i-1);
						aux="("+sAux.getName()+" AND "+tAux.getCondition()+")";
					}
					/*AÃ±ado el Set a una etapa*/
					((Step) seq.getList().get(i)).addMySet(aux);
					aux ="";
					
					/*Busco el Reset a una etapa*/
					//si hay dos objetos mas en la lista 
				
					
					if(seq.getList().size() > (i+2)){
						//si ese segundo objeto es una etapa
						if(seq.getList().get(i+2) instanceof Step){
							Step sAux= (Step) seq.getList().get(i+2);
							aux=sAux.getName();
						}
					}else{
						//por cada elemento de de la lista next
						for (String nex : seq.getNextLits()){
							aux=aux+" OR "+nex;
						}
					}
					/*AÃ±adir el Reset a una etapa*/
					((Step) seq.getList().get(i)).addMyReset(aux);
					
					
					//Relleno la lista con los Set-Reset
					functionBlock.add("\n(* Set -Reset ___________________________"
							+ "_____________________________________ "+actualStep
							+" *)");
					String set=((Step) seq.getList().get(i)).getMySet();
					String reset =((Step) seq.getList().get(i)).getMyReset();
					//si es una etapa inicial
					if(((Step) seq.getList().get(i)).getType().equals("initial")){
						auxSet = "\n\tIF ( "+set+" OR Init ) THEN\n\t\t"+actualStep+":=1;";
						auxReset = "\n\tEND_IF;\n\tIF ( "+reset+" OR Reset ) THEN\n\t\t"+actualStep+":=0;";	
					}else {
						auxSet = "\n\tIF ( "+set+" ) THEN\n\t\t"+actualStep+":=1;";
						auxReset =  "\n\tEND_IF;\n\tIF ( "+reset+" OR Init OR Reset ) THEN\n\t\t"+actualStep+":=0;";
					}
					functionBlock.add(auxSet);
					functionBlock.add(auxReset);
					functionBlock.add("\n\tEND_IF;\n");
				}	
			}
		}
		functionBlock.add("\nEND_FUNCTION_BLOCK");
		return functionBlock;
		
	}
	/**Rellena las listas de emergencia*/
	public void getEmergency(){
		
		for (Sequence s : getListS()) {
			
			s.getEmergency();
			
			if(s.getStepStartEmergency() != -1){		
				Step stepStart = (Step) s.getList().get(s.getStepStartEmergency());
				setStepStartEmergency(stepStart.getName());
				setListEmergencyStart(stepStart.getGrafcetsStartEmergency());
				
			}	else if(s.getStepStopEmergency() != -1){
				Step stepStop = (Step) s.getList().get(s.getStepStopEmergency());
				setStepStopEmergency(stepStop.getName());
				setListEmergencyStop(stepStop.getGrafcetsStopEmergency());
			}
			
		}
		
	}
	public boolean compareStartAndStopLists() {
		Collections.sort(this.listEmergencyStop);
	    Collections.sort(this.listEmergencyStart);      
	    return getListEmergencyStart().equals(getListEmergencyStop());
	}
	
	public LinkedList<String> getListSignalsGrafcet(){
		/*Esta lista se va rellenando al ir añadiendo 
		 * una secuencia al grafcet*/
		return this.signalsGrafcet;
	}
	
}
