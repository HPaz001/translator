package com.hpaz.translator.preprocess;

import java.nio.file.FileSystems;
import java.util.HashMap;
import java.util.Map;

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
	 */

	// creo las variables necesarias para guardar la informacion de etiquetas
	private Grafcet grafcet;
	private Jump jump;
	private Road road;
	private Sequence sequence;
	private Step step;
	private Transition transition;
	
	private Action action;
	private boolean isComment;
	// variables auxiliares
	/** isStep= True si lo que se esta procesando es una step */
	private boolean isStep;
	/** isTransition=True si lo que se esta procesasndo es una transition */
	private boolean isTransition;
	private String text;
	private String actualTag;
	private String previousTag;
	// private boolean CorrectlyXML;

	public Preprocess(String inputXML, String outputDir, String pLanguage, String pCompatibility) {
		this.grafcet=null;
		this.jump=null;
		this.road=null;
		this.sequence=null;
		this.step=null;
		this.transition=null;

		this.action=null;
		this.isComment = false;
		this.isStep = false;
		this.isTransition = false;
		
		this.text=null;
		this.actualTag = null;
		this.previousTag = null;
		
		// this.CorrectlyXML=true;
		String separator = FileSystems.getDefault().getSeparator();
		String fileName = inputXML.substring(inputXML.lastIndexOf(separator) + 1, inputXML.length() - 4);
		Project.getProject().addName(fileName + "_" + pLanguage);
		Project.getProject().addOutputPath(outputDir);
		Project.getProject().addLanguage(pLanguage);
		Project.getProject().addProgram(pCompatibility);
		
	}

	/**
	 * Detecta el principio del documento public void startDocument() throws
	 * SAXException {}
	 */

	/**
	 * Detecta el fin del documento public void endDocument() throws
	 * SAXException {}
	 */

	/** Esto lo hace por cada etiqueta que hay */
	public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
		previousTag = actualTag;
		actualTag = localName;
		Map<String, String> attributesAux;
		/* Instancio las variables segun van apareciendo su etiqueta */

		if (actualTag.equals(GrafcetTagsConstants.GRAFCET_TAG)) {
			grafcet = new Grafcet();
			attributesAux = processingAttributes(attributes);
			grafcet.fillAttributes(attributesAux);

		} else if (actualTag.equals(GrafcetTagsConstants.SEQUENCE_TAG)) {
			sequence = new Sequence();
			attributesAux = processingAttributes(attributes);
			sequence.fillAttributes(attributesAux);

		} else if (actualTag.equals(GrafcetTagsConstants.STEP_TAG)) {
			step = new Step();
			isStep = true;
			attributesAux = processingAttributes(attributes);
			step.fillAttributes(attributesAux);

		} else if (actualTag.equals(GrafcetTagsConstants.TRANSITION_TAG)) {
			transition = new Transition();
			isTransition = true;

		} else if (actualTag.equals(GrafcetTagsConstants.ACTION_TAG)) {
			action = new Action();
			attributesAux = processingAttributes(attributes);
			action.fillAttributes(attributesAux);
			
		} else if (actualTag.equals(GrafcetTagsConstants.HLINK_TAG)) {// road
			road = new Road();
			attributesAux = processingAttributes(attributes);
			road.fillAttributes(attributesAux);
			
		} else if (actualTag.equals(GrafcetTagsConstants.JUMP_TAG)) {
			jump = new Jump();
			attributesAux = processingAttributes(attributes);
			jump.fillAttributes(attributesAux);

		} else if (actualTag.equals(GrafcetTagsConstants.NODE_TAG)) { // node
			// guardo las dos secuencias de las q viene o va en el road
			road.addSequences(Integer.parseInt(attributes.getValue(0)));

		} else if (actualTag.equals(GrafcetTagsConstants.COMMENT_TAG)) {// comment
			isComment = true;
		} 
	}

	/** Para obtener los textos que hay dentro de una etiqueta */
	public void characters(char[] ch, int start, int length) throws SAXException {

		String texto = String.valueOf(ch, start, length);
	
		text = texto;
		// quito los enter, tabulaciones y espacios en blanco
		text = text.replace("\n", "");
		text = text.replace("\t", "");
		text = text.trim();

		// si la etiqueta tiene texto
		if (text != null && !text.isEmpty()) {
			// si la etiqueta es text entonces es el texto de una accion
			if (actualTag.equals(GrafcetTagsConstants.TEXT_TAG)) {
				// TODO este texto tambien puede venir negado o con flancos de
				// subida y bajada
				action.addText(text);

				/*
				 * Si la etiqueta es condition, puede ser de una transition o de
				 * un step ya que la acction puede ser condicionada
				 */
			} else if (actualTag.equals(GrafcetTagsConstants.CONDITION_TAG)) { // condition
				// si es un paso la condition sera de la accion
				if (isStep) {
					action.addCondition(text);
				} else if (isTransition) {
					transition.addCondition(text);
				}

				/*
				 * cpl etiqueta de negacion puede estar en un commentario, una
				 * accion o una transicion
				 */
			} else if (actualTag.equals(GrafcetTagsConstants.CPL_TAG)) {// cpl
				// si es de un comentario
				if (isComment) {
					addComent(" NOT " + text);
				} else if (isStep) {
					action.addCondition("( NOT (" + text + "))");
				} else if (isTransition) {
					transition.addCondition("( NOT (" + text + "))");
				}
			} else if (actualTag.equals(GrafcetTagsConstants.COMMENT_TAG)) {// comment
					addComent(text);
			}

				/*
				 * si la etiqueta es re (flanco de subida) Puede estar en :
				 * Action, transitio, comment
				 */
			 /*
				 * else if (actualTag.equals(GrafcetTagsConstants.RE_TAG)) { if
				 * (isComment) { // si esta dentro de un comentario a#ado a
				 * cometario addComent(" RE " + text); } else if (isStep) { //
				 * Si es un step puede ser de una condicion o de una action if
				 * (previousTag.equals(GrafcetTagsConstants.CONDITION_TAG)) {
				 * action.addCondition(" RE "); } else { action.addText(" RE ");
				 * } } else if (isTransition) { // si esta dentro de una
				 * transition transition.addCondition(" RE "); }
				 * 
				 * 
				 * si la etiqueta es fe (flanco de bajada) Puede estar en :
				 * Action, transitio, comment
				 * 
				 * } else if (actualTag.equals(GrafcetTagsConstants.FE_TAG)) {
				 * if (isComment) { // si esta dentro de un comentario añado a
				 * cometario addComent(" FE "); } else if (isStep) { // Si es un
				 * step puede ser de una condicion o de una action if
				 * (previousTag.equals(GrafcetTagsConstants.CONDITION_TAG)) {
				 * action.addCondition(" FE "); } else { action.addText(" FE ");
				 * } } else if (isTransition) { // si esta dentro de una
				 * transition transition.addCondition(" FE ");
				 * 
				 * } }
				 */
		}

	}

	/** Cuando detecta un fin de etiqueta */
	public void endElement(String uri, String localName, String name) throws SAXException {
		actualTag = localName;

		if (actualTag.equals(GrafcetTagsConstants.SEQUENCE_TAG)) {// Sequence
			// anado la secuencia al grafcet
			grafcet.addSequence(sequence);

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
			grafcet.fillPreviousAndNextSequencesLists();
			grafcet.addSetAndResetToStep();
			// si es el de emergencia genero las listas correspondientes
			if (grafcet.isEmergency()) {
				grafcet.getEmergency();
			}

			// genero asignaciones de accion := paso
			Map<String, String> auxMap = grafcet.getActionStepMap();
			Project.getProject().generateActionStepMap(auxMap);

			// añado el grafcet al proyecto
			Project.getProject().addGrafcet(grafcet);
			// si es una etiqueta de negacion
		} else if (actualTag.equals(GrafcetTagsConstants.CPL_TAG)) { // cpl
			/*
			 * vuelvo la actual tag a la antigua tag ya que si hay mas texto en
			 * una etiqueta detecta todo negado
			 */
			actualTag = previousTag;
			previousTag = "";
			/*
			 * si la etiqueta es re (flanco de subida) Puede estar en : Action,
			 * transitio, comment
			 */
		} else if (actualTag.equals(GrafcetTagsConstants.COMMENT_TAG)) { // comment
			isComment = false;
			/*
			 * si la etiqueta es re (flanco de subida) Puede estar en : Action,
			 * transitio, comment
			 */
		} else if (actualTag.equals(GrafcetTagsConstants.RE_TAG)) {
			if (isComment) {
				// si esta dentro de un comentario a#ado a cometario
				addComent(" RE ");
			} else if (isStep && !isComment) {
				// Si es un step puede ser de una condicion o de una action
				if (previousTag.equals(GrafcetTagsConstants.CONDITION_TAG)) {
					action.addCondition(" RE ");
				} else {
					action.addText(" RE ");
				}
			} else if (isTransition && !isStep && !isComment) {
				// si esta dentro de una transition
				transition.addCondition(" RE ");
			}
			/*
			 * si la etiqueta es fe (flanco de bajada) Puede estar en : Action,
			 * transitio, comment
			 */
		} else if (actualTag.equals(GrafcetTagsConstants.FE_TAG)) {
			if (isComment) {
				// si esta dentro de un comentario añado a cometario
				addComent(" FE ");
			} else if (isStep && !isComment) {
				// Si es un step puede ser de una condicion o de una action
				if (previousTag.equals(GrafcetTagsConstants.CONDITION_TAG)) {
					action.addCondition(" FE ");
				} else {
					action.addText(" FE ");
				}
			} else if (isTransition && !isStep && !isComment) {
				// si esta dentro de una transition
				transition.addCondition(" FE ");

			}
		}

	}

	private Map<String, String> processingAttributes(Attributes pAttributes) {
		Map<String, String> auxVar = new HashMap<String, String>();
		for (int i = 0; i < pAttributes.getLength(); i++) {
			auxVar.put(pAttributes.getQName(i), pAttributes.getValue(i));
		}
		return auxVar;
	}

	/**
	 * Añade el comentario donde le corresponde, ya que puede ser de una
	 * transicion o de un paso
	 */
	private void addComent(String comm) {
		/* Un comentario puede ser tanto de un paso como de una transicion */
		if (isStep) {
			// comentario de una accion que esta en step
			step.addComment(comm);

		} else if (isTransition) {
			// comentario de una transicion
			transition.addComment(comm);
		}
	}
}
