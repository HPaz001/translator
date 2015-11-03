package com.hpaz.translator.grafcetelements;

import com.hpaz.translator.grafcetelements.constants.GrafcetTagsConstants;

public class Timer {
	/**Nombre del temporizador*/
	private String nameTimer;
	/**Etapas en las que se activa el temporizador*/
	private String stepNameTimer;
	/**Tiempo que transcurre temporizando (nro)*/
	private int time;
	/**Tipo de temporizador*/
	private String typeTimer;
	/**Tipo de Tiempo del temporizador: segundos, minutos, horas...*/
	private String typeTime;
	
	public Timer(){
		this.nameTimer = null;
		this.stepNameTimer = null;
		this.typeTime = null;
		this.typeTimer = null;
		this.time = 0;
	}
	
	public Timer fillTimer(String string){
		//this.nameTimer = list[0];
		//addStepNameTimer(list[1]);
		String aux = string;
		//Extraigo el texto sin numero
		aux = aux.replaceAll("[0-9]", "");
		addTypeTime(aux.trim());
		//Extraigo el numero sin texto
		aux = string.replaceAll("[a-z]|[A-Z]", "");
		addTime(Integer.parseInt(aux.trim()));
		
		return this;
	}

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
	
	public String getTypeTimer() {
		return typeTimer;
	}

	public void addTypeTimer(String typeTimer) {
		this.typeTimer = typeTimer;
	}

	public String getTypeTime() {
		return typeTime;
	}
	
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
	
	private void addTime(int pTime) {
		this.time = pTime;
	}

	private void addTypeTime(String tipeTime) {
		//para los segundos
		if("seg".equalsIgnoreCase(tipeTime)||"sg".equalsIgnoreCase(tipeTime)||"s".equalsIgnoreCase(tipeTime)){
			this.typeTime = "s";
		}
		//TODO falta mirar como expresa twincat el resto de tiempos
	}

}
