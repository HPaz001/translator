package com.hpaz.translator.preprocess;

import java.util.LinkedList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

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

	// variables auxiliares
	private boolean isStep;// True si lo que se esta procesasndo es una step
	private boolean isTransition;//True si lo que se esta procesasndo es una
									// transition
	private String actualTag;
	private String previousTag;
	private String language;// el lenguaje al que se quiere pasar
	private String nameProject;
	private String compatibility;

	/*private static Preprocess myPreprocess;*/
	
	public Preprocess(String pNamePro, String pLanguage, String pCompatibility) {
		language = pLanguage;
		actualTag = "";
		text = "";
		nameProject = pNamePro;
		isStep = false;
		isTransition = false;
		previousTag = "";
		compatibility=pCompatibility;
	}
	
	// si no existe el preproceso lo creo
/*	public static Preprocess getMyPreprocess(){
		if(myPreprocess == null){
			myPreprocess = new Preprocess();
		}
		return myPreprocess;
	}*/
	
	/*public void addVarsPreprocess(String pNamePro, String pLanguage, String pCompatibility) {
		language = pLanguage;
		actualTag = "";
		text = "";
		nameProject = pNamePro;
		isStep = false;
		isTransition = false;
		previousTag = "";
		compatibility=pCompatibility;
	}
*/
	public void startDocument() throws SAXException {
		System.out.println("\nPrincipio del documento...");

	}

	public void endDocument() throws SAXException {
		System.out.println("\nFin del documento...");
	}

	// Esto lo hace por cada etiqueta que hay
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
			// comment=false;
		} else if (actualTag.equals(GrafcetTagsConstants.RE_TAG)) {
			// si la etiqueta es re (flanco de subida)
			// transition.activateFs();
		} else if (actualTag.equals(GrafcetTagsConstants.FE_TAG)) {
			// si la etiqueta es fe (flanco  de bajada)
			/// transition.activateFb();
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

	// Para obtener los textos que hay dentro de una etiqueta
	public void characters(char[] ch, int start, int length) throws SAXException {

		String texto = String.valueOf(ch, start, length);

		text = texto;
		text = text.replace("\n", "");
		text = text.replace("\t", "");
		text = text.trim();

		if (text != null && !text.isEmpty()) {
			if (actualTag.equals(GrafcetTagsConstants.TEXT_TAG)) {
				step.setAction(step.getAction() + text);
			} else if (actualTag.equals(GrafcetTagsConstants.CONDITION_TAG)) { // condition
				// texto dentro de la condicion
				/*
				 * sustituyo el + y el * y añado la negacion en el caso de q
				 * exista
				 */
				text = changeSigns(text);
				transition.setConditionComp(transition.getConditionComp() + " " + text);
				if (text.contains("+") || text.contains("*")) {
					transition.addListConditionSep(removeSigns(text));
				} else {
					transition.addListConditionSep(text);
				}
			} else if (actualTag.equals(GrafcetTagsConstants.CPL_TAG)) {// cpl
				/*
				 * sustituyo el + y el * y añado la negacion en el caso de q
				 * exista
				 */
				text = changeSigns(text);
				// transition.setConditionComp(transition.getConditionComp()+"
				// "+text);
				text = transition.getConditionComp() + "(NOT (" + text + "))";
				transition.setConditionComp(text);
			} else if (actualTag.equals(GrafcetTagsConstants.COMMENT_TAG)) {// comment
				// si el comentario es de una accion o de una transicion
				if (previousTag.equals(GrafcetTagsConstants.CONDITION_TAG)) {
					// comentario de una transicion
					transition.setComment(transition.getComment() + " " + text);
				} else if (previousTag.equals(GrafcetTagsConstants.TEXT_TAG)) {
					// comentario de una accion que esta en step
					step.setComment(step.getComment() + " " + text);
				}
			} else if (actualTag.equals(GrafcetTagsConstants.RE_TAG)) {
				// si la etiqueta es re (flanco de subida)
				if (previousTag.equals(GrafcetTagsConstants.COMMENT_TAG)) {
					// debo saber si es de un step o transition
					if (isTransition) {
						transition.setComment(transition.getComment() + " " + text);
					} else if (isStep) {
						step.setComment(step.getComment() + " " + text);
					}
				} else {
					// transition.activateFs();
					transition.setConditionComp(transition.getConditionComp() + " (RE " + text + ")");
					transition.addListConditionSep(text);
				}
			} else if (actualTag.equals(GrafcetTagsConstants.FE_TAG)) {
				// si la etiqueta es fe (flanco de bajada)

				if (previousTag.equals(GrafcetTagsConstants.COMMENT_TAG)) {
					// debo saber si es de un step o transition
					if (isTransition) {
						transition.setComment(transition.getComment() + " " + text);
					} else if (isStep) {
						step.setComment(step.getComment() + " " + text);
					}
				} else {
					// transition.activateFb();
					transition.setConditionComp(transition.getConditionComp() + " (FE " + text + ")");
					transition.addListConditionSep(text);
				}
			}
		}

	}

	// Cuando detecta un fin de etiqueta
	public void endElement(String uri, String localName, String name) throws SAXException {
		actualTag = localName;
		// Si la etiqueta es sequence guardo el id de la etiqueta sequence
		// (secuencias)

		if (actualTag.equals(GrafcetTagsConstants.SEQUENCE_TAG)) {// Sequence
			// añado la secuencia al grafcet
			grafcet.addSeq(sequence);
		} else if (actualTag.equals(GrafcetTagsConstants.STEP_TAG)) {// Step
			// añado el step a la secuencia
			sequence.addTorS(step);
			isStep = false;
		} else if (actualTag.equals(GrafcetTagsConstants.TRANSITION_TAG)) { // Transition
			// añado la transicion a la secuencia
			sequence.addTorS(transition);
			isTransition = false;
		} else if (actualTag.equals(GrafcetTagsConstants.HLINK_TAG)) { // hlink
			// añado el camino al grafcet
			grafcet.addRoad(road);
		} else if (actualTag.equals(GrafcetTagsConstants.PROJECT_TAG)) {// project
			// añado el lenguaje y el nombre del proyecto
			Project.getProject().setLanguage(language);
			Project.getProject().setName(nameProject);
			try {
				Project.getProject().print(compatibility);
				//Output.getSalida().exportarFicheroVG(Project.getProject().printVarGlobal(), nameProject);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (actualTag.equals(GrafcetTagsConstants.GRAFCET_TAG)) { // Grafcet
			// añado el grafcet al proyecto
			Project.getProject().addGragcet(grafcet);
		}
	}
	
	/** este metodo guarda cada atributo en su correspondiente clase*/
	private void processingAttributes(String pNameAtt, String pAtt) {

		if (actualTag.equals(GrafcetTagsConstants.GRAFCET_TAG)) {// Grafcet
			// añado el tipo, nombre, comentario y propietario
			if (pNameAtt.equals("type")) {
				grafcet.setType(pAtt);
			} else if (pNameAtt.equals("name")) {
				grafcet.setName(pAtt);
			} else if (pNameAtt.equals("comment")) {
				grafcet.setComment(pAtt);
			} else if (pNameAtt.equals("owner")) {
				grafcet.setOwner(pAtt);
			}
		} else if (actualTag.equals(GrafcetTagsConstants.SEQUENCE_TAG)) {// Sequence
			// añado el id
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
			step.setTypeAction(pAtt);
		} else if (actualTag.equals(GrafcetTagsConstants.HLINK_TAG)) {// hlink
			// Si hay una convergencia o divergencia estara en la etiketa hlink,
			// guardaremos si es diver o conver
			// guardo en road el tipo, de q secuencia viene
			if (pNameAtt.equals("seqid")) {
				road.setSeqIni(Integer.parseInt(pAtt));
			} else if (pNameAtt.equals("type")) {
				road.setTypeRoad(pAtt);
			}
		} else if (actualTag.equals(GrafcetTagsConstants.NODE_TAG)) { // node
			// guardo las dos secuencias de las q viene o va en el road
			if (road.getSeqOne() == 0) {
				road.setSeqOne(Integer.parseInt(pAtt));
			} else if (road.getSeqTwo() == 0) {
				road.setSeqTwo(Integer.parseInt(pAtt));
			}
		} else if (actualTag.equals(GrafcetTagsConstants.JUMP_TAG)) {// jump
			// a�ado desde y adonde
			if (pNameAtt.equals("seqid_from")) {
				jump.setFromSeq(Integer.parseInt(pAtt));
			} else if (pNameAtt.equals("seqid_to")) {
				jump.setToSeq(Integer.parseInt(pAtt));
			}
		}
	}

	private String changeSigns(String text) {

		if (text.indexOf("+") != -1) {
			text = text.replace("+", " OR ");
		}
		if (text.indexOf("*") != -1) {
			text = text.replace("*", " AND ");
		}

		return text;

	}

	private LinkedList<String> removeSigns(String t) {

		LinkedList<String> aux = new LinkedList<String>();

		boolean itr = true;
		int posS = 0;
		text = t.trim();

		while (itr) {
			if (text.indexOf("+") == (-1) || text.indexOf("*") == (-1)) {
				itr = false;
				aux.add(text);
			} else {
				// si es un signo mas coloco en poss la posicion donde esta
				if (text.indexOf("+") != (-1)) {
					posS = text.indexOf("+");
					// si es un signo por coloco en poss la posicion donde esta
				} else if (text.indexOf("*") != (-1)) {
					posS = text.indexOf("*");
				}
				/*A�ado el texto desde la posicion 0 a la del signo ya q esta
				  no se incluye*/
				aux.add(text.substring(0, posS));
				/*Dejo en text el resto del string para seguirlo tratando*/
				text = text.substring(posS + 1, text.length() + 1);
			}
		}

		return aux;

	}

}
