package com.hpaz.translator.grafcetelements;

import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Action {
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
		this.comment="";
		this.startEmergency=new LinkedList<String>();
		this.stopEmergency=new LinkedList<String>();
		this.emergency=false;
	}
	public void fillAttributes(Map<String, String> pAttributes) {
		// añado el tipo
		this.type = pAttributes.get("type");		
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getText() {
		return text;
	}
	public void addText(String pText) {
		if (getText() == null){
			this.text = pText;
		}else {
			this.text = getText() + " " + pText;
		}
		Pattern pat = Pattern.compile("^F/G.*");
		Matcher mat = pat.matcher(getText());
		if(mat.matches()){
			this.emergency=true;
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
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
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
	
	public void printAction(){
		System.out.println("----- ACTION ------");
		System.out.println("Accion  : " + getText());
		System.out.println("	Tipo: " + getType());
		System.out.println("	Condicion: " + getCondition());
		System.out.println("	Comentario: " + getComment());
	}
	
	/**Para saber la etepa de la emergencia,
	 * Devuelve stop o start*/
	public String getEmergency() {
		String s="";
		/*Uso expresiones regulares*/
		/*Emergencia forzado a stop*/
		Pattern pat = Pattern.compile("^F/G.*.>\\{\\}$");
		Matcher mat = pat.matcher(getText());
		if(mat.matches()){
			s= "stop";
			generateListEmergency(getText(),s);
		}else{
			/*Emergencia forzado a start*/
			Pattern pat1 = Pattern.compile("^F/G.*.>\\{X.[0-9]\\}$");
			Matcher mat1 = pat1.matcher(getText());
			if(mat1.matches()){
				s= "start";	
				generateListEmergency(getText(),s);
			}
		}
		
		return s;
	}
	
	private void generateListEmergency(String pText, String pOpc) {
		LinkedList<String> auxtList=new LinkedList<String>();
		//Quito los espacios en blanco
		String  auxText = pText.trim();
		//Me quedo solo con los nombres de los grafcets
		auxText = auxText.replaceAll("F/G|>\\{\\}|>\\{X.[0-9]\\}|>\\{X0\\}", "");
		boolean aux=true;
		int cont = 0;
	
		while(aux){
			cont=auxText.indexOf(",");
			if(cont!=(-1)){
				auxtList.add(auxText.substring(0,cont));
				auxText=auxText.substring(cont+1, auxText.length());
			}else{
				auxtList.add(auxText);
				aux=false;
			}
		}
		
		if(pOpc.equals("stop")){
			this.stopEmergency.addAll(auxtList);
		}else if(pOpc.equals("start")){
			this.startEmergency.addAll(auxtList);
		}
		
	}



	
}
