package com.hpaz.translator.grafcetelements;

//TODO cont expr reg
/*Pattern cont = Pattern.compile("^Cont.*.=[0-9]$");*/


public class Counter {
	private String nameCounter;
	
	public Counter(){
		this.nameCounter=null;
	}
	
	public enum typeCounter
	{
		CTU,CTD,CTUD
	}

	public String getNameCounter() {
		return nameCounter;
	}

	public void addNameCounter(String pNameCounter) {
		this.nameCounter = pNameCounter;
	}

	
}
