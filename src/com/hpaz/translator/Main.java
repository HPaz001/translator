package com.hpaz.translator;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.GregorianCalendar;
import org.xml.sax.InputSource;  
import org.xml.sax.SAXException;  
import org.xml.sax.XMLReader;  
import org.xml.sax.helpers.XMLReaderFactory;
import com.hpaz.translator.preprocess.Preprocess;  


public class Main {

	private static String file; // ruta del file xml
	private static String language; // language al que transformar
	private static String compatibility;

	public static void main(String[] args) {
		GregorianCalendar cal = new GregorianCalendar();
		//Imprimo la fecha y hora actual al iniciar 
		System.out.println(cal.get(GregorianCalendar.HOUR) + ":"
				+ cal.get(GregorianCalendar.MINUTE) + ":"
				+ cal.get(GregorianCalendar.SECOND) + ":"
				+ cal.get(GregorianCalendar.MILLISECOND));
		
		//Compruebo si se han metido los argumentos correctos
		if (args.length < 3) {
			imprimirError();
		} else {  
			try {
				file = args[0];
				language= args[1];
				compatibility=args[2];				
				// Verifico que sean las opciones correctas	da igual si usa mayus o minus
				if ((language.equalsIgnoreCase("IL")||language.equalsIgnoreCase("ST")||
						language.equalsIgnoreCase("LD")||language.equalsIgnoreCase("FBD")||
						language.equalsIgnoreCase("SFC"))&&(compatibility.equalsIgnoreCase("T")||
						compatibility.equalsIgnoreCase("PL")|| compatibility.equalsIgnoreCase("PC"))/*&&
						(file.matches(regex))*/) {
					String name = file;
					name=name.substring(4, file.length()-4)+"_"+language;
					ejecutar(name, language, compatibility);
				} else{
					imprimirError();
				}
			} catch (NumberFormatException e) {
				imprimirError();
			} 
			
			//Imprimo la fecha y hora actual al finalizar
			cal = new GregorianCalendar();
			System.out.println(cal.get(GregorianCalendar.HOUR) + ":"
					+ cal.get(GregorianCalendar.MINUTE) + ":"
					+ cal.get(GregorianCalendar.SECOND) + ":"
					+ cal.get(GregorianCalendar.MILLISECOND));
		}
			
		

	}

	private static void imprimirError() {
		System.out.println("El programa fue invocado de manera incorrecta, intentalo de nuevo.");
		System.out.println("El programa debe de tener los siguientes parametros:");
		System.out.println("\t String = Fichero de grafcets con extencion xml.");
		System.out.println("\t String = language al que se quiere transformar:");
		System.out.println("\t IL --> Lista de instrucciones.");
		System.out.println("\t ST --> Texto estructurado.");
		System.out.println("\t LD --> Diagrama de escalera.");
		System.out.println("\t FBD --> Diagrama de bloques funcionales.");
		System.out.println("\t SFC --> Grafico de funciones secuenciales.");
		System.out.println("\t String = Software de compatibilidad:");
		System.out.println("\t T --> TwinCAT.");
		System.out.println("\t PL --> PL7PRO.");
		System.out.println("\t PC --> PCWorx.");
	}

	//Rellena la lista de grafcets q hay en proyecto
	private static void ejecutar(String pNomPro, String planguage, String pCompatibility) {
	
		try {		
			// Creamos la factoria de parseadores por defecto
			XMLReader reader = XMLReaderFactory.createXMLReader();
			// Añadimos nuestro manejador al reader
			//reader.setContentHandler(Preprocess.getMyPreprocess().addVarsPreprocess(pNomPro, planguage, pCompatibility));
			reader.setContentHandler(new Preprocess(pNomPro, planguage, pCompatibility));
			
			// Procesamos el xml
			file.replace(";","");
			reader.parse(new InputSource(new FileInputStream("xml/"+file)));
			
			//genero el fichero de salida
			//FormatoSalida.getSalida().exportarFichero();
			
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
