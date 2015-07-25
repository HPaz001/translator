package com.hpaz.translator.grafcetelements;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


public class Grafcet {
	
	private String type;
	private String name;
	private String comment;
	private String owner; // propietario
	private LinkedList<Jump> listJ;
	private LinkedList<Sequence> listS;
	private LinkedList<Road> listR;
	

	
	public Grafcet() {
		this.type="";
		this.name="";
		this.comment="";
		this.owner="";
		this.listJ = new LinkedList<Jump>();
		this.listS= new LinkedList<Sequence>();
		this.listR = new LinkedList<Road>();
		
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
	/**Recibe la secuencia y el numero de secuecia
	 * rellenara la lista de seccuencias 
	 * la posicion de la lista sera el numero de secuencia -1 */
	public void addSeq(Sequence pS,int pIndex) {
		/*la posicion de la lista sera el numero de secuencia -1 */
		this.listS.add(pIndex-1, pS);
	}
	public LinkedList<Road> getListR() {
		return listR;
	}
	public void addRoad(Road pR) {
		this.listR.add(pR);
	}
	public LinkedList<String> grafcetVarGlobalStages() {
		LinkedList<String> vG = new LinkedList<String>();
		vG.add("\n\t(*---"+this.name+"---*)\n\n");
		/*if(!this.owner.equals("")){
			vG.add("(* Propietario: "+this.owner+"*)\n");
		}
		if(!this.comment.equals("")){
			vG.add("(*"+this.comment+"*)\n");
		}
		ESTO SE USARA EN EL SET Y RESET*/
		
		//por cada secuencia de la lista
		for (Sequence s : listS) {
			vG.addAll(s.printSeqVG());
		}
		
		return vG;
	}
	public LinkedList<String> grafcetVarGlobalSignals() {
		LinkedList<String> vG = new LinkedList<String>();
		for (Sequence s : listS) {
			vG.addAll(s.getSignals());
		}
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
						//añado 
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
			listS.get(j.getToSeq()).addPreviousSeq("("+step.getName()+" AND "+t.getConditionComp()+")");
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
					listS.get(sR).addPreviousSeq("("+step.getName()+" AND "+t.getConditionComp()+")");
					
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
					listS.get(r.getSeqIni()).addPreviousSeq(t.getConditionComp());
					
					
					
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
	
	
	
	
}
