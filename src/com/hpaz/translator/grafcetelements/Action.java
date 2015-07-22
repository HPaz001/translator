package com.hpaz.translator.grafcetelements;

public class Action {
	private String type;
	private String text;
	private String condition;
	private String comment;//comentario de la accion
	
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
	public void printAction(){
		System.out.println("----- ACTION ------");
		System.out.println("Accion  : " + getText());
		System.out.println("	Tipo: " + getType());
		System.out.println("	Condicion: " + getCondition());
		System.out.println("	Comentario: " + getComment());
	}
}
