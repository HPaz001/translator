package com.hpaz.translator.grafcetelements;

//TODO cont expr reg
/*Pattern cont = Pattern.compile("^Cont.*.=[0-9]$");*/


public class Counter {
	private String nameCounter;
	private String typeCounter;
	
	public Counter(){
		this.nameCounter=null;
		this.addTypeCounter(null);
	}
	
	/*public enum typeCounter
	{
		CTU,CTD,CTUD
	}*/

	public String getNameCounter() {
		return nameCounter;
	}

	public void addNameCounter(String pNameCounter) {
		this.nameCounter = pNameCounter;
	}

	public String getTypeCounter() {
		return typeCounter;
	}

	public void addTypeCounter(String typeCounter) {
		this.typeCounter = typeCounter;
	}

	
}
