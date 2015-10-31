package com.hpaz.translator.grafcetelements;

import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Action {

	/** guarda el tipo de la accion */
	private String type;
	/** guarda el texto de la accion */
	private String text;
	/** Gurda l acondicion de la accion */
	private String condition;
	/** comentario de la accion */
	private String comment;
	/**
	 * Se guardara la variable de mensaje en caso de que la accion contenga un
	 * mensaje
	 */
	private String message;

	private LinkedList<String> stopEmergency;
	private LinkedList<String> startEmergency;
	private boolean emergency;

	public Action() {
		this.type = "";
		this.text = null;
		this.condition = null;
		this.comment = null;
		this.message = null;
		this.startEmergency = new LinkedList<String>();
		this.stopEmergency = new LinkedList<String>();
		this.emergency = false;
	}

	public void fillAttributes(Map<String, String> pAttributes) {
		// añado el tipo
		addType(pAttributes.get("type"));
	}

	public String getType() {
		return type;
	}

	public String getText() {
		return text;
	}

	public void addText(String pText) {
		// añado el texto de la accion me va llegando por partes
		if (getText() == null) {
			this.text = pText;
		} else {
			this.text = getText() + " " + pText;
		}
	}

	public String getCondition() {
		return condition;
	}

	public void addCondition(String pCondition) {
		// añado el texto de la condicion me va llegando por partes
		if (getCondition() == null) {
			this.condition = pCondition;
		} else {
			this.condition = getCondition() + " " + pCondition;
		}
	}

	public void addComment(String comment) {
		// añado el texto del comentario me va llegando por partes
		if (getComment() == null) {
			this.comment = comment;
		} else {
			this.comment = getComment() + " " + comment;
		}

	}

	public LinkedList<String> getStopEmergency() {
		return this.stopEmergency;
	}

	public LinkedList<String> getStartEmergency() {
		return this.startEmergency;
	}

	public boolean isEmergency() {
		return emergency;
	}

	/** Llama a la funcion de generado de listas de emergencia */
	public void getEmergency() {

		/* Uso expresiones regulares */
		/* Emergencia forzado a stop */
		String actionText = getText().trim();
		Pattern pat = Pattern.compile("^F/G.*.>\\{\\}$");
		Matcher mat = pat.matcher(actionText);
		if (mat.matches()) {
			addStopEmergency(generateListEmergency(actionText));
		} else {
			/* Emergencia forzado a start */
			Pattern pat1 = Pattern.compile("^F/G.*.>\\{X.[0-9]\\}$");
			Matcher mat1 = pat1.matcher(actionText);
			if (mat1.matches()) {
				addStartEmergency(generateListEmergency(actionText));
			}
		}

	}

	public void addEmergency(boolean b) {
		this.emergency = b;

	}

	public String getMessage() {
		return message;
	}

	public void addMessage(String message) {
		if (getMessage() == null) {
			this.message = message;
		}
	}

	/**
	 * Formatea el texto, la condicion y el comentario para que quite espacios,
	 * parentesis, ...
	 */
	public void formatAction() {
		String auxText = getText(), auxCondition = getCondition(), auxComment = getComment();
		if (auxText != null) {
			// quito los espacios y parentesis en caso de que los tenga
			this.text = auxText.replaceAll(" |\\(|\\)", "");
		}
		if (auxCondition != null) {
			// quito los espacios y parentesis en caso de que los tenga
			this.condition = auxCondition.replaceAll(" |\\(|\\)", "");
		}
		if (auxComment != null) {
			// quito los espacios, asteriscos y parentesis en caso de que los
			// tenga
			this.comment = auxComment.replaceAll(" |\\(|\\)|*", "");
		}
	}

	private void addType(String type) {
		this.type = type;
	}

	private void addStopEmergency(LinkedList<String> stopEmergency) {
		this.stopEmergency = stopEmergency;
	}

	private void addStartEmergency(LinkedList<String> startEmergency) {
		this.startEmergency = startEmergency;
	}

	private String getComment() {
		return comment;
	}

	private LinkedList<String> generateListEmergency(String pActionEmergency) {
		LinkedList<String> returnList = new LinkedList<String>();
		// Quito los espacios en blanco
		String auxText = pActionEmergency;
		// Me quedo solo con los nombres de los grafcets separados por ,
		auxText = auxText.replaceAll("F/G|>|\\{\\}|\\{X.[0-9]\\}|\\{X0\\}| ", "");
		// boolean aux=true;
		// int cont = 0;
		// separo por coma
		String[] auxLis = auxText.split(",");
		// por cada elemento de las lista
		for (int i = 0; i < auxLis.length; i++) {
			// guardo el nombre de un grafcet
			returnList.add(auxLis[i].trim());
		}
		// devuelvo la lista
		return returnList;
	}

}
