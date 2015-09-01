package com.hpaz.translator.grafcetelements;

import com.hpaz.translator.grafcetelements.constants.GrafcetTagsConstants;

public class Timer {
	private String nameTimer;
	private String stepNameTimer;
	private int time;
	private String typeTimer;
	private String typeTime;
	
	
	
	public Timer(){
		this.nameTimer = null;
		this.stepNameTimer = null;
		this.typeTime = null;
		this.typeTimer = null;
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
			this.stepNameTimer = "RE "+pStepNameTimer;
		} else{
			this.stepNameTimer = this.stepNameTimer + " OR RE " + pStepNameTimer;
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
/*	public void printConsole() {
		System.out.println("nameTimer: "+getNameTimer());
		System.out.println("stepNameTimer: "+getStepNameTimer());
		System.out.println("time: "+getTime());
		System.out.println("typeTime: "+getTypeTime());
		System.out.println("typeTimer: "+this.typeTimer);
		
	}*/
	public boolean equals(String tim) {
		return this.nameTimer.equals(tim);
	}
	public String getGlobalsVarTimer(String pTypeProgram){
		String var ="";
		if (pTypeProgram.equals(GrafcetTagsConstants.PROGRAM_OPT1)){//TwinCat
			var ="\t" + this.nameTimer + "Q\t: BOOL;\n"+
					"\t" + this.nameTimer + "IN\t: BOOL;\n" +
					"\t" + this.nameTimer + "PT\t: TIME;\n" +
					"\t" + this.nameTimer + "ET\t: TIME;\n";
		}else if (pTypeProgram.equals(GrafcetTagsConstants.PROGRAM_OPT3)) {//PLCOpen
			var = "<variable name=\""+this.nameTimer+"Q\"><type><BOOL /></type></variable>"
					+ "<variable name=\""+this.nameTimer+"IN\"><type><BOOL /></type></variable>"
					+ "<variable name=\""+this.nameTimer+"PT\"><type><TIME /></type></variable>"
					+ "<variable name=\""+this.nameTimer+"ET\"><type><TIME /></type></variable>";
		}
		
		return var;
		
	}
	/**Devuelve un array de string donde el primero corresponde a las variables 
	 * del program main y el segundo al programa en si*/
	public String getProgramMainTimer(){
		
		String  programMain = 	"\n\t" + this.nameTimer + "PT:= T#" +this.time +this.typeTime+";\n" +
								"\t" + this.nameTimer + "(IN:="+this.nameTimer+"IN , PT:="+this.nameTimer+
										"PT , Q=>"+this.nameTimer+"Q , ET=> "+this.nameTimer+"ET);\n";
		
			
		return programMain;
		
	}
	public String getExternalVarsPLCOpen(){
		
		String externalVar = "<br /><variable name=\"" + this.nameTimer + "Q\" group=\"Default\"><type><BOOL /></type></variable>"
				+ "<br /><variable name=\"" + this.nameTimer + "IN\" group=\"Default\"><type><BOOL /></type></variable>"
				+ "<br /><variable name=\"" + this.nameTimer + "PT\" group=\"Default\"><type><TIME /></type></variable>"
				+ "<br /><variable name=\"" + this.nameTimer + "ET\" group=\"Default\"><type><TIME /></type></variable>";
		
		return externalVar;
		
	}
	/***/
	public String getBodyPLCOpen(){
		/*<br />TempPT:=T#2s;
		<br />Temp(IN:=TempIN, PT:=TempPT);
		<br />TempQ:=Temp.Q;
		<br />TempET:=Temp.ET;*/
		String   body =  "<br />" + this.nameTimer + "IN:="+this.stepNameTimer+";"
				+ "<br />" + this.nameTimer + "PT:= T#" +this.time +this.typeTime+";"
				+ "<br />" + this.nameTimer + "(IN:="+this.nameTimer+"IN , PT:="+this.nameTimer+"PT);"
				+ "<br />"+this.nameTimer+"Q:="+this.nameTimer+".Q;"
				+ "<br />"+this.nameTimer+"ET:="+this.nameTimer+".ET;";
		
			
		return body;
		
	}
}
