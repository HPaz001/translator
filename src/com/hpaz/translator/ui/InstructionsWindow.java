package com.hpaz.translator.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

public class InstructionsWindow extends JDialog {

	private JPanel contentPane;

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
			
			InputStream in = this.getClass().getResourceAsStream("files/instructions.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			while((currentLine = br.readLine()) != null){
				allText += currentLine + "<br>";
			}
			
			textPane.setText("<html>"+allText+"</html>");
			
		}catch (IOException e) {
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
