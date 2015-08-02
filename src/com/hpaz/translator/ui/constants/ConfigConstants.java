package com.hpaz.translator.ui.constants;

public class ConfigConstants {
	
	public static final String SIGNAL_DATA_TYPE_INPUT = "Entrada";
	public static final String SIGNAL_DATA_TYPE_OUTPUT = "Salida";
	public static final String SIGNAL_DATA_TYPE_MEMORY= "Memoria";
	public static final String SIGNAL_DATA_TYPE_CONSTANT = "Constante";
	public static final String SIGNAL_DATA_TYPE_SYSTEM = "Sistema";
	public static final String SIGNAL_DATA_TYPE_NOSIGNAL = "No es una señal";	

	
	/** Los objetos en cada zona pueden ser (definición mínima) :
	 bits (X);
	 bytes (B) - 8 bits;
	 words (W) -16 bits;
	 double words (D) - 32 bits.
	 word real con coma flotante (F) - 32 bits */
	public static final String SIGNAL_VARIABLE_TYPE_BOOLEAN = "Booleana";
	public static final String SIGNAL_VARIABLE_TYPE_BYTE = "Byte";
	public static final String SIGNAL_VARIABLE_TYPE_WORD = "Word";
	public static final String SIGNAL_VARIABLE_TYPE_DOUBLEWORD = "Double Word";	
	
	/**Retardo a la conexión*/
	public static final String SIGNAL_TEMP_TYPE_TON = "TON";
	/**Retardo a la desconexión*/
	public static final String SIGNAL_TEMP_TYPE_TOFF= "TOF";
	/**Impulso*/
	public static final String SIGNAL_TEMP_TYPE_TP = "TP";
	public static final String SIGNAL_TEMP_TYPE_NOTEMP = "No es un temporizador";
	
	/**Contador que decrementa*/
	public static final String SIGNAL_COUNTER_TYPE_CTD = "CTD";
	/**Contador que incrementa*/
	public static final String SIGNAL_COUNTER_TYPE_CTU = "CTU";
	/**Contador que incrementa y decrementa*/
	public static final String SIGNAL_COUNTER_TYPE_CTUD = "CTUD";
	public static final String SIGNAL_COUNTER_TYPE_NOCOUNTER = "No es un contador";
	
}
