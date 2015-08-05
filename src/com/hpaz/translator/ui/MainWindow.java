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

public class MainWindow extends JFrame {

	private JPanel contentPane;
	private JTextField textFieldInput;
	private JTextField textFieldOutput;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow frame = new MainWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainWindow() {
		setResizable(false);
		setTitle("Traductor");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 626, 500);
		contentPane = new JPanel();
		contentPane.setToolTipText("sssss");
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		textFieldInput = new JTextField();
		textFieldInput.setColumns(256);

		JButton buttonInputSelect = new JButton("Examinar");
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
		lblNewLabel.setFont(new Font("Verdana", Font.BOLD, 18));

		JLabel label = new JLabel("Selecciona la carpeta donde se guardara la salida:");
		label.setFont(new Font("Verdana", Font.BOLD, 18));

		textFieldOutput = new JTextField();
		textFieldOutput.setColumns(256);

		JButton buttonOutputSelect = new JButton("Examinar");
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
		panelLanguage.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 1, true), " Lenguaje de salida ",
				TitledBorder.LEFT, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelLanguage.setToolTipText("");

		JPanel panelCompatibility = new JPanel();
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
		btnNewButton.setFont(new Font("Verdana", Font.PLAIN, 18));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String xmlPath = textFieldInput.getText();
				String outputPath = textFieldOutput.getText();
				String selectedCompatibility = choiceCompatibility.getSelectedItem();
				String language = "st";

				if (xmlPath.isEmpty() || outputPath.isEmpty() || selectedCompatibility.isEmpty()) {
					JOptionPane.showMessageDialog(contentPane,
						    "Todos los campos del formulario tienen que estar rellenos",
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
					//System.out.println("Todos los campos tienen que estar rellenos");
				} else {
					File inputXML = new File(xmlPath);
					File outputDir = new File(outputPath);

					if (!inputXML.exists()) {
						JOptionPane.showMessageDialog(contentPane,
							    "El XML de entrada no existe",
							    "Error",
							    JOptionPane.ERROR_MESSAGE);
						//System.out.println("El XML de entrada no existe");
					} else if (!outputDir.exists()) {
						JOptionPane.showMessageDialog(contentPane,
							    "El Directorio de salida no existe",
							    "Error",
							    JOptionPane.ERROR_MESSAGE);
						//System.out.println("El Directorio de salida no existe");
					} else {
						try {		
							// Creamos la factoria de parseadores por defecto
							XMLReader reader = XMLReaderFactory.createXMLReader();
							// Añadimos nuestro manejador al reader
							//reader.setContentHandler(Preprocess.getMyPreprocess().addVarsPreprocess(pNomPro, planguage, pCompatibility));
							reader.setContentHandler(new Preprocess(xmlPath, outputPath, language, selectedCompatibility));
							
							// Procesamos el xml
							//file.replace(";","");
							reader.parse(new InputSource(new FileInputStream(xmlPath)));
							
							//genero el fichero de salida
							//FormatoSalida.getSalida().exportarFichero();
							dispose();
							new ConfigWindow().setVisible(true);
							
						} catch (Exception ex) {
							ex.printStackTrace();
						}// TODO todo esta bien, llamar al algoritmo
					}
				}
			}
		});

		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING).addGroup(gl_contentPane
				.createSequentialGroup().addContainerGap(15, Short.MAX_VALUE)
				.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 595, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_contentPane.createSequentialGroup()
										.addComponent(textFieldInput, GroupLayout.PREFERRED_SIZE, 392,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(buttonInputSelect, GroupLayout.PREFERRED_SIZE, 89,
												GroupLayout.PREFERRED_SIZE))
								.addComponent(label, GroupLayout.PREFERRED_SIZE, 595, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_contentPane.createSequentialGroup()
										.addComponent(textFieldOutput, GroupLayout.PREFERRED_SIZE, 392,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(buttonOutputSelect, GroupLayout.PREFERRED_SIZE, 89,
												GroupLayout.PREFERRED_SIZE))
								.addGroup(Alignment.TRAILING,
										gl_contentPane.createSequentialGroup()
												.addComponent(panelLanguage, GroupLayout.PREFERRED_SIZE, 248,
														GroupLayout.PREFERRED_SIZE)
												.addGap(29)
												.addComponent(panelCompatibility, GroupLayout.PREFERRED_SIZE, 248,
														GroupLayout.PREFERRED_SIZE)
												.addGap(42)))
						.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
								.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 137, GroupLayout.PREFERRED_SIZE)
								.addGap(231)))));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPane
				.createSequentialGroup().addGap(20)
				.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE).addGap(13)
				.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup().addGap(1).addComponent(textFieldInput,
								GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
						.addComponent(buttonInputSelect, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE))
				.addGap(24).addComponent(label, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE).addGap(13)
				.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup().addGap(1).addComponent(textFieldOutput,
								GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
						.addComponent(buttonOutputSelect, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE))
				.addGap(18)
				.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(panelLanguage, GroupLayout.PREFERRED_SIZE, 173, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_contentPane.createSequentialGroup().addGap(30).addComponent(panelCompatibility,
								GroupLayout.PREFERRED_SIZE, 116, GroupLayout.PREFERRED_SIZE)))
				.addPreferredGap(ComponentPlacement.RELATED, 42, Short.MAX_VALUE)
				.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)
				.addContainerGap()));
		gl_contentPane.linkSize(SwingConstants.HORIZONTAL, new Component[] { panelLanguage, panelCompatibility });
		panelLanguage.setLayout(null);

		JLabel lblNewLabel_1 = new JLabel("Seleccione uno:");
		lblNewLabel_1.setBounds(25, 25, 193, 14);
		panelCompatibility.add(lblNewLabel_1);
		contentPane.setLayout(gl_contentPane);

	}
}
