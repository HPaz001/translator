package com.hpaz.translator.output;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.FileSystems;
import java.util.LinkedList;


public class Output {

	private static String FILESYSTEM_SEPARATOR = FileSystems.getDefault().getSeparator();
	private static String FILE_EXTENSION = ".txt";

	private static Output myOutput = new Output();

	private Output() {
	}
	public static Output getOutput() {
		return myOutput;
	}

	/**
	 * text : lista de String.
	 * name : nombre del fichero sin las extension.
	 */
	public void exportFile(LinkedList<String> text, String name, String outputFolder) throws Exception {

		// Inicializamos variables que necesitaremos
		File fichero = null;
		BufferedWriter pw = null;
		FileWriter fw = null;
		PrintWriter wr = null;// new PrintWriter(fw);
		// indico el nombre que tendra el fichero
		fichero = new File(outputFolder + FILESYSTEM_SEPARATOR + name + FILE_EXTENSION);
		fichero.createNewFile();

		try {
			fw = new FileWriter(fichero);
			pw = new BufferedWriter(fw);
			wr = new PrintWriter(pw);
			wr.write("");
			
			// Comenzamos a escribi en el fichero
			for (String s : text) {
				wr.append(s);// anado lineas al fichero
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
