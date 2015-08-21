package com.hpaz.translator.ui;

import java.awt.Choice;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
import com.hpaz.translator.preprocess.Preprocess;

public class ConfigWindow extends JFrame {

	private JPanel contentPane;
	private JTextField textFieldInput;
	private JTextField textFieldOutput;

	/**
	 * Launch the application.
	 
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ConfigWindow frame = new ConfigWindow();
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
	public ConfigWindow() {
		setResizable(false);
		setTitle("Configuración del programa");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 626, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		textFieldInput = new JTextField();
		textFieldInput.setBounds(20, 64, 392, 25);
		textFieldInput.setColumns(256);

		JButton buttonInputSelect = new JButton("Examinar");
		buttonInputSelect.setBounds(412, 63, 89, 27);
		buttonInputSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter(".xml", "xml");
				fileChooser.setDialogTitle("Selecciona el fichero XML a tratar");
				fileChooser.setFileFilter(filter);
				int returnValue = fileChooser.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					textFieldInput.setText(selectedFile.getPath());
				}
			}
		});

		JLabel lblNewLabel = new JLabel("Selecciona el fichero XML que deseas traducir:");
		lblNewLabel.setBounds(20, 25, 595, 25);
		lblNewLabel.setFont(new Font("Verdana", Font.BOLD, 18));

		JLabel label = new JLabel("Selecciona la carpeta donde se guardara la salida:");
		label.setBounds(20, 114, 595, 25);
		label.setFont(new Font("Verdana", Font.BOLD, 18));

		textFieldOutput = new JTextField();
		textFieldOutput.setBounds(20, 153, 392, 25);
		textFieldOutput.setColumns(256);

		JButton buttonOutputSelect = new JButton("Examinar");
		buttonOutputSelect.setBounds(412, 152, 89, 27);
		buttonOutputSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Selecciona el directorio de salida");
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnValue = fileChooser.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					textFieldOutput.setText(selectedFile.getPath());
				}
			}
		});

		JPanel panelLanguage = new JPanel();
		panelLanguage.setBounds(48, 197, 248, 173);
		panelLanguage.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 1, true), " Lenguaje de salida ",
				TitledBorder.LEFT, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelLanguage.setToolTipText("");

		JPanel panelCompatibility = new JPanel();
		panelCompatibility.setBounds(325, 227, 248, 116);
		panelCompatibility.setToolTipText("");
		panelCompatibility.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 1, true),
				" Compatibilidad con ", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(0, 0, 0)));

		JRadioButton rdbtnLenguageST = new JRadioButton("Structured Text (ST)");
		rdbtnLenguageST.setEnabled(false);
		rdbtnLenguageST.setSelected(true);
		rdbtnLenguageST.setBounds(20, 26, 204, 23);
		panelLanguage.add(rdbtnLenguageST);

		JRadioButton rdbtnLenguageIL = new JRadioButton("Instruction List (IL)");
		rdbtnLenguageIL.setEnabled(false);
		rdbtnLenguageIL.setBounds(20, 52, 204, 23);
		panelLanguage.add(rdbtnLenguageIL);

		JRadioButton rdbtnLenguageSFC = new JRadioButton("Secuential Function Chart (SFC)");
		rdbtnLenguageSFC.setEnabled(false);
		rdbtnLenguageSFC.setBounds(20, 78, 204, 23);
		panelLanguage.add(rdbtnLenguageSFC);

		JRadioButton rdbtnLenguageLD = new JRadioButton("Ladder Diagram (LD)");
		rdbtnLenguageLD.setEnabled(false);
		rdbtnLenguageLD.setBounds(20, 104, 204, 23);
		panelLanguage.add(rdbtnLenguageLD);

		JRadioButton rdbtnLenguageFBD = new JRadioButton("Function Block Diagram (FBD)");
		rdbtnLenguageFBD.setEnabled(false);
		rdbtnLenguageFBD.setBounds(20, 130, 204, 23);
		panelLanguage.add(rdbtnLenguageFBD);
		panelCompatibility.setLayout(null);

		Choice choiceCompatibility = new Choice();
		choiceCompatibility.setBounds(22, 50, 203, 25);
		choiceCompatibility.add("");
		choiceCompatibility.add("PLC TSX Micro (PL7Pro)");
		choiceCompatibility.add("PLC Beckhoff (TwinCAT)");
		choiceCompatibility.add("PLCOpen");
		panelCompatibility.add(choiceCompatibility);

		JButton btnNewButton = new JButton("Iniciar");
		btnNewButton.setBounds(364, 409, 137, 43);
		btnNewButton.setFont(new Font("Verdana", Font.PLAIN, 18));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String xmlPath = textFieldInput.getText();
				String outputPath = textFieldOutput.getText();
				String selectedCompatibility = choiceCompatibility.getSelectedItem();
				String language = "st";
				//Si no estan rellenos todos los campos, mensaje de error
				if (xmlPath.isEmpty() || outputPath.isEmpty() || selectedCompatibility.isEmpty()) {
					JOptionPane.showMessageDialog(contentPane,
						    "Todos los campos del formulario tienen que estar rellenos",
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
					
				} else {// todos los campos rellenos
					File inputXML = new File(xmlPath);
					File outputDir = new File(outputPath);
					//compruebo el fichero de entrada
					if (!inputXML.exists()) {
						JOptionPane.showMessageDialog(contentPane,
							    "El XML de entrada no existe",
							    "Error",
							    JOptionPane.ERROR_MESSAGE);
						//compruebo el directorio de salida
					} else if (!outputDir.exists()) {
						JOptionPane.showMessageDialog(contentPane,
							    "El Directorio de salida no existe",
							    "Error",
							    JOptionPane.ERROR_MESSAGE);
					} else {
						//si todos los datos introducidos son correctos
						try {		
							// Creamos la factoria de parseadores por defecto
							XMLReader reader = XMLReaderFactory.createXMLReader();
							
							//reader.setContentHandler(Preprocess.getMyPreprocess().addVarsPreprocess(pNomPro, planguage, pCompatibility));
							reader.setContentHandler(new Preprocess(xmlPath, outputPath, language, selectedCompatibility));
							
							// Procesamos el xml
							reader.parse(new InputSource(new FileInputStream(xmlPath)));
							
							dispose();
							new VariableInitWindow().setVisible(true);
							
						} catch (Exception ex) {
							ex.printStackTrace();
						}// TODO todo esta bien, llamar al algoritmo
					}
				}
			}
		});
		panelLanguage.setLayout(null);

		JLabel lblNewLabel_1 = new JLabel("Seleccione uno:");
		lblNewLabel_1.setBounds(25, 25, 193, 14);
		panelCompatibility.add(lblNewLabel_1);
		contentPane.setLayout(null);
		contentPane.add(lblNewLabel);
		contentPane.add(textFieldInput);
		contentPane.add(buttonInputSelect);
		contentPane.add(label);
		contentPane.add(textFieldOutput);
		contentPane.add(buttonOutputSelect);
		contentPane.add(panelLanguage);
		contentPane.add(panelCompatibility);
		contentPane.add(btnNewButton);
		
		JButton btnVolverAlMenu = new JButton("Volver al Menu");
		btnVolverAlMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
				new MainProgramWindow().setVisible(true);
			}
		});
		btnVolverAlMenu.setFont(new Font("Verdana", Font.PLAIN, 18));
		btnVolverAlMenu.setBounds(103, 409, 179, 43);
		contentPane.add(btnVolverAlMenu);

	}
}
