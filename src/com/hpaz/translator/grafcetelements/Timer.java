package com.hpaz.translator.grafcetelements;

public class Timer {
	private String nameTimer;
	private String stepNameTimer;
	private int time;
	private String typeTimer;
	private String typeTime;
	
	
	
	public Timer(){
		this.nameTimer = null;
		this.stepNameTimer = null;
		this.addTypeTime(null);
		this.addTypeTimer(null);
		this.time = 0;
	}
	public Timer fillTimer(String[] list){
		//this.nameTimer = list[0];
		this.addStepNameTimer(list[1]);
		String aux = list[2];
		//Extraigo el texto sin numero
		aux = aux.replaceAll("[0-9]", "");
		this.addTypeTime(aux.trim());
		//Extraigo el numero sin texto
		aux = list[2].replaceAll("[a-z]|[A-Z]", "");
		this.time = Integer.parseInt(aux.trim());
		
		return this;
	}

	/*public enum typeTimer
	{
		TON,TOF,TP
	}
	public enum typeTime
	{
		//TODO mirar en el PL7 los tipos de tiempos q hay
		MS,S,M,H
	}*/

	public String getNameTimer() {
		return nameTimer;
	}

	public void addNameTimer(String pNameTimer) {
		this.nameTimer = pNameTimer;
	}

	public String getStepNameTimer() {
		return stepNameTimer;
	}

	public void addStepNameTimer(String pStepNameTimer) {
		if(stepNameTimer==null){
			this.stepNameTimer = pStepNameTimer;
		} else{
			this.stepNameTimer = this.stepNameTimer + " OR " + pStepNameTimer;
		}
	}

	public int getTime() {
		return time;
	}

	public void addTime(int pTime) {
		this.time = pTime;
	}

	public String getTypeTimer() {
		return typeTimer;
	}

	public void addTypeTimer(String typeTimer) {
		this.typeTimer = typeTimer;
	}

	public String getTypeTime() {
		return typeTime;
	}

	public void addTypeTime(String tipeTime) {
		this.typeTime = tipeTime;
	}
	public void printConsole() {
		System.out.println("nameTimer: "+getNameTimer());
		System.out.println("stepNameTimer: "+getStepNameTimer());
		System.out.println("time: "+getTime());
		System.out.println("typeTime: "+getTypeTime());
		System.out.println("typeTimer: "+this.typeTimer);
		
	}
	public boolean equals(Timer tim) {
		return this.nameTimer.equals(tim.getNameTimer());
	}
	public String getGlobalsVarTimer(){
		String globalsVar = "\t" + this.nameTimer + "Q\t: BOOL;\n"+
							"\t" + this.nameTimer + "IN\t: BOOL;\n" +
							"\t" + this.nameTimer + "PT\t: TIME;\n" +
							"\t" + this.nameTimer + "ET\t: TIME;\n";
		
		return globalsVar;
		
	}
	/**Devuelve un array de string donde el primero corresponde a las variables 
	 * del program main y el segundo al programa en si*/
	public String[] getProgramMainTimer(){
		String []  programMain = {"\t" + this.nameTimer + "\t: "+this.typeTimer+";\n",
								 "\t" + this.nameTimer + "IN:="+this.stepNameTimer+";\n" +
								"\t" + this.nameTimer + "PT:= T#" +this.time +this.typeTime+";\n" +
								"\t" + this.nameTimer + "(IN:="+this.nameTimer+"IN , PT:="+this.nameTimer+
										"PT , Q=>"+this.nameTimer+"Q , ET=> "+this.nameTimer+"ET);\n"};
		
			
		return programMain;
		
	}
}
