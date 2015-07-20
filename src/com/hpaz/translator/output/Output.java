package com.hpaz.translator.output;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.FileSystems;
import java.util.LinkedList;


public class Output {

	private static String FILESYSTEM_SEPARATOR = FileSystems.getDefault().getSeparator();
	private static String OUTPUT_FOLDER = "output";
	private static String FILE_EXTENSION = ".txt";

	private static Output myOutput = new Output();

	private Output() {
	}
	public static Output getOutput() {
		return myOutput;
	}

	/**
	 * pNombreF contendra el nombre del fichero con las extencion
	 * correspondiente incluida
	 */
	public void exportarFichero(LinkedList<String> text, String name) throws Exception {

		// Inicializamos variables que necesitaremos
		File fichero = null;
		BufferedWriter pw = null;
		FileWriter fw = null;
		PrintWriter wr = null;// new PrintWriter(fw);
		// indico el nombre que tendra el fichero
		// (/*Proyecto.getProyecto().getNombreProyecto()+"_"+Proyecto.getProyecto().getLenguaje()*/)
		fichero = new File(OUTPUT_FOLDER + FILESYSTEM_SEPARATOR + name + FILE_EXTENSION);
		fichero.createNewFile();
		// if(Proyecto.getProyecto().getLenguaje().equals("ST")){

		try {
			fw = new FileWriter(fichero);
			pw = new BufferedWriter(fw);
			wr = new PrintWriter(pw);
			wr.write("");
			// Comenzamos a escribi en el fichero
			for (String s : text) {
				wr.append(s);// a�ado lineas al fichero
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		// cierro
		wr.close();
		pw.close();
		fw.close();
	}

}
