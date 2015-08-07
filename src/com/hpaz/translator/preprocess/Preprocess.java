package com.hpaz.translator.preprocess;

import java.nio.file.FileSystems;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.hpaz.translator.grafcetelements.Action;
import com.hpaz.translator.grafcetelements.Grafcet;
import com.hpaz.translator.grafcetelements.Jump;
import com.hpaz.translator.grafcetelements.Project;
import com.hpaz.translator.grafcetelements.Road;
import com.hpaz.translator.grafcetelements.Sequence;
import com.hpaz.translator.grafcetelements.Step;
import com.hpaz.translator.grafcetelements.Transition;
import com.hpaz.translator.grafcetelements.constants.GrafcetTagsConstants;

public class Preprocess extends DefaultHandler {
	/**
	 * Recoger informacion del XML
	 * 
	 * @author hpaz
	 *
	 */

	// creo las variables necesarias para guardar la informacion de etiquetas
	private Grafcet grafcet;
	private Jump jump;
	private Road road;
	private Sequence sequence;
	private Step step;
	private Transition transition;
	private String text;
	private Action action;

	// variables auxiliares
	private boolean isStep;// True si lo que se esta procesasndo es una step
	private boolean isTransition;// True si lo que se esta procesasndo es una
									// transition
	private String actualTag;
	private String previousTag;
	private String language;// el lenguaje al que se quiere pasar
	private String nameProject;
	private String compatibility;
	private int actualSequence;

	/* private static Preprocess myPreprocess; */

	public Preprocess(String inputXML, String outputDir, String pLanguage, String pCompatibility) {

		String separator = FileSystems.getDefault().getSeparator();
		String fileName = inputXML.substring(inputXML.lastIndexOf(separator) + 1, inputXML.length() - 4);

		language = pLanguage;
		actualTag = "";
		text = "";
		nameProject = fileName + "_" + pLanguage;
		isStep = false;
		isTransition = false;
		previousTag = "";
		compatibility = pCompatibility;
		this.actualSequence = 0;

		Project.getProject().setOutputPath(outputDir);
	}

	// si no existe el preproceso lo creo
	/*
	 * public static Preprocess getMyPreprocess(){ if(myPreprocess == null){
	 * myPreprocess = new Preprocess(); } return myPreprocess; }
	 */

	/*
	 * public void addVarsPreprocess(String pNamePro, String pLanguage, String
	 * pCompatibility) { language = pLanguage; actualTag = ""; text = "";
	 * nameProject = pNamePro; isStep = false; isTransition = false; previousTag
	 * = ""; compatibility=pCompatibility; }
	 */

	public void startDocument() throws SAXException {
		// System.out.println("\nPrincipio del documento...");
	}

	public void endDocument() throws SAXException {
		// System.out.println("\nFin del documento...");
	}

	/** Esto lo hace por cada etiqueta que hay */
	public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
		previousTag = actualTag;
		actualTag = localName;

		/* Instancio las variables segun van apareciendo su etiqueta */

		if (actualTag.equals(GrafcetTagsConstants.GRAFCET_TAG)) {
			grafcet = new Grafcet();

		} else if (actualTag.equals(GrafcetTagsConstants.SEQUENCE_TAG)) {
			sequence = new Sequence();

		} else if (actualTag.equals(GrafcetTagsConstants.STEP_TAG)) {
			step = new Step();
			isStep = true;

		} else if (actualTag.equals(GrafcetTagsConstants.TRANSITION_TAG)) {
			transition = new Transition();
			isTransition = true;

		} else if (actualTag.equals(GrafcetTagsConstants.ACTION_TAG)) {
			action = new Action();

		} else if (actualTag.equals(GrafcetTagsConstants.HLINK_TAG)) {// road
			road = new Road();

		} else if (actualTag.equals(GrafcetTagsConstants.JUMP_TAG)) {
			jump = new Jump();

		}

		// Recorremos los atributos de la etiqueta actual
		for (int i = 0; i < attributes.getLength(); i++) {
			// este metodo guarda cada atributo en su correspondiente clase
			processingAttributes(attributes.getQName(i), attributes.getValue(i));

		}
	}

	/** Para obtener los textos que hay dentro de una etiqueta */
	public void characters(char[] ch, int start, int length) throws SAXException {

		String texto = String.valueOf(ch, start, length);

		text = texto;
		// quito los enter, tambulaciones y espacios en blanco
		text = text.replace("\n", "");
		text = text.replace("\t", "");
		text = text.trim();

		// si la etiqueta tiene texto
		if (text != null && !text.isEmpty()) {

			// si la etiqueta es text entonces es el texto de una accion
			if (actualTag.equals(GrafcetTagsConstants.TEXT_TAG)) {
				// step.setAction(step.getAction() + text);
				action.setText(action.getText() + text);

				/*
				 * Si la etiqueta es condition, puede ser de una transition o de
				 * un step ya que la acction puede ser condicionada
				 */
			} else if (actualTag.equals(GrafcetTagsConstants.CONDITION_TAG)) { // condition

				// si es un paso la condition sera de la accion
				if (isStep) {
					/*
					 * añado la condicion completa, lo q habia + lo nuevo y
					 * dejo los signos ya los cambiare en la propia transition
					 */
					// step.setCondition(step.getCondition() + " " + text);
					action.setCondition(action.getCondition() + " " + text);

				} else if (isTransition) {
					transition.addCondition(text);
					/*
					 * añado la condicion completa, lo q habia + lo nuevo y
					 * dejo los signos ya los cambiare en la propia transition
					 * 
					 * transition.setConditionComp(transition.getConditionComp()
					 * + " " + text);
					 */
					/*
					 * la añado en la lista por separados para tener todas las
					 * señales por separado
					 * 
					 * if (text.contains("+") || text.contains("*")) {
					 * transition.addListConditionSep(removeSigns(text)); } else
					 * { transition.addListConditionSep(text); }
					 */
				}

			} else if (actualTag.equals(GrafcetTagsConstants.CPL_TAG)) {// cpl
				/*
				 * cpl etiqueta de negacion puede estar en un commentario, una
				 * accion o una transicion
				 */

				// si es de un comentario
				if (previousTag.equals(GrafcetTagsConstants.COMMENT_TAG)) {
					addComent(" NOT "+text);
					// si es un paso la condition sera de la accion
				} else if (isStep) {
					/*
					 * Si es de un step puede ser tanto de una condicion de la
					 * accion o de la propia accion
					 */
					// step.setCondition(step.getCondition() + "(NOT (" + text+
					// "))");
					action.setCondition(action.getCondition() + "(NOT (" + text + "))");

				} else if (isTransition) {
					/*
					 * añado la condicion completa, lo q habia + lo nuevo y
					 * dejo los signos ya los cambiare en la propia transition
					 */
					transition.addCondition("(NOT (" + text + "))");
				}

			} else if (actualTag.equals(GrafcetTagsConstants.COMMENT_TAG)) {// comment
				/*
				 * Un comentario puede ser tanto de un paso como de una
				 * transicion
				 */
				addComent(text);

			} else if (actualTag.equals(GrafcetTagsConstants.RE_TAG)) {
				/*
				 * si la etiqueta es re (flanco de subida) Puede estar en :
				 * Action, transitio, comment
				 */
				if (previousTag.equals(GrafcetTagsConstants.COMMENT_TAG)) {
					// si esta dentro de un comentario añado a cometario
					addComent(" RE "+text);

				} else if (isStep) {
					// Si es un step puede ser de una condicion o de una action
					if (previousTag.equals(GrafcetTagsConstants.CONDITION_TAG)) {
						action.setCondition(action.getCondition() + "RE " + text + "");
					} else {
						action.setText(action.getText() + " RE " + text + "");
					}

				} else if (isTransition) {
					// si esta dentro de una transition
					transition.addCondition(" (RE " + text + ")");
					// transition.addListConditionSep(text);
				}
			} else if (actualTag.equals(GrafcetTagsConstants.FE_TAG)) {
				/*
				 * si la etiqueta es fe (flanco de bajada) Puede estar en :
				 * Action, transitio, comment
				 */
				if (previousTag.equals(GrafcetTagsConstants.COMMENT_TAG)) {
					// si esta dentro de un comentario añado a cometario
					addComent(" FE "+text);

				} else if (isStep) {
					// Si es un step puede ser de una condicion o de una action
					if (previousTag.equals(GrafcetTagsConstants.CONDITION_TAG)) {
						action.setCondition(action.getCondition() + " FE " + text + "");
					} else {
						action.setText(action.getText() + " FE " + text + "");
					}

				} else if (isTransition) {
					// si esta dentro de una transition
					transition.addCondition("(FE " + text + ")");
				
				}
			}
		}

	}

	/** Cuando detecta un fin de etiqueta */
	public void endElement(String uri, String localName, String name) throws SAXException {
		actualTag = localName;
		// Si la etiqueta es sequence guardo el id de la etiqueta sequence
		// (secuencias)

		if (actualTag.equals(GrafcetTagsConstants.SEQUENCE_TAG)) {// Sequence
			// anado la secuencia al grafcet
			grafcet.addSequence(sequence, actualSequence);
			actualSequence = 0;

		} else if (actualTag.equals(GrafcetTagsConstants.ACTION_TAG)) { // Action
			// anado la accion al step
			step.addAction(action);

		} else if (actualTag.equals(GrafcetTagsConstants.STEP_TAG)) {// Step
			// anado el step a la secuencia
			sequence.addTransitionOrStep(step);
			isStep = false;

		} else if (actualTag.equals(GrafcetTagsConstants.TRANSITION_TAG)) { // Transition
			// anado la transicion a la secuencia
			sequence.addTransitionOrStep(transition);
			isTransition = false;

		} else if (actualTag.equals(GrafcetTagsConstants.HLINK_TAG)) { // hlink
			// añado el camino al grafcet
			grafcet.addRoad(road);

		} else if (actualTag.equals(GrafcetTagsConstants.JUMP_TAG)) { // jump
			// añado el salto al grafcet
			grafcet.addJump(jump);

		} else if (actualTag.equals(GrafcetTagsConstants.GRAFCET_TAG)) { // Grafcet
			// añado el grafcet al proyecto
			Project.getProject().addGrafcet(grafcet);

		} else if (actualTag.equals(GrafcetTagsConstants.PROJECT_TAG)) {// project
			// añado el lenguaje y el nombre del proyecto
			Project.getProject().addLanguage(language);
			Project.getProject().addName(nameProject);
			Project.getProject().addProgram(compatibility);
		}
	}

	/** Guarda cada atributo en su correspondiente clase */
	private void processingAttributes(String pNameAtt, String pAtt) {

		if (actualTag.equals(GrafcetTagsConstants.GRAFCET_TAG)) {// Grafcet
			// aNado el tipo, nombre, comentario y propietario
			if (pNameAtt.equals("type")) {
				grafcet.setType(pAtt);
			} else if (pNameAtt.equals("name")) {
				/* Para añadir expresiones regulares */
				Pattern pat = Pattern.compile("^GEmergencia.*|^GEmergency.*");
				Matcher mat = pat.matcher(pAtt);
				if (mat.matches()) {
					grafcet.setEmergency(true);
				}
				grafcet.setName(pAtt);
			} else if (pNameAtt.equals("comment")) {
				grafcet.setComment(pAtt);
			} else if (pNameAtt.equals("owner")) {
				grafcet.setOwner(pAtt);
			}

		} else if (actualTag.equals(GrafcetTagsConstants.SEQUENCE_TAG)) {// Sequence
			// añado el id
			actualSequence = Integer.parseInt(pAtt);
			sequence.setIdSeq(Integer.parseInt(pAtt));

		} else if (actualTag.equals(GrafcetTagsConstants.STEP_TAG)) {// Step
			// añado tipo y nombre
			if (pNameAtt.equals("type")) {
				step.setType(pAtt);
			} else if (pNameAtt.equals("name")) {
				step.setName(pAtt);
			}

		} else if (actualTag.equals(GrafcetTagsConstants.ACTION_TAG)) {// action
			// añado el tipo
			action.setType(pAtt);

		} else if (actualTag.equals(GrafcetTagsConstants.HLINK_TAG)) {// hlink
			// Si hay una convergencia o divergencia estara en la etiketa hlink,
			// guardaremos si es diver o conver
			// guardo en road el tipo, de q secuencia viene
			if (pNameAtt.equals("seqid")) {
				road.setSeqIni(Integer.parseInt(pAtt));
			} else if (pNameAtt.equals("type")) {
				road.setType(pAtt);
			}

		} else if (actualTag.equals(GrafcetTagsConstants.NODE_TAG)) { // node
			// guardo las dos secuencias de las q viene o va en el road
			road.addSequences(Integer.parseInt(pAtt));

		} else if (actualTag.equals(GrafcetTagsConstants.JUMP_TAG)) {// jump
			// añado desde y a donde
			if (pNameAtt.equals("seqid_from")) {
				jump.setFromSeq(Integer.parseInt(pAtt));
			} else if (pNameAtt.equals("seqid_to")) {
				jump.setToSeq(Integer.parseInt(pAtt));
			}
		}
	}

	/**
	 * Añade el comentario donde le corresponde, ya que puede ser de una
	 * transicion o de un paso
	 */
	private void addComent(String comm) {

		/* Un comentario puede ser tanto de un paso como de una transicion */
		if (isStep) {
			// comentario de una accion que esta en step
			step.setComment(step.getComment() + " " + comm);

		} else if (isTransition) {
			// comentario de una transicion
			transition.setComment(transition.getComment() + " " + comm);
		}
	}

}
