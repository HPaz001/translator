package com.hpaz.translator.grafcetelements;


/*TODO Contador etapas init .. Creo que se deben guardar las etapas cuando se inicializan y en q etapas*/


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
	public void printConsole() {
		System.out.println("getNameCounter: "+getNameCounter());
		System.out.println("getStepCountes: "+getStepCountes());
		System.out.println("getTypeCounter: "+getTypeCounter());
	}

	
}
