package com.hpaz.translator.grafcetelements;

/*TODO Contador etapas init .. Creo que se deben guardar las etapas cuando se inicializan y en q etapas*/

public class Counter {
	private String nameCounter;
	private String typeCounter;
	private String stepCountes;

	public Counter() {
		this.nameCounter = null;
		this.typeCounter=null;
		this.stepCountes = null;
	}

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
		if (this.stepCountes == null) {
			this.stepCountes = stepCountes;
		} else {
			this.stepCountes = this.stepCountes + " OR " + stepCountes;
		}

	}
/*
	public void printConsole() {
		System.out.println("getNameCounter: " + nameCounter);
		System.out.println("getStepCountes: " + stepCountes);
		System.out.println("getTypeCounter: " + typeCounter);
	}
*/
	public String getGlobalsVarCounter() {

		String globalsVar = "";

		if (this.typeCounter.equals("CTD")) {
			globalsVar = "\t" + this.nameCounter + "CD\t: BOOL;\n" + "\t" + this.nameCounter + "LOAD\t: BOOL;\n" + "\t"
					+ this.nameCounter + "PV\t: INT;\n" + "\t" + this.nameCounter + "Q\t: BOOL;\n" + "\t"
					+ this.nameCounter + "CV\t: INT;\n";

		} else if (this.typeCounter.equals("CTU")) {
			globalsVar = "\t" + this.nameCounter + "CU\t: BOOL;\n" + "\t" + this.nameCounter + "RESET\t: BOOL;\n" + "\t"
					+ this.nameCounter + "PV\t: INT;\n" + "\t" + this.nameCounter + "Q\t: BOOL;\n" + "\t"
					+ this.nameCounter + "CV\t: INT;\n";

		} else if (this.typeCounter.equals("CTUD")) {
			globalsVar = "\t" + this.nameCounter + "CU\t: BOOL;\n" + "\t" + this.nameCounter + "CD\t: BOOL;\n" + "\t"
					+ this.nameCounter + "RESET\t: BOOL;\n" + "\t" + this.nameCounter + "LOAD\t: BOOL;\n" + "\t"
					+ this.nameCounter + "PV\t: INT;\n" + "\t" + this.nameCounter + "QU\t: BOOL;\n" + "\t"
					+ this.nameCounter + "QD\t: BOOL;\n" + "\t" + this.nameCounter + "CV\t: INT;\n";
		}

		return globalsVar;

	}

	/**
	 * Devuelve un array con dos elemetos en uno la iniciacion del tipo de count
	 * (var del program main) y en otro parte del program main
	 */
	public String[] getProgramMainCounter() {
		String[] programMain = new String[2];

		if (this.typeCounter.equals("CTD")) {

			// TODO contador inicializaciones preguntar q falta
			programMain[0] = "\t" + this.nameCounter + "\t: " + this.typeCounter + ";\n";
			programMain[1] = this.nameCounter + "CD:= " + this.nameCounter + "CD," + " LOAD:= " + this.nameCounter
					+ "LOAD," + " PV:= " + this.nameCounter + "PV," + " Q=> " + this.nameCounter + "Q," + " CV=> "
					+ this.nameCounter + "CV);";

		} else if (this.typeCounter.equals("CTU")) {

			// TODO contador inicializaciones preguntar q falta
			programMain[0] = "\t" + this.nameCounter + "\t: " + this.typeCounter + ";\n";
			programMain[1] = this.nameCounter + "(CU:= " + this.nameCounter + "CU ," + " RESET:= " + this.nameCounter
					+ "RESET," + " PV:= " + this.nameCounter + "PV," + " Q=> " + this.nameCounter + "Q," + " CV=> "
					+ this.nameCounter + "CV);";

		} else if (this.typeCounter.equals("CTUD")) {

			// TODO contador inicializaciones preguntar q falta
			programMain[0] = "\t" + this.nameCounter + "\t: " + this.typeCounter + ";\n";
			programMain[1] = this.nameCounter + "(CU:= " + this.nameCounter + "CU ," + "CD:= " + this.nameCounter
					+ "CD," + " RESET:= " + this.nameCounter + "RESET," + " LOAD:= " + this.nameCounter + "LOAD,"
					+ " PV:= " + this.nameCounter + "PV," + " QU=> " + this.nameCounter + "QU," + " QD=> "
					+ this.nameCounter + "QD," + " CV=> " + this.nameCounter + "CV);";
		}

		return programMain;

	}

}
