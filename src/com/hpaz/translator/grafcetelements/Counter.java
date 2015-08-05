package com.hpaz.translator.grafcetelements;

//TODO cont expr reg
/*Pattern cont = Pattern.compile("^Cont.*.=[0-9]$");*/


public class Counter {
	private String nameCounter;
	private String typeCounter;
	private String stepCountes;
	
	public Counter(){
		this.nameCounter=null;
		this.addTypeCounter(null);
		this.stepCountes=null;
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
	
	public boolean equals(String pCounter) {
		return this.nameCounter.equals(pCounter);
	}

	public String getStepCountes() {
		return stepCountes;
	}

	public void addStepCountes(String stepCountes) {
		if(this.stepCountes  == null){
			this.stepCountes = stepCountes;
		}else{
			this.stepCountes = this.stepCountes + " OR " + stepCountes;
		}
		
	}

	
}
