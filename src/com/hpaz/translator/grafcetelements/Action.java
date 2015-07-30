package com.hpaz.translator.grafcetelements;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hpaz.translator.grafcetelements.constants.GrafcetTagsConstants;

public class Action {
	private String type;
	private String text;
	private String condition;
	private String comment;//comentario de la accion
	private LinkedList<String> stopEmergency;
	private LinkedList<String> startEmergency;
	
	public Action() {
		this.type="";
		this.text="";
		this.condition="";
		this.comment="";
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
	public void setText(String text) {
		this.text = text;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
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
		if(getType().equalsIgnoreCase((String) GrafcetTagsConstants.ACTION_FORCING_ORDER)){
			/*Para añadir expresiones regulares*/
			/*Emergencia forzado a stop*/
			Pattern pat = Pattern.compile("^F/G.*.>\\{\\}$");
			Matcher mat = pat.matcher(getText());
			
			/*Emergencia forzado a start*/
			Pattern pat1 = Pattern.compile("^F/G.*.>\\{.*.\\}$");
			Matcher mat1 = pat1.matcher(getText());
		
			if(mat.matches()){
				s= "stop";
				generateListEmergency(getText(),s);
			}else{
				if(mat1.matches()){
					s= "start";	
					generateListEmergency(getText(),s);
				}
				
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
