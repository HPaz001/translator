package com.hpaz.translator.grafcetelements;

import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Action {
	//guarda el tipo de la accion
	private String type;
	//guarda el texto de la accion
	private String text;
	private String condition;
	private String comment;//comentario de la accion
	private LinkedList<String> stopEmergency;
	private LinkedList<String> startEmergency;
	private boolean emergency;
	


	public Action() {
		this.type="";
		this.text=null;
		this.condition=null;
		this.comment=null;
		this.startEmergency=new LinkedList<String>();
		this.stopEmergency=new LinkedList<String>();
		this.emergency=false;
	}
	public void fillAttributes(Map<String, String> pAttributes) {
		// aÃ±ado el tipo
		addType(pAttributes.get("type"));
	}
	
	private void addType(String type) {
		this.type = type;
	}
	
	private void addStopEmergency(LinkedList<String> stopEmergency) {
		this.stopEmergency = stopEmergency;
	}
	private void addStartEmergency(LinkedList<String> startEmergency) {
		this.startEmergency = startEmergency;
	}
	public String getType() {
		return type;
	}
	public String getText() {
		return text;
	}
	public void addText(String pText) {
		//añado el testo de la accion me va llegando por partes
		if (getText() == null){
			this.text = pText;
		}else {
			this.text = getText() + " " + pText;
		}		
	}
	public String getCondition() {
		return condition;
	}
	public void addCondition(String pCondition) {
		if (getCondition() == null){
			this.condition = pCondition;
		}else {
			this.condition = getCondition() + " " + pCondition;
		}
	}
	private String getComment() {
		return comment;
	}
	public void addComment(String comment) {
		if (getComment() == null){
			this.comment = comment;
		}else {
			this.comment = getComment()+ " " +comment;
		}
		
		
	}
	
	public LinkedList<String> getStopEmergency() {
		return this.stopEmergency;
	}
	
public LinkedList<String> getStartEmergency() {
		return this.startEmergency;
	}
	
public boolean isEmergency() {
		return emergency;
	}
	
	/*public void printAction(){
		System.out.println("----- ACTION ------");
		System.out.println("Accion  : " + getText());
		System.out.println("	Tipo: " + getType());
		System.out.println("	Condicion: " + getCondition());
		System.out.println("	Comentario: " + getComment());
	}*/
	
	/**Llama a la funcion de generado de listas de emergencia*/
	public void getEmergency() {
		
		/*Uso expresiones regulares*/
		/*Emergencia forzado a stop*/
		Pattern pat = Pattern.compile("^F/G.*.> \\{\\}$");
		Matcher mat = pat.matcher(getText().trim());
		if(mat.matches()){
			addStopEmergency(generateListEmergency());
		}else{
			/*Emergencia forzado a start*/
			Pattern pat1 = Pattern.compile("^F/G.*.> \\{X.[0-9]\\}$");
			Matcher mat1 = pat1.matcher(getText().trim());
			if(mat1.matches()){
				addStartEmergency(generateListEmergency());
			}
		}
		
	}
	
	private LinkedList<String> generateListEmergency() {
		LinkedList<String> returnList=new LinkedList<String>();
		//Quito los espacios en blanco
		String  auxText = getText().trim();
		//Me quedo solo con los nombres de los grafcets separados por ,
		auxText = auxText.replaceAll("F/G|>|\\{\\}|\\{X.[0-9]\\}|\\{X0\\}| ", "");
		//boolean aux=true;
		//int cont = 0;
		//separo por coma
		String [] auxLis = auxText.split(",");
		//por cada elemento de las lista
		for (int i = 0; i < auxLis.length; i++) {
			//guardo el nombre de un grafcet
			returnList.add(auxLis[i].trim());
		}
		//devuelvo la lista
		return returnList;
	}
	public void addEmergency(boolean b) {
		this.emergency = b;
		
	}



	
}
