package com.hpaz.translator.grafcetelements;

import com.hpaz.translator.grafcetelements.constants.GrafcetTagsConstants;

/*TODO Contador etapas init .. Creo que se deben guardar las etapas cuando se inicializan y en q etapas*/

public class Counter {
	/** Nombre del contador */
	private String nameCounter;
	/** Tipo de contador */
	private String typeCounter;
	/** Valor inicial del temporizador, por defecto es 0(RESET) */
	private String initialValue;
	/**
	 * Etapas que dan Valor inicial al temp, estas seran las etapas que
	 * modifican el LOAD
	 */
	private String stepInitialValue;
	/** Etapas de decrementan al contador */
	private String stepCountesCD;
	/** Etaps que incrementan al conador */
	private String stepCountesCU;

	public Counter() {
		this.nameCounter = null;
		this.typeCounter = null;
		this.stepCountesCD = null;
		this.stepCountesCU = null;
		this.initialValue = null;
		this.stepInitialValue=null;
	}

	public String getInitialValue() {
		return initialValue;
	}

	public void addInitialValue(String initialValue) {
		// si es null entonces es la primera vez que se inicia el contador
		if (getInitialValue() == null) {
			this.initialValue = initialValue;
		}
		/*
		 * si no es null entonces ya se inicio una vez, y el valor dado por
		 * n-esima vez debe sre siempre igual al inicial, por lo que para no
		 * hacer salta un error directamente lo ignoro y dejo el valor inicial.
		 */

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
	public String getStepInitialValue() {
		return stepInitialValue;
	}

	public void addStepInitialValue(String stepInitialValue) {
		if (this.stepInitialValue == null) {
			this.stepInitialValue = stepInitialValue;
		} else {
			this.stepInitialValue = this.stepInitialValue + " OR " + stepInitialValue;
		}
	}
	public String getStepCountesCD() {
		return stepCountesCD;
	}

	public void addStepCountesCD(String stepCountes) {
		if (this.stepCountesCD == null) {
			this.stepCountesCD = stepCountes;
		} else {
			this.stepCountesCD = this.stepCountesCD + " OR " + stepCountes;
		}

	}

	public String getStepCountesCU() {
		return stepCountesCU;
	}

	public void addStepCountesCU(String stepCountes) {
		if (this.stepCountesCU == null) {
			this.stepCountesCU = stepCountes;
		} else {
			this.stepCountesCU = this.stepCountesCU + " OR " + stepCountes;
		}

	}

	public String getGlobalsVarCounter(String pTypeProgram, String type) {

		String var = "";
		addTypeCounter(type);
		if (pTypeProgram.equals(GrafcetTagsConstants.PROGRAM_OPT1)) {// TwinCat
			if ("CTD".equals(type)) {
				var = "\t" + this.nameCounter + "CD\t: BOOL;\n" + "\t" + this.nameCounter + "LOAD\t: BOOL;\n" + "\t"
						+ this.nameCounter + "PV\t: INT;\n" + "\t" + this.nameCounter + "Q\t: BOOL;\n" + "\t"
						+ this.nameCounter + "CV\t: INT;\n";

			} else if ("CTU".equals(type)) {
				var = "\t" + this.nameCounter + "CU\t: BOOL;\n" + "\t" + this.nameCounter + "RESET\t: BOOL;\n" + "\t"
						+ this.nameCounter + "PV\t: INT;\n" + "\t" + this.nameCounter + "Q\t: BOOL;\n" + "\t"
						+ this.nameCounter + "CV\t: INT;\n";

			} else if ("CTUD".equals(this.typeCounter)) {
				var = "\t" + this.nameCounter + "CU\t: BOOL;\n" + "\t" + this.nameCounter + "CD\t: BOOL;\n" + "\t"
						+ this.nameCounter + "RESET\t: BOOL;\n" + "\t" + this.nameCounter + "LOAD\t: BOOL;\n" + "\t"
						+ this.nameCounter + "PV\t: INT;\n" + "\t" + this.nameCounter + "QU\t: BOOL;\n" + "\t"
						+ this.nameCounter + "QD\t: BOOL;\n" + "\t" + this.nameCounter + "CV\t: INT;\n";
			}
		} else if (pTypeProgram.equals(GrafcetTagsConstants.PROGRAM_OPT3)) {// PLCOpen
			if ("CTD".equals(type)) {
				var = "<variable name=\"" + this.nameCounter + "CD\"><type><BOOL /></type></variable>"
						+ "<variable name=\"" + this.nameCounter + "LOAD\"><type><BOOL /></type></variable>"
						+ "<variable name=\"" + this.nameCounter + "PV\"><type><BOOL /></type></variable>"
						+ "<variable name=\"" + this.nameCounter + "Q\"><type><BOOL /></type></variable>"
						+ "<variable name=\"" + this.nameCounter + "CV\"><type><BOOL /></type></variable>";

			} else if ("CTU".equals(type)) {
				var = "<variable name=\"" + this.nameCounter + "CU\"><type><BOOL /></type></variable>"
						+ "<variable name=\"" + this.nameCounter + "RESET\"><type><BOOL /></type></variable>"
						+ "<variable name=\"" + this.nameCounter + "PV\"><type><BOOL /></type></variable>"
						+ "<variable name=\"" + this.nameCounter + "Q\"><type><BOOL /></type></variable>"
						+ "<variable name=\"" + this.nameCounter + "CV\"><type><BOOL /></type></variable>";

			} else if ("CTUD".equals(this.typeCounter)) {
				var = "<variable name=\"" + this.nameCounter + "CU\"><type><BOOL /></type></variable>"
						+ "<variable name=\"" + this.nameCounter + "CD\"><type><BOOL /></type></variable>"
						+ "<variable name=\"" + this.nameCounter + "RESET\"><type><BOOL /></type></variable>"
						+ "<variable name=\"" + this.nameCounter + "LOAD\"><type><BOOL /></type></variable>"
						+ "<variable name=\"" + this.nameCounter + "PV\"><type><BOOL /></type></variable>"
						+ "<variable name=\"" + this.nameCounter + "QU\"><type><BOOL /></type></variable>"
						+ "<variable name=\"" + this.nameCounter + "QD\"><type><BOOL /></type></variable>"
						+ "<variable name=\"" + this.nameCounter + "CV\"><type><BOOL /></type></variable>";
			}
		}

		return var;

	}

	/**
	 * Devuelve un array con dos elemetos en uno la iniciacion del tipo de count
	 * (var del program main) y el otro parte en la segunda parte del program
	 * main
	 */
	public String getProgramMainCounter() {
		String programMain = "";

		if (this.typeCounter.equals("CTD")) {

			// TODO contador inicializaciones preguntar q falta
			// programMain[0] = "\t" + this.nameCounter + "\t: " +
			// this.typeCounter + ";\n";
			programMain = this.nameCounter + "CD:= " + this.nameCounter + "CD," + " LOAD:= " + this.nameCounter
					+ "LOAD," + " PV:= " + this.nameCounter + "PV," + " Q=> " + this.nameCounter + "Q," + " CV=> "
					+ this.nameCounter + "CV);";

		} else if (this.typeCounter.equals("CTU")) {

			// programMain[0] = "\t" + this.nameCounter + "\t: " +
			// this.typeCounter + ";\n";
			programMain = this.nameCounter + "(CU:= " + this.nameCounter + "CU ," + " RESET:= " + this.nameCounter
					+ "RESET," + " PV:= " + this.nameCounter + "PV," + " Q=> " + this.nameCounter + "Q," + " CV=> "
					+ this.nameCounter + "CV);";

		} else if (this.typeCounter.equals("CTUD")) {

			// programMain[0] = "\t" + this.nameCounter + "\t: " +
			// this.typeCounter + ";\n";
			programMain = this.nameCounter + "(CU:= " + this.nameCounter + "CU ," + "CD:= " + this.nameCounter + "CD,"
					+ " RESET:= " + this.nameCounter + "RESET," + " LOAD:= " + this.nameCounter + "LOAD," + " PV:= "
					+ this.nameCounter + "PV," + " QU=> " + this.nameCounter + "QU," + " QD=> " + this.nameCounter
					+ "QD," + " CV=> " + this.nameCounter + "CV);";
		}

		return programMain;

	}

	public String getExternalVarsPLCOpen() {
		String externalVar = "";

		if (this.typeCounter.equals("CTD")) {
			externalVar = "<br /><variable name=\"" + this.nameCounter
					+ "CD\" group=\"Default\"><type><BOOL /></type></variable>" + "<br /><variable name=\""
					+ this.nameCounter + "LOAD\" group=\"Default\"><type><BOOL /></type></variable>"
					+ "<br /><variable name=\"" + this.nameCounter
					+ "PV\" group=\"Default\"><type><INT /></type></variable>" + "<br /><variable name=\""
					+ this.nameCounter + "Q\" group=\"Default\"><type><BOOL /></type></variable>"
					+ "<br /><variable name=\"" + this.nameCounter
					+ "CV\" group=\"Default\"><type><INT /></type></variable>";

		} else if (this.typeCounter.equals("CTU")) {
			externalVar = "<br /><variable name=\"" + this.nameCounter
					+ "CU\" group=\"Default\"><type><BOOL /></type></variable>" + "<br /><variable name=\""
					+ this.nameCounter + "RESET\" group=\"Default\"><type><BOOL /></type></variable>"
					+ "<br /><variable name=\"" + this.nameCounter
					+ "PV\" group=\"Default\"><type><INT /></type></variable>" + "<br /><variable name=\""
					+ this.nameCounter + "Q\" group=\"Default\"><type><BOOL /></type></variable>"
					+ "<br /><variable name=\"" + this.nameCounter
					+ "CV\" group=\"Default\"><type><INT /></type></variable>";

		} else if (this.typeCounter.equals("CTUD")) {
			externalVar = "<br /><variable name=\"" + this.nameCounter
					+ "CD\" group=\"Default\"><type><BOOL /></type></variable>" + "<br /><variable name=\""
					+ this.nameCounter + "CU\" group=\"Default\"><type><BOOL /></type></variable>"
					+ "<br /><variable name=\"" + this.nameCounter
					+ "RESET\" group=\"Default\"><type><BOOL /></type></variable>" + "<br /><variable name=\""
					+ this.nameCounter + "LOAD\" group=\"Default\"><type><BOOL /></type></variable>"
					+ "<br /><variable name=\"" + this.nameCounter
					+ "PV\" group=\"Default\"><type><INT /></type></variable>" + "<br /><variable name=\""
					+ this.nameCounter + "QU\" group=\"Default\"><type><BOOL /></type></variable>"
					+ "<br /><variable name=\"" + this.nameCounter
					+ "QD\" group=\"Default\"><type><BOOL /></type></variable>" + "<br /><variable name=\""
					+ this.nameCounter + "CV\" group=\"Default\"><type><INT /></type></variable>";

		}

		return externalVar;

	}

	/***/
	public String getBodyPLCOpen() {
		String body = "";

		// TODO contador inicializaciones preguntar q falta y q cada tipo es
		// distinto
		if (this.typeCounter.equals("CTD")) {

			body = "<br />" + this.nameCounter + "CD:= " + this.nameCounter + "CD," + " LOAD:= " + this.nameCounter
					+ "LOAD," + " PV:= " + this.nameCounter + "PV," + " Q=> " + this.nameCounter + "Q," + " CV=> "
					+ this.nameCounter + "CV);";

		} else if (this.typeCounter.equals("CTU")) {

			body = "<br />" + this.nameCounter + "(CU:= " + this.nameCounter + "CU ," + " RESET:= " + this.nameCounter
					+ "RESET," + " PV:= " + this.nameCounter + "PV," + " Q=> " + this.nameCounter + "Q," + " CV=> "
					+ this.nameCounter + "CV);";

		} else if (this.typeCounter.equals("CTUD")) {

			body = "<br />" + this.nameCounter + "(CU:= " + this.nameCounter + "CU ," + "CD:= " + this.nameCounter
					+ "CD," + " RESET:= " + this.nameCounter + "RESET," + " LOAD:= " + this.nameCounter + "LOAD,"
					+ " PV:= " + this.nameCounter + "PV," + " QU=> " + this.nameCounter + "QU," + " QD=> "
					+ this.nameCounter + "QD," + " CV=> " + this.nameCounter + "CV);";
		}

		return body;

	}

}
