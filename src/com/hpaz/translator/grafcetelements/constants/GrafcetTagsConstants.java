package com.hpaz.translator.grafcetelements.constants;

public class GrafcetTagsConstants {
	
	public static final String PROJECT_TAG = "project";
	public static final String GRAFCET_TAG = "grafcet";
	public static final String SEQUENCE_TAG = "sequence";
	public static final String STEP_TAG = "step";
	public static final String TRANSITION_TAG = "transition";
	public static final String ACTION_TAG = "action";
	public static final String TEXT_TAG = "text";
	public static final String CONDITION_TAG = "condition";
	public static final String RE_TAG = "re"; /**FLANCO ASCENDENTE*/
	public static final String FE_TAG = "fe";/**FLANCO DESCENDENTE*/
	public static final String CPL_TAG = "cpl";
	public static final String HLINK_TAG = "hlink";
	public static final String NODE_TAG = "node";
	public static final String JUMP_TAG = "jump";
	public static final String COMMENT_TAG = "comment";
	public static final String ACTION_FORCING_ORDER = "forcing order";
	public static final String ACTION_CONDITIONAL = "conditional";
	public static enum typeCounter
	{
		CTU,CTD,CTUD
	}
	
	//TODO Esto deberia estar en otra clase ya que no forma parte de elementos de grafcet
	public static final String PROGRAM_OPT1 = "PLC Beckhoff (TwinCAT)";
	public static final String PROGRAM_OPT2 = "PLC TSX Micro (PL7Pro)";
	public static final String PROGRAM_OPT3 = "PLCOpen";
	
}
