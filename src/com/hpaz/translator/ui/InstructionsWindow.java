package com.hpaz.translator.ui;

import java.awt.BorderLayout;
import java.awt.Dialog.ModalExclusionType;
import java.awt.Dialog.ModalityType;
import java.awt.EventQueue;
import java.awt.Rectangle;
import java.awt.ScrollPane;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextPane;

public class InstructionsWindow extends JDialog {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					InstructionsWindow frame = new InstructionsWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/

	/**
	 * Create the frame.
	 */
	public InstructionsWindow() {
		setResizable(false);
		setTitle("Instrucciones de uso");
		setModal(true);
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel textPane = new JLabel();
		textPane.setBounds(10, 11, 424, 249);
		//extPane.setEditable(false);
		
		try {
			String currentLine;
			String allText = "";
			this.getClass().getCanonicalName();
			
			BufferedReader br = new BufferedReader(new FileReader(new File ("src/com/hpaz/translator/ui/files/instructions.txt")));
			
			while((currentLine = br.readLine()) != null){
				allText += currentLine + "<br>";
			}
			
			textPane.setText("<html>"+allText+"</html>");
			
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			textPane.setText("Ha ocurido un error al leer el fichero.");
		}
		
		JScrollPane scrollPanel = new JScrollPane(textPane);
		scrollPanel.setBounds(10, 11, 424, 249);
		contentPane.add(scrollPanel);
		
		JScrollBar vertical = scrollPanel.getVerticalScrollBar();
		vertical.setValue(vertical.getMinimum());
		
	}
}
