package com.hpaz.translator.grafcetelements;

public class Step {
	/***/
	
	private String type;
	private String name;
	private String action;
	private String typeAction;
	private String comment;
	private String condition; //segun el tipo de accion tiene condicion o no
	
	
	public Step() {
		type="";
		name="";
		action="";
		typeAction="";
		comment="";
		condition="";
		
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
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getTypeAction() {
		return typeAction;
	}
	public void setTypeAction(String typeAction) {
		this.typeAction = typeAction;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String printStepVG() {
		String s = "\t"+this.name+"\t: BOOL;\n";
		return s;
	}
	public void printStep(){
		System.out.println("----- STEP ------");
		System.out.println("Nombre: "+this.name);
		System.out.println("Tipo: "+this.type);
		System.out.println("Accion: "+this.action);
		System.out.println("Tipo accion: "+this.typeAction);
		System.out.println("Condicion: "+this.condition);
		System.out.println("Comentario: "+this.comment);
	}
	
}
