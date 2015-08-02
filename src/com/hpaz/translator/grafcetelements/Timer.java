package com.hpaz.translator.grafcetelements;
//TODO temporizadores exp reg
/*De esta forma puedo comparar etapa y tiempo en la transicion
Pattern temp = Pattern.compile("^Temp.*.=[0-9].*");
En una transicion
Pattern temp = Pattern.compile("^Temp.*./X.[0-9]./[0-9].*");
*/

public class Timer {
	private String nameTimer;
	private String stepNameTimer;
	private int time;
	
	
	public Timer(){
		this.nameTimer = null;
		this.stepNameTimer = null;
		this.time = 0;
	}

	public enum typeTimer
	{
		TON,TOF,TP
	}
	public enum typeTime
	{
		//TODO mirar en el PL7 los tipos de tiempos q hay
		MS,S,M,H
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
		this.stepNameTimer = pStepNameTimer;
	}

	public int getTime() {
		return time;
	}

	public void addTime(int pTime) {
		this.time = pTime;
	}

}
