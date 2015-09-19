package com.hpaz.translator.grafcetelements;

import com.hpaz.translator.grafcetelements.constants.GrafcetTagsConstants;

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

	public String getGlobalsVarCounter(String pTypeProgram) {
		
		String var ="";
		
		if (pTypeProgram.equals(GrafcetTagsConstants.PROGRAM_OPT1)){//TwinCat
			if (this.typeCounter.equals("CTD")) {
				var = "\t" + this.nameCounter + "CD\t: BOOL;\n" + "\t" 
						+ this.nameCounter + "LOAD\t: BOOL;\n" + "\t"
						+ this.nameCounter + "PV\t: INT;\n" + "\t" 
						+ this.nameCounter + "Q\t: BOOL;\n" + "\t"
						+ this.nameCounter + "CV\t: INT;\n";

			} else if (this.typeCounter.equals("CTU")) {
				var = "\t" + this.nameCounter + "CU\t: BOOL;\n" + "\t" 
						+ this.nameCounter + "RESET\t: BOOL;\n" + "\t"
						+ this.nameCounter + "PV\t: INT;\n" + "\t" 
						+ this.nameCounter + "Q\t: BOOL;\n" + "\t"
						+ this.nameCounter + "CV\t: INT;\n";

			} else if (this.typeCounter.equals("CTUD")) {
				var = "\t" + this.nameCounter + "CU\t: BOOL;\n" + "\t" 
						+ this.nameCounter + "CD\t: BOOL;\n" + "\t"
						+ this.nameCounter + "RESET\t: BOOL;\n" + "\t" 
						+ this.nameCounter + "LOAD\t: BOOL;\n" + "\t"
						+ this.nameCounter + "PV\t: INT;\n" + "\t" 
						+ this.nameCounter + "QU\t: BOOL;\n" + "\t"
						+ this.nameCounter + "QD\t: BOOL;\n" + "\t" 
						+ this.nameCounter + "CV\t: INT;\n";
			}
		}else if (pTypeProgram.equals(GrafcetTagsConstants.PROGRAM_OPT3)) {//PLCOpen
			if (this.typeCounter.equals("CTD")) {
				var = "<variable name=\"" + this.nameCounter + "CD\"><type><BOOL /></type></variable>" 
						+"<variable name=\"" + this.nameCounter + "LOAD\"><type><BOOL /></type></variable>"
						+ "<variable name=\"" +this.nameCounter + "PV\"><type><BOOL /></type></variable>"
						+ "<variable name=\"" +this.nameCounter + "Q\"><type><BOOL /></type></variable>"
						+ "<variable name=\"" +this.nameCounter + "CV\"><type><BOOL /></type></variable>";

			} else if (this.typeCounter.equals("CTU")) {
				var = "<variable name=\"" + this.nameCounter + "CU\"><type><BOOL /></type></variable>"
						+"<variable name=\"" + this.nameCounter + "RESET\"><type><BOOL /></type></variable>"
						+"<variable name=\"" + this.nameCounter + "PV\"><type><BOOL /></type></variable>" 
						+"<variable name=\"" + this.nameCounter + "Q\"><type><BOOL /></type></variable>"
						+"<variable name=\"" + this.nameCounter + "CV\"><type><BOOL /></type></variable>";

			} else if (this.typeCounter.equals("CTUD")) {
				var = "<variable name=\"" + this.nameCounter + "CU\"><type><BOOL /></type></variable>" 
						+"<variable name=\"" + this.nameCounter + "CD\"><type><BOOL /></type></variable>"
						+"<variable name=\"" + this.nameCounter + "RESET\"><type><BOOL /></type></variable>"
						+"<variable name=\"" + this.nameCounter + "LOAD\"><type><BOOL /></type></variable>"
						+"<variable name=\"" + this.nameCounter + "PV\"><type><BOOL /></type></variable>"
						+"<variable name=\"" + this.nameCounter + "QU\"><type><BOOL /></type></variable>"
						+"<variable name=\"" + this.nameCounter + "QD\"><type><BOOL /></type></variable>"
						+"<variable name=\"" + this.nameCounter + "CV\"><type><BOOL /></type></variable>";
			}
		}
		
		return var;

	}

	/**
	 * Devuelve un array con dos elemetos en uno la iniciacion del tipo de count
	 * (var del program main) y en otro parte del program main
	 */
	public String getProgramMainCounter() {
		String programMain = "";

		if (this.typeCounter.equals("CTD")) {

			// TODO contador inicializaciones preguntar q falta
			//programMain[0] = "\t" + this.nameCounter + "\t: " + this.typeCounter + ";\n";
			programMain = this.nameCounter + "CD:= " + this.nameCounter + "CD," + " LOAD:= " + this.nameCounter
					+ "LOAD," + " PV:= " + this.nameCounter + "PV," + " Q=> " + this.nameCounter + "Q," + " CV=> "
					+ this.nameCounter + "CV);";

		} else if (this.typeCounter.equals("CTU")) {

			//programMain[0] = "\t" + this.nameCounter + "\t: " + this.typeCounter + ";\n";
			programMain = this.nameCounter + "(CU:= " + this.nameCounter + "CU ," + " RESET:= " + this.nameCounter
					+ "RESET," + " PV:= " + this.nameCounter + "PV," + " Q=> " + this.nameCounter + "Q," + " CV=> "
					+ this.nameCounter + "CV);";

		} else if (this.typeCounter.equals("CTUD")) {

			//programMain[0] = "\t" + this.nameCounter + "\t: " + this.typeCounter + ";\n";
			programMain = this.nameCounter + "(CU:= " + this.nameCounter + "CU ," + "CD:= " + this.nameCounter
					+ "CD," + " RESET:= " + this.nameCounter + "RESET," + " LOAD:= " + this.nameCounter + "LOAD,"
					+ " PV:= " + this.nameCounter + "PV," + " QU=> " + this.nameCounter + "QU," + " QD=> "
					+ this.nameCounter + "QD," + " CV=> " + this.nameCounter + "CV);";
		}

		return programMain;

	}
	
	public String getExternalVarsPLCOpen(){
		String externalVar = "";
		
		if (this.typeCounter.equals("CTD")) {
			externalVar = "<br /><variable name=\"" + this.nameCounter + "CD\" group=\"Default\"><type><BOOL /></type></variable>"
					+ "<br /><variable name=\"" + this.nameCounter + "LOAD\" group=\"Default\"><type><BOOL /></type></variable>"
					+ "<br /><variable name=\"" + this.nameCounter + "PV\" group=\"Default\"><type><INT /></type></variable>"
					+ "<br /><variable name=\"" + this.nameCounter + "Q\" group=\"Default\"><type><BOOL /></type></variable>"
					+ "<br /><variable name=\"" + this.nameCounter + "CV\" group=\"Default\"><type><INT /></type></variable>";

		} else if (this.typeCounter.equals("CTU")) {
			externalVar =  "<br /><variable name=\"" + this.nameCounter + "CU\" group=\"Default\"><type><BOOL /></type></variable>"
					+ "<br /><variable name=\"" + this.nameCounter + "RESET\" group=\"Default\"><type><BOOL /></type></variable>"
					+ "<br /><variable name=\"" + this.nameCounter + "PV\" group=\"Default\"><type><INT /></type></variable>"
					+ "<br /><variable name=\"" + this.nameCounter + "Q\" group=\"Default\"><type><BOOL /></type></variable>"
					+ "<br /><variable name=\"" + this.nameCounter + "CV\" group=\"Default\"><type><INT /></type></variable>";
	

		} else if (this.typeCounter.equals("CTUD")) {
			externalVar = "<br /><variable name=\"" + this.nameCounter + "CD\" group=\"Default\"><type><BOOL /></type></variable>"
					+ "<br /><variable name=\"" + this.nameCounter + "CU\" group=\"Default\"><type><BOOL /></type></variable>"
					+ "<br /><variable name=\"" + this.nameCounter + "RESET\" group=\"Default\"><type><BOOL /></type></variable>"
					+ "<br /><variable name=\"" + this.nameCounter + "LOAD\" group=\"Default\"><type><BOOL /></type></variable>"
					+ "<br /><variable name=\"" + this.nameCounter + "PV\" group=\"Default\"><type><INT /></type></variable>"
					+ "<br /><variable name=\"" + this.nameCounter + "QU\" group=\"Default\"><type><BOOL /></type></variable>"
					+ "<br /><variable name=\"" + this.nameCounter + "QD\" group=\"Default\"><type><BOOL /></type></variable>"
					+ "<br /><variable name=\"" + this.nameCounter + "CV\" group=\"Default\"><type><INT /></type></variable>";
	
		}
		
		
		return externalVar;
		
	}
	
	/***/
	public String getBodyPLCOpen(){
		String   body = "";
		
		// TODO contador inicializaciones preguntar q falta y q cada tipo es distinto
		if (this.typeCounter.equals("CTD")) {
			
			body = "<br />"+this.nameCounter + "CD:= " + this.nameCounter + "CD," + " LOAD:= " + this.nameCounter
					+ "LOAD," + " PV:= " + this.nameCounter + "PV," + " Q=> " + this.nameCounter + "Q," + " CV=> "
					+ this.nameCounter + "CV);";

		} else if (this.typeCounter.equals("CTU")) {

			body = "<br />"+this.nameCounter + "(CU:= " + this.nameCounter + "CU ," + " RESET:= " + this.nameCounter
					+ "RESET," + " PV:= " + this.nameCounter + "PV," + " Q=> " + this.nameCounter + "Q," + " CV=> "
					+ this.nameCounter + "CV);";

		} else if (this.typeCounter.equals("CTUD")) {

			body = "<br />"+this.nameCounter + "(CU:= " + this.nameCounter + "CU ," + "CD:= " + this.nameCounter
					+ "CD," + " RESET:= " + this.nameCounter + "RESET," + " LOAD:= " + this.nameCounter + "LOAD,"
					+ " PV:= " + this.nameCounter + "PV," + " QU=> " + this.nameCounter + "QU," + " QD=> "
					+ this.nameCounter + "QD," + " CV=> " + this.nameCounter + "CV);";
		}
			
		return body;
		
	}

}
